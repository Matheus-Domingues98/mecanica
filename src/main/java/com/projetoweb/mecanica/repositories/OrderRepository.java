package com.projetoweb.mecanica.repositories;

import com.projetoweb.mecanica.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
