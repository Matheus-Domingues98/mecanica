package com.projetoweb.mecanica.entities;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "tb_estoque")
public class Estoque {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_estoque")
    private Long idEstoque;

    @Column(nullable = false)
    private Integer quantidadeEstoque;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "produto_id", nullable = false, unique = true)
    private Produto produto;

    public Estoque() {
    }

    public Estoque(Long idEstoque, Integer quantidadeEstoque, Produto produto) {
        this.idEstoque = idEstoque;
        this.quantidadeEstoque = quantidadeEstoque;
        this.produto = produto;
    }

    public Long getIdEstoque() {
        return idEstoque;
    }

    public void setIdEstoque(Long idEstoque) {
        this.idEstoque = idEstoque;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public Integer getQuantidadeEstoque() {
        return quantidadeEstoque;
    }

    public void setQuantidadeEstoque(Integer quantidadeEstoque) {
        this.quantidadeEstoque = quantidadeEstoque;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Estoque estoque = (Estoque) o;
        return Objects.equals(idEstoque, estoque.idEstoque) && Objects.equals(quantidadeEstoque, estoque.quantidadeEstoque) && Objects.equals(produto, estoque.produto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idEstoque, quantidadeEstoque, produto);
    }

    public void venderProduto(Integer quantidade) {
        if (this.quantidadeEstoque < quantidade) {
            throw  new IllegalStateException("Quantidade de estoque insuficiente");
        } else {
            this.quantidadeEstoque -= quantidade;
        }
    }

    public void adicionarEstoque(Integer quantidade) {
        quantidadeEstoque += quantidade;
    }

    public boolean temEstoqueSuficiente(Integer quantidade) {
        return quantidadeEstoque >= quantidade;
    }
}
