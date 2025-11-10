# Relacionamento Carro-Order e Controle de Estoque

## 📋 Implementações Realizadas

### 1. Relacionamento Carro → Order

#### **Entidade Order**
Adicionado relacionamento ManyToOne com Carro:
```java
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "carro_id", nullable = false)
private Carro carro;
```

**Justificativa**: Toda ordem de serviço precisa estar vinculada a um veículo específico que está sendo reparado.

---

### 2. DTOs Atualizados

#### **OrderCreateDto**
```java
@NotNull(message = "ID do carro é obrigatório")
private Long carroId;
```

#### **OrderDto** (Response)
```java
private Long carroId;
private String carroPlaca;
private String carroModelo;
```

**Benefício**: Cliente pode ver qual carro está sendo reparado sem precisar fazer consultas adicionais.

---

### 3. Controle de Estoque Completo

#### **Exceção Customizada**
```java
EstoqueInsuficienteException
```
- Mensagem específica com produto, quantidade disponível e solicitada
- Herda de `BusinessException` (HTTP 400)

#### **Métodos na Entidade Estoque**

**Novos métodos:**
```java
// Decrementa estoque com validação
public void decrementar(Integer quantidade)

// Incrementa estoque com validação  
public void incrementar(Integer quantidade)

// Verifica disponibilidade
public boolean temEstoqueSuficiente(Integer quantidade)

// Retorna quantidade disponível
public Integer getQuantidade()
```

**Métodos legados (deprecated):**
- `venderProduto()` → usa `decrementar()`
- `adicionarEstoque()` → usa `incrementar()`

---

### 4. Validações no OrderService

#### **Validação de Carro**
```java
// 1. Verifica se o carro existe
Carro carro = carroRepository.findById(dto.getCarroId())
    .orElseThrow(() -> new ResourceNotFoundException("Carro", "ID", dto.getCarroId()));

// 2. Valida se o carro pertence ao cliente
if (!carro.getCliente().getId().equals(cliente.getId())) {
    throw new BusinessException("O carro selecionado não pertence ao cliente informado");
}
```

#### **Controle de Estoque Automático**
```java
// 1. Verifica se produto tem estoque cadastrado
if (produto.getEstoque() == null) {
    throw new BusinessException("Produto sem estoque cadastrado: " + produto.getNomeProd());
}

// 2. Valida se há estoque suficiente
if (!produto.getEstoque().temEstoqueSuficiente(item.getQuantidade())) {
    throw new EstoqueInsuficienteException(
        produto.getNomeProd(),
        produto.getEstoque().getQuantidade(),
        item.getQuantidade()
    );
}

// 3. Decrementa o estoque automaticamente
produto.getEstoque().decrementar(item.getQuantidade());
```

---

## 🎯 Fluxo Completo de Criação de Ordem

### **Antes (Problemas)**
```json
POST /orders
{
  "clienteId": 1,
  "produtos": [{"produtoId": 1, "quantidade": 100}]
}
```
❌ Não sabia qual carro estava sendo reparado  
❌ Criava ordem mesmo sem estoque  
❌ Estoque não era decrementado

### **Agora (Solução)**
```json
POST /orders
{
  "clienteId": 1,
  "carroId": 1,
  "produtos": [{"produtoId": 1, "quantidade": 2}],
  "servicos": [{"servicoId": 1, "quantidade": 1}]
}
```

**Validações executadas:**
1. ✅ Cliente existe?
2. ✅ Carro existe?
3. ✅ Carro pertence ao cliente?
4. ✅ Produtos existem?
5. ✅ Produtos têm estoque cadastrado?
6. ✅ Há estoque suficiente?
7. ✅ Decrementa estoque automaticamente

---

## 📊 Exemplos de Respostas

### **Sucesso (201 Created)**
```json
{
  "id": 1,
  "clienteId": 1,
  "carroId": 1,
  "carroPlaca": "ABC-1234",
  "carroModelo": "Uno",
  "status": "RECEBIDO",
  "ativo": true,
  "produtos": [
    {
      "orderId": 1,
      "produtoId": 1,
      "nome": "Pneu",
      "preco": 100.0,
      "quantidade": 2,
      "subTotal": 200.0
    }
  ],
  "servicos": [
    {
      "orderId": 1,
      "servicoId": 1,
      "nome": "Trocar pneu",
      "descricao": "Realizar a troca de pneu",
      "preco": 100.0,
      "duracaoMinutos": 150,
      "quantidade": 1,
      "subTotal": 100.0
    }
  ]
}
```

