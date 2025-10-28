package com.projetoweb.mecanica.config;

import com.projetoweb.mecanica.entities.Carro;
import com.projetoweb.mecanica.entities.Cliente;
import com.projetoweb.mecanica.repositories.CarroRepository;
import com.projetoweb.mecanica.repositories.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.lang.reflect.Array;
import java.util.Arrays;

@Configuration
@Profile("test")
public class TestConfig implements CommandLineRunner {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private CarroRepository carroRepository;


    @Override
    public void run(String... args) throws Exception {

        Cliente c1 = new Cliente(null, "Matheus", "123456789", "11912345678", "matheus@gmail.com");
        Cliente c2 = new Cliente(null, "Maria", "987654321", "11987654321", "maria@gmail.com");

        clienteRepository.saveAll(Arrays.asList(c1, c2));

        Carro car1 = new Carro(null, "Uno", "Fiat", 2020, "ABC-1234", "Vermelho", c1);
        Carro car2 = new Carro(null, "Gol", "Volkswagen", 2022, "DEF-5678", "Azul", c2);

        carroRepository.saveAll(Arrays.asList(car1, car2));




    }

}
