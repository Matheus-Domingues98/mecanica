package com.projetoweb.mecanica.services;

import com.projetoweb.mecanica.dto.cliente_dto.ClienteCreateDto;
import com.projetoweb.mecanica.dto.cliente_dto.ClienteResponseDto;
import com.projetoweb.mecanica.entities.Cliente;
import com.projetoweb.mecanica.exceptions.DuplicateResourceException;
import com.projetoweb.mecanica.exceptions.ResourceNotFoundException;
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
        // Remove caracteres especiais (pontos, traços, barras) para busca flexível
        String cleanDoc = doc.replaceAll("[^0-9]", "");
        
        Cliente entity = clienteRepository.findByDoc(cleanDoc).orElseThrow(
                () -> new ResourceNotFoundException("Cliente", "documento", doc)
        );
        return ClienteMapper.toDto(entity);
    }


    // Metodo de insercao - OK
    public ClienteResponseDto insert(ClienteCreateDto obj) {
        // Verifica se já existe cliente com o mesmo documento
        if (clienteRepository.findByDoc(obj.getDoc()).isPresent()) {
            throw new DuplicateResourceException("Cliente", "documento", obj.getDoc());
        }
        
        // Verifica se já existe cliente com o mesmo email
        if (clienteRepository.findByEmail(obj.getEmail()).isPresent()) {
            throw new DuplicateResourceException("Cliente", "email", obj.getEmail());
        }
        
        Cliente entity = ClienteMapper.toEntity(obj);
        entity = clienteRepository.save(entity);
        return ClienteMapper.toDto(entity);
    }

    // Metodo de exclusao - OK
    @Transactional
    public void delete(Long id) {
        if (!clienteRepository.existsById(id)) {
            throw new ResourceNotFoundException("Cliente", "ID", id);
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

        // Verifica se o novo documento já existe em outro cliente
        if (!entity.getDoc().equals(obj.getDoc())) {
            clienteRepository.findByDoc(obj.getDoc()).ifPresent(c -> {
                if (!c.getId().equals(id)) {
                    throw new DuplicateResourceException("Cliente", "documento", obj.getDoc());
                }
            });
        }

        // Verifica se o novo email já existe em outro cliente
        if (!entity.getEmail().equals(obj.getEmail())) {
            clienteRepository.findByEmail(obj.getEmail()).ifPresent(c -> {
                if (!c.getId().equals(id)) {
                    throw new DuplicateResourceException("Cliente", "email", obj.getEmail());
                }
            });
        }

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
                () -> new ResourceNotFoundException("Cliente", "ID", id)
        );
    }
}
