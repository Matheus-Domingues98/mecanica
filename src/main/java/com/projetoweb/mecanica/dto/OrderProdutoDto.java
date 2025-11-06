package com.projetoweb.mecanica.dto;

public class OrderProdutoDto {

    private Long orderId;
    private Long produtoId;
    private String nome;
    private Double preco;
    private Integer quantidade;
    private Double subtotal;

    public OrderProdutoDto() {
    }

    public OrderProdutoDto(Long orderId, Long produtoId, String nome, Double preco, Integer quantidade, Double subtotal) {
        this.orderId = orderId;
        this.produtoId = produtoId;
        this.nome = nome;
        this.preco = preco;
        this.quantidade = quantidade;
        this.subtotal = subtotal;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getProdutoId() {
        return produtoId;
    }

    public void setProdutoId(Long produtoId) {
        this.produtoId = produtoId;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
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
