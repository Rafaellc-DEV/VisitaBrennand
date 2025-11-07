package com.exemplo.visitabrennand.repository;

import com.exemplo.visitabrennand.model.Aviso;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AvisoRepository extends JpaRepository<Aviso, Long> {
}
