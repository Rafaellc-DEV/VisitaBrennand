package br.com.projetos3.visita.repository;

import br.com.projetos3.visita.entity.Regra;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegraRepository extends JpaRepository<Regra, Long> {
}