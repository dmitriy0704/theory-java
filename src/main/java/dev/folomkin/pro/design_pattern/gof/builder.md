# Строитель (Builder)

Строитель — это порождающий паттерн проектирования, который позволяет создавать
сложные объекты пошагово. Строитель даёт возможность использовать один и тот же
код строительства для получения разных представлений объектов. Он особенно
полезен, когда объект имеет множество параметров, а их комбинации могут
варьироваться, или когда процесс создания требует нескольких этапов.

### **Описание паттерна**

**Цель**:

- Разделить создание сложного объекта на отдельные шаги, чтобы упростить его
  конструирование.
- Обеспечить гибкость в создании различных конфигураций объекта.
- Избежать конструкторов с большим количеством параметров (телескопический
  конструктор).

**Когда использовать**:

- Когда объект имеет много опциональных параметров или сложную логику
  инициализации.
- Когда нужно создавать разные конфигурации одного типа объекта.
- Когда процесс создания объекта должен быть независим от его структуры.
- Когда требуется обеспечить неизменяемость объекта после создания.

**Примеры использования**:

- Создание сложных объектов, таких как документы, UI-формы или конфигурации.
- Построение HTTP-запросов (например, с помощью `OkHttp` или `HttpClient`).
- Генерация объектов с множеством параметров (например, конфигурация
  автомобиля).

### **Структура паттерна**

<img src="/img/design_pattern/design_patterns/builder_structure.png" alt="factory_method" style="width: 100%; max-width: 550px">

1. Интерфейс строителя объявляет шаги конструирования продуктов, общие для всех
   видов строителей.
2. Конкретные строители реализуют строительные шаги, каждый по-своему.
   Конкретные строители могут производить разнородные объекты, не имеющие общего
   интерфейса.
3. Продукт — создаваемый объект. Продукты, сделанные разными строителями, не
   обязаны иметь общий интерфейс.
4. Директор определяет порядок вызова строительных шагов для производства той
   или иной конфигурации продуктов.
5. Обычно Клиент подаёт в конструктор директора уже готовый объект-строитель, и
   в дальнейшем данный директор использует только его. Но возможен и другой
   вариант, когда клиент передаёт строителя через параметр строительного метода
   директора. В этом случае можно каждый раз применять разных строителей для
   производства различных представлений объектов.

### **Реализация в Java**

Пример: создание объекта `Computer` с различными конфигурациями (процессор,
оперативная память, хранилище).

```java
// Продукт
class Computer {
    private final String cpu;
    private final int ram;
    private final int storage;
    private final boolean hasGraphicsCard;

    private Computer(Builder builder) {
        this.cpu = builder.cpu;
        this.ram = builder.ram;
        this.storage = builder.storage;
        this.hasGraphicsCard = builder.hasGraphicsCard;
    }

    @Override
    public String toString() {
        return "Computer [CPU=" + cpu + ", RAM=" + ram + "GB, Storage=" + storage + "GB, GraphicsCard=" + hasGraphicsCard + "]";
    }

    // Внутренний класс Builder
    public static class Builder {
        private String cpu;
        private int ram;
        private int storage;
        private boolean hasGraphicsCard;

        public Builder setCpu(String cpu) {
            this.cpu = cpu;
            return this;
        }

        public Builder setRam(int ram) {
            this.ram = ram;
            return this;
        }

        public Builder setStorage(int storage) {
            this.storage = storage;
            return this;
        }

        public Builder setGraphicsCard(boolean hasGraphicsCard) {
            this.hasGraphicsCard = hasGraphicsCard;
            return this;
        }

        public Computer build() {
            return new Computer(this);
        }
    }
}

// Клиентский код
public class BuilderExample {
    public static void main(String[] args) {
        // Создание базового компьютера
        Computer basicComputer = new Computer.Builder()
                .setCpu("Intel i3")
                .setRam(8)
                .setStorage(256)
                .build();
        System.out.println(basicComputer);
        // Вывод: Computer [CPU=Intel i3, RAM=8GB, Storage=256GB, GraphicsCard=false]

        // Создание игрового компьютера
        Computer gamingComputer = new Computer.Builder()
                .setCpu("Intel i9")
                .setRam(32)
                .setStorage(1000)
                .setGraphicsCard(true)
                .build();
        System.out.println(gamingComputer);
        // Вывод: Computer [CPU=Intel i9, RAM=32GB, Storage=1000GB, GraphicsCard=true]
    }
}
```

### **Как это работает**

1. Клиент использует `Builder` для пошаговой настройки параметров объекта (
   `setCpu`, `setRam` и т.д.).
2. Каждый метод строителя возвращает `this`, что позволяет использовать цепочку
   вызовов (fluent interface).
3. После настройки всех параметров клиент вызывает `build()`, который создаёт
   финальный объект `Computer`.
4. Продукт (`Computer`) является неизменяемым, так как его поля задаются только
   через конструктор и помечены как `final`.

### **Реальное использование в Java**

