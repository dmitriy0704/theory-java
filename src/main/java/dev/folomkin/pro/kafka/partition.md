## Партиции

Партиции в Apache Kafka — это ключевой механизм, который обеспечивает *
*масштабируемость**, **производительность** и **параллелизм обработки данных**.
Вот подробнее, зачем они нужны:

### 1. **Масштабируемость**

Kafka-топик разбивается на несколько **партиций** (разделов), которые можно
распределить по разным брокерам. Это позволяет масштабировать нагрузку по
нескольким серверам.

**Пример**:  
У тебя есть топик с 6 партициями, и кластер из 3 брокеров — тогда каждая машина
может обрабатывать часть данных, а не один брокер всё сразу.

### 2. **Параллельная обработка**

Каждую партицию может читать отдельный **consumer** (или consumer-поток), что
позволяет обрабатывать данные параллельно.

**Плюс:** повышается производительность и скорость обработки данных.  
**Ограничение:** количество потребителей в consumer group не может быть больше,
чем количество партиций.

### 3. **Упорядоченность данных**

Kafka **гарантирует порядок сообщений только внутри одной партиции**.

**Если важен порядок:** нужно продумать, по какому ключу делать
партиционирование (например, user_id), чтобы все сообщения одного пользователя
шли в одну и ту же партицию.

### 4. **Физическая организация хранения**

Партиции — это по сути отдельные лог-файлы на диске, что делает их удобными для
хранения, репликации и управления.

### 5. **Репликация и отказоустойчивость**

Каждая партиция может иметь **реплики** на других брокерах (вторичные копии),
что делает систему устойчивой к сбоям.

---------

> ## - как работать с партициями из java

Отличный вопрос! 🔧 Работа с **партициями Kafka из Java** позволяет
контролировать, **в какую партицию отправляется сообщение**, а также **читать из
конкретных партиций**. Это важно, когда нужно управлять параллелизмом, порядком
или маршрутизацией сообщений.

Разберём по частям:

---

## 📨 1. Producer: отправка в конкретную партицию

### Пример: Java Kafka Producer с ручным указанием партиции

```java
import org.apache.kafka.clients.producer.*;

import java.util.Properties;

public class CustomPartitionProducer {

    public static void main(String[] args) {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringSerializer");

        KafkaProducer<String, String> producer = new KafkaProducer<>(props);

        String topic = "example-topic";
        String key = "user-123";
        String value = "Hello with partition!";
        int partition = 1; // вручную указываем партицию

        ProducerRecord<String, String> record = new ProducerRecord<>(topic, partition, key, value);
        producer.send(record, (metadata, exception) -> {
            if (exception == null) {
                System.out.printf("Отправлено в partition %d с offset %d%n",
                        metadata.partition(), metadata.offset());
            } else {
                exception.printStackTrace();
            }
        });

        producer.close();
    }
}
```

---

## 🔄 2. Consumer: чтение из конкретных партиций

### Используем `assign()` вместо `subscribe()`

```java
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;

import java.time.Duration;
import java.util.*;

public class SpecificPartitionConsumer {

    public static void main(String[] args) {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "manual-partition-consumer");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);

        String topic = "example-topic";
        int partition = 1;

        TopicPartition topicPartition = new TopicPartition(topic, partition);
        consumer.assign(List.of(topicPartition));

        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(1));
            for (ConsumerRecord<String, String> record : records) {
                System.out.printf("📥 Partition: %d | Offset: %d | Value: %s%n",
                        record.partition(), record.offset(), record.value());
            }
        }
    }
}
```

---

## 📚 Дополнительно: узнать список партиций у топика

```java
List<PartitionInfo> partitions = producer.partitionsFor("example-topic");
for(
PartitionInfo p :partitions){
        System.out.

println("🧩 Partition: "+p.partition());
        }
```

---

## 🧠 Зачем это может быть нужно?

| Сценарий                              | Применение партиций                           |
|---------------------------------------|-----------------------------------------------|
| 🧾 Гарантированный порядок сообщений  | Все сообщения с одним ключом → 1 партиция     |
| ⚖️ Балансировка нагрузки              | Ручное распределение по партициям             |
| 📦 Партиционирование по бизнес-логике | Например, по userId, location, type и т.д.    |
| 📤 Replay / offload                   | Читать вручную с offset'а конкретной партиции |

