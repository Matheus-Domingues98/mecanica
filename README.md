# Sistema de Gestão de Mecânica 🚗

Sistema completo de gestão para oficinas mecânicas desenvolvido com Spring Boot e MySQL.

## 📋 Funcionalidades

- **Gestão de Clientes**: Cadastro completo com validação de CPF/CNPJ
- **Gestão de Veículos**: Controle de carros vinculados aos clientes
- **Catálogo de Produtos**: Gerenciamento de peças e produtos
- **Catálogo de Serviços**: Cadastro de serviços oferecidos
- **Ordens de Serviço**: Sistema completo de workflow para OS
- **Estatísticas**: Dashboard com métricas do negócio

## 🛠️ Tecnologias

- **Java 17**
- **Spring Boot 3.5.7**
- **Spring Data JPA**
- **MySQL 8.0+**
- **Maven**
- **Bean Validation**
- **ModelMapper**

## 📦 Estrutura do Projeto

```
mecanica/
├── src/main/java/com/projetoweb/mecanica/
│   ├── controllers/      # Endpoints REST
│   ├── services/         # Lógica de negócio
│   ├── repositories/     # Acesso a dados
│   ├── entities/         # Entidades JPA
│   ├── dto/              # Data Transfer Objects
│   ├── mapper/           # Conversão Entity <-> DTO
│   ├── validation/       # Validadores customizados
│   ├── exceptions/       # Tratamento de exceções
│   └── config/           # Configurações
└── src/main/resources/
    └── application.properties
```

## 🚀 Como Executar

### Pré-requisitos

1. **Java 17** instalado
2. **Maven** instalado
3. **MySQL** rodando na porta 3306

### Passo 1: Configurar o Banco de Dados

```sql
-- Criar banco de dados (opcional, será criado automaticamente)
CREATE DATABASE mecanica_db;
```

### Passo 2: Configurar Credenciais

Edite o arquivo `src/main/resources/application.properties` se necessário:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/mecanica_db?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=root
```

### Passo 3: Executar a Aplicação

```bash
# Navegar até o diretório do projeto
cd /Users/mmdomingues/Desktop/Matheus/mecanica

# Executar com Maven
./mvnw spring-boot:run

# Ou compilar e executar
./mvnw clean package
java -jar target/mecanica-0.0.1-SNAPSHOT.jar
```

A aplicação estará disponível em: **http://localhost:8080**

## 📚 Documentação da API

### Endpoints Principais

#### Clientes
- `GET /clientes` - Listar todos
- `GET /clientes/{id}` - Buscar por ID
- `GET /clientes/doc/{doc}` - Buscar por CPF/CNPJ
- `POST /clientes` - Criar novo
- `PUT /clientes/{id}` - Atualizar
- `DELETE /clientes/{id}` - Deletar

#### Carros
- `GET /carros` - Listar todos
- `GET /carros/{id}` - Buscar por ID
- `GET /carros/placa/{placa}` - Buscar por placa
- `POST /carros` - Criar novo
- `PUT /carros/{id}` - Atualizar
- `DELETE /carros/{id}` - Deletar

#### Produtos
- `GET /produtos` - Listar todos
- `GET /produtos/ativos` - Listar ativos
- `GET /produtos/{id}` - Buscar por ID
- `POST /produtos` - Criar novo
- `PUT /produtos/{id}` - Atualizar
- `PATCH /produtos/{id}/desativar` - Desativar
- `PATCH /produtos/{id}/ativar` - Ativar
- `DELETE /produtos/{id}` - Deletar

#### Serviços
- `GET /servicos` - Listar todos
- `GET /servicos/ativos` - Listar ativos
- `GET /servicos/{id}` - Buscar por ID
- `POST /servicos` - Criar novo
- `PUT /servicos/{id}` - Atualizar
- `PATCH /servicos/{id}/desativar` - Desativar
- `PATCH /servicos/{id}/ativar` - Ativar
- `DELETE /servicos/{id}` - Deletar

#### Ordens de Serviço
- `GET /orders` - Listar todas
- `GET /orders/ativos` - Listar ativas
- `GET /orders/{id}` - Buscar por ID
- `GET /orders/cliente/{clienteId}` - Buscar por cliente
- `GET /orders/status/{status}` - Buscar por status
- `GET /orders/statistics` - Obter estatísticas
- `POST /orders` - Criar nova
- `PATCH /orders/{id}/status?status={STATUS}` - Atualizar status
- `PATCH /orders/{id}/enviar-aprovacao` - Enviar para aprovação
- `PATCH /orders/{id}/aprovar-orcamento` - Aprovar orçamento
- `PATCH /orders/{id}/rejeitar-orcamento` - Rejeitar orçamento
- `PATCH /orders/{id}/finalizar` - Finalizar ordem
- `PATCH /orders/{id}/entregar` - Entregar ordem
- `PATCH /orders/{id}/cancelar` - Cancelar ordem
- `DELETE /orders/{id}` - Deletar

## 🧪 Testando com Postman

### Opção 1: Importar Collection

1. Abra o Postman
2. Clique em **Import**
3. Selecione o arquivo `Mecanica_API.postman_collection.json`
4. A collection estará pronta para uso

### Opção 2: Seguir o Guia Manual

Consulte o arquivo `POSTMAN_TESTS.md` para exemplos detalhados de todas as requisições.

## 📊 Fluxo de Ordem de Serviço

```
RECEBIDO
    ↓
