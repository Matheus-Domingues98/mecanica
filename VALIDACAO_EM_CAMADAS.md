# Validação em Camadas - Boas Práticas

## 📋 Estratégia Implementada

Este documento explica a estratégia de validação em camadas aplicada no projeto, reduzindo código boilerplate e seguindo as boas práticas do Spring Boot.

---

## 🎯 Princípio: Separação de Responsabilidades

```
┌──────────────────────────────────────────────────────────┐
│ 1. CONTROLLER (@Valid)                                   │
│    ↓ Bean Validation automática                          │
│    • @NotNull, @Size, @Email, @Pattern                   │
│    • Validadores customizados (@CpfCnpj, @PlacaVeiculo)  │
│    • Retorna 400 com detalhes dos erros                  │
├──────────────────────────────────────────────────────────┤
│ 2. SERVICE                                                │
│    ↓ Regras de negócio                                   │
│    • Verificar se recurso existe (404)                   │
│    • Validar duplicidade (409)                           │
│    • Validar estoque (400)                               │
│    • Validar relacionamentos (400)                       │
├──────────────────────────────────────────────────────────┤
│ 3. ENTITY                                                 │
│    ↓ Invariantes de domínio                              │
│    • Regras que SEMPRE devem ser verdadeiras             │
│    • Métodos de negócio (podeSerCancelada())             │
│    • Constraints de banco (nullable, unique)             │
└──────────────────────────────────────────────────────────┘
```

---

## ✅ O Que Foi Refatorado

### **Antes: Validações Redundantes**

```java
@Transactional
public OrderDto insert(OrderCreateDto dto) {
    // ❌ Validações redundantes - Bean Validation já faz isso
    if (dto == null) {
        throw new IllegalArgumentException("OrderCreateDto nao pode ser nulo");
    }
    if (dto.getClienteId() == null) {
        throw new IllegalArgumentException("ID do cliente nao pode ser nulo");
    }
    if (dto.getCarroId() == null) {
        throw new IllegalArgumentException("ID do carro nao pode ser nulo");
    }
    
    // Loop com validações redundantes
    for (OrderCreateDto.ItemProdutoDto item : dto.getProdutos()) {
        if (item.getProdutoId() == null) {
            throw new IllegalArgumentException("ID do produto nao pode ser nulo");
        }
        if (item.getQuantidade() == null || item.getQuantidade() <= 0) {
            throw new IllegalArgumentException("Quantidade deve ser maior que zero");
        }
        // ... lógica de negócio
    }
}
```

### **Depois: Service Focado em Negócio**

```java
@Transactional
public OrderDto insert(OrderCreateDto dto) {
    // ✅ Bean Validation já garante que dto, clienteId e carroId não são nulos
    
    Cliente cliente = clienteRepository.findById(dto.getClienteId())
            .orElseThrow(() -> new ResourceNotFoundException("Cliente", "ID", dto.getClienteId()));

    Carro carro = carroRepository.findById(dto.getCarroId())
            .orElseThrow(() -> new ResourceNotFoundException("Carro", "ID", dto.getCarroId()));

    // ✅ APENAS validação de negócio
    if (!carro.getCliente().getId().equals(cliente.getId())) {
        throw new BusinessException("O carro selecionado nao pertence ao cliente informado");
    }
    
    // Loop limpo, focado em negócio
    for (OrderCreateDto.ItemProdutoDto item : dto.getProdutos()) {
        // ✅ Bean Validation já garante que produtoId e quantidade são válidos
        
        Produto produto = produtoRepository.findById(item.getProdutoId())
                .orElseThrow(() -> new ResourceNotFoundException("Produto", "ID", item.getProdutoId()));

        // ✅ APENAS validações de negócio: estoque
        if (produto.getEstoque() == null) {
            throw new BusinessException("Produto sem estoque cadastrado: " + produto.getNomeProd());
        }
        
        if (!produto.getEstoque().temEstoqueSuficiente(item.getQuantidade())) {
            throw new EstoqueInsuficienteException(
                    produto.getNomeProd(),
                    produto.getEstoque().getQuantidade(),
                    item.getQuantidade()
            );
        }

        produto.getEstoque().decrementar(item.getQuantidade());
        entity.adicionarProduto(produto, item.getQuantidade());
    }
}
```

