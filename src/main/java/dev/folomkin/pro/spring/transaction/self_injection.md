# Self-injection

**Self-Injection** в Spring — это практика, когда компонент (например,
Spring-бин) внедряет (инжектит) сам себя через контейнер Spring.
Вместо того чтобы просто вызывать методы внутри класса напрямую, класс получает
ссылку на самого себя из Spring-контекста и вызывает методы через эту ссылку.

## Зачем это нужно?

Основная причина — **корректная работа аспектов (AOP), прокси и транзакций**.

Например, если у вас есть метод с аннотацией `@Transactional`, и вы вызываете
этот метод из другого метода того же класса напрямую (
`this.someTransactionalMethod()`), то Spring **не применит прокси и не создаст
транзакцию**, потому что вызов идёт внутри одного объекта.

Если же вызвать метод через прокси (через self injection), то аспекты сработают
корректно.

## Пример self injection

```java

@Component
public class MyService {

    @Autowired
    private MyService self;  // self injection

    public void methodA() {
        // вызов метода с транзакцией через прокси
        self.methodB();
    }

    @Transactional
    public void methodB() {
        // код, который должен выполняться в транзакции
    }
}
```

Здесь вызов `self.methodB()` пройдет через прокси Spring, и транзакция будет
создана. Если бы вызвали `this.methodB()`, то аннотация `@Transactional` не
сработала бы.

## Как работает?

Spring создает прокси-объект для бина и внедряет его в поле `self`. Таким
образом, при вызове методов через `self` вызывается проксированный объект с
применением аспектов.

#### Важные моменты

- Self injection может привести к циклическим зависимостям, если не аккуратно
  использовать.
- Альтернативы: использовать `ApplicationContext` для получения бина или
  рефакторить код так, чтобы методы с аспектами вызывались извне.
- В Spring 5+ можно использовать аннотацию `@Lazy` при инжекции самого себя,
  чтобы избежать проблем с циклическими зависимостями:

```java

@Autowired
@Lazy
private MyService self;
```

#### Кратко

**Self Injection** — это когда бин внедряет сам себя из контекста Spring для
того, чтобы внутренние вызовы методов проходили через прокси и корректно
работали такие механизмы как транзакции и AOP.

====


**Self Injection** в **Spring Framework** — это концепция, при которой **объект
сам себе инжектит зависимости** через механизмы Spring DI (Dependency
Injection). Обычно это может происходить с помощью **аннотаций**, таких как
`@Autowired`, когда объект **автоматически подставляется** в сам себя как
зависимость.

### 📌 Что такое инъекция зависимостей в Spring?

**Инъекция зависимостей (DI)** — это принцип, согласно которому объекты не
создают свои зависимости самостоятельно, а получают их извне (через
конструкторы, методы или поля). Spring поддерживает этот механизм через
контейнер, который управляет созданием объектов и их зависимостями.

### 🔄 Self Injection: когда объект инжектит сам себя

**Self Injection** возникает, когда компонент (bean) Spring инжектит **сам себя
** в своём собственном контексте. Это может быть полезно в некоторых ситуациях,
например, когда нужно обеспечить доступ к текущему экземпляру класса в самом
классе, например, для внедрения через какие-то специфические методы или для
манипуляции внутри компонента.

### 🧑‍💻 Пример Self Injection в Spring

Предположим, у нас есть компонент, который сам себе инжектит зависимость с
помощью `@Autowired`.

1. **Компонент с Self Injection**:

```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SelfInjectingComponent {

    private SelfInjectingComponent self;

    // Self Injection через setter метод
    @Autowired
    public void setSelf(SelfInjectingComponent self) {
        this.self = self;
    }

    public void printSelf() {
        System.out.println(self);
    }
}
```

- В данном примере класс `SelfInjectingComponent` получает сам себя через *
  *setter** метод, помеченный аннотацией `@Autowired`. Это и есть **Self
  Injection**.

2. **Конфигурация Spring**:

```java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "com.example")
public class AppConfig {
    // Можешь добавить дополнительные бины
}
```

3. **Запуск Spring приложения**:

```java
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        SelfInjectingComponent component = context.getBean(SelfInjectingComponent.class);
        component.printSelf(); // Выведет ссылку на сам объект
    }
}
```

