package com.projetoweb.mecanica.repositories;

import com.projetoweb.mecanica.entities.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    @Query("SELECT c FROM Cliente c WHERE c.doc = :doc")
    Optional<Cliente> findByDoc(@Param("doc") String doc);
    
    @Query("SELECT c FROM Cliente c WHERE c.email = :email")
    Optional<Cliente> findByEmail(@Param("email") String email);
}