---

Хочешь:

- Пример с **Spring Kafka и `@KafkaListener` на конкретную партицию**?
- Реализацию **custom partitioner** (по бизнес-логике)?
- Пример с **seekToBeginning() / seekToOffset()**?

Я могу показать всё 🙌

---- 

- Пример с Spring Kafka и @KafkaListener на конкретную партицию?

Конечно! 🙌 Вот как можно использовать `@KafkaListener` в **Spring Kafka** для
прослушивания **конкретных партиций** топика — удобно, если хочешь
контролировать распределение нагрузки или обрабатывать сообщения определённого
типа отдельно.

---

## ✅ Пример: `@KafkaListener` с привязкой к партициям

```java
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class PartitionedListener {

    @KafkaListener(
            topicPartitions = {
                    @org.springframework.kafka.annotation.TopicPartition(
                            topic = "example-topic",
                            partitions = {"0", "1"}  // слушаем только партиции 0 и 1
                    )
            },
            groupId = "partitioned-group"
    )
    public void listenToSpecificPartitions(ConsumerRecord<String, String> record) {
        System.out.printf("🔊 Message from partition %d: %s%n",
                record.partition(), record.value());
    }
}
```

---

## ⚙️ Как это работает

| Аннотация                 | Описание                                                  |
|---------------------------|-----------------------------------------------------------|
| `@KafkaListener`          | Подписка на Kafka-сообщения                               |
| `topicPartitions`         | Привязка к **определённым партициям**                     |
| `partitions = {"0", "1"}` | Слушает только указанные партиции                         |
| `ConsumerRecord<K, V>`    | Позволяет получить доступ к partition, offset, key и т.д. |

---

## 🧠 Полезно знать

- Работает **только с `concurrency = 1`** (или ты сам обязан обеспечить
  корректную работу с несколькими потоками).
- Для **динамического распределения** лучше использовать обычный
  `@KafkaListener(topic = "...")` — Spring сам распределит партиции.
- Можно использовать `@KafkaListener` с `concurrency` и указанием
  `topicPartitions` одновременно (но нужно быть осторожным с разделением
  потоков).

---

## 🔄 Пример с `concurrency`

```java

@KafkaListener(
        topicPartitions = {
                @TopicPartition(topic = "example-topic", partitions = {"2", "3"})
        },
        groupId = "group-X",
        concurrency = "2"
)
public void listenWithConcurrency(String message) {
    System.out.println("⚙️ " + message);
}
```

---

Хочешь:

- Пример с **динамическим выбором партиции на основе параметров**?
- Пример с **обработкой offset вручную**?
- Или, наоборот, чтобы **всё топик + все партиции** обрабатывались равномерно?

--------------------


Отлично! 🎯 Динамический выбор партиции — это когда **Kafka Producer сам
определяет, в какую партицию отправлять сообщение**, на основе значений (
например, `userId`, `region`, `orderId`, и т.д.).

---

## ✅ Цель:

Мы хотим реализовать **свой собственный логический `Partitioner`**, который
будет:

- направлять сообщения в разные партиции на основе ключа;
- учитывать, например, `userId % partitionCount`.

---

## 📦 1. Кастомный Partitioner

Создадим класс `UserIdPartitioner`:

```java
import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;

import java.util.Map;

public class UserIdPartitioner implements Partitioner {

    @Override
    public int partition(String topic, Object key, Object value, byte[] keyBytes, byte[] valueBytes, Cluster cluster) {
        int partitionCount = cluster.partitionCountForTopic(topic);

        if (key == null) {
            return 0; // по умолчанию
        }

        // Пример: если key = "user-123", берём hash-table.md, делим на количество партиций
        return Math.abs(key.hashCode()) % partitionCount;
    }

    @Override
    public void close() {
        // нет необходимости что-то закрывать
    }

    @Override
    public void configure(Map<String, ?> configs) {
        // не используется
    }
}
```

---

## ⚙️ 2. ProducerConfig: указание кастомного партиционера

```java
Properties props = new Properties();
props.

put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
props.

put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
props.

put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");

// ВАЖНО: подключаем наш кастомный партиционер
props.

put(ProducerConfig.PARTITIONER_CLASS_CONFIG, "com.example.kafka.partition.UserIdPartitioner");
```