### **Erro: Carro não pertence ao cliente (400)**
```json
{
  "timestamp": "2025-11-07T15:38:00",
  "status": 400,
  "error": "Business Rule Violation",
  "message": "O carro selecionado não pertence ao cliente informado",
  "path": "/orders"
}
```

### **Erro: Estoque Insuficiente (400)**
```json
{
  "timestamp": "2025-11-07T15:38:00",
  "status": 400,
  "error": "Business Rule Violation",
  "message": "Estoque insuficiente para o produto 'Pneu'. Disponível: 5, Solicitado: 10",
  "path": "/orders"
}
```

### **Erro: Produto sem estoque cadastrado (400)**
```json
{
  "timestamp": "2025-11-07T15:38:00",
  "status": 400,
  "error": "Business Rule Violation",
  "message": "Produto sem estoque cadastrado: Óleo de Motor",
  "path": "/orders"
}
```

---

## 🔄 Impacto nas Operações

### **Criação de Ordem**
- ✅ Requer `carroId` obrigatório
- ✅ Valida propriedade do carro
- ✅ Decrementa estoque automaticamente
- ✅ Impede criação se estoque insuficiente

### **Consulta de Ordem**
- ✅ Retorna dados do carro (placa, modelo)
- ✅ Facilita identificação visual
- ✅ Menos consultas ao banco

### **Estoque**
- ✅ Controle automático ao criar ordem
- ✅ Validações robustas
- ✅ Mensagens de erro específicas

---

## 🚀 Próximos Passos Sugeridos

### **Funcionalidades Adicionais**
1. **Devolução de Estoque**: Ao cancelar ordem, devolver produtos ao estoque
2. **Histórico de Manutenções**: Consultar todas as ordens de um carro
3. **Alertas de Estoque Baixo**: Notificar quando estoque < mínimo
4. **Reserva de Estoque**: Reservar produtos ao criar orçamento

### **Melhorias de Negócio**
```java
// Ao cancelar ordem, devolver estoque
public OrderDto cancelar(Long id) {
    Order entity = findByIdEntity(id);
    
    // Devolver produtos ao estoque
    if (entity.getOrderProdutos() != null) {
        for (OrderProduto op : entity.getOrderProdutos()) {
            op.getProduto().getEstoque().incrementar(op.getQuantidade());
        }
    }
    
    entity.cancelar();
    orderRepository.save(entity);
    return OrderMapper.toDto(entity);
}
```

### **Consultas Úteis**
```java
// Histórico de manutenções por carro
@Query("SELECT o FROM Order o WHERE o.carro.id = :carroId ORDER BY o.id DESC")
List<Order> findByCarroId(@Param("carroId") Long carroId);

// Produtos com estoque baixo
@Query("SELECT p FROM Produto p WHERE p.estoque.quantidadeEstoque < :minimo")
List<Produto> findProdutosComEstoqueBaixo(@Param("minimo") Integer minimo);
```

---

## ✅ Checklist de Implementação

- [x] Adicionar relacionamento Carro → Order
- [x] Atualizar OrderCreateDto com carroId
- [x] Atualizar OrderDto com dados do carro
- [x] Criar exceção EstoqueInsuficienteException
- [x] Implementar métodos de controle em Estoque
- [x] Validar propriedade do carro no OrderService
- [x] Implementar controle automático de estoque
- [x] Atualizar OrderMapper
- [x] Atualizar TestConfig com dados de teste
- [ ] Criar testes unitários para controle de estoque
- [ ] Criar testes de integração para validações
- [ ] Implementar devolução de estoque ao cancelar
- [ ] Adicionar endpoint de histórico por carro

---

## 📈 Benefícios Alcançados

| Aspecto | Antes | Depois |
|---------|-------|--------|
| **Rastreabilidade** | ❌ Não sabia qual carro | ✅ Carro vinculado à ordem |
| **Controle de Estoque** | ❌ Manual/Inexistente | ✅ Automático e validado |
| **Validações** | ❌ Básicas | ✅ Completas (carro, estoque) |
| **Mensagens de Erro** | ❌ Genéricas | ✅ Específicas e informativas |
| **Integridade** | ❌ Podia criar ordem sem estoque | ✅ Garante disponibilidade |
| **Experiência do Cliente** | ❌ Dados incompletos | ✅ Informações completas do carro |

---

## 🎓 Conclusão

As implementações de **relacionamento Carro-Order** e **controle de estoque** resolvem dois problemas críticos do sistema:

1. **Rastreabilidade**: Agora é possível saber exatamente qual veículo está sendo reparado
2. **Integridade de Dados**: Impossível criar ordens sem estoque disponível

O sistema está mais robusto, com validações completas e mensagens de erro claras, proporcionando uma experiência melhor tanto para usuários quanto para desenvolvedores.
