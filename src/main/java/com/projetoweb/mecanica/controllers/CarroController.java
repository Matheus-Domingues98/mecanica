package com.projetoweb.mecanica.controllers;

import com.projetoweb.mecanica.dto.CarroCreateDto;
import com.projetoweb.mecanica.dto.CarroResponseDto;
import com.projetoweb.mecanica.services.CarroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/carros")
public class CarroController {

    @Autowired
    private CarroService carroService;

    @GetMapping
    public ResponseEntity<List<CarroResponseDto>> findAll() {

        List<CarroResponseDto> dtoList = carroService.findAll();
        return ResponseEntity.ok().body(dtoList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CarroResponseDto> findById(@PathVariable Long id) {

        CarroResponseDto dto = carroService.findById(id);
        return ResponseEntity.ok().body(dto);
    }

    @GetMapping("/placa/{placa}")
    public ResponseEntity<CarroResponseDto> findByPlaca(@PathVariable String placa) {

        CarroResponseDto obj = carroService.findByPlaca(placa);
        return ResponseEntity.ok().body(obj);
    }

    @PostMapping
    public ResponseEntity<CarroResponseDto> insert(@RequestBody CarroCreateDto carro) {

        CarroResponseDto dto = carroService.insert(carro);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(dto.getId()).toUri();
        return ResponseEntity.created(uri).body(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CarroResponseDto> update(@PathVariable Long id, @RequestBody CarroCreateDto obj) {

        CarroResponseDto dto = carroService.update(id, obj);
        return ResponseEntity.ok().body(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        carroService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
