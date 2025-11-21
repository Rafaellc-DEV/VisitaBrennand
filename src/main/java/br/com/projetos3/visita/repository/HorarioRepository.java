package br.com.projetos3.visita.repository;

import br.com.projetos3.visita.entity.Horario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HorarioRepository extends JpaRepository<Horario, Long> {
}