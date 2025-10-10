package br.com.projetos3.visita.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Entity
public class Feedback {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull @Min(1) @Max(5)
    private Integer nota;

    @NotBlank @Size(max = 500)
    private String comentario;

    private LocalDateTime criadoEm = LocalDateTime.now();

    // getters/setters
    public Long getId() { return id; }
    public Integer getNota() { return nota; }
    public void setNota(Integer nota) { this.nota = nota; }
    public String getComentario() { return comentario; }
    public void setComentario(String comentario) { this.comentario = comentario; }
    public LocalDateTime getCriadoEm() { return criadoEm; }
    public void setCriadoEm(LocalDateTime criadoEm) { this.criadoEm = criadoEm; }
}
