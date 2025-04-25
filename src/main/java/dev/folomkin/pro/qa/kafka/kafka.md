# Kafka

===============================================================================

## Основные компоненты

Вот основные компоненты Apache Kafka — их немного, но они играют ключевые роли:

---

## 🧩 Основные компоненты Kafka:

### 1. **Producer (производитель)**

Отправляет (пишет) сообщения в Kafka-топики.

- Обычно это микросервисы, приложения, скрипты и т.д.
- Сообщения можно отправлять с ключом (для контроля попадания в нужную
  партицию).

---

### 2. **Consumer (потребитель)**

Читает (подписывается на) сообщения из Kafka-топиков.

- Может быть частью **consumer group**, чтобы обрабатывать данные параллельно.
- Гарантирует, что каждое сообщение будет обработано хотя бы одним consumer-ом
  из группы.

---

### 3. **Topic (топик)**

Логическая категория, в которую продюсеры пишут сообщения, а потребители читают.

- Делится на **партиции**.
- Как правило, называют по бизнес-смыслу: `user-events`, `orders`, `logs`.

---

### 4. **Partition (партиция)**

Физическая разбивка топика на части.

- Позволяет масштабировать Kafka.
- Обеспечивает параллелизм и упорядоченность сообщений.

---

### 5. **Broker (брокер)**

Сервер Kafka, который хранит партиции и обрабатывает запросы от клиентов.

- Кластер Kafka состоит из нескольких брокеров.
- Каждый брокер может хранить партиции разных топиков.

---

### 6. **Kafka Cluster (кластер Kafka)**

Набор брокеров, работающих вместе как единое целое.

- Обеспечивает отказоустойчивость и масштабирование.
- Один из брокеров назначается **контроллером** — он управляет распределением
  партиций.

---

### 7. **Zookeeper** *(в старых версиях)*

Раньше использовался для управления метаданными, отслеживания состояния брокеров
и т.д.

> ⚠️ В новых версиях Kafka (начиная с 2.8) есть возможность работать **без
Zookeeper** — это называется **KRaft mode**.

---

### 8. **Consumer Group (группа потребителей)**

Набор consumer-ов, обрабатывающих данные совместно.

- Каждая партиция назначается только одному consumer-у внутри группы.
- Позволяет горизонтально масштабировать обработку сообщений.

---

===============================================================================

## Producer

Конечно! Давай разберём, кто такой **Kafka Producer** и как он работает:

---

## 🧑‍🏭 **Kafka Producer** — это компонент, который **отправляет (публикует)

сообщения** в Kafka-топики.

Простыми словами — **производитель данных**. Он берёт события/сообщения и
передаёт их в Kafka, где их позже прочитают консьюмеры.

---

### 🔧 Что делает продюсер:

1. **Подключается к Kafka** по `bootstrap.servers`.
2. **Выбирает топик** и, при необходимости, ключ (key) и раздел (partition).
3. **Сериализует** сообщение (например, в JSON, Avro, Protobuf и т.п.).
4. **Отправляет** сообщение брокеру Kafka.
5. Получает **подтверждение** доставки (опционально).

---

### 🧱 Пример кода на Python (с использованием `kafka-python`):

```python
from kafka import KafkaProducer
import json

producer = KafkaProducer(
    bootstrap_servers='localhost:9092',
    value_serializer=lambda v: json.dumps(v).encode('utf-8')
)

message = {"event": "user_signup", "user_id": 123}
producer.send('user-events', value=message)

producer.flush()
```

---

### ⚙️ Важные параметры:

- `bootstrap.servers` — адреса Kafka-брокеров.
- `key.serializer` и `value.serializer` — преобразуют данные в байты.
- `acks` — сколько подтверждений ждать:
    - `0` — не ждать;
    - `1` — от лидера;
    - `all` — от всех реплик (надежнее, но медленнее).
- `partitioner` — определяет, в какую партицию отправлять сообщение.

---

### 🎯 Зачем использовать ключ (key)?

Ключ влияет на **партиционирование**. Сообщения с одинаковым ключом будут
попадать в одну и ту же партицию, что сохраняет **порядок**.

```python
producer.send('user-events', key=b'user_123', value=message)
```

---

### ✅ Основные плюсы Kafka Producer:

- Высокая производительность
- Настраиваемая надёжность доставки
- Возможность записи в несколько партиций/топиков
- Поддержка асинхронной и синхронной отправки

---

Если хочешь — могу показать пример на Java, Golang или рассказать, как
протестировать продюсера с помощью Kafka CLI.

----------------

Окей! Давай начнём с **CLI-утилиты**, а потом дам пример на **Java**.

---

## 🧪 🔧 **Тест Kafka Producer через CLI**

Если у тебя установлен Kafka, ты можешь отправить тестовые сообщения с помощью
встроенной команды:

```bash
bin/kafka-console-producer.sh --broker-list localhost:9092 --topic test-topic
```

👉 После запуска просто начинай печатать сообщения в консоль — они будут
публиковаться в Kafka.

> Чтобы потом прочитать их:

```bash
bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic test-topic --from-beginning
```

---

## ☕️ Пример Kafka Producer на **Java**

Используется Kafka Client API (`org.apache.kafka:kafka-clients`).

### 1. **Добавь зависимость (Maven):**

```xml

<dependency>
    <groupId>org.apache.kafka</groupId>
    <artifactId>kafka-clients</artifactId>
    <version>3.7.0</version>
</dependency>
```

---

### 2. **Java-код продюсера:**

```java
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

public class SimpleProducer {
    public static void main(String[] args) {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        KafkaProducer<String, String> producer = new KafkaProducer<>(props);

        for (int i = 0; i < 5; i++) {
            String key = "key-" + i;
            String value = "Hello Kafka " + i;
            producer.send(new ProducerRecord<>("test-topic", key, value));
        }

        producer.close();
    }
}
```

---

## 🛠 Как это протестировать:

1. Запусти Kafka и Zookeeper (или Kafka в KRaft-режиме).
2. Создай топик:

```bash
bin/kafka-topics.sh --create --topic test-topic --bootstrap-server localhost:9092 --partitions 1 --replication-factor 1
```

3. Запусти продюсер и наблюдай, как данные попадают в Kafka.

---

Хочешь ещё пример на Go, Node.js, или объяснение, как отправлять сообщения в *
*автоматическом режиме (по расписанию)**?


----

Отличный вопрос! 👌  
Ты можешь **автоматически отправлять сообщения в Kafka по расписанию** с помощью
таймеров, cron-подобных задач или шедулеров, в зависимости от языка.

Вот варианты для популярных технологий:

---

## 🧠 Общая идея:

Тебе нужно:

1. Настроить Kafka Producer.
2. Использовать таймер (например, каждую минуту или раз в 10 секунд).
3. Генерировать/читать данные и отправлять их в Kafka.

---

## 🐍 Пример на **Python** (каждые 5 секунд)

```python
from kafka import KafkaProducer
import json
import time
from datetime import datetime

producer = KafkaProducer(
    bootstrap_servers='localhost:9092',
    value_serializer=lambda v: json.dumps(v).encode('utf-8')
)

def send_message():
    message = {
        "timestamp": datetime.now().isoformat(),
        "status": "heartbeat"
    }
    producer.send('scheduled-events', value=message)
    print("Message sent:", message)

try:
    while True:
        send_message()
        time.sleep(5)  # Пауза 5 секунд
except KeyboardInterrupt:
    print("Stopped.")
    producer.close()
```

---

## ☕️ Пример на **Java** (с использованием `ScheduledExecutorService`)

```java
import org.apache.kafka.clients.producer.*;

import java.util.*;
import java.util.concurrent.*;

public class ScheduledKafkaProducer {
    public static void main(String[] args) {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        KafkaProducer<String, String> producer = new KafkaProducer<>(props);

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        Runnable task = () -> {
            String message = "Scheduled message at " + new Date();
            producer.send(new ProducerRecord<>("scheduled-events", message));
            System.out.println("Sent: " + message);
        };

        scheduler.scheduleAtFixedRate(task, 0, 10, TimeUnit.SECONDS);  // Каждые 10 сек
    }
}
```

---

## 🕐 Использование **cron** (системный подход)

Можно создать отдельный скрипт, и запускать его по расписанию через cron (
Linux/macOS):

```bash
# Открыть crontab:
crontab -e

# Добавить строку (пример: запуск каждую минуту):
* * * * * /usr/bin/python3 /home/user/send_kafka.py
```

---

Если скажешь, какой язык ты используешь — подкину тебе пример под него.  
Также можно использовать фреймворки вроде **Apache NiFi**, **Airflow**, или даже
**Spring Boot + Scheduler** для более продвинутых задач.


---
Отлично, тогда вот конкретно для **Java** несколько вариантов, как отправлять
сообщения в Kafka **по расписанию**:

---

## ✅ Вариант 1: **ScheduledExecutorService** (встроенный инструмент)

### 📄 Пример: отправка каждые 10 секунд

```java
import org.apache.kafka.clients.producer.*;

import java.util.*;
import java.util.concurrent.*;

public class ScheduledKafkaProducer {

    public static void main(String[] args) {
        // Конфигурация Kafka Producer
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        KafkaProducer<String, String> producer = new KafkaProducer<>(props);

        // Создание планировщика задач
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

        Runnable task = () -> {
            String message = "Auto message at " + new Date();
            ProducerRecord<String, String> record = new ProducerRecord<>("auto-topic", message);
            producer.send(record, (metadata, exception) -> {
                if (exception == null) {
                    System.out.println("✅ Sent: " + message);
                } else {
                    System.err.println("❌ Error: " + exception.getMessage());
                }
            });
        };

        // Расписание: запуск задачи каждые 10 секунд
        scheduler.scheduleAtFixedRate(task, 0, 10, TimeUnit.SECONDS);

        // Пример: остановка по таймеру через 1 минуту (опционально)
        scheduler.schedule(() -> {
            scheduler.shutdown();
            producer.close();
            System.out.println("Producer & Scheduler stopped.");
        }, 1, TimeUnit.MINUTES);
    }
}
```

---

## ✅ Вариант 2: **Spring Boot + @Scheduled**

Если ты используешь **Spring Boot**, это вообще идеально — просто добавляешь
аннотацию `@Scheduled`.

### 📄 Пример:

**1. Конфигурация**

```java

@Configuration
@EnableScheduling
public class KafkaConfig {

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate(ProducerFactory<String, String> factory) {
        return new KafkaTemplate<>(factory);
    }

    @Bean
    public ProducerFactory<String, String> producerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return new DefaultKafkaProducerFactory<>(config);
    }
}
```

**2. Продюсер с расписанием**

```java

@Component
public class ScheduledProducer {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Scheduled(fixedRate = 5000)  // Каждые 5 секунд
    public void sendMessage() {
        String message = "Scheduled Spring message at " + new Date();
        kafkaTemplate.send("auto-topic", message);
        System.out.println("📤 Sent: " + message);
    }
}
```

> 🔄 Не забудь добавить `@EnableScheduling` в конфигурацию и зависимость
`spring-kafka` в `pom.xml`.

---

## Хочешь?

- пример с чтением из базы и отправкой в Kafka?
- расписание по cron-выражению (`@Scheduled(cron = "...")`)?

Скажи, и я подкину точечный пример.


----------

Давай посмотрим, как можно связать **чтение данных из базы** и **отправку
сообщений в Kafka** на Java. Предположим, что ты используешь Spring Boot и базу
данных (например, PostgreSQL).

---

### 📚 **Предположим следующее:**

- База данных содержит таблицу `users` с колонками `id`, `name`, `email`.
- Нужно регулярно читать данные из таблицы и отправлять их в Kafka.

---

### 🔧 **Настройка проекта**

**1. Зависимости в `pom.xml`:**

```xml

<dependencies>
    <!-- Spring Boot -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.kafka</groupId>
        <artifactId>spring-kafka</artifactId>
    </dependency>
    <!-- PostgreSQL драйвер -->
    <dependency>
        <groupId>org.postgresql</groupId>
        <artifactId>postgresql</artifactId>
        <version>42.6.0</version>
    </dependency>
</dependencies>
```

---

**2. Настройка `application.yml`:**

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/mydb
    username: your_username
    password: your_password
  jpa:
    hibernate:
      ddl-auto: none
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect

kafka:
  bootstrap-servers: localhost:9092
  topic: users-topic
```

---

### 🛠 **Код приложения**

#### **1. Модель данных `User`**

```java
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class User {
    @Id
    private Long id;
    private String name;
    private String email;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
```

---

#### **2. Репозиторий для работы с базой данных**

```java
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
```

---

#### **3. Kafka Producer**

```java
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${kafka.topic}")
    private String topic;

    public KafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String message) {
        kafkaTemplate.send(topic, message);
        System.out.println("📤 Sent to Kafka: " + message);
    }
}
```

---

#### **4. Сервис для чтения из базы и отправки в Kafka**

```java
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final KafkaProducer kafkaProducer;
    private final ObjectMapper objectMapper;

    public UserService(UserRepository userRepository, KafkaProducer kafkaProducer, ObjectMapper objectMapper) {
        this.userRepository = userRepository;
        this.kafkaProducer = kafkaProducer;
        this.objectMapper = objectMapper;
    }

    @Scheduled(fixedRate = 10000) // Каждые 10 секунд
    public void fetchAndSendUsers() {
        List<User> users = userRepository.findAll();
        users.forEach(user -> {
            try {
                String userJson = objectMapper.writeValueAsString(user);
                kafkaProducer.sendMessage(userJson);
            } catch (Exception e) {
                System.err.println("❌ Error serializing user: " + e.getMessage());
            }
        });
    }
}
```

---

#### **5. Конфигурация для Kafka и расписания**

```java
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.HashMap;
import java.util.Map;