---

## 🚀 3. Пример отправки с динамическим партиционированием

```java
KafkaProducer<String, String> producer = new KafkaProducer<>(props);

String topic = "dynamic-topic";
String userId = "user-42"; // это и будет ключом для партиционирования

ProducerRecord<String, String> record = new ProducerRecord<>(topic, userId, "Событие для " + userId);
producer.

send(record, (metadata, exception) ->{
        if(exception ==null){
        System.out.

printf("✅ Отправлено в partition %d (offset %d)%n",metadata.partition(),metadata.

offset());
        }else{
        exception.

printStackTrace();
    }
            });

            producer.

close();
```

---

## 🔍 Как это работает

- Kafka вызывает метод `partition(...)` при каждой отправке.
- Ты контролируешь логику выбора партиции.
- Kafka автоматически узнаёт, сколько партиций у топика —
  `cluster.partitionCountForTopic(topic)`.

---

## 🧠 Кейс использования

| Логика               | Партиция                                      |
|----------------------|-----------------------------------------------|
| `userId % N`         | Гарантирует порядок сообщений по пользователю |
| `region -> партиция` | Региональная маршрутизация                    |
| `priority == HIGH`   | В особую партицию                             |

---

## 🧪 Хочешь тест?

Можно написать unit-тест и проверить, как `UserIdPartitioner` выбирает партиции
для разных ключей 😉

---

Нужно:

- пример с кастомным `Partitioner` в Spring Kafka?
- `Partitioner` на основе содержимого JSON?
- автоматические метки или MDC в Kafka-пайплайн?

Я покажу, если интересно 🙌


----------------
unit-тест и проверить, как `UserIdPartitioner` выбирает партиции
для разных ключей 😉

С удовольствием! 🧪 Давай напишем unit-тест для кастомного `Partitioner`, чтобы
убедиться, что он правильно рассчитывает партиции на основе ключа.

---

## ✅ 1. Предположим, у нас есть такой `UserIdPartitioner`:

```java
public class UserIdPartitioner implements Partitioner {

    @Override
    public int partition(String topic, Object key, Object value, byte[] keyBytes,
                         byte[] valueBytes, Cluster cluster) {
        int partitionCount = cluster.partitionCountForTopic(topic);
        return (key == null) ? 0 : Math.abs(key.hashCode()) % partitionCount;
    }

    @Override
    public void configure(Map<String, ?> configs) {
    }

    @Override
    public void close() {
    }
}
```

---

## ✅ 2. Напишем JUnit-тест (с использованием Mockito)

```java
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.PartitionInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserIdPartitionerTest {

    private UserIdPartitioner partitioner;
    private Cluster cluster;

    @BeforeEach
    void setUp() {
        partitioner = new UserIdPartitioner();

        // Мокаем кластер с 3 партициями
        List<PartitionInfo> partitions = List.of(
                new PartitionInfo("test-topic", 0, null, null, null),
                new PartitionInfo("test-topic", 1, null, null, null),
                new PartitionInfo("test-topic", 2, null, null, null)
        );
        cluster = mock(Cluster.class);
        when(cluster.partitionCountForTopic("test-topic")).thenReturn(partitions.size());
    }

    @Test
    void testPartitionIsConsistentForSameKey() {
        String topic = "test-topic";
        String key = "user-123";

        int partition1 = partitioner.partition(topic, key, null, null, null, cluster);
        int partition2 = partitioner.partition(topic, key, null, null, null, cluster);

        assertEquals(partition1, partition2, "Partition for same key should be consistent");
        assertTrue(partition1 >= 0 && partition1 < 3, "Partition index must be valid");
    }

    @Test
    void testPartitionIsZeroForNullKey() {
        String topic = "test-topic";
        int partition = partitioner.partition(topic, null, "value", null, null, cluster);

        assertEquals(0, partition, "Partition should be 0 when key is null");
    }
}
```

---

## 📦 Что мы проверяем

| Тест                          | Проверка                                           |
|-------------------------------|----------------------------------------------------|
| `same key → same partition`   | Сообщения с одинаковым ключом идут в одну партицию |
| `key == null → partition = 0` | Если ключ не задан, отправляем в партицию 0        |
| `valid partition range`       | Партиция в пределах от `0` до `partitionCount - 1` |

