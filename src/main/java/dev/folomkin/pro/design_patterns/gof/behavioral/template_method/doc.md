# Шаблонный метод (Template method)

- это поведенческий паттерн проектирования, который определяет скелет алгоритма,
  перекладывая ответственность за некоторые его шаги на подклассы. Паттерн
  позволяет подклассам переопределять шаги алгоритма, не меняя его общей
  структуры.

Применимость: Шаблонные методы можно встретить во многих библиотечных классах
Java. Разработчики создают их, чтобы позволить клиентам легко и быстро расширять
стандартный код при помощи наследования.

Примеры Шаблонных методов в стандартных библиотеках Java:

Все не-абстрактные методы классов java.io.InputStream, java.io.OutputStream,
java.io.Reader и java.io.Writer.

Все не-абстрактные методы классов java.util.AbstractList, java.util.AbstractSet
и java.util.AbstractMap.

javax.servlet.http.HttpServlet, все методы doXXX() по умолчанию возвращают
HTTP-код 405 "Method Not Allowed". Однако вы можете переопределить их при
желании.

Признаки применения паттерна: Класс заставляет своих потомков реализовать
методы-шаги, но самостоятельно реализует структуру алгоритма.

## Структура

![template_method_structure.png](/img/design_pattern/design_patterns/template_method_structure.png)

1. Абстрактный класс определяет шаги алгоритма и содержит
   шаблонныйметод,состоящийизвызововэтихшагов.Шаги
   могут быть как абстрактными, так и содержать реализацию
   поумолчанию.
2. Конкретный класс переопределяет некоторые (или все)
   шагиалгоритма.Конкретныеклассынепереопределяютсам
   шаблонныйметод.

## Примеры кода

**Переопределение шагов алгоритма**

Социальные сети предоставляют собственные методы API для авторизации, постинга и
выхода, но общий процесс для всех сетей совпадает.

```java
package dev.folomkin.design_patterns.pattterns.behavioral.template_method;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

//-> ./networks
//-> ./networks/Network.java: Базовый класс социальной сети

/**
 * Базовый класс социальной сети.
 */
abstract class Network {
    String userName;
    String password;

    Network() {
    }

    /**
     * Публикация данных в любой сети.
     * Шаблонный метод должен быть задан в базовом классе. Он
     * состоит из вызовов методов в определённом порядке. Чаще
     * всего эти методы являются шагами некоего алгоритма.
     * Шаблонных методов в классе может быть несколько.
     */
    public boolean post(String message) {
        // Проверка данных пользователя перед постом в соцсеть. Каждая сеть для
        // проверки использует разные методы.
        if (logIn(this.userName, this.password)) {
            // Отправка данных.
            boolean result = sendData(message.getBytes());
            logOut();
            return result;
        }
        return false;
    }

    abstract boolean logIn(String userName, String password);

    abstract boolean sendData(byte[] data);

    abstract void logOut();
}

//-> ./networks/Facebook.java:
//-> Конкретная социальная сеть

/**
 * Класс социальной сети.
 */
class Facebook extends template_method.behavioral.gof.dev.folomkin.pro.design_patterns.Network {
    public Facebook(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public boolean logIn(String userName, String password) {
        System.out.println("\nChecking user's parameters");
        System.out.println("Name: " + this.userName);
        System.out.print("Password: ");
        for (int i = 0; i < this.password.length(); i++) {
            System.out.print("*");
        }
        simulateNetworkLatency();
        System.out.println("\n\nLogIn success on Facebook");
        return true;
    }

    public boolean sendData(byte[] data) {
        boolean messagePosted = true;
        if (messagePosted) {
            System.out.println("Message: '" + new String(data) + "' was posted on Facebook");
            return true;
        } else {
            return false;
        }
    }

    public void logOut() {
        System.out.println("User: '" + userName + "' was logged out from Facebook");
    }

    private void simulateNetworkLatency() {
        try {
            int i = 0;
            System.out.println();
            while (i < 10) {
                System.out.print(".");
                Thread.sleep(500);
                i++;
            }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }
}

//-> ./networks/Twitter.java:
//-> Ещё одна конкретная социальная сеть

/**
 * Класс социальной сети.
 */
class Twitter extends template_method.behavioral.gof.dev.folomkin.pro.design_patterns.Network {

    public Twitter(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public boolean logIn(String userName, String password) {
        System.out.println("\nChecking user's parameters");
        System.out.println("Name: " + this.userName);
        System.out.print("Password: ");
        for (int i = 0; i < this.password.length(); i++) {
            System.out.print("*");
        }
        simulateNetworkLatency();
        System.out.println("\n\nLogIn success on Twitter");
        return true;
    }

    public boolean sendData(byte[] data) {
        boolean messagePosted = true;
        if (messagePosted) {
            System.out.println("Message: '" + new String(data) + "' was posted on Twitter");
            return true;
        } else {
            return false;
        }
    }

    public void logOut() {
        System.out.println("User: '" + userName + "' was logged out from Twitter");
    }

    private void simulateNetworkLatency() {
        try {
            int i = 0;
            System.out.println();
            while (i < 10) {
                System.out.print(".");
                Thread.sleep(500);
                i++;
            }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }
}

//-> ./Demo.java; Клиентский код

/**
 * Демо-класс. Здесь всё сводится воедино.
 */
public class Code {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        template_method.behavioral.gof.dev.folomkin.pro.design_patterns.Network network = null;
        System.out.print("Input user name: ");
        String userName = reader.readLine();
        System.out.print("Input password: ");
        String password = reader.readLine();

        // Вводим сообщение.
        System.out.print("Input message: ");
        String message = reader.readLine();

        System.out.println("\nChoose social network for posting message.\n" +
                "1 - Facebook\n" +
                "2 - Twitter");
        int choice = Integer.parseInt(reader.readLine());

        // Создаем сетевые объекты и публикуем пост.
        if (choice == 1) {
            network = new template_method.behavioral.gof.dev.folomkin.pro.design_patterns.Facebook(userName, password);
        } else if (choice == 2) {
            network = new template_method.behavioral.gof.dev.folomkin.pro.design_patterns.Twitter(userName, password);
        }
        network.post(message);
    }
}



```

## Шаги реализации

1. Изучите алгоритм и подумайте, можно ли его разбить на
   шаги. Прикиньте, какие шаги будут стандартными для всех
   вариаций алгоритма, а какие— изменяющимися.
2. Создайте абстрактный базовый класс. Определите в нём
   шаблонный метод. Этот метод должен состоять из вызовов
   шагов алгоритма. Имеет смысл сделать шаблонный метод
   финальным,чтобыподклассынемоглипереопределитьего
   (если ваш язык программирования этопозволяет).
3. Добавьтевабстрактныйклассметодыдлякаждогоизшагов
   алгоритма. Вы можете сделать эти методы абстрактными
   илидобавитькакую-тореализациюпоумолчанию.Впервом
   случае все подклассы должны будут реализовать эти мето-
   ды,авовтором—толькоеслиреализацияшагавподклассе
   отличается от стандартнойверсии.
4. Подумайте о введении в алгоритм хуков. Чаще всего, хуки располагают между
   основными шагами алгоритма, а также до и после всех шагов.
5. Создайте конкретные классы, унаследовав их от абстрактного класса. Реализуйте
   в них все недостающие шаги и хуки.