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

    /**
     * Carrega a regra de ID 1. Se não existir, cria uma nova em memória.
     */
    public Regra getRegra() {
        return repo.findById(REGRA_ID).orElseGet(() -> {
            Regra novaRegra = new Regra();
            novaRegra.setTitulo("Regras de Visitação");
            novaRegra.setConteudo("<p>Edite as regras de visitação aqui...</p>");
            novaRegra.setAtualizadoEm(LocalDateTime.now());
            return novaRegra;
        });
    }

    /**
     * Salva a regra.
     * AVISO: Esta versão confia que o HTML foi sanitizado
     * no frontend (via DOMPurify) antes de ser enviado.
     */
    public Regra salvar(Regra regra) {

        // 1. Definir os dados de controle
        regra.setId(REGRA_ID); // Força o ID = 1
        regra.setAtualizadoEm(LocalDateTime.now());

        // 2. Salvar o HTML (que veio pré-sanitizado do frontend)
        return repo.save(regra);
    }
}