---

## 📊 Benefícios Alcançados

| Aspecto | Antes | Depois |
|---------|-------|--------|
| **Linhas de código** | ~250 linhas | ~200 linhas (-20%) |
| **Validações no Service** | 15+ validações manuais | 5 validações de negócio |
| **Clareza** | Mistura validação + negócio | Focado em negócio |
| **Manutenibilidade** | Validações duplicadas | Validações centralizadas |
| **Testabilidade** | Precisa testar validações básicas | Testa apenas regras de negócio |

---

## 🔍 Onde Cada Tipo de Validação Deve Estar

### **1. DTO (Bean Validation)**

```java
public class OrderCreateDto {
    @NotNull(message = "ID do cliente é obrigatório")
    private Long clienteId;
    
    @NotNull(message = "ID do carro é obrigatório")
    private Long carroId;
    
    @Valid  // Valida objetos aninhados
    private List<ItemProdutoDto> produtos;
    
    public static class ItemProdutoDto {
        @NotNull(message = "ID do produto é obrigatório")
        private Long produtoId;
        
        @NotNull(message = "Quantidade é obrigatória")
        @Positive(message = "Quantidade deve ser maior que zero")
        private Integer quantidade;
    }
}
```

**Valida:**
- ✅ Campos obrigatórios (`@NotNull`)
- ✅ Formato de dados (`@Email`, `@Pattern`)
- ✅ Tamanho (`@Size`, `@Min`, `@Max`)
- ✅ Valores positivos (`@Positive`)
- ✅ Validações customizadas (`@CpfCnpj`, `@PlacaVeiculo`)

### **2. Service (Regras de Negócio)**

```java
@Service
public class OrderService {
    
    public OrderDto insert(OrderCreateDto dto) {
        // ✅ Buscar recursos (404 se não existir)
        Cliente cliente = clienteRepository.findById(dto.getClienteId())
                .orElseThrow(() -> new ResourceNotFoundException(...));
        
        // ✅ Validar relacionamentos
        if (!carro.getCliente().getId().equals(cliente.getId())) {
            throw new BusinessException("Carro não pertence ao cliente");
        }
        
        // ✅ Validar estoque
        if (!produto.getEstoque().temEstoqueSuficiente(quantidade)) {
            throw new EstoqueInsuficienteException(...);
        }
        
        // ✅ Validar duplicidade
        if (clienteRepository.findByDoc(doc).isPresent()) {
            throw new DuplicateResourceException(...);
        }
    }
}
```

**Valida:**
- ✅ Recursos existem no banco
- ✅ Relacionamentos são válidos
- ✅ Regras de negócio complexas
- ✅ Duplicidade de dados
- ✅ Estado do sistema (estoque, status)

### **3. Entity (Invariantes de Domínio)**

```java
@Entity
public class Order {
    
    // ✅ Constraints de banco
    @Column(nullable = false)
    private OrderStatus status;
    
    // ✅ Regras que SEMPRE devem ser verdadeiras
    public boolean podeSerCancelada() {
        return this.status == OrderStatus.RECEBIDO ||
               this.status == OrderStatus.EM_DIAGNOSTICO ||
               this.status == OrderStatus.AGUARDANDO_APROVACAO;
    }
    
    // ✅ Métodos que garantem consistência
    public void cancelar() {
        if (!podeSerCancelada()) {
            throw new IllegalStateException("Ordem não pode ser cancelada no status: " + this.status);
        }
        this.desativar();
        this.status = OrderStatus.CANCELADO;
    }
}
```

**Valida:**
- ✅ Invariantes de domínio
- ✅ Transições de estado válidas
- ✅ Constraints de banco de dados

---

## 🚫 O Que NÃO Fazer

### **❌ Validar Null em Path Variables**

```java
// ❌ NÃO FAZER - Spring já converte automaticamente
public OrderDto findById(Long id) {
    if (id == null) {
        throw new IllegalArgumentException("ID não pode ser nulo");
    }
    // ...
}

// ✅ FAZER - Confiar no Spring
public OrderDto findById(Long id) {
    // Spring garante que id não é null se a rota foi chamada
    return orderRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(...));
}
```

