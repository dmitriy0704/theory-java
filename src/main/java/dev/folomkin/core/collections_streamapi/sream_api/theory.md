# Stream API

[Коллекции](../collections/theory.md)

## Введение

Интерфейс java.util.stream.Stream<T> — поток объектов для преобразования
коллекций, массивов. В потоке не хранятся элементы операции, он не модифицирует
источник, а формирует в ответ на действие новый поток. Операции в потоке не
выполняются до момента, пока не потребуется получить конечный результат
выполнения.

Stream нельзя воспринимать как просто поток ввода\вывода. Этот поток
создается на основе коллекции\массива, элементы которой переходят в состояние
информационного ожидания действия, переводящего поток в следующее
состояние до достижения терминальной цели, после чего поток прекращает
свое существование.

Получить stream из коллекции можно методом stream() интерфейса Collection,
а из массива методом Stream.of(T…values).

```java
class Book {
    private String title;
    private String author;
    private int pages;

    public Book(String title, String author, int pages) {
        this.title = title;
        this.author = author;
        this.pages = pages;
    }

    // Getters & Setters...

    @Override
    public String toString() {
        return "Book{" + "title='" + title + '\'' + ", author='" + author + '\'' + ", pages=" + pages + '}';
    }
}

public class Code {
    public static void main(String[] args) {
        List<Book> books = new ArrayList<>();
        books.add(new Book("Spring in Actions", "David", 480));
        books.add(new Book("Spring Pro", "Rob", 560));
        books.add(new Book("Spring QuickStart", "Spilke", 520));
        List<Book> listBooks = books
                .stream()
                .filter(b -> b.getPages() < 500)
                .map(b -> {
                    b.setTitle(b.getTitle().toUpperCase());
                    return b;
                })
                .toList();
        listBooks.forEach(System.out::println);
        List<String> strings = List.of("as a the d on and".split("\\s"));
        strings.stream();
    }
}
```

## Методы:

### Промежуточные:

- **filter(Predicate<? super T> predicate)** - выбор элементов из потока на
  основании работы предиката в новый поток. Отбрасываются все элементы, не
  удовлетворяющие условию предиката:

```java
public class Code {
    public static void main(String[] args) {
        List<String> strings = List.of("as a the d on and".split("\\s"));
        strings.stream()
                .filter(s -> s.length() > 2)
                .forEach(System.out::println);
    }
}
```

- **map(Function<? super T, ? extends R> mapper)** — изменение всех элементов
  потока, применяя к каждому элементу функцию mapper. Тип параметризации потока
  может изменяться, если типизация T и R относится к различным классам:

```java
public class Code {
    public static void main(String[] args) {
        List<String> strings = List.of("as a the d on and".split("\\s"));
        strings.stream()
                .map(String::toUpperCase)
                .forEach(System.out::println);
    }
}
```

- **flatMap(Function<T, Stream<R>> mapper)** - преобразовывает один объект,
  как правило, составной, в объект более простой структуры, например, массив
  в строку, список в объект, список списков в один список

```java

class BookList implements Iterable<String> {
    private int year;
    private List<String> names = new ArrayList<>();

    public BookList(int year) {
        this.year = year;
    }

    public List<String> getNames() {
        return List.copyOf(names);
    }

    public boolean add(String name) {
        return names.add(name);
    }

    @Override
    public Iterator<String> iterator() {
        return names.iterator();
    }
}

public class Code {
    public static void main(String[] args) {
        BookList books1 = new BookList(2023);
        books1.add("Spring in Actions");
        books1.add("Spring Pro");
        books1.add("Spring QuickStart");
        BookList books2 = new BookList(2024);
        books2.add("Kotlin in Actions");
        books2.add("Kotlin for Android");
        List<BookList> booksList = List.of(books1, books2);
        List<String> currencyList = booksList.stream()
                .map(b -> b.getNames())         // -> Stream<List<String>>
                .flatMap(b -> b.stream())       // -> Stream<String>
                .collect(Collectors.toList());
        System.out.println(currencyList);
    }
```

- **peek(Consumer<T> consumer)** — возвращает поток, содержащий все элементы
  исходного потока. Используется для просмотра элементов в текущем состоянии
  потока. Можно использовать для записи логов:

```java
 public static void main(String[] args) {
    List<String> strings = List.of("Java Python C# C++ JavaScript PHP".split("\\s+"));
    strings.stream()
            .peek(System.out::println)
            .map(String::toUpperCase)
            .forEach(System.out::println);

}
```

- **sorted(Comparator<T> comparator) и sort()** — сортировка в новый поток:

```java
public static void main(String[] args) {
    List<String> strings = List.of("Java Python C# C++ JavaScript PHP".split("\\s+"));
    strings.stream()
            .sorted(String::compareToIgnoreCase)
            //.sorted(Comparator.comparingInt(String::length))
            .forEach(System.out::println);
}
```

- **limit(long maxSize)** — ограничивает выходящий поток заданным в параметре
  значением;

```java
public static void main(String[] args) {
    List<String> strings = List.of("Java Python C# C++ JavaScript PHP".split("\\s+"));
    strings.stream()
            .limit(3) // -> выведет первые 3 значения 
            .forEach(System.out::println);

}
```

- **skip(long n)** — не включает в выходной поток первые n элементов исходного
  потока;

```java
public static void main(String[] args) {
    List<String> strings = List.of("Java Python C# C++ JavaScript PHP".split("\\s+"));
    strings.stream()
            .skip(3) // -> пропустит первые 3 значения 
            .forEach(System.out::println);
}
```

- **distinct()** — удаляет из потока все идентичные элементы;

```java
public static void main(String[] args) {
    List<String> strings = List.of("Java Python Java JavaScript PHP".split("\\s+"));
    strings.stream()
            .distinct()
            .forEach(System.out::println);

    // -> Java Python JavaScrip PHP
}
```

### Терминальные:

Сводят поток к результату. Результатом может быть новая коллекция, объект
некоторого класса, число. Промежуточные операции обязательно должны завершаться
терминальными, иначе они не выполнятся, так как просто не имеют смысла.

- void forEach(Consumer<T> action) — выполняет действие над каждым элементом
  потока. Чтобы результат действия сохранялся, реализация action должна
  предусматривать фиксацию результата в каком-либо объекте или потоке вывода;
- Optional<T> findFirst() - находит первый элемент в потоке;
