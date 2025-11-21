package br.com.projetos3.visita.service;

import br.com.projetos3.visita.entity.Horario;
import br.com.projetos3.visita.repository.HorarioRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class HorarioService {

    private final HorarioRepository repo;
    private static final Long ID_FIXO = 1L;

    public HorarioService(HorarioRepository repo) {
        this.repo = repo;
    }

    public Horario getHorario() {
        return repo.findById(ID_FIXO).orElseGet(() -> {
            Horario novo = new Horario();
            novo.setTituloSemana("Terça a Sexta-feira");
            novo.setHorarioSemana("09:00 às 16:00");
            novo.setTituloFimDeSemana("Sábado e Domingo");
            novo.setHorarioFimDeSemana("08:00 às 16:00");
            novo.setAtualizadoEm(LocalDateTime.now());
            return repo.save(novo);
        });
    }

    public Horario salvar(Horario horario) {
        horario.setId(ID_FIXO);
        horario.setAtualizadoEm(LocalDateTime.now());
        return repo.save(horario);
    }
}