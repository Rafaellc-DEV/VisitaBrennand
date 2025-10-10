package br.com.projetos3.visita.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;

@Entity
public class Aviso {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate data = LocalDate.now();

    @NotBlank
    private String texto;

    private boolean ativo = true;

    // getters/setters
    public Long getId() { return id; }
    public LocalDate getData() { return data; }
    public void setData(LocalDate data) { this.data = data; }
    public String getTexto() { return texto; }
    public void setTexto(String texto) { this.texto = texto; }
    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }
}
