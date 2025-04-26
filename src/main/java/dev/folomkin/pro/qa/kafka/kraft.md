### 8. Kraft

Отличный вопрос! 🚀

---

## 🧠 Что такое **KRaft** в Kafka?

**KRaft** (Kafka Raft Metadata mode) — это **новый способ управления метаданными
в Apache Kafka**, который **заменяет необходимость в ZooKeeper**.

> Раньше Kafka зависела от **ZooKeeper** для хранения информации о брокерах,
> топиках, ACL и других метаданных.

**KRaft** делает Kafka **самодостаточной**: теперь всё хранится и
синхронизируется **внутри самого кластера Kafka**, без внешнего ZooKeeper.

---

## 📜 KRaft расшифровка:

- **K**afka
- **Raft** — это алгоритм консенсуса для согласованности данных между серверами.

---

## 🔥 Почему Kafka ушла от ZooKeeper?

| Была проблема                              | KRaft решает                                    |
|--------------------------------------------|-------------------------------------------------|
| 🎭 Сложная архитектура (Kafka + ZooKeeper) | 📦 Всё в одной системе Kafka                    |
| 🔄 Медленная реакция на изменения          | ⚡ Быстрая синхронизация через Raft              |
| 🔥 Много ручных настроек ZooKeeper         | 🔧 Проще конфигурировать и масштабировать Kafka |
| 🛡 Возможность расхождения данных          | 🛡 Чистая консистентность через Raft-протокол   |

---

## 🛠 Как работает кластер KRaft?

- Есть **контроллеры** (KRaft controllers) — специальные брокеры, которые
  управляют метаданными;
- Остальные брокеры подключаются к контроллерам;
- Метаданные (топики, ACL, состояния брокеров) хранятся в **логах Raft** и
  синхронизируются между контроллерами.

---

## 📦 Архитектура:

```
Kafka Cluster (KRaft Mode)
┌─────────────────────────────────────────┐
│ Controllers (исполняют Raft-протокол)    │
│  - Broker 1 (Controller)                 │
│  - Broker 2 (Controller)                 │
│  - Broker 3 (Controller)                 │
└─────────────────────────────────────────┘
        ▲
        │
    Brokers (Рабочие узлы)
```

---

## 📋 Основные изменения при использовании KRaft:

| Что меняется                 | Подробности                                                 |
|------------------------------|-------------------------------------------------------------|
| ❌ Нет ZooKeeper              | Kafka сама управляет метаданными                            |
| 📄 Новый формат конфигурации | Нужно указывать `process.roles` (broker, controller)        |
| ⚡ Упрощён старт кластера     | Меньше компонентов, проще деплой                            |
| 🔥 Быстрее failover          | Контроллеры используют Raft для быстрой переизбрании лидера |

---

## 🛠 Минимальная конфигурация для KRaft:

```properties
# application.properties
process.roles=broker,controller
node.id=1
controller.quorum.voters=1@localhost:9093,2@localhost:9094,3@localhost:9095
listeners=PLAINTEXT://:9092,CONTROLLER://:9093
log.dirs=/tmp/kraft-combined-logs
```

(отдельные порты для контроллера и брокера)

---

## 🧪 Состояние:

| Версия Kafka | Состояние KRaft                       |
|--------------|---------------------------------------|
| 2.8.x        | Experimental                          |
| 3.0.x        | Beta                                  |
| 3.3.x+       | Production-ready (готов к продакшену) |

В новых версиях **Kafka 4.0** полностью удалят поддержку ZooKeeper. 🔥

---

## ⚡ Итого:

- Kafka **переходит на KRaft** как на новое ядро управления кластером.
- **ZooKeeper больше не нужен**.
- Упрощение инфраструктуры + повышение производительности + отказоустойчивость.

---

Хочешь, я ещё покажу:

- Минималку `docker-compose` для кластера на KRaft?
- Как выглядит failover контроллера в KRaft?
- Или сравнение логов KRaft vs обычный Kafka-брокер?

