package com.projetoweb.mecanica.controllers;

import com.projetoweb.mecanica.dto.ServicoDto;
import com.projetoweb.mecanica.services.ServicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/servicos")
public class ServicoController {

    @Autowired
    private ServicoService servicoService;

    @GetMapping
    public ResponseEntity<List<ServicoDto>> findAll() {
        List<ServicoDto> dtoList = servicoService.findAll();
        return ResponseEntity.ok().body(dtoList);
    }

    @GetMapping("/ativos")
    public ResponseEntity<List<ServicoDto>> findAllAtivos() {
        List<ServicoDto> dtoList = servicoService.findAllAtivos();
        return ResponseEntity.ok().body(dtoList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServicoDto> findById(@PathVariable Long id) {
        ServicoDto dto = servicoService.findById(id);
        return ResponseEntity.ok().body(dto);
    }

    @PostMapping
    public ResponseEntity<ServicoDto> insert(@RequestBody ServicoDto dto) {
        ServicoDto createdDto = servicoService.insert(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdDto.getId())
                .toUri();
        return ResponseEntity.created(uri).body(createdDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServicoDto> update(@PathVariable Long id, @RequestBody ServicoDto dto) {
        ServicoDto updatedDto = servicoService.update(id, dto);
        return ResponseEntity.ok().body(updatedDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        servicoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/desativar")
    public ResponseEntity<ServicoDto> desativar(@PathVariable Long id) {
        ServicoDto dto = servicoService.desativar(id);
        return ResponseEntity.ok().body(dto);
    }

    @PatchMapping("/{id}/ativar")
    public ResponseEntity<ServicoDto> ativar(@PathVariable Long id) {
        ServicoDto dto = servicoService.ativar(id);
        return ResponseEntity.ok().body(dto);
    }
}
