# Implementação de Orçamento Automático e Aprovação

## 📋 Resumo da Implementação

Foi implementado o sistema completo de **orçamento automático** e **aprovação de orçamento** para as Ordens de Serviço (OS).

---

## ✅ Funcionalidades Implementadas

### 1. **Orçamento Automático**

#### Campos Adicionados na Entidade `Order`:
- `valorTotal` (Double): Armazena o valor total calculado automaticamente
- `orcamentoAprovado` (Boolean): Indica se o orçamento foi aprovado pelo cliente
- `dataAprovacao` (LocalDateTime): Registra quando o orçamento foi aprovado

#### Método de Cálculo:
```java
public void calcularValorTotal() {
    // Soma todos os produtos (preço × quantidade)
    // Soma todos os serviços (preço × quantidade)
    // Atualiza o campo valorTotal
}
```

**Quando é calculado:**
- Automaticamente ao criar uma nova OS
- O valor é calculado antes de salvar no banco de dados

---

### 2. **Fluxo de Aprovação de Orçamento**

#### Estados da OS:
1. **RECEBIDO** → OS criada
2. **EM_DIAGNOSTICO** → Mecânico avaliando o veículo
3. **AGUARDANDO_APROVACAO** → Orçamento enviado ao cliente
4. **EM_EXECUCAO** → Cliente aprovou, serviço em andamento
5. **FINALIZADO** → Serviço concluído
6. **ENTREGUE** → Veículo entregue ao cliente
7. **CANCELADO** → OS cancelada (pode ser por rejeição do orçamento)

#### Métodos Implementados:

**`enviarParaAprovacao(Long id)`**
- Muda status de `EM_DIAGNOSTICO` → `AGUARDANDO_APROVACAO`
- Usado pela oficina após avaliar o veículo

**`aprovarOrcamento(Long id)`**
- Valida que o status é `AGUARDANDO_APROVACAO`
- Marca `orcamentoAprovado = true`
- Registra `dataAprovacao = now()`
- Muda status para `EM_EXECUCAO`
- Usado pelo cliente para aprovar

**`rejeitarOrcamento(Long id)`**
- Valida que o status é `AGUARDANDO_APROVACAO`
- Devolve produtos ao estoque
- Marca `orcamentoAprovado = false`
- Muda status para `CANCELADO`
- Desativa a OS
- Usado pelo cliente para rejeitar

---

## 🔌 Endpoints da API

### **POST** `/orders`
Cria uma nova OS com orçamento calculado automaticamente.

**Request Body:**
```json
{
  "clienteId": 1,
  "carroId": 1,
  "produtos": [
    {"produtoId": 1, "quantidade": 2}
  ],
  "servicos": [
    {"servicoId": 1, "quantidade": 1}
  ]
}
```

**Response:**
```json
{
  "id": 1,
  "clienteId": 1,
  "carroId": 1,
  "status": "RECEBIDO",
  "valorTotal": 450.00,
  "orcamentoAprovado": false,
  "dataAprovacao": null,
  "produtos": [...],
  "servicos": [...]
}
```

---

### **PATCH** `/orders/{id}/enviar-aprovacao`
Envia o orçamento para aprovação do cliente.

**Pré-requisito:** Status deve ser `EM_DIAGNOSTICO`

**Response:**
```json
{
  "id": 1,
  "status": "AGUARDANDO_APROVACAO",
  "valorTotal": 450.00,
  "orcamentoAprovado": false
}
```

---

### **PATCH** `/orders/{id}/aprovar-orcamento`
Cliente aprova o orçamento.

**Pré-requisito:** Status deve ser `AGUARDANDO_APROVACAO`

**Response:**
```json
{
  "id": 1,
  "status": "EM_EXECUCAO",
  "valorTotal": 450.00,
  "orcamentoAprovado": true,
  "dataAprovacao": "2025-11-10T18:15:30"
}
```

---

### **PATCH** `/orders/{id}/rejeitar-orcamento`
Cliente rejeita o orçamento.

