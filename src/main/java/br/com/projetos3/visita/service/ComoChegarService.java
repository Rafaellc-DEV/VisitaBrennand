package br.com.projetos3.visita.service;

import br.com.projetos3.visita.entity.ComoChegar;
import br.com.projetos3.visita.repository.ComoChegarRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class ComoChegarService {

    private final ComoChegarRepository repo;
    private static final Long ID_FIXO = 1L;

    public ComoChegarService(ComoChegarRepository repo) {
        this.repo = repo;
    }

    public ComoChegar getComoChegar() {
        return repo.findById(ID_FIXO).orElseGet(() -> {
            ComoChegar novo = new ComoChegar();

            // Conteúdo padrão HTML com informações úteis
            String conteudoPadrao = """
                <p>O Parque de Esculturas Francisco Brennand está localizado no recife de proteção do porto, em frente ao Marco Zero. Veja as opções de acesso:</p>
                
                <h3>🛥️ Travessia de Barco (Marco Zero)</h3>
                <p>Pequenas embarcações realizam a travessia saindo do Marco Zero (Recife Antigo).</p>
                <ul>
                    <li><strong>Valor médio:</strong> R$ 10,00 (ida e volta) por pessoa.</li>
                    <li><strong>Bicicletas:</strong> Alguns barqueiros cobram taxa extra (aprox. R$ 5,00) para levar bicicletas.</li>
                    <li><strong>Tempo:</strong> A travessia leva cerca de 5 minutos.</li>
                </ul>

                <h3>🚗 De Carro ou Uber</h3>
                <p>O acesso terrestre é feito pelo bairro de Brasília Teimosa.</p>
                <ol>
                    <li>Siga pela Av. Brasília Formosa até o final.</li>
                    <li>Entre na rua que dá acesso ao Mole do Porto.</li>
                    <li>Há estacionamento limitado próximo à entrada do parque.</li>
                </ol>

                <h3>🛳️ Catamarã Tours</h3>
                <p>A empresa Catamarã Tours oferece passeios que contemplam o parque. Consulte o site oficial para horários e valores atualizados.</p>
            """;

            novo.setConteudo(conteudoPadrao);
            novo.setAtualizadoEm(LocalDateTime.now());

            // Salva no banco para persistir esse padrão
            return repo.save(novo);
        });
    }

    public ComoChegar salvar(ComoChegar comoChegar) {
        comoChegar.setId(ID_FIXO);
        comoChegar.setAtualizadoEm(LocalDateTime.now());
        return repo.save(comoChegar);
    }
}