import static org.apache.kafka.clients.producer.ProducerConfig.*;

@Configuration
@EnableScheduling
public class KafkaConfig {

    public ProducerFactory<String, String> producerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        config.put(KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        config.put(VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        return new DefaultKafkaProducerFactory<>(config);
    }

    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}
```

---

### 🚀 **Как это работает:**

1. **`UserService`**:

- Каждые 10 секунд (или любое другое время) запускает метод
  `fetchAndSendUsers()`.
- Забирает всех пользователей из базы через `UserRepository`.
- Сериализует объект `User` в JSON.
- Отправляет его в Kafka через `KafkaProducer`.

2. Kafka принимает сообщения и сохраняет их в топик `users-topic`.

---

### 🎯 Расширения:

- Можно добавить фильтрацию (например, отправлять только новых пользователей).
- Установить cron-выражение вместо `fixedRate`:
  ```java
  @Scheduled(cron = "0 0/1 * * * ?") // Каждую минуту
  ```
- Добавить логирование с помощью **SLF4J** или **Logback**.

Если тебе нужны доработки или ещё примеры — дай знать!

===============================================================================

## Consumer (потребитель)

Конечно! Давай разложим всё по полочкам — кто такой **Kafka Consumer**, как он
работает и как его использовать на практике, особенно в Java 👇

---

## 🧾 Что такое **Kafka Consumer**?

**Kafka Consumer** — это компонент, который **читает сообщения** из
Kafka-топиков. Он слушает один или несколько топиков, получает новые сообщения и
обрабатывает их.

> 💬 Проще говоря: продюсер — это тот, кто пишет в Kafka, а консьюмер — тот, кто
> читает.

---

## 🔄 Основной цикл работы:

1. Подключается к Kafka через `bootstrap.servers`.
2. Подписывается на топик(и) (`subscribe()`).
3. Получает сообщения из партиций топика.
4. Обрабатывает их.
5. Коммитит смещения (offset) — говорит Kafka, что это сообщение уже прочитано.

---

## 🧱 Пример на **Java (Plain Java без Spring)**

```java
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class SimpleConsumer {
    public static void main(String[] args) {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("group.id", "my-consumer-group");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("auto.offset.reset", "earliest");  // с самого начала

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Collections.singletonList("my-topic"));  // Подписка на 1 топик

        try {
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));  // Забираем сообщения
                for (ConsumerRecord<String, String> record : records) {
                    System.out.printf("📥 Received: key=%s, value=%s, partition=%d, offset=%d%n",
                            record.key(), record.value(), record.partition(), record.offset());
                }
                consumer.commitSync(); // Подтверждение прочтения
            }
        } finally {
            consumer.close();
        }
    }
}
```

---

## 🧠 Ключевые параметры консьюмера:

| Параметр             | Назначение                                                        |
|----------------------|-------------------------------------------------------------------|
| `bootstrap.servers`  | Список Kafka-брокеров                                             |
| `group.id`           | Идентификатор группы консьюмеров                                  |
| `auto.offset.reset`  | Что делать, если нет сохраненного смещения (`earliest`, `latest`) |
| `enable.auto.commit` | Автоматически ли коммитить offset (по умолчанию `true`)           |
| `max.poll.records`   | Сколько сообщений за раз можно прочитать                          |

---

## 👥 Что такое **Consumer Group**?

- Kafka позволяет **нескольким консьюмерам** объединяться в **группу** (
  `group.id`).
- Каждая партиция одного топика обрабатывается **только одним** консьюмером из
  группы.
- Это даёт **параллелизм и масштабирование**.

### Пример:

Если у топика 3 партиции и 3 консьюмера в группе — каждый будет обрабатывать по
одной партиции.

---

## ✅ Пример на Spring Boot (очень просто!)

```java

@KafkaListener(topics = "my-topic", groupId = "my-group")
public void listen(String message) {
    System.out.println("📥 Spring Consumer received: " + message);
}
```

> Работает автоматически, если у тебя есть `KafkaTemplate`, конфигурация и
> аннотация `@EnableKafka`.

---

## Хочешь:

- пример консьюмера, читающего JSON?
- пример с ручным управлением offset?
- использование Spring Kafka Listener с фильтрацией сообщений?

-------------------
Отличный выбор — **Spring Kafka + фильтрация сообщений в Listener'е** — это
очень удобно и гибко!  
Сейчас покажу тебе, как сделать так, чтобы Kafka-консьюмер **получал только
нужные сообщения**, прямо во время обработки.

---

## 🎯 Что мы хотим:

Предположим, сообщения — это JSON-объекты, например:

```json
{
  "type": "USER_CREATED",
  "userId": 123
}
```

И мы хотим, чтобы Listener **обрабатывал только сообщения
с `"type": "USER_CREATED"`**, игнорируя всё остальное.

---

## 🛠 Вариант 1: Фильтрация через

`RecordFilterStrategy` (на уровне KafkaListenerContainer)

Это делается через **конфигурацию**.

### 🔧 Конфигурация:

```java
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.listener.RecordFilterStrategy;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConsumerConfig {

    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "my-group");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        return new org.springframework.kafka.core.DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
        var factory = new ConcurrentKafkaListenerContainerFactory<String, String>();
        factory.setConsumerFactory(consumerFactory());

        // 🔍 Добавляем фильтр
        factory.setRecordFilterStrategy((record) -> {
            String value = record.value();
            return !value.contains("\"type\":\"USER_CREATED\"");
        });

        return factory;
    }
}
```

> ⚠️ Важно: фильтр работает ДО вызова `@KafkaListener`. То есть сообщения, не
> прошедшие фильтр, **не попадут в метод**.

---

## 🎙 Kafka Listener:

```java
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class UserEventListener {

    @KafkaListener(topics = "user-events", containerFactory = "kafkaListenerContainerFactory")
    public void handleUserCreated(String message) {
        System.out.println("✅ Обрабатываю сообщение: " + message);
    }
}
```

---

## 🧠 Как работает:

- Kafka получает сообщение.
- Фильтр проверяет, содержит ли оно `"type": "USER_CREATED"`.
- Только если да — `@KafkaListener` его получает.
- Остальные просто игнорируются.

---

## 🧪 Вариант 2: Фильтрация в самом методе `@KafkaListener`

Можно фильтровать и вручную внутри метода, особенно если тебе нужно более
сложная логика:

```java

@KafkaListener(topics = "user-events", groupId = "manual-filter")
public void listen(String message) {
    if (message.contains("\"type\":\"USER_CREATED\"")) {
        System.out.println("✅ Обрабатываю USER_CREATED: " + message);
    } else {
        System.out.println("❌ Пропускаю: " + message);
    }
}
```

Но такой способ не экономит ресурсы — Kafka всё равно передаёт сообщение.

---

## Хочешь усложнить?

- Сделаем парсинг JSON с `ObjectMapper`?
- Фильтрацию по времени или пользовательским полям?
- Или фильтрацию **по key**, а не по значению?

Скажи — и добавим!

## Сделаем парсинг JSON с `ObjectMapper`?

Конечно! Давай покажу, как можно **парсить JSON-сообщения** в Java с помощью *
*Jackson `ObjectMapper`**, особенно в контексте Spring Kafka Listener.

---

## 🎯 Цель:

Получаем сообщение в формате JSON:

```json
{
  "type": "USER_CREATED",
  "userId": 123,
  "name": "Alice"
}
```

Парсим его в Java-объект и обрабатываем только нужные события.

---

## 🧱 1. Модель (POJO) — `UserEvent`

```java
public class UserEvent {
    private String type;
    private Long userId;
    private String name;

    // Getters и Setters
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "UserEvent{type=" + type + ", userId=" + userId + ", name=" + name + "}";
    }
}
```

---

## 🧠 2. Kafka Listener с `ObjectMapper`

```java
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class UserEventConsumer {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = "user-events", groupId = "json-group")
    public void handleEvent(String message) {
        try {
            UserEvent event = objectMapper.readValue(message, UserEvent.class);

            if ("USER_CREATED".equals(event.getType())) {
                System.out.println("✅ Обрабатываем нового пользователя: " + event);
            } else {
                System.out.println("🔕 Игнорируем тип события: " + event.getType());
            }

        } catch (Exception e) {
            System.err.println("❌ Ошибка при парсинге JSON: " + e.getMessage());
        }
    }
}
```

---

## 🛠 Что происходит:

1. Kafka получает JSON-строку.
2. `ObjectMapper.readValue()` превращает её в `UserEvent`.
3. Внутри метода ты можешь удобно работать с Java-объектом, а не с "сырой"
   строкой.
4. Можно фильтровать, логировать, сохранять в БД и т.д.

---

## 💡 Совет:

Если ты часто работаешь с JSON-сообщениями, можно обобщить ObjectMapper через
`@Bean`:

```java

@Bean
public ObjectMapper objectMapper() {
    return new ObjectMapper();
}
```

И затем внедрять его через `@Autowired`.

---

Хочешь пример с **Jackson + LocalDate**, **вложенными объектами**, или *
*массивами** в JSON?  
Я с радостью покажу!






-------------------

===============================================================================

## Topic (топик)

Конечно! Давай разберёмся, **что такое топик в Apache Kafka** — это один из
ключевых элементов всей архитектуры. Объясню простыми словами 👇

---

## 📦 Что такое **топик (topic)** в Kafka?

**Топик** — это логическая категория (или канал), куда продюсеры отправляют
сообщения, и из которой консьюмеры читают их.

> 🔁 Kafka — это как система сообщений. А **топик — это как папка или категория
сообщений**.

---

## 🎯 Пример:

Представь, что у тебя есть приложение для онлайн-магазина.  
Ты можешь создать такие топики:

- `orders` — для новых заказов
- `payments` — для оплаты
- `shipments` — для доставки

Продюсеры пишут сообщения в нужный топик, а консьюмеры обрабатывают их.

---

## 🧩 Как устроен топик внутри?

Каждый топик состоит из **одной или нескольких партиций**:

```
Topic: "orders"
├── Partition 0 → [msg1, msg2, msg3, ...]
├── Partition 1 → [msg4, msg5, ...]
├── Partition 2 → [msg6, msg7, ...]
```

Партиции позволяют:

- 📈 **масштабировать нагрузку** (консьюмеры обрабатывают данные параллельно)
- 💾 **хранить сообщения независимо** (сообщения остаются в Kafka по времени или
  объёму, а не "поглощаются", как в очереди)

---

## 🕒 Хранение сообщений в топике

- Kafka **не удаляет** сообщение сразу после обработки.
- Сообщения могут храниться **по времени** (например, 7 дней) или по размеру (
  например, 1 ГБ).
- Консьюмер сам запоминает, **до какого сообщения он дошёл** (offset).

---

## ⚙️ Настройки топика (примеры)

| Настройка            | Значение                                          |
|----------------------|---------------------------------------------------|
| `partitions`         | Кол-во партиций в топике                          |
| `replication.factor` | Кол-во копий каждого сообщения                    |
| `retention.ms`       | Сколько времени хранить сообщения                 |
| `cleanup.policy`     | Что делать при переполнении (`delete`, `compact`) |

---

## 🛠 Создание топика (CLI):

```bash
kafka-topics.sh --create \
  --bootstrap-server localhost:9092 \
  --replication-factor 1 \
  --partitions 3 \
  --topic orders
```

---

## 👥 Один топик — много подписчиков

Kafka — это **публикация/подписка (pub/sub)**.  
Ты можешь создать **несколько консьюмер-групп**, и каждый будет читать один и
тот же топик **независимо**.

> Например: топик `orders` читается одной группой для логирования, другой — для
> аналитики, третьей — для выполнения заказов.

---

## 🧠 Вопросы на подумать:

- Хочешь узнать, как **настроить ретеншн** (время хранения сообщений)?
- Как сделать **топик с дедупликацией** (`compaction`)?
- Или как просмотреть/удалить топики?

Скажи, и я покажу!

## - Или как просмотреть/удалить топики?

Конечно! Давай покажу, как **просматривать и удалять топики в Apache Kafka** с
помощью командной строки (CLI). Это суперполезно для отладки и администрирования
🔧

---

## 📋 Как **просмотреть список всех топиков**

```bash
kafka-topics.sh --list --bootstrap-server localhost:9092
```

> Покажет список всех топиков, доступных на указанном брокере.

---

## 🔍 Как **посмотреть информацию о конкретном топике**

```bash
kafka-topics.sh --describe --topic my-topic --bootstrap-server localhost:9092
```

Это выведет информацию:

- количество партиций
- уровень репликации
- лидеры и ISR
- и другую системную информацию

### Пример вывода:

```
Topic: my-topic   PartitionCount: 3   ReplicationFactor: 1
  Partition: 0    Leader: 0   Replicas: 0   Isr: 0
  Partition: 1    Leader: 0   Replicas: 0   Isr: 0
  Partition: 2    Leader: 0   Replicas: 0   Isr: 0
```

---

## 🗑 Как **удалить топик**

```bash
kafka-topics.sh --delete --topic my-topic --bootstrap-server localhost:9092
```

> ⚠️ Удаление работает **только если** в `server.properties` на брокере
> установлено:

```properties
delete.topic.enable=true
```

Если это выключено, Kafka не будет физически удалять топики.

---

## 🧼 Альтернатива: удаление всех сообщений (без удаления топика)

Если хочешь **очистить топик**, но не удалять его:

```bash
kafka-delete-records.sh --bootstrap-server localhost:9092 --offset-json-file delete.json
```

И файл `delete.json`:

```json
{
  "partitions": [
    {
      "topic": "my-topic",
      "partition": 0,
      "offset": 100
    }
  ]
}
```

Это удалит все сообщения **до offset 100**.

---

## 📦 Где находятся эти команды?

Все эти утилиты (`kafka-topics.sh`, `kafka-delete-records.sh`) находятся в
папке:

```
$KAFKA_HOME/bin
```

На Windows — это `.bat` файлы: `kafka-topics.bat`, и т.д.

---

## ❓ Хочешь ещё:

- пример на **Java AdminClient** для управления топиками?
- как **создать топик программно**?
- как **просмотреть сообщения внутри топика**?

Скажи, и я покажу 🙂





> - ## - как **создать топик программно**?
Конечно! Создать топик программно можно с помощью Kafka Admin API — это удобно,
если ты хочешь управлять топиками прямо из Java-приложения, без вызова скриптов
CLI.

---

## 🧱 Зависимость (Maven)

Сначала подключи нужную зависимость, если ещё не подключил Kafka:

```xml

