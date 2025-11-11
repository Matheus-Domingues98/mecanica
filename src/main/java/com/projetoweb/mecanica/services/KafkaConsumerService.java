package com.projetoweb.mecanica.services;

import com.projetoweb.mecanica.config.KafkaConfig;
import com.projetoweb.mecanica.dto.OrderEventDto;
import com.projetoweb.mecanica.dto.PaymentEventDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumerService.class);

    @KafkaListener(topics = KafkaConfig.ORDER_CREATED_TOPIC, groupId = "${spring.kafka.consumer.group-id}")
    public void consumeOrderCreatedEvent(OrderEventDto orderEvent) {
        logger.info("Received order created event: {}", orderEvent);
        
        // Aqui você pode adicionar lógica de negócio
        // Por exemplo: enviar notificação, atualizar estoque, etc.
        processOrderCreated(orderEvent);
    }

    @KafkaListener(topics = KafkaConfig.ORDER_UPDATED_TOPIC, groupId = "${spring.kafka.consumer.group-id}")
    public void consumeOrderUpdatedEvent(OrderEventDto orderEvent) {
        logger.info("Received order updated event: {}", orderEvent);
        
        // Aqui você pode adicionar lógica de negócio
        processOrderUpdated(orderEvent);
    }

    @KafkaListener(topics = KafkaConfig.PAYMENT_PROCESSED_TOPIC, groupId = "${spring.kafka.consumer.group-id}")
    public void consumePaymentProcessedEvent(PaymentEventDto paymentEvent) {
        logger.info("Received payment processed event: {}", paymentEvent);
        
        // Aqui você pode adicionar lógica de negócio
        processPaymentProcessed(paymentEvent);
    }

    private void processOrderCreated(OrderEventDto orderEvent) {
        logger.info("Processing order created: orderId={}, status={}", 
            orderEvent.getOrderId(), orderEvent.getStatus());
        
        // Exemplo de lógica:
        // - Enviar email de confirmação
        // - Atualizar dashboard
        // - Notificar mecânicos disponíveis
    }

    private void processOrderUpdated(OrderEventDto orderEvent) {
        logger.info("Processing order updated: orderId={}, status={}", 
            orderEvent.getOrderId(), orderEvent.getStatus());
        
        // Exemplo de lógica:
        // - Notificar cliente sobre mudança de status
        // - Atualizar relatórios
    }

    private void processPaymentProcessed(PaymentEventDto paymentEvent) {
        logger.info("Processing payment: paymentId={}, orderId={}, status={}", 
            paymentEvent.getPaymentId(), paymentEvent.getOrderId(), paymentEvent.getStatus());
        
        // Exemplo de lógica:
        // - Atualizar status do pedido
        // - Gerar nota fiscal
        // - Enviar confirmação de pagamento
    }
}
