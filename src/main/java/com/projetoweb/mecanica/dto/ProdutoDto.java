package com.projetoweb.mecanica.dto;

public class ProdutoDto {

    private Long id;
    private String nome;
    private Double preco;
    private boolean ativo;

    public ProdutoDto() {
    }

    public ProdutoDto(Long id, String nome, Double preco, boolean ativo) {
        this.id = id;
        this.nome = nome;
        this.preco = preco;
        this.ativo = ativo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }
}
