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

            // Valores padrão iniciais
            novo.setBarcoDescricao("Saída do Marco Zero (Recife Antigo).");
            novo.setBarcoPreco("R$ 10,00 (ida e volta)");
            novo.setBarcoTempo("~5 minutos");
            novo.setBarcoBike("Aceita bicicleta (+R$ 5)");

            novo.setCarroDescricao("Acesso por Brasília Teimosa.");
            novo.setCarroPasso1("Av. Brasília Formosa até o fim");
            novo.setCarroPasso2("Entre na rua do Mole do Porto");
            novo.setCarroPasso3("Estacionamento no local");

            novo.setCatamaraDescricao("Passeio turístico completo pelo rio.");
            novo.setCatamaraLink("https://www.catamarantours.com.br");

            novo.setAtualizadoEm(LocalDateTime.now());
            return repo.save(novo);
        });
    }

    public ComoChegar salvar(ComoChegar comoChegar) {
        comoChegar.setId(ID_FIXO);
        comoChegar.setAtualizadoEm(LocalDateTime.now());
        return repo.save(comoChegar);
    }
}