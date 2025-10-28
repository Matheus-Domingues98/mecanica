package com.projetoweb.mecanica.services;

import com.projetoweb.mecanica.dto.CarroInsertDto;
import com.projetoweb.mecanica.dto.CarroResponseDto;
import com.projetoweb.mecanica.dto.ClienteRequestDto;
import com.projetoweb.mecanica.dto.ClienteResponseDto;
import com.projetoweb.mecanica.entities.Carro;
import com.projetoweb.mecanica.entities.Cliente;
import com.projetoweb.mecanica.repositories.CarroRepository;
import com.projetoweb.mecanica.repositories.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public List<ClienteResponseDto> findAll() {

        List<Cliente> clientes = clienteRepository.findAll();

        return clientes.stream()
                .map(ClienteResponseDto::new)
                .toList();
    }

    public ClienteResponseDto findById(Long id) {

        Cliente entity = clienteRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Cliente nao encontrado com ID: " + id)
        );

        return new ClienteResponseDto(entity);
    }

    public ClienteResponseDto findByDoc(String doc) {
        Cliente entity = clienteRepository.findByDoc(doc).orElseThrow(
                () -> new RuntimeException("Cliente nao encontrado com documento: " + doc)
        );

        return new ClienteResponseDto(entity);
    }


    public ClienteResponseDto insert(ClienteRequestDto obj) {
        Cliente entity = new Cliente(
                null,
                obj.getNome(),
                obj.getDoc(),
                obj.getTelefone(),
                obj.getEmail());

        if (obj.getCarros() != null) {
            for (CarroInsertDto carro : obj.getCarros()) {
                Carro entityCarro = new Carro(
                        null,
                        carro.getModelo(),
                        carro.getMarca(),
                        carro.getAnoFabricacao(),
                        carro.getPlaca(),
                        carro.getCor(),
                        entity
                );
                entity.getCarros().add(entityCarro);
            }
        }
        Cliente savedCliente = clienteRepository.save(entity);

        return new ClienteResponseDto(savedCliente);
    }

    @Transactional
    public void delete(Long id) {
        if (!clienteRepository.existsById(id)) {
            throw new RuntimeException("Cliente nao encontrado com ID: " + id);
        }
        clienteRepository.deleteById(id);
    }


    public ClienteResponseDto update(Long id, ClienteResponseDto obj) {
        Cliente entity = clienteRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Cliente não encontrado com ID: " + id)
        );

        entity.setNome(obj.getNome());
        entity.setDoc(obj.getDoc());
        entity.setTelefone(obj.getTelefone());
        entity.setEmail(obj.getEmail());
        /*
            Se o cliente tiver carros, não há como atualizar essa lista
            Usa
            ClienteResponseDto
            (que tem carros) mas ignora esse campo
            Decisão necessária:

            Criar ClienteUpdateDto sem carros (se carros não devem ser atualizados por aqui)
            OU implementar lógica para atualizar carros também
         */

        Cliente updateCliente = clienteRepository.save(entity);

        return new ClienteResponseDto(updateCliente);
    }

}
