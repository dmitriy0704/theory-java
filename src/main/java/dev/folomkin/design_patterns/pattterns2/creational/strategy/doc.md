# Стратегия(Strategy)

Шаблон проектирования Strategy (Стратегия) — это один из шаблонов поведенческого
проектирования. Он используется, когда у нас есть несколько алгоритмов для
конкретной задачи, и позволяет клиенту выбирать желаемую реализацию во время
выполнения, инкапсулируя каждый из них и делая эти реализации взаимозаменяемыми.

Ключевые компоненты шаблона проектирования Strategy:

- Контекст: это класс, который имеет ссылку на интерфейс стратегии и может
  переключаться между различными конкретными стратегиями.
- Интерфейс стратегии: интерфейс, определяющий общие методы, которые должны
  реализовать все конкретные стратегии.
- Конкретные стратегии: это различные реализации интерфейса стратегии. Каждая
  конкретная стратегия предусматривает свой алгоритм или поведение.

```java
// Интерфейс Strategy
public interface PaymentStrategy {
  void pay(BigDecimal amount);
}

// Конкретные стратегии
@Component
public class CreditCardStrategy implements PaymentStrategy {

  public void pay(BigDecimal amount) {
    System.out.printf("Amount %s paid using CreditCardStrategy", amount);
  }
}

@Component
public class PayPalStrategy implements PaymentStrategy {

  public void pay(BigDecimal amount) {
    System.out.printf("Amount %s paid using PayPalStrategy", amount);
  }
}

```

У нас будет два типа способов оплаты: кредитная карта и PayPal.

```java
public enum PayMethod {
  PAYPAL("payPal"),
  CREDIT_CARD("creditCard");

  private final String value;

  PayMethod(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
```