<dependency>
    <groupId>org.apache.kafka</groupId>
    <artifactId>kafka-clients</artifactId>
    <version>3.6.0</version> <!-- или последняя доступная -->
</dependency>
```

Если ты используешь Spring Boot, и у тебя уже подключен `spring-kafka`, то эта
зависимость подтянется автоматически.

---

## ✅ Пример: создание топика с Java

```java
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;

import java.util.Collections;
import java.util.Properties;

public class KafkaTopicCreator {
    public static void main(String[] args) {
        // 1. Настройки подключения
        Properties config = new Properties();
        config.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");

        // 2. Создаём AdminClient
        try (AdminClient admin = AdminClient.create(config)) {

            // 3. Создаём топик: имя, партиции, репликация
            NewTopic newTopic = new NewTopic("my-new-topic", 3, (short) 1);

            // 4. Отправляем запрос на создание
            admin.createTopics(Collections.singletonList(newTopic)).all().get();

            System.out.println("✅ Топик успешно создан!");
        } catch (Exception e) {
            System.err.println("❌ Ошибка при создании топика: " + e.getMessage());
        }
    }
}
```

---

## ⚙️ Аргументы `NewTopic`

```java
new NewTopic("имя_топика",кол-во_партиций, фактор_репликации)
```

- `"my-topic"` — имя топика
- `3` — количество партиций
- `(short) 1` — фактор репликации (на сколько брокеров копируется)

---

## 📦 Пример с кастомными настройками:

Можно добавить настройки, например, время хранения сообщений:

```java
NewTopic topic = new NewTopic("my-topic", 3, (short) 1)
        .configs(Map.of(
                "retention.ms", "3600000",      // хранить 1 час
                "cleanup.policy", "delete"      // удалять старые сообщения
        ));
```

---

## 💡 Полезно:

- Код можно вызывать при старте приложения, чтобы проверять и создавать топики "
  на лету"
- Можно также использовать Spring Kafka `KafkaAdmin` — хочешь пример на Spring
  Boot?

---

Если хочешь пример на **удаление топика через Java**, или создание топика *
*только если он не существует** — я тоже покажу!



> - ## *удаление топика через Java

Конечно! Удалить Kafka-топик через Java тоже можно с помощью `AdminClient` из
Kafka Admin API. Это почти так же просто, как его создать.

---

## 🧱 Пример: удаление топика через Java

```java
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;

import java.util.Collections;
import java.util.Properties;

public class KafkaTopicDeleter {
    public static void main(String[] args) {
        // 1. Настройка подключения
        Properties config = new Properties();
        config.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");

        // 2. Создаём AdminClient
        try (AdminClient admin = AdminClient.create(config)) {

            // 3. Удаляем топик
            String topicToDelete = "my-topic";
            admin.deleteTopics(Collections.singletonList(topicToDelete)).all().get();

            System.out.println("🗑️ Топик \"" + topicToDelete + "\" успешно удалён.");
        } catch (Exception e) {
            System.err.println("❌ Ошибка при удалении топика: " + e.getMessage());
        }
    }
}
```

---

## ⚠️ Важно: удаление должно быть разрешено

На брокере Kafka в конфигурации должно быть включено:

```properties
delete.topic.enable=true
```

Если это значение `false`, то Kafka не будет физически удалять топики — они
останутся как "marked for deletion", но не исчезнут.

---

## ✅ Советы

- Перед удалением можно вызвать `listTopics()` и проверить наличие топика.
- Можно удалять сразу несколько топиков (списком).
- Если удаляемый топик не существует — будет выброшено исключение
  `UnknownTopicOrPartitionException`.

---

Конечно! Удалить Kafka-топик через Java тоже можно с помощью `AdminClient` из
Kafka Admin API. Это почти так же просто, как его создать.

---

## 🧱 Пример: удаление топика через Java

```java
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;

import java.util.Collections;
import java.util.Properties;

public class KafkaTopicDeleter {
    public static void main(String[] args) {
        // 1. Настройка подключения
        Properties config = new Properties();
        config.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");

        // 2. Создаём AdminClient
        try (AdminClient admin = AdminClient.create(config)) {

            // 3. Удаляем топик
            String topicToDelete = "my-topic";
            admin.deleteTopics(Collections.singletonList(topicToDelete)).all().get();

            System.out.println("🗑️ Топик \"" + topicToDelete + "\" успешно удалён.");
        } catch (Exception e) {
            System.err.println("❌ Ошибка при удалении топика: " + e.getMessage());
        }
    }
}
```

---

## ⚠️ Важно: удаление должно быть разрешено

На брокере Kafka в конфигурации должно быть включено:

```properties
delete.topic.enable=true
```

Если это значение `false`, то Kafka не будет физически удалять топики — они
останутся как "marked for deletion", но не исчезнут.

---

## ✅ Советы

- Перед удалением можно вызвать `listTopics()` и проверить наличие топика.
- Можно удалять сразу несколько топиков (списком).
- Если удаляемый топик не существует — будет выброшено исключение
  `UnknownTopicOrPartitionException`.

---

## 🔍 Хочешь дополнительно:

- Проверить, существует ли топик перед удалением?
- Удалить *все* топики, удовлетворяющие шаблону?
- Пример с использованием Spring Kafka `KafkaAdmin`?

Я могу всё это показать — просто скажи!

> ## - Пример с использованием Spring Kafka `KafkaAdmin`?
Конечно! Вот как можно **удалять топики программно** с использованием **Spring
Kafka** и `KafkaAdmin`. Это ещё удобнее, особенно если ты уже используешь Spring
Boot.

---

## 📦 Зависимости (если ещё не добавлены)

Если ты используешь Maven:

```xml

<dependency>
    <groupId>org.springframework.kafka</groupId>
    <artifactId>spring-kafka</artifactId>
</dependency>
```

---

## ⚙️ 1. Конфигурация `KafkaAdmin`

Создаём бин `KafkaAdmin`, который будет использоваться для административных
операций (в том числе удаления топиков):

```java
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        return new KafkaAdmin(configs);
    }
}
```

---

## 🧽 2. Удаление топика с использованием `KafkaAdmin`

Spring Kafka не предоставляет метод `deleteTopic(...)` напрямую, но ты можешь
получить `AdminClient` из `KafkaAdmin` и использовать стандартный API Kafka:

```java
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.DeleteTopicsResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.concurrent.ExecutionException;

@Service
public class TopicManagementService {

    @Autowired
    private KafkaAdmin kafkaAdmin;

    public void deleteTopic(String topicName) {
        try (AdminClient adminClient = AdminClient.create(kafkaAdmin.getConfig())) {
            DeleteTopicsResult result = adminClient.deleteTopics(Collections.singletonList(topicName));
            result.all().get(); // блокирующий вызов — ждём завершения
            System.out.println("🗑️ Топик \"" + topicName + "\" успешно удалён.");
        } catch (ExecutionException ee) {
            System.err.println("❌ Ошибка Kafka при удалении: " + ee.getCause().getMessage());
        } catch (Exception e) {
            System.err.println("❌ Ошибка при удалении топика: " + e.getMessage());
        }
    }
}
```

---

## 🧪 Использование:

Например, можешь вызвать этот метод из REST-контроллера, сервиса, или прямо из
`@PostConstruct`:

```java

@Component
public class Demo {

    @Autowired
    private TopicManagementService service;

    @PostConstruct
    public void testDelete() {
        service.deleteTopic("my-topic");
    }
}
```

---

## 🛡 Проверка перед удалением (опционально):

Хочешь — можем добавить проверку на существование топика перед удалением.
Скажи — и я покажу.

---

## ✅ Итого:

- Удаление топика легко делается с помощью `KafkaAdmin + AdminClient`.
- Удобно интегрируется в Spring-приложение.
- Можно расширить и использовать для создания, проверки, описания и т.п.

Хочешь в этот сервис добавить **создание топика**, **проверку существования**
или **очистку по шаблону**?


> - ## что такое KafkaAdmin  и  AdminClient?

Отличный вопрос! 🤓 Давай разберёмся, чем отличаются **`KafkaAdmin`** и *
*`AdminClient`**, и **какую роль они играют в Apache Kafka и Spring Kafka**.

---

## 🛠️ `AdminClient` (из Apache Kafka)

### 📌 Что это такое?

`AdminClient` — это **низкоуровневый Kafka API**, предоставляемый самой Apache
Kafka (`org.apache.kafka.clients.admin.AdminClient`), который позволяет
выполнять **административные операции**, такие как:

| Операция                  | Пример                |
|---------------------------|-----------------------|
| 📦 Создание топиков       | `createTopics(...)`   |
| 🗑️ Удаление топиков      | `deleteTopics(...)`   |
| 🔍 Получение информации   | `describeTopics(...)` |
| ✅ Проверка наличия топика | `listTopics()`        |

### 🔧 Пример (без Spring):

```java
AdminClient admin = AdminClient.create(props);
admin.

createTopics(...);
admin.

deleteTopics(...);
```

---

## 🌱 `KafkaAdmin` (из Spring Kafka)

### 📌 Что это такое?

`KafkaAdmin` — это **обёртка в Spring Kafka** (
`org.springframework.kafka.core.KafkaAdmin`), которая:

- инкапсулирует настройки `bootstrap.servers`
- позволяет Spring'у **автоматически создавать топики** при запуске
- может быть использован для создания `AdminClient`

### ✅ Главная фишка:

Если ты зарегистрируешь `KafkaAdmin` как бин, Spring Boot может **сам создать
топики**, если ты также зарегистрируешь `NewTopic`-бины:

```java

@Bean
public NewTopic topic() {
    return new NewTopic("auto-created-topic", 1, (short) 1);
}
```

---

## 🧩 Связь между ними

- `KafkaAdmin` — это **Spring-обёртка** для конфигурации
- `AdminClient` — это **реальный клиент**, который делает всю работу

Их можно использовать **вместе**:

```java
AdminClient adminClient = AdminClient.create(kafkaAdmin.getConfig());
```

---

## 💡 Когда использовать что?

| Сценарий                                        | Используй                       |
|-------------------------------------------------|---------------------------------|
| Простой Spring Boot проект                      | `KafkaAdmin` + `@Bean NewTopic` |
| Нужны динамические операции (удаление/проверка) | `AdminClient`                   |
| Работа вне Spring (plain Java)                  | `AdminClient`                   |

---

## Хочешь больше?

- Хочешь пример автоматического **создания нескольких топиков через Spring**?
- Или **как безопасно проверять, существует ли топик перед удалением**?

Я могу показать — скажи только 🙂

> - ## пример автоматического создания нескольких топиков через Spring

Конечно! 🎯 Вот как можно **автоматически создать несколько топиков в Spring Boot
** с использованием `KafkaAdmin` и `NewTopic` бинов. Spring Kafka сам создаст их
при запуске приложения — удобно и прозрачно 🙌

---

## ✅ Шаги:

### 📦 1. Подключи зависимость `spring-kafka` (если ещё не подключена)

```xml

<dependency>
    <groupId>org.springframework.kafka</groupId>
    <artifactId>spring-kafka</artifactId>
</dependency>
```

---

### ⚙️ 2. Конфигурация Kafka и автоматическое создание топиков

```java
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaTopicConfig {

    // 1. Настройки KafkaAdmin
    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        return new KafkaAdmin(configs);
    }

    // 2. Автоматическое создание топиков
    @Bean
    public NewTopic ordersTopic() {
        return new NewTopic("orders", 3, (short) 1); // 3 партиции, 1 реплика
    }

    @Bean
    public NewTopic paymentsTopic() {
        return new NewTopic("payments", 2, (short) 1);
    }

    @Bean
    public NewTopic shipmentsTopic() {
        return new NewTopic("shipments", 1, (short) 1);
    }
}
```

---

### 🔄 Что произойдёт при старте Spring Boot?

- Spring создаст подключение к Kafka через `KafkaAdmin`.
- Все `NewTopic` бины будут обработаны.
- Если топика ещё **нет на брокере** — он будет **создан автоматически**.
- Если топик уже есть — **ничего не произойдёт** (ошибок не будет).

---

## ⚠️ Обрати внимание

- Топики **создаются только при первом запуске**, если их не было.
- Убедись, что у Kafka-брокера включено создание топиков (или отключено
  авто-создание, если хочешь это делать явно).

---

## 💡 Хочешь расширить?

- Добавить **конфигурации хранения сообщений** (`retention.ms`,
  `cleanup.policy`)?
- Или пример **динамического создания топиков во время выполнения**?

Скажи, и я покажу 😊



> - ## создание топика **только если он не существует

Отлично! Ниже пример на Java, как **создать Kafka-топик программно, только если
он ещё не существует**, с использованием `AdminClient` из Kafka (в Spring Boot
это тоже можно легко использовать).

---

## ✅ Пример: создание топика, только если он не существует

```java
import org.apache.kafka.clients.admin.*;

import java.util.*;
import java.util.concurrent.ExecutionException;