💬 Скажи — и подготовлю! 🚀

---------------


Отлично, давай соберем минимальный пример! 🔥

Вот самый простой **`docker-compose.yml`** для запуска **Kafka в режиме KRaft (
без ZooKeeper)** на локальной машине:

---

# 📄 `docker-compose.yml`

```yaml
version: '3.8'

services:
  kafka-1:
    image: confluentinc/cp-kafka:7.4.0
    hostname: kafka-1
    container_name: kafka-1
    ports:
      - "9092:9092"
    environment:
      KAFKA_PROCESS_ROLES: broker,controller
      KAFKA_NODE_ID: 1
      KAFKA_CONTROLLER_QUORUM_VOTERS: 1@kafka-1:9093
      KAFKA_LISTENERS: PLAINTEXT://0.0.0.0:9092,CONTROLLER://0.0.0.0:9093
      KAFKA_LISTENER_SECURITY_PROTOCOL_MAP: PLAINTEXT:PLAINTEXT,CONTROLLER:PLAINTEXT
      KAFKA_CONTROLLER_LISTENER_NAMES: CONTROLLER
      KAFKA_INTER_BROKER_LISTENER_NAME: PLAINTEXT
      KAFKA_LOG_DIRS: /tmp/kraft-combined-logs
      KAFKA_AUTO_CREATE_TOPICS_ENABLE: "true"
    restart: unless-stopped
```

---

# 🛠 Пояснения:

| Переменная                        | Значение                                               |
|-----------------------------------|--------------------------------------------------------|
| `KAFKA_PROCESS_ROLES`             | Указывает роли (`broker`, `controller`)                |
| `KAFKA_NODE_ID`                   | Уникальный ID узла                                     |
| `KAFKA_CONTROLLER_QUORUM_VOTERS`  | Кто участвует в выборах контроллера (`1@kafka-1:9093`) |
| `KAFKA_LISTENERS`                 | Настройка портов для брокера и контроллера             |
| `KAFKA_LOG_DIRS`                  | Где хранить данные                                     |
| `KAFKA_AUTO_CREATE_TOPICS_ENABLE` | Автоматическое создание топиков                        |

---

# 🚀 Как запустить?

```bash
docker-compose up -d
```

Через несколько секунд Kafka будет доступна на `localhost:9092` без ZooKeeper.
🚀

---

# 📈 Если хочешь **кластер** на несколько брокеров?

Просто склонируем сервис с изменёнными:

- `KAFKA_NODE_ID`
- `KAFKA_CONTROLLER_QUORUM_VOTERS`
- `ports`
- именами контейнеров

Могу сразу показать полную версию кластера на 3 брокера/контроллера! 🚀  
Хочешь? 🔥 (будет сразу с балансировкой партиций)

-------------------


Вот пример **`docker-compose.yml`** для запуска кластера Kafka на **KRaft** с
тремя брокерами, которые также выступают контроллерами. 🚀

---

# 📄 `docker-compose.yml`

