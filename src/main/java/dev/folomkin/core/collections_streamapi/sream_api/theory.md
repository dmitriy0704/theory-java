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
                .map(s -> s.length())
                .forEach(System.out::println);
    }
}
```

