package br.com.projetos3.visita.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
public class Regra {

    @Id
    private Long id; // Vamos usar um ID fixo = 1

    @NotBlank(message = "O título é obrigatório")
    @Size(min = 2, max = 120, message = "O título deve ter entre 2 e 120 caracteres")
    private String titulo;

    @NotBlank(message = "O conteúdo não pode estar vazio")
    @Column(columnDefinition = "TEXT") // Para textos longos
    private String conteudo;

    private LocalDateTime atualizadoEm;

    // Construtor padrão
    public Regra() {
        this.id = 1L; // Garante que o ID seja sempre 1
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = 1L; } // Ignora qualquer ID, força ser 1
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getConteudo() { return conteudo; }
    public void setConteudo(String conteudo) { this.conteudo = conteudo; }
    public LocalDateTime getAtualizadoEm() { return atualizadoEm; }
    public void setAtualizadoEm(LocalDateTime atualizadoEm) { this.atualizadoEm = atualizadoEm; }
}