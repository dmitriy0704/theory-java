# Итератор(Iterator)

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