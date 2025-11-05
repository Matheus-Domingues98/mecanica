package mapper;

import com.projetoweb.mecanica.dto.ClienteCreateDto;
import com.projetoweb.mecanica.dto.ClienteResponseDto;
import com.projetoweb.mecanica.entities.Cliente;
import org.modelmapper.ModelMapper;

public class ClienteMapper {

    private static final ModelMapper modelMapper = new ModelMapper();

    public static Cliente toEntity(ClienteCreateDto dto) {
        return modelMapper.map(dto, Cliente.class);
    }

    public static ClienteResponseDto toDto(Cliente cliente) {
        return modelMapper.map(cliente, ClienteResponseDto.class);
    }
}
