package com.projetoweb.mecanica.entities;

import com.projetoweb.mecanica.entities.enums.FormaDePagamento;
import jakarta.persistence.*;

@Entity
@Table(name = "tb_pagamento")
public class Pagamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pagamento")
    private Long id;

    @Column(nullable = false)
    private Double valor;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private FormaDePagamento formaDePagamento;

    @OneToOne
    @JoinColumn(name = "order_id", nullable = false, unique = true)
    private Order order;

    public Pagamento() {
    }

    public Pagamento(Long id, FormaDePagamento formaDePagamento, Order order) {
        this.id = id;
        this.formaDePagamento = formaDePagamento;
        this.order = order;
        this.valor = calcularValor();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public FormaDePagamento getFormaDePagamento() {
        return formaDePagamento;
    }

    public void setFormaDePagamento(FormaDePagamento formaDePagamento) {
        this.formaDePagamento = formaDePagamento;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Double calcularValor() {
        if (order == null) {
            return 0.0;
        }
        
        double total = 0.0;
        
        if (order.getOrderProdutos() != null) {
            total += order.getOrderProdutos().stream()
                    .mapToDouble(OrderProduto::subTotalProduto)
                    .sum();
        }
        
        if (order.getOrderServicos() != null) {
            total += order.getOrderServicos().stream()
                    .mapToDouble(OrderServico::subTotalServico)
                    .sum();
        }
        
        return total;
    }
}
