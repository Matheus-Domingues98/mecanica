package com.projetoweb.mecanica.dto;

import com.projetoweb.mecanica.entities.Carro;

public class CarroResponseDto {

    private Long id;
    private String modelo;
    private String marca;
    private Integer anoFabricacao;
    private String placa;
    private String cor;

    private Long cliente;

    public CarroResponseDto() {
    }

    public CarroResponseDto(Carro entity) {
        this.id = entity.getId();
        this.modelo = entity.getModelo();
        this.marca = entity.getMarca();
        this.anoFabricacao = entity.getAnoFabricacao();
        this.placa = entity.getPlaca();
        this.cor = entity.getCor();

        if (entity.getCliente() != null) {
            this.cliente = entity.getCliente().getId();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getCliente() {
        return cliente;
    }

    public void setCliente(Long cliente) {
        this.cliente = cliente;
    }
}
