# Guia de Testes - Kafka Integration

## ✅ Pré-requisitos

1. **Kafka rodando:**
```bash
brew services list
# Deve mostrar kafka como "started"
```

2. **MySQL rodando** (para a aplicação iniciar)

3. **Aplicação Spring Boot rodando** na porta 8080

---

## 🧪 Testes Disponíveis

### 1. Teste Rápido (Sem Autenticação)

```bash
curl http://localhost:8080/api/kafka/test/quick
```

**Resposta esperada:**
```
Quick test event sent! Check the logs to see the consumer receiving it.
```

**O que acontece:**
- Envia um evento de pedido criado para o Kafka
- O consumer recebe automaticamente e processa
- Verifique os logs da aplicação para ver o fluxo completo

---

### 2. Teste de Evento de Pedido Criado

```bash
curl -X POST http://localhost:8080/api/kafka/test/order-created \
  -H "Content-Type: application/json" \
  -d '{
    "orderId": 123,
    "status": "CREATED",
    "clientName": "Maria Santos",
    "totalValue": 2500.00,
    "eventType": "ORDER_CREATED"
  }'
```

**Resposta esperada:**
```
Order created event sent to Kafka
```

---

### 3. Teste de Evento de Pedido Atualizado

```bash
curl -X POST http://localhost:8080/api/kafka/test/order-updated \
  -H "Content-Type: application/json" \
  -d '{
    "orderId": 123,
    "status": "IN_PROGRESS",
    "clientName": "Maria Santos",
    "totalValue": 2500.00,
    "eventType": "ORDER_UPDATED"
  }'
```

---

### 4. Teste de Evento de Pagamento Processado

```bash
curl -X POST http://localhost:8080/api/kafka/test/payment-processed \
  -H "Content-Type: application/json" \
  -d '{
    "paymentId": 456,
    "orderId": 123,
    "paymentMethod": "CREDIT_CARD",
    "amount": 2500.00,
    "status": "APPROVED"
  }'
```

---

## 📊 Verificar Logs da Aplicação

Após enviar eventos, você verá nos logs:

### Producer (Enviando):
```
INFO  KafkaProducerService : Sending order created event: OrderEventDto{orderId=123...}
INFO  KafkaProducerService : Order created event sent successfully: orderId=123, offset=0
```

### Consumer (Recebendo):
```
INFO  KafkaConsumerService : Received order created event: OrderEventDto{orderId=123...}
INFO  KafkaConsumerService : Processing order created: orderId=123, status=CREATED
```

---

## 🔍 Verificar Tópicos do Kafka

### Listar todos os tópicos:
```bash
kafka-topics --list --bootstrap-server localhost:9092
```

**Deve mostrar:**
- order-created
- order-updated
- payment-processed

### Ver mensagens em um tópico:
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

---

## 🧪 Teste Completo - Fluxo de Pedido

Execute em sequência para simular um fluxo completo:

```bash
# 1. Criar pedido
curl -X POST http://localhost:8080/api/kafka/test/order-created \
  -H "Content-Type: application/json" \
  -d '{
    "orderId": 999,
    "status": "CREATED",
    "clientName": "João Silva",
    "totalValue": 1500.00,
    "eventType": "ORDER_CREATED"
  }'

# 2. Atualizar status do pedido
curl -X POST http://localhost:8080/api/kafka/test/order-updated \
  -H "Content-Type: application/json" \
  -d '{
    "orderId": 999,
    "status": "IN_PROGRESS",
    "clientName": "João Silva",
    "totalValue": 1500.00,
    "eventType": "ORDER_UPDATED"
  }'

# 3. Processar pagamento
curl -X POST http://localhost:8080/api/kafka/test/payment-processed \
  -H "Content-Type: application/json" \
  -d '{
    "paymentId": 777,
    "orderId": 999,
    "paymentMethod": "PIX",
    "amount": 1500.00,
    "status": "APPROVED"
  }'

# 4. Finalizar pedido
curl -X POST http://localhost:8080/api/kafka/test/order-updated \
  -H "Content-Type: application/json" \
  -d '{
    "orderId": 999,
    "status": "COMPLETED",
    "clientName": "João Silva",
    "totalValue": 1500.00,
    "eventType": "ORDER_COMPLETED"
  }'
```

---

## 🐛 Troubleshooting

### Erro: "Connection refused" na porta 8080
**Solução:** Inicie a aplicação Spring Boot

### Erro: "Connection refused" na porta 9092
**Solução:** Inicie o Kafka
```bash
brew services start kafka
```

### Erro: HTTP 403 Forbidden
**Solução:** Já corrigido! Os endpoints `/api/kafka/**` estão liberados no SecurityConfig

### Mensagens não aparecem nos logs
**Verificar:**
1. Kafka está rodando: `brew services list`
2. Tópicos foram criados: `kafka-topics --list --bootstrap-server localhost:9092`
3. Logs da aplicação não têm erros

### Consumer não está recebendo mensagens
**Verificar:**
1. O consumer group está configurado: `spring.kafka.consumer.group-id=mecanica-group`
2. Reinicie a aplicação
3. Verifique se há mensagens no tópico usando `kafka-console-consumer`

---

## 📈 Monitoramento em Tempo Real

### Terminal 1 - Ver logs da aplicação
```bash
# Se estiver rodando via IDE, veja o console
# Se estiver rodando via terminal:
tail -f logs/application.log
```

### Terminal 2 - Monitorar tópico order-created
```bash
kafka-console-consumer --bootstrap-server localhost:9092 \
  --topic order-created --from-beginning
```

### Terminal 3 - Enviar eventos
```bash
curl http://localhost:8080/api/kafka/test/quick
```

---

## ✅ Checklist de Sucesso

- [ ] Kafka rodando (`brew services list`)
- [ ] Aplicação Spring Boot rodando (porta 8080)
- [ ] Teste rápido funciona: `curl http://localhost:8080/api/kafka/test/quick`
- [ ] Logs mostram Producer enviando mensagem
- [ ] Logs mostram Consumer recebendo mensagem
- [ ] Tópicos criados: `kafka-topics --list --bootstrap-server localhost:9092`
- [ ] Mensagens visíveis no console consumer

---

## 🎯 Próximos Passos

Após confirmar que tudo funciona:

1. **Integrar com serviços existentes** - Adicionar eventos Kafka nos seus OrderService, PaymentService, etc.
2. **Adicionar tratamento de erros** - Dead Letter Queue (DLQ) para mensagens com falha
3. **Adicionar retry logic** - Reprocessar mensagens em caso de erro temporário
4. **Monitoramento** - Adicionar métricas e dashboards
5. **Testes automatizados** - Criar testes de integração com Kafka
