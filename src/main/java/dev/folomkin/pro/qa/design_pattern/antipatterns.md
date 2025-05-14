# Антипаттерны

Антипаттерны в программировании — это распространённые решения, которые кажутся
правильными, но приводят к проблемам в поддержке, масштабируемости или
производительности кода. В контексте Java антипаттерны часто связаны с
неправильным использованием объектно-ориентированного программирования,
управлением ресурсами или архитектурой. Ниже приведены основные антипаттерны,
характерные для Java, с описанием, примерами и рекомендациями, как их избегать.

---

### 1. **God Object (Божественный объект)**

**Описание**: Один класс выполняет слишком много функций, становясь центральным
узлом системы. Это нарушает принцип единственной ответственности (SRP).

**Пример**:

```java
class GodClass {
    public void processOrder(Order order) { /* Логика обработки заказа */ }

    public void sendEmail(String message) { /* Отправка email */ }

    public void saveToDatabase(Order order) { /* Сохранение в БД */ }

    public void generateReport() { /* Генерация отчёта */ }
    // ... и ещё 100 методов
}
```

**Проблемы**:

- Сложность поддержки из-за большого размера класса.
- Высокая связанность с другими частями системы.
- Трудности с тестированием.

**Решение**:

- Разделите класс на несколько, каждый с чёткой ответственностью.
- Применяйте GRASP (например, Information Expert) и SOLID.
- Используйте паттерны, такие как Facade или Strategy.

**Исправленный пример**:

```java
class OrderProcessor {
    public void processOrder(Order order) { /* Логика обработки заказа */ }
}

class EmailService {
    public void sendEmail(String message) { /* Отправка email */ }
}

class OrderRepository {
    public void saveToDatabase(Order order) { /* Сохранение в БД */ }
}
```

---

### 2. **Magic Numbers/String (Магические числа/строки)**

**Описание**: Использование жёстко закодированных чисел или строк без объяснения
их значения.

**Пример**:

```java
public class Order {
    public double calculateDiscount(double price) {
        if (price > 1000) {
            return price * 0.1; // Что означают 1000 и 0.1?
        }
        return price;
    }
}
```

**Проблемы**:

- Код трудно читать и поддерживать.
- Изменение значений требует поиска по всему коду.

**Решение**:

- Используйте именованные константы.
- Выносите конфигурацию в отдельные файлы (например, `properties`).

**Исправленный пример**:

```java
public class Order {
    private static final double DISCOUNT_RATE = 0.1;
    private static final double MINIMUM_DISCOUNT_PRICE = 1000;

    public double calculateDiscount(double price) {
        if (price > MINIMUM_DISCOUNT_PRICE) {
            return price * DISCOUNT_RATE;
        }
        return price;
    }
}
```

---

### 3. **Spaghetti Code (Код-спагетти)**

**Описание**: Код с запутанной структурой, без чёткой организации, часто с
чрезмерным использованием goto-подобной логики или глубокими вложенными
условиями.

**Пример**:

```java
public void processOrder(Order order) {
    if (order != null) {
        if (order.getItems().size() > 0) {
            for (OrderItem item : order.getItems()) {
                if (item.getPrice() > 0) {
                    // ... ещё 5 уровней вложенности
                }
            }
        }
    }
}
```

**Проблемы**:

- Трудно читать и отлаживать.
- Высокая вероятность ошибок.

**Решение**:

- Используйте ранний возврат (guard clauses).
- Разделяйте логику на методы.
- Применяйте паттерны, такие как Chain of Responsibility.

**Исправленный пример**:

```java
public void processOrder(Order order) {
    if (order == null || order.getItems().isEmpty()) {
        return;
    }
    for (OrderItem item : order.getItems()) {
        processItem(item);
    }
}

private void processItem(OrderItem item) {
    if (item.getPrice() <= 0) {
        return;
    }
    // Логика обработки
}
```

---

### 4. **Singleton Abuse (Злоупотребление синглтоном)**

**Описание**: Чрезмерное использование паттерна Singleton для глобального
доступа к объектам, что приводит к скрытым зависимостям и проблемам с
тестированием.

**Пример**:

```java
public class DatabaseConnection {
    private static DatabaseConnection instance = new DatabaseConnection();

    private DatabaseConnection() {
    }

    public static DatabaseConnection getInstance() {
        return instance;
    }

    public void query(String sql) { /* Выполнение запроса */ }
}

public class OrderService {
    public void saveOrder(Order order) {
        DatabaseConnection.getInstance().query("INSERT ..."); // Жёсткая зависимость
    }
}
```

**Проблемы**:

- Скрытые зависимости.
- Трудности с тестированием (нельзя подменить реализацию).
- Проблемы с потокобезопасностью.

**Решение**:

- Используйте Dependency Injection (например, через Spring).
- Ограничивайте использование Singleton реальными случаями (например, логгер).

**Исправленный пример**:

```java
public interface DatabaseConnection {
    void query(String sql);
}

public class MySQLConnection implements DatabaseConnection {
    public void query(String sql) { /* Реализация */ }
}

public class OrderService {
    private final DatabaseConnection dbConnection;

    public OrderService(DatabaseConnection dbConnection) {
        this.dbConnection = dbConnection; // Внедрение зависимости
    }

    public void saveOrder(Order order) {
        dbConnection.query("INSERT ...");
    }
}
```

