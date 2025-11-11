package com.projetoweb.mecanica.services;

import com.projetoweb.mecanica.config.KafkaConfig;
import com.projetoweb.mecanica.dto.OrderEventDto;
import com.projetoweb.mecanica.dto.PaymentEventDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class KafkaProducerService {

    private static final Logger logger = LoggerFactory.getLogger(KafkaProducerService.class);

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public KafkaProducerService(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendOrderCreatedEvent(OrderEventDto orderEvent) {
        logger.info("Sending order created event: {}", orderEvent);
        CompletableFuture<SendResult<String, Object>> future = 
            kafkaTemplate.send(KafkaConfig.ORDER_CREATED_TOPIC, orderEvent.getOrderId().toString(), orderEvent);
        
        future.whenComplete((result, ex) -> {
            if (ex == null) {
                logger.info("Order created event sent successfully: orderId={}, offset={}", 
                    orderEvent.getOrderId(), result.getRecordMetadata().offset());
            } else {
                logger.error("Failed to send order created event: orderId={}", orderEvent.getOrderId(), ex);
            }
        });
    }

    public void sendOrderUpdatedEvent(OrderEventDto orderEvent) {
        logger.info("Sending order updated event: {}", orderEvent);
        CompletableFuture<SendResult<String, Object>> future = 
            kafkaTemplate.send(KafkaConfig.ORDER_UPDATED_TOPIC, orderEvent.getOrderId().toString(), orderEvent);
        
        future.whenComplete((result, ex) -> {
            if (ex == null) {
                logger.info("Order updated event sent successfully: orderId={}, offset={}", 
                    orderEvent.getOrderId(), result.getRecordMetadata().offset());
            } else {
                logger.error("Failed to send order updated event: orderId={}", orderEvent.getOrderId(), ex);
            }
        });
    }

    public void sendPaymentProcessedEvent(PaymentEventDto paymentEvent) {
        logger.info("Sending payment processed event: {}", paymentEvent);
        CompletableFuture<SendResult<String, Object>> future = 
            kafkaTemplate.send(KafkaConfig.PAYMENT_PROCESSED_TOPIC, paymentEvent.getPaymentId().toString(), paymentEvent);
        
        future.whenComplete((result, ex) -> {
            if (ex == null) {
                logger.info("Payment processed event sent successfully: paymentId={}, offset={}", 
                    paymentEvent.getPaymentId(), result.getRecordMetadata().offset());
            } else {
                logger.error("Failed to send payment processed event: paymentId={}", paymentEvent.getPaymentId(), ex);
            }
        });
    }
}