public class SafeTopicCreator {

    public static void main(String[] args) {
        String bootstrapServers = "localhost:9092";
        String topicName = "my-unique-topic";

        Properties props = new Properties();
        props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);

        try (AdminClient adminClient = AdminClient.create(props)) {
            // Получаем список уже существующих топиков
            Set<String> existingTopics = adminClient.listTopics().names().get();

            if (!existingTopics.contains(topicName)) {
                System.out.println("🚀 Топик \"" + topicName + "\" не существует. Создаём...");

                NewTopic newTopic = new NewTopic(topicName, 3, (short) 1);
                adminClient.createTopics(Collections.singletonList(newTopic)).all().get();

                System.out.println("✅ Топик успешно создан!");
            } else {
                System.out.println("⚠️ Топик \"" + topicName + "\" уже существует. Пропускаем создание.");
            }

        } catch (ExecutionException ee) {
            System.err.println("❌ Ошибка Kafka: " + ee.getCause().getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

---

### 🧠 Что делает этот код?

1. Получает список всех топиков с `adminClient.listTopics().names()`.
2. Проверяет, есть ли нужный топик в списке.
3. Только если его **нет** — создаёт новый `NewTopic`.

---

### ⚠️ Важно:

- Убедись, что у Kafka-брокера не включено `auto.create.topics.enable=true`,
  иначе Kafka может "по-тихому" создать топик, как только ты попытаешься в него
  писать.
- Если Kafka работает в кластере, укажи несколько `bootstrap.servers`.

---

## 🔄 Хочешь Spring-версию того же подхода?

Могу показать, как сделать проверку и создание через `KafkaAdmin` в Spring
Boot — скажи только!

> - ## Spring-версию того же подхода?


Отлично! Сейчас покажу, как реализовать **создание Kafka-топика в Spring Boot
только если он не существует**, с использованием `KafkaAdmin` и `AdminClient`.

---

## ✅ Пример: Spring Boot + KafkaAdmin + проверка существования топика

### 📦 1. Конфигурация KafkaAdmin (если ещё нет)

```java
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        return new KafkaAdmin(configs);
    }
}
```

---

### ⚙️ 2. Сервис для безопасного создания топика

```java
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;

@Service
public class TopicManager {

    @Autowired
    private KafkaAdmin kafkaAdmin;

    public void createTopicIfNotExists(String topicName, int partitions, short replicationFactor) {
        try (AdminClient adminClient = AdminClient.create(kafkaAdmin.getConfig())) {
            Set<String> existingTopics = adminClient.listTopics().names().get();

            if (!existingTopics.contains(topicName)) {
                NewTopic topic = new NewTopic(topicName, partitions, replicationFactor);
                adminClient.createTopics(Collections.singletonList(topic)).all().get();
                System.out.println("✅ Топик \"" + topicName + "\" успешно создан.");
            } else {
                System.out.println("⚠️ Топик \"" + topicName + "\" уже существует.");
            }

        } catch (Exception e) {
            System.err.println("❌ Ошибка при создании топика: " + e.getMessage());
        }
    }
}
```

---

### 🧪 3. Использование (например, при старте приложения)

```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class TopicInitializer {

    @Autowired
    private TopicManager topicManager;

    @PostConstruct
    public void init() {
        topicManager.createTopicIfNotExists("my-topic", 3, (short) 1);
        topicManager.createTopicIfNotExists("another-topic", 1, (short) 1);
    }
}
```

---

## 📌 Итого:

- Используется Spring Kafka (`KafkaAdmin`) для конфигурации.
- Создание топика происходит **только при его отсутствии**.
- Это удобно, когда ты не хочешь использовать авто-создание Kafka или хочешь
  полный контроль над параметрами.

---

Хочешь дополнительно:

- добавим кастомные параметры топика (`retention.ms`, `cleanup.policy`)?
- или удаление по шаблону, или REST-контроллер для управления топиками?

Готов расширить, если нужно!


> - ## или удаление по шаблону, или REST-контроллер для управления топиками?

Отлично! Сейчас покажу **два примера**:

1. ✅ **Удаление топиков по шаблону** — например, удалить все, начинающиеся с
   `test_`
2. 🌐 **REST-контроллер для управления топиками** — создать, удалить, просмотреть

---

## ✅ 1. Удаление топиков по шаблону

```java
import org.apache.kafka.clients.admin.AdminClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PatternTopicDeleter {

    @Autowired
    private KafkaAdmin kafkaAdmin;

    public void deleteTopicsMatchingPattern(String prefix) {
        try (AdminClient adminClient = AdminClient.create(kafkaAdmin.getConfig())) {
            Set<String> allTopics = adminClient.listTopics().names().get();

            Set<String> topicsToDelete = allTopics.stream()
                    .filter(name -> name.startsWith(prefix))
                    .collect(Collectors.toSet());

            if (!topicsToDelete.isEmpty()) {
                adminClient.deleteTopics(topicsToDelete).all().get();
                System.out.println("🗑️ Удалены топики: " + topicsToDelete);
            } else {
                System.out.println("🔍 Нет топиков с префиксом: " + prefix);
            }

        } catch (Exception e) {
            System.err.println("❌ Ошибка при удалении топиков: " + e.getMessage());
        }
    }
}
```

---

## 🌐 2. REST-контроллер для управления топиками

```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/kafka/topics")
public class TopicController {

    @Autowired
    private TopicManager topicManager;

    @Autowired
    private PatternTopicDeleter patternTopicDeleter;

    @PostMapping("/create")
    public String createTopic(@RequestParam String name,
                              @RequestParam(defaultValue = "1") int partitions,
                              @RequestParam(defaultValue = "1") short replication) {
        topicManager.createTopicIfNotExists(name, partitions, replication);
        return "OK";
    }

    @DeleteMapping("/delete")
    public String deleteByPrefix(@RequestParam String prefix) {
        patternTopicDeleter.deleteTopicsMatchingPattern(prefix);
        return "Deleted if matched";
    }
}
```

---

### 🔧 Использование через Postman / curl:

- Создание:
  ```bash
  curl -X POST "http://localhost:8080/kafka/topics/create?name=test_topic&partitions=3&replication=1"
  ```

- Удаление всех топиков, начинающихся с `test_`:
  ```bash
  curl -X DELETE "http://localhost:8080/kafka/topics/delete?prefix=test_"
  ```

---

## 🔒 Опционально: защита

Ты можешь добавить:

- аутентификацию (Spring Security)
- логгирование / аудит
- защиту от удаления системных топиков (например, фильтр
  `!topic.startsWith("_")`)

---

Хочешь я соберу весь код в единый минимальный Spring Boot проект или могу
добавить Swagger-документацию к этим endpoint'ам?

> - ## минимальный Spring Boot проект

```java
// 📁 src/main/java/com/example/kafkaadmin/KafkaAdminApplication.java
package com.example.kafkaadmin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class KafkaAdminApplication {
    public static void main(String[] args) {
        SpringApplication.run(KafkaAdminApplication.class, args);
    }
}

// 📁 src/main/java/com/example/kafkaadmin/config/KafkaConfig.java
package com.example.kafkaadmin.config;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {
    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        return new KafkaAdmin(configs);
    }
}

// 📁 src/main/java/com/example/kafkaadmin/service/TopicManager.java
package com.example.kafkaadmin.service;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;

@Service
public class TopicManager {

    @Autowired
    private KafkaAdmin kafkaAdmin;

    public void createTopicIfNotExists(String topicName, int partitions, short replicationFactor) {
        try (AdminClient adminClient = AdminClient.create(kafkaAdmin.getConfig())) {
            Set<String> existingTopics = adminClient.listTopics().names().get();
            if (!existingTopics.contains(topicName)) {
                NewTopic topic = new NewTopic(topicName, partitions, replicationFactor);
                adminClient.createTopics(Collections.singletonList(topic)).all().get();
                System.out.println("✅ Топик '" + topicName + "' создан.");
            } else {
                System.out.println("⚠️ Топик '" + topicName + "' уже существует.");
            }
        } catch (Exception e) {
            System.err.println("❌ Ошибка создания топика: " + e.getMessage());
        }
    }
}

// 📁 src/main/java/com/example/kafkaadmin/service/PatternTopicDeleter.java
package com.example.kafkaadmin.service;

import org.apache.kafka.clients.admin.AdminClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PatternTopicDeleter {

    @Autowired
    private KafkaAdmin kafkaAdmin;

    public void deleteTopicsMatchingPattern(String prefix) {
        try (AdminClient adminClient = AdminClient.create(kafkaAdmin.getConfig())) {
            Set<String> allTopics = adminClient.listTopics().names().get();
            Set<String> topicsToDelete = allTopics.stream()
                    .filter(name -> name.startsWith(prefix))
                    .collect(Collectors.toSet());

            if (!topicsToDelete.isEmpty()) {
                adminClient.deleteTopics(topicsToDelete).all().get();
                System.out.println("🗑️ Удалены топики: " + topicsToDelete);
            } else {
                System.out.println("🔍 Нет топиков с префиксом: " + prefix);
            }
        } catch (Exception e) {
            System.err.println("❌ Ошибка при удалении топиков: " + e.getMessage());
        }
    }
}

// 📁 src/main/java/com/example/kafkaadmin/controller/TopicController.java
package com.example.kafkaadmin.controller;

import com.example.kafkaadmin.service.PatternTopicDeleter;
import com.example.kafkaadmin.service.TopicManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation .*;

@RestController
@RequestMapping("/kafka/topics")
public class TopicController {

    @Autowired
    private TopicManager topicManager;

    @Autowired
    private PatternTopicDeleter patternTopicDeleter;

    @PostMapping("/create")
    public String createTopic(@RequestParam String name,
                              @RequestParam(defaultValue = "1") int partitions,
                              @RequestParam(defaultValue = "1") short replication) {
        topicManager.createTopicIfNotExists(name, partitions, replication);
        return "OK";
    }

    @DeleteMapping("/delete")
    public String deleteTopicsByPrefix(@RequestParam String prefix) {
        patternTopicDeleter.deleteTopicsMatchingPattern(prefix);
        return "Удаление завершено";
    }
}

```

Готово! Я собрал для тебя полноценный минимальный Spring Boot проект, который:

📦 Создаёт Kafka топики (только если не существуют)

🗑 Удаляет топики по префиксу

🌐 Предоставляет REST API (/kafka/topics/create и /kafka/topics/delete

> - ## Swagger UI

```java
// 📁 src/main/java/com/example/kafkaadmin/KafkaAdminApplication.java
package com.example.kafkaadmin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Kafka Admin API", version = "1.0", description = "Управление Kafka топиками"))
public class KafkaAdminApplication {
    public static void main(String[] args) {
        SpringApplication.run(KafkaAdminApplication.class, args);
    }
}

// 📁 src/main/java/com/example/kafkaadmin/config/KafkaConfig.java
package com.example.kafkaadmin.config;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {
    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        return new KafkaAdmin(configs);
    }
}

// 📁 src/main/java/com/example/kafkaadmin/config/OpenApiConfig.java
package com.example.kafkaadmin.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("Kafka Admin API")
                .version("1.0")
                .description("REST API для управления Kafka топиками"));
    }
}

// 📁 src/main/java/com/example/kafkaadmin/service/TopicManager.java
package com.example.kafkaadmin.service;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;

@Service
public class TopicManager {

    @Autowired
    private KafkaAdmin kafkaAdmin;

    public void createTopicIfNotExists(String topicName, int partitions, short replicationFactor) {
        try (AdminClient adminClient = AdminClient.create(kafkaAdmin.getConfig())) {
            Set<String> existingTopics = adminClient.listTopics().names().get();
            if (!existingTopics.contains(topicName)) {
                NewTopic topic = new NewTopic(topicName, partitions, replicationFactor);
                adminClient.createTopics(Collections.singletonList(topic)).all().get();
                System.out.println("✅ Топик '" + topicName + "' создан.");
            } else {
                System.out.println("⚠️ Топик '" + topicName + "' уже существует.");
            }
        } catch (Exception e) {
            System.err.println("❌ Ошибка создания топика: " + e.getMessage());
        }
    }
}

// 📁 src/main/java/com/example/kafkaadmin/service/PatternTopicDeleter.java
package com.example.kafkaadmin.service;

import org.apache.kafka.clients.admin.AdminClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PatternTopicDeleter {

    @Autowired
    private KafkaAdmin kafkaAdmin;

    public void deleteTopicsMatchingPattern(String prefix) {
        try (AdminClient adminClient = AdminClient.create(kafkaAdmin.getConfig())) {
            Set<String> allTopics = adminClient.listTopics().names().get();
            Set<String> topicsToDelete = allTopics.stream()
                    .filter(name -> name.startsWith(prefix))
                    .collect(Collectors.toSet());

            if (!topicsToDelete.isEmpty()) {
                adminClient.deleteTopics(topicsToDelete).all().get();
                System.out.println("🗑️ Удалены топики: " + topicsToDelete);
            } else {
                System.out.println("🔍 Нет топиков с префиксом: " + prefix);
            }
        } catch (Exception e) {
            System.err.println("❌ Ошибка при удалении топиков: " + e.getMessage());
        }
    }
}

// 📁 src/main/java/com/example/kafkaadmin/controller/TopicController.java
package com.example.kafkaadmin.controller;

import com.example.kafkaadmin.service.PatternTopicDeleter;
import com.example.kafkaadmin.service.TopicManager;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation .*;

@RestController
@RequestMapping("/kafka/topics")
@Tag(name = "Kafka Topic Admin", description = "Операции для управления топиками")
public class TopicController {

    @Autowired
    private TopicManager topicManager;

    @Autowired
    private PatternTopicDeleter patternTopicDeleter;

    @PostMapping("/create")
    @Operation(summary = "Создать топик")
    public String createTopic(@RequestParam String name,
                              @RequestParam(defaultValue = "1") int partitions,
                              @RequestParam(defaultValue = "1") short replication) {
        topicManager.createTopicIfNotExists(name, partitions, replication);
        return "OK";
    }

    @DeleteMapping("/delete")
    @Operation(summary = "Удалить топики по префиксу")
    public String deleteTopicsByPrefix(@RequestParam String prefix) {
        patternTopicDeleter.deleteTopicsMatchingPattern(prefix);
        return "Удаление завершено";
    }
}

```

-----------------------------------------------

Вот как добавить **Swagger UI** в наш проект для документирования REST API:

---

## ✅ Шаги:

### 📦 1. Добавь зависимости Swagger в `pom.xml`

```xml

<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.1.0</version>
</dependency>
```

---

### ⚙️ 2. Добавь базовую конфигурацию (опционально)

Springdoc автоматически подключает Swagger UI по умолчанию. Однако, для
кастомизации можно добавить конфигурацию:

```java
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Kafka Topic Manager API")
                        .version("1.0")
                        .description("API для управления Kafka-топиками (создание, удаление по шаблону)"));
    }
}
```

---

### 🌐 3. Swagger UI: где найти?

После запуска приложения Swagger UI будет доступен по адресу:

```
http://localhost:8080/swagger-ui/index.html
```

---

### 🧪 4. Обновлённый REST API (с описаниями для Swagger)

```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/kafka/topics")
public class TopicController {

    @Autowired
    private TopicManager topicManager;

    @Autowired
    private PatternTopicDeleter patternTopicDeleter;

    @PostMapping("/create")
    public String createTopic(
            @RequestParam String name,
            @RequestParam(defaultValue = "1") int partitions,
            @RequestParam(defaultValue = "1") short replication) {
        topicManager.createTopicIfNotExists(name, partitions, replication);
        return "Топик создан или уже существует.";
    }

    @DeleteMapping("/delete")
    public String deleteByPrefix(@RequestParam String prefix) {
        patternTopicDeleter.deleteTopicsMatchingPattern(prefix);
        return "Топики с указанным префиксом удалены (если были).";
    }
}
```

---

## 💡 Что теперь?

1. Запусти приложение и
   открой [Swagger UI](http://localhost:8080/swagger-ui/index.html).
2. Попробуй выполнить команды создания и удаления топиков прямо из Swagger
   интерфейса.
3. Могу добавить Docker-композит для запуска Kafka + Spring?

> - ## Docker-композит для запуска Kafka + Spring?

Супер! Вот тебе **полный `docker-compose.yml`** для запуска **Kafka +
Zookeeper + Spring Boot приложения**, чтобы всё работало из коробки 🚀

---

## 📁 Структура проекта

```
project-root/
├── docker-compose.yml
├── src/ (твой Spring Boot код)
├── pom.xml
└── Dockerfile
```

---

## 🐳 1. `docker-compose.yml`

```yaml
version: '3.8'

services:

  zookeeper:
    image: confluentinc/cp-zookeeper:7.4.0
    hostname: zookeeper
    container_name: zookeeper
    ports:
      - "2181:2181"
    environment:
      ZOOKEEPER_CLIENT_PORT: 2181

  kafka:
    image: confluentinc/cp-kafka:7.4.0
    hostname: kafka
    container_name: kafka
    ports:
      - "9092:9092"
      - "29092:29092"  # для доступа из других контейнеров
    environment:
      KAFKA_BROKER_ID: 1
      KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181
      KAFKA_LISTENER_SECURITY_PROTOCOL_MAP: PLAINTEXT:PLAINTEXT,PLAINTEXT_INTERNAL:PLAINTEXT
      KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://localhost:9092,PLAINTEXT_INTERNAL://kafka:29092
      KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR: 1
      KAFKA_AUTO_CREATE_TOPICS_ENABLE: "false"
    depends_on:
      - zookeeper

  spring-app:
    build: ..
    container_name: spring-kafka-app
    ports:
      - "8080:8080"
    depends_on:
      - kafka
    environment:
      SPRING_KAFKA_BOOTSTRAP_SERVERS: kafka:29092
```

---

## 🐳 2. `Dockerfile` для твоего Spring Boot приложения

```Dockerfile
FROM openjdk:17-jdk-slim
VOLUME /tmp
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

---

## ⚙️ 3. `application.yml` (Spring Boot)

```yaml
spring:
  kafka:
    bootstrap-servers: ${SPRING_KAFKA_BOOTSTRAP_SERVERS:localhost:9092}
    client-id: topic-manager
    properties:
      security.protocol: PLAINTEXT
```

> ❗ Если запускаешь **локально без Docker**, переменная
`SPRING_KAFKA_BOOTSTRAP_SERVERS` просто не подставится, и будет использоваться
`localhost:9092`.

---

## 🚀 Как запустить всё вместе

```bash
# 1. Собери jar-файл
mvn clean package

# 2. Запусти всё
docker-compose up --build
```

---

## ✅ Что получаешь:

- 📦 Kafka и Zookeeper внутри Docker
- 🌱 Spring Boot приложение, связанное с Kafka
- 🔗 Swagger доступен по
  адресу: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

---

Хочешь я подготовлю `Makefile`, `README.md` или автоматическую инициализацию
Kafka-топиков в Docker?


> - ## Конфиг из application.yml 🛠


Конечно! Вот пример **полного `application.yml`** для твоего Spring Boot
Kafka-проекта, включая:

- подключение к Kafka
- настройку Spring Kafka Listener
- гибкую работу с Docker через переменные окружения
- (опционально) Swagger Info

---

## ✅ `src/main/resources/application.yml`

```yaml
server:
  port: 8080

spring:
  kafka:
    bootstrap-servers: ${SPRING_KAFKA_BOOTSTRAP_SERVERS:localhost:9092}
    client-id: kafka-topic-manager
    consumer:
      group-id: topic-manager-group
      auto-offset-reset: earliest
      key-deserializer: org.apache.kafka.common.serialization.StringDeserializer
      value-deserializer: org.apache.kafka.common.serialization.StringDeserializer
    producer:
      key-serializer: org.apache.kafka.common.serialization.StringSerializer
      value-serializer: org.apache.kafka.common.serialization.StringSerializer
    listener:
      missing-topics-fatal: false
      ack-mode: record

logging:
  level:
    org.apache.kafka: WARN
    org.springframework.kafka: INFO
    com.yourapp: DEBUG

# Swagger OpenAPI (если нужно кастомизировать)
springdoc:
  api-docs:
    path: /v3/api-docs
  swagger-ui:
    path: /swagger-ui.html
    operationsSorter: method
    tagsSorter: alpha
  info:
    title: Kafka Topic Manager API
    description: API для создания и удаления Kafka топиков
    version: 1.0
```

---

## 🔍 Что делает этот конфиг?

| Раздел                  | Описание                                                                                                  |
|-------------------------|-----------------------------------------------------------------------------------------------------------|
| `bootstrap-servers`     | использует `localhost:9092` по умолчанию, но в Docker — подставляется `${SPRING_KAFKA_BOOTSTRAP_SERVERS}` |
| `consumer` и `producer` | задаёт сериализаторы/десериализаторы                                                                      |
| `listener.ack-mode`     | задаёт как KafkaListener обрабатывает сообщения (например: `record`, `batch`, `manual`)                   |
| `logging.level`         | помогает отфильтровать логи Kafka/Spring                                                                  |
| `springdoc`             | настраивает Swagger UI и OpenAPI описание                                                                 |

---

## 💡 Хочешь:

> - ## вынести параметры в `.env` файл для Docker?

Отлично, вот как ты можешь **вынести параметры Kafka и Spring Boot в `.env` файл
** и подключить их в `docker-compose.yml` — это упростит настройку и позволит
легко менять конфигурацию без правки кода.

---

## 📁 Структура проекта с `.env`

```
project-root/
├── .env
├── docker-compose.yml
├── Dockerfile
├── src/
├── pom.xml
└── application.yml
```

---

## ✅ 1. `.env` файл (в корне проекта)

```env
# Kafka
SPRING_KAFKA_BOOTSTRAP_SERVERS=kafka:29092

# Spring
SPRING_APP_PORT=8080
```

---

## ⚙️ 2. `docker-compose.yml` с использованием `.env`

```yaml
version: '3.8'

services:

  zookeeper:
    image: confluentinc/cp-zookeeper:7.4.0
    container_name: zookeeper
    ports:
      - "2181:2181"
    environment:
      ZOOKEEPER_CLIENT_PORT: 2181

  kafka:
    image: confluentinc/cp-kafka:7.4.0
    container_name: kafka
    ports:
      - "9092:9092"
      - "29092:29092"
    environment:
      KAFKA_BROKER_ID: 1
      KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181
      KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://localhost:9092,PLAINTEXT_INTERNAL://kafka:29092
      KAFKA_LISTENER_SECURITY_PROTOCOL_MAP: PLAINTEXT:PLAINTEXT,PLAINTEXT_INTERNAL:PLAINTEXT
      KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR: 1
      KAFKA_AUTO_CREATE_TOPICS_ENABLE: "false"
    depends_on:
      - zookeeper

  spring-app:
    build: .
    container_name: spring-kafka-app
    ports:
      - "${SPRING_APP_PORT}:8080"
    depends_on:
      - kafka
    environment:
      SPRING_KAFKA_BOOTSTRAP_SERVERS: ${SPRING_KAFKA_BOOTSTRAP_SERVERS}
```

---

## 🔧 3. `application.yml` (Spring Boot)

```yaml
server:
  port: ${SPRING_APP_PORT:8080}

spring:
  kafka:
    bootstrap-servers: ${SPRING_KAFKA_BOOTSTRAP_SERVERS:localhost:9092}
```

---

## 🚀 Как запустить?

```bash
# 1. Собери jar-файл
mvn clean package

# 2. Запусти всё с переменными из .env
docker-compose --env-file .env up --build
```

---

## ✅ Преимущества:

- Можно использовать **разные `.env` файлы для окружений** (dev, test, prod)
- Удобно для CI/CD
- Гибкость при переносе проекта

---

> - ## пример `.env.prod` и `docker-compose.override.yml` для
прод-сборки.

Отлично! Вот как можно организовать **отдельный прод-окружение** с `.env.prod` и
`docker-compose.override.yml`. Это поможет тебе **гибко переключаться между
dev/prod**, например для деплоя на сервер или в облако 🚀

---

## 📁 Структура проекта с прод-конфигурацией

```
project-root/
├── .env               ← настройки по умолчанию (dev)
├── .env.prod          ← настройки для продакшена
├── docker-compose.yml
├── docker-compose.override.yml  ← специфичен для прода
├── Dockerfile
├── src/
└── pom.xml
```

---

## ✅ 1. `.env.prod`

```env
# Production Kafka (например, внешний брокер или облако)
SPRING_KAFKA_BOOTSTRAP_SERVERS=kafka.prod.internal:9092

# Production Spring Boot port (можно 80 или 443)
SPRING_APP_PORT=8080
```

---

## ⚙️ 2. `docker-compose.override.yml`

Это файл, который автоматически подхватывается Docker при запуске, если ты
используешь
`docker-compose -f docker-compose.yml -f docker-compose.override.yml`.

```yaml
version: '3.8'

services:
  spring-app:
    environment:
      SPRING_PROFILES_ACTIVE: prod
    ports:
      - "${SPRING_APP_PORT}:8080"
    restart: always
    logging:
      driver: "json-file"
      options:
        max-size: "10m"
        max-file: "3"
```

> Здесь можно также указать:
> - healthcheck'и
> - volume'ы
> - подключение к внешней Kafka (например, облачной)

---

## 🚀 Как запустить в прод-режиме

```bash
# 1. Собери приложение
mvn clean package

# 2. Запусти с прод-переменными
docker-compose --env-file .env.prod -f docker-compose.yml -f docker-compose.override.yml up --build -d
```

---

## 🧠 Советы:

- `.env` — по умолчанию для локальной разработки
- `.env.prod` — для продакшена (или можно сделать `.env.dev`, `.env.staging` и
  т.д.)
- `docker-compose.override.yml` — для продовых настроек (авторестарт, логгинг,
  профили и пр.)

---

Хочешь — добавлю:

- `Makefile` для запуска `make dev` / `make prod`
- CI/CD деплой в Docker Hub или сервер
- Health check API и логирование в файл

> - ## как **просмотреть сообщения внутри топика**?

Чтобы **просмотреть сообщения внутри Kafka топика**, есть несколько способов —
как из консоли, так и программно. Вот основные:

---

## 🛠️ 1. **Через консоль Kafka (CLI)**

Если у тебя установлен Kafka локально или в Docker:

```bash
kafka-console-consumer.sh \
  --bootstrap-server localhost:9092 \
  --topic <имя_топика> \
  --from-beginning
```

🔹 Параметр `--from-beginning` позволяет прочитать все сообщения с начала.

> В Docker:

```bash
docker exec -it kafka \
  kafka-console-consumer --bootstrap-server localhost:9092 \
  --topic your_topic --from-beginning
```

---

## 🧪 2. **Пример на Java (с использованием Consumer API)**

```java
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class TopicReader {

    public static void main(String[] args) {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "debug-reader");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Collections.singletonList("your_topic"));

        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(1));
            for (ConsumerRecord<String, String> record : records) {
                System.out.printf("📨 Message: key=%s, value=%s, offset=%d%n",
                        record.key(), record.value(), record.offset());
            }
        }
    }
}
```

---

## 🌐 3. **Через Web-интерфейсы**

Если хочешь UI:

- [AKHQ (Kafka HQ)](https://akhq.io)
- [Kafka Tool](https://www.kafkatool.com/)
- [Conduktor](https://www.conduktor.io/)
- [Redpanda Console](https://redpanda.com)

> Их можно запускать как Docker-контейнер и подключать к своим брокерам.

---

## 🔐 Примечание:

- Просмотр работает только если сообщения ещё не удалены (по TTL или по размеру
  логов).
- Убедись, что у тебя есть доступ к брокеру Kafka (без SSL/SASL или с нужной
  конфигурацией).

---

> - ## пример Spring Kafka Listener для дебага?
Вот тебе простой и удобный **Spring Kafka Listener**, который логирует все
входящие сообщения — идеально для **отладки (debug)** 🐞

---

## ✅ Пример: Kafka Listener в Spring Boot

```java
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class DebugKafkaListener {

    private static final Logger log = LoggerFactory.getLogger(DebugKafkaListener.class);

    @KafkaListener(
            topics = "your_topic",
            groupId = "debug-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void listen(ConsumerRecord<String, String> record) {
        log.info("📥 Получено сообщение: key='{}', value='{}', offset={}, partition={}, topic={}",
                record.key(), record.value(), record.offset(), record.partition(), record.topic());
    }
}
```

---

## 🛠️ Дополнительно: конфигурация Listener Factory (если нужно)

```java
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConsumerConfig {

    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "debug-group");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }
}
```

---

## 🔍 Что ты получаешь:

- Логирование всех сообщений (даже с оффсетом и партицией)
- Готово для подмены топика, групп и прочего
- Можно расширить фильтрацией, JSON-парсингом, пересылкой и т.д.

---

Хочешь я добавлю:

> - ## JSON-десериализацию через `ObjectMapper`?

Вот пример, как можно сделать **десериализацию JSON-сообщений с
помощью `ObjectMapper`** в `@KafkaListener` — для удобной работы с объектами
вместо строк 🔥

---

## ✅ 1. DTO-класс (пример)

```java
public class UserEvent {
    private String userId;
    private String action;
    private long timestamp;

