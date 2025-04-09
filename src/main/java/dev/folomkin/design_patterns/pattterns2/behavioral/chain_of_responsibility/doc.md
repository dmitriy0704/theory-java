# Цепочка обязанностей<br> (chain of responsibility)

- это поведенческий паттерн
  проектирования, который позволяет передавать запросы
  последовательно по цепочке обработчиков. Каждый
  последующий обработчик решает, может ли он обработать
  запрос сам и стоит ли передавать запрос дальше по цепи.

Избавляет от жёсткой привязки отправителя запроса к его получателю, позволяя
выстраивать цепь из различных обработчиков динамически.

Применимость: Паттерн встречается в Java не так уж часто, так как для его
применения нужна цепь объектов, например, связанный список.

Область применения цепочки обязанностей — всевозможные обработчики событий,
последовательные проверки доступа и прочее.

Примеры Цепочки обязанностей в стандартных библиотеках Java:

java.util.logging.Logger#log()
javax.servlet.Filter#doFilter()
Признаки применения паттерна: Цепочку обязанностей можно определить по спискам
обработчиков или проверок, через которые пропускаются запросы. Особенно если
порядок следования обработчиков важен.

## Структура

![chain_structure.png](/img/design_pattern/design_patterns/chain_structure.png)

1. Обработчик определяет общий для всех конкретных обработчиков интерфейс.
   Обычно достаточно описать единственный метод обработки запросов, но иногда
   здесь может
   быть объявлен и метод выставления следующего
   обработчика.
2. Базовый обработчик — опциональный класс, который позволяет избавиться от
   дублирования одного и того же кода
   во всех конкретных обработчиках.  
   Обычно этот класс имеет поле для хранения ссылки на следующий обработчик в
   цепочке. Клиент связывает обработчики в цепь, подавая ссылку на следующий
   обработчик
   через конструктор или сеттер поля. Также здесь можно реализовать базовый
   метод обработки, который бы просто
   перенаправлял запрос следующему обработчику, проверив
   его наличие.
3. Конкретные обработчики содержат код обработки запросов. При получении запроса
   каждый обработчик решает,
   может ли он обработать запрос, а также стоит ли передать
   его следующему объекту.  
   В большинстве случаев обработчики могут работать сами по
   себе и быть неизменяемыми, получив все нужные детали
   через параметры конструктора.
4. Клиент может либо сформировать цепочку обработчиков
   единожды, либо перестраивать её динамически, в зависимости от логики
   программы. Клиент может отправлять
   запросы любому из объектов цепочки, не обязательно первому из них.

## Примеры кода

**Слои авторизации и аутентификации пользователей.**

Этот пример показывает как пользовательские данные проходят последовательную
аутентификацию в множестве обработчиков, связанных в одну цепь.

Этот пример отличается от канонической версии тем, что проверка обрывается, если
очередной обработчик не может обработать запрос. В классическом варианте,
следование по цепочке заканчивается как только нашёлся элемент цепи, который
может обработать запрос. Просто знайте, что Концептуальный пример от этого не
меняется, а код отличается только условием выхода из цепи.

