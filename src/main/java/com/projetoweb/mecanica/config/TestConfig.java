package com.projetoweb.mecanica.config;

import com.projetoweb.mecanica.entities.*;
import com.projetoweb.mecanica.entities.enums.FormaDePagamento;
import com.projetoweb.mecanica.entities.enums.OrderStatus;
import com.projetoweb.mecanica.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.lang.reflect.Array;
import java.time.Duration;
import java.util.Arrays;

@Configuration
@Profile("test")
public class TestConfig implements CommandLineRunner {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private CarroRepository carroRepository;

    @Autowired
    private ServicoRepository servicoRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private EstoqueRepository estoqueRepository;

    @Autowired
    private OrderProdutoRepository orderProdutoRepository;

    @Autowired
    private OrderServicoRepository orderServicoRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private PagamentoRepository pagamentoRepository;

    @Override
    public void run(String... args) throws Exception {

        Cliente c1 = new Cliente(null, "Matheus", "123456789", "11912345678", "matheus@gmail.com");
        Cliente c2 = new Cliente(null, "Maria", "987654321", "11987654321", "maria@gmail.com");

        clienteRepository.saveAll(Arrays.asList(c1, c2));

        Carro car1 = new Carro(null, "Uno", "Fiat", 2020, "ABC-1234", "Vermelho", c1);
        Carro car2 = new Carro(null, "Gol", "Volkswagen", 2022, "DEF-5678", "Azul", c2);

        carroRepository.saveAll(Arrays.asList(car1, car2));

        Servico s1 = new Servico(null, "Trocar pneu", "Realizar a troca de pneu", 100.0, Duration.ofHours(2).plusMinutes(30));
        Servico s2 = new Servico(null, "Trocar oleo", "Realizar troca periodica de oler", 500.0, Duration.ofHours(2).plusMinutes(25));

        servicoRepository.saveAll(Arrays.asList(s1, s2));

        Produto p1 = new Produto(null, "Pneu", 100.0, null);
        Produto p2 = new Produto(null, "Oleo", 500.0, null);

        Estoque estoque1 = new Estoque(null, 10, p1);
        Estoque estoque2 = new Estoque(null, 20, p2);
        p1.setEstoque(estoque1);
        p2.setEstoque(estoque2);

        p1 = produtoRepository.save(p1);
        p2 = produtoRepository.save(p2);

        // Criar Order primeiro
        Order order1 = new Order(null, c1, null, OrderStatus.RECEBIDO);
        order1 = orderRepository.save(order1);  // Salvar order primeiro
        
        // Criar OrderProduto manualmente
        OrderProduto op1 = new OrderProduto(order1, p1, p1.getNomeProd(), p1.getPrecoProd(), 2);
        OrderProduto op2 = new OrderProduto(order1, p2, p2.getNomeProd(), p2.getPrecoProd(), 1);
        
        orderProdutoRepository.saveAll(Arrays.asList(op1, op2));
        
        // Criar OrderServico manualmente
        OrderServico os1 = new OrderServico(order1, s1, s1.getNomeServ(), s1.getPrecoServ(), 1, 
                                            s1.getDescricaoServ(), s1.getDuracaoServ());
        OrderServico os2 = new OrderServico(order1, s2, s2.getNomeServ(), s2.getPrecoServ(), 1,
                                            s2.getDescricaoServ(), s2.getDuracaoServ());
        
        orderServicoRepository.saveAll(Arrays.asList(os1, os2));
        
        // Calcular valor total manualmente
        double valorTotal = op1.subTotalProduto() + op2.subTotalProduto() + 
                           os1.subTotalServico() + os2.subTotalServico();
        
        // Criar pagamento com valor calculado
        Pagamento pag1 = new Pagamento();
        pag1.setFormaDePagamento(FormaDePagamento.PIX);
        pag1.setValor(valorTotal);
        pag1.setOrder(order1);
        
        // Salvar pagamento
        pagamentoRepository.save(pag1);




    }

}
