package br.com.projetos3.visita.service;

import br.com.projetos3.visita.entity.Regra;
import br.com.projetos3.visita.repository.RegraRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class RegraService {

    private final RegraRepository repo;
    private static final Long REGRA_ID = 1L;

    public RegraService(RegraRepository repo) {
        this.repo = repo;
    }

    public Regra getRegra() {
        // Tenta buscar no banco. Se não existir, cria um padrão com as regras clássicas.
        return repo.findById(REGRA_ID).orElseGet(() -> {
            Regra novaRegra = new Regra();
            novaRegra.setTitulo("Regras de Visitação");

            // Define o conteúdo padrão em HTML (lista de regras)
            String conteudoPadrao = """
                <ul>
                    <li>Crianças devem estar acompanhadas dos responsáveis.</li>
                    <li>Jogue lixo nas lixeiras e mantenha o parque limpo.</li>
                    <li>Proibido fumar nas áreas de circulação.</li>
                    <li><strong>Proibido subir drone</strong> (exceto com autorização prévia).</li>
                    <li>Não toque nas obras de arte.</li>
                </ul>
            """;

            novaRegra.setConteudo(conteudoPadrao);
            novaRegra.setAtualizadoEm(LocalDateTime.now());

            // Salva no banco para que, da próxima vez, carregue do banco
            return repo.save(novaRegra);
        });
    }

    public Regra salvar(Regra regra) {
        regra.setId(REGRA_ID); // Garante que sempre atualiza o registro ID=1
        regra.setAtualizadoEm(LocalDateTime.now());
        return repo.save(regra);
    }
}