---

## 🧰 Требуемые зависимости (если используешь Maven)

```xml

<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter</artifactId>
    <version>5.9.3</version>
    <scope>test</scope>
</dependency>
<dependency>
<groupId>org.mockito</groupId>
<artifactId>mockito-core</artifactId>
<version>5.11.0</version>
<scope>test</scope>
</dependency>
```

---

Хочешь:

- протестировать `Partitioner` на основе JSON-поля?
- пример с интеграционным тестом Kafka + TestContainers?

Можем добавить 💡


----------

пример с интеграционным тестом Kafka + TestContainers?

Конечно! 🚀 Интеграционные тесты с **Kafka + Testcontainers** — мощный способ *
*тестировать Kafka-продюсеров и консьюмеров в живой среде**, не поднимая кластер
вручную.

---

## 🔧 Что мы сделаем:

- Запустим Kafka через Testcontainers;
- Напишем тест, который:
    - создаёт топик;
    - отправляет сообщение;
    - читает его обратно через Consumer.

---

## ✅ 1. Зависимости (Maven)

```xml

<dependencies>
    <!-- Spring Kafka -->
    <dependency>
        <groupId>org.springframework.kafka</groupId>
        <artifactId>spring-kafka</artifactId>
    </dependency>

    <!-- Testcontainers Kafka -->
    <dependency>
        <groupId>org.testcontainers</groupId>
        <artifactId>kafka</artifactId>
        <version>1.19.3</version>
        <scope>test</scope>
    </dependency>

    <!-- JUnit 5 -->
    <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter</artifactId>
        <version>5.9.3</version>
        <scope>test</scope>
    </dependency>
</dependencies>
```

---

## ✅ 2. Интеграционный тест

```java
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class KafkaIntegrationTest {

    static KafkaContainer kafka;

    @BeforeAll
    static void startKafka() {
        kafka = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.5.0"));
        kafka.start();
    }

    @AfterAll
    static void stopKafka() {
        kafka.stop();
    }

    @Test
    void testProduceAndConsume() {
        String topic = "test-topic";

        // Producer config
        Properties producerProps = new Properties();
        producerProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafka.getBootstrapServers());
        producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        // Send message
        KafkaProducer<String, String> producer = new KafkaProducer<>(producerProps);
        producer.send(new ProducerRecord<>(topic, "key-1", "hello from test")).join();
        producer.close();

        // Consumer config
        Properties consumerProps = new Properties();
        consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafka.getBootstrapServers());
        consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, "test-group");
        consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(consumerProps);
        consumer.subscribe(List.of(topic));

        ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(5));
        consumer.close();

        List<ConsumerRecord<String, String>> recordList = new ArrayList<>();
        records.forEach(recordList::add);

        assertFalse(recordList.isEmpty(), "Expected at least one record");
        assertEquals("hello from test", recordList.get(0).value());
    }
}
```

---

## 🔥 Что ты получаешь:

- **Полноценный Kafka-брокер** на Docker в тесте;
- Надёжный механизм CI/CD-тестирования Kafka логики;
- Возможность использовать в Spring-тестах, например, с `@Testcontainers`.

---

## ⚡️ Бонус: Если ты хочешь использовать `@SpringBootTest`

Ты можешь поднять Kafka с Testcontainers и **прокинуть порт в `application.yml`
** через `@DynamicPropertySource`.

Если нужно — покажу этот вариант тоже!

---

Хочешь расширить:

- 🔄 Тестировать Listener через `@KafkaListener`?
- 🔎 Протестировать `JsonDeserializer`?
- 🔧 Конфигурацию с `KafkaAdmin` и auto-create topics?

Скажи — покажу 🧪✨

----------

🔧 Конфигурацию с KafkaAdmin и auto-create topics?

Отлично! 🔧 Давай настроим Spring Boot приложение так, чтобы оно **автоматически
создавалo Kafka-топики** при запуске, используя `KafkaAdmin`.

---

## ✅ Что будет:

- Используем `KafkaAdmin` для подключения к кластеру;
- Конфигурируем автоматическое создание топиков (`NewTopic`);
- Всё работает автоматически при старте приложения.

---

## 📦 1. Зависимости (`pom.xml`)

