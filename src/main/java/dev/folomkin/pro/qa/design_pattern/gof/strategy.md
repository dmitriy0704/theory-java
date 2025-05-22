# Strategy (Стратегия)

Стратегия — это поведенческий паттерн проектирования, который определяет
семейство схожих алгоритмов и помещает каждый из них в собственный класс, после
чего алгоритмы можно взаимозаменять прямо во время исполнения программы.

Он особенно полезен, когда нужно поддерживать разные варианты поведения или
алгоритмов для одной задачи.

### **Описание паттерна**

**Цель**:

- Определить семейство алгоритмов и сделать их взаимозаменяемыми.
- Позволить клиентскому коду выбирать алгоритм во время выполнения.
- Изолировать логику алгоритма от контекста, который его использует.

**Когда использовать**:

- Когда нужно использовать разные варианты одного алгоритма (например, разные
  способы оплаты, сортировки, сжатия данных).
- Когда в коде много условных операторов (`if-else`), зависящих от типа
  поведения, которые можно вынести в отдельные классы.
- Когда нужно заменять алгоритм без изменения клиентского кода.
- Когда требуется изолировать реализацию алгоритма от его использования.

**Примеры использования**:

- Различные способы оплаты в интернет-магазине (PayPal, кредитная карта,
  криптовалюта).
- Алгоритмы сортировки (`Comparator` в Java).
- Разные стратегии рендеринга графики (2D, 3D).

### **Структура паттерна**

<img src="/img/design_pattern/design_patterns/strategy_structure.png" alt="factory_method" style="width: 100%; max-width: 550px">

1. Контекст хранит ссылку на объект конкретной стратегии, работая с ним через
   общий интерфейс стратегий.
2. Стратегия определяет интерфейс, общий для всех вариаций алгоритма. Контекст
   использует этот интерфейс для вызова алгоритма.<br>
   Для контекста неважно, какая именно вариация алгоритма будет выбрана, так как
   все они имеют одинаковый интерфейс.
3. Конкретные стратегии реализуют различные вариации алгоритма.
4. Во время выполнения программы контекст получает вызовы от клиента и
   делегирует их объекту конкретной стратегии.
5. Клиент должен создать объект конкретной стратегии и передать его в
   конструктор контекста. Кроме этого, клиент должен иметь возможность заменить
   стратегию на лету, используя сеттер. Благодаря этому, контекст не будет знать
   о том, какая именно стратегия сейчас выбрана.

### **Реализация в Java**

Пример: реализация разных способов оплаты в интернет-магазине.

```java
import java.util.*;

// Интерфейс стратегии
interface PaymentStrategy {
    void pay(double amount);
}

// Конкретные стратегии
class CreditCardPayment implements PaymentStrategy {
    private String cardNumber;

    public CreditCardPayment(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    @Override
    public void pay(double amount) {
        System.out.println("Paid " + amount + " using Credit Card ending in " + cardNumber.substring(cardNumber.length() - 4));
    }
}

class PayPalPayment implements PaymentStrategy {
    private String email;

    public PayPalPayment(String email) {
        this.email = email;
    }

    @Override
    public void pay(double amount) {
        System.out.println("Paid " + amount + " using PayPal account " + email);
    }
}

// Контекст
class ShoppingCart {
    private PaymentStrategy paymentStrategy;
    private List<Double> items;

    public ShoppingCart() {
        this.items = new ArrayList<>();
    }

    public void setPaymentStrategy(PaymentStrategy paymentStrategy) {
        this.paymentStrategy = paymentStrategy;
    }

    public void addItem(double price) {
        items.add(price);
    }

    public void checkout() {
        double total = items.stream().mapToDouble(Double::doubleValue).sum();
        if (paymentStrategy == null) {
            throw new IllegalStateException("Payment strategy not set");
        }
        paymentStrategy.pay(total);
    }
}

// Клиентский код
public class StrategyExample {
    public static void main(String[] args) {
        ShoppingCart cart = new ShoppingCart();
        cart.addItem(100.0);
        cart.addItem(50.0);

        // Оплата кредитной картой
        cart.setPaymentStrategy(new CreditCardPayment("1234-5678-9012-3456"));
        cart.checkout(); // Вывод: Paid 150.0 using Credit Card ending in 3456

        // Оплата через PayPal
        cart.setPaymentStrategy(new PayPalPayment("user@example.com"));
        cart.checkout(); // Вывод: Paid 150.0 using PayPal account user@example.com
    }
}
```

### **Как это работает**

1. Клиент создаёт контекст (`ShoppingCart`) и задаёт конкретную стратегию (
   `CreditCardPayment` или `PayPalPayment`) через метод `setPaymentStrategy`.
2. Контекст использует стратегию для выполнения действия (`pay`) без знания её
   внутренней реализации.
3. Клиент может менять стратегию во время выполнения, не изменяя код контекста.

### **Реальное использование в Java**

1. **Java Collections (Comparator)**:
   `Collections.sort` использует паттерн Strategy через интерфейс `Comparator`:
   ```java
   List<String> names = Arrays.asList("Bob", "Alice", "Charlie");
   Collections.sort(names, String::compareToIgnoreCase); // Стратегия сортировки
   System.out.println(names); // [Alice, Bob, Charlie]
   ```
   Здесь `Comparator` — это стратегия, определяющая порядок сортировки.