EM_DIAGNOSTICO
    ↓
AGUARDANDO_APROVACAO
    ↓
EM_EXECUCAO (após aprovação)
    ↓
FINALIZADO
    ↓
ENTREGUE

* CANCELADO (pode ocorrer antes de EM_EXECUCAO)
```

## ✅ Validações Implementadas

### Cliente
- **Nome**: 3-100 caracteres
- **CPF**: 11 dígitos numéricos
- **CNPJ**: 14 dígitos numéricos
- **Telefone**: Formato (11) 98765-4321 ou 11987654321
- **Email**: Formato válido

### Carro
- **Modelo**: 2-50 caracteres
- **Marca**: 2-50 caracteres
- **Ano**: 1900-2100
- **Placa**: Formato ABC1234 ou ABC1D23 (Mercosul)

### Produto
- **Nome**: 2-100 caracteres
- **Preço**: Maior que zero

### Serviço
- **Nome**: 3-100 caracteres
- **Preço**: Maior que zero
- **Duração**: Maior que zero (em minutos)

## 🔐 Regras de Negócio

### Ordem de Serviço

1. **Criação**: Ordem criada com status `RECEBIDO`
2. **Cálculo Automático**: Valor total calculado automaticamente
3. **Aprovação**: Apenas ordens em `AGUARDANDO_APROVACAO` podem ser aprovadas
4. **Cancelamento**: Apenas antes de `EM_EXECUCAO`
5. **Finalização**: Apenas ordens em `EM_EXECUCAO`
6. **Entrega**: Apenas ordens `FINALIZADAS`

### Produtos e Serviços

- Podem ser desativados sem deletar
- Produtos/serviços inativos não aparecem em listagens de "ativos"
- Mantém histórico em ordens antigas mesmo quando desativados

## 📝 Exemplos de Uso

### Criar um Cliente

```bash
curl -X POST http://localhost:8080/clientes \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "João Silva",
    "doc": "12345678901",
    "telefone": "(11) 98765-4321",
    "email": "joao.silva@email.com"
  }'
```

### Criar uma Ordem de Serviço

```bash
curl -X POST http://localhost:8080/orders \
  -H "Content-Type: application/json" \
  -d '{
    "clienteId": 1,
    "carroId": 1,
    "produtos": [
      {"produtoId": 1, "quantidade": 1}
    ],
    "servicos": [
      {"servicoId": 1, "quantidade": 1}
    ]
  }'
```

## 🐛 Troubleshooting

### Erro de Conexão com MySQL

```bash
# Verificar se MySQL está rodando
mysql.server status

# Iniciar MySQL (Mac)
mysql.server start

# Verificar porta
lsof -i :3306
```

### Erro de Dependências

```bash
# Limpar e reinstalar dependências
./mvnw clean install -U
```

### Recriar Banco de Dados

```sql
DROP DATABASE mecanica_db;
CREATE DATABASE mecanica_db;
```

## 📄 Arquivos de Configuração

- `pom.xml` - Dependências Maven
- `application.properties` - Configurações da aplicação
- `POSTMAN_TESTS.md` - Guia completo de testes
- `Mecanica_API.postman_collection.json` - Collection do Postman

## 🤝 Contribuindo

1. Fork o projeto
2. Crie uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request

## 📞 Suporte

Para dúvidas ou problemas, consulte:
- `POSTMAN_TESTS.md` - Exemplos detalhados
- Logs da aplicação
- Issues do projeto

## 📅 Versão

**v0.0.1-SNAPSHOT** - Versão inicial

---

**Desenvolvido com ☕ e Spring Boot**