```xml

<dependency>
    <groupId>org.springframework.kafka</groupId>
    <artifactId>spring-kafka</artifactId>
</dependency>
```

---

## ⚙️ 2. `application.yml`

```yaml
spring:
  kafka:
    bootstrap-servers: localhost:9092
    client-id: my-kafka-app
    admin:
      properties:
        retries: 3
```

---

## 🧠 3. Конфигурация `KafkaAdmin` и `NewTopic`

```java
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public KafkaAdmin kafkaAdmin() {
        // Конфигурация для подключения к Kafka
        Map<String, Object> configs = new HashMap<>();
        configs.put("bootstrap.servers", "localhost:9092");
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic userEventsTopic() {
        return TopicBuilder.name("user-events")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic auditLogsTopic() {
        return TopicBuilder.name("audit-logs")
                .partitions(1)
                .replicas(1)
                .compact() // Пример с compact-сегментом (опционально)
                .build();
    }
}
```

---

## 🔍 Что делает Spring?

- При старте приложения Spring:
    1. Инициализирует `KafkaAdmin`;
    2. Читает все бины `NewTopic`;
    3. Создаёт топики, если они **ещё не существуют** в Kafka.

> 💡 Повторный запуск не пересоздаёт топики — Kafka проверит, что они уже есть.

---

## 🛑 Важно:

- Kafka **не изменит конфигурацию топика**, если он уже существует (например,
  изменить количество партиций — нельзя).
- Рекомендуется: использовать `kafkaAdmin` **только в DEV/CI** или при
  миграциях.

---

## ✅ Проверка в логе

После старта ты увидишь что-то вроде:

```
Created topic user-events with 3 partitions and 1 replicas.
```

---

## 🎁 Бонус:

Хочешь REST-контроллер для **добавления топиков во время работы** или **удаления
по имени/шаблону**? Это тоже можно реализовать!

Готов показать 👇

Отлично! 🚀 Давай сделаем **REST-контроллер для управления Kafka-топиками**:

- ✅ Создание топика по запросу;
- 🗑 Удаление топика по имени;
- 🧹 Удаление по шаблону;
- 📜 Просмотр всех текущих топиков.

---

## 📦 Зависимости (если ещё не добавлены):

```xml

<dependency>
    <groupId>org.springframework.kafka</groupId>
    <artifactId>spring-kafka</artifactId>
</dependency>
<dependency>
<groupId>org.springframework.boot</groupId>
<artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

---

## 🔧 KafkaAdminService (управление топиками)

```java
import org.apache.kafka.clients.admin.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
public class KafkaAdminService {

    private final AdminClient adminClient;

    public KafkaAdminService(@Value("${spring.kafka.bootstrap-servers}") String bootstrapServers) {
        Properties props = new Properties();
        props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        this.adminClient = AdminClient.create(props);
    }

    public void createTopic(String name, int partitions, short replicas) throws ExecutionException, InterruptedException {
        NewTopic topic = new NewTopic(name, partitions, replicas);
        adminClient.createTopics(Collections.singleton(topic)).all().get();
    }

    public void deleteTopic(String name) throws ExecutionException, InterruptedException {
        adminClient.deleteTopics(Collections.singleton(name)).all().get();
    }

    public List<String> listTopics() throws ExecutionException, InterruptedException {
        return new ArrayList<>(adminClient.listTopics().names().get());
    }

    public List<String> deleteTopicsByPrefix(String prefix) throws ExecutionException, InterruptedException {
        List<String> toDelete = listTopics().stream()
                .filter(name -> name.startsWith(prefix))
                .collect(Collectors.toList());
        adminClient.deleteTopics(toDelete).all().get();
        return toDelete;
    }
}
```

---

## 🌐 KafkaTopicController

```java
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/kafka")
public class KafkaTopicController {

    private final KafkaAdminService kafkaAdminService;

    public KafkaTopicController(KafkaAdminService kafkaAdminService) {
        this.kafkaAdminService = kafkaAdminService;
    }

    @PostMapping("/topics")
    public String createTopic(@RequestParam String name,
                              @RequestParam(defaultValue = "1") int partitions,
                              @RequestParam(defaultValue = "1") short replicas) throws Exception {
        kafkaAdminService.createTopic(name, partitions, replicas);
        return "✅ Topic created: " + name;
    }

