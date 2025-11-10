package com.projetoweb.mecanica.exceptions;

public class EstoqueInsuficienteException extends BusinessException {
    
    public EstoqueInsuficienteException(String message) {
        super(message);
    }
    
    public EstoqueInsuficienteException(String produtoNome, Integer quantidadeDisponivel, Integer quantidadeSolicitada) {
        super(String.format("Estoque insuficiente para o produto '%s'. Disponível: %d, Solicitado: %d", 
                produtoNome, quantidadeDisponivel, quantidadeSolicitada));
    }
}