**Pré-requisito:** Status deve ser `AGUARDANDO_APROVACAO`

**Efeitos:**
- Devolve produtos ao estoque
- Cancela a OS
- Desativa a OS

**Response:**
```json
{
  "id": 1,
  "status": "CANCELADO",
  "valorTotal": 450.00,
  "orcamentoAprovado": false,
  "ativo": false
}
```

---

## 🔄 Fluxo Completo de Uso

### Cenário 1: Aprovação do Orçamento

```
1. POST /orders
   → Cria OS com status RECEBIDO
   → valorTotal calculado automaticamente

2. PATCH /orders/1/status?status=EM_DIAGNOSTICO
   → Mecânico inicia diagnóstico

3. PATCH /orders/1/enviar-aprovacao
   → Status muda para AGUARDANDO_APROVACAO
   → Cliente recebe orçamento

4. PATCH /orders/1/aprovar-orcamento
   → Cliente aprova
   → Status muda para EM_EXECUCAO
   → orcamentoAprovado = true

5. PATCH /orders/1/status?status=FINALIZADO
   → Serviço concluído

6. PATCH /orders/1/status?status=ENTREGUE
   → Veículo entregue
```

### Cenário 2: Rejeição do Orçamento

```
1. POST /orders
   → Cria OS com status RECEBIDO

2. PATCH /orders/1/status?status=EM_DIAGNOSTICO
   → Mecânico inicia diagnóstico

3. PATCH /orders/1/enviar-aprovacao
   → Status muda para AGUARDANDO_APROVACAO

4. PATCH /orders/1/rejeitar-orcamento
   → Cliente rejeita
   → Produtos devolvidos ao estoque
   → Status muda para CANCELADO
   → OS desativada
```

---

## 🛡️ Validações Implementadas

### Aprovação de Orçamento:
- ✅ Apenas OS com status `AGUARDANDO_APROVACAO` podem ser aprovadas
- ✅ Não permite aprovar orçamento já aprovado anteriormente
- ✅ Registra data/hora da aprovação

### Rejeição de Orçamento:
- ✅ Apenas OS com status `AGUARDANDO_APROVACAO` podem ser rejeitadas
- ✅ Devolve automaticamente produtos ao estoque
- ✅ Cancela e desativa a OS

### Envio para Aprovação:
- ✅ Apenas OS com status `EM_DIAGNOSTICO` podem ser enviadas
- ✅ Impede envio em outros status

---

## 📊 Campos no Banco de Dados

### Tabela `tb_order`:
```sql
ALTER TABLE tb_order ADD COLUMN valor_total DOUBLE NOT NULL DEFAULT 0.0;
ALTER TABLE tb_order ADD COLUMN orcamento_aprovado BOOLEAN NOT NULL DEFAULT FALSE;
ALTER TABLE tb_order ADD COLUMN data_aprovacao TIMESTAMP;
```

---

## 🎯 Próximos Passos Sugeridos

1. **Notificação por Email (Kafka)**
   - Enviar email ao cliente quando orçamento for enviado para aprovação
   - Notificar oficina quando cliente aprovar/rejeitar

2. **Histórico de Orçamentos**
   - Permitir múltiplas versões de orçamento
   - Registrar alterações de valores

3. **Prazo de Validade**
   - Adicionar campo `validadeOrcamento`
   - Cancelar automaticamente orçamentos expirados

4. **Desconto/Acréscimo**
   - Permitir aplicar desconto no orçamento total
   - Adicionar campo de observações

---

## ✅ Checklist de Requisitos Atendidos

- [x] Orçamento gerado automaticamente com base nos serviços e peças
- [x] Envio do orçamento ao cliente para aprovação
- [x] Alteração automática dos status conforme ações no sistema
- [x] Validação de estoque ao criar OS
- [x] Devolução de estoque ao rejeitar orçamento
- [x] Registro de data/hora de aprovação
- [x] Endpoints REST para todo o fluxo
