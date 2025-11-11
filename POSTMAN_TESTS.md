# Guia de Testes - API Mecânica

## Configuração Inicial

**Base URL:** `http://localhost:8080`

## Pré-requisitos

1. **MySQL deve estar rodando** na porta 3306
2. **Credenciais configuradas** no `application.properties`:
   - Username: `root`
   - Password: `root`
   - Database: `mecanica_db` (será criado automaticamente)

---

## 1. CLIENTES

### 1.1 Criar Cliente
**POST** `/clientes`

```json
{
  "nome": "João Silva",
  "doc": "12345678901",
  "telefone": "(11) 98765-4321",
  "email": "joao.silva@email.com"
}
```

### 1.2 Criar Cliente (CNPJ)
**POST** `/clientes`

```json
{
  "nome": "Empresa XYZ Ltda",
  "doc": "12345678000190",
  "telefone": "(11) 3456-7890",
  "email": "contato@empresaxyz.com"
}
```

### 1.3 Listar Todos os Clientes
**GET** `/clientes`

### 1.4 Buscar Cliente por ID
**GET** `/clientes/{id}`

Exemplo: `GET /clientes/1`

### 1.5 Buscar Cliente por CPF/CNPJ
**GET** `/clientes/doc/{doc}`

Exemplo: `GET /clientes/doc/12345678901`

### 1.6 Buscar Cliente por CPF/CNPJ (Query Param)
**GET** `/clientes/cpf-cnpj?documento=12345678901`

### 1.7 Atualizar Cliente
**PUT** `/clientes/{id}`

```json
{
  "nome": "João Silva Santos",
  "doc": "12345678901",
  "telefone": "(11) 98765-4321",
  "email": "joao.santos@email.com"
}
```

### 1.8 Inativar Cliente
**DELETE** `/clientes/inativar/{id}`

### 1.9 Deletar Cliente
**DELETE** `/clientes/{id}`

---

## 2. CARROS

### 2.1 Criar Carro
**POST** `/carros`

```json
{
  "modelo": "Civic",
  "marca": "Honda",
  "anoFabricacao": 2020,
  "placa": "ABC1234",
  "cor": "Preto",
  "clienteId": 1
}
```

### 2.2 Criar Carro (Placa Mercosul)
**POST** `/carros`

```json
{
  "modelo": "Corolla",
  "marca": "Toyota",
  "anoFabricacao": 2023,
  "placa": "ABC1D23",
  "cor": "Branco",
  "clienteId": 1
}
```

### 2.3 Listar Todos os Carros
**GET** `/carros`

### 2.4 Buscar Carro por ID
**GET** `/carros/{id}`

### 2.5 Buscar Carro por Placa
**GET** `/carros/placa/{placa}`

Exemplo: `GET /carros/placa/ABC1234`

### 2.6 Atualizar Carro
**PUT** `/carros/{id}`

```json
{
  "modelo": "Civic EX",
  "marca": "Honda",
  "anoFabricacao": 2020,
  "placa": "ABC1234",
  "cor": "Preto Perolizado",
  "clienteId": 1
}
```

### 2.7 Deletar Carro
**DELETE** `/carros/{id}`

---

## 3. PRODUTOS

### 3.1 Criar Produto
**POST** `/produtos`

```json
{
  "nome": "Óleo Motor 5W30",
  "preco": 45.90
}
```

### 3.2 Criar Múltiplos Produtos
**POST** `/produtos`

```json
{
  "nome": "Filtro de Óleo",
  "preco": 25.00
}
```

```json
{
  "nome": "Pastilha de Freio",
  "preco": 120.00
}
```

```json
{
  "nome": "Vela de Ignição",
  "preco": 35.00
}
```

### 3.3 Listar Todos os Produtos
**GET** `/produtos`

### 3.4 Listar Produtos Ativos
**GET** `/produtos/ativos`

### 3.5 Buscar Produto por ID
**GET** `/produtos/{id}`

### 3.6 Atualizar Produto
**PUT** `/produtos/{id}`

```json
{
  "nome": "Óleo Motor 5W30 Sintético",
  "preco": 55.90
}
```

### 3.7 Desativar Produto
**PATCH** `/produtos/{id}/desativar`

### 3.8 Ativar Produto
**PATCH** `/produtos/{id}/ativar`

### 3.9 Deletar Produto
**DELETE** `/produtos/{id}`

---

## 4. SERVIÇOS

### 4.1 Criar Serviço
**POST** `/servicos`

```json
{
  "nome": "Troca de Óleo",
  "descricao": "Troca de óleo do motor com filtro",
  "preco": 80.00,
  "duracaoMinutos": 30
}
```

### 4.2 Criar Múltiplos Serviços
**POST** `/servicos`

```json
{
  "nome": "Alinhamento e Balanceamento",
  "descricao": "Alinhamento e balanceamento completo",
  "preco": 150.00,
  "duracaoMinutos": 60
}
```

```json
{
  "nome": "Revisão Completa",
  "descricao": "Revisão geral do veículo",
  "preco": 350.00,
  "duracaoMinutos": 180
}
```

