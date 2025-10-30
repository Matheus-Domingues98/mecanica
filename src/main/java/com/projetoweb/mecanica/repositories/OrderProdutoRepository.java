package com.projetoweb.mecanica.repositories;

import com.projetoweb.mecanica.entities.OrderProduto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderProdutoRepository extends JpaRepository<OrderProduto, Long> {
}
