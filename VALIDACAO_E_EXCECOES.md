# Validação de Dados e Tratamento de Exceções

## Implementações Realizadas

### 1. Exceções Customizadas

Criadas 4 exceções customizadas para melhor tratamento de erros:

- **`ResourceNotFoundException`**: Quando um recurso não é encontrado
- **`BusinessException`**: Para violações de regras de negócio
- **`ValidationException`**: Para erros de validação customizados
- **`DuplicateResourceException`**: Quando há tentativa de criar recurso duplicado

**Localização**: `src/main/java/com/projetoweb/mecanica/exceptions/`

### 2. GlobalExceptionHandler

Implementado `@RestControllerAdvice` para tratamento global de exceções com respostas padronizadas.

**Exceções tratadas**:
- `ResourceNotFoundException` → HTTP 404
- `BusinessException` → HTTP 400
- `ValidationException` → HTTP 400
- `DuplicateResourceException` → HTTP 409
- `MethodArgumentNotValidException` → HTTP 400 (validação Bean Validation)
- `IllegalArgumentException` → HTTP 400
- `IllegalStateException` → HTTP 400
- `Exception` → HTTP 500 (fallback)

**Formato de resposta de erro**:
```json
{
  "timestamp": "2025-11-07T15:30:00",
  "status": 400,
  "error": "Validation Failed",
  "message": "Erro de validação nos campos fornecidos",
  "path": "/clientes",
  "fieldErrors": [
    {
      "field": "nome",
      "message": "Nome é obrigatório",
      "rejectedValue": null
    }
  ]
}
```

### 3. Validadores Customizados

#### CPF/CNPJ Validator
- **Anotação**: `@CpfCnpj`
- **Validação**: Verifica dígitos verificadores de CPF (11 dígitos) e CNPJ (14 dígitos)
- **Aceita**: Formatos com ou sem máscara (pontos, traços)

#### Placa de Veículo Validator
- **Anotação**: `@PlacaVeiculo`
- **Validação**: Aceita placas no formato antigo (ABC-1234) e Mercosul (ABC1D23)
- **Aceita**: Com ou sem hífen

**Localização**: `src/main/java/com/projetoweb/mecanica/validation/`

### 4. Validações nos DTOs

#### ClienteCreateDto
- `nome`: Obrigatório, 3-100 caracteres
- `doc`: Obrigatório, validação CPF/CNPJ
- `telefone`: Obrigatório, formato brasileiro
- `email`: Obrigatório, formato válido, máx 100 caracteres

#### CarroCreateDto
- `modelo`: Obrigatório, 2-50 caracteres
- `marca`: Obrigatória, 2-50 caracteres
- `anoFabricacao`: Obrigatório, entre 1900-2100
- `placa`: Obrigatória, validação de formato
- `cor`: Opcional, máx 30 caracteres
- `clienteId`: Obrigatório

#### ProdutoDto
- `nome`: Obrigatório, 2-100 caracteres
- `preco`: Obrigatório, valor positivo

#### ServicoDto
- `nome`: Obrigatório, 3-100 caracteres
- `descricao`: Opcional, máx 500 caracteres
- `preco`: Obrigatório, valor positivo
- `duracaoMinutos`: Obrigatória, valor positivo

#### OrderCreateDto
- `clienteId`: Obrigatório
- `produtos`: Lista validada com `@Valid`
  - `produtoId`: Obrigatório
  - `quantidade`: Obrigatória, valor positivo
- `servicos`: Lista validada com `@Valid`
  - `servicoId`: Obrigatório
  - `quantidade`: Obrigatória, valor positivo

### 5. Atualização dos Services

Todos os services foram atualizados para usar as exceções customizadas:

- **ClienteService**: Validação de duplicidade de documento e email
- **CarroService**: Validação de duplicidade de placa
- **OrderService**: Uso de `ResourceNotFoundException` em todas as buscas

### 6. Atualização dos Controllers

Adicionado `@Valid` em todos os métodos POST e PUT dos controllers:
- `ClienteController`
- `CarroController`
- `OrderController`
- `ProdutoController`
- `ServicoController`

### 7. Dependências Adicionadas

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
</dependency>
```

## Exemplos de Uso

### Criar Cliente com Validação
```bash
POST /clientes
{
  "nome": "João Silva",
  "doc": "123.456.789-10",
  "telefone": "(11) 98765-4321",
  "email": "joao@email.com"
}
```

**Resposta de erro (400)**:
```json
{
  "timestamp": "2025-11-07T15:30:00",
  "status": 400,
  "error": "Validation Failed",
  "message": "Erro de validação nos campos fornecidos",
  "path": "/clientes",
  "fieldErrors": [
    {
      "field": "doc",
      "message": "CPF ou CNPJ inválido",
      "rejectedValue": "123.456.789-10"
    }
  ]
}
```

### Criar Carro com Placa Inválida
```bash
POST /carros
{
  "modelo": "Civic",
  "marca": "Honda",
  "anoFabricacao": 2020,
  "placa": "INVALIDA",
  "clienteId": 1
}
```

**Resposta de erro (400)**:
```json
{
  "timestamp": "2025-11-07T15:30:00",
  "status": 400,
  "error": "Validation Failed",
  "message": "Erro de validação nos campos fornecidos",
  "path": "/carros",
  "fieldErrors": [
    {
      "field": "placa",
      "message": "Placa de veículo inválida. Use o formato ABC-1234 ou ABC1D23 (Mercosul)",
      "rejectedValue": "INVALIDA"
    }
  ]
}
```

### Recurso Não Encontrado
```bash
GET /clientes/999
```

**Resposta de erro (404)**:
```json
{
  "timestamp": "2025-11-07T15:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Cliente não encontrado(a) com ID: 999",
  "path": "/clientes/999"
}
```

### Recurso Duplicado
```bash
POST /clientes
{
  "nome": "Maria Santos",
  "doc": "12345678900",  // CPF já cadastrado
  "telefone": "(11) 91234-5678",
  "email": "maria@email.com"
}
```

**Resposta de erro (409)**:
```json
{
  "timestamp": "2025-11-07T15:30:00",
  "status": 409,
  "error": "Duplicate Resource",
  "message": "Cliente já existe com documento: 12345678900",
  "path": "/clientes"
}
```

## Benefícios

1. **Validação Automática**: Dados são validados antes de chegarem ao service
2. **Mensagens Claras**: Erros específicos e informativos para o cliente
3. **Código Limpo**: Menos validações manuais nos services
4. **Segurança**: Validação de CPF/CNPJ e placas garante dados consistentes
5. **Manutenibilidade**: Tratamento centralizado de exceções
6. **Padrão REST**: Códigos HTTP apropriados para cada tipo de erro

## Próximos Passos Sugeridos

1. Adicionar validações de negócio mais complexas (ex: estoque)
2. Implementar logs estruturados de erros
3. Adicionar testes unitários para validadores
4. Criar documentação Swagger com exemplos de erros
5. Implementar rate limiting para proteção de APIs
