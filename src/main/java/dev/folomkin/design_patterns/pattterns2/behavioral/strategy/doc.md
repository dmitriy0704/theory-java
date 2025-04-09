# Стратегия (Strategy)

- это поведенческий паттерн проектирования,
  который определяет семейство схожих алгоритмов и
  помещает каждый из них в собственный класс, после чего
  алгоритмы можно взаимозаменять прямо во время
  исполнения программы.

Применимость: Стратегия часто используется в Java-коде, особенно там, где нужно
подменять алгоритм во время выполнения программы. Начиная с Java 8, многие
примеры стратегии можно заменить простыми lambda-выражениями.

Примеры Стратегии в стандартных библиотеках Java:

java.util.Comparator#compare(), вызываемые из Collections#sort().

javax.servlet.http.HttpServlet: метод service(), а также все методы doXXX()
принимают объекты HttpServletRequest и HttpServletResponse в параметрах.

javax.servlet.Filter#doFilter()

Признаки применения паттерна: Класс делегирует выполнение вложенному объекту
абстрактного типа или интерфейса.

## Структура

![strategy_structure.png](/img/design_pattern/design_patterns/strategy_structure.png)

1. Контекст хранит ссылку на объект конкретной стратегии, работая с ним через
   общий интерфейс стратегий.
2. Стратегия определяет интерфейс, общий для всех вариаций алгоритма. Контекст
   использует этот интерфейс для вызова алгоритма.<br><br>
   Для контекста неважно, какая именно вариация алгоритма будет выбрана, так как
   все они имеют одинаковый интерфейс.
3. Конкретные стратегии реализуют различные вариации алгоритма.
4. Во время выполнения программы контекст получает вызовы от клиента и
   делегирует их объекту конкретной стратегии.
5. Клиент должен создать объект конкретной стратегии и передать его в
   конструктор контекста. Кроме этого, клиент должен иметь возможность заменить
   стратегию на лету, используя сеттер.Благодаряэтому,контекст не будет знать о
   том, какая именно стратегия сейчас выбрана.

## Пример кода

**Методы оплаты в интернет магазине**

В этом примере Стратегия реализует выбор платёжного метода в интернет магазине.
Когда пользователь сформировал заказ, он получает выбор из нескольких платёжных
стредств: электронного кошелька или кредитной карты.

В данном случае конкретные стратегии платёжных методов не только проводят саму
оплату, но и собирают необходимые данные на форме заказа.

