package com.projetoweb.mecanica.entities;

import com.projetoweb.mecanica.entities.enums.OrderStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_order")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_order")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "carro_id", nullable = false)
    private Carro carro;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private OrderStatus status;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    private Pagamento pagamento;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderProduto> orderProdutos;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderServico> orderServicos;

    @Column(nullable = false)
    private boolean ativo = true;

    @Column(name = "valor_total", nullable = false)
    private Double valorTotal = 0.0;

    @Column(name = "orcamento_aprovado", nullable = false)
    private Boolean orcamentoAprovado = false;

    @Column(name = "data_aprovacao")
    private LocalDateTime dataAprovacao;

    @Column(name = "data_criacao", nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    @Column(name = "data_inicio_execucao")
    private LocalDateTime dataInicioExecucao;

    @Column(name = "data_finalizacao")
    private LocalDateTime dataFinalizacao;

    public Order() {
        this.dataCriacao = LocalDateTime.now();
    }

    public Order(Long id, Cliente cliente, Pagamento pagamento, OrderStatus status) {
        this.id = id;
        this.cliente = cliente;
        this.pagamento = pagamento;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Carro getCarro() {
        return carro;
    }

    public void setCarro(Carro carro) {
        this.carro = carro;
    }

    public Pagamento getPagamento() {
        return pagamento;
    }

    public void setPagamento(Pagamento pagamento) {
        this.pagamento = pagamento;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public List<OrderProduto> getOrderProdutos() {
        return orderProdutos;
    }

    public void setOrderProdutos(List<OrderProduto> orderProdutos) {
        this.orderProdutos = orderProdutos;
    }

    public List<OrderServico> getOrderServicos() {
        return orderServicos;
    }

    public void setOrderServicos(List<OrderServico> orderServicos) {
        this.orderServicos = orderServicos;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public Double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(Double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public Boolean getOrcamentoAprovado() {
        return orcamentoAprovado;
    }

    public void setOrcamentoAprovado(Boolean orcamentoAprovado) {
        this.orcamentoAprovado = orcamentoAprovado;
    }

    public LocalDateTime getDataAprovacao() {
        return dataAprovacao;
    }

    public void setDataAprovacao(LocalDateTime dataAprovacao) {
        this.dataAprovacao = dataAprovacao;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public LocalDateTime getDataInicioExecucao() {
        return dataInicioExecucao;
    }

    public void setDataInicioExecucao(LocalDateTime dataInicioExecucao) {
        this.dataInicioExecucao = dataInicioExecucao;
    }

    public LocalDateTime getDataFinalizacao() {
        return dataFinalizacao;
    }

    public void setDataFinalizacao(LocalDateTime dataFinalizacao) {
        this.dataFinalizacao = dataFinalizacao;
    }

    public void desativar() {
        this.ativo = false;
    }

    public void ativar() {
        this.ativo = true;
    }

    public void adicionarProduto(Produto produto, Integer quantidade) {

        if (produto == null) {
            throw new IllegalArgumentException("Produto não pode ser nulo");
        }
        if (quantidade == null || quantidade <=0) {
            throw new IllegalArgumentException("Quantidade deve ser maior que zero");
        }

        OrderProduto orderProduto = new OrderProduto(
                this, produto, produto.getNomeProd(),
                produto.getPrecoProd(), quantidade
        );

        if (this.orderProdutos == null) {
            this.orderProdutos = new ArrayList<>();
        }
        this.orderProdutos.add(orderProduto);
    }

    public void adicionarServico(Servico servico, Integer quantidade) {
        if (servico == null) {
            throw  new IllegalArgumentException("Servico não pode ser nulo");
        }
        if (servico == null || quantidade <= 0) {
            throw  new IllegalArgumentException("Quantidade deve ser maior que zero");
        }

        OrderServico orderServico = new OrderServico(
                this, servico, servico.getNomeServ(),
                servico.getPrecoServ(), quantidade,
                servico.getDescricaoServ(),servico.getDuracaoServ()
                );
        if (this.orderServicos == null) {
            this.orderServicos = new ArrayList<>();
        }
        this.orderServicos.add(orderServico);
    }

    public boolean podeSerCancelada() {
        return this.status == OrderStatus.RECEBIDO ||
                this.status == OrderStatus.EM_DIAGNOSTICO ||
                this.status == OrderStatus.AGUARDANDO_APROVACAO;
    }

    public void cancelar() {
        if (!podeSerCancelada()) {
            throw new IllegalStateException("Ordem não pode cancelada no status: " + this.status);
        }
        this.desativar();
        this.status = OrderStatus.CANCELADO;
    }

    /**
     * Calcula o valor total do orçamento somando produtos e serviços
     */
    public void calcularValorTotal() {
        double totalProdutos = 0.0;
        double totalServicos = 0.0;

        if (this.orderProdutos != null) {
            totalProdutos = this.orderProdutos.stream()
                    .mapToDouble(OrderProduto::subTotalProduto)
                    .sum();
        }

        if (this.orderServicos != null) {
            totalServicos = this.orderServicos.stream()
                    .mapToDouble(OrderServico::subTotalServico)
                    .sum();
        }

        this.valorTotal = totalProdutos + totalServicos;
    }

    /**
     * Aprova o orçamento e muda o status para EM_EXECUCAO
     */
    public void aprovarOrcamento() {
        if (this.status != OrderStatus.AGUARDANDO_APROVACAO) {
            throw new IllegalStateException(
                    "Apenas ordens com status AGUARDANDO_APROVACAO podem ser aprovadas. Status atual: " + this.status
            );
        }
        if (this.orcamentoAprovado) {
            throw new IllegalStateException("Orçamento já foi aprovado anteriormente");
        }

        this.orcamentoAprovado = true;
        this.dataAprovacao = LocalDateTime.now();
        this.dataInicioExecucao = LocalDateTime.now();
        this.status = OrderStatus.EM_EXECUCAO;
    }

    /**
     * Rejeita o orçamento e cancela a ordem
     */
    public void rejeitarOrcamento() {
        if (this.status != OrderStatus.AGUARDANDO_APROVACAO) {
            throw new IllegalStateException(
                    "Apenas ordens com status AGUARDANDO_APROVACAO podem ser rejeitadas. Status atual: " + this.status
            );
        }

        this.orcamentoAprovado = false;
        this.status = OrderStatus.CANCELADO;
        this.desativar();
    }

    /**
     * Finaliza a ordem de serviço
     */
    public void finalizar() {
        if (this.status != OrderStatus.EM_EXECUCAO) {
            throw new IllegalStateException(
                    "Apenas ordens com status EM_EXECUCAO podem ser finalizadas. Status atual: " + this.status
            );
        }

        this.dataFinalizacao = LocalDateTime.now();
        this.status = OrderStatus.FINALIZADO;
    }

    /**
     * Marca a ordem como entregue
     */
    public void entregar() {
        if (this.status != OrderStatus.FINALIZADO) {
            throw new IllegalStateException(
                    "Apenas ordens com status FINALIZADO podem ser entregues. Status atual: " + this.status
            );
        }

        this.status = OrderStatus.ENTREGUE;
    }
}
