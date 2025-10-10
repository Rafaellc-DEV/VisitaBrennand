package br.com.projetos3.visita.service;

import br.com.projetos3.visita.entity.Aviso;
import br.com.projetos3.visita.repository.AvisoRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AvisoService {
    private final AvisoRepository repo;
    public AvisoService(AvisoRepository repo){ this.repo = repo; }

    public List<Aviso> vigentes(){ return repo.findByAtivoTrueOrderByDataDesc(); }
}