    @DeleteMapping("/topics/{name}")
    public String deleteTopic(@PathVariable String name) throws Exception {
        kafkaAdminService.deleteTopic(name);
        return "🗑 Topic deleted: " + name;
    }

    @GetMapping("/topics")
    public List<String> listTopics() throws Exception {
        return kafkaAdminService.listTopics();
    }

    @DeleteMapping("/topics/prefix/{prefix}")
    public List<String> deleteByPrefix(@PathVariable String prefix) throws Exception {
        return kafkaAdminService.deleteTopicsByPrefix(prefix);
    }
}
```

---

## 🧪 Примеры запросов:

**Создание:**

```http
POST /api/kafka/topics?name=my-topic&partitions=3&replicas=1
```

**Удаление по имени:**

```http
DELETE /api/kafka/topics/my-topic
```

**Удаление по префиксу:**

```http
DELETE /api/kafka/topics/prefix/test-
```

**Получить список всех топиков:**

```http
GET /api/kafka/topics
```

---

## 🚀 Хочешь бонус?

> - ## Добавить **Swagger UI** для тестирования этих ручек?
Отлично! 🎉 Давай добавим **Swagger UI** (через Springdoc OpenAPI), чтобы удобно
тестировать наш Kafka REST API прямо из браузера.

---

## ✅ 1. Добавь зависимости в `pom.xml`

```xml

<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.2.0</version>
</dependency>
```

---

## 🛠 2. Swagger автоматически подхватит твои контроллеры

Если ты используешь аннотации `@RestController`, `@RequestMapping`,
`@GetMapping` и т.п. — Swagger всё сам найдёт. Но можно добавить немного
описаний для красоты:

```java
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/kafka")
@Tag(name = "Kafka Topic Management", description = "Управление топиками Kafka")
public class KafkaTopicController {

    private final KafkaAdminService kafkaAdminService;

    public KafkaTopicController(KafkaAdminService kafkaAdminService) {
        this.kafkaAdminService = kafkaAdminService;
    }

    @Operation(summary = "Создать Kafka-топик")
    @PostMapping("/topics")
    public String createTopic(@RequestParam String name,
                              @RequestParam(defaultValue = "1") int partitions,
                              @RequestParam(defaultValue = "1") short replicas) throws Exception {
        kafkaAdminService.createTopic(name, partitions, replicas);
        return "✅ Topic created: " + name;
    }

    @Operation(summary = "Удалить Kafka-топик")
    @DeleteMapping("/topics/{name}")
    public String deleteTopic(@PathVariable String name) throws Exception {
        kafkaAdminService.deleteTopic(name);
        return "🗑 Topic deleted: " + name;
    }

    @Operation(summary = "Получить список всех Kafka-топиков")
    @GetMapping("/topics")
    public List<String> listTopics() throws Exception {
        return kafkaAdminService.listTopics();
    }

    @Operation(summary = "Удалить топики по префиксу")
    @DeleteMapping("/topics/prefix/{prefix}")
    public List<String> deleteByPrefix(@PathVariable String prefix) throws Exception {
        return kafkaAdminService.deleteTopicsByPrefix(prefix);
    }
}
```

---

## 🌐 3. Запуск и просмотр

После запуска приложения Swagger будет доступен по адресу:

```
http://localhost:8080/swagger-ui.html
```

или

```
http://localhost:8080/swagger-ui/index.html
```

(оба работают, зависит от версии)

---

## 📜 Необязательно, но можно:

Добавить описание API:

```yaml
springdoc:
  api-docs:
    path: /v3/api-docs
  swagger-ui:
    path: /swagger-ui.html
    operationsSorter: method
