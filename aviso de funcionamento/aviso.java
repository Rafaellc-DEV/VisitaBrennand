package com.exemplo.visitabrennand.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Aviso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate data;
    private String situacao;
    @Column(length = 500)
    private String aviso;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public LocalDate getData() { return data; }
    public void setData(LocalDate data) { this.data = data; }
    public String getSituacao() { return situacao; }
    public void setSituacao(String situacao) { this.situacao = situacao; }
    public String getAviso() { return aviso; }
    public void setAviso(String aviso) { this.aviso = aviso; }
}
