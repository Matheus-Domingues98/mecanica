package com.projetoweb.mecanica.controllers;

import com.projetoweb.mecanica.dto.ProdutoDto;
import com.projetoweb.mecanica.services.ProdutoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;

    @GetMapping
    public ResponseEntity<List<ProdutoDto>> findAll() {
        List<ProdutoDto> dtoList = produtoService.findAll();
        return ResponseEntity.ok().body(dtoList);
    }

    @GetMapping("/ativos")
    public ResponseEntity<List<ProdutoDto>> findAllAtivos() {
        List<ProdutoDto> dtoList = produtoService.findAllAtivos();
        return ResponseEntity.ok().body(dtoList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProdutoDto> findById(@PathVariable Long id) {
        ProdutoDto dto = produtoService.findById(id);
        return ResponseEntity.ok().body(dto);
    }

    @PostMapping
    public ResponseEntity<ProdutoDto> insert(@Valid @RequestBody ProdutoDto dto) {
        ProdutoDto createdDto = produtoService.insert(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdDto.getId())
                .toUri();
        return ResponseEntity.created(uri).body(createdDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProdutoDto> update(@PathVariable Long id, @Valid @RequestBody ProdutoDto dto) {
        ProdutoDto updatedDto = produtoService.update(id, dto);
        return ResponseEntity.ok().body(updatedDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        produtoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/desativar")
    public ResponseEntity<ProdutoDto> desativar(@PathVariable Long id) {
        ProdutoDto dto = produtoService.desativar(id);
        return ResponseEntity.ok().body(dto);
    }

    @PatchMapping("/{id}/ativar")
    public ResponseEntity<ProdutoDto> ativar(@PathVariable Long id) {
        ProdutoDto dto = produtoService.ativar(id);
        return ResponseEntity.ok().body(dto);
    }
}
