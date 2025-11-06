package com.projetoweb.mecanica.mapper;

import com.projetoweb.mecanica.dto.PagamentoDto;
import com.projetoweb.mecanica.entities.Pagamento;

public class PagamentoMapper {

    public static PagamentoDto toDto(Pagamento pagamento) {
        if (pagamento == null) {
            return null;
        }
        Long orderId = pagamento.getOrder() != null ? pagamento.getOrder().getId() : null;
        
        return new PagamentoDto(
                pagamento.getId(),
                pagamento.getValor(),
                pagamento.getFormaDePagamento(),
                orderId
        );
    }

    public static Pagamento toEntity(PagamentoDto dto) {
        if (dto == null) {
            return null;
        }
        Pagamento pagamento = new Pagamento();
        pagamento.setId(dto.getId());
        pagamento.setValor(dto.getValor());
        pagamento.setFormaDePagamento(dto.getFormaDePagamento());
        return pagamento;
    }
}
