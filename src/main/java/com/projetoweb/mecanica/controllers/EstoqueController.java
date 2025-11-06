package com.projetoweb.mecanica.controllers;

import com.projetoweb.mecanica.dto.EstoqueDto;
import com.projetoweb.mecanica.services.EstoqueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/estoques")
public class EstoqueController {

    @Autowired
    private EstoqueService estoqueService;

    @GetMapping
    public ResponseEntity<List<EstoqueDto>> findAll() {
        List<EstoqueDto> dtoList = estoqueService.findAll();
        return ResponseEntity.ok().body(dtoList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EstoqueDto> findById(@PathVariable Long id) {
        EstoqueDto dto = estoqueService.findById(id);
        return ResponseEntity.ok().body(dto);
    }

    @PostMapping
    public ResponseEntity<EstoqueDto> insert(@RequestBody EstoqueDto dto) {
        EstoqueDto createdDto = estoqueService.insert(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdDto.getId())
                .toUri();
        return ResponseEntity.created(uri).body(createdDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EstoqueDto> update(@PathVariable Long id, @RequestBody EstoqueDto dto) {
        EstoqueDto updatedDto = estoqueService.update(id, dto);
        return ResponseEntity.ok().body(updatedDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        estoqueService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/vender")
    public ResponseEntity<EstoqueDto> venderProduto(@PathVariable Long id, @RequestParam Integer quantidade) {
        EstoqueDto dto = estoqueService.venderProduto(id, quantidade);
        return ResponseEntity.ok().body(dto);
    }

    @PatchMapping("/{id}/adicionar")
    public ResponseEntity<EstoqueDto> adicionarProduto(@PathVariable Long id, @RequestParam Integer quantidade) {
        EstoqueDto dto = estoqueService.adicionarProduto(id, quantidade);
        return ResponseEntity.ok().body(dto);
    }

    @GetMapping("/{id}/verificar-estoque")
    public ResponseEntity<Boolean> temEstoqueSuficiente(@PathVariable Long id, @RequestParam Integer quantidade) {
        boolean suficiente = estoqueService.temEstoqueSuficiente(id, quantidade);
        return ResponseEntity.ok().body(suficiente);
    }
}
