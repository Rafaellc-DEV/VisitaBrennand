package br.com.projetos3.visita.repository;

import br.com.projetos3.visita.entity.Aviso;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface AvisoRepository extends JpaRepository<Aviso, Long> {

    List<Aviso> findByAtivoTrueOrderByDataDesc();

    // CORREÇÃO 1: Removido "AndAtivoTrue"
    List<Aviso> findByData(LocalDate data);

    // CORREÇÃO 2: Removido "AtivoTrueAnd"
    List<Aviso> findByDataBetween(LocalDate start, LocalDate end);
}