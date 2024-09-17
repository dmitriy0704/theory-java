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

- **void forEach(Consumer<T> action)** — выполняет действие над каждым элементом
  потока. Чтобы результат действия сохранялся, реализация action должна
  предусматривать фиксацию результата в каком-либо объекте или потоке вывода;
- **Optional<T> findFirst()** - находит первый элемент в потоке;

```java
public static void main(String[] args) {
    List<String> strings = List.of("Java Python Java JavaScript PHP".split("\\s+"));
    String strFirst = strings.stream()
            .filter(s -> s.matches("J\\w*"))
            .findFirst()
            .orElse("none");
    System.out.println(strFirst); // -> Java

    // Фильтр выбрал в потоке все элементы, удовлетворяющие условию 
    // предиката, а метод findFirst() нашел первый элемент.
}
```

- **Optional<T> findAny()** - находит элемент в потоке:

```java
public static void main(String[] args) {
    List<String> strings = List.of("Java Python Java JavaScript PHP".split("\\s+"));
    String anyStr = strings.stream()
            .filter(s -> s.matches("an[a-z]"))
            .findAny()
            .orElse("none");
    System.out.println(anyStr);

    // Фильтр выбрал в потоке все элементы, удовлетворяющие условию 
    // предиката, а метод findFirst() нашел первый элемент.
}
```

- **long count()** - возвращает число элементов потока;

```java
public static void main(String[] args) {
    List<String> strings = List.of("Java Python Java JavaScript PHP".split("\\s+"));
    long count = strings.stream()
            .filter(s -> s.matches("a\\w*"))
            .count();
    System.out.println(count);
}
```

- **boolean allMatch(Predicate<T> predicate)** - возвращает истину, если все
  элементы stream удовлетворяют условию предиката;

```java
public static void main(String[] args) {
    List<String> strings = List.of("Java Python Java JavaScript PHP".split("\\s+"));
    long count = strings.stream()
            .allMatch(s -> s.length() < 5); // false
}
```

- **boolean anyMatch(Predicate<T> predicate)** — возвращает истину, если
  хотя бы один элемент stream удовлетворяет условию предиката;

```java
public static void main(String[] args) {
    List<String> strings = List.of("Java Python Java JavaScript PHP".split("\\s+"));
    boolean res2 = strings.stream().anyMatch(s -> s.startsWith("a")); // true
}
```

- **boolean noneMatch(Predicate<T> predicate)** — возвращает истину, если ни
  один элемент stream не удовлетворяет условию предиката;

```java
public static void main(String[] args) {
    List<String> strings = List.of("Java Python Java JavaScript PHP".split("\\s+"));
    boolean res3 = strings.stream().noneMatch(s -> s.endsWith("z")); // true
}
```

- **Optional<T> reduce(T identity, BinaryOperator<T> accumulator)** — сводит все
  элементы потока к одному результирующему объекту, завернутому в оболочку
  Optional;

```java
public static void main(String[] args) {
    List<String> strings = List.of("Java Python Java JavaScript PHP".split("\\s+"));
    int sumLength = strings.stream()
            .map(s -> s.length())
            .reduce(0, (n1, n2) -> n1 + n2);
    // Поток строк преобразуется в поток их длин и метод reduce() вычисляет 
    // сумму всех длин строк.
}
```

- **<R, A> R collect(Collector<? super T, A, R> collector)** - собирает элементы
  в коллекцию или объект другого типа;

```java
  public static void main(String[] args) {
    Map<String, Integer> map = Arrays.stream("Java Python JavaScript PHP".split("\\s+"))
            .collect(Collectors.toMap(s -> s, s -> s.length()));
    System.out.println(map); // -> {Java=4, JavaScript=10, PHP=3, Python=6}
}
```

- **Optional<T> min(Comparator<T> comparator)** - находит минимальный элемент;

