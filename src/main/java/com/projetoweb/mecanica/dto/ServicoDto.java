package com.projetoweb.mecanica.dto;

import jakarta.validation.constraints.*;

public class ServicoDto {

    private Long id;
    
    @NotBlank(message = "Nome do serviço é obrigatório")
    @Size(min = 3, max = 100, message = "Nome do serviço deve ter entre 3 e 100 caracteres")
    private String nome;
    
    @Size(max = 500, message = "Descrição deve ter no máximo 500 caracteres")
    private String descricao;
    
    @NotNull(message = "Preço é obrigatório")
    @Positive(message = "Preço deve ser maior que zero")
    private Double preco;
    
    @NotNull(message = "Duração é obrigatória")
    @Positive(message = "Duração deve ser maior que zero")
    private Long duracaoMinutos;
    
    private boolean ativo;

    public ServicoDto() {
    }

    public ServicoDto(Long id, String nome, String descricao, Double preco, Long duracaoMinutos, boolean ativo) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.duracaoMinutos = duracaoMinutos;
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

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }
}
