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
        return ResponseEntity.ok("Order created event sent to Kafka");
    }

    @PostMapping("/test/order-updated")
    public ResponseEntity<String> testOrderUpdated(@RequestBody OrderEventDto orderEvent) {
        kafkaProducerService.sendOrderUpdatedEvent(orderEvent);
        return ResponseEntity.ok("Order updated event sent to Kafka");
    }

    @PostMapping("/test/payment-processed")
    public ResponseEntity<String> testPaymentProcessed(@RequestBody PaymentEventDto paymentEvent) {
        kafkaProducerService.sendPaymentProcessedEvent(paymentEvent);
        return ResponseEntity.ok("Payment processed event sent to Kafka");
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
        return ResponseEntity.ok("Quick test event sent! Check the logs to see the consumer receiving it.");
    }
}
