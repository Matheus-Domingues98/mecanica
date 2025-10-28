package com.projetoweb.mecanica.dto;

import com.projetoweb.mecanica.entities.Cliente;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ClienteResponseDto {

    private Long id;
    private String nome;
    private String telefone;
    private String email;
    private String doc;

    private List<CarroResponseDto> carros = new ArrayList<>();

    public ClienteResponseDto() {
    }

    public ClienteResponseDto(Cliente cliente) {
        this.id = cliente.getId();
        this.nome = cliente.getNome();
        this.telefone = cliente.getTelefone();
        this.email = cliente.getEmail();
        this.doc = cliente.getDoc();

        if(cliente.getCarros() != null){
            this.carros = cliente.getCarros()
                    .stream().map(CarroResponseDto::new)
                    .collect(Collectors.toList());
        }
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

    public String getDoc() {
        return doc;
    }

    public void setDoc(String doc) {
        this.doc = doc;
    }

    public List<CarroResponseDto> getCarros() {
        return carros;
    }

    public void setCarros(List<CarroResponseDto> carros) {
        this.carros = carros;
    }
}
