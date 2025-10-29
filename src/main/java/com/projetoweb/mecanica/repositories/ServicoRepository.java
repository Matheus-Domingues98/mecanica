package com.projetoweb.mecanica.repositories;

import com.projetoweb.mecanica.entities.Servico;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServicoRepository extends JpaRepository<Servico, Long> {
}