---

### 5. **Leaking Abstractions (Утечка абстракций)**

**Описание**: Абстракция раскрывает детали реализации, вынуждая клиентов
зависеть от них.

**Пример**:

```java
public class FileReader {
    public String[] readLines(String filePath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        List<String> lines = new ArrayList<>();
        String line;
        while ((line = reader.readLine()) != null) {
            lines.add(line);
        }
        reader.close();
        return lines.toArray(new String[0]);
    }
}
```

**Проблемы**:

- Клиент должен знать, как обрабатывать исключения `IOException`.
- Жёсткая привязка к файлам (нельзя использовать для других источников).

**Решение**:

- Скрывайте детали реализации за интерфейсом.
- Используйте try-with-resources для управления ресурсами.

**Исправленный пример**:

```java
public interface DataReader {
    List<String> readLines();
}

public class FileDataReader implements DataReader {
    private final String filePath;

    public FileDataReader(String filePath) {
        this.filePath = filePath;
    }

    public List<String> readLines() {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            return reader.lines().collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException("Failed to read file", e);
        }
    }
}
```

---

### 6. **Premature Optimization (Преждевременная оптимизация)**

**Описание**: Оптимизация кода до анализа производительности, что приводит к
усложнению без реальной выгоды.

**Пример**:

```java
public class OrderProcessor {
    private final StringBuilder sb = new StringBuilder(1000); // Предполагаем, что строка будет большой

    public String generateOrderSummary(List<OrderItem> items) {
        sb.setLength(0); // Очистка StringBuilder
        for (OrderItem item : items) {
            sb.append(item.getName()).append(": ").append(item.getPrice()).append("\n");
        }
        return sb.toString();
    }
}
```

**Проблемы**:

- Усложняет код без доказанной необходимости.
- Может скрывать другие проблемы.

**Решение**:

- Пишите простой код и оптимизируйте только после профилирования.
- Используйте стандартные библиотеки, если они достаточно эффективны.

**Исправленный пример**:

```java
public class OrderProcessor {
    public String generateOrderSummary(List<OrderItem> items) {
        return items.stream()
                .map(item -> item.getName() + ": " + item.getPrice())
                .collect(Collectors.joining("\n"));
    }
}
```

---

### 7. **Copy-Paste Programming (Программирование копи-пастом)**

**Описание**: Повторение кода вместо создания переиспользуемых компонентов.

**Пример**:

```java
public class OrderService {
    public void processRegularOrder(Order order) {
        System.out.println("Processing order: " + order.getId());
        order.setStatus("PROCESSED");
        // ... 10 строк кода
    }

    public void processUrgentOrder(Order order) {
        System.out.println("Processing order: " + order.getId());
        order.setStatus("PROCESSED");
        // ... те же 10 строк кода
    }
}
```

**Проблемы**:

- Дублирование кода усложняет поддержку.
- Изменения нужно вносить во все копии.

**Решение**:

- Выделяйте общую логику в методы или классы.
- Используйте шаблоны, такие как Template Method.

**Исправленный пример**:

```java
public class OrderService {
    public void processRegularOrder(Order order) {
        processOrder(order);
    }

    public void processUrgentOrder(Order order) {
        processOrder(order);
        // Дополнительная логика для срочных заказов
    }

    private void processOrder(Order order) {
        System.out.println("Processing order: " + order.getId());
        order.setStatus("PROCESSED");
    }
}
```

---

### 8. **Resource Leaks (Утечка ресурсов)**

**Описание**: Неправильное управление ресурсами, такими как файлы, соединения
или потоки, что приводит к их исчерпанию.

**Пример**:

```java
public class FileProcessor {
    public void processFile(String filePath) throws IOException {
        FileInputStream fis = new FileInputStream(filePath);
        // Обработка файла
        fis.close(); // Может не выполниться при исключении
    }
}
```

**Проблемы**:

- Утечка ресурсов при исключениях.
- Ограниченные ресурсы (например, дескрипторы файлов) исчерпываются.

**Решение**:

- Используйте try-with-resources.
- Применяйте пулы соединений для баз данных.

**Исправленный пример**:

```java
public class FileProcessor {
    public void processFile(String filePath) throws IOException {
        try (FileInputStream fis = new FileInputStream(filePath)) {
            // Обработка файла
        }
    }
}
```

---

### Как избегать антипаттернов

1. **Следуйте принципам SOLID и GRASP**: Они помогают проектировать гибкий и
   поддерживаемый код.
2. **Пишите тесты**: Unit-тесты выявляют проблемы с зависимостями и структурой.
3. **Рефакторите регулярно**: Упрощайте код, удаляйте дублирование.
4. **Используйте инструменты анализа кода**: SonarQube, Checkstyle или PMD могут
   находить антипаттерны.
5. **Изучайте паттерны проектирования**: Они предлагают проверенные решения
   вместо импровизации.

Если вам нужен более глубокий разбор какого-либо антипаттерна или пример для
конкретного случая, дайте знать!