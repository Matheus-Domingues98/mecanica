package com.projetoweb.mecanica.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import java.time.LocalDateTime;

public class OrderEventDto {

    private Long orderId;
    private String status;
    private String clientName;
    private Double totalValue;
    
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime eventTime;
    
    private String eventType;

    public OrderEventDto() {
    }

    public OrderEventDto(Long orderId, String status, String clientName, Double totalValue, String eventType) {
        this.orderId = orderId;
        this.status = status;
        this.clientName = clientName;
        this.totalValue = totalValue;
        this.eventTime = LocalDateTime.now();
        this.eventType = eventType;
    }

    // Getters and Setters
    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public Double getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(Double totalValue) {
        this.totalValue = totalValue;
    }

    public LocalDateTime getEventTime() {
        return eventTime;
    }

    public void setEventTime(LocalDateTime eventTime) {
        this.eventTime = eventTime;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    @Override
    public String toString() {
        return "OrderEventDto{" +
                "orderId=" + orderId +
                ", status='" + status + '\'' +
                ", clientName='" + clientName + '\'' +
                ", totalValue=" + totalValue +
                ", eventTime=" + eventTime +
                ", eventType='" + eventType + '\'' +
                '}';
    }
}
