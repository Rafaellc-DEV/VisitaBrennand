package com.exemplo.visitabrennand.service;

import com.exemplo.visitabrennand.model.Aviso;
import com.exemplo.visitabrennand.repository.AvisoRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AvisoService {
    private final AvisoRepository avisoRepository;

    public AvisoService(AvisoRepository avisoRepository) {
        this.avisoRepository = avisoRepository;
    }

    public Aviso salvar(Aviso aviso) {
        return avisoRepository.save(aviso);
    }

    public List<Aviso> listar() {
        return avisoRepository.findAll();
    }
}