    // Геттеры и сеттеры
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "UserEvent{" +
                "userId='" + userId + '\'' +
                ", action='" + action + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
```

---

## ✅ 2. Kafka Listener с ObjectMapper

```java
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class JsonKafkaListener {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = "user-events", groupId = "json-group")
    public void listen(ConsumerRecord<String, String> record) {
        try {
            String json = record.value();
            UserEvent event = objectMapper.readValue(json, UserEvent.class);
            System.out.println("📦 Получен объект: " + event);
        } catch (Exception e) {
            System.err.println("❌ Ошибка при разборе JSON: " + e.getMessage());
        }
    }
}
```

---

## 🔍 Что происходит:

| Шаг                              | Что делает                            |
|----------------------------------|---------------------------------------|
| `ConsumerRecord<String, String>` | Получаем JSON в виде строки           |
| `ObjectMapper.readValue()`       | Преобразуем строку в объект           |
| `UserEvent`                      | Класс, соответствующий структуре JSON |

---

## 🧠 Советы:

- Можно внедрить `ObjectMapper` через `@Autowired`, если хочешь настраивать (
  модули, формат даты и пр.)
- Если Kafka-провайдер отправляет не `String`, а байты — используй
  `ConsumerRecord<String, byte[]>` и десериализуй вручную

---

Хочешь:

> - ## автоматическую десериализацию через `JsonDeserializer` от Spring Kafka?

Отлично! Вот как можно настроить **автоматическую десериализацию JSON-сообщений
через `JsonDeserializer` от Spring Kafka**, чтобы `@KafkaListener` сразу получал
Java-объект вместо строки 🔥

---

## 🧩 1. DTO-класс (пример)

```java
public class UserEvent {
    private String userId;
    private String action;
    private long timestamp;

