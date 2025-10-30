package com.projetoweb.mecanica.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import java.time.Duration;

@Entity
@Table(name = "tb_servico")
public class Servico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_servico")
    private Long idServ;
    
    @Column(nullable = false, length = 100)
    private String nomeServ;
    
    @Column(length = 500)
    private String descricaoServ;
    
    @Column(nullable = false)
    private Double precoServ;
    
    @Column(nullable = false)
    private Duration duracaoServ;

    @Column(nullable = false)
    private boolean ativo = true;

    public Servico() {
    }

    public Servico(Long idServ, String nomeServ, String descricaoServ, Double precoServ, Duration duracaoServ) {
        this.idServ = idServ;
        this.nomeServ = nomeServ;
        this.descricaoServ = descricaoServ;
        this.precoServ = precoServ;
        this.duracaoServ = duracaoServ;
    }

    public Long getIdServ() {
        return idServ;
    }

    public void setIdServ(Long idServ) {
        this.idServ = idServ;
    }

    public String getNomeServ() {
        return nomeServ;
    }

    public void setNomeServ(String nomeServ) {
        this.nomeServ = nomeServ;
    }

    public String getDescricaoServ() {
        return descricaoServ;
    }

    public void setDescricaoServ(String descricaoServ) {
        this.descricaoServ = descricaoServ;
    }

    public Double getPrecoServ() {
        return precoServ;
    }

    public void setPrecoServ(Double precoServ) {
        this.precoServ = precoServ;
    }

    public Duration getDuracaoServ() {
        return duracaoServ;
    }

    public void setDuracaoServ(Duration duracaoServ) {
        this.duracaoServ = duracaoServ;
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

