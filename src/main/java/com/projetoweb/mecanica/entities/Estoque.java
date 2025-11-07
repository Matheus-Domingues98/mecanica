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

    /**
     * Decrementa o estoque ao vender/usar um produto
     * @param quantidade Quantidade a ser decrementada
     * @throws IllegalStateException se não houver estoque suficiente
     */
    public void decrementar(Integer quantidade) {
        if (quantidade == null || quantidade <= 0) {
            throw new IllegalArgumentException("Quantidade deve ser maior que zero");
        }
        if (this.quantidadeEstoque < quantidade) {
            throw new IllegalStateException("Estoque insuficiente. Disponível: " + this.quantidadeEstoque + ", Solicitado: " + quantidade);
        }
        this.quantidadeEstoque -= quantidade;
    }

    /**
     * Incrementa o estoque ao adicionar produtos
     * @param quantidade Quantidade a ser adicionada
     */
    public void incrementar(Integer quantidade) {
        if (quantidade == null || quantidade <= 0) {
            throw new IllegalArgumentException("Quantidade deve ser maior que zero");
        }
        this.quantidadeEstoque += quantidade;
    }

    /**
     * Verifica se há estoque suficiente
     * @param quantidade Quantidade desejada
     * @return true se houver estoque suficiente
     */
    public boolean temEstoqueSuficiente(Integer quantidade) {
        return quantidade != null && quantidadeEstoque >= quantidade;
    }

    /**
     * Retorna a quantidade disponível em estoque
     * @return quantidade disponível
     */
    public Integer getQuantidade() {
        return quantidadeEstoque;
    }

    // Métodos legados - mantidos para compatibilidade
    @Deprecated
    public void venderProduto(Integer quantidade) {
        decrementar(quantidade);
    }

    @Deprecated
    public void adicionarEstoque(Integer quantidade) {
        incrementar(quantidade);
    }
}
