package com.projetoweb.mecanica.services;

import com.projetoweb.mecanica.dto.OrderCreateDto;
import com.projetoweb.mecanica.dto.OrderDto;
import com.projetoweb.mecanica.dto.OrderStatisticsDto;
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

import java.time.Duration;
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
        // Spring converte automaticamente path variable, não precisa validar null
        Order entity = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "ID", id));
        return OrderMapper.toDto(entity);
    }

    @Transactional
    public OrderDto insert(OrderCreateDto dto) {
        // Bean Validation já garante que dto, clienteId e carroId não são nulos
        
        Cliente cliente = clienteRepository.findById(dto.getClienteId())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente", "ID", dto.getClienteId()));

        Carro carro = carroRepository.findById(dto.getCarroId())
                .orElseThrow(() -> new ResourceNotFoundException("Carro", "ID", dto.getCarroId()));

        // Validação de negócio: carro pertence ao cliente
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
                // Bean Validation já garante que produtoId e quantidade são válidos
                
                Produto produto = produtoRepository.findById(item.getProdutoId())
                        .orElseThrow(() -> new ResourceNotFoundException("Produto", "ID", item.getProdutoId()));

                // Validações de negócio: estoque
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

                produto.getEstoque().decrementar(item.getQuantidade());
                entity.adicionarProduto(produto, item.getQuantidade());
            }
        }

        // Adicionar serviços
        if (dto.getServicos() != null && !dto.getServicos().isEmpty()) {
            for (OrderCreateDto.ItemServicoDto item : dto.getServicos()) {
                // Bean Validation já garante que servicoId e quantidade são válidos
                
                Servico servico = servicoRepository.findById(item.getServicoId())
                        .orElseThrow(() -> new ResourceNotFoundException("Servico", "ID", item.getServicoId()));

                entity.adicionarServico(servico, item.getQuantidade());
            }
        }

        // Calcula o valor total do orçamento automaticamente
        entity.calcularValorTotal();
        
        entity = orderRepository.save(entity);
        return OrderMapper.toDto(entity);
    }

    @Transactional
    public OrderDto updateStatus(Long id, OrderStatus novoStatus) {
        // Path variable e request param são convertidos automaticamente pelo Spring
        Order entity = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "ID", id));

        entity.setStatus(novoStatus);
        entity = orderRepository.save(entity);

        return OrderMapper.toDto(entity);
    }

    @Transactional
    public void delete(Long id) {
        Order entity = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "ID", id));
        orderRepository.deleteById(id);
    }

    @Transactional
    public OrderDto cancelar(Long id) {
        Order entity = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "ID", id));

        if (entity.getOrderProdutos() != null) {
            for (OrderProduto op : entity.getOrderProdutos()) {
                if (op.getProduto().getEstoque() != null) {
                    op.getProduto().getEstoque().incrementar(op.getQuantidade());
                }
            }
        }
        entity.cancelar();
        entity = orderRepository.save(entity);

        return OrderMapper.toDto(entity);
    }

    @Transactional
    public OrderDto desativar(Long id) {
        Order entity = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "ID", id));

        entity.desativar();
        entity = orderRepository.save(entity);

        return OrderMapper.toDto(entity);
    }

    @Transactional
    public OrderDto ativar(Long id) {
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
        List<Order> orders = orderRepository.findByClienteId(clienteId);
        return orders.stream().map(OrderMapper::toDto).toList();
    }

    @Transactional(readOnly = true)
    public List<OrderDto> findByStatus(OrderStatus status) {
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .filter(order -> order.getStatus() == status)
                .map(OrderMapper::toDto)
                .toList();
    }

    @Transactional
    public OrderDto aprovarOrcamento(Long id) {
        Order entity = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "ID", id));

        entity.aprovarOrcamento();
        entity = orderRepository.save(entity);

        return OrderMapper.toDto(entity);
    }

    @Transactional
    public OrderDto rejeitarOrcamento(Long id) {
        Order entity = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "ID", id));

        // Devolve os produtos ao estoque antes de rejeitar
        if (entity.getOrderProdutos() != null) {
            for (OrderProduto op : entity.getOrderProdutos()) {
                if (op.getProduto().getEstoque() != null) {
                    op.getProduto().getEstoque().incrementar(op.getQuantidade());
                }
            }
        }

        entity.rejeitarOrcamento();
        entity = orderRepository.save(entity);

        return OrderMapper.toDto(entity);
    }

    @Transactional
    public OrderDto enviarParaAprovacao(Long id) {
        Order entity = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "ID", id));

        if (entity.getStatus() != OrderStatus.EM_DIAGNOSTICO) {
            throw new BusinessException(
                    "Apenas ordens com status EM_DIAGNOSTICO podem ser enviadas para aprovação. Status atual: " + entity.getStatus()
            );
        }

        entity.setStatus(OrderStatus.AGUARDANDO_APROVACAO);
        entity = orderRepository.save(entity);

        return OrderMapper.toDto(entity);
    }

    @Transactional
    public OrderDto finalizar(Long id) {
        Order entity = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "ID", id));

        entity.finalizar();
        entity = orderRepository.save(entity);

        return OrderMapper.toDto(entity);
    }

    @Transactional
    public OrderDto entregar(Long id) {
        Order entity = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "ID", id));

        entity.entregar();
        entity = orderRepository.save(entity);

        return OrderMapper.toDto(entity);
    }

    @Transactional(readOnly = true)
    public OrderStatisticsDto getStatistics() {
        List<Order> allOrders = orderRepository.findAll();

        long totalOrdens = allOrders.size();
        long ordensFinalizadas = allOrders.stream()
                .filter(o -> o.getStatus() == OrderStatus.FINALIZADO || o.getStatus() == OrderStatus.ENTREGUE)
                .count();
        long ordensEmAndamento = allOrders.stream()
                .filter(o -> o.getStatus() == OrderStatus.RECEBIDO || 
                             o.getStatus() == OrderStatus.EM_DIAGNOSTICO || 
                             o.getStatus() == OrderStatus.AGUARDANDO_APROVACAO || 
                             o.getStatus() == OrderStatus.EM_EXECUCAO)
                .count();
        long ordensCanceladas = allOrders.stream()
                .filter(o -> o.getStatus() == OrderStatus.CANCELADO)
                .count();

        // Calcula tempo médio de execução (dataInicioExecucao até dataFinalizacao)
        Double tempoMedioExecucao = allOrders.stream()
                .filter(o -> o.getDataInicioExecucao() != null && o.getDataFinalizacao() != null)
                .mapToLong(o -> Duration.between(o.getDataInicioExecucao(), o.getDataFinalizacao()).toMinutes())
                .average()
                .orElse(0.0);

        // Calcula tempo médio total (dataCriacao até dataFinalizacao)
        Double tempoMedioTotal = allOrders.stream()
                .filter(o -> o.getDataCriacao() != null && o.getDataFinalizacao() != null)
                .mapToLong(o -> Duration.between(o.getDataCriacao(), o.getDataFinalizacao()).toMinutes())
                .average()
                .orElse(0.0);

        return new OrderStatisticsDto(
                totalOrdens,
                ordensFinalizadas,
                ordensEmAndamento,
                ordensCanceladas,
                tempoMedioExecucao,
                tempoMedioTotal
        );
    }
}
