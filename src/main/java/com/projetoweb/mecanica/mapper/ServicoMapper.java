package com.projetoweb.mecanica.mapper;

import com.projetoweb.mecanica.dto.ServicoDto;
import com.projetoweb.mecanica.entities.Servico;

import java.time.Duration;

public class ServicoMapper {

    public static ServicoDto toDto(Servico servico) {
        if (servico == null) {
            return null;
        }
        Long duracaoMinutos = servico.getDuracaoServ() != null 
                ? servico.getDuracaoServ().toMinutes() 
                : null;
        
        return new ServicoDto(
                servico.getIdServ(),
                servico.getNomeServ(),
                servico.getDescricaoServ(),
                servico.getPrecoServ(),
                duracaoMinutos,
                servico.isAtivo()
        );
    }

    public static Servico toEntity(ServicoDto dto) {
        if (dto == null) {
            return null;
        }
        Servico servico = new Servico();
        servico.setIdServ(dto.getId());
        servico.setNomeServ(dto.getNome());
        servico.setDescricaoServ(dto.getDescricao());
        servico.setPrecoServ(dto.getPreco());
        
        if (dto.getDuracaoMinutos() != null) {
            servico.setDuracaoServ(Duration.ofMinutes(dto.getDuracaoMinutos()));
        }
        
        servico.setAtivo(dto.isAtivo());
        return servico;
    }
}
