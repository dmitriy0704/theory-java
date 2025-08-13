# Шаблонный метод(Template Method)

**Паттерн Template Method (Шаблонный метод)** — это поведенческий паттерн
проектирования, который определяет общий алгоритм в суперклассе, позволяя
подклассам переопределять отдельные шаги этого алгоритма без изменения его общей
структуры. Он обеспечивает "скелет" алгоритма, где неизменяемая часть остаётся в
суперклассе, а изменяемые части делегируются подклассам.

### **Описание паттерна**

**Цель**:

- Определить общий алгоритм, позволяя подклассам настраивать отдельные его шаги.
- Обеспечить повторное использование кода для общей логики.
- Гарантировать, что структура алгоритма остаётся неизменной.

**Когда использовать**:

- Когда несколько классов имеют схожий алгоритм, но с различиями в отдельных
  шагах.
- Когда нужно избежать дублирования кода, вынося общую логику в суперкласс.
- Когда требуется контролировать порядок выполнения шагов алгоритма.
- Когда подклассы должны переопределять только определённые части алгоритма.

**Примеры использования**:

- Обработка данных в разных форматах (например, чтение CSV, JSON).
- Шаблоны обработки запросов в веб-приложениях (например, фильтры в Servlet
  API).
- Жизненный цикл классов в фреймворках (например, методы `onCreate` в Android).

### **Структура паттерна**

<img src="/img/design_pattern/design_patterns/template_method_structure.png" alt="factory_method" style="width: 100%; max-width: 550px">

1. Абстрактный класс определяет шаги алгоритма и содержит шаблонный метод,
   состоящий из вызовов этих шагов. Шаги могут быть как абстрактными, так и
   содержать реализацию по умолчанию.
2. Конкретный класс переопределяет некоторые (или все)шаги алгоритма. Конкретные
   классы не переопределяют сам шаблонный метод.

### **Реализация в Java**

Пример: шаблонный метод для приготовления напитков (кофе и чая), где процесс
приготовления схож, но отдельные шаги различаются.

```java
// Абстрактный класс
abstract class BeverageMaker {
    // Шаблонный метод
    public final void prepareBeverage() {
        boilWater();
        brew();
        pourInCup();
        if (customerWantsCondiments()) {
            addCondiments();
        }
    }

    // Общие методы
    private void boilWater() {
        System.out.println("Boiling water");
    }

    private void pourInCup() {
        System.out.println("Pouring into cup");
    }

    // Абстрактные методы, которые должны реализовать подклассы
    protected abstract void brew();

    protected abstract void addCondiments();

    // Хук (hook) для необязательной настройки
    protected boolean customerWantsCondiments() {
        return true; // По умолчанию добавляем добавки
    }
}

// Конкретный класс: Кофе
class CoffeeMaker extends BeverageMaker {
    @Override
    protected void brew() {
        System.out.println("Brewing coffee grounds");
    }

    @Override
    protected void addCondiments() {
        System.out.println("Adding sugar and milk");
    }
}

// Конкретный класс: Чай
class TeaMaker extends BeverageMaker {
    @Override
    protected void brew() {
        System.out.println("Steeping tea leaves");
    }

    @Override
    protected void addCondiments() {
        System.out.println("Adding lemon");
    }

    @Override
    protected boolean customerWantsCondiments() {
        return false; // По умолчанию без добавок
    }
}

// Клиентский код
public class TemplateMethodExample {
    public static void main(String[] args) {
        System.out.println("Making Coffee:");
        BeverageMaker coffee = new CoffeeMaker();
        coffee.prepareBeverage();
        // Вывод:
        // Boiling water
        // Brewing coffee grounds
        // Pouring into cup
        // Adding sugar and milk

        System.out.println("\nMaking Tea:");
        BeverageMaker tea = new TeaMaker();
        tea.prepareBeverage();
        // Вывод:
        // Boiling water
        // Steeping tea leaves
        // Pouring into cup
    }
}
```

### **Как это работает**

1. Абстрактный класс `BeverageMaker` определяет шаблонный метод
   `prepareBeverage()`, который фиксирует порядок шагов: кипячение воды,
   заваривание, наливание в чашку и добавление добавок (если нужно).
