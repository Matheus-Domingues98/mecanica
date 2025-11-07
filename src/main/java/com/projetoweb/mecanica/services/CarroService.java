package com.projetoweb.mecanica.services;

import com.projetoweb.mecanica.dto.carro_dto.CarroCreateDto;
import com.projetoweb.mecanica.dto.carro_dto.CarroResponseDto;
import com.projetoweb.mecanica.entities.Carro;
import com.projetoweb.mecanica.entities.Cliente;
import com.projetoweb.mecanica.exceptions.DuplicateResourceException;
import com.projetoweb.mecanica.exceptions.ResourceNotFoundException;
import com.projetoweb.mecanica.repositories.CarroRepository;
import com.projetoweb.mecanica.repositories.ClienteRepository;
import com.projetoweb.mecanica.mapper.CarroMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CarroService {

    @Autowired
    private CarroRepository carroRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    // Metodo de busca - OK
    public List<CarroResponseDto> findAll() {
        List<Carro> carros = carroRepository.findAll();
        return carros.stream()
                .map(CarroMapper::toDto)
                .toList();
    }

    // Metodo de busca por Placa - OK
    public CarroResponseDto findByPlaca(String placa) {
        Carro entity = carroRepository.findByPlaca(placa).orElseThrow(
                () -> new ResourceNotFoundException("Carro", "placa", placa)
        );
        return CarroMapper.toDto(entity);
    }

    // Metodo de insercao - OK
    public CarroResponseDto insert(CarroCreateDto obj) {
        // Verifica se já existe carro com a mesma placa
        if (carroRepository.findByPlaca(obj.getPlaca()).isPresent()) {
            throw new DuplicateResourceException("Carro", "placa", obj.getPlaca());
        }
        
        // Buscar o cliente pelo ID
        Cliente cliente = clienteRepository.findById(obj.getClienteId())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente", "ID", obj.getClienteId()));

        // Mapear DTO para entidade
        Carro entity = CarroMapper.toEntity(obj);
        entity.setCliente(cliente);

        // Salvar
        entity = carroRepository.save(entity);
        return CarroMapper.toDto(entity);
    }

    // Metodo de exclusao - OK
    @Transactional
    public void delete(Long id) {
        if (!carroRepository.existsById(id)) {
            throw new ResourceNotFoundException("Carro", "ID", id);
        }
        carroRepository.deleteById(id);
    }

    // Metodo de atualizacao - OK
    public CarroResponseDto update(Long id, CarroCreateDto obj) {
        // Busca o carro pelo ID
        Carro entity = findByIdEntity(id);

        // Atualiza os campos
        entity.setModelo(obj.getModelo());
        entity.setMarca(obj.getMarca());
        entity.setAnoFabricacao(obj.getAnoFabricacao());
        entity.setPlaca(obj.getPlaca());
        entity.setCor(obj.getCor());

        // Se o clienteId foi alterado, atualizar o relacionamento
        if (obj.getClienteId() != null && !obj.getClienteId().equals(entity.getCliente().getId())) {
            Cliente novoCliente = clienteRepository.findById(obj.getClienteId())
                    .orElseThrow(() -> new ResourceNotFoundException("Cliente", "ID", obj.getClienteId()));
            entity.setCliente(novoCliente);
        }

        // Salvar
        entity = carroRepository.save(entity);

        // Converter para dto
        return CarroMapper.toDto(entity);
    }

    // Metodo de busca por ID (publico) - OK
    public CarroResponseDto findById(Long id) {
        Carro entity = findByIdEntity(id);
        return CarroMapper.toDto(entity);
    }

    // Metodo auxiliar privado para buscar entidade
    private Carro findByIdEntity(Long id) {
        return carroRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Carro", "ID", id)
        );
    }
}