1. **StringBuilder**:
   Класс `StringBuilder` реализует паттерн Builder для создания строк:
   ```java
   StringBuilder builder = new StringBuilder()
           .append("Hello")
           .append(" ")
           .append("World");
   String result = builder.toString(); // Hello World
   ```

2. **Java HttpClient**:
   Класс `HttpClient` в Java 11+ использует Builder для настройки HTTP-клиента:
   ```java
   import java.net.http.HttpClient;

   HttpClient client = HttpClient.newBuilder()
           .version(HttpClient.Version.HTTP_2)
           .connectTimeout(Duration.ofSeconds(10))
           .build();
   ```

3. **Lombok**:
   Аннотация `@Builder` в Lombok автоматически генерирует реализацию паттерна
   Builder:
   ```java
   import lombok.Builder;

   @Builder
   class Computer {
       private String cpu;
       private int ram;
       private int storage;
   }

   Computer computer = Computer.builder()
           .cpu("Intel i5")
           .ram(16)
           .storage(512)
           .build();
   ```

4. **Spring Boot**:
   Конфигурация объектов, таких как `RestTemplate` или `WebClient`, часто
   использует Builder:
   ```java
   import org.springframework.web.client.RestTemplate;

   RestTemplate restTemplate = new RestTemplateBuilder()
           .setConnectTimeout(Duration.ofSeconds(5))
           .build();
   ```

### **Преимущества**

- **Гибкость**: Позволяет создавать разные конфигурации объекта.
- **Читаемость**: Код с цепочкой вызовов (`fluent interface`) легко читается.
- **Неизменяемость**: Легко создать неизменяемый объект, задавая поля через
  конструктор.
- **Избежание телескопического конструктора**: Устраняет проблему с множеством
  перегруженных конструкторов.
- **Изоляция логики**: Процесс создания отделён от структуры объекта.

### **Недостатки**

- **Увеличение кода**: Требуется отдельный класс `Builder`, что увеличивает
  количество кода.
- **Сложность для простых объектов**: Для объектов с 1-2 полями Builder может
  быть избыточным.
- **Дополнительные объекты**: Создание строителя требует выделения памяти.

### **Отличие от других паттернов**

- **Builder vs Factory Method**:
    - **Builder** фокусируется на пошаговом создании сложного объекта с
      множеством параметров.
    - **Factory Method** создаёт объект одним вызовом, определяя его тип через
      подклассы.
- **Builder vs Abstract Factory**:
    - **Builder** создаёт один объект с гибкой конфигурацией.
    - **Abstract Factory** создаёт семейства связанных объектов.
- **Builder vs Prototype**:
    - **Builder** конструирует объект с нуля, задавая параметры.
    - **Prototype** создаёт объект путём копирования существующего.

### **Проблемы и антипаттерны**

1. **Неполная инициализация**: Если клиент забудет задать обязательные
   параметры, объект может быть создан в некорректном состоянии.
    - **Решение**: Добавьте проверки в метод `build()` или сделайте обязательные
      параметры частью конструктора строителя:
      ```java
      public static class Builder {
          private final String cpu; // Обязательное поле
          private int ram;
          private int storage;
 
          public Builder(String cpu) {
              this.cpu = cpu;
          }
 
          public Computer build() {
              if (ram <= 0 || storage <= 0) {
                  throw new IllegalStateException("RAM and Storage must be set");
              }
              return new Computer(this);
          }
      }
      ```
2. **Сложность тестирования**: Тестирование объектов с множеством параметров
   может быть громоздким.
    - **Решение**: Используйте библиотеки, такие как `Test Data Builder`, или
      Lombok для упрощения.
3. **Избыточность для простых объектов**: Применение Builder к объектам с 1-2
   полями увеличивает сложность без пользы.
    - **Решение**: Используйте Builder только для сложных объектов.

### **Современные альтернативы в Java**

- **Lombok @Builder**:
  Автоматически генерирует реализацию паттерна, уменьшая шаблонный код (см.
  пример выше).
- **Java Records**:
  Для простых неизменяемых объектов можно использовать `record`, хотя они не
  поддерживают пошаговое создание:
  ```java
  record Computer(String cpu, int ram, int storage) {}
  ```
  Для сложных случаев record можно комбинировать с Builder.
- **Functional Builders**:
  В Java 8+ можно использовать функциональные интерфейсы для создания объектов:
  ```java
  Function<Consumer<Computer.Builder>, Computer> computerFactory = config -> {
      Computer.Builder builder = new Computer.Builder();
      config.accept(builder);
      return builder.build();
  };

  Computer computer = computerFactory.apply(b -> b.setCpu("Intel i7").setRam(16));
  ```

### **Итог**

Паттерн Builder — это мощный инструмент для создания сложных объектов с
множеством параметров, обеспечивающий читаемость, гибкость и неизменяемость. В
Java он широко используется в стандартной библиотеке (`StringBuilder`,
`HttpClient`) и фреймворках (Spring, Lombok). Он особенно полезен для избежания
телескопических конструкторов и упрощения работы с конфигурациями. Однако для
простых объектов Builder может быть избыточным, и в таких случаях лучше
использовать конструкторы или `record`.