2. Конкретные методы (`boilWater`, `pourInCup`) содержат общую логику,
   одинаковую для всех напитков.
3. Абстрактные методы (`brew`, `addCondiments`) должны быть реализованы
   подклассами (`CoffeeMaker`, `TeaMaker`).
4. Хук `customerWantsCondiments` позволяет подклассам настраивать поведение (
   например, отключить добавки для чая).
5. Клиент вызывает `prepareBeverage()` на объекте подкласса, и алгоритм
   выполняется с учётом специфичных для подкласса шагов.

### **Реальное использование в Java**

1. **Servlet API**:
   В Java Servlet API метод `service()` в `HttpServlet` действует как шаблонный
   метод:
   ```java
   import javax.servlet.http.HttpServlet;
   import javax.servlet.http.HttpServletRequest;
   import javax.servlet.http.HttpServletResponse;

   public class MyServlet extends HttpServlet {
       @Override
       protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
           // Обработка GET-запроса
       }

       @Override
       protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
           // Обработка POST-запроса
       }
   }
   ```
   Метод `service()` в `HttpServlet` определяет общий алгоритм обработки
   HTTP-запросов, вызывая `doGet`, `doPost` и другие методы, которые
   переопределяются в подклассах.

2. **Abstract Classes в Java Collections**:
   Классы, такие как `AbstractList`, предоставляют шаблонные методы:
   ```java
   import java.util.AbstractList;

   public class MyList extends AbstractList<String> {
       private final String[] data = {"A", "B", "C"};

       @Override
       public String get(int index) {
           return data[index];
       }

       @Override
       public int size() {
           return data.length;
       }
   }
   ```
   `AbstractList` реализует общую логику списка, а подклассы переопределяют
   методы, такие как `get` и `size`.

3. **Spring Framework**:
   Spring использует Template Method в классах, таких как `JdbcTemplate`:
   ```java
   import org.springframework.jdbc.core.JdbcTemplate;

   JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
   jdbcTemplate.execute("SELECT * FROM users");
   ```
   `JdbcTemplate` определяет общий алгоритм выполнения SQL-запросов (
   подключение, выполнение, обработка ошибок), а конкретные запросы задаются
   клиентом.

### **Преимущества**

- **Повторное использование кода**: Общая логика выносится в суперкласс,
  уменьшая дублирование.
- **Контроль структуры**: Шаблонный метод гарантирует неизменность общей
  структуры алгоритма.
- **Гибкость**: Подклассы могут настраивать отдельные шаги, не затрагивая общий
  алгоритм.
- **Соответствие принципу Open/Closed**: Алгоритм открыт для расширения (новые
  подклассы), но закрыт для модификации.

### **Недостатки**

- **Ограниченность наследованием**: Подклассы должны наследовать абстрактный
  класс, что может быть неудобно в сравнении с композицией.
- **Жёсткая структура**: Изменение порядка шагов требует модификации шаблонного
  метода.
- **Сложность поддержки**: При большом числе подклассов может быть трудно
  отслеживать все переопределения.
- **Ограниченная гибкость**: Подклассы могут переопределять только те шаги,
  которые предусмотрены в суперклассе.

### **Отличие от других паттернов**

- **Template Method vs Strategy**:
    - **Template Method** использует наследование для настройки шагов алгоритма.
    - **Strategy** использует композицию, передавая алгоритм как объект.
- **Template Method vs Factory Method**:
    - **Template Method** определяет алгоритм с переопределяемыми шагами.
    - **Factory Method** фокусируется на создании объектов через абстрактный
      метод.
- **Template Method vs Builder**:
    - **Template Method** фиксирует порядок шагов для выполнения алгоритма.
    - **Builder** фокусируется на пошаговом создании объекта.

### **Проблемы и антипаттерны**

1. **Слишком жёсткий шаблон**: Если алгоритм слишком строго фиксирует шаги,
   подклассы могут быть ограничены в гибкости.
    - **Решение**: Используйте хуки или делайте больше методов защищёнными (
      `protected`), чтобы подклассы могли их переопределять.
2. **Злоупотребление наследованием**: Наследование может привести к сильной
   связанности и усложнить тестирование.
    - **Решение**: Рассмотрите Strategy или композицию для большей гибкости.
