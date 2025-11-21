package br.com.projetos3.visita.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
public class ComoChegar {

    @Id
    private Long id = 1L; // Singleton: ID sempre será 1

    @NotBlank(message = "O conteúdo não pode estar vazio")
    @Column(columnDefinition = "TEXT") // Permite textos longos (HTML)
    private String conteudo;

    private LocalDateTime atualizadoEm;

    public ComoChegar() {
        this.id = 1L;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = 1L; } // Força ID 1
    public String getConteudo() { return conteudo; }
    public void setConteudo(String conteudo) { this.conteudo = conteudo; }
    public LocalDateTime getAtualizadoEm() { return atualizadoEm; }
    public void setAtualizadoEm(LocalDateTime atualizadoEm) { this.atualizadoEm = atualizadoEm; }
}