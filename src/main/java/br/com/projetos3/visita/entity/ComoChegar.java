package br.com.projetos3.visita.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
public class ComoChegar {

    @Id
    private Long id = 1L; // Singleton: ID sempre será 1

    // --- Bloco 1: Barco ---
    @NotBlank(message = "A descrição do barco é obrigatória")
    private String barcoDescricao;

    @NotBlank(message = "O preço do barco é obrigatório")
    private String barcoPreco;

    @NotBlank(message = "O tempo do barco é obrigatório")
    private String barcoTempo;

    @NotBlank(message = "A info sobre bicicleta é obrigatória")
    private String barcoBike;

    // --- Bloco 2: Carro ---
    @NotBlank(message = "A descrição do carro é obrigatória")
    private String carroDescricao;

    @NotBlank(message = "O passo 1 é obrigatório")
    private String carroPasso1;

    @NotBlank(message = "O passo 2 é obrigatório")
    private String carroPasso2;

    @NotBlank(message = "O passo 3 é obrigatório")
    private String carroPasso3;

    // --- Bloco 3: Catamarã ---
    @NotBlank(message = "A descrição do catamarã é obrigatória")
    private String catamaraDescricao;

    @NotBlank(message = "O link do catamarã é obrigatório")
    private String catamaraLink;

    private LocalDateTime atualizadoEm;

    public ComoChegar() {
        this.id = 1L;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = 1L; }

    public String getBarcoDescricao() { return barcoDescricao; }
    public void setBarcoDescricao(String barcoDescricao) { this.barcoDescricao = barcoDescricao; }
    public String getBarcoPreco() { return barcoPreco; }
    public void setBarcoPreco(String barcoPreco) { this.barcoPreco = barcoPreco; }
    public String getBarcoTempo() { return barcoTempo; }
    public void setBarcoTempo(String barcoTempo) { this.barcoTempo = barcoTempo; }
    public String getBarcoBike() { return barcoBike; }
    public void setBarcoBike(String barcoBike) { this.barcoBike = barcoBike; }

    public String getCarroDescricao() { return carroDescricao; }
    public void setCarroDescricao(String carroDescricao) { this.carroDescricao = carroDescricao; }
    public String getCarroPasso1() { return carroPasso1; }
    public void setCarroPasso1(String carroPasso1) { this.carroPasso1 = carroPasso1; }
    public String getCarroPasso2() { return carroPasso2; }
    public void setCarroPasso2(String carroPasso2) { this.carroPasso2 = carroPasso2; }
    public String getCarroPasso3() { return carroPasso3; }
    public void setCarroPasso3(String carroPasso3) { this.carroPasso3 = carroPasso3; }

    public String getCatamaraDescricao() { return catamaraDescricao; }
    public void setCatamaraDescricao(String catamaraDescricao) { this.catamaraDescricao = catamaraDescricao; }
    public String getCatamaraLink() { return catamaraLink; }
    public void setCatamaraLink(String catamaraLink) { this.catamaraLink = catamaraLink; }

    public LocalDateTime getAtualizadoEm() { return atualizadoEm; }
    public void setAtualizadoEm(LocalDateTime atualizadoEm) { this.atualizadoEm = atualizadoEm; }
}