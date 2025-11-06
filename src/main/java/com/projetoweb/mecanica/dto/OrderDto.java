package com.projetoweb.mecanica.dto;

import com.projetoweb.mecanica.entities.enums.OrderStatus;

import java.util.List;

public class OrderDto {

    private Long id;
    private Long clienteId;
    private OrderStatus status;
    private boolean ativo;
    private List<OrderProdutoDto> produtos;
    private List<OrderServicoDto> servicos;

    public OrderDto() {
    }

    public OrderDto(Long id, Long clienteId, OrderStatus status, boolean ativo, List<OrderProdutoDto> produtos, List<OrderServicoDto> servicos) {
        this.id = id;
        this.clienteId = clienteId;
        this.status = status;
        this.ativo = ativo;
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
