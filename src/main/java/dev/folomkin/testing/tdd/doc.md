# TDD

```java
//code
package dev.folomkin.testing.tdd;

import java.math.BigDecimal;

public class TicketRevenue {

    private final static int TICKET_PRICE = 30;

    public BigDecimal estimateTotalRevenue(int numberOfTicketsSold)
            throws IllegalArgumentException {

        if (numberOfTicketsSold < 0 || numberOfTicketsSold > 100) {
            throw new IllegalArgumentException("Должно быть == 1..100");
        }

        // Продано N биллетов
        return new BigDecimal(TICKET_PRICE * numberOfTicketsSold);
    }
}


//test

package dev.folomkin.tdd;

import dev.folomkin.testing.tdd.TicketRevenue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TicketRevenueTest {
    private TicketRevenue venueRevenue;
    private BigDecimal expectedRevenue;

    @BeforeEach
    public void setUp() {
        venueRevenue = new TicketRevenue();
    }

    @DisplayName("Продано отрицательное количество билетов")
    @Test
    public void failIfLessThanZeroTicketsAreSold() {
        assertThrows(IllegalArgumentException.class, () ->
                venueRevenue.estimateTotalRevenue(-1));
    }

    @DisplayName("Продано 0 билетов")
    @Test
    public void zeroSalesEqualsZeroRevenue() {
        assertEquals(BigDecimal.ZERO,
                venueRevenue.estimateTotalRevenue(0));
    }

    @DisplayName("Продан 1 билет")
    @Test
    public void oneTicketSoldIsThirtyInRevenue() {
        expectedRevenue = new BigDecimal("30");
        assertEquals(expectedRevenue, venueRevenue.estimateTotalRevenue(1));
    }

    @DisplayName("Продано N билетов")
    @Test
    public void tenTicketsSoldIsThreeHundredInRevenue() {
        expectedRevenue = new BigDecimal("300");
        assertEquals(expectedRevenue, venueRevenue.estimateTotalRevenue(10));
    }

    @DisplayName("Продано больше 100 билетов")
    @Test
    public void failIfMoreThanOneHundredTicketsAreSold() {
        assertThrows(IllegalArgumentException.class, () ->
                venueRevenue.estimateTotalRevenue(101));
    }
}



```

# Тестовые дублеры

- пустышки (dummy) - Объект, который передается, но не используется; обычно
  служит для того,
  чтобы выполнить формальные требования к списку параметров метода
- заглушки (stub) - Объект, который всегда возвращает один и тот же
  заготовленный результат;
  также может хранить фиктивное состояние
- суррогаты (fake) - Работоспособная реализация (не обязательно
  эксплуатационного качества),
  которая заменяет фактическую реализацию
- макеты, имитации (mock) - Объект, который представляет серию ожиданий и выдает
  заготовленные
  ответы

## Пустышка

Пустышка (dummy)— простейший из четырех типов тестовых дублеров. Напомним, что
его назначение — фигурировать в списках параметров или удовлетворять
обязательным требованиям к полям, когда вы твердо уверены в том, что
объект никогда не будет использоваться. Во многих случаях даже можно пере-
дать пустой объект (а то и null, хотя безопасность при этом не гарантируется).

```java
package dev.folomkin.testing.tdd;

import java.math.BigDecimal;

public class Ticket {

  public static final int BASIC_TICKET_PRICE = 30; // Цена по умолчанию
  private static final BigDecimal DISCOUNT_RATE = new BigDecimal("0.9"); // Скидка по умолчанию


  private final BigDecimal price;
  private final String clientName;

  public Ticket(String clientName) {
    this.clientName = clientName;
    price = new BigDecimal(BASIC_TICKET_PRICE);
  }

  public Ticket(String clientName, BigDecimal price) {
    this.clientName = clientName;
    this.price = price;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public BigDecimal getDiscountPrice() {
    return price.multiply(DISCOUNT_RATE);
  }

}

//test
package dev.folomkin.tdd;

import dev.folomkin.testing.tdd.stub.Ticket;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TicketTest {

  private static String dummyName = "Alice";

  @Test
  public void tenPercentDiscount() {
    dev.folomkin.testing.tdd.stub.Ticket ticket = new dev.folomkin.testing.tdd.stub.Ticket(dummyName, new BigDecimal("10"));
    assertEquals(new BigDecimal("9.0"), ticket.getDiscountPrice());
  }
}

```

## Заглушка

Заглушку (stub) обычно используют тогда, когда имеет смысл подменить реальную
реализацию объектом, который каждый раз возвращает один и тот же
ответ.

```java
package dev.folomkin.tdd;

import dev.folomkin.testing.tdd.stub.Price;
import dev.folomkin.testing.tdd.stub.Ticket;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TicketTest {

  private static String dummyName = "Alice";

  @Test
  public void tenPercentDiscount() {
    // Вместо реального класса используется стаб
    Price price = new StubPrice();

    Ticket ticket = new Ticket(price);
    assertEquals(new BigDecimal("9.0"), ticket.getDiscountPrice());
  }
}

```