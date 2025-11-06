package com.projetoweb.mecanica.services;

import com.projetoweb.mecanica.dto.PagamentoDto;
import com.projetoweb.mecanica.entities.Order;
import com.projetoweb.mecanica.entities.Pagamento;
import com.projetoweb.mecanica.repositories.OrderRepository;
import com.projetoweb.mecanica.repositories.PagamentoRepository;
import com.projetoweb.mecanica.mapper.PagamentoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PagamentoService {

    @Autowired
    private PagamentoRepository pagamentoRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Transactional(readOnly = true)
    public List<PagamentoDto> findAll() {
        List<Pagamento> pagamentos = pagamentoRepository.findAll();
        return pagamentos.stream()
                .map(PagamentoMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public PagamentoDto findById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID nao pode ser nulo");
        }
        Pagamento entity = pagamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pagamento nao encontrado com ID: " + id));
        return PagamentoMapper.toDto(entity);
    }

    @Transactional
    public PagamentoDto insert(PagamentoDto dto) {
        if (dto == null) {
            throw new IllegalArgumentException("PagamentoDto nao pode ser nulo");
        }
        if (dto.getFormaDePagamento() == null) {
            throw new IllegalArgumentException("Forma de pagamento nao pode ser nula");
        }
        if (dto.getOrderId() == null) {
            throw new IllegalArgumentException("ID do pedido nao pode ser nulo");
        }

        Order order = orderRepository.findById(dto.getOrderId())
                .orElseThrow(() -> new RuntimeException("Pedido nao encontrado com ID: " + dto.getOrderId()));

        Pagamento entity = new Pagamento();
        entity.setFormaDePagamento(dto.getFormaDePagamento());
        entity.setOrder(order);
        entity.setValor(entity.calcularValor());

        entity = pagamentoRepository.save(entity);
        return PagamentoMapper.toDto(entity);
    }

    @Transactional
    public PagamentoDto update(Long id, PagamentoDto dto) {
        if (id == null) {
            throw new IllegalArgumentException("ID nao pode ser nulo");
        }
        if (dto == null) {
            throw new IllegalArgumentException("PagamentoDto nao pode ser nulo");
        }
        if (dto.getFormaDePagamento() == null) {
            throw new IllegalArgumentException("Forma de pagamento nao pode ser nula");
        }

        Pagamento entity = pagamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pagamento nao encontrado com ID: " + id));

        entity.setFormaDePagamento(dto.getFormaDePagamento());
        entity.setValor(entity.calcularValor());

        entity = pagamentoRepository.save(entity);
        return PagamentoMapper.toDto(entity);
    }

    @Transactional
    public void delete(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID nao pode ser nulo");
        }
        pagamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pagamento nao encontrado com ID: " + id));
        pagamentoRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public PagamentoDto findByOrderId(Long orderId) {
        if (orderId == null) {
            throw new IllegalArgumentException("ID do pedido nao pode ser nulo");
        }
        
        List<Pagamento> pagamentos = pagamentoRepository.findAll();
        Pagamento pagamento = pagamentos.stream()
                .filter(p -> p.getOrder() != null && p.getOrder().getId().equals(orderId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Pagamento nao encontrado para o pedido ID: " + orderId));
        
        return PagamentoMapper.toDto(pagamento);
    }

    @Transactional
    public PagamentoDto recalcularValor(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID nao pode ser nulo");
        }

        Pagamento entity = pagamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pagamento nao encontrado com ID: " + id));

        entity.setValor(entity.calcularValor());
        entity = pagamentoRepository.save(entity);

        return PagamentoMapper.toDto(entity);
    }
}