3. **Сложность тестирования**: Тестирование подклассов может быть затруднено
   из-за зависимости от суперкласса.
    - **Решение**: Используйте мок-объекты или извлекайте логику в отдельные
      классы.

### **Современные альтернативы в Java**

- **Strategy с композицией**:
  Вместо наследования можно использовать Strategy для большей гибкости:
  ```java
  interface BrewStrategy {
      void brew();
  }

  class CoffeeBrew implements BrewStrategy {
      public void brew() { System.out.println("Brewing coffee grounds"); }
  }

  class BeverageMaker {
      private final BrewStrategy brewStrategy;

      public BeverageMaker(BrewStrategy brewStrategy) {
          this.brewStrategy = brewStrategy;
      }

      public void prepareBeverage() {
          System.out.println("Boiling water");
          brewStrategy.brew();
          System.out.println("Pouring into cup");
      }
  }
  ```
- **Лямбда-выражения**:
  В Java 8+ шаги алгоритма можно передавать как лямбда-выражения:
  ```java
  class BeverageMaker {
      public void prepareBeverage(Runnable brew, Runnable addCondiments) {
          System.out.println("Boiling water");
          brew.run();
          System.out.println("Pouring into cup");
          addCondiments.run();
      }
  }

  BeverageMaker maker = new BeverageMaker();
  maker.prepareBeverage(
          () -> System.out.println("Brewing coffee grounds"),
          () -> System.out.println("Adding sugar and milk")
  );
  ```
- **Spring Template Classes**:
  Spring использует подход, похожий на Template Method, в классах, таких как
  `JdbcTemplate` или `RestTemplate`, где общая логика фиксирована, а клиент
  предоставляет детали.

### **Итог**

Паттерн Template Method — это эффективный способ определения общего алгоритма с
возможностью настройки его шагов в подклассах. Он широко используется в Java,
особенно в стандартной библиотеке (Servlet API, Collections) и фреймворках (
Spring). Паттерн обеспечивает повторное использование кода и контроль структуры,
но его зависимость от наследования может быть ограничением. В современных
приложениях Template Method иногда заменяется Strategy или лямбда-выражениями
для большей гибкости.

----

## Итератор(Iterator)

Итератор — это поведенческий паттерн проектирования, который даёт возможность
последовательно обходить элементы составных объектов, не раскрывая их
внутреннего представления. Он позволяет клиентам обходить элементы коллекции, не
зная, как они хранятся (например, в массиве, списке или хэш-таблице), и
обеспечивает единый интерфейс для работы с разными типами коллекций.

### **Описание паттерна**

**Цель**:

- Обеспечить стандартизированный способ обхода элементов коллекции.
- Скрыть детали реализации коллекции от клиента.
- Поддерживать различные способы обхода (например, прямой, обратный,
  фильтрованный).

**Когда использовать**:

- Когда нужно предоставить доступ к элементам сложной структуры данных без
  раскрытия её внутренней организации.
- Когда требуется поддерживать несколько способов обхода одной коллекции.
- Когда нужно унифицировать работу с разными типами коллекций (списки, массивы,
  деревья).
- Когда коллекция должна быть обойдена без изменения её структуры.

**Примеры использования**:

- Обход элементов коллекций в Java (`List`, `Set`, `Map`).
- Итерация по результатам базы данных (например, `ResultSet` в JDBC).
- Перебор узлов в структурах данных, таких как деревья или графы.

### **Структура паттерна**

<img src="/img/design_pattern/design_patterns/iterator_structure.png" alt="factory_method" style="width: 100%; max-width: 550px">

1. Итератор описывает интерфейс для доступа и обхода элементов коллекции.
2. Конкретный итератор реализует алгоритм обхода какой-то конкретной коллекции.
   Объект итератора должен сам отслеживать текущую позицию при обходе коллекции,
   чтобы отдельные итераторы могли обходить одну и ту же коллекцию независимо.
3. Коллекция описывает интерфейс получения итератора из коллекции. Как мы уже
   говорили, коллекции не всегда являются списком. Это может быть и база данных,
   и удалённое API, и даже дерево Компоновщика. Поэтому сама коллекция может
   создавать итераторы, так как она знает, какие именно итераторы способны с ней
   работать.
