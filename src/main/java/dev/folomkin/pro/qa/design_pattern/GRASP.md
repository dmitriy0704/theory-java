# GRASP паттерны
GRASP (General Responsibility Assignment Software Patterns) — это набор принципов проектирования, которые помогают распределить обязанности между классами и объектами в объектно-ориентированном программировании. Эти паттерны применимы к любому языку, включая Java. Ниже перечислены основные GRASP-паттерны с кратким описанием и примерами их реализации на Java:

---

### 1. **Creator (Создатель)**
**Описание**: Определяет, какой класс должен создавать объекты другого класса. Обычно класс A создаёт объекты класса B, если A содержит B, агрегирует B, или использует B.

**Пример**:
```java
class Order {
    private List<OrderItem> items;

    public Order() {
        items = new ArrayList<>();
    }

    public OrderItem addItem(Product product, int quantity) {
        OrderItem item = new OrderItem(product, quantity); // Order создаёт OrderItem
        items.add(item);
        return item;
    }
}

class OrderItem {
    private Product product;
    private int quantity;

    public OrderItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }
}
```

---

### 2. **Information Expert (Информационный эксперт)**
**Описание**: Обязанности назначаются классу, который обладает всей необходимой информацией для их выполнения.

**Пример**:
```java
class Order {
    private List<OrderItem> items;

    public double calculateTotal() {
        return items.stream()
                    .mapToDouble(OrderItem::getSubtotal)
                    .sum(); // Order знает о своих элементах и может вычислить сумму
    }
}

class OrderItem {
    private Product product;
    private int quantity;

    public double getSubtotal() {
        return product.getPrice() * quantity; // OrderItem знает о продукте и количестве
    }
}
```

---

### 3. **Low Coupling (Низкая связанность)**
**Описание**: Минимизирует зависимости между классами, чтобы изменения в одном классе не затрагивали другие.

**Пример**:
```java
interface PaymentProcessor {
    void processPayment(double amount);
}

class PayPalProcessor implements PaymentProcessor {
    public void processPayment(double amount) {
        // Логика оплаты через PayPal
    }
}

class Order {
    private PaymentProcessor processor;

    public Order(PaymentProcessor processor) {
        this.processor = processor; // Внедрение зависимости
    }

    public void checkout(double total) {
        processor.processPayment(total); // Низкая связанность через интерфейс
    }
}
```

---

### 4. **High Cohesion (Высокая связность)**
**Описание**: Все методы и данные класса должны быть связаны с его единственной ответственностью.

**Пример**:
```java
class Customer {
    private String name;
    private String email;

    public void updateProfile(String name, String email) {
        this.name = name;
        this.email = email; // Все методы относятся к управлению данными клиента
    }

    public String getProfile() {
        return "Name: " + name + ", Email: " + email;
    }
}
```

---

### 5. **Controller (Контроллер)**
**Описание**: Назначает классу роль посредника для обработки запросов, координируя взаимодействие между моделью и представлением.

**Пример**:
```java
class OrderController {
    private OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    public void createOrder(String productId, int quantity) {
        orderService.createOrder(productId, quantity); // Контроллер управляет процессом
    }
}

class OrderService {
    public void createOrder(String productId, int quantity) {
        // Логика создания заказа
    }
}
```

---

### 6. **Polymorphism (Полиморфизм)**
**Описание**: Поведение системы зависит от типа объекта, а не от условных операторов.

**Пример**:
```java
interface Discount {
    double applyDiscount(double price);
}

class PercentageDiscount implements Discount {
    private double percentage;

    public PercentageDiscount(double percentage) {
        this.percentage = percentage;
    }

    public double applyDiscount(double price) {
        return price * (1 - percentage / 100);
    }
}

class FixedDiscount implements Discount {
    private double amount;

    public FixedDiscount(double amount) {
        this.amount = amount;
    }

    public double applyDiscount(double price) {
        return price - amount;
    }
}

class Order {
    public double calculatePrice(double originalPrice, Discount discount) {
        return discount.applyDiscount(originalPrice); // Полиморфное поведение
    }
}
```

---

### 7. **Pure Fabrication (Чистая выдумка)**
**Описание**: Создаётся искусственный класс для повышения связности или снижения связанности, даже если он не представляет реальную сущность.

**Пример**:
```java
class InvoiceGenerator {
    public void generateInvoice(Order order) {
        // Логика формирования счёта
        System.out.println("Invoice for order: " + order.calculateTotal());
    }
}

class Order {
    private List<OrderItem> items;

    public double calculateTotal() {
        return items.stream().mapToDouble(OrderItem::getSubtotal).sum();
    }
}
```

---

### 8. **Indirection (Перенаправление)**
**Описание**: Создаёт промежуточный объект для управления взаимодействием между компонентами, снижая их прямую зависимость.

**Пример**:
```java
interface OrderRepository {
    void save(Order order);
}

class DatabaseOrderRepository implements OrderRepository {
    public void save(Order order) {
        // Сохранение в базе данных
    }
}

class OrderService {
    private OrderRepository repository;

    public OrderService(OrderRepository repository) {
        this.repository = repository; // Перенаправление через репозиторий
    }

    public void saveOrder(Order order) {
        repository.save(order);
    }
}
```

---

### 9. **Protected Variations (Защищённые вариации)**
**Описание**: Защищает систему от изменений в одной части, изолируя нестабильные элементы через интерфейсы или абстракции.

**Пример**:
```java
interface Notification {
    void send(String message);
}

class EmailNotification implements Notification {
    public void send(String message) {
        // Отправка email
    }
}

class SMSNotification implements Notification {
    public void send(String message) {
        // Отправка SMS
    }
}

class Order {
    private Notification notification;

    public Order(Notification notification) {
        this.notification = notification; // Изоляция через интерфейс
    }

    public void complete() {
        notification.send("Order completed!");
    }
}
```

---

### Применение GRASP в Java
GRASP-паттерны не являются шаблонами проектирования в классическом смысле (как GoF), а скорее принципами, которые помогают принимать решения о распределении обязанностей. Они часто используются в сочетании с другими паттернами (например, Factory, Strategy) и принципами SOLID.

Если вам нужен более подробный пример какого-либо паттерна или помощь с конкретной задачей, напишите!