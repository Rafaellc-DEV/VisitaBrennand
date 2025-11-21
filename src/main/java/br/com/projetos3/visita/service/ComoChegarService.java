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

            // AQUI: O HTML NOVO (CARDS) VAI DENTRO DO JAVA
            String conteudoPadrao = """
                <div class="transport-grid">
                    <div class="transport-card">
                        <div class="transport-icon-box"><i class="bi bi-boat"></i></div>
                        <h6 class="transport-title">Travessia de Barco</h6>
                        <p class="transport-desc">Saída do Marco Zero (Recife Antigo).</p>
                        <ul class="transport-details">
                            <li><i class="bi bi-cash-coin"></i> <span>R$ 10,00 (ida e volta)</span></li>
                            <li><i class="bi bi-clock"></i> <span>~5 minutos</span></li>
                            <li><i class="bi bi-bicycle"></i> <span>Aceita bicicleta (+R$ 5)</span></li>
                        </ul>
                    </div>

                    <div class="transport-card">
                        <div class="transport-icon-box"><i class="bi bi-car-front"></i></div>
                        <h6 class="transport-title">Carro ou Uber</h6>
                        <p class="transport-desc">Acesso por Brasília Teimosa.</p>
                        <div class="transport-steps">
                            <div class="step"><span class="num">1</span> Av. Brasília Formosa até o fim</div>
                            <div class="step"><span class="num">2</span> Entre na rua do Mole do Porto</div>
                            <div class="step"><span class="num">3</span> Estacionamento no local</div>
                        </div>
                    </div>

                    <div class="transport-card">
                        <div class="transport-icon-box"><i class="bi bi-ticket-perforated"></i></div>
                        <h6 class="transport-title">Catamarã Tours</h6>
                        <p class="transport-desc">Passeio turístico completo pelo rio.</p>
                        <div style="margin-top: auto;">
                            <a href="https://www.catamarantours.com.br" target="_blank" class="btn btn-outline-brand w-100 btn-sm">
                                Ver Site Oficial <i class="bi bi-box-arrow-up-right ms-1"></i>
                            </a>
                        </div>
                    </div>
                </div>
            """;

            novo.setConteudo(conteudoPadrao);
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