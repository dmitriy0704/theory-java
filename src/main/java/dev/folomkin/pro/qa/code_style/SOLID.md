# SOLID

SOLID — это акроним, представляющий пять принципов объектно-ориентированного
программирования и проектирования, которые помогают создавать гибкие,
масштабируемые и поддерживаемые системы. Эти принципы были сформулированы
Робертом Мартином (Uncle Bob) и активно применяются в разработке, включая
программирование на Java. Рассмотрим каждый принцип с примерами на Java:

---

### 1. **S — Single Responsibility Principle (Принцип единственной ответственности)**

**Каждый класс должен иметь только одну причину для изменения.**

Класс должен быть ответственен только за одну задачу или функциональность. Это
уменьшает связанность и упрощает поддержку кода.

**Пример**:  
Допустим, у нас есть класс, который обрабатывает заказы и отправляет уведомления
по email. Это нарушает SRP, так как класс выполняет две задачи. Лучше разделить
их.

```java
// Плохо: Класс делает два дела
class Order {
    void createOrder() {
        // Логика создания заказа
        System.out.println("Order created");
    }

    void sendEmail() {
        // Логика отправки email
        System.out.println("Email sent");
    }
}

// Хорошо: Разделяем ответственность
class Order {
    void createOrder() {
        // Логика создания заказа
        System.out.println("Order created");
    }
}

class EmailService {
    void sendEmail() {
        // Логика отправки email
        System.out.println("Email sent");
    }
}
```

### 2. **O — Open/Closed Principle (Принцип открытости/закрытости)**

**Классы должны быть открыты для расширения, но закрыты для модификации.**

Это значит, что поведение класса можно расширять (например, через наследование
или интерфейсы), не изменяя его исходный код.

**Пример**:  
Рассмотрим расчет скидки для разных типов клиентов. Вместо изменения класса для
каждого нового типа клиента, используем интерфейс.

```java
// Плохо: Изменяем класс для каждого нового типа скидки
class DiscountCalculator {
    double calculateDiscount(String customerType) {
        if (customerType.equals("Regular")) {
            return 0.1;
        } else if (customerType.equals("VIP")) {
            return 0.2;
        }
        return 0;
    }
}

// Хорошо: Используем интерфейс для расширения
interface Discount {
    double calculateDiscount();
}

class RegularCustomer implements Discount {
    @Override
    public double calculateDiscount() {
        return 0.1;
    }
}

class VIPCustomer implements Discount {
    @Override
    public double calculateDiscount() {
        return 0.2;
    }
}

class DiscountCalculator {
    double calculate(Discount discount) {
        return discount.calculateDiscount();
    }
}
```

### 3. L — Liskov Substitution Principle (Принцип подстановки Барбары Лисков)

**Объекты базового класса должны быть заменяемыми объектами производного класса
без изменения поведения программы.**

Производные классы должны дополнять, а не изменять поведение базового класса.

**Пример**:  
Класс `Bird` и его наследник `Penguin`. Пингвины не летают, поэтому метод `fly`
в `Penguin` может нарушить принцип, если он изменяет ожидаемое поведение.

```java
// Плохо: Нарушение LSP
class Bird {
    void fly() {
        System.out.println("Flying");
    }
}

class Penguin extends Bird {
    @Override
    void fly() {
        throw new UnsupportedOperationException("Penguins can't fly");
    }
}

// Хорошо: Разделяем поведение через интерфейсы
interface Flyable {
    void fly();
}

class Sparrow implements Flyable {
    @Override
    public void fly() {
        System.out.println("Sparrow is flying");
    }
}

class Penguin {
    // Пингвин не реализует Flyable, так как не умеет летать
}
```

### 4. **I — Interface Segregation Principle (Принцип разделения интерфейсов)**

**Клиенты не должны быть вынуждены реализовывать интерфейсы, которые они не
используют.**

