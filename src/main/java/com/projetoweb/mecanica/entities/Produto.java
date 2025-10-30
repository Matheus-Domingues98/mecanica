package com.projetoweb.mecanica.entities;

import jakarta.persistence.*;


@Entity
@Table(name = "tb_produto")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_produto")
    private Long idProd;
    
    @Column(nullable = false, length = 100)
    private String nomeProd;
    
    @Column(nullable = false)
    private Double precoProd;

    @OneToOne(mappedBy = "produto", cascade = CascadeType.ALL)
    private Estoque estoque;

    @Column(nullable = false)
    private boolean ativo = true;

    public Produto() {
    }

    public Produto(Long idProd, String nomeProd, Double precoProd, Estoque estoque) {
        this.idProd = idProd;
        this.nomeProd = nomeProd;
        this.precoProd = precoProd;
        this.estoque = estoque;
    }

    public Long getIdProd() {
        return idProd;
    }

    public void setIdProd(Long idProd) {
        this.idProd = idProd;
    }

    public String getNomeProd() {
        return nomeProd;
    }

    public void setNomeProd(String nomeProd) {
        this.nomeProd = nomeProd;
    }

    public Double getPrecoProd() {
        return precoProd;
    }

    public void setPrecoProd(Double precoProd) {
        this.precoProd = precoProd;
    }

    public Estoque getEstoque() {
        return estoque;
    }

    public void setEstoque(Estoque estoque) {
        this.estoque = estoque;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public void desativar() {
        this.ativo = false;
    }

    public void ativar() {
        this.ativo = true;
    }
}
