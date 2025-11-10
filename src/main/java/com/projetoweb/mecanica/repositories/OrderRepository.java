package com.projetoweb.mecanica.repositories;

import com.projetoweb.mecanica.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT o FROM Order o WHERE o.cliente.id = :clienteId")
    List<Order> findByClienteId(@Param("clienteId") Long clienteId);
}
