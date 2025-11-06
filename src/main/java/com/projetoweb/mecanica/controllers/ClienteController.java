package com.projetoweb.mecanica.controllers;

import com.projetoweb.mecanica.dto.cliente_dto.ClienteCreateDto;
import com.projetoweb.mecanica.dto.cliente_dto.ClienteResponseDto;
import com.projetoweb.mecanica.services.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping
    public ResponseEntity<List<ClienteResponseDto>> findAll() {

        List<ClienteResponseDto> dtoList = clienteService.findAll();
        return ResponseEntity.ok().body(dtoList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponseDto> findById(@PathVariable Long id) {

        ClienteResponseDto dto = clienteService.findById(id);
        return ResponseEntity.ok().body(dto);
    }

    @GetMapping("/doc/{doc}")
    public ResponseEntity<ClienteResponseDto> findByDoc(@PathVariable String doc) {

        ClienteResponseDto obj = clienteService.findByDoc(doc);
        return ResponseEntity.ok().body(obj);
    }

    @PostMapping
    public ResponseEntity<ClienteResponseDto> insert(@RequestBody ClienteCreateDto cliente) {

        ClienteResponseDto dto = clienteService.insert(cliente);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(dto.getId()).toUri();
        return ResponseEntity.created(uri).body(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponseDto> update(@PathVariable Long id, @RequestBody ClienteCreateDto obj) {

        ClienteResponseDto dto = clienteService.update(id, obj);
        return ResponseEntity.ok().body(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        clienteService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/inativar/{id}")
    public ResponseEntity<Void> inativar(@PathVariable Long id) {
        clienteService.inativar(id);
        return ResponseEntity.noContent().build();
    }
}
