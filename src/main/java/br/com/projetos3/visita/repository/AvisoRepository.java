package br.com.projetos3.visita.repository;

import br.com.projetos3.visita.entity.Aviso;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AvisoRepository extends JpaRepository<Aviso, Long> {
    List<Aviso> findByAtivoTrueOrderByDataDesc();
}
