package com.projetoweb.mecanica.dto;

public class OrderServicoDto {

    private Long orderId;
    private Long servicoId;
    private String nome;
    private String descricao;
    private Double preco;
    private Long duracaoMinutos;
    private Integer quantidade;
    private Double subtotal;

    public OrderServicoDto() {
    }

    public OrderServicoDto(Long orderId, Long servicoId, String nome, String descricao, Double preco, Long duracaoMinutos, Integer quantidade, Double subtotal) {
        this.orderId = orderId;
        this.servicoId = servicoId;
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.duracaoMinutos = duracaoMinutos;
        this.quantidade = quantidade;
        this.subtotal = subtotal;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getServicoId() {
        return servicoId;
    }

    public void setServicoId(Long servicoId) {
        this.servicoId = servicoId;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }

    public Long getDuracaoMinutos() {
        return duracaoMinutos;
    }

    public void setDuracaoMinutos(Long duracaoMinutos) {
        this.duracaoMinutos = duracaoMinutos;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public Double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }
}
