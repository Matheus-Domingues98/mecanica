package com.projetoweb.mecanica.mapper;

import com.projetoweb.mecanica.dto.OrderDto;
import com.projetoweb.mecanica.dto.OrderProdutoDto;
import com.projetoweb.mecanica.dto.OrderServicoDto;
import com.projetoweb.mecanica.entities.Order;
import com.projetoweb.mecanica.entities.OrderProduto;
import com.projetoweb.mecanica.entities.OrderServico;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OrderMapper {

    public static OrderDto toDto(Order order) {
        if (order == null) {
            return null;
        }

        Long clienteId = order.getCliente() != null ? order.getCliente().getId() : null;
        Long carroId = order.getCarro() != null ? order.getCarro().getId() : null;
        String carroPlaca = order.getCarro() != null ? order.getCarro().getPlaca() : null;
        String carroModelo = order.getCarro() != null ? order.getCarro().getModelo() : null;

        List<OrderProdutoDto> produtos = order.getOrderProdutos() != null
                ? order.getOrderProdutos().stream()
                .map(OrderMapper::toProdutoDto)
                .collect(Collectors.toList())
                : new ArrayList<>();

        List<OrderServicoDto> servicos = order.getOrderServicos() != null
                ? order.getOrderServicos().stream()
                .map(OrderMapper::toServicoDto)
                .collect(Collectors.toList())
                : new ArrayList<>();

        return new OrderDto(
                order.getId(),
                clienteId,
                carroId,
                carroPlaca,
                carroModelo,
                order.getStatus(),
                order.isAtivo(),
                produtos,
                servicos
        );
    }

    public static OrderProdutoDto toProdutoDto(OrderProduto orderProduto) {
        if (orderProduto == null) {
            return null;
        }

        Long orderId = orderProduto.getOrder() != null ? orderProduto.getOrder().getId() : null;
        Long produtoId = orderProduto.getProduto() != null ? orderProduto.getProduto().getIdProd() : null;

        return new OrderProdutoDto(
                orderId,
                produtoId,
                orderProduto.getNome(),
                orderProduto.getPreco(),
                orderProduto.getQuantidade(),
                orderProduto.subTotalProduto()
        );
    }

    public static OrderServicoDto toServicoDto(OrderServico orderServico) {
        if (orderServico == null) {
            return null;
        }

        Long orderId = orderServico.getOrder() != null ? orderServico.getOrder().getId() : null;
        Long servicoId = orderServico.getServico() != null ? orderServico.getServico().getIdServ() : null;
        Long duracaoMinutos = orderServico.getDuracao() != null ? orderServico.getDuracao().toMinutes() : null;

        return new OrderServicoDto(
                orderId,
                servicoId,
                orderServico.getNome(),
                orderServico.getDescricao(),
                orderServico.getPreco(),
                duracaoMinutos,
                orderServico.getQuantidade(),
                orderServico.subTotalServico()
        );
    }
}
