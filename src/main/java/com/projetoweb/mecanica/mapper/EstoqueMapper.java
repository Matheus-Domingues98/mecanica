package com.projetoweb.mecanica.mapper;

import com.projetoweb.mecanica.dto.EstoqueDto;
import com.projetoweb.mecanica.entities.Estoque;
import org.modelmapper.ModelMapper;

public class EstoqueMapper {

    private static ModelMapper modelMapper = new ModelMapper();

    public static Estoque toEntity(EstoqueDto dto) {
        return modelMapper.map(dto, Estoque.class);
    }

    public static  EstoqueDto toDto(Estoque estoque) {
        return modelMapper.map(estoque, EstoqueDto.class);
    }
}
