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
        logger.info("Enviando evento de pedido criado: {}", orderEvent);
        CompletableFuture<SendResult<String, Object>> future = 
            kafkaTemplate.send(KafkaConfig.ORDER_CREATED_TOPIC, orderEvent.getOrderId().toString(), orderEvent);
        
        future.whenComplete((result, ex) -> {
            if (ex == null) {
                logger.info("Evento de pedido criado enviado com sucesso: orderId={}, offset={}", 
                    orderEvent.getOrderId(), result.getRecordMetadata().offset());
            } else {
                logger.error("Falha ao enviar evento de pedido criado: orderId={}", orderEvent.getOrderId(), ex);
            }
        });
    }

    public void sendOrderUpdatedEvent(OrderEventDto orderEvent) {
        logger.info("Enviando evento de pedido atualizado: {}", orderEvent);
        CompletableFuture<SendResult<String, Object>> future = 
            kafkaTemplate.send(KafkaConfig.ORDER_UPDATED_TOPIC, orderEvent.getOrderId().toString(), orderEvent);
        
        future.whenComplete((result, ex) -> {
            if (ex == null) {
                logger.info("Evento de pedido atualizado enviado com sucesso: orderId={}, offset={}", 
                    orderEvent.getOrderId(), result.getRecordMetadata().offset());
            } else {
                logger.error("Falha ao enviar evento de pedido atualizado: orderId={}", orderEvent.getOrderId(), ex);
            }
        });
    }

    public void sendPaymentProcessedEvent(PaymentEventDto paymentEvent) {
        logger.info("Enviando evento de pagamento processado: {}", paymentEvent);
        CompletableFuture<SendResult<String, Object>> future = 
            kafkaTemplate.send(KafkaConfig.PAYMENT_PROCESSED_TOPIC, paymentEvent.getPaymentId().toString(), paymentEvent);
        
        future.whenComplete((result, ex) -> {
            if (ex == null) {
                logger.info("Evento de pagamento processado enviado com sucesso: paymentId={}, offset={}", 
                    paymentEvent.getPaymentId(), result.getRecordMetadata().offset());
            } else {
                logger.error("Falha ao enviar evento de pagamento processado: paymentId={}", paymentEvent.getPaymentId(), ex);
            }
        });
    }
}
