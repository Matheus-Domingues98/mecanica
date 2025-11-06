package com.projetoweb.mecanica.controllers;

import com.projetoweb.mecanica.dto.PagamentoDto;
import com.projetoweb.mecanica.services.PagamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/pagamentos")
public class PagamentoController {

    @Autowired
    private PagamentoService pagamentoService;

    @GetMapping
    public ResponseEntity<List<PagamentoDto>> findAll() {
        List<PagamentoDto> dtoList = pagamentoService.findAll();
        return ResponseEntity.ok().body(dtoList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PagamentoDto> findById(@PathVariable Long id) {
        PagamentoDto dto = pagamentoService.findById(id);
        return ResponseEntity.ok().body(dto);
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<PagamentoDto> findByOrderId(@PathVariable Long orderId) {
        PagamentoDto dto = pagamentoService.findByOrderId(orderId);
        return ResponseEntity.ok().body(dto);
    }

    @PostMapping
    public ResponseEntity<PagamentoDto> insert(@RequestBody PagamentoDto dto) {
        PagamentoDto createdDto = pagamentoService.insert(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdDto.getId())
                .toUri();
        return ResponseEntity.created(uri).body(createdDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PagamentoDto> update(@PathVariable Long id, @RequestBody PagamentoDto dto) {
        PagamentoDto updatedDto = pagamentoService.update(id, dto);
        return ResponseEntity.ok().body(updatedDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        pagamentoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/recalcular")
    public ResponseEntity<PagamentoDto> recalcularValor(@PathVariable Long id) {
        PagamentoDto dto = pagamentoService.recalcularValor(id);
        return ResponseEntity.ok().body(dto);
    }
}
