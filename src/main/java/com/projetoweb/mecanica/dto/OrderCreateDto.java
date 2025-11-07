package com.projetoweb.mecanica.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class OrderCreateDto {

    @NotNull(message = "ID do cliente é obrigatório")
    private Long clienteId;
    
    @NotNull(message = "ID do carro é obrigatório")
    private Long carroId;
    
    @Valid
    private List<ItemProdutoDto> produtos;
    
    @Valid
    private List<ItemServicoDto> servicos;

    public OrderCreateDto() {
    }

    public OrderCreateDto(Long clienteId, Long carroId, List<ItemProdutoDto> produtos, List<ItemServicoDto> servicos) {
        this.clienteId = clienteId;
        this.carroId = carroId;
        this.produtos = produtos;
        this.servicos = servicos;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public Long getCarroId() {
        return carroId;
    }

    public void setCarroId(Long carroId) {
        this.carroId = carroId;
    }

    public List<ItemProdutoDto> getProdutos() {
        return produtos;
    }

    public void setProdutos(List<ItemProdutoDto> produtos) {
        this.produtos = produtos;
    }

    public List<ItemServicoDto> getServicos() {
        return servicos;
    }

    public void setServicos(List<ItemServicoDto> servicos) {
        this.servicos = servicos;
    }

    public static class ItemProdutoDto {
        @NotNull(message = "ID do produto é obrigatório")
        private Long produtoId;
        
        @NotNull(message = "Quantidade é obrigatória")
        @jakarta.validation.constraints.Positive(message = "Quantidade deve ser maior que zero")
        private Integer quantidade;

        public ItemProdutoDto() {
        }

        public ItemProdutoDto(Long produtoId, Integer quantidade) {
            this.produtoId = produtoId;
            this.quantidade = quantidade;
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

    public static class ItemServicoDto {
        @NotNull(message = "ID do serviço é obrigatório")
        private Long servicoId;
        
        @NotNull(message = "Quantidade é obrigatória")
        @jakarta.validation.constraints.Positive(message = "Quantidade deve ser maior que zero")
        private Integer quantidade;

        public ItemServicoDto() {
        }

        public ItemServicoDto(Long servicoId, Integer quantidade) {
            this.servicoId = servicoId;
            this.quantidade = quantidade;
        }

        public Long getServicoId() {
            return servicoId;
        }

        public void setServicoId(Long servicoId) {
            this.servicoId = servicoId;
        }

        public Integer getQuantidade() {
            return quantidade;
        }

        public void setQuantidade(Integer quantidade) {
            this.quantidade = quantidade;
        }
    }
}
