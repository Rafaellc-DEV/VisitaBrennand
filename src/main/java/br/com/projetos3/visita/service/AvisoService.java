package br.com.projetos3.visita.service;

import br.com.projetos3.visita.entity.Aviso;
import br.com.projetos3.visita.repository.AvisoRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Set; // IMPORTAR
import java.util.stream.Collectors; // IMPORTAR

@Service
public class AvisoService {
    private final AvisoRepository repo;
    public AvisoService(AvisoRepository repo){ this.repo = repo; }

    public List<Aviso> vigentes(){ return repo.findByAtivoTrueOrderByDataDesc(); }
    public List<Aviso> porData(LocalDate data) { return repo.findByDataAndAtivoTrue(data); }

    // MÉTODO : Retorna um Set de datas que possuem avisos ativos no período
    public Set<LocalDate> findDatesWithAvisos(LocalDate start, LocalDate end) {
        return repo.findByAtivoTrueAndDataBetween(start, end)
                .stream()
                .map(Aviso::getData) // Extrai apenas a data
                .collect(Collectors.toSet());
    }
}