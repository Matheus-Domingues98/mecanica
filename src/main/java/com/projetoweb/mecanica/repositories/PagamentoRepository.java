package com.projetoweb.mecanica.repositories;

import com.projetoweb.mecanica.entities.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PagamentoRepository extends JpaRepository<Pagamento, Long> {
}