    // Геттеры, сеттеры и toString()
}
```

---

## ⚙️ 2. Конфигурация ConsumerFactory с `JsonDeserializer`

```java
import com.example.dto.UserEvent;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConsumerConfig {

    @Bean
    public ConsumerFactory<String, UserEvent> userEventConsumerFactory() {
        JsonDeserializer<UserEvent> deserializer = new JsonDeserializer<>(UserEvent.class);
        deserializer.addTrustedPackages("*"); // доверяем всем пакетам

        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "json-auto-group");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        return new DefaultKafkaConsumerFactory<>(
                props,
                new StringDeserializer(),
                deserializer
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, UserEvent> userEventKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, UserEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(userEventConsumerFactory());
        return factory;
    }
}
```

---

## ✅ 3. Kafka Listener — теперь без ObjectMapper

```java
import com.example.dto.UserEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class UserEventListener {

    @KafkaListener(
            topics = "user-events",
            groupId = "json-auto-group",
            containerFactory = "userEventKafkaListenerContainerFactory"
    )
    public void handle(UserEvent event) {
        System.out.println("✅ Авто-десериализованный объект: " + event);
    }
}
```

---

## 🧠 Примечания:

| Параметр                              | Значение                                |
|---------------------------------------|-----------------------------------------|
| `JsonDeserializer<>(UserEvent.class)` | сообщает Kafka, какой тип ожидать       |
| `addTrustedPackages("*")`             | можно ограничить безопасными пакетами   |
| `containerFactory = ...`              | подключаем нужный бин, если разные типы |

---

## ✨ Плюсы:

✅ Чистый код  
✅ Без ручной десериализации  
✅ Готов к вложенным JSON, массивам и т.д.

---

Хочешь — покажу как сделать **общий Listener для нескольких типов сообщений**,
или **обрабатывать JSON с вложенными объектами и списками**.


> - ## как сделать общий Listener для нескольких типов сообщений

Отличный вопрос! 🙌 Если в одном топике могут приходить **разные типы сообщений
** (разные классы), и ты хочешь **обрабатывать их в одном `@KafkaListener`**,
есть несколько подходов.

Вот самые удобные:

---

## 🧠 Подход 1: Обработка вручную с `ObjectMapper` по `type` в JSON

### ✅ Пример JSON-сообщений:

```json
{
  "type": "UserEvent",
  "userId": "123",
  "action": "login"
}
{
  "type": "OrderEvent",
  "orderId": "A001",
  "status": "paid"
}
```

### ✅ Классы:

```java
public class UserEvent {
    public String userId;
    public String action;
    // геттеры/сеттеры
}

public class OrderEvent {
    public String orderId;
    public String status;
    // геттеры/сеттеры
}
```

---

### ✅ KafkaListener с распознаванием типа:

```java
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class GenericKafkaListener {

    private final ObjectMapper mapper = new ObjectMapper();

    @KafkaListener(topics = "mixed-events", groupId = "multi-type-group")
    public void listen(String json) {
        try {
            JsonNode node = mapper.readTree(json);
            String type = node.get("type").asText();

            switch (type) {
                case "UserEvent" -> {
                    UserEvent userEvent = mapper.treeToValue(node, UserEvent.class);
                    System.out.println("👤 UserEvent: " + userEvent);
                }
                case "OrderEvent" -> {
                    OrderEvent orderEvent = mapper.treeToValue(node, OrderEvent.class);
                    System.out.println("🛒 OrderEvent: " + orderEvent);
                }
                default -> System.out.println("⚠️ Неизвестный тип: " + type);
            }

        } catch (Exception e) {
            System.err.println("❌ Ошибка обработки: " + e.getMessage());
        }
    }
}
```

---

## 🧠 Подход 2: Авто-определение класса через

`@JsonTypeInfo` (полиформная десериализация)

### ✅ Шаг 1: Общий интерфейс + аннотации

```java
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = UserEvent.class, name = "UserEvent"),
        @JsonSubTypes.Type(value = OrderEvent.class, name = "OrderEvent")
})
public interface BaseEvent {
}
```

### ✅ Подклассы:

```java
public class UserEvent implements BaseEvent {
    public String userId;
    public String action;
}

public class OrderEvent implements BaseEvent {
    public String orderId;
    public String status;
}
```

---

### ✅ KafkaListener с ObjectMapper (один класс BaseEvent):

```java

@KafkaListener(topics = "mixed-events", groupId = "typed-event-group")
public void handle(String json) {
    try {
        BaseEvent event = mapper.readValue(json, BaseEvent.class);

        if (event instanceof UserEvent user) {
            System.out.println("👤 UserEvent: " + user);
        } else if (event instanceof OrderEvent order) {
            System.out.println("🛒 OrderEvent: " + order);
        }

    } catch (Exception e) {
        System.err.println("❌ Ошибка парсинга: " + e.getMessage());
    }
}
```

---

## 💡 Когда какой способ использовать?

| Сценарий                                   | Способ                                                   |
|--------------------------------------------|----------------------------------------------------------|
| JSON уже содержит `type`                   | Подход 2 (`@JsonTypeInfo`) — элегантный и автоматический |
| JSON разный, `type` можно добавить вручную | Подход 1 — полная гибкость                               |
| Много типов / нужно отделить бизнес-логику | Разные `@KafkaListener`'ы с фильтрами                    |

---

Хочешь — покажу как **автоматически фильтровать по типу** или как *
*распараллелить обработку разных типов сообщений** в отдельных потоках 👇

> - ## обрабатывать JSON с вложенными объектами и списками*

Отличный вопрос! 💥 Обработка JSON с **вложенными объектами и списками** в Spring
Kafka — это супер удобно, особенно с `ObjectMapper` или автоматической
десериализацией. Ниже покажу оба подхода.

---

## ✅ Пример JSON с вложенностями

```json
{
  "orderId": "ORD123",
  "status": "shipped",
  "customer": {
    "id": "CUST001",
    "name": "Alice"
  },
  "items": [
    {
      "productId": "P001",
      "quantity": 2
    },
    {
      "productId": "P002",
      "quantity": 1
    }
  ]
}
```

---

## 🧩 1. DTO-классы для вложенного JSON

```java
public class OrderEvent {
    private String orderId;
    private String status;
    private Customer customer;
    private List<OrderItem> items;

    // геттеры, сеттеры, toString()
}

public class Customer {
    private String id;
    private String name;

    // геттеры, сеттеры, toString()
}

public class OrderItem {
    private String productId;
    private int quantity;

    // геттеры, сеттеры, toString()
}
```

---

## 💡 Подход 1: Авто-десериализация через `JsonDeserializer`

### 🔧 Конфигурация:

```java

@Bean
public ConsumerFactory<String, OrderEvent> orderConsumerFactory() {
    JsonDeserializer<OrderEvent> deserializer = new JsonDeserializer<>(OrderEvent.class);
    deserializer.addTrustedPackages("*");

    Map<String, Object> props = new HashMap<>();
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
    props.put(ConsumerConfig.GROUP_ID_CONFIG, "order-group");
    props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

    return new DefaultKafkaConsumerFactory<>(
            props,
            new StringDeserializer(),
            deserializer
    );
}

@Bean
public ConcurrentKafkaListenerContainerFactory<String, OrderEvent> orderKafkaListenerContainerFactory() {
    ConcurrentKafkaListenerContainerFactory<String, OrderEvent> factory =
            new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(orderConsumerFactory());
    return factory;
}
```

---

### 📥 Listener:

```java

@KafkaListener(
        topics = "orders",
        groupId = "order-group",
        containerFactory = "orderKafkaListenerContainerFactory"
)
public void handleOrder(OrderEvent orderEvent) {
    System.out.println("🛒 Order: " + orderEvent);
    System.out.println("👤 Customer: " + orderEvent.getCustomer());
    orderEvent.getItems().forEach(item ->
            System.out.println("📦 Item: " + item.getProductId() + ", qty=" + item.getQuantity())
    );
}
```

---

## 💡 Подход 2: Ручная десериализация с `ObjectMapper`

Если JSON не стабилен или ты хочешь больше контроля:

```java

@KafkaListener(topics = "orders", groupId = "order-debug-group")
public void handleRaw(String json) {
    try {
        OrderEvent event = objectMapper.readValue(json, OrderEvent.class);
        // ... дальше как в примере выше
    } catch (Exception e) {
        System.err.println("❌ Ошибка JSON: " + e.getMessage());
    }
}
```

---

## ✅ Поддерживаются:

- вложенные объекты (`Customer`)
- списки (`List<OrderItem>`)
- enum-поля
- вложенные JSON с любым уровнем глубины

---

Хочешь пример с массивом заказов или вложенным `Map<String, Object>`? Или
реализацию через `@JsonCreator`/`@JsonProperty` для кастомного парсинга?

Могу добавить 👇


> - ## - @KafkaListener с фильтрацией по полям JSON?

Конечно! 🔍 Аннотация `@KafkaListener` поддерживает **фильтрацию сообщений**, в
том числе по значениям **внутри JSON**, с помощью **RecordFilterStrategy**.

Это особенно удобно, если ты хочешь **обрабатывать не все сообщения, а только
те, которые соответствуют определённым условиям** — например,
`"type" == "ALERT"` или `"status" == "FAILED"`.

---

## ✅ Пример JSON

```json
{
  "type": "ALERT",
  "message": "CPU usage is high",
  "level": "critical"
}
```

---

## 🧩 DTO-класс (если нужен)

```java
public class AlertEvent {
    private String type;
    private String message;
    private String level;

    // геттеры/сеттеры
}
```

---

## 🧠 Фильтрация с помощью `RecordFilterStrategy`

### 📁 Конфигурация Listener с фильтрацией по `type`

```java
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.listener.RecordFilterStrategy;

@Configuration
public class KafkaFilterConfig {

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> filteredKafkaListenerContainerFactory(
            ConsumerFactory<String, String> consumerFactory, ObjectMapper objectMapper) {

        ConcurrentKafkaListenerContainerFactory<String, String> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);

        factory.setRecordFilterStrategy(new RecordFilterStrategy<>() {
            @Override
            public boolean filter(ConsumerRecord<String, String> record) {
                try {
                    String value = record.value();
                    var jsonNode = objectMapper.readTree(value);
                    String type = jsonNode.get("type").asText();
                    return !type.equals("ALERT"); // ❌ фильтруем всё, что не ALERT
                } catch (Exception e) {
                    return true; // фильтруем если не удалось распарсить
                }
            }
        });

        return factory;
    }
}
```

---

## 🔔 @KafkaListener с этой фильтрацией

```java

@Service
public class AlertListener {

    @KafkaListener(
            topics = "events",
            groupId = "filter-group",
            containerFactory = "filteredKafkaListenerContainerFactory"
    )
    public void listen(String message) {
        System.out.println("⚠️ Получен ALERT: " + message);
    }
}
```

---

## 🔥 Что делает фильтрация:

| Поведение      | Значение                                                   |
|----------------|------------------------------------------------------------|
| `return true`  | Сообщение будет **отфильтровано** (не попадёт в обработку) |
| `return false` | Сообщение будет **обработано**                             |

---

## 🧠 Можно фильтровать по:

- значениям полей (`type`, `status`, `level`, и т.д.)
- вложенным JSON-полям (`jsonNode.get("user").get("role").asText()`)
- длине/содержимому строки
- регулярным выражениям, null-полям и пр.

---


> ## - Выносить фильтры в отдельный `@Component`

Отличный выбор! 😎 Вынести фильтрацию в отдельный `@Component` — это **чисто,
переиспользуемо и удобно для тестов**. Ниже покажу, как это реализовать.

---

## ✅ 1. Создай свой фильтр-класс с `RecordFilterStrategy`

```java
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.listener.RecordFilterStrategy;
import org.springframework.stereotype.Component;

@Component
public class AlertTypeFilter implements RecordFilterStrategy<String, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public boolean filter(ConsumerRecord<String, String> record) {
        try {
            JsonNode json = objectMapper.readTree(record.value());
            String type = json.path("type").asText();
            // true = отфильтровать, false = пропустить
            return !type.equalsIgnoreCase("ALERT");
        } catch (Exception e) {
            System.err.println("❌ Ошибка парсинга: " + e.getMessage());
            return true; // отфильтровываем сломанные JSON
        }
    }
}
```

---

## ⚙️ 2. Настрой `ConcurrentKafkaListenerContainerFactory` с этим фильтром

```java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;

@Configuration
public class KafkaFilterConfig {

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> filteredKafkaListenerContainerFactory(
            ConsumerFactory<String, String> consumerFactory,
            AlertTypeFilter alertTypeFilter // 🔥 внедряем наш компонент
    ) {
        var factory = new ConcurrentKafkaListenerContainerFactory<String, String>();
        factory.setConsumerFactory(consumerFactory);
        factory.setRecordFilterStrategy(alertTypeFilter); // ✅ используем внешний фильтр
        return factory;
    }
}
```

---

## 📥 3. Слушатель с фильтрацией

```java
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class AlertKafkaListener {

    @KafkaListener(
            topics = "events",
            groupId = "alert-filter-group",
            containerFactory = "filteredKafkaListenerContainerFactory"
    )
    public void handleAlert(String message) {
        System.out.println("🚨 Принят ALARM: " + message);
    }
}
```

---

## 💡 Плюсы подхода:

| Преимущество                  | Описание                                                            |
|-------------------------------|---------------------------------------------------------------------|
| 🔁 Переиспользуемость         | Один и тот же фильтр можно подключать к нескольким `@KafkaListener` |
| 🧪 Удобно тестировать         | Можно писать unit-тесты отдельно от Kafka                           |
| ♻️ Разделение ответственности | KafkaListener не перегружен логикой фильтрации                      |
| 🛠️ Конфигурация гибкая       | Легко подставить другие фильтры через `@Qualifier`                  |

---

Если хочешь:

- несколько разных фильтров для разных тем
- фильтрацию по вложенным полям (`user.role == "admin"`)
- фильтрацию в Spring Cloud Stream

— с радостью помогу 🙌

> ## 🔥 Отличная идея! Если у тебя несколько Kafka-топиков, и ты хочешь **разные
фильтры для каждого**, Spring Kafka отлично это поддерживает. Ключ — это *
*разные контейнеры (`KafkaListenerContainerFactory`) с разными фильтрами
**, и ты сам выбираешь, какой где применить.

---

## 💡 Общая схема:

1. Создаём отдельный `RecordFilterStrategy`-класс на каждый случай
2. Для каждого фильтра — свой `ConcurrentKafkaListenerContainerFactory`
3. Используем нужную `containerFactory` в `@KafkaListener`

---

## 📦 1. Два разных фильтра

```java

@Component
public class AlertOnlyFilter implements RecordFilterStrategy<String, String> {
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public boolean filter(ConsumerRecord<String, String> record) {
        try {
            String type = mapper.readTree(record.value()).path("type").asText();
            return !type.equalsIgnoreCase("ALERT"); // пропускаем только ALERT
        } catch (Exception e) {
            return true;
        }
    }
}

@Component
public class OnlyHighPriorityFilter implements RecordFilterStrategy<String, String> {
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public boolean filter(ConsumerRecord<String, String> record) {
        try {
            String priority = mapper.readTree(record.value()).path("priority").asText();
            return !priority.equalsIgnoreCase("HIGH"); // только HIGH
        } catch (Exception e) {
            return true;
        }
    }
}
```

---

## ⚙️ 2. Конфигурация контейнеров с фильтрами

```java

@Configuration
public class KafkaFilterConfig {

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> alertContainerFactory(
            ConsumerFactory<String, String> consumerFactory,
            AlertOnlyFilter alertOnlyFilter
    ) {
        var factory = new ConcurrentKafkaListenerContainerFactory<String, String>();
        factory.setConsumerFactory(consumerFactory);
        factory.setRecordFilterStrategy(alertOnlyFilter);
        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> highPriorityContainerFactory(
            ConsumerFactory<String, String> consumerFactory,
            OnlyHighPriorityFilter onlyHighPriorityFilter
    ) {
        var factory = new ConcurrentKafkaListenerContainerFactory<String, String>();
        factory.setConsumerFactory(consumerFactory);
        factory.setRecordFilterStrategy(onlyHighPriorityFilter);
        return factory;
    }
}
```

---

## 📥 3. Два KafkaListener-а с разными фильтрами

```java

@Service
public class MultiFilteredListeners {

    @KafkaListener(
            topics = "alerts",
            groupId = "alert-group",
            containerFactory = "alertContainerFactory"
    )
    public void handleAlerts(String message) {
        System.out.println("⚠️ ALERT-получено: " + message);
    }

    @KafkaListener(
            topics = "notifications",
            groupId = "priority-group",
            containerFactory = "highPriorityContainerFactory"
    )
    public void handleHighPriority(String message) {
        System.out.println("🔥 HIGH PRIORITY: " + message);
    }
}
```

---

## ✨ Результат:

| Топик           | Фильтр                    | Listener                  |
|-----------------|---------------------------|---------------------------|
| `alerts`        | только `type == ALERT`    | `handleAlerts(...)`       |
| `notifications` | только `priority == HIGH` | `handleHighPriority(...)` |

---

Хочешь сделать фильтры более универсальными? Например,
`@Component("filterByFieldValue")` и передавать поля и значения как параметры?

Могу показать 🔧

------------------------------

Отлично! Давай сделаем **универсальный фильтр**, который можно конфигурировать:
передаёшь имя поля и нужное значение — и он отфильтровывает сообщения, где это
поле не совпадает.

---

## 🛠️ 1. Универсальный фильтр-компонент

```java
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.listener.RecordFilterStrategy;

public class FieldValueFilter implements RecordFilterStrategy<String, String> {

    private final ObjectMapper mapper = new ObjectMapper();
    private final String fieldName;
    private final String expectedValue;

    public FieldValueFilter(String fieldName, String expectedValue) {
        this.fieldName = fieldName;
        this.expectedValue = expectedValue;
    }

    @Override
    public boolean filter(ConsumerRecord<String, String> record) {
        try {
            JsonNode json = mapper.readTree(record.value());
            String actualValue = json.path(fieldName).asText();
            return !expectedValue.equalsIgnoreCase(actualValue);
        } catch (Exception e) {
            return true; // отфильтровываем невалидный JSON
        }
    }
}
```

---

## 🔧 2. Конфигурация с параметрами через `@Bean`

```java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;

@Configuration
public class KafkaDynamicFilterConfig {

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> typeAlertContainerFactory(
            ConsumerFactory<String, String> consumerFactory
    ) {
        var factory = new ConcurrentKafkaListenerContainerFactory<String, String>();
        factory.setConsumerFactory(consumerFactory);

        // Фильтр: type == ALERT
        factory.setRecordFilterStrategy(new FieldValueFilter("type", "ALERT"));

        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> levelCriticalContainerFactory(
            ConsumerFactory<String, String> consumerFactory
    ) {
        var factory = new ConcurrentKafkaListenerContainerFactory<String, String>();
        factory.setConsumerFactory(consumerFactory);

        // Фильтр: level == critical
        factory.setRecordFilterStrategy(new FieldValueFilter("level", "critical"));

        return factory;
    }
}
```

---

## ✅ 3. Использование в `@KafkaListener`

```java

@Service
public class UniversalFilterKafkaListener {

    @KafkaListener(
            topics = "alerts",
            groupId = "filter-type-group",
            containerFactory = "typeAlertContainerFactory"
    )
    public void listenAlerts(String message) {
        System.out.println("📣 Принят тип ALERT: " + message);
    }

    @KafkaListener(
            topics = "alerts",
            groupId = "filter-level-group",
            containerFactory = "levelCriticalContainerFactory"
    )
    public void listenCritical(String message) {
        System.out.println("🚨 Критический уровень: " + message);
    }
}
```

---

## ⚡ Хотите ещё мощнее?

Можно сделать бины фильтров **динамическими**:

```java

@Bean
@Scope("prototype")
public FieldValueFilter fieldValueFilter(String field, String value) {
    return new FieldValueFilter(field, value);
}
```

А если подключаешь Spring Boot 3+, можно даже генерировать `containerFactory` на
лету через `KafkaListenerEndpointRegistrar`.

---

Если хочешь, могу показать:

- как проверять вложенные поля (например, `user.role == "admin"`)
- как комбинировать несколько условий (AND/OR)
- как сделать фильтр, читающий конфиги из `application.yml`

Напомни — и соберу 😉

-----------------------------

> - ## как сделать фильтр, читающий конфиги из application.yml

Отличный запрос! 💡 Давай сделаем **универсальный Kafka-фильтр**, параметры
которого (например, `fieldName` и `expectedValue`) будут **подгружаться
из `application.yml`** через обычный Spring `@ConfigurationProperties`.

---

### 📁 Пример `application.yml`

```yaml
kafka:
  filters:
    type-alert:
      field: type
      value: ALERT
    priority-high:
      field: priority
      value: HIGH
```

---

## 🧱 1. DTO-класс для конфигурации

```java
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "kafka.filters")
public class KafkaFilterProperties {

    private Map<String, FieldConfig> filters;

    public static class FieldConfig {
        private String field;
        private String value;

        // геттеры/сеттеры
        public String getField() {
            return field;
        }

        public void setField(String field) {
            this.field = field;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    public Map<String, FieldConfig> getFilters() {
        return filters;
    }

    public void setFilters(Map<String, FieldConfig> filters) {
        this.filters = filters;
    }
}
```

---

## ⚙️ 2. Конфигурация Kafka Listener'ов с фильтрами из YAML

```java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;

@Configuration
public class KafkaFilterConfigFromYaml {

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> typeAlertContainerFactory(
            ConsumerFactory<String, String> consumerFactory,
            KafkaFilterProperties properties
    ) {
        var filterConfig = properties.getFilters().get("type-alert");
        var filter = new FieldValueFilter(filterConfig.getField(), filterConfig.getValue());

        var factory = new ConcurrentKafkaListenerContainerFactory<String, String>();
        factory.setConsumerFactory(consumerFactory);
        factory.setRecordFilterStrategy(filter);

        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> priorityHighContainerFactory(
            ConsumerFactory<String, String> consumerFactory,
            KafkaFilterProperties properties
    ) {
        var filterConfig = properties.getFilters().get("priority-high");
        var filter = new FieldValueFilter(filterConfig.getField(), filterConfig.getValue());

        var factory = new ConcurrentKafkaListenerContainerFactory<String, String>();
        factory.setConsumerFactory(consumerFactory);
        factory.setRecordFilterStrategy(filter);

        return factory;
    }
}
```

---

## 🔍 3. Универсальный фильтр (как раньше)

```java
public class FieldValueFilter implements RecordFilterStrategy<String, String> {

    private final ObjectMapper mapper = new ObjectMapper();
    private final String fieldName;
    private final String expectedValue;

    public FieldValueFilter(String fieldName, String expectedValue) {
        this.fieldName = fieldName;
        this.expectedValue = expectedValue;
    }

