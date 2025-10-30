package com.projetoweb.mecanica.entities;

import com.projetoweb.mecanica.entities.pk.OrderProdutoPk;
import jakarta.persistence.*;

@Entity
@Table(name = "order_produto")
public class OrderProduto {

    @EmbeddedId
    private OrderProdutoPk id = new OrderProdutoPk();

    @Column(nullable = false, length = 100)
    private String nome;
    
    @Column(nullable = false)
    private Double preco;
    
    @Column(nullable = false)
    private Integer quantidade;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("orderId")
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("produtoId")
    @JoinColumn(name = "produto_id")
    private Produto produto;

    public OrderProduto() {
    }

    public OrderProduto(Order order, Produto produto, String nome, Double preco, Integer quantidade) {
        this.order = order;
        this.produto = produto;

        this.id.setOrderId(order.getId());
        this.id.setProdutoId(produto.getIdProd());

        this.nome = nome;
        this.preco = preco;
        this.quantidade = quantidade;
    }

    public OrderProdutoPk getId() {
        return id;
    }

    public void setId(OrderProdutoPk id) {
        this.id = id;
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

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
        this.id.setOrderId(order.getId());
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
        this.id.setProdutoId(produto.getIdProd());
    }

    public Double subTotalProduto() {
        return preco * quantidade;
    }
}

