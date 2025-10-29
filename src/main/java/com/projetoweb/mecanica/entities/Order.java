package com.projetoweb.mecanica.entities;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "tb_order")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "carro_id")
    private Carro carro;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    private Pagamento pagamento;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderProduto> orderProdutos;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderServico> orderServicos;

    public Order() {
    }

    public Order(Long id, Cliente cliente, Carro carro, Pagamento pagamento) {
        this.id = id;
        this.cliente = cliente;
        this.carro = carro;
        this.pagamento = pagamento;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Carro getCarro() {
        return carro;
    }

    public void setCarro(Carro carro) {
        this.carro = carro;
    }

    public Pagamento getPagamento() {
        return pagamento;
    }

    public void setPagamento(Pagamento pagamento) {
        this.pagamento = pagamento;
    }

    public List<OrderProduto> getOrderProdutos() {
        return orderProdutos;
    }

    public void setOrderProdutos(List<OrderProduto> orderProdutos) {
        this.orderProdutos = orderProdutos;
    }

    public List<OrderServico> getOrderServicos() {
        return orderServicos;
    }

    public void setOrderServicos(List<OrderServico> orderServicos) {
        this.orderServicos = orderServicos;
    }
}
