package com.projetoweb.mecanica.repositories;

import com.projetoweb.mecanica.entities.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
}
