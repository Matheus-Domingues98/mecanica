package com.projetoweb.mecanica.dto;

public class EstoqueDto {

    private Long id;
    private Long produtoId;
    private Integer quantidade;

    public EstoqueDto() {
    }

    public EstoqueDto(Long id, Long produtoId, Integer quantidade) {
        this.id = id;
        this.produtoId = produtoId;
        this.quantidade = quantidade;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProdutoId() {
        return produtoId;
    }

    public void setProdutoId(Long produtoId) {
        this.produtoId = produtoId;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }
}
