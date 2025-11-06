package com.projetoweb.mecanica.mapper;

import com.projetoweb.mecanica.dto.cliente_dto.ClienteCreateDto;
import com.projetoweb.mecanica.dto.cliente_dto.ClienteResponseDto;
import com.projetoweb.mecanica.entities.Cliente;
import org.modelmapper.ModelMapper;

import java.util.stream.Collectors;

public class ClienteMapper {

    private static final ModelMapper modelMapper = new ModelMapper();

    static {
        // Configurar para evitar mapeamento circular entre Cliente e Carro
        modelMapper.typeMap(Cliente.class, ClienteResponseDto.class).addMappings(mapper -> {
            mapper.skip(ClienteResponseDto::setCarros);
        });
    }

    public static Cliente toEntity(ClienteCreateDto dto) {
        return modelMapper.map(dto, Cliente.class);
    }

    public static ClienteResponseDto toDto(Cliente cliente) {
        ClienteResponseDto dto = modelMapper.map(cliente, ClienteResponseDto.class);
        
        // Mapear manualmente a lista de carros para evitar referência circular
        if (cliente.getCarros() != null && !cliente.getCarros().isEmpty()) {
            dto.setCarros(
                cliente.getCarros().stream()
                    .map(CarroMapper::toDto)
                    .collect(Collectors.toList())
            );
        }
        
        return dto;
    }
}
