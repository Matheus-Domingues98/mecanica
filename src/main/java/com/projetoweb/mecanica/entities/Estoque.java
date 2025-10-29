package com.projetoweb.mecanica.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "tb_estoque")
public class Estoque {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEstoque;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "produto_id")
    private Produto produto;

    public Estoque() {
    }

    public Estoque(Long idEstoque, Produto produto) {
        this.idEstoque = idEstoque;

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
}
