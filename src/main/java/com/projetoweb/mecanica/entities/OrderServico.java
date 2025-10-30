package com.projetoweb.mecanica.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.projetoweb.mecanica.entities.pk.OrderServicoPk;
import jakarta.persistence.*;

import java.time.Duration;

@Entity
@Table(name = "order_servico")
public class OrderServico {

    @EmbeddedId
    private OrderServicoPk id = new OrderServicoPk();

    @Column(nullable = false, length = 100)
    private String nome;
    
    @Column(nullable = false)
    private Double preco;
    
    @Column(length = 500)
    private String descricao;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    @Column(nullable = false)
    private Duration duracao;
    
    @Column(nullable = false)
    private Integer quantidade;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("orderId")
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("servicoId")
    @JoinColumn(name = "servico_id")
    private Servico servico;

    public OrderServico() {
    }

    public OrderServico(Order order, Servico servico, String nome, Double preco, Integer quantidade, String descricao, Duration duracao) {
        this.order = order;
        this.servico = servico;

        this.id.setOrderId(order.getId());
        this.id.setServicoId(servico.getIdServ());

        this.nome = nome;
        this.preco = preco;
        this.descricao = descricao;
        this.duracao = duracao;
        this.quantidade = quantidade;
    }

    public OrderServicoPk getId() {
        return id;
    }

    public void setId(OrderServicoPk id) {
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

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Duration getDuracao() {
        return duracao;
    }

    public void setDuracao(Duration duracao) {
        this.duracao = duracao;
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
    }

    public Servico getServico() {
        return servico;
    }

    public void setServico(Servico servico) {
        this.servico = servico;
    }

    public Double subTotalServico() {
        return  preco * quantidade;
    }
}