Интерфейсы должны быть узкими и специфичными, чтобы классы не реализовали
ненужные методы.

**Пример**:  
Интерфейс с множеством методов может заставить классы реализовывать ненужные
функции.

```java
// Плохо: Слишком общий интерфейс
interface Worker {
    void work();

    void eat();
}

class Robot implements Worker {
    @Override
    public void work() {
        System.out.println("Robot is working");
    }

    @Override
    public void eat() {
        // Роботы не едят, но вынуждены реализовать метод
        throw new UnsupportedOperationException("Robots don't eat");
    }
}

// Хорошо: Разделяем интерфейсы
interface Workable {
    void work();
}

interface Eatable {
    void eat();
}

class Robot implements Workable {
    @Override
    public void work() {
        System.out.println("Robot is working");
    }
}

class Human implements Workable, Eatable {
    @Override
    public void work() {
        System.out.println("Human is working");
    }

    @Override
    public void eat() {
        System.out.println("Human is eating");
    }
}
```

### 5. **D — Dependency Inversion Principle (Принцип инверсии зависимостей)**

**Модули высокого уровня не должны зависеть от модулей низкого уровня. Оба
должны зависеть от абстракций.**

> Классы верхних уровней не должны зависеть от классов нижних уровней. <br>
> Оба должны зависеть от абстракций. <br>
> Абстракции не должны зависеть от деталей. <br>
> Детали должны зависеть от абстракций.


Зависимости должны строиться через абстракции (интерфейсы или абстрактные
классы), а не через конкретные реализации.

**Пример**:  
Класс, зависящий от конкретной реализации базы данных, трудно заменить на
другую.

```java
// Плохо: Прямая зависимость от конкретной реализации
class UserService {
    private MySQLDatabase database = new MySQLDatabase();

    void saveUser() {
        database.save();
    }
}

class MySQLDatabase {
    void save() {
        System.out.println("Saving to MySQL");
    }
}

// Хорошо: Зависимость от абстракции
interface Database {
    void save();
}

class MySQLDatabase implements Database {
    @Override
    public void save() {
        System.out.println("Saving to MySQL");
    }
}

class UserService {
    private Database database;

    public UserService(Database database) {
        this.database = database;
    }

    void saveUser() {
        database.save();
    }
}
```

### Ключевые аспекты DIP

1. **Абстракции**:
    - Используйте интерфейсы или абстрактные классы для определения контрактов.
    - Абстракции должны быть стабильными и не зависеть от конкретных деталей.

2. **Внедрение зависимостей (Dependency Injection)**:
    - Зависимости передаются извне (через конструктор, сеттер или параметры
      метода), а не создаются внутри класса.
    - Популярные способы: конструктор, сеттер, или использование
      DI-фреймворков (например, Spring).

3. **Инверсия управления (Inversion of Control, IoC)**:
    - DIP часто реализуется через IoC, где управление созданием и связыванием
      объектов передается контейнеру (например, Spring IoC Container).

4. **Тестируемость**:
    - DIP упрощает модульное тестирование, так как зависимости можно заменить на
      моки или стабы.


### Применение SOLID в Java

- **SRP** помогает избежать "божественных объектов", делая классы проще для
  тестирования и поддержки.
- **OCP** позволяет добавлять новую функциональность без изменения существующего
  кода, что упрощает масштабирование.
- **LSP** гарантирует, что подклассы не ломают ожидаемое поведение, что важно
  для полиморфизма.
- **ISP** уменьшает количество ненужного кода и упрощает реализацию интерфейсов.
- **DIP** делает систему гибкой, позволяя легко заменять зависимости (например,
  базы данных или внешние сервисы).

### Заключение

Применение принципов SOLID в Java требует дисциплины, но приводит к созданию
более чистого, модульного и тестируемого кода. Используйте интерфейсы,
абстрактные классы и инверсию зависимостей, чтобы сделать ваш код гибким и
готовым к изменениям.
