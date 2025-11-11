# Instalação Manual do Apache Kafka no macOS

## 📥 Opção 1: Instalação via Homebrew (Recomendado)

```bash
# Instalar Kafka (inclui Zookeeper)
brew install kafka

# Verificar instalação
kafka-topics --version
```

Após instalar, os comandos estarão disponíveis:
- `zookeeper-server-start`
- `kafka-server-start`
- `kafka-topics`
- `kafka-console-producer`
- `kafka-console-consumer`

---

## 📦 Opção 2: Download Manual

### Passo 1: Download

1. Acesse: https://kafka.apache.org/downloads
2. Baixe a versão mais recente (ex: kafka_2.13-3.6.1.tgz)
3. Ou use o comando:

```bash
cd ~/Downloads
curl -O https://downloads.apache.org/kafka/3.6.1/kafka_2.13-3.6.1.tgz
```

### Passo 2: Extrair

```bash
# Extrair o arquivo
tar -xzf kafka_2.13-3.6.1.tgz

# Mover para um diretório apropriado
sudo mv kafka_2.13-3.6.1 /usr/local/kafka

# Criar link simbólico (opcional, facilita atualizações)
sudo ln -s /usr/local/kafka /usr/local/kafka-current
```

### Passo 3: Configurar Variáveis de Ambiente

Adicione ao seu `~/.zshrc` ou `~/.bash_profile`:

```bash
# Abrir arquivo de configuração
nano ~/.zshrc

# Adicionar estas linhas:
export KAFKA_HOME=/usr/local/kafka
export PATH=$PATH:$KAFKA_HOME/bin
```

Depois, recarregue o terminal:

```bash
source ~/.zshrc
```

### Passo 4: Verificar Instalação

```bash
# Verificar se os comandos estão disponíveis
kafka-topics.sh --version
```

---

## 🚀 Iniciar Kafka

### Opção A: Com Homebrew

```bash
# Terminal 1 - Iniciar Zookeeper
zookeeper-server-start /usr/local/etc/kafka/zookeeper.properties

# Terminal 2 - Iniciar Kafka
kafka-server-start /usr/local/etc/kafka/server.properties
```

### Opção B: Instalação Manual

```bash
# Terminal 1 - Iniciar Zookeeper
cd /usr/local/kafka
bin/zookeeper-server-start.sh config/zookeeper.properties

# Terminal 2 - Iniciar Kafka
cd /usr/local/kafka
bin/kafka-server-start.sh config/server.properties
```

---

## 🐳 Opção 3: Usar Docker (Mais Fácil!)

Se você tem Docker instalado, esta é a forma mais simples:

### Passo 1: Criar arquivo docker-compose.yml

Crie um arquivo `docker-compose.yml` na raiz do projeto:

```yaml
version: '3'
services:
  zookeeper:
    image: confluentinc/cp-zookeeper:latest
    environment:
      ZOOKEEPER_CLIENT_PORT: 2181
      ZOOKEEPER_TICK_TIME: 2000
    ports:
      - "2181:2181"

  kafka:
    image: confluentinc/cp-kafka:latest
    depends_on:
      - zookeeper
    ports:
      - "9092:9092"
    environment:
      KAFKA_BROKER_ID: 1
      KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181
      KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://localhost:9092
      KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR: 1
      KAFKA_TRANSACTION_STATE_LOG_MIN_ISR: 1
      KAFKA_TRANSACTION_STATE_LOG_REPLICATION_FACTOR: 1
```

### Passo 2: Iniciar com Docker

```bash
# Iniciar Kafka e Zookeeper
docker-compose up -d

# Ver logs
docker-compose logs -f

# Parar
docker-compose down
```

---

## ✅ Verificar se está funcionando

### Criar um tópico de teste

**Com Homebrew:**
```bash
kafka-topics --create --topic test --bootstrap-server localhost:9092 --partitions 1 --replication-factor 1
```

**Com instalação manual:**
```bash
cd /usr/local/kafka
bin/kafka-topics.sh --create --topic test --bootstrap-server localhost:9092 --partitions 1 --replication-factor 1
```

**Com Docker:**
```bash
docker exec -it <container-id-kafka> kafka-topics --create --topic test --bootstrap-server localhost:9092 --partitions 1 --replication-factor 1
```

### Listar tópicos

```bash
kafka-topics --list --bootstrap-server localhost:9092
```

### Testar Producer/Consumer

**Terminal 1 - Producer:**
```bash
kafka-console-producer --topic test --bootstrap-server localhost:9092
```

**Terminal 2 - Consumer:**
```bash
kafka-console-consumer --topic test --from-beginning --bootstrap-server localhost:9092
```

Digite mensagens no producer e veja aparecer no consumer!

---

## 🔧 Configurações Úteis

### Alterar porta do Kafka (se necessário)

Edite o arquivo de configuração:

**Homebrew:** `/usr/local/etc/kafka/server.properties`
**Manual:** `/usr/local/kafka/config/server.properties`

```properties
# Alterar porta
listeners=PLAINTEXT://localhost:9092
```

### Limpar dados do Kafka

```bash
# Parar Kafka e Zookeeper primeiro
# Depois deletar os diretórios de dados

# Homebrew
rm -rf /usr/local/var/lib/kafka-logs
rm -rf /usr/local/var/lib/zookeeper

# Manual
rm -rf /tmp/kafka-logs
rm -rf /tmp/zookeeper
```

---

## 🎯 Recomendação

Para desenvolvimento local, recomendo usar **Docker** (Opção 3) pois:
- ✅ Mais fácil de instalar
- ✅ Não polui o sistema
- ✅ Fácil de iniciar/parar
- ✅ Fácil de limpar dados
- ✅ Mesma versão em todos os ambientes

Se não quiser usar Docker, use **Homebrew** (Opção 1).

---

## 📚 Próximos Passos

Após instalar e iniciar o Kafka:

1. Inicie sua aplicação Spring Boot
2. Teste o endpoint: `http://localhost:8080/api/kafka/test/quick`
3. Verifique os logs da aplicação para ver as mensagens sendo enviadas e recebidas

## 🆘 Problemas Comuns

### "Connection refused" ao iniciar aplicação

- Certifique-se que Zookeeper está rodando
- Certifique-se que Kafka está rodando
- Verifique se a porta 9092 está livre: `lsof -i :9092`

### Kafka não inicia

- Verifique se Zookeeper está rodando primeiro
- Verifique os logs em `/usr/local/var/log/kafka/` (Homebrew)
- Limpe os dados antigos se necessário

### Comandos não encontrados

- Verifique se adicionou o PATH corretamente
- Recarregue o terminal: `source ~/.zshrc`
- Use `.sh` no final dos comandos na instalação manual
