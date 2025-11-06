package com.projetoweb.mecanica.dto;

import com.projetoweb.mecanica.entities.enums.FormaDePagamento;

public class PagamentoDto {

    private Long id;
    private Double valor;
    private FormaDePagamento formaDePagamento;
    private Long orderId;

    public PagamentoDto() {
    }

    public PagamentoDto(Long id, Double valor, FormaDePagamento formaDePagamento, Long orderId) {
        this.id = id;
        this.valor = valor;
        this.formaDePagamento = formaDePagamento;
        this.orderId = orderId;
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

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
}