```yaml
version: '3.8'

services:
  kafka-1:
    image: confluentinc/cp-kafka:7.4.0
    hostname: kafka-1
    container_name: kafka-1
    ports:
      - "9092:9092" # Порт для клиента
      - "9093:9093" # Порт для контроллера
    environment:
      KAFKA_PROCESS_ROLES: broker,controller
      KAFKA_NODE_ID: 1
      KAFKA_CONTROLLER_QUORUM_VOTERS: 1@kafka-1:9093,2@kafka-2:9093,3@kafka-3:9093
      KAFKA_LISTENERS: PLAINTEXT://0.0.0.0:9092,CONTROLLER://0.0.0.0:9093
      KAFKA_LISTENER_SECURITY_PROTOCOL_MAP: PLAINTEXT:PLAINTEXT,CONTROLLER:PLAINTEXT
      KAFKA_CONTROLLER_LISTENER_NAMES: CONTROLLER
      KAFKA_INTER_BROKER_LISTENER_NAME: PLAINTEXT
      KAFKA_LOG_DIRS: /tmp/kraft-combined-logs
      KAFKA_AUTO_CREATE_TOPICS_ENABLE: "true"
    restart: unless-stopped

  kafka-2:
    image: confluentinc/cp-kafka:7.4.0
    hostname: kafka-2
    container_name: kafka-2
    ports:
      - "9094:9092"
      - "9095:9093"
    environment:
      KAFKA_PROCESS_ROLES: broker,controller
      KAFKA_NODE_ID: 2
      KAFKA_CONTROLLER_QUORUM_VOTERS: 1@kafka-1:9093,2@kafka-2:9093,3@kafka-3:9093
      KAFKA_LISTENERS: PLAINTEXT://0.0.0.0:9092,CONTROLLER://0.0.0.0:9093
      KAFKA_LISTENER_SECURITY_PROTOCOL_MAP: PLAINTEXT:PLAINTEXT,CONTROLLER:PLAINTEXT
      KAFKA_CONTROLLER_LISTENER_NAMES: CONTROLLER
      KAFKA_INTER_BROKER_LISTENER_NAME: PLAINTEXT
      KAFKA_LOG_DIRS: /tmp/kraft-combined-logs
      KAFKA_AUTO_CREATE_TOPICS_ENABLE: "true"
    restart: unless-stopped

  kafka-3:
    image: confluentinc/cp-kafka:7.4.0
    hostname: kafka-3
    container_name: kafka-3
    ports:
      - "9096:9092"
      - "9097:9093"
    environment:
      KAFKA_PROCESS_ROLES: broker,controller
      KAFKA_NODE_ID: 3
      KAFKA_CONTROLLER_QUORUM_VOTERS: 1@kafka-1:9093,2@kafka-2:9093,3@kafka-3:9093
      KAFKA_LISTENERS: PLAINTEXT://0.0.0.0:9092,CONTROLLER://0.0.0.0:9093
      KAFKA_LISTENER_SECURITY_PROTOCOL_MAP: PLAINTEXT:PLAINTEXT,CONTROLLER:PLAINTEXT
      KAFKA_CONTROLLER_LISTENER_NAMES: CONTROLLER
      KAFKA_INTER_BROKER_LISTENER_NAME: PLAINTEXT
      KAFKA_LOG_DIRS: /tmp/kraft-combined-logs
      KAFKA_AUTO_CREATE_TOPICS_ENABLE: "true"
    restart: unless-stopped
```

---

# ⚡ Что происходит:

1. **Три брокера**: `kafka-1`, `kafka-2`, `kafka-3`.
2. Все брокеры являются **контроллерами** и участвуют в выборах через **KRaft**.
3. **Контроллеры синхронизируют метаданные** через Raft-протокол.
4. Клиенты могут подключаться к брокерам:
    - `kafka-1`: `localhost:9092`
    - `kafka-2`: `localhost:9094`
    - `kafka-3`: `localhost:9096`

---

# 🚀 Как запустить:

1. **Запуск кластера**:
   ```bash
   docker-compose up -d
   ```

2. **Проверка состояния**:
   Используй встроенные скрипты Kafka для проверки кластера.

   Пример: Список топиков
   ```bash
   docker exec kafka-1 kafka-topics --bootstrap-server kafka-1:9092 --list
   ```

3. **Создание топика**:
   ```bash
   docker exec kafka-1 kafka-topics --bootstrap-server kafka-1:9092 --create --topic test-topic --partitions 3 --replication-factor 3
   ```

4. **Проверка метаданных**:
   ```bash
   docker exec kafka-1 kafka-topics --bootstrap-server kafka-1:9092 --describe --topic test-topic
   ```

---

# 📈 Что дальше?

- Добавить producer/consumer для тестирования?
- Или настройку мониторинга через Prometheus/Grafana?  
  Пиши, сделаем! 🚀

-------------------
Добавить producer/consumer для тестирования?