2. **Spring Framework**:
   В Spring разные реализации сервисов (например, для обработки запросов) могут
   быть инкапсулированы как стратегии:
   ```java
   @Service
   interface PaymentService {
       void pay(double amount);
   }

   @Service
   class CreditCardService implements PaymentService {
       public void pay(double amount) { /* Логика */ }
   }

   @Autowired
   private PaymentService paymentService; // Инъекция стратегии
   ```

3. **Stream API**:
   Функции, передаваемые в методы `map`, `filter` или `reduce`, действуют как
   стратегии:
   ```java
   List<Integer> numbers = Arrays.asList(1, 2, 3);
   numbers.stream().map(n -> n * 2).forEach(System.out::println); // Стратегия: умножение на 2
   ```

### **Преимущества**

- **Гибкость**: Легко добавлять новые алгоритмы, создавая новые классы
  стратегий.
- **Соответствие принципу Open/Closed**: Контекст открыт для расширения (новые
  стратегии), но закрыт для модификации.
- **Изоляция**: Логика алгоритма отделена от контекста, что упрощает
  тестирование.
- **Устранение условных операторов**: Вместо `if-else` для выбора поведения
  используется полиморфизм.
- **Динамическая смена поведения**: Стратегию можно менять во время выполнения.

### **Недостатки**

- **Увеличение числа классов**: Каждая новая стратегия требует отдельного
  класса.
- **Дополнительная сложность**: Для простых случаев паттерн может быть
  избыточным.
- **Необходимость настройки**: Клиент должен знать, какую стратегию выбрать, и
  передать её в контекст.

### **Отличие от других паттернов**

- **Strategy vs State**:
    - **Strategy** определяет взаимозаменяемые алгоритмы, которые не зависят от
      состояния контекста.
    - **State** управляет поведением, зависящим от внутреннего состояния
      объекта, и может изменять состояние контекста.
- **Strategy vs Command**:
    - **Strategy** фокусируется на выборе алгоритма для одной задачи.
    - **Command** инкапсулирует запрос как объект, часто для выполнения действий
      позже (например, undo/redo).
- **Strategy vs Template Method**:
    - **Strategy** использует композицию (стратегия передаётся в контекст).
    - **Template Method** использует наследование (подклассы переопределяют шаги
      алгоритма).

### **Проблемы и антипаттерны**

1. **Слишком много стратегий**: Если стратегий становится слишком много, код
   может стать громоздким.
    - **Решение**: Рассмотрите использование функциональных интерфейсов или
      лямбда-выражений для простых случаев.
2. **Жёсткая привязка клиента**: Клиент должен знать о всех стратегиях и
   выбирать подходящую.
    - **Решение**: Используйте Factory или DI (например, Spring) для создания
      стратегий.
3. **Избыточность для простых случаев**: Если есть только один алгоритм, паттерн
   может быть лишним.
    - **Решение**: Используйте Strategy только для задач с несколькими
      вариантами поведения.

### **Современные альтернативы в Java**

- **Лямбда-выражения и функциональные интерфейсы**:
  В Java 8+ Strategy часто заменяется лямбда-выражениями, что уменьшает
  необходимость в отдельных классах:
  ```java
  interface PaymentStrategy {
      void pay(double amount);
  }

  PaymentStrategy creditCard = amount -> System.out.println("Paid " + amount + " via Credit Card");
  PaymentStrategy payPal = amount -> System.out.println("Paid " + amount + " via PayPal");

  cart.setPaymentStrategy(creditCard);
  cart.checkout();
  ```
- **Spring Dependency Injection**:
  Spring позволяет внедрять стратегии как зависимости, упрощая их выбор:
  ```java
  @Service("creditCard")
  class CreditCardPayment implements PaymentStrategy { /* ... */ }

  @Autowired
  @Qualifier("creditCard")
  private PaymentStrategy paymentStrategy;
  ```
- **Map с перечислениями**:
  Для простых случаев можно использовать `Enum` и `Map` для выбора стратегии:
  ```java
  enum PaymentType {
      CREDIT_CARD(amount -> System.out.println("Paid " + amount + " via Credit Card")),
      PAYPAL(amount -> System.out.println("Paid " + amount + " via PayPal"));

      private final Consumer<Double> paymentAction;

      PaymentType(Consumer<Double> paymentAction) {
          this.paymentAction = paymentAction;
      }

      void pay(double amount) {
          paymentAction.accept(amount);
      }
  }
  ```

### **Итог**

Паттерн Strategy — это мощный инструмент для управления различными алгоритмами
или поведениями, обеспечивающий гибкость и изоляцию кода. Он широко используется
в Java, особенно в стандартной библиотеке (`Comparator`, Stream API) и
фреймворках (Spring). В современных приложениях Strategy часто упрощается с
помощью лямбда-выражений или DI, что делает код компактнее. Однако для сложных
систем с множеством алгоритмов классическая реализация Strategy остаётся
предпочтительной.
