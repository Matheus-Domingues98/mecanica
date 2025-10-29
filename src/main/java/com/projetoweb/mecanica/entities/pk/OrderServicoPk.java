package com.projetoweb.mecanica.entities.pk;

import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
public class OrderServicoPk {

    private Long orderId;
    private Long servicoId;

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getServicoId() {
        return servicoId;
    }

    public void setServicoId(Long servicoId) {
        this.servicoId = servicoId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        OrderServicoPk that = (OrderServicoPk) o;
        return Objects.equals(orderId, that.orderId) && Objects.equals(servicoId, that.servicoId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId, servicoId);
    }
}
