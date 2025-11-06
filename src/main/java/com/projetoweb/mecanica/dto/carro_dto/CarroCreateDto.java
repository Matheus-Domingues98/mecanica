package com.projetoweb.mecanica.dto.carro_dto;



public class CarroCreateDto {

    private String modelo;
    private String marca;
    private Integer anoFabricacao;
    private String placa;
    private String cor;
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
