# Kafka Integration - Mecânica API

## 📋 Visão Geral

Este projeto agora inclui integração completa com Apache Kafka para mensageria assíncrona e processamento de eventos.

## 🚀 Configuração do Kafka

### Pré-requisitos

1. **Instalar Kafka localmente:**

```bash
# macOS (usando Homebrew)
brew install kafka

# Ou baixar do site oficial
# https://kafka.apache.org/downloads
```

### Iniciar Kafka

```bash
# 1. Iniciar Zookeeper
zookeeper-server-start /usr/local/etc/kafka/zookeeper.properties

# 2. Em outro terminal, iniciar Kafka
kafka-server-start /usr/local/etc/kafka/server.properties
```

## 📡 Tópicos Configurados

- **order-created** - Eventos de criação de pedidos
- **order-updated** - Eventos de atualização de pedidos
- **payment-processed** - Eventos de processamento de pagamentos

## 🔧 Configuração

As configurações do Kafka estão em `application.properties`:

```properties
spring.kafka.bootstrap-servers=localhost:9092
spring.kafka.consumer.group-id=mecanica-group
spring.kafka.consumer.auto-offset-reset=earliest
```

## 📝 Como Usar

### 1. Testar Kafka com Endpoint Rápido

```bash
# Teste rápido (não requer autenticação se configurado)
curl -X GET http://localhost:8080/api/kafka/test/quick
```

### 2. Enviar Evento de Pedido Criado

```bash
curl -X POST http://localhost:8080/api/kafka/test/order-created \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {seu-token}" \
  -d '{
    "orderId": 1,
    "status": "CREATED",
    "clientName": "João Silva",
    "totalValue": 1500.00,
    "eventType": "ORDER_CREATED"
  }'
```

### 3. Enviar Evento de Pedido Atualizado

```bash
curl -X POST http://localhost:8080/api/kafka/test/order-updated \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {seu-token}" \
  -d '{
    "orderId": 1,
    "status": "IN_PROGRESS",
    "clientName": "João Silva",
    "totalValue": 1500.00,
    "eventType": "ORDER_UPDATED"
  }'
```

### 4. Enviar Evento de Pagamento Processado

```bash
curl -X POST http://localhost:8080/api/kafka/test/payment-processed \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {seu-token}" \
  -d '{
    "paymentId": 1,
    "orderId": 1,
    "paymentMethod": "CREDIT_CARD",
    "amount": 1500.00,
    "status": "APPROVED"
  }'
```

## 🎯 Integração com Serviços Existentes

### Exemplo: Enviar evento ao criar pedido

```java
@Service
public class OrderService {
    
    private final KafkaProducerService kafkaProducerService;
    
    public OrderDto createOrder(OrderDto orderDto) {
        // Salvar pedido no banco
        Order order = orderRepository.save(order);
        
        // Enviar evento para Kafka
        OrderEventDto event = new OrderEventDto(
            order.getId(),
            order.getStatus().toString(),
            order.getCliente().getNome(),
            order.getValorTotal(),
            "ORDER_CREATED"
        );
        kafkaProducerService.sendOrderCreatedEvent(event);
        
        return orderDto;
    }
}
```

## 📊 Monitoramento

### Ver mensagens nos tópicos (linha de comando)

```bash
# Ver mensagens do tópico order-created
kafka-console-consumer --bootstrap-server localhost:9092 \
  --topic order-created --from-beginning

# Ver mensagens do tópico order-updated
kafka-console-consumer --bootstrap-server localhost:9092 \
  --topic order-updated --from-beginning

# Ver mensagens do tópico payment-processed
kafka-console-consumer --bootstrap-server localhost:9092 \
  --topic payment-processed --from-beginning
```

## 🔍 Logs

Os eventos são logados automaticamente:
- **Producer**: Logs quando eventos são enviados
- **Consumer**: Logs quando eventos são recebidos e processados

Verifique os logs da aplicação para ver o fluxo de mensagens.

## 🛠️ Estrutura de Arquivos

```
src/main/java/com/projetoweb/mecanica/
├── config/
│   └── KafkaConfig.java              # Configuração de tópicos
├── dto/
│   ├── OrderEventDto.java            # DTO para eventos de pedido
│   └── PaymentEventDto.java          # DTO para eventos de pagamento
├── services/
│   ├── KafkaProducerService.java     # Serviço para enviar mensagens
│   └── KafkaConsumerService.java     # Serviço para receber mensagens
└── controllers/
    └── KafkaTestController.java      # Controller para testes
```

## 🚨 Troubleshooting

### Kafka não está conectando

1. Verifique se Zookeeper está rodando
2. Verifique se Kafka está rodando
3. Confirme que a porta 9092 está disponível

### Mensagens não estão sendo consumidas

1. Verifique os logs da aplicação
2. Confirme que o consumer group está configurado corretamente
3. Verifique se os tópicos foram criados: `kafka-topics --list --bootstrap-server localhost:9092`

## 📚 Próximos Passos

- Integrar eventos Kafka com os serviços existentes (OrderService, PaymentService)
- Adicionar tratamento de erros e retry logic
- Implementar Dead Letter Queue (DLQ) para mensagens com falha
- Adicionar métricas e monitoramento com Prometheus/Grafana
