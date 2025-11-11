package com.projetoweb.mecanica.controllers;

import com.projetoweb.mecanica.dto.OrderEventDto;
import com.projetoweb.mecanica.dto.PaymentEventDto;
import com.projetoweb.mecanica.services.KafkaProducerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/kafka")
public class KafkaTestController {

    private final KafkaProducerService kafkaProducerService;

    public KafkaTestController(KafkaProducerService kafkaProducerService) {
        this.kafkaProducerService = kafkaProducerService;
    }

    @PostMapping("/test/order-created")
    public ResponseEntity<String> testOrderCreated(@RequestBody OrderEventDto orderEvent) {
        kafkaProducerService.sendOrderCreatedEvent(orderEvent);
        return ResponseEntity.ok("Evento de pedido criado enviado para o Kafka");
    }

    @PostMapping("/test/order-updated")
    public ResponseEntity<String> testOrderUpdated(@RequestBody OrderEventDto orderEvent) {
        kafkaProducerService.sendOrderUpdatedEvent(orderEvent);
        return ResponseEntity.ok("Evento de pedido atualizado enviado para o Kafka");
    }

    @PostMapping("/test/payment-processed")
    public ResponseEntity<String> testPaymentProcessed(@RequestBody PaymentEventDto paymentEvent) {
        kafkaProducerService.sendPaymentProcessedEvent(paymentEvent);
        return ResponseEntity.ok("Evento de pagamento processado enviado para o Kafka");
    }

    @GetMapping("/test/quick")
    public ResponseEntity<String> quickTest() {
        // Criar evento de teste rápido
        OrderEventDto orderEvent = new OrderEventDto(
            1L,
            "CREATED",
            "João Silva",
            1500.00,
            "ORDER_CREATED"
        );
        
        kafkaProducerService.sendOrderCreatedEvent(orderEvent);
        return ResponseEntity.ok("Evento de teste rápido enviado! Verifique os logs para ver o consumidor recebendo.");
    }

    @GetMapping("/test/order-created-sample")
    public ResponseEntity<String> testOrderCreatedSample() {
        OrderEventDto orderEvent = new OrderEventDto(
            100L,
            "CREATED",
            "Maria Santos",
            2500.00,
            "ORDER_CREATED"
        );
        kafkaProducerService.sendOrderCreatedEvent(orderEvent);
        return ResponseEntity.ok("✅ Evento de pedido criado enviado! ID do Pedido: 100, Cliente: Maria Santos, Valor: R$ 2500,00");
    }

    @GetMapping("/test/order-updated-sample")
    public ResponseEntity<String> testOrderUpdatedSample() {
        OrderEventDto orderEvent = new OrderEventDto(
            100L,
            "IN_PROGRESS",
            "Maria Santos",
            2500.00,
            "ORDER_UPDATED"
        );
        kafkaProducerService.sendOrderUpdatedEvent(orderEvent);
        return ResponseEntity.ok("✅ Evento de pedido atualizado enviado! ID do Pedido: 100, Status: EM_ANDAMENTO");
    }

    @GetMapping("/test/payment-processed-sample")
    public ResponseEntity<String> testPaymentProcessedSample() {
        PaymentEventDto paymentEvent = new PaymentEventDto(
            50L,
            100L,
            "CREDIT_CARD",
            2500.00,
            "APPROVED"
        );
        kafkaProducerService.sendPaymentProcessedEvent(paymentEvent);
        return ResponseEntity.ok("✅ Evento de pagamento processado enviado! ID do Pagamento: 50, ID do Pedido: 100, Valor: R$ 2500,00, Status: APROVADO");
    }

    @GetMapping("/test/all")
    public ResponseEntity<String> testAll() {
        // 1. Order Created
        OrderEventDto orderCreated = new OrderEventDto(
            999L,
            "CREATED",
            "Pedro Oliveira",
            3000.00,
            "ORDER_CREATED"
        );
        kafkaProducerService.sendOrderCreatedEvent(orderCreated);

        // 2. Order Updated
        OrderEventDto orderUpdated = new OrderEventDto(
            999L,
            "IN_PROGRESS",
            "Pedro Oliveira",
            3000.00,
            "ORDER_UPDATED"
        );
        kafkaProducerService.sendOrderUpdatedEvent(orderUpdated);

        // 3. Payment Processed
        PaymentEventDto payment = new PaymentEventDto(
            888L,
            999L,
            "PIX",
            3000.00,
            "APPROVED"
        );
        kafkaProducerService.sendPaymentProcessedEvent(payment);

        return ResponseEntity.ok("✅ Todos os 3 eventos enviados com sucesso! Verifique os logs para vê-los sendo processados.");
    }
}