4. Конкретная коллекция возвращает новый экземпляр определённого конкретного
   итератора, связав его с текущим объектом коллекции. Обратите внимание, что
   сигнатура метода возвращает интерфейс итератора. Это позволяет клиенту не
   зависеть от конкретных классов итераторов.
5. Клиент работает со всеми объектами через интерфейсы коллекции и итератора.
   Так клиентский код не зависит от конкретных классов, что позволяет применять
   различные итераторы, не изменяя существующий код программы.<br> В общем
   случае клиенты не создают объекты итераторов, а получают их из коллекций. Тем
   не менее, если клиенту требуется специальный итератор, он всегда может
   создать его самостоятельно.

### **Реализация в Java**

Пример: итератор для обхода пользовательской коллекции книг.

```java
import java.util.ArrayList;
import java.util.List;

// Интерфейс итератора
interface Iterator<T> {
    boolean hasNext();

    T next();
}

// Интерфейс агрегата
interface BookCollection {
    Iterator<Book> createIterator();
}

// Класс продукта
class Book {
    private final String title;

    public Book(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}

// Конкретный агрегат
class Library implements BookCollection {
    private final List<Book> books;

    public Library() {
        this.books = new ArrayList<>();
    }

    public void addBook(Book book) {
        books.add(book);
    }

    @Override
    public Iterator<Book> createIterator() {
        return new LibraryIterator(books);
    }
}

// Конкретный итератор
class LibraryIterator implements Iterator<Book> {
    private final List<Book> books;
    private int index;

    public LibraryIterator(List<Book> books) {
        this.books = books;
        this.index = 0;
    }

    @Override
    public boolean hasNext() {
        return index < books.size();
    }

    @Override
    public Book next() {
        if (!hasNext()) {
            throw new IndexOutOfBoundsException("No more books");
        }
        return books.get(index++);
    }
}

// Клиентский код
public class IteratorExample {
    public static void main(String[] args) {
        Library library = new Library();
        library.addBook(new Book("The Hobbit"));
        library.addBook(new Book("1984"));
        library.addBook(new Book("Pride and Prejudice"));

        Iterator<Book> iterator = library.createIterator();
        while (iterator.hasNext()) {
            Book book = iterator.next();
            System.out.println("Book: " + book.getTitle());
        }
        // Вывод:
        // Book: The Hobbit
        // Book: 1984
        // Book: Pride and Prejudice
    }
}
```

### **Как это работает**

1. Клиент запрашивает итератор у коллекции (`Library`) через метод
   `createIterator()`.
2. Итератор (`LibraryIterator`) отслеживает текущую позицию в коллекции (с
   помощью `index`) и предоставляет методы `hasNext()` и `next()` для доступа к
   элементам.
3. Клиент использует итератор для последовательного обхода элементов, не зная,
   как они хранятся в коллекции (например, в `ArrayList`).
4. Коллекция остаётся неизменной, а итератор управляет процессом обхода.

### **Реальное использование в Java**

Java имеет встроенную поддержку паттерна Iterator через интерфейсы
`java.util.Iterator` и `java.util.Iterable`, которые используются повсеместно в
стандартной библиотеке.

1. **Collections Framework**:
   Все коллекции, реализующие `Iterable` (например, `List`, `Set`, `Map`),
   предоставляют итератор:
   ```java
   import java.util.ArrayList;
   import java.util.Iterator;

   ArrayList<String> list = new ArrayList<>();
   list.add("A");
   list.add("B");
   list.add("C");

   Iterator<String> iterator = list.iterator();
   while (iterator.hasNext()) {
       System.out.println(iterator.next());
   }
   ```
   Или с использованием цикла `for-each`, который автоматически использует
   итератор:
   ```java
   for (String item : list) {
       System.out.println(item);
   }
   ```

2. **JDBC ResultSet**:
   `ResultSet` в JDBC реализует итератор для обхода строк результата запроса:
   ```java
   import java.sql.ResultSet;
   import java.sql.Statement;
   import java.sql.Connection;

   try (Connection conn = getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM users")) {
       while (rs.next()) {
           System.out.println(rs.getString("name"));
       }
   }
   ```

3. **Stream API**:
   В Java 8+ итераторы частично заменены Stream API, но они всё ещё используются
   под капотом:
   ```java
   import java.util.List;

   List<String> list = List.of("A", "B", "C");
   list.stream().forEach(System.out::println);
   ```

