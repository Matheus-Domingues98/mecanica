package com.projetoweb.mecanica.dto.carro_dto;

import com.projetoweb.mecanica.validation.PlacaVeiculo;
import jakarta.validation.constraints.*;

public class CarroCreateDto {

    @NotBlank(message = "Modelo é obrigatório")
    @Size(min = 2, max = 50, message = "Modelo deve ter entre 2 e 50 caracteres")
    private String modelo;
    
    @NotBlank(message = "Marca é obrigatória")
    @Size(min = 2, max = 50, message = "Marca deve ter entre 2 e 50 caracteres")
    private String marca;
    
    @NotNull(message = "Ano de fabricação é obrigatório")
    @Min(value = 1900, message = "Ano de fabricação deve ser maior ou igual a 1900")
    @Max(value = 2100, message = "Ano de fabricação deve ser menor ou igual a 2100")
    private Integer anoFabricacao;
    
    @NotBlank(message = "Placa é obrigatória")
    @PlacaVeiculo
    private String placa;
    
    @Size(max = 30, message = "Cor deve ter no máximo 30 caracteres")
    private String cor;
    
    @NotNull(message = "ID do cliente é obrigatório")
    private Long clienteId;

    public CarroCreateDto() {
    }

    public CarroCreateDto(String modelo, String marca, Integer anoFabricacao, String placa, String cor, Long clienteId) {
        this.modelo = modelo;
        this.marca = marca;
        this.anoFabricacao = anoFabricacao;
        this.placa = placa;
        this.cor = cor;
        this.clienteId = clienteId;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public Integer getAnoFabricacao() {
        return anoFabricacao;
    }

    public void setAnoFabricacao(Integer anoFabricacao) {
        this.anoFabricacao = anoFabricacao;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }
}
