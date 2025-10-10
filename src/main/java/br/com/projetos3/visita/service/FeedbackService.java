package br.com.projetos3.visita.service;

import br.com.projetos3.visita.entity.Feedback;
import br.com.projetos3.visita.repository.FeedbackRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

@Service
public class FeedbackService {
    private final FeedbackRepository repo;
    public FeedbackService(FeedbackRepository repo){ this.repo = repo; }

    public Feedback salvar(@Valid Feedback f){
        // Bean Validation já cobre: nota 1..5 e comentário obrigatório
        return repo.save(f);
    }
}
