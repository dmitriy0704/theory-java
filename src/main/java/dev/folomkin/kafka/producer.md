
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

