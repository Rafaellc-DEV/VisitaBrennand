package br.com.projetos3.visita.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Entity
public class Feedback {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // CAMPO NOVO
    @NotBlank(message = "O nome é obrigatório")
    @Size(min = 2, max = 80, message = "O nome deve ter entre 2 e 80 caracteres")
    private String nome;

    // CAMPO NOVO
    @NotBlank(message = "O tipo é obrigatório")
    private String tipo; // "Sugestão" ou "Reclamação"

    @NotNull(message = "A nota é obrigatória")
    @Min(value = 1, message = "Nota mínima é 1")
    @Max(value = 5, message = "Nota máxima é 5")
    private Integer nota;

    @NotBlank(message = "O comentário é obrigatório")
    @Size(min = 5, max = 1000, message = "O comentário deve ter entre 5 e 1000 caracteres")
    private String comentario;

    private LocalDateTime criadoEm = LocalDateTime.now();

    // getters/setters
    public Long getId() { return id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public Integer getNota() { return nota; }
    public void setNota(Integer nota) { this.nota = nota; }
    public String getComentario() { return comentario; }
    public void setComentario(String comentario) { this.comentario = comentario; }
    public LocalDateTime getCriadoEm() { return criadoEm; }
    public void setCriadoEm(LocalDateTime criadoEm) { this.criadoEm = criadoEm; }
}