# Фасад (Facade)

**Паттерн Facade (Фасад)** — это структурный паттерн проектирования, который
предоставляет упрощённый интерфейс к сложной подсистеме, скрывая её внутреннюю
сложность. Он действует как "единая точка входа", упрощая взаимодействие клиента
с набором классов или модулей, которые вместе решают задачу.

### **Описание паттерна**

**Цель**:

- Обеспечить простой и удобный интерфейс для работы со сложной подсистемой.
- Скрыть детали реализации подсистемы от клиента.
- Уменьшить связанность между клиентским кодом и подсистемой.

**Когда использовать**:

- Когда подсистема состоит из множества классов с сложными взаимодействиями, а
  клиенту нужен простой доступ.
- Когда требуется изолировать клиентский код от деталей реализации подсистемы.
- Когда нужно разделить подсистему на слои или предоставить точку входа для
  внешнего использования.
- Для упрощения работы с библиотеками или API с большим количеством методов.

**Примеры использования**:

- Упрощение работы с API фреймворков (например, JDBC или Hibernate).
- Управление сложными системами, такими как мультимедийные плееры или
  компиляторы.
- Обёртывание устаревших систем для интеграции с новым кодом.

### **Структура паттерна**

<img src="/img/design_pattern/design_patterns/facade_structure.png" alt="factory_method" style="width: 100%; max-width: 550px">

1. **Фасад** предоставляет быстрый доступ к определённой функциональности
   подсистемы. Он «знает», каким классам нужно переадресовать запрос, и какие
   данные для этого нужны.
2. **Дополнительный фасад** можно ввести, чтобы не «захламлять» единственный
   фасад разнородной функциональностью. Он может использоваться как клиентом,
   так и другими фасадами.
3. **Сложная подсистема** состоит из множества разнообразных классов. Для того
   чтобы заставить их что-то делать, нужно знать подробности устройства
   подсистемы, порядок инициализации объектов и так далее.<br>
   Классы подсистемы не знают о существовании фасада и работают друг с другом
   напрямую.
4. **Клиент** использует фасад вместо прямой работы с объектами сложной
   подсистемы.

### **Реализация в Java**

Пример: фасад для упрощения работы с подсистемой домашнего кинотеатра, которая
включает DVD-плеер, проектор и аудиосистему.

```java
// Подсистема: классы с индивидуальной функциональностью
class DVDPlayer {
    public void on() {
        System.out.println("DVD Player is on");
    }

    public void play(String movie) {
        System.out.println("Playing movie: " + movie);
    }

    public void off() {
        System.out.println("DVD Player is off");
    }
}

class Projector {
    public void on() {
        System.out.println("Projector is on");
    }

    public void setInput(String input) {
        System.out.println("Projector input set to " + input);
    }

    public void off() {
        System.out.println("Projector is off");
    }
}

class AudioSystem {
    public void on() {
        System.out.println("Audio System is on");
    }

    public void setVolume(int level) {
        System.out.println("Audio volume set to " + level);
    }

    public void off() {
        System.out.println("Audio System is off");
    }
}

// Фасад
class HomeTheaterFacade {
    private final DVDPlayer dvdPlayer;
    private final Projector projector;
    private final AudioSystem audioSystem;

    public HomeTheaterFacade(DVDPlayer dvdPlayer, Projector projector, AudioSystem audioSystem) {
        this.dvdPlayer = dvdPlayer;
        this.projector = projector;
        this.audioSystem = audioSystem;
    }

    public void watchMovie(String movie) {
        System.out.println("Preparing to watch movie...");
        dvdPlayer.on();
        projector.on();
        projector.setInput("DVD");
        audioSystem.on();
        audioSystem.setVolume(5);
        dvdPlayer.play(movie);
        System.out.println("Movie started!");
    }

    public void endMovie() {
        System.out.println("Shutting down home theater...");
        dvdPlayer.off();
        projector.off();
        audioSystem.off();
        System.out.println("Home theater shut down.");
    }
}

// Клиентский код
public class FacadeExample {
    public static void main(String[] args) {
        DVDPlayer dvdPlayer = new DVDPlayer();
        Projector projector = new Projector();
        AudioSystem audioSystem = new AudioSystem();

        HomeTheaterFacade homeTheater = new HomeTheaterFacade(dvdPlayer, projector, audioSystem);

        homeTheater.watchMovie("Inception");
        // Вывод:
        // Preparing to watch movie...
        // DVD Player is on
        // Projector is on
        // Projector input set to DVD
        // Audio System is on
        // Audio volume set to 5
        // Playing movie: Inception
        // Movie started!

        homeTheater.endMovie();
        // Вывод:
        // Shutting down home theater...
        // DVD Player is off
        // Projector is off
        // Audio System is off
        // Home theater shut down.
    }
}
```

### **Как это работает**

1. Клиент создаёт экземпляры классов подсистемы (`DVDPlayer`, `Projector`,
   `AudioSystem`) и передаёт их в фасад (`HomeTheaterFacade`).
2. Фасад предоставляет высокоуровневые методы (`watchMovie`, `endMovie`),
   которые координируют работу подсистемы.
3. Клиент вызывает методы фасада, не взаимодействуя напрямую с подсистемой, что
   упрощает код и скрывает сложность.

### **Реальное использование в Java**

1. **JDBC API**:
   Класс `java.sql.DriverManager` действует как фасад, упрощая работу с
   подключением к базе данных:
   ```java
   import java.sql.Connection;
   import java.sql.DriverManager;

   Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb", "user", "pass");
   ```
   `DriverManager` скрывает детали загрузки драйверов и управления соединениями.

2. **SLF4J/Logging**:
   Логгеры, такие как SLF4J, предоставляют фасад для различных реализаций
   логирования (Log4j, JUL):
   ```java
   import org.slf4j.Logger;
   import org.slf4j.LoggerFactory;

   Logger logger = LoggerFactory.getLogger(MyClass.class);
   logger.info("Hello, world!");
   ```
   SLF4J скрывает выбор конкретной системы логирования.

3. **Spring Framework**:
   В Spring фасады часто используются для упрощения работы с подсистемами.
   Например, `JdbcTemplate` — это фасад для работы с JDBC:
   ```java
   import org.springframework.jdbc.core.JdbcTemplate;

   JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
   jdbcTemplate.update("INSERT INTO users (name) VALUES (?)", "Alice");
   ```
   `JdbcTemplate` скрывает сложности управления соединениями, обработки
   исключений и т.д.

### **Преимущества**

- **Упрощение интерфейса**: Клиент работает с простыми методами вместо сложной
  подсистемы.
- **Снижение связанности**: Клиентский код зависит только от фасада, а не от
  множества классов подсистемы.
- **Инкапсуляция**: Скрывает детали реализации подсистемы.
- **Гибкость**: Легко изменить или заменить подсистему, не затрагивая клиентский
  код.
- **Поддержка слоёв**: Фасад может служить точкой входа для слоя архитектуры.

### **Недостатки**

- **Ограничение функциональности**: Фасад может предоставлять только
  подмножество возможностей подсистемы.
- **Риск превращения в "божественный объект"**: Если фасад обрастает слишком
  многими методами, он может стать сложным для поддержки.
- **Дополнительный слой**: Вводит ещё один класс, что может быть избыточным для
  простых систем.

### **Отличие от других паттернов**

- **Facade vs Adapter**:
    - **Facade** упрощает интерфейс к сложной подсистеме.
    - **Adapter** преобразует интерфейс одного класса в другой, совместимый с
      клиентом.
- **Facade vs Mediator**:
    - **Facade** предоставляет упрощённый доступ к подсистеме, не координируя её
      части.
    - **Mediator** управляет взаимодействием между объектами, уменьшая их прямые
      связи.
- **Facade vs Abstract Factory**:
    - **Facade** скрывает сложность подсистемы, предоставляя высокоуровневые
      операции.
    - **Abstract Factory** создаёт семейства связанных объектов.

### **Проблемы и антипаттерны**

1. **Слишком сложный фасад**: Если фасад включает слишком много методов, он
   становится трудно поддерживаемым.
    - **Решение**: Ограничивайте фасад высокоуровневыми операциями, оставляя
      детали подсистеме.
2. **Нарушение инкапсуляции**: Если фасад предоставляет прямой доступ к объектам
   подсистемы, он теряет свою цель.
    - **Решение**: Возвращайте результаты, а не ссылки на внутренние объекты.
3. **Избыточность для простых систем**: В небольших системах фасад может быть
   ненужным усложнением.
    - **Решение**: Используйте фасад только для систем с реальной сложностью.

### **Современные альтернативы в Java**

- **Spring Service Layer**:
  В Spring сервисы часто действуют как фасады, упрощая доступ к бизнес-логике:
  ```java
  @Service
  class UserService {
      public void registerUser(String name, String email) {
          // Взаимодействие с репозиториями, валидацией и т.д.
      }
  }
  ```
- **API Gateways**:
  В микросервисной архитектуре API-шлюзы (например, Spring Cloud Gateway)
  выступают как фасады, предоставляя единый интерфейс к множеству сервисов.
- **Fluent Interfaces**:
  Для упрощения взаимодействия можно использовать цепочки вызовов вместо фасада:
  ```java
  HomeTheater theater = new HomeTheater()
          .withDVDPlayer()
          .withProjector()
          .startMovie("Inception");
  ```

### **Итог**

Паттерн Facade — это эффективный способ упростить взаимодействие со сложной
подсистемой, скрывая её детали и предоставляя удобный интерфейс. В Java он
широко используется в стандартной библиотеке (JDBC, SLF4J) и фреймворках (
Spring). Фасад снижает связанность, улучшает читаемость кода и облегчает
поддержку, но требует осторожности, чтобы не превратиться в громоздкий объект.
Он особенно полезен в системах с высокой сложностью или при интеграции с
устаревшими API.

