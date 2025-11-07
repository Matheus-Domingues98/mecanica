package com.projetoweb.mecanica.dto;

import jakarta.validation.constraints.*;

public class ProdutoDto {

    private Long id;
    
    @NotBlank(message = "Nome do produto é obrigatório")
    @Size(min = 2, max = 100, message = "Nome do produto deve ter entre 2 e 100 caracteres")
    private String nome;
    
    @NotNull(message = "Preço é obrigatório")
    @Positive(message = "Preço deve ser maior que zero")
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