```java
    public static void main(String[] args) {
    List<String> strings = List.of("Java PHP".split("\\s+"));
    String min = strings.stream()
            .min(Comparator.comparingInt(s -> s.charAt(s.length() - 1)))
            .orElse("none");
    System.out.println(min); // -> PHP
}
```

- **Optional<T> max(Comparator<T> comparator)** — находит максимальный элемент.

```java
//  Action и его метод подсчета суммы кодов — символов строки:
class Action {
    public static int sumCharCode(String str) {
        return str.codePoints().reduce(0, (v1, v2) -> v1 + v2);
    }
}

public class Code {
    public static void main(String[] args) {
        List<String> strings = List.of("Java PHP".split("\\s+"));
        String max = strings.stream()
                .max(Comparator.comparingInt(Action::sumCharCode))
                .orElse("empty");
        System.out.println(max);
    }
}

// Поиск строки с максимальной суммой кодов его символов.
// -> Java
```

**Источники Stream API:**

- collection.stream(),
- Arrays.stream(int[] array),
- Files.walk(Path path),
- Files.list(Path path),
- bufferedReader.lines(),
- Stream.iterate(T seed, UnaryOperator<T> f),
- Stream.generate(Supplier<? extends T> s),
- Stream.of(T...t),
- Stream.ofNullable(T t),
- Stream.empty(),
- Stream.concat(Stream<? extends T> a, Stream<? extends T> b),
- string.lines(),
- string.codePoints(),
- string.chars(),
- random.ints(),
- random.doubles(),
- random.longs() и другие.

## Алгоритмы сведения Collectors

Методы класса java.util.stream.Collectors представляют основные возможности,
позволяя произвести обработку stream к результату, будь то число, строка или
коллекция. Каждый статический метод класса создает экземпляр Collector для
передачи методу collect() интерфейса Stream, который и выполнит действия по
преобразованию stream алгоритмом, содержащимся в экземпляре Collector.

**Методы:**

- **toCollection(Supplier<C> collectionFactory), toList(), toSet()** -
  преобразование в коллекцию;

```java
public static void main(String[] args) {
    List<String> strings = List.of("Java Python Go JavaScript PHP".split("\\s+"));
    List<Integer> listLengths = strings.stream()
            .map(String::length)
            .collect(Collectors.toList());
    System.out.println(listLengths); // -> [4, 6, 2, 10, 3]
}
```

- **joining(CharSequence delimiter)** - обеспечивает конкатенацию строк с
  заданным
  разделителем;

```java
public static void main(String[] args) {
    List<String> strings = List.of("Java Python Go JavaScript PHP".split("\\s+"));
    String res = strings.stream()
            .map(String::toUpperCase)
            .collect(Collectors.joining(" : "));
    System.out.println(res); // -> JAVA : PYTHON : GO : JAVASCRIPT : PHP
}
```

- **mapping(Function<? super T,? extends U> mapper,Collector<? super
  U,A,R> downstream)** - позволяет преобразовать элементы одного типа в элементы
  другого типа;

```java
public static void main(String[] args) {
    List<String> strings = List.of("Java Python Go JavaScript PHP".split("\\s+"));
    List<Integer> result = strings.stream()
            .collect(Collectors.mapping(s ->
                    (int) s.charAt(0), Collectors.toList()));
    System.out.println(result); // -> [74, 80, 71, 74, 80]
}
```

- **minBy(Comparator<? super T> c)/maxBy(Comparator<? super T> c)** - коллектор
  для нахождения минимального или максимального элемента в потоке;

```java
public static void main(String[] args) {
    List<String> strings = List.of("Java Python Go JavaScript PHP".split("\\s+"));
    String minLex = strings.stream()
            .collect(Collectors.minBy(String::compareTo))
            .orElse("none");
    System.out.println(minLex); // -> Go
}
```

- **filtering(Predicate<? super T> predicate, Collector<? R> downstream)** -
  выполняет фильтрацию элементов;

