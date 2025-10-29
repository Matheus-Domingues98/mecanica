package com.projetoweb.mecanica.config;

import com.projetoweb.mecanica.entities.Carro;
import com.projetoweb.mecanica.entities.Cliente;
import com.projetoweb.mecanica.entities.Produto;
import com.projetoweb.mecanica.entities.Servico;
import com.projetoweb.mecanica.repositories.CarroRepository;
import com.projetoweb.mecanica.repositories.ClienteRepository;
import com.projetoweb.mecanica.repositories.ProdutoRepository;
import com.projetoweb.mecanica.repositories.ServicoRepository;
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


    @Override
    public void run(String... args) throws Exception {

        Cliente c1 = new Cliente(null, "Matheus", "123456789", "11912345678", "matheus@gmail.com");
        Cliente c2 = new Cliente(null, "Maria", "987654321", "11987654321", "maria@gmail.com");

        clienteRepository.saveAll(Arrays.asList(c1, c2));

        Carro car1 = new Carro(null, "Uno", "Fiat", 2020, "ABC-1234", "Vermelho", c1);
        Carro car2 = new Carro(null, "Gol", "Volkswagen", 2022, "DEF-5678", "Azul", c2);

        carroRepository.saveAll(Arrays.asList(car1, car2));

        Servico s1 = new Servico(null, "Trocar pneu", "Realizar a troca de pneu", 100.0, Duration.ofHours(2));
        Servico s2 = new Servico(null, "Trocar oleo", "Realizar troca periodica de oler", 500.0, Duration.ofHours(2));

        servicoRepository.saveAll(Arrays.asList(s1, s2));

        Produto p1 = new Produto(null, "Pneu", 100.0, 10);
        Produto p2 = new Produto(null, "Oleo",  500.0, 10);

        produtoRepository.saveAll(Arrays.asList(p1, p2));






    }

}