```java
package dev.folomkin.design_patterns.pattterns2.behavioral.strategy;


//-> ./strategies
//-> ./strategies/PayStrategy.java: Общий интерфейс стратегий оплаты

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Общий интерфейс всех стратегий.
 */
interface PayStrategy {
    boolean pay(int paymentAmount);

    void collectPaymentDetails();
}

//-> ./strategies/PayByPayPal.java: Оплата через PayPal


/**
 * Конкретная стратегия. Реализует оплату корзины интернет магазина через
 * платежную систему PayPal.
 */
class PayByPayPal implements PayStrategy {
    private static final Map<String, String> DATA_BASE = new HashMap<>();
    private final BufferedReader READER = new BufferedReader(new InputStreamReader(System.in));
    private String email;
    private String password;
    private boolean signedIn;

    static {
        DATA_BASE.put("amanda1985", "amanda@ya.com");
        DATA_BASE.put("qwerty", "john@amazon.eu");
    }

    /**
     * Собираем данные от клиента.
     */
    @Override
    public void collectPaymentDetails() {
        try {
            while (!signedIn) {
                System.out.print("Enter the user's email: ");
                email = READER.readLine();
                System.out.print("Enter the password: ");
                password = READER.readLine();
                if (verify()) {
                    System.out.println("Data verification has been successful.");
                } else {
                    System.out.println("Wrong email or password!");
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private boolean verify() {
        setSignedIn(email.equals(DATA_BASE.get(password)));
        return signedIn;
    }

    /**
     * Если клиент уже вошел в систему, то для следующей оплаты данные вводить
     * не придется.
     */
    @Override
    public boolean pay(int paymentAmount) {
        if (signedIn) {
            System.out.println("Paying " + paymentAmount + " using PayPal.");
            return true;
        } else {
            return false;
        }
    }

    private void setSignedIn(boolean signedIn) {
        this.signedIn = signedIn;
    }
}


//-> ./strategies/PayByCreditCard.java:
//-> Оплата кредиткой

/**
 * Конкретная стратегия. Реализует оплату корзины интернет магазина кредитной
 * картой клиента.
 */
class PayByCreditCard implements PayStrategy {
    private final BufferedReader READER = new BufferedReader(new InputStreamReader(System.in));
    private CreditCard card;

    /**
     * Собираем данные карты клиента.
     */
    @Override
    public void collectPaymentDetails() {
        try {
            System.out.print("Enter the card number: ");
            String number = READER.readLine();
            System.out.print("Enter the card expiration date 'mm/yy': ");
            String date = READER.readLine();
            System.out.print("Enter the CVV code: ");
            String cvv = READER.readLine();
            card = new CreditCard(number, date, cvv);

            // Валидируем номер карты...

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * После проверки карты мы можем совершить оплату. Если клиент продолжает
     * покупки, мы не запрашиваем карту заново.
     */
    @Override
    public boolean pay(int paymentAmount) {
        if (cardIsPresent()) {
            System.out.println("Paying " + paymentAmount + " using Credit Card.");
            card.setAmount(card.getAmount() - paymentAmount);
            return true;
        } else {
            return false;
        }
    }

    private boolean cardIsPresent() {
        return card != null;
    }
}

//-> ./strategies/CreditCard.java:
//-> Кредитная карта

/**
 * Очень наивная реализация кредитной карты.
 */
class CreditCard {
    private int amount;
    private String number;
    private String date;
    private String cvv;

    CreditCard(String number, String date, String cvv) {
        this.amount = 100_000;
        this.number = number;
        this.date = date;
        this.cvv = cvv;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }
}

//-> ./order
//-> ./order/Order.java:
//-> Класс заказа

/**
 * Класс заказа. Ничего не знает о том каким способом (стратегией) будет
 * расчитыватся клиент, а просто вызывает метод оплаты. Все остальное стратегия
 * делает сама.
 */
class Order {
    private int totalCost = 0;
    private boolean isClosed = false;

    public void processOrder(PayStrategy strategy) {
        strategy.collectPaymentDetails();
        // Здесь мы могли бы забрать и сохранить платежные данные из стратегии.
    }

    public void setTotalCost(int cost) {
        this.totalCost += cost;
    }

    public int getTotalCost() {
        return totalCost;
    }

    public boolean isClosed() {
        return isClosed;
    }

    public void setClosed() {
        isClosed = true;
    }
}

//-> ./Demo.java:
//-> Клиентский код

/**
 * Первый в мире консольный интерет магазин.
 */
public class Code {
    private static Map<Integer, Integer> priceOnProducts = new HashMap<>();
    private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private static Order order = new Order();
    private static PayStrategy strategy;

    static {
        priceOnProducts.put(1, 2200);
        priceOnProducts.put(2, 1850);
        priceOnProducts.put(3, 1100);
        priceOnProducts.put(4, 890);
    }

    public static void main(String[] args) throws IOException {
        while (!order.isClosed()) {
            int cost;

            String continueChoice;
            do {
                System.out.print("Please, select a product:" + "\n" +
                        "1 - Mother board" + "\n" +
                        "2 - CPU" + "\n" +
                        "3 - HDD" + "\n" +
                        "4 - Memory" + "\n");
                int choice = Integer.parseInt(reader.readLine());
                cost = priceOnProducts.get(choice);
                System.out.print("Count: ");
                int count = Integer.parseInt(reader.readLine());
                order.setTotalCost(cost * count);
                System.out.print("Do you wish to continue selecting products? Y/N: ");
                continueChoice = reader.readLine();
            } while (continueChoice.equalsIgnoreCase("Y"));

            if (strategy == null) {
                System.out.println("Please, select a payment method:" + "\n" +
                        "1 - PalPay" + "\n" +
                        "2 - Credit Card");
                String paymentMethod = reader.readLine();

                // Клиент создаёт различные стратегии на основании
                // пользовательских данных, конфигурации и прочих параметров.
                if (paymentMethod.equals("1")) {
                    strategy = new PayByPayPal();
                } else {
                    strategy = new PayByCreditCard();
                }
            }

            // Объект заказа делегирует сбор платёжных данны стратегии, т.к.
            // только стратегии знают какие данные им нужны для приёма оплаты.
            order.processOrder(strategy);

            System.out.print("Pay " + order.getTotalCost() + " units or Continue shopping? P/C: ");
            String proceed = reader.readLine();
            if (proceed.equalsIgnoreCase("P")) {
                // И наконец, стратегия запускает приём платежа.
                if (strategy.pay(order.getTotalCost())) {
                    System.out.println("Payment has been successful.");
                } else {
                    System.out.println("FAIL! Please, check your data.");
                }
                order.setClosed();
            }
        }
    }
}


```

## Шаги реализации

1. Определите алгоритм, который подвержен частым измене-
   ниям.Такжеподойдёталгоритм,имеющийнескольковариа-
   ций,которыевыбираютсявовремявыполненияпрограммы.
2. Создайтеинтерфейсстратегий,описывающийэтоталгоритм.
   Он должен быть общим для всех вариантовалгоритма.
3. Поместитевариацииалгоритмавсобственныеклассы,кото-
   рые реализуют этотинтерфейс.
4. В классе контекста создайте поле для хранения ссылки на
   текущий объект-стратегию, а также метод для её измене-
   ния.Убедитесьвтом,чтоконтекстработаетсэтимобъектом
   только через общий интерфейсстратегий.
5. Клиенты контекста должны подавать в него соответствую-
   щийобъект-стратегию,когдахотят,чтобыконтекствёлсебя
   определённымобразом.