Отличная идея! 🚀  
Давай сразу сделаем простой **producer** и **consumer** через обычные консольные команды Kafka внутри наших контейнеров.

---

## 📤 **Producer** (отправить сообщение в топик)

1. Откроем терминал внутри контейнера `kafka-1`:

```bash
docker exec -it kafka-1 bash
```

2. Отправляем сообщение через встроенный скрипт:

```bash
kafka-console-producer --bootstrap-server kafka-1:9092 --topic test-topic
```

3. Появится приглашение. Теперь можно писать сообщения вручную:

```plaintext
Hello from producer!
Another message!
```

(каждое сообщение отправляется при нажатии Enter)

> ❗ Если топика `test-topic` нет — создай его командой перед этим:
> ```bash
> kafka-topics --bootstrap-server kafka-1:9092 --create --topic test-topic --partitions 3 --replication-factor 3
> ```

---

## 📥 **Consumer** (читать сообщения из топика)

В другом терминале:

1. Подключись к контейнеру:

```bash
docker exec -it kafka-1 bash
```

2. Запусти consumer, чтобы слушать сообщения:

```bash
kafka-console-consumer --bootstrap-server kafka-1:9092 --topic test-topic --from-beginning
```

Все сообщения, которые отправишь producer'ом, будут видны здесь. 👀

---

## 🔥 Пример:

**Producer терминал**:

```bash
> Hello World
> Kafka is awesome!
```

**Consumer терминал**:

```plaintext
Hello World
Kafka is awesome!
```

---

## 🛠 Автоматизация (одной командой):

Если хочешь, могу собрать маленький **скрипт** для запуска producer/consumer автоматически через `docker-compose exec`, чтобы быстрее тестировать.

Хочешь пример такого скрипта? 🔥  
(буквально `bash`-файл для локального теста)

-------

Отлично! 🔥  
Держи простой скрипт для быстрого тестирования **Kafka Producer** и **Consumer** в нашем docker-compose кластере:

---

# 📜 `kafka-test.sh`

```bash
#!/bin/bash

# Скрипт для быстрой проверки Kafka в режиме KRaft
# Автор: ChatGPT

TOPIC_NAME="test-topic"
BROKER="kafka-1:9092"

echo "🔎 Проверяем наличие топика: $TOPIC_NAME..."

docker exec kafka-1 kafka-topics --bootstrap-server $BROKER --list | grep -w $TOPIC_NAME > /dev/null

if [ $? -ne 0 ]; then
  echo "✅ Топик не найден. Создаём топик: $TOPIC_NAME..."
  docker exec kafka-1 kafka-topics --bootstrap-server $BROKER --create --topic $TOPIC_NAME --partitions 3 --replication-factor 3
else
  echo "✅ Топик уже существует."
fi

echo ""
echo "🚀 Открываем producer для отправки сообщений..."
echo "(В другом терминале откройте consumer!)"
echo ""

# Запускаем producer
docker exec -it kafka-1 kafka-console-producer --bootstrap-server $BROKER --topic $TOPIC_NAME
```

---

# 🛠 Как использовать:

1. Сохрани этот скрипт в файл, например `kafka-test.sh`.
2. Сделай его исполняемым:

```bash
chmod +x kafka-test.sh
```

3. Запусти:

```bash
./kafka-test.sh
```

---

# ➡ Параллельно открой второй терминал для Consumer:

```bash
docker exec -it kafka-1 kafka-console-consumer --bootstrap-server kafka-1:9092 --topic test-topic --from-beginning
```

И ты будешь видеть все сообщения, которые отправляешь! 🎯

---

# 📦 Что автоматизировано:
- Проверка: существует ли топик.
- Автоматическое создание топика (если нет).
- Быстрый запуск producer-а.

---

# ⚡ Бонус

Хочешь, ещё добавлю:
- Скрипт для **массовой отправки сообщений** в топик для нагрузочного теста?
- Или скрипт **одновременного запуска consumer и producer** в отдельных терминалах?

Пиши! 🚀