```json
{
  "nome": "Troca de Pastilha de Freio",
  "descricao": "Substituição das pastilhas de freio dianteiras",
  "preco": 200.00,
  "duracaoMinutos": 90
}
```

### 4.3 Listar Todos os Serviços
**GET** `/servicos`

### 4.4 Listar Serviços Ativos
**GET** `/servicos/ativos`

### 4.5 Buscar Serviço por ID
**GET** `/servicos/{id}`

### 4.6 Atualizar Serviço
**PUT** `/servicos/{id}`

```json
{
  "nome": "Troca de Óleo Premium",
  "descricao": "Troca de óleo sintético premium com filtro de alta performance",
  "preco": 120.00,
  "duracaoMinutos": 45
}
```

### 4.7 Desativar Serviço
**PATCH** `/servicos/{id}/desativar`

### 4.8 Ativar Serviço
**PATCH** `/servicos/{id}/ativar`

### 4.9 Deletar Serviço
**DELETE** `/servicos/{id}`

---

## 5. ORDENS DE SERVIÇO (ORDERS)

### 5.1 Criar Ordem de Serviço
**POST** `/orders`

```json
{
  "clienteId": 1,
  "carroId": 1,
  "produtos": [
    {
      "produtoId": 1,
      "quantidade": 1
    },
    {
      "produtoId": 2,
      "quantidade": 1
    }
  ],
  "servicos": [
    {
      "servicoId": 1,
      "quantidade": 1
    }
  ]
}
```

### 5.2 Criar Ordem Complexa
**POST** `/orders`

```json
{
  "clienteId": 1,
  "carroId": 1,
  "produtos": [
    {
      "produtoId": 1,
      "quantidade": 1
    },
    {
      "produtoId": 2,
      "quantidade": 1
    },
    {
      "produtoId": 3,
      "quantidade": 4
    }
  ],
  "servicos": [
    {
      "servicoId": 1,
      "quantidade": 1
    },
    {
      "servicoId": 2,
      "quantidade": 1
    },
    {
      "servicoId": 4,
      "quantidade": 1
    }
  ]
}
```

### 5.3 Listar Todas as Ordens
**GET** `/orders`

### 5.4 Listar Ordens Ativas
**GET** `/orders/ativos`

### 5.5 Buscar Ordem por ID
**GET** `/orders/{id}`

### 5.6 Buscar Ordens por Cliente
**GET** `/orders/cliente/{clienteId}`

### 5.7 Buscar Ordens por Status
**GET** `/orders/status/{status}`

Status disponíveis:
- `RECEBIDO`
- `EM_DIAGNOSTICO`
- `AGUARDANDO_APROVACAO`
- `EM_EXECUCAO`
- `FINALIZADO`
- `ENTREGUE`
- `CANCELADO`

Exemplo: `GET /orders/status/RECEBIDO`

### 5.8 Atualizar Status da Ordem
**PATCH** `/orders/{id}/status?status=EM_DIAGNOSTICO`

### 5.9 Enviar para Aprovação
**PATCH** `/orders/{id}/enviar-aprovacao`

### 5.10 Aprovar Orçamento
**PATCH** `/orders/{id}/aprovar-orcamento`

### 5.11 Rejeitar Orçamento
**PATCH** `/orders/{id}/rejeitar-orcamento`

### 5.12 Finalizar Ordem
**PATCH** `/orders/{id}/finalizar`

### 5.13 Entregar Ordem
**PATCH** `/orders/{id}/entregar`

### 5.14 Cancelar Ordem
**PATCH** `/orders/{id}/cancelar`

### 5.15 Desativar Ordem
**PATCH** `/orders/{id}/desativar`

### 5.16 Ativar Ordem
**PATCH** `/orders/{id}/ativar`

### 5.17 Obter Estatísticas
**GET** `/orders/statistics`

### 5.18 Deletar Ordem
**DELETE** `/orders/{id}`

---

## 6. FLUXO COMPLETO DE TESTE

### Passo 1: Criar Cliente
```json
POST /clientes
{
  "nome": "Maria Santos",
  "doc": "98765432100",
  "telefone": "(11) 91234-5678",
  "email": "maria.santos@email.com"
}
```
**Anote o ID retornado (ex: clienteId = 1)**

### Passo 2: Criar Carro para o Cliente
```json
POST /carros
{
  "modelo": "Gol",
  "marca": "Volkswagen",
  "anoFabricacao": 2019,
  "placa": "XYZ9876",
  "cor": "Prata",
  "clienteId": 1
}
```
**Anote o ID retornado (ex: carroId = 1)**

### Passo 3: Criar Produtos
```json
POST /produtos
{
  "nome": "Óleo Motor 10W40",
  "preco": 42.00
}
```
**Anote o ID (ex: produtoId = 1)**

```json
POST /produtos
{
  "nome": "Filtro de Ar",
  "preco": 35.00
}
```
**Anote o ID (ex: produtoId = 2)**

