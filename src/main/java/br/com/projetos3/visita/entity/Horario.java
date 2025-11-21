package br.com.projetos3.visita.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDateTime;

@Entity
public class Horario {

    @Id
    private Long id = 1L; // Singleton

    @NotBlank(message = "O título dos dias de semana é obrigatório")
    private String tituloSemana;

    @NotBlank(message = "O horário da semana é obrigatório")
    // Validação Regex: Garante que exista pelo menos um padrão de hora (ex: 09:00) no texto
    @Pattern(regexp = ".*\\d{2}:\\d{2}.*", message = "Formato de horário inválido. Use HH:mm (ex: 09:00)")
    private String horarioSemana;

    @NotBlank(message = "O título do fim de semana é obrigatório")
    private String tituloFimDeSemana;

    @NotBlank(message = "O horário do fim de semana é obrigatório")
    @Pattern(regexp = ".*\\d{2}:\\d{2}.*", message = "Formato de horário inválido. Use HH:mm (ex: 09:00)")
    private String horarioFimDeSemana;

    private LocalDateTime atualizadoEm;

    public Horario() {
        this.id = 1L;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = 1L; }

    public String getTituloSemana() { return tituloSemana; }
    public void setTituloSemana(String tituloSemana) { this.tituloSemana = tituloSemana; }

    public String getHorarioSemana() { return horarioSemana; }
    public void setHorarioSemana(String horarioSemana) { this.horarioSemana = horarioSemana; }

    public String getTituloFimDeSemana() { return tituloFimDeSemana; }
    public void setTituloFimDeSemana(String tituloFimDeSemana) { this.tituloFimDeSemana = tituloFimDeSemana; }

    public String getHorarioFimDeSemana() { return horarioFimDeSemana; }
    public void setHorarioFimDeSemana(String horarioFimDeSemana) { this.horarioFimDeSemana = horarioFimDeSemana; }

    public LocalDateTime getAtualizadoEm() { return atualizadoEm; }
    public void setAtualizadoEm(LocalDateTime atualizadoEm) { this.atualizadoEm = atualizadoEm; }
}