```java
public static void main(String[] args) {
    List<String> strings = List.of("Java Python Go JavaScript PHP".split("\\s+"));
    List<String> lists = strings.stream()
            .collect(Collectors.filtering(s -> s.length() > 2, Collectors.toList()));
    System.out.println(lists); // -> Java Python JavaScript PHP
}
```

- **counting()** - позволяет посчитать количество элементов потока;

```java
public static void main(String[] args) {
    List<String> strings = List.of("Java Python Go JavaScript PHP".split("\\s+"));
    long counter = strings.stream()
            .collect(Collectors.counting());
    System.out.println(counter); // -> 5
}
```

- summingInt(ToIntFunction<? super T> mapper) — выполняет суммирование
  элементов. Существуют версии для Long и Double;

```java
public static void main(String[] args) {
    List<String> strings = List.of("Java Python Go JavaScript PHP".split("\\s+"));
    int length = strings.stream()
            .collect(Collectors.summingInt(
                    String::length)
            );
    System.out.println(length);
}
```

- **averagingInt(ToIntFunction<? super T> mapper)** - вычисляет среднее
  арифметическое элементов потока. Существуют версии для Long и Double;

```java
public static void main(String[] args) {
    List<String> strings = List.of("Java Python Go JavaScript PHP".split("\\s+"));
    Double averageLength = strings.stream()
            .collect(Collectors.averagingDouble(
                            String::length
                    )
            );
    System.out.println(averageLength);
}
```

- **reducing(T identity, BinaryOperator<T> op)** - коллектор, осуществляющий
  редукцию (сведение) элементов на основании заданного бинарного оператора;

```java
public static void main(String[] args) {
    List<String> strings = List.of("Java Python Go JavaScript PHP".split("\\s+"));
    int sumCodeFirstChars = strings.stream()
            .map(s -> (int) s.charAt(0))
            .collect(Collectors.reducing(0, (a, b) -> a + b));
    System.out.println(sumCodeFirstChars);
}
```

- **reducing(U identity, Function<? super T,? extends U> mapper,
  BinaryOperator<U> op)** - аналогичное предыдущему действие. В примере ниже
  производится умножение всех длин строк, но без предварительного преобразования
  stream к другому типу;

```java
public static void main(String[] args) {
    List<String> strings = List.of("Java Python Go JavaScript PHP".split("\\s+"));
    int productLength = strings.stream()
            .collect(Collectors.reducing(1, s -> s.length(), (s1, s2) -> s1 * s2));
    System.out.println(productLength);
}
```

- **groupingBy(Function<? super T, ? extends K> classifier)** - коллектор
  группировки элементов потока;

```java
public static void main(String[] args) {
    List<String> strings = List.of("Java Python Go JavaScript PH".split("\\s+"));
    Map<Integer, List<String>> byLength = strings.stream()
            .collect(Collectors.groupingBy(String::length));
    System.out.println(byLength);
    // -> {2=[Go, PH], 4=[Java], 6=[Python], 10=[JavaScript]}
}
```

- **partitioningBy(Predicate<? super T> predicate)** — коллектор разбиения
  элементов потока;

```java
  public static void main(String[] args) {
    List<String> strings = List.of("Java Python Go JavaScript PH".split("\\s+"));
    Map<Boolean, List<String>> boolLength = strings.stream()
            .collect(Collectors.partitioningBy(s -> s.length() < 3));
    System.out.println(boolLength);
    // -> {false=[Java, Python, JavaScript], true=[Go, PH]}
}
```

### Метасимвол в коллекции

Метасимволы используются при параметризации коллекций для расширения
возможностей самой коллекции и обеспечения ее типобезопасности.
Например, если параметр метода предыдущего примера изменить с List<Order>
на List<? extends Order>, то в метод можно будет передавать коллекции,
параметризованные любым допустимым типом, а именно классом Order и любым
его подклассом, что невозможно при записи без анонимного символа.