### Passo 4: Criar Serviços
```json
POST /servicos
{
  "nome": "Troca de Óleo Completa",
  "descricao": "Troca de óleo e filtros",
  "preco": 100.00,
  "duracaoMinutos": 45
}
```
**Anote o ID (ex: servicoId = 1)**

### Passo 5: Criar Ordem de Serviço
```json
POST /orders
{
  "clienteId": 1,
  "carroId": 1,
  "produtos": [
    {
      "produtoId": 1,
      "quantidade": 1
    },
    {
      "produtoId": 2,
      "quantidade": 1
    }
  ],
  "servicos": [
    {
      "servicoId": 1,
      "quantidade": 1
    }
  ]
}
```
**Anote o ID da ordem (ex: orderId = 1)**

### Passo 6: Atualizar Status para Diagnóstico
```
PATCH /orders/1/status?status=EM_DIAGNOSTICO
```

### Passo 7: Enviar para Aprovação
```
PATCH /orders/1/enviar-aprovacao
```

### Passo 8: Aprovar Orçamento
```
PATCH /orders/1/aprovar-orcamento
```
*Status muda automaticamente para EM_EXECUCAO*

### Passo 9: Finalizar Ordem
```
PATCH /orders/1/finalizar
```

### Passo 10: Entregar Ordem
```
PATCH /orders/1/entregar
```

### Passo 11: Verificar Estatísticas
```
GET /orders/statistics
```

---

## 7. TESTES DE VALIDAÇÃO

### 7.1 Cliente com CPF Inválido (deve falhar)
```json
POST /clientes
{
  "nome": "Teste",
  "doc": "12345",
  "telefone": "(11) 98765-4321",
  "email": "teste@email.com"
}
```

### 7.2 Cliente sem Nome (deve falhar)
```json
POST /clientes
{
  "doc": "12345678901",
  "telefone": "(11) 98765-4321",
  "email": "teste@email.com"
}
```

### 7.3 Carro com Placa Inválida (deve falhar)
```json
POST /carros
{
  "modelo": "Civic",
  "marca": "Honda",
  "anoFabricacao": 2020,
  "placa": "INVALIDA",
  "cor": "Preto",
  "clienteId": 1
}
```

### 7.4 Produto com Preço Negativo (deve falhar)
```json
POST /produtos
{
  "nome": "Produto Teste",
  "preco": -10.00
}
```

### 7.5 Ordem sem Cliente (deve falhar)
```json
POST /orders
{
  "carroId": 1,
  "produtos": [],
  "servicos": []
}
```

---

## 8. NOTAS IMPORTANTES

### Status da Ordem de Serviço
O fluxo correto de status é:
1. **RECEBIDO** → Ordem criada
2. **EM_DIAGNOSTICO** → Diagnóstico em andamento
3. **AGUARDANDO_APROVACAO** → Aguardando aprovação do cliente
4. **EM_EXECUCAO** → Orçamento aprovado, serviço em execução
5. **FINALIZADO** → Serviço concluído
6. **ENTREGUE** → Veículo entregue ao cliente
7. **CANCELADO** → Ordem cancelada (pode ocorrer antes de EM_EXECUCAO)

### Cancelamento
- Ordens só podem ser canceladas nos status: RECEBIDO, EM_DIAGNOSTICO ou AGUARDANDO_APROVACAO
- Após aprovação (EM_EXECUCAO), não é possível cancelar

### Validações
- **CPF**: 11 dígitos numéricos
- **CNPJ**: 14 dígitos numéricos
- **Placa**: Formato antigo (ABC1234) ou Mercosul (ABC1D23)
- **Telefone**: (11) 98765-4321 ou 11987654321
- **Email**: Formato válido de email

### Cálculo de Valores
- O valor total da ordem é calculado automaticamente somando:
  - (Preço do Produto × Quantidade) para cada produto
  - (Preço do Serviço × Quantidade) para cada serviço

---

## 9. TROUBLESHOOTING

### Erro de Conexão com MySQL
1. Verifique se o MySQL está rodando: `mysql.server status` (Mac) ou `systemctl status mysql` (Linux)
2. Verifique as credenciais no `application.properties`
3. Certifique-se de que a porta 3306 está disponível

### Erro 404 - Not Found
- Verifique se a aplicação está rodando na porta 8080
- Confirme a URL base: `http://localhost:8080`

### Erro 400 - Bad Request
- Verifique se o JSON está formatado corretamente
- Confirme se todos os campos obrigatórios estão presentes
- Verifique as validações (CPF, CNPJ, placa, etc.)

### Erro 500 - Internal Server Error
- Verifique os logs da aplicação
- Confirme se as entidades relacionadas existem (cliente, carro, produto, serviço)

---

## 10. COMANDOS ÚTEIS

### Iniciar a Aplicação
```bash
cd /Users/mmdomingues/Desktop/Matheus/mecanica
./mvnw spring-boot:run
```

### Verificar MySQL
```bash
mysql -u root -p
USE mecanica_db;
SHOW TABLES;
```

### Limpar Banco de Dados
```sql
DROP DATABASE mecanica_db;
CREATE DATABASE mecanica_db;
```

---

**Última atualização:** 2025-11-11