```

---

## ✅ Готово!

Теперь ты можешь тестировать Kafka REST API прямо в браузере, без Postman, curl
или кастомных клиентов.

Хочешь добавить:

- ✅ Авторизацию (например, JWT)?
- ✅ Группировку по тегам?
- ✅ Пример JSON-тела запроса?

Пиши — добавлю 💪

> - ## Или защиту через Spring Security?

Отлично! 💂‍♂️ Давай добавим **Spring Security** к нашему Kafka REST API, чтобы
Swagger и ручки были защищены — например, по логину и паролю (basic auth), или
токену (JWT — по желанию).

---

## 🛠 Что сделаем:

- Включим Spring Security;
- Добавим базовую аутентификацию (`username/password`);
- Защитим Swagger и REST API;
- Добавим пользователя в памяти;
- Можно будет расширить до JWT, если нужно.

---

## ✅ 1. Зависимость в `pom.xml`

```xml

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
```

---

## 🔐 2. Конфигурация Spring Security

Создаём `SecurityConfig.java`:

```java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults()); // Basic Auth

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new InMemoryUserDetailsManager(
                User.withUsername("admin")
                        .password("{noop}admin123") // {noop} — без шифрования
                        .roles("ADMIN")
                        .build()
        );
    }
}
```

---

## 🔑 3. Теперь при обращении к API или Swagger:

- Браузер покажет **basic auth** логин/пароль;
- Ввести:
    - **логин**: `admin`
    - **пароль**: `admin123`

---

## 🌐 Swagger UI с авторизацией:

Ты можешь нажать **Authorize** в Swagger UI и ввести логин/пароль. Swagger
отправит заголовок `Authorization: Basic ...` автоматически.

---

## ✅ Готово!

Теперь твой Kafka REST API защищён базовой аутентификацией.

---

## ⚡️ Хочешь next level?

- 🔐 JWT авторизация вместо basic auth?
- 🧾 Подключение пользователей из базы (JPA)?
- 🔒 Роли на методы `@PreAuthorize("hasRole('ADMIN')")`?

Пиши — настроим!

> - ## Или поддержку топиков через `.yml` конфиг?


Отличный шаг! 🔧 Добавим поддержку **Kafka-топиков через `application.yml`**,
чтобы не хардкодить их в Java-коде, а удобно конфигурировать при деплое или в
разных окружениях (dev, prod, staging и т.д.).

---

## ✅ Что сделаем:

- Опишем список топиков в `application.yml`;
- Создадим бин-конфигурацию, читающую список;
- Создадим топики через `KafkaAdmin`.

---

## 📁 1. `application.yml`

```yaml
kafka:
  topics:
    - name: user-events
      partitions: 3
      replicas: 1
    - name: audit-logs
      partitions: 1
      replicas: 1
```

---

## 🧾 2. Класс конфигурации (с бин-прочтением списка)

### 📦 `KafkaTopicsProperties.java`

```java
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "kafka")
@Data
public class KafkaTopicsProperties {
    private List<Topic> topics;

    @Data
    public static class Topic {
        private String name;
        private int partitions;
        private short replicas;
    }
}
```

---

## 🛠 3. Конфигурация `KafkaAdmin` + автосоздание топиков

### 📦 `KafkaDynamicTopicConfig.java`

```java
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
public class KafkaDynamicTopicConfig {

    private final KafkaTopicsProperties topicProps;

    public KafkaDynamicTopicConfig(KafkaTopicsProperties topicProps) {
        this.topicProps = topicProps;
    }

    @Bean
    public KafkaAdmin kafkaAdmin() {
        return new KafkaAdmin(Map.of("bootstrap.servers", "localhost:9092"));
    }

    @Bean
    public List<NewTopic> kafkaTopics() {
        return topicProps.getTopics().stream()
                .map(t -> TopicBuilder.name(t.getName())
                        .partitions(t.getPartitions())
                        .replicas(t.getReplicas())
                        .build())
                .collect(Collectors.toList());
    }
}
```

> 📌 В боевом окружении `bootstrap.servers` можно прокинуть из `application.yml`
> или `.env`.

---

## ✅ Как это работает:

- При запуске приложения Spring Boot:
    - загружает `kafka.topics` из YAML;
    - создаёт список `NewTopic` бинов;
    - `KafkaAdmin` видит их и автоматически создаёт.

---

## 🧪 Пример:

```bash
Topic created: user-events (3 partitions, 1 replica)
Topic created: audit-logs (1 partition, 1 replica)
```

---

## 💡 Бонусы:

- 🔁 Легко управлять топиками в разных окружениях;
- 🧩 Гибко настраивается через Spring Profiles;
- 🛡 Можно использовать для валидации, логирования или мониторинга.

---

Хочешь:

- ⏫ Обновлять партиции через конфиг?
- 🌐 Позволить REST-контроллеру читать YAML-конфиг?
- ⚙️ Использовать `.env` для параметров?

Скажи — покажу 💪

