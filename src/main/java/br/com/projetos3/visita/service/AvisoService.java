package br.com.projetos3.visita.service;

import br.com.projetos3.visita.entity.Aviso;
import br.com.projetos3.visita.repository.AvisoRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AvisoService {
    private final AvisoRepository repo;
    public AvisoService(AvisoRepository repo){ this.repo = repo; }

    public List<Aviso> vigentes(){ return repo.findByAtivoTrueOrderByDataDesc(); }

    // CORREÇÃO 1: Chamando repo.findByData (sem "AndAtivoTrue")
    public List<Aviso> porData(LocalDate data) {
        return repo.findByData(data);
    }

    // CORREÇÃO 2: Chamando repo.findByDataBetween (sem "AtivoTrueAnd")
    public Set<LocalDate> findDatesWithAvisos(LocalDate start, LocalDate end) {
        return repo.findByDataBetween(start, end) // <-- MUDANÇA AQUI
                .stream()
                .map(Aviso::getData) // Extrai apenas a data
                .collect(Collectors.toSet());
    }
}