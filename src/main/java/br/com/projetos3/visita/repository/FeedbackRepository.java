package br.com.projetos3.visita.repository;

import br.com.projetos3.visita.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {}