```java

package dev.folomkin.design_patterns.pattterns2.behavioral.chain_of_responsibility;


//-> ./middleware
//-> ./middleware/Middleware.java: Базовый класс проверок

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Базовый класс цепочки.
 */
abstract class Middleware {
    private Middleware next;

    /**
     * Помогает строить цепь из объектов-проверок.
     */
    public static Middleware link(Middleware first, Middleware... chain) {
        Middleware head = first;
        for (Middleware nextInChain : chain) {
            head.next = nextInChain;
            head = nextInChain;
        }
        return first;
    }

    /**
     * Подклассы реализуют в этом методе конкретные проверки.
     */
    public abstract boolean check(String email, String password);

    /**
     * Запускает проверку в следующем объекте или завершает проверку, если мы в
     * последнем элементе цепи.
     */
    protected boolean checkNext(String email, String password) {
        if (next == null) {
            return true;
        }
        return next.check(email, password);
    }
}

//-> ./middleware/ThrottlingMiddleware.java:
//->  Проверка на лимит запросов

/**
 * Конкретный элемент цепи обрабатывает запрос по-своему.
 */
class ThrottlingMiddleware extends Middleware {
    private int requestPerMinute;
    private int request;
    private long currentTime;

    public ThrottlingMiddleware(int requestPerMinute) {
        this.requestPerMinute = requestPerMinute;
        this.currentTime = System.currentTimeMillis();
    }

    /**
     * Обратите внимание, вызов checkNext() можно вставить как в начале этого
     * метода, так и в середине или в конце.
     * <p>
     * Это даёт еще один уровень гибкости по сравнению с проверками в цикле.
     * Например, элемент цепи может пропустить все остальные проверки вперёд и
     * запустить свою проверку в конце.
     */
    public boolean check(String email, String password) {
        if (System.currentTimeMillis() > currentTime + 60_000) {
            request = 0;
            currentTime = System.currentTimeMillis();
        }

        request++;

        if (request > requestPerMinute) {
            System.out.println("Request limit exceeded!");
            Thread.currentThread().stop();
        }
        return checkNext(email, password);
    }
}

//-> ./middleware/UserExistsMiddleware.java:
//-> Проверка пароля

/**
 * Конкретный элемент цепи обрабатывает запрос по-своему.
 */
class UserExistsMiddleware extends Middleware {
    private Server server;

    public UserExistsMiddleware(Server server) {
        this.server = server;
    }

    public boolean check(String email, String password) {
        if (!server.hasEmail(email)) {
            System.out.println("This email is not registered!");
            return false;
        }
        if (!server.isValidPassword(email, password)) {
            System.out.println("Wrong password!");
            return false;
        }
        return checkNext(email, password);
    }
}

//-> ./middleware/RoleCheckMiddleware.java:
//-> Проверка роли

/**
 * Конкретный элемент цепи обрабатывает запрос по-своему.
 */
class RoleCheckMiddleware extends Middleware {
    public boolean check(String email, String password) {
        if (email.equals("admin@example.com")) {
            System.out.println("Hello, admin!");
            return true;
        }
        System.out.println("Hello, user!");
        return checkNext(email, password);
    }
}

//-> ./server
//-> ./server/Server.java:Сервер, на который заходим

/**
 * Класс сервера.
 */
class Server {
    private Map<String, String> users = new HashMap<>();
    private Middleware middleware;

    /**
     * Клиент подаёт готовую цепочку в сервер. Это увеличивает гибкость и
     * упрощает тестирование класса сервера.
     */
    public void setMiddleware(Middleware middleware) {
        this.middleware = middleware;
    }

    /**
     * Сервер получает email и пароль от клиента и запускает проверку
     * авторизации у цепочки.
     */
    public boolean logIn(String email, String password) {
        if (middleware.check(email, password)) {
            System.out.println("Authorization have been successful!");

            // Здесь должен быть какой-то полезный код, работающий для
            // авторизированных пользователей.

            return true;
        }
        return false;
    }

    public void register(String email, String password) {
        users.put(email, password);
    }

    public boolean hasEmail(String email) {
        return users.containsKey(email);
    }

    public boolean isValidPassword(String email, String password) {
        return users.get(email).equals(password);
    }
}

//-> ./Demo.java:
//-> Клиентский код


/**
 * Демо-класс. Здесь всё сводится воедино.
 */
public class Demo {
    private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private static Server server;

    private static void init() {
        server = new Server();
        server.register("admin@example.com", "admin_pass");
        server.register("user@example.com", "user_pass");

        // Проверки связаны в одну цепь. Клиент может строить различные цепи,
        // используя одни и те же компоненты.
        Middleware middleware = Middleware.link(
                new ThrottlingMiddleware(2),
                new UserExistsMiddleware(server),
                new RoleCheckMiddleware()
        );

        // Сервер получает цепочку от клиентского кода.
        server.setMiddleware(middleware);
    }

    public static void main(String[] args) throws IOException {
        init();

        boolean success;
        do {
            System.out.print("Enter email: ");
            String email = reader.readLine();
            System.out.print("Input password: ");
            String password = reader.readLine();
            success = server.logIn(email, password);
        } while (!success);
    }
}

```

## Шаги реализации

1. Создайтеинтерфейсобработчикаиопишитевнёмосновной
   методобработки.
   Продумайте, в каком виде клиент должен передавать дан-
   ные запроса в обработчик. Самый гибкий способ — пре-
   вратитьданныезапросавобъектипередаватьегоцеликом
   через параметры методаобработчика.

2. Имеет смысл создать абстрактный базовый класс обработ-
   чиков,чтобынедублироватьреализациюметодаполучения
   следующегообработчикавовсехконкретныхобработчиках.
   Добавьте в базовый обработчик поле для хранения ссылки
   на следующий объект цепочки. Устанавливайте начальное
   значениеэтогополячерезконструктор.Этосделаетобъекты
   обработчиковнеизменяемыми.Ноеслипрограммапредпо-
   лагает динамическую перестройку цепочек, можете доба-
   вить и сеттер дляполя.<br>
   Реализуйтебазовыйметодобработкитак,чтобыонперена-
   правлялзапросследующемуобъекту,проверивегоналичие.
   Этопозволитполностьюскрытьполе-ссылкуотподклассов,
   дав им возможность передавать запросы дальше по цепи,
   обращаясь к родительской реализацииметода
3. Один за другим создайте классы конкретных обработчиков
   и реализуйте в них методы обработки запросов. При полу-
   чении запроса каждый обработчик долженрешить:
    - Может ли он обработать запрос или нет?
    - Следует ли передать запрос следующему обработчику
      или нет?
4. Клиент может собирать цепочку обработчиков самостоя-
   тельно,опираясьнасвоюбизнес-логику,либополучатьуже
   готовые цепочки извне. В последнем случае цепочки соби-
   раютсяфабричнымиобъектами,опираясь на конфигурацию
   приложения или параметры окружения.
5. Клиент может посылать запросы любому обработчику в
   цепи, а не только первому. Запрос будет передаваться по
   цепочке до тех пор, пока какой-то обработчик не откажется передавать его
   дальше, либо когда будет достигнут конец цепи.
6. Клиент должен знать о динамической природе цепочки и
   быть готов к таким случаям:
    - Цепочка может состоять из единственного объекта.
    - Запросы могут не достигать конца цепи.
    - Запросы могут достигать конца, оставаясь не обработанными.