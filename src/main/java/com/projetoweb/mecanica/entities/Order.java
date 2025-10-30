package com.projetoweb.mecanica.entities;

import com.projetoweb.mecanica.entities.enums.OrderStatus;
import jakarta.persistence.*;

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

    public Order() {
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



    //podeSerCancelada() - validar se pode cancelar com base no status
}
