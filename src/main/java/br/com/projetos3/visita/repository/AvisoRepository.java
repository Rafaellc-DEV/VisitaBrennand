package br.com.projetos3.visita.repository;

import br.com.projetos3.visita.entity.Aviso;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface AvisoRepository extends JpaRepository<Aviso, Long> {
    List<Aviso> findByAtivoTrueOrderByDataDesc();
    List<Aviso> findByDataAndAtivoTrue(LocalDate data);

    // MÉTODO : Busca avisos ativos dentro de um período
    List<Aviso> findByAtivoTrueAndDataBetween(LocalDate start, LocalDate end);
}