    @Override
    public boolean filter(ConsumerRecord<String, String> record) {
        try {
            JsonNode json = mapper.readTree(record.value());
            String actualValue = json.path(fieldName).asText();
            return !expectedValue.equalsIgnoreCase(actualValue);
        } catch (Exception e) {
            return true;
        }
    }
}
```

---

## ✅ Преимущества подхода:

| Особенность              | Описание                                        |
|--------------------------|-------------------------------------------------|
| 🛠️ Конфигурируемость    | Меняешь фильтр — не нужно перекомпилировать код |
| ♻️ Переиспользуемость    | Один фильтр — много топиков                     |
| 🧪 Удобство тестирования | Можно мокать `KafkaFilterProperties`            |

---

Хочешь пример, как **один бин** конфигурирует несколько топиков по
`Map<String, FieldConfig>` динамически? Или как подгружать конфиг в runtime?



> - ## - Логирование в файл или БД?

Отличный вопрос! 🎯 В Spring Boot (включая Kafka-приложения) можно логировать **в
файл, в консоль, в базу данных, в сторонние системы (например, ELK, Grafana Loki
и т.д.)**. Давай разберём основные способы.

---

## 📁 1. Логирование в файл (через Logback)

### 🔧 `application.yml`

```yaml
logging:
  level:
    root: INFO
    com.yourapp: DEBUG
  file:
    name: logs/app.log
```

> Spring Boot автоматически создаст `logs/app.log` и начнёт туда писать.

---

### 💡 Пример логирования в коде:

```java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class KafkaLoggerService {

    private static final Logger log = LoggerFactory.getLogger(KafkaLoggerService.class);

    public void handleMessage(String message) {
        log.info("📩 Получено сообщение: {}", message);
    }
}
```

---

## 🧾 2. Логирование в базу данных

Логирование в БД обычно делается через **Logback + DB appender**.

### 💡 Используем Logback + JDBC

#### 📦 Зависимость:

```xml

<dependency>
    <groupId>ch.qos.logback</groupId>
    <artifactId>logback-classic</artifactId>
</dependency>
```

#### 🧾 Таблица в БД (например, PostgreSQL)

```sql
CREATE TABLE logs
(
    id       SERIAL PRIMARY KEY,
    log_date TIMESTAMP,
    level    VARCHAR(10),
    logger   VARCHAR(255),
    message  TEXT,
    thread   VARCHAR(50)
);
```

#### 📄 `logback-spring.xml`

```xml

<configuration>
    <appender name="DB" class="ch.qos.logback.classic.db.DBAppender">
        <connectionSource
                class="ch.qos.logback.core.db.DriverManagerConnectionSource">
            <driverClass>org.postgresql.Driver</driverClass>
            <url>jdbc:postgresql://localhost:5432/logs_db</url>
            <user>postgres</user>
            <password>password</password>
        </connectionSource>
    </appender>

    <root level="INFO">
        <appender-ref ref="DB"/>
    </root>
</configuration>
```

> ⚠️ Понадобится JDBC-драйвер под твою БД (например, PostgreSQL, MySQL и т.д.)

---

## 📦 3. Логирование в Kafka (или внешнюю систему мониторинга)

Можно логировать события прямо в Kafka (например, из audit-сервисов), или
отправлять в сторонние системы вроде:

- **ELK Stack (Elasticsearch + Logstash + Kibana)**
- **Grafana Loki + Promtail**
- **Fluentd / FluentBit**

Это делается через Logstash Appender, Kafka Appender или через Spring Boot
Actuator + Micrometer.

---

## ✅ Резюме:

| Цель                    | Рекомендуемый способ                         |
|-------------------------|----------------------------------------------|
| Файл                    | `logging.file.name` или Logback FileAppender |
| База данных             | Logback DBAppender + custom таблица          |
| Kafka / внешние системы | KafkaAppender, Logstash, Fluentd             |
| Гибкая фильтрация логов | Spring Profiles + SLF4J/Logback уровни       |

---

Если хочешь:

- 🔍 Пример с Logback + Kafka
- 🛡️ Отдельный логгер для ошибок
- 🗃️ Разделение логов по классам
- 🔀 Асинхронное логирование

— просто скажи, и я покажу 😊


----------------

Пример с Logback + Kafka

Отличный выбор! 🚀 Логирование с Logback в Kafka — это мощный способ отправлять
логи в централизованную систему (например, Logstash, ELK, Kafka Consumer,
Grafana Loki через Promtail и т.д.).

---

## 🎯 Цель

Отправлять логи из Java-приложения в **Kafka-топик** с помощью Logback.

---

## 📦 1. Зависимости (Maven)

Logback не поддерживает Kafka из коробки, поэтому подключим Kafka Appender:

```xml

<dependency>
    <groupId>com.github.danielwegener</groupId>
    <artifactId>logback-kafka-appender</artifactId>
    <version>0.1.0</version>
</dependency>
```

> Также нужна стандартная Kafka-библиотека, если не подключена:

```xml

<dependency>
    <groupId>org.apache.kafka</groupId>
    <artifactId>kafka-clients</artifactId>
</dependency>
```

---

## ⚙️ 2. Конфиг `logback-spring.xml`

Создай файл `src/main/resources/logback-spring.xml`:

```xml

<configuration>

    <appender name="KAFKA"
              class="com.github.danielwegener.logback.kafka.KafkaAppender">
        <encoder class="ch.qos.logback.classic.encoder.PatternLayoutEncoder">
            <pattern>%d{yyyy-MM-dd HH:mm:ss} %-5level [%thread] %logger{36} -
                %msg%n
            </pattern>
        </encoder>

        <topic>logs-app</topic>

        <producerConfig>
            bootstrap.servers=localhost:9092
            key.serializer=org.apache.kafka.common.serialization.StringSerializer
            value.serializer=org.apache.kafka.common.serialization.StringSerializer
        </producerConfig>
    </appender>

    <root level="INFO">
        <appender-ref ref="KAFKA"/>
    </root>

</configuration>
```

> Ты можешь добавить и обычный логгер в файл или консоль параллельно, если
> нужно.

---

## 💬 3. Пример логирования

```java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class KafkaLogExample {

    private static final Logger log = LoggerFactory.getLogger(KafkaLogExample.class);

    public void processSomething() {
        log.info("👋 Привет из Kafka Appender!");
    }
}
```

---

## 🧪 4. Проверка

Можно использовать Kafka CLI, чтобы просмотреть сообщения:

```bash
kafka-console-consumer.sh \
  --bootstrap-server localhost:9092 \
  --topic logs-app \
  --from-beginning
```

Ты увидишь лог-сообщения прямо из Kafka 🎉

---

## 🧠 Примечания:

- **Формат** логов можно настроить в `pattern`
- Можно сделать **JSON-логирование** с `ch.qos.logback.contrib.json` (покажу,
  если нужно)
- Можно использовать отдельный `KafkaAppender` для ошибок (`level=ERROR`)
- Также можно использовать **LogstashEncoder** для интеграции с ELK

---

Хочешь:

- JSON-формат логов?
- Сжатие и буферизацию перед отправкой в Kafka?
- Несколько топиков для разных уровней (INFO/ERROR)?

Могу показать всё 😉

- JSON-формат логов?

--------------------

Отлично! 💡 Давай сделаем логирование в **Kafka в формате JSON** — это особенно
полезно, если ты потом будешь парсить логи в **Logstash, Elasticsearch, Splunk,
Grafana Loki и др.**

---

## 📦 1. Зависимости для JSON-логов через Logback

Добавим [logstash-logback-encoder](https://github.com/logstash/logstash-logback-encoder):

```xml

<dependency>
    <groupId>net.logstash.logback</groupId>
    <artifactId>logstash-logback-encoder</artifactId>
    <version>7.4</version>
</dependency>
```

> Эта библиотека позволяет удобно сериализовать логи в JSON.

---

## ⚙️ 2. `logback-spring.xml` с JSON + Kafka

```xml

<configuration>

    <appender name="KAFKA_JSON"
              class="com.github.danielwegener.logback.kafka.KafkaAppender">
        <encoder class="net.logstash.logback.encoder.LogstashEncoder"/>

        <topic>logs-json</topic>

        <producerConfig>
            bootstrap.servers=localhost:9092
            key.serializer=org.apache.kafka.common.serialization.StringSerializer
            value.serializer=org.apache.kafka.common.serialization.StringSerializer
        </producerConfig>
    </appender>

    <root level="INFO">
        <appender-ref ref="KAFKA_JSON"/>
    </root>

</configuration>
```

---

## 🧪 Пример JSON-сообщения в Kafka

Сообщение будет выглядеть примерно так:

```json
{
  "@timestamp": "2025-04-25T15:30:42.123+00:00",
  "level": "INFO",
  "thread": "main",
  "logger_name": "com.example.MyService",
  "message": "Привет из JSON логов!",
  "context": "default",
  "host": "localhost"
}
```

---

## ✅ Преимущества JSON-логов

| Возможность                 | Преимущество                               |
|-----------------------------|--------------------------------------------|
| 🎯 Структурированность      | Удобно парсить, фильтровать, анализировать |
| 🔍 Поиск по полям           | `logger_name`, `level`, `message`, `tags`  |
| 📊 Интеграция с ELK/Grafana | Легко строить графики, дашборды            |
| 📦 Kafka совместимость      | Kafka консюмеры читают как обычный JSON    |

---

## ➕ Кастомизация JSON-логов

Хочешь добавить свои поля (например, `userId`, `traceId`, `env` и т.д.)?

Просто добавь:

```xml

<encoder class="net.logstash.logback.encoder.LogstashEncoder">
    <customFields>{"env":"prod", "app":"kafka-logger"}</customFields>
</encoder>
```

---

## 🔁 Комбинированный лог

Ты можешь оставить **один лог в файл (обычный)**, другой — **в Kafka в JSON**.
Просто добавь 2 `appender`'а и подключи их к `root` или нужному логгеру.

---

Хочешь:

- 👤 Добавить данные пользователя или запроса (`MDC`) в логи?
- 📂 Разделение логов по уровням?
- 📄 JSON-логи одновременно в Kafka и Elasticsearch?

Готов показать 😎
------------------

- 📂 Разделение логов по уровням?

Супер! 🔥 Разделение логов по уровням (например, `INFO`, `ERROR`) — это *
*практика продвинутого логирования**, которая помогает:

- фильтровать потоки логов по важности;
- хранить `ERROR`-логи отдельно (например, для alert'ов, мониторинга);
- делать разные маршруты: `INFO` → файл, `ERROR` → Kafka/Slack/Sentry.

---

## 🎯 Цель

Настроим логирование с разделением:

- `INFO` → Kafka топик `logs-info`
- `ERROR` → Kafka топик `logs-error`

И всё это — в **формате JSON**.

---

## 📦 Зависимости

(Уже должны быть подключены, но на всякий случай)

```xml
<!-- JSON-логирование -->
<dependency>
    <groupId>net.logstash.logback</groupId>
    <artifactId>logstash-logback-encoder</artifactId>
    <version>7.4</version>
</dependency>

        <!-- Kafka-аппендер -->
<dependency>
<groupId>com.github.danielwegener</groupId>
<artifactId>logback-kafka-appender</artifactId>
<version>0.1.0</version>
</dependency>
```

---

## ⚙️ `logback-spring.xml` с разделением уровней

```xml

<configuration>

    <!-- INFO / DEBUG → Kafka topic: logs-info -->
    <appender name="KAFKA_INFO"
              class="com.github.danielwegener.logback.kafka.KafkaAppender">
        <encoder class="net.logstash.logback.encoder.LogstashEncoder"/>
        <topic>logs-info</topic>
        <producerConfig>
            bootstrap.servers=localhost:9092
            key.serializer=org.apache.kafka.common.serialization.StringSerializer
            value.serializer=org.apache.kafka.common.serialization.StringSerializer
        </producerConfig>
        <filter class="ch.qos.logback.classic.filter.LevelFilter">
            <level>INFO</level>
            <onMatch>ACCEPT</onMatch>
            <onMismatch>DENY</onMismatch>
        </filter>
    </appender>

    <!-- ERROR → Kafka topic: logs-error -->
    <appender name="KAFKA_ERROR"
              class="com.github.danielwegener.logback.kafka.KafkaAppender">
        <encoder class="net.logstash.logback.encoder.LogstashEncoder"/>
        <topic>logs-error</topic>
        <producerConfig>
            bootstrap.servers=localhost:9092
            key.serializer=org.apache.kafka.common.serialization.StringSerializer
            value.serializer=org.apache.kafka.common.serialization.StringSerializer
        </producerConfig>
        <filter class="ch.qos.logback.classic.filter.ThresholdFilter">
            <level>ERROR</level>
        </filter>
    </appender>

    <!-- Рутовый логгер -->
    <root level="INFO">
        <appender-ref ref="KAFKA_INFO"/>
        <appender-ref ref="KAFKA_ERROR"/>
    </root>

</configuration>
```

---

## 🧪 Пример кода для проверки

```java
private static final Logger log = LoggerFactory.getLogger(MyService.class);

public void testLogs() {
    log.info("✅ Это информационное сообщение");
    log.error("❌ А это ошибка!");
}
```

---

## 🔍 Проверка в Kafka:

```bash
kafka-console-consumer.sh \
  --bootstrap-server localhost:9092 \
  --topic logs-info \
  --from-beginning
```

```bash
kafka-console-consumer.sh \
  --bootstrap-server localhost:9092 \
  --topic logs-error \
  --from-beginning
```

---

## ✅ Что ещё можно сделать?

| Идея                          | Как реализовать                                |
|-------------------------------|------------------------------------------------|
| 📁 INFO в файл, ERROR в Kafka | Добавить `FileAppender` с фильтром             |
| 🧵 Разделение по потокам      | Использовать MDC + фильтры                     |
| 🧪 Логи в Sentry или Slack    | Обработка `ERROR`-топика внешними consumer'ами |
| 📊 Аналитика по логам         | Подключить Logstash/Fluentd + Elasticsearch    |

---

Хочешь:

- 🔀 Несколько Kafka-топиков с разными форматами?
- 🎯 Логировать только ошибки конкретного сервиса?
- 🧩 Интеграцию с AOP для логирования входящих запросов?

С радостью покажу 😊

===============================================================================


## Broker (брокер)


## Kafka Cluster (кластер Kafka)

## Zookeeper (в старых версиях)

## Consumer Group (группа потребителей)

## Bootstrap Server