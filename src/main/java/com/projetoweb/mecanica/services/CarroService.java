package com.projetoweb.mecanica.services;

import com.projetoweb.mecanica.dto.CarroResponseDto;
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
public class CarroService {

    @Autowired
    private CarroRepository carroRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    public List<CarroResponseDto> findAll() {

        List<Carro> carros = carroRepository.findAll();

        return carros.stream()
                .map(CarroResponseDto::new)
                .toList();
    }

    public CarroResponseDto findById(Long id) {

        Carro entity = carroRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Carro nao encontrado com ID: " + id)
        );

        return  new CarroResponseDto(entity);
    }


    public CarroResponseDto insert(CarroResponseDto obj) {

       Long clienteId = obj.getCliente();

       Cliente cliente = clienteRepository.findById(clienteId).orElseThrow(
               () -> new RuntimeException("Cliente nao encontrado com ID: " + clienteId)
       );

       Carro entity = new Carro(
               null,
               obj.getModelo(),
               obj.getMarca(),
               obj.getAnoFabricacao(),
               obj.getPlaca(),
               obj.getCor(),
               cliente
       );

       entity = carroRepository.save(entity);
       return new CarroResponseDto(entity);
    }


    public void delete(Long id) {
        if (!carroRepository.existsById(id)) {
            throw new RuntimeException("Carro nao encontrado com ID: " + id);
        }
        carroRepository.deleteById(id);
    }


    public CarroResponseDto update(Long id, CarroResponseDto obj) {
        Carro entity = carroRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Carro não encontrado com ID: " + id)
        );

        entity.setModelo(obj.getModelo());
        entity.setMarca(obj.getMarca());
        entity.setAnoFabricacao(obj.getAnoFabricacao());
        entity.setPlaca(obj.getPlaca());
        entity.setCor(obj.getCor());
        if (obj.getCliente() != null) {
            Cliente cliente = clienteRepository.findById(obj.getCliente()).orElseThrow(
                    () -> new RuntimeException("Cliente nao encontrado com ID: " + obj.getCliente())
            );
            entity.setCliente(cliente);
        }

        Carro updateCarro = carroRepository.save(entity);

        return new CarroResponseDto(updateCarro);

    }
}
