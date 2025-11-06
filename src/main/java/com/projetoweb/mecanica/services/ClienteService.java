package com.projetoweb.mecanica.services;

import com.projetoweb.mecanica.dto.cliente_dto.ClienteCreateDto;
import com.projetoweb.mecanica.dto.cliente_dto.ClienteResponseDto;
import com.projetoweb.mecanica.entities.Cliente;
import com.projetoweb.mecanica.repositories.ClienteRepository;
import com.projetoweb.mecanica.mapper.ClienteMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    // Metodo de busca - OK
    public List<ClienteResponseDto> findAll() {
        List<Cliente> clientes = clienteRepository.findAll();
        return clientes.stream()
                .map(ClienteMapper::toDto)
                .toList();
    }


    // Metodo de busca por documento - OK
    public ClienteResponseDto findByDoc(String doc) {
        Cliente entity = clienteRepository.findByDoc(doc).orElseThrow(
                () -> new RuntimeException("Cliente nao encontrado com documento: " + doc)
        );
        return ClienteMapper.toDto(entity);
    }


    // Metodo de insercao - OK
    public ClienteResponseDto insert(ClienteCreateDto obj) {
        Cliente entity = ClienteMapper.toEntity(obj);
        entity = clienteRepository.save(entity);
        return ClienteMapper.toDto(entity);
    }

    // Metodo de exclusao - OK
    @Transactional
    public void delete(Long id) {
        if (!clienteRepository.existsById(id)) {
            throw new RuntimeException("Cliente nao encontrado com ID: " + id);
        }
        clienteRepository.deleteById(id);
    }

    @Transactional
    public void inativar(Long id) {
        Cliente entity = findByIdEntity(id);
        entity.setAtivo(false);
        clienteRepository.save(entity);
    }


    // Metodo de atualizacao - OK
    public ClienteResponseDto update(Long id, ClienteCreateDto obj) {
        // Busca o cliente pelo ID
        Cliente entity = findByIdEntity(id);

        // Atualiza os campos
        entity.setNome(obj.getNome());
        entity.setDoc(obj.getDoc());
        entity.setTelefone(obj.getTelefone());
        entity.setEmail(obj.getEmail());

        // Salvar
        entity = clienteRepository.save(entity);

        // Converter para dto
        return ClienteMapper.toDto(entity);
    }

    // Metodo de busca por ID (publico) - OK
    public ClienteResponseDto findById(Long id) {
        Cliente entity = findByIdEntity(id);
        return ClienteMapper.toDto(entity);
    }

    // Metodo auxiliar privado para buscar entidade
    private Cliente findByIdEntity(Long id) {
        return clienteRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Cliente nao encontrado com ID: " + id)
        );
    }
}
