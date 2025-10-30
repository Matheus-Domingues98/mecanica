package com.projetoweb.mecanica.repositories;

import com.projetoweb.mecanica.entities.Estoque;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EstoqueRepository extends JpaRepository<Estoque, Long> {
}