### 🔍 В чем смысл Self Injection?

**Self Injection** может быть полезен в специфичных случаях, например:

- **Для кэширования**: когда компонент сам себе инжектирует зависимости для
  работы с кэшом.
- **Для рекурсии**: когда объект должен быть способен работать сам с собой в
  рекурсивных структурах.
- **Циклические зависимости**: хотя Spring не рекомендует циклические
  зависимости, бывают случаи, когда такой подход используется для их разрешения.

Однако стоит помнить, что использование **Self Injection** — это довольно редкая
практика, которая чаще всего может сигнализировать о том, что архитектура
нуждается в **рефакторинге**. В большинстве случаев, **инжекция зависимостей**
должна быть направлена на **внедрение внешних зависимостей**, а не инъекцию
самого себя.

---

### 🧩 Проблемы с Self Injection

1. **Циклические зависимости**:

- Если в вашем приложении несколько компонентов инжектят друг друга (циклическая
  зависимость), это может привести к непредсказуемому поведению. Spring сможет
  решить проблему циклических зависимостей только в случае **конструкторной
  инъекции** и правильного порядка инстанцирования.
- Однако, **self-injection** не является типичной причиной циклических
  зависимостей, хотя такой подход может быть частью общей проблемы с
  архитектурой.

2. **Архитектурные проблемы**:

- Частое использование Self Injection может указывать на проблемы с дизайном
  класса или компонента. Лучше подумать, не нужно ли **пересмотреть архитектуру
  **, если такой механизм часто используется.

---

### 🧠 Когда стоит использовать Self Injection?

- **Инициализация через бин**: если требуется инжектить зависимости **только
  через Spring** (например, для корректной работы в контексте Spring).
- **Тестирование**: в некоторых случаях Self Injection используется в **тестовых
  приложениях**, когда важно, чтобы зависимости инжектировались, а сам объект
  должен быть доступен для проверки в других местах.

---

## 💡 Альтернативы Self Injection

Часто можно избежать Self Injection, используя другие механизмы инъекции
зависимостей:

1. **Конструкторная инъекция**:
   Лучше всегда использовать **конструкторную инъекцию** для явного указания
   зависимостей, что облегчает тестирование и поддержание кода. Например:

```java

@Component
public class SomeComponent {

    private final AnotherComponent anotherComponent;

    @Autowired
    public SomeComponent(AnotherComponent anotherComponent) {
        this.anotherComponent = anotherComponent;
    }
}
```

2. **Делегирование через другие бины**:
   Иногда лучше делегировать работу другим компонентам, а не инжектить сам себя.
   Это помогает избежать чрезмерной зависимости от самого компонента.

---

### 🤔 Резюме:

- **Self Injection** — это редкая практика, при которой компонент инжектирует
  сам себя.
- Этот подход может быть полезен в специфичных случаях, но часто он указывает на
  проблемы с архитектурой.
- Обычно **self-injection** используется через `@Autowired` на полях, методах
  или конструкторах, но часто такие практики можно избежать с помощью других
  методов DI.
- Лучше всего использовать **конструкторную инъекцию** для ясности зависимостей
  и лёгкости тестирования.

=====

### self-injection

**Self-injection** (или самопроецирование) в Spring — это паттерн, при котором
бин внедряет самого себя через `@Autowired` (или другой механизм внедрения
зависимостей) для вызова собственных методов через прокси-объект. Этот подход
используется, чтобы обойти ограничения Spring, связанные с применением AOP (
аспектно-ориентированного программирования), например, при работе с аннотацией
`@Transactional`, когда требуется, чтобы метод вызывался через прокси для
активации транзакции.

### Зачем нужен self-injection?

В Spring аннотации, такие как `@Transactional`, `@Async`, или кастомные аспекты,
работают через **прокси-объекты**, которые создаются Spring для управления
поведением (например, транзакциями). Однако, если метод, помеченный такой
аннотацией, вызывается **внутри того же класса** (через `this.method()`), вызов
**не проходит через прокси**, и аннотация (например, `@Transactional`) не
срабатывает. Self-injection решает эту проблему, позволяя вызывать метод через
проксированный бин.

### Как работает self-injection?

1. Бин внедряет самого себя как зависимость с помощью `@Autowired`, `@Resource`
   или другого механизма.
2. Вместо вызова метода через `this`, метод вызывается через внедрённый
   прокси-объект.
3. Прокси перехватывает вызов и применяет логику, связанную с аннотациями (
   например, создаёт транзакцию).

### Пример self-injection с `@Transactional`

```java

@Service
public class UserService {

    @Autowired
    private UserService self; // Self-injection: внедряем свой собственный бин
    @Autowired
    private UserRepository userRepository;

    public void createUser(String username) {
        self.saveUser(username); // Вызов через прокси, а не через this
    }

    @Transactional
    public void saveUser(String username) {
        userRepository.save(new User(username)); // Транзакция будет работать
    }
}
```

- В методе `createUser` вызов `self.saveUser(username)` идёт через прокси-объект
  `UserService`, что позволяет Spring применить логику `@Transactional`.
- Без self-injection вызов `this.saveUser(username)` обошёл бы прокси, и
  транзакция не была бы создана.

### Когда использовать self-injection?

- **Для активации аннотаций AOP**: Когда нужно, чтобы методы, помеченные
  `@Transactional`, `@Async`, или кастомными аспектами, вызывались через прокси
  внутри того же класса.
- **Для избежания выделения логики в отдельный бин**: Self-injection позволяет
  сохранить связанную логику в одном классе, вместо создания отдельного сервиса.
- **Для сложной транзакционной логики**: Например, когда один метод должен
  вызывать другой с определённым поведением транзакции (например,
  `Propagation.REQUIRES_NEW`).

### Альтернативы self-injection

1. **Выделение метода в отдельный бин**:
   ```java
   @Service
   public class UserService {
       @Autowired
       private TransactionalService transactionalService;

       public void createUser(String username) {
           transactionalService.saveUser(username); // Вызов через другой бин
       }
   }

   @Service
   public class TransactionalService {
       @Autowired
       private UserRepository userRepository;

       @Transactional
       public void saveUser(String username) {
           userRepository.save(new User(username));
       }
   }
   ```

2. **Использование AspectJ**:
    - Настройка Spring для использования AspectJ вместо прокси позволяет
      применять аннотации даже при внутренних вызовах. Это требует
      дополнительных зависимостей и конфигурации (например, `spring-aspects` и
      AspectJ Weaver).

3. **Программное управление транзакциями**:
    - Использование `TransactionTemplate` вместо `@Transactional` позволяет явно
      управлять транзакциями, избегая проблем с прокси.
   ```java
   @Service
   public class UserService {
       @Autowired
       private UserRepository userRepository;
       @Autowired
       private TransactionTemplate transactionTemplate;

       public void createUser(String username) {
           transactionTemplate.execute(status -> {
               userRepository.save(new User(username));
               return null;
           });
       }
   }
   ```

### Проблемы и ограничения self-injection

- **Циклические зависимости**:
    - Spring может выдать ошибку циклической зависимости, если бин зависит от
      самого себя. Это можно обойти, используя `@Lazy`:
      ```java
      @Autowired
      @Lazy
      private UserService self;
      ```
    - Или использовать `@Resource` вместо `@Autowired`, так как `@Resource`
      работает с уже созданным бином.

- **Усложнение кода**:
    - Self-injection может сделать код менее читаемым, так как добавляет
      зависимость на самого себя.

- **Антипаттерн**:
    - Некоторые разработчики считают self-injection антипаттерном, так как он
      может указывать на плохую архитектуру. В идеале, логика должна быть
      разделена на разные бины, чтобы избежать необходимости в self-injection.

### Конфигурация

Для работы self-injection достаточно стандартной конфигурации Spring с
включённым управлением транзакций:

```java

@Configuration
@EnableTransactionManagement
public class AppConfig {
    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }
}
```

### Итог

Self-injection — это техника в Spring, которая позволяет вызывать методы,
помеченные аннотациями вроде `@Transactional`, через прокси-объект внутри того
же класса. Она решает проблему обхода прокси при внутренних вызовах, но требует
осторожности из-за возможных циклических зависимостей и усложнения кода.
Альтернативы, такие как выделение логики в отдельный бин или использование
`TransactionTemplate`, могут быть предпочтительнее в зависимости от архитектуры
приложения.

