# Implementação de Monitoramento de Tempo Médio e Busca por CPF/CNPJ

## 📋 Resumo da Implementação

Foi implementado:
1. **Monitoramento de tempo médio de execução** das ordens de serviço
2. **Busca de cliente por CPF/CNPJ** com limpeza automática de caracteres especiais
3. **Timestamps completos** para rastreamento do ciclo de vida das OS

---

## ✅ 1. Monitoramento de Tempo Médio

### Campos Adicionados na Entidade `Order`:

```java
@Column(name = "data_criacao", nullable = false, updatable = false)
private LocalDateTime dataCriacao;

@Column(name = "data_inicio_execucao")
private LocalDateTime dataInicioExecucao;

@Column(name = "data_finalizacao")
private LocalDateTime dataFinalizacao;
```

### Timestamps Automáticos:

- **`dataCriacao`**: Definida automaticamente no construtor da Order
- **`dataInicioExecucao`**: Definida quando o orçamento é aprovado
- **`dataFinalizacao`**: Definida quando a OS é finalizada

### Métodos Adicionados:

#### `finalizar()`
```java
public void finalizar() {
    // Valida que status é EM_EXECUCAO
    // Define dataFinalizacao = now()
    // Muda status para FINALIZADO
}
```

#### `entregar()`
```java
public void entregar() {
    // Valida que status é FINALIZADO
    // Muda status para ENTREGUE
}
```

---

## 📊 Endpoint de Estatísticas

### **GET** `/orders/statistics`

Retorna estatísticas completas sobre as ordens de serviço.

**Response:**
```json
{
  "totalOrdens": 150,
  "ordensFinalizadas": 120,
  "ordensEmAndamento": 25,
  "ordensCanceladas": 5,
  "tempoMedioExecucaoMinutos": 180.5,
  "tempoMedioTotalMinutos": 4320.0
}
```

### Métricas Calculadas:

#### **1. Total de Ordens**
Contagem de todas as OS no sistema.

#### **2. Ordens Finalizadas**
OS com status `FINALIZADO` ou `ENTREGUE`.

#### **3. Ordens em Andamento**
OS com status:
- `RECEBIDO`
- `EM_DIAGNOSTICO`
- `AGUARDANDO_APROVACAO`
- `EM_EXECUCAO`

#### **4. Ordens Canceladas**
OS com status `CANCELADO`.

#### **5. Tempo Médio de Execução (minutos)**
Tempo médio entre `dataInicioExecucao` e `dataFinalizacao`.

**Cálculo:**
```
Soma de (dataFinalizacao - dataInicioExecucao) / Quantidade de OS finalizadas
```

**Representa:** Tempo real de trabalho na oficina.

#### **6. Tempo Médio Total (minutos)**
Tempo médio entre `dataCriacao` e `dataFinalizacao`.

**Cálculo:**
```
Soma de (dataFinalizacao - dataCriacao) / Quantidade de OS finalizadas
```

**Representa:** Tempo total desde a chegada até a finalização (inclui tempo de aprovação, espera, etc.).

---

## 🔄 Fluxo Completo com Timestamps

```
1. POST /orders
   → dataCriacao = now()
   → status = RECEBIDO

2. PATCH /orders/1/status?status=EM_DIAGNOSTICO
   → Mecânico avalia o veículo

3. PATCH /orders/1/enviar-aprovacao
   → status = AGUARDANDO_APROVACAO

4. PATCH /orders/1/aprovar-orcamento
   → dataInicioExecucao = now()
   → dataAprovacao = now()
   → status = EM_EXECUCAO

5. PATCH /orders/1/finalizar
   → dataFinalizacao = now()
   → status = FINALIZADO

6. PATCH /orders/1/entregar
   → status = ENTREGUE
```

### Exemplo de Tempos:

```
dataCriacao:         2025-11-10 08:00:00
dataInicioExecucao:  2025-11-10 10:30:00  (após aprovação)
dataFinalizacao:     2025-11-10 14:30:00  (serviço concluído)

Tempo de Execução: 240 minutos (4 horas)
Tempo Total:       390 minutos (6,5 horas)
```

---

## 🔍 2. Busca de Cliente por CPF/CNPJ

### Melhorias Implementadas:

#### **Limpeza Automática de Caracteres**
O sistema agora remove automaticamente:
- Pontos (`.`)
- Traços (`-`)
- Barras (`/`)
- Espaços

**Exemplo:**
```
Entrada: "123.456.789-00"
Busca:   "12345678900"

Entrada: "12.345.678/0001-90"
Busca:   "12345678000190"
```

### Endpoints Disponíveis:

#### **1. GET** `/clientes/doc/{doc}`
Busca por documento usando path variable.

**Exemplos:**
```
GET /clientes/doc/12345678900
GET /clientes/doc/123.456.789-00
GET /clientes/doc/12.345.678/0001-90
```

Todos funcionam e retornam o mesmo cliente!

#### **2. GET** `/clientes/cpf-cnpj?documento={doc}`
Busca por documento usando query parameter.

**Exemplos:**
```
GET /clientes/cpf-cnpj?documento=12345678900
GET /clientes/cpf-cnpj?documento=123.456.789-00
```

**Response:**
```json
{
  "id": 1,
  "nome": "João Silva",
  "doc": "12345678900",
  "telefone": "11987654321",
  "email": "joao@email.com",
  "ativo": true
}
```

---

## 🎯 Novos Endpoints Implementados

### **Order Controller**

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| PATCH | `/orders/{id}/finalizar` | Finaliza a OS (EM_EXECUCAO → FINALIZADO) |
| PATCH | `/orders/{id}/entregar` | Marca como entregue (FINALIZADO → ENTREGUE) |
| GET | `/orders/statistics` | Retorna estatísticas e tempo médio |

### **Cliente Controller**

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/clientes/cpf-cnpj?documento={doc}` | Busca por CPF/CNPJ (query param) |
| GET | `/clientes/doc/{doc}` | Busca por documento (path variable) |

---

## 📊 Exemplo de Uso - Dashboard de Gestão

### Consultar Estatísticas:
```bash
GET /orders/statistics
```

**Response:**
```json
{
  "totalOrdens": 50,
  "ordensFinalizadas": 35,
  "ordensEmAndamento": 12,
  "ordensCanceladas": 3,
  "tempoMedioExecucaoMinutos": 240.5,
  "tempoMedioTotalMinutos": 2880.0
}
```

**Interpretação:**
- ✅ 35 ordens finalizadas (70% de conclusão)
- 🔄 12 ordens em andamento
- ❌ 3 ordens canceladas
- ⏱️ Tempo médio de execução: **4 horas** (240 min)
- 📅 Tempo médio total: **2 dias** (2880 min = 48h)

---

## 🔄 Fluxo Completo de Criação de OS por CPF/CNPJ

### Passo 1: Buscar Cliente por CPF
```bash
GET /clientes/cpf-cnpj?documento=123.456.789-00
```

**Response:**
```json
{
  "id": 5,
  "nome": "Maria Santos",
  "doc": "12345678900"
}
```

### Passo 2: Buscar Veículos do Cliente
```bash
GET /carros?clienteId=5
```

### Passo 3: Criar OS
```bash
POST /orders
{
  "clienteId": 5,
  "carroId": 10,
  "produtos": [...],
  "servicos": [...]
}
```

---

## 🛡️ Validações Implementadas

### Finalização de OS:
- ✅ Apenas OS com status `EM_EXECUCAO` podem ser finalizadas
- ✅ Define `dataFinalizacao` automaticamente
- ✅ Muda status para `FINALIZADO`

### Entrega de OS:
- ✅ Apenas OS com status `FINALIZADO` podem ser entregues
- ✅ Muda status para `ENTREGUE`

### Busca por CPF/CNPJ:
- ✅ Remove caracteres especiais automaticamente
- ✅ Aceita CPF/CNPJ formatado ou não
- ✅ Retorna erro 404 se não encontrar

---

## 📈 Benefícios para Gestão

### 1. **Monitoramento de Performance**
- Identificar gargalos no processo
- Comparar tempo de execução vs tempo total
- Detectar atrasos na aprovação

### 2. **Métricas de Negócio**
- Taxa de conclusão de ordens
- Taxa de cancelamento
- Capacidade de atendimento

### 3. **Planejamento**
- Estimar prazos com base em histórico
- Dimensionar equipe necessária
- Prever tempo de entrega

### 4. **Experiência do Cliente**
- Busca rápida por CPF/CNPJ
- Não precisa decorar formato
- Histórico completo de serviços

---

## 🗄️ Campos no Banco de Dados

### Tabela `tb_order`:
```sql
ALTER TABLE tb_order ADD COLUMN data_criacao TIMESTAMP NOT NULL;
ALTER TABLE tb_order ADD COLUMN data_inicio_execucao TIMESTAMP;
ALTER TABLE tb_order ADD COLUMN data_finalizacao TIMESTAMP;
```

---

## 📊 Exemplo de Relatório Gerencial

```
╔════════════════════════════════════════════════╗
║        RELATÓRIO DE ORDENS DE SERVIÇO         ║
╠════════════════════════════════════════════════╣
║ Total de Ordens:              150              ║
║ Finalizadas:                  120 (80%)        ║
║ Em Andamento:                  25 (17%)        ║
║ Canceladas:                     5 (3%)         ║
╠════════════════════════════════════════════════╣
║ TEMPO MÉDIO DE EXECUÇÃO:    3h 30min          ║
║ TEMPO MÉDIO TOTAL:          2 dias 12h        ║
╠════════════════════════════════════════════════╣
║ Tempo de Aprovação Médio:   1 dia 8h          ║
║ (Total - Execução)                             ║
╚════════════════════════════════════════════════╝
```

---

## ✅ Checklist de Requisitos Atendidos

- [x] Monitoramento do tempo médio de execução dos serviços
- [x] Identificação do cliente por CPF/CNPJ
- [x] Timestamps completos para rastreamento
- [x] Estatísticas de ordens de serviço
- [x] Busca flexível por documento (com ou sem formatação)
- [x] Validações de transição de status
- [x] Endpoints REST para todas as operações

---

## 🎯 Próximos Passos Sugeridos

1. **Dashboard Visual**
   - Gráficos de tempo médio
   - Evolução de ordens ao longo do tempo

2. **Alertas**
   - Notificar quando OS ultrapassar tempo médio
   - Alertar ordens paradas há muito tempo

3. **Relatórios Avançados**
   - Tempo médio por tipo de serviço
   - Performance por mecânico
   - Análise de pico de demanda

4. **Exportação**
   - Exportar estatísticas em PDF
   - Relatórios periódicos automáticos
