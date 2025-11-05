package com.projetoweb.mecanica.repositories;

import com.projetoweb.mecanica.entities.Carro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CarroRepository extends JpaRepository<Carro, Long> {

    // Spring Data JPA gera automaticamente a query: SELECT * FROM tb_carro WHERE placa = ?
    Optional<Carro> findByPlaca(String placa);
}
