package com.projetoweb.mecanica.dto;

import com.projetoweb.mecanica.entities.enums.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

public class OrderDto {

    private Long id;
    private Long clienteId;
    private Long carroId;
    private String carroPlaca;
    private String carroModelo;
    private OrderStatus status;
    private boolean ativo;
    private Double valorTotal;
    private Boolean orcamentoAprovado;
    private LocalDateTime dataAprovacao;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataInicioExecucao;
    private LocalDateTime dataFinalizacao;
    private List<OrderProdutoDto> produtos;
    private List<OrderServicoDto> servicos;

    public OrderDto() {
    }

    public OrderDto(Long id, Long clienteId, Long carroId, String carroPlaca, String carroModelo, OrderStatus status, boolean ativo, Double valorTotal, Boolean orcamentoAprovado, LocalDateTime dataAprovacao, LocalDateTime dataCriacao, LocalDateTime dataInicioExecucao, LocalDateTime dataFinalizacao, List<OrderProdutoDto> produtos, List<OrderServicoDto> servicos) {
        this.id = id;
        this.clienteId = clienteId;
        this.carroId = carroId;
        this.carroPlaca = carroPlaca;
        this.carroModelo = carroModelo;
        this.status = status;
        this.ativo = ativo;
        this.valorTotal = valorTotal;
        this.orcamentoAprovado = orcamentoAprovado;
        this.dataAprovacao = dataAprovacao;
        this.dataCriacao = dataCriacao;
        this.dataInicioExecucao = dataInicioExecucao;
        this.dataFinalizacao = dataFinalizacao;
        this.produtos = produtos;
        this.servicos = servicos;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public Long getCarroId() {
        return carroId;
    }

    public void setCarroId(Long carroId) {
        this.carroId = carroId;
    }

    public String getCarroPlaca() {
        return carroPlaca;
    }

    public void setCarroPlaca(String carroPlaca) {
        this.carroPlaca = carroPlaca;
    }

    public String getCarroModelo() {
        return carroModelo;
    }

    public void setCarroModelo(String carroModelo) {
        this.carroModelo = carroModelo;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public Double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(Double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public Boolean getOrcamentoAprovado() {
        return orcamentoAprovado;
    }

    public void setOrcamentoAprovado(Boolean orcamentoAprovado) {
        this.orcamentoAprovado = orcamentoAprovado;
    }

    public LocalDateTime getDataAprovacao() {
        return dataAprovacao;
    }

    public void setDataAprovacao(LocalDateTime dataAprovacao) {
        this.dataAprovacao = dataAprovacao;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public LocalDateTime getDataInicioExecucao() {
        return dataInicioExecucao;
    }

    public void setDataInicioExecucao(LocalDateTime dataInicioExecucao) {
        this.dataInicioExecucao = dataInicioExecucao;
    }

    public LocalDateTime getDataFinalizacao() {
        return dataFinalizacao;
    }

    public void setDataFinalizacao(LocalDateTime dataFinalizacao) {
        this.dataFinalizacao = dataFinalizacao;
    }

    public List<OrderProdutoDto> getProdutos() {
        return produtos;
    }

    public void setProdutos(List<OrderProdutoDto> produtos) {
        this.produtos = produtos;
    }

    public List<OrderServicoDto> getServicos() {
        return servicos;
    }

    public void setServicos(List<OrderServicoDto> servicos) {
        this.servicos = servicos;
    }
}