### **❌ Duplicar Validações do DTO no Service**

```java
// ❌ NÃO FAZER - Bean Validation já faz isso
public OrderDto insert(OrderCreateDto dto) {
    if (dto.getClienteId() == null) {
        throw new IllegalArgumentException("Cliente ID é obrigatório");
    }
    // ...
}

// ✅ FAZER - Confiar no Bean Validation
public OrderDto insert(OrderCreateDto dto) {
    // @NotNull no DTO já garante que clienteId não é null
    Cliente cliente = clienteRepository.findById(dto.getClienteId())
            .orElseThrow(...);
}
```

### **❌ Validar Formato no Service**

```java
// ❌ NÃO FAZER - Validação de formato pertence ao DTO
public ClienteDto insert(ClienteCreateDto dto) {
    if (!dto.getEmail().contains("@")) {
        throw new ValidationException("Email inválido");
    }
    // ...
}

// ✅ FAZER - Usar Bean Validation no DTO
public class ClienteCreateDto {
    @Email(message = "Email inválido")
    private String email;
}
```

---

## 🎓 Quando Validar no Service

Valide no service **APENAS** quando:

1. **Precisa consultar o banco de dados**
   ```java
   // Verificar se recurso existe
   if (!clienteRepository.existsById(id)) {
       throw new ResourceNotFoundException(...);
   }
   ```

2. **Precisa validar relacionamentos entre entidades**
   ```java
   // Verificar se carro pertence ao cliente
   if (!carro.getCliente().getId().equals(cliente.getId())) {
       throw new BusinessException(...);
   }
   ```

3. **Precisa validar estado do sistema**
   ```java
   // Verificar estoque disponível
   if (!produto.getEstoque().temEstoqueSuficiente(quantidade)) {
       throw new EstoqueInsuficienteException(...);
   }
   ```

4. **Precisa validar duplicidade**
   ```java
   // Verificar se documento já existe
   if (clienteRepository.findByDoc(doc).isPresent()) {
       throw new DuplicateResourceException(...);
   }
   ```

---

## 📈 Impacto no Código

### **Redução de Linhas**

```
OrderService.insert():
- Antes: 139 linhas (com validações redundantes)
- Depois: 116 linhas (focado em negócio)
- Redução: 23 linhas (-16.5%)
```

### **Melhoria na Legibilidade**

**Antes:**
```java
// 15 linhas de validações básicas
if (dto == null) { ... }
if (dto.getClienteId() == null) { ... }
if (dto.getCarroId() == null) { ... }
if (item.getProdutoId() == null) { ... }
if (item.getQuantidade() == null || item.getQuantidade() <= 0) { ... }
// ... finalmente a lógica de negócio
```

**Depois:**
```java
// Direto para a lógica de negócio
Cliente cliente = clienteRepository.findById(dto.getClienteId())...
Carro carro = carroRepository.findById(dto.getCarroId())...
if (!carro.getCliente().getId().equals(cliente.getId())) { ... }
```

---

## ✅ Checklist de Validação

Ao criar um novo endpoint, pergunte-se:

- [ ] **DTO tem `@Valid` no controller?**
- [ ] **Campos obrigatórios têm `@NotNull`?**
- [ ] **Valores numéricos têm `@Positive` ou `@Min`?**
- [ ] **Strings têm `@Size` quando necessário?**
- [ ] **Emails têm `@Email`?**
- [ ] **Objetos aninhados têm `@Valid`?**
- [ ] **Service valida APENAS regras de negócio?**
- [ ] **Entity tem métodos para garantir invariantes?**

---

## 🎯 Conclusão

**Validação em camadas:**
1. **DTO**: Formato e obrigatoriedade (Bean Validation)
2. **Service**: Regras de negócio e estado do sistema
3. **Entity**: Invariantes de domínio

**Resultado:**
- ✅ Código mais limpo e legível
- ✅ Menos duplicação
- ✅ Mais fácil de testar
- ✅ Mais fácil de manter
- ✅ Separação clara de responsabilidades

**Esta é a abordagem recomendada pela comunidade Spring e está alinhada com os princípios SOLID e Clean Code.**