### **Преимущества**

- **Инкапсуляция**: Скрывает внутреннюю структуру коллекции от клиента.
- **Универсальность**: Предоставляет единый интерфейс для обхода разных типов
  коллекций.
- **Гибкость**: Поддерживает различные способы обхода (например, прямой,
  обратный, фильтрованный).
- **Безопасность**: Позволяет обходить коллекцию без её модификации.
- **Поддержка for-each**: В Java коллекции, реализующие `Iterable`,
  автоматически работают с циклом `for-each`.

### **Недостатки**

- **Ограниченная функциональность**: Классический итератор поддерживает только
  последовательный доступ (нет возможности вернуться назад или пропустить
  элементы).
- **Сложность для сложных структур**: Реализация итератора для деревьев или
  графов может быть нетривиальной.
- **Потенциальные проблемы с concorrency**: Итератор может выбросить
  `ConcurrentModificationException`, если коллекция изменяется во время обхода.
- **Менее мощный, чем Stream API**: В современных Java-приложениях Stream API
  часто предпочтительнее для сложных операций.

### **Отличие от других паттернов**

- **Iterator vs Composite**:
    - **Iterator** предоставляет способ обхода элементов коллекции.
    - **Composite** управляет древовидной структурой объектов, позволяя работать
      с группами как с единым объектом.
- **Iterator vs Visitor**:
    - **Iterator** фокусируется на последовательном доступе к элементам.
    - **Visitor** позволяет выполнять операции над элементами, передавая логику
      в отдельный объект.
- **Iterator vs Strategy**:
    - **Iterator** определяет способ обхода коллекции.
    - **Strategy** определяет взаимозаменяемые алгоритмы для выполнения задачи.

### **Проблемы и антипаттерны**

1. **ConcurrentModificationException**: Если коллекция изменяется во время
   итерации, итератор может выбросить исключение.
    - **Решение**: Используйте `CopyOnWriteArrayList` или синхронизируйте доступ
      к коллекции.
    - **Альтернатива**: Используйте Stream API, которая работает с неизменяемыми
      данными.
2. **Сложность реализации для нестандартных структур**: Итераторы для деревьев
   или графов требуют сложной логики.
    - **Решение**: Используйте рекурсию или библиотеки, такие как Guava, для
      упрощения.
3. **Злоупотребление итератором**: Использование итератора для простых
   коллекций, где достаточно `for-each` или Stream.
    - **Решение**: Применяйте итератор только для сложных или нестандартных
      сценариев.

### **Современные альтернативы в Java**

- **Stream API**:
  В Java 8+ Stream API предоставляет более мощный и декларативный способ
  обработки коллекций:
  ```java
  List<String> list = List.of("A", "B", "C");
  list.stream()
      .filter(s -> s.startsWith("A"))
      .forEach(System.out::println);
  ```
  Stream API поддерживает ленивые вычисления, параллелизм и сложные операции (
  фильтрация, маппинг).

- **Spliterator**:
  В Java 8 введён `Spliterator` (Splittable Iterator), который поддерживает
  параллельную обработку:
  ```java
  import java.util.Spliterator;
  import java.util.List;

  List<String> list = List.of("A", "B", "C");
  Spliterator<String> spliterator = list.spliterator();
  spliterator.forEachRemaining(System.out::println);
  ```

- **Guava Iterators**:
  Библиотека Guava предоставляет утилиты для работы с итераторами, такие как
  `Iterators.filter` или `Iterators.transform`:
  ```java
  import com.google.common.collect.Iterators;
  import java.util.Iterator;

  Iterator<String> filtered = Iterators.filter(list.iterator(), s -> s.startsWith("A"));
  ```

### **Итог**

Паттерн Iterator — это фундаментальный инструмент для последовательного доступа
к элементам коллекции, скрывающий её внутреннюю структуру. В Java он встроен в
стандартную библиотеку через `Iterator` и `Iterable`, что делает его основой для
работы с коллекциями. Хотя Stream API и Spliterator в современных приложениях
часто заменяют классический итератор для сложных операций, паттерн остаётся
актуальным для нестандартных коллекций или случаев, где требуется явный контроль
над обходом.