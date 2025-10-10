package br.com.projetos3.visita.service;

import br.com.projetos3.visita.entity.Sugestao;
import br.com.projetos3.visita.repository.SugestaoRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

@Service
public class SugestaoService {
    private final SugestaoRepository repo;
    public SugestaoService(SugestaoRepository repo){ this.repo = repo; }

    public Sugestao salvar(@Valid Sugestao s){
        // Bean Validation garante que texto não esteja vazio
        return repo.save(s);
    }
}
