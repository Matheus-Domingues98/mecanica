package com.projetoweb.mecanica.mapper;

import com.projetoweb.mecanica.dto.carro_dto.CarroCreateDto;
import com.projetoweb.mecanica.dto.carro_dto.CarroResponseDto;
import com.projetoweb.mecanica.entities.Carro;
import org.modelmapper.ModelMapper;

public class CarroMapper {

    private static final ModelMapper modelMapper = new ModelMapper();

    static {
        // Configurar para não mapear o objeto Cliente completo, apenas o ID
        modelMapper.typeMap(Carro.class, CarroResponseDto.class).addMappings(mapper -> {
            mapper.map(src -> src.getCliente().getId(), CarroResponseDto::setClienteId);
        });
    }

    public static Carro toEntity(CarroCreateDto dto) {
        return modelMapper.map(dto, Carro.class);
    }

    public static CarroResponseDto toDto(Carro carro) {
        CarroResponseDto dto = modelMapper.map(carro, CarroResponseDto.class);
        if (carro.getCliente() != null) {
            dto.setClienteId(carro.getCliente().getId());
        }
        return dto;
    }
}
