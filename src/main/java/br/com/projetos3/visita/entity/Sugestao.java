package br.com.projetos3.visita.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
public class Sugestao {
    public enum Tipo { SUGESTAO, RECLAMACAO }

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Tipo tipo = Tipo.SUGESTAO;

    @NotBlank
    private String texto;

    private LocalDateTime criadoEm = LocalDateTime.now();

    // getters/setters
    public Long getId() { return id; }
    public Tipo getTipo() { return tipo; }
    public void setTipo(Tipo tipo) { this.tipo = tipo; }
    public String getTexto() { return texto; }
    public void setTexto(String texto) { this.texto = texto; }
    public LocalDateTime getCriadoEm() { return criadoEm; }
    public void setCriadoEm(LocalDateTime criadoEm) { this.criadoEm = criadoEm; }
}
