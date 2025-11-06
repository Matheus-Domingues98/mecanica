package com.projetoweb.mecanica.dto.cliente_dto;

public class ClienteCreateDto {

    private String nome;
    private String doc;
    private String telefone;
    private String email;

    public ClienteCreateDto() {
    }

    public ClienteCreateDto(String nome, String doc, String telefone, String email) {
        this.nome = nome;
        this.doc = doc;
        this.telefone = telefone;
        this.email = email;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDoc() {
        return doc;
    }

    public void setDoc(String doc) {
        this.doc = doc;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
