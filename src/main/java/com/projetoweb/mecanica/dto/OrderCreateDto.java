package com.projetoweb.mecanica.dto;

import java.util.List;

public class OrderCreateDto {

    private Long clienteId;
    private List<ItemProdutoDto> produtos;
    private List<ItemServicoDto> servicos;

    public OrderCreateDto() {
    }

    public OrderCreateDto(Long clienteId, List<ItemProdutoDto> produtos, List<ItemServicoDto> servicos) {
        this.clienteId = clienteId;
        this.produtos = produtos;
        this.servicos = servicos;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
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
        private Long produtoId;
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
        private Long servicoId;
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
