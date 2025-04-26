
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
