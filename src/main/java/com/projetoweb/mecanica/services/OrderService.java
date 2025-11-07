package com.projetoweb.mecanica.services;

import com.projetoweb.mecanica.dto.OrderCreateDto;
import com.projetoweb.mecanica.dto.OrderDto;
import com.projetoweb.mecanica.entities.*;
import com.projetoweb.mecanica.entities.enums.OrderStatus;
import com.projetoweb.mecanica.exceptions.BusinessException;
import com.projetoweb.mecanica.exceptions.EstoqueInsuficienteException;
import com.projetoweb.mecanica.exceptions.ResourceNotFoundException;
import com.projetoweb.mecanica.repositories.*;
import com.projetoweb.mecanica.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private ServicoRepository servicoRepository;

    @Autowired
    private CarroRepository carroRepository;

    @Transactional(readOnly = true)
    public List<OrderDto> findAll() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(OrderMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public OrderDto findById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID nao pode ser nulo");
        }
        Order entity = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "ID", id));
        return OrderMapper.toDto(entity);
    }

    @Transactional
    public OrderDto insert(OrderCreateDto dto) {
        if (dto == null) {
            throw new IllegalArgumentException("OrderCreateDto nao pode ser nulo");
        }
        if (dto.getClienteId() == null) {
            throw new IllegalArgumentException("ID do cliente nao pode ser nulo");
        }

        Cliente cliente = clienteRepository.findById(dto.getClienteId())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente", "ID", dto.getClienteId()));

        // Validar e buscar o carro
        if (dto.getCarroId() == null) {
            throw new IllegalArgumentException("ID do carro nao pode ser nulo");
        }
        Carro carro = carroRepository.findById(dto.getCarroId())
                .orElseThrow(() -> new ResourceNotFoundException("Carro", "ID", dto.getCarroId()));

        // Validar se o carro pertence ao cliente
        if (!carro.getCliente().getId().equals(cliente.getId())) {
            throw new BusinessException("O carro selecionado nao pertence ao cliente informado");
        }

        Order entity = new Order();
        entity.setCliente(cliente);
        entity.setCarro(carro);
        entity.setStatus(OrderStatus.RECEBIDO);
        entity.setAtivo(true);

        // Salvar primeiro para gerar o ID
        entity = orderRepository.save(entity);

        // Adicionar produtos
        if (dto.getProdutos() != null && !dto.getProdutos().isEmpty()) {
            for (OrderCreateDto.ItemProdutoDto item : dto.getProdutos()) {
                if (item.getProdutoId() == null) {
                    throw new IllegalArgumentException("ID do produto nao pode ser nulo");
                }
                if (item.getQuantidade() == null || item.getQuantidade() <= 0) {
                    throw new IllegalArgumentException("Quantidade do produto deve ser maior que zero");
                }

                Produto produto = produtoRepository.findById(item.getProdutoId())
                        .orElseThrow(() -> new ResourceNotFoundException("Produto", "ID", item.getProdutoId()));

                // Validar estoque
                if (produto.getEstoque() == null) {
                    throw new BusinessException("Produto sem estoque cadastrado: " + produto.getNomeProd());
                }
                
                if (!produto.getEstoque().temEstoqueSuficiente(item.getQuantidade())) {
                    throw new EstoqueInsuficienteException(
                            produto.getNomeProd(),
                            produto.getEstoque().getQuantidade(),
                            item.getQuantidade()
                    );
                }

                // Decrementar estoque
                produto.getEstoque().decrementar(item.getQuantidade());

                entity.adicionarProduto(produto, item.getQuantidade());
            }
        }

        // Adicionar serviços
        if (dto.getServicos() != null && !dto.getServicos().isEmpty()) {
            for (OrderCreateDto.ItemServicoDto item : dto.getServicos()) {
                if (item.getServicoId() == null) {
                    throw new IllegalArgumentException("ID do servico nao pode ser nulo");
                }
                if (item.getQuantidade() == null || item.getQuantidade() <= 0) {
                    throw new IllegalArgumentException("Quantidade do servico deve ser maior que zero");
                }

                Servico servico = servicoRepository.findById(item.getServicoId())
                        .orElseThrow(() -> new ResourceNotFoundException("Servico", "ID", item.getServicoId()));

                entity.adicionarServico(servico, item.getQuantidade());
            }
        }

        entity = orderRepository.save(entity);
        return OrderMapper.toDto(entity);
    }

    @Transactional
    public OrderDto updateStatus(Long id, OrderStatus novoStatus) {
        if (id == null) {
            throw new IllegalArgumentException("ID nao pode ser nulo");
        }
        if (novoStatus == null) {
            throw new IllegalArgumentException("Status nao pode ser nulo");
        }

        Order entity = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "ID", id));

        entity.setStatus(novoStatus);
        entity = orderRepository.save(entity);

        return OrderMapper.toDto(entity);
    }

    @Transactional
    public void delete(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID nao pode ser nulo");
        }
        orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido nao encontrado com ID: " + id));
        orderRepository.deleteById(id);
    }

    @Transactional
    public OrderDto cancelar(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID nao pode ser nulo");
        }

        Order entity = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "ID", id));

        entity.cancelar();
        entity = orderRepository.save(entity);

        return OrderMapper.toDto(entity);
    }

    @Transactional
    public OrderDto desativar(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID nao pode ser nulo");
        }

        Order entity = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "ID", id));

        entity.desativar();
        entity = orderRepository.save(entity);

        return OrderMapper.toDto(entity);
    }

    @Transactional
    public OrderDto ativar(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID nao pode ser nulo");
        }

        Order entity = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "ID", id));

        entity.ativar();
        entity = orderRepository.save(entity);

        return OrderMapper.toDto(entity);
    }

    @Transactional(readOnly = true)
    public List<OrderDto> findAllAtivos() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .filter(Order::isAtivo)
                .map(OrderMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<OrderDto> findByClienteId(Long clienteId) {
        if (clienteId == null) {
            throw new IllegalArgumentException("ID do cliente nao pode ser nulo");
        }

        List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .filter(order -> order.getCliente() != null && order.getCliente().getId().equals(clienteId))
                .map(OrderMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<OrderDto> findByStatus(OrderStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("Status nao pode ser nulo");
        }

        List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .filter(order -> order.getStatus() == status)
                .map(OrderMapper::toDto)
                .toList();
    }
}
