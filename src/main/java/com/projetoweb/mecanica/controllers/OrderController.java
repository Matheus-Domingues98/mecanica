package com.projetoweb.mecanica.controllers;

import com.projetoweb.mecanica.dto.OrderCreateDto;
import com.projetoweb.mecanica.dto.OrderDto;
import com.projetoweb.mecanica.entities.enums.OrderStatus;
import com.projetoweb.mecanica.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping
    public ResponseEntity<List<OrderDto>> findAll() {
        List<OrderDto> dtoList = orderService.findAll();
        return ResponseEntity.ok().body(dtoList);
    }

    @GetMapping("/ativos")
    public ResponseEntity<List<OrderDto>> findAllAtivos() {
        List<OrderDto> dtoList = orderService.findAllAtivos();
        return ResponseEntity.ok().body(dtoList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> findById(@PathVariable Long id) {
        OrderDto dto = orderService.findById(id);
        return ResponseEntity.ok().body(dto);
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<OrderDto>> findByClienteId(@PathVariable Long clienteId) {
        List<OrderDto> dtoList = orderService.findByClienteId(clienteId);
        return ResponseEntity.ok().body(dtoList);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<OrderDto>> findByStatus(@PathVariable OrderStatus status) {
        List<OrderDto> dtoList = orderService.findByStatus(status);
        return ResponseEntity.ok().body(dtoList);
    }

    @PostMapping
    public ResponseEntity<OrderDto> insert(@RequestBody OrderCreateDto dto) {
        OrderDto createdDto = orderService.insert(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdDto.getId())
                .toUri();
        return ResponseEntity.created(uri).body(createdDto);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<OrderDto> updateStatus(@PathVariable Long id, @RequestParam OrderStatus status) {
        OrderDto updatedDto = orderService.updateStatus(id, status);
        return ResponseEntity.ok().body(updatedDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        orderService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<OrderDto> cancelar(@PathVariable Long id) {
        OrderDto dto = orderService.cancelar(id);
        return ResponseEntity.ok().body(dto);
    }

    @PatchMapping("/{id}/desativar")
    public ResponseEntity<OrderDto> desativar(@PathVariable Long id) {
        OrderDto dto = orderService.desativar(id);
        return ResponseEntity.ok().body(dto);
    }

    @PatchMapping("/{id}/ativar")
    public ResponseEntity<OrderDto> ativar(@PathVariable Long id) {
        OrderDto dto = orderService.ativar(id);
        return ResponseEntity.ok().body(dto);
    }
}
