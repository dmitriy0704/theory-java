# STREAM API

## ВВЕДЕНИЕ

Интерфейс java.util.stream.Stream\<T> — поток объектов для преобразования
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

## КОМПОНЕНТЫ STREAM API

1. Источник (Source) - откуда приходят данные. Это может быть коллекция, массив,
   файл, генератор или любой другой источник данных.
2. Операции - преобразовывают и/или обрабатывают данные.
3. Поток (Stream) - последовательность элементов, подлежащих параллельной или
   последовательной обработке.
4. Пайплайн (Pipeline) - последовательность операций в потоке, применяемых к
   данным.
5. Терминал (Terminal) - место выхода данных из потока. Терминальная операция
   означает окончание обработки потока и возвращает результат.

## КАК РАБОТАЕТ STREAM API

```java
public class Code {
    public static void main(String[] args) {
        List<String> list = List.of("one", "two", "thee");
        list.stream()
                .filter(s -> {
                    System.out.println("filter: " + s);
                    return s.length() <= 3;
                })
                .map(s1 -> {
                    System.out.println("map: " + s1);
                    return s1.toUpperCase();
                })
                .forEach(x -> {
                    System.out.println("forEach: " + x);
                });

        // ->
        // filter: one
        // map: one
        // forEach: ONE
        // filter: two
        // map: two
        // forEach: TWO
        // filter: thee
    }
}
```

Сначала первый элемент проходит через пайплайн, затем второй, а третий не
проходит проверку в filter(), поэтому его обработка прекращается на этом этапе.

### STATELESS И STATEFULL ОПЕРАЦИИ

Операции без состояния (stateless), такие как map() и filter(), обрабатывают
каждый элемент потока независимо от других. Они не требуют информации о других
элементах для своей работы, что делает их идеально подходящими для параллельной
обработки.

С другой стороны, операции, сохраняющие состояние (statefull), такие как
sorted(), distinct() или limit(), требуют знания о других элементах для своей
работы. Это означает, что им приходится учитывать все (или часть) элементы в
потоке перед выдачей какого-либо результата.

Если пайплайн содержит только операции без состояния, то он может быть
обработан "в один проход". Если же он содержит операции с состоянием, то
пайплайн разбивается на секции, где каждая секция заканчивается операцией с
состоянием.

```java
public class Code {
    public static void main(String[] args) {
        List<String> list = List.of("one", "two", "thee");
        list.stream()
                .filter(s -> {
                    System.out.println("filter: " + s);
                    return s.length() <= 3;
                })
                .map(s1 -> {
                    System.out.println("map: " + s1);
                    return s1.toUpperCase();
                })
                .sorted()
                .forEach(x -> {
                    System.out.println("forEach: " + x);
                });

        // ->
        // filter: one
        // map: one
        // filter: two
        // map: two
        // filter: thee
        // forEach: ONE
        // forEach: TWO
    }
}
```

Теперь весь поток элементов должен быть проанализирован и отсортирован, прежде
чем можно будет выполнить следующую операцию. Это значит, что все элементы
должны пройти через фильтр, быть отсортированы. Таким образом, операция sorted()
создает "точку синхронизации" в пайплайне обработки.

### SPLITITERATOR

В основе стримов лежит Iterator на стеройдах, так называемый **_Spliterator_**.

**_Spliterator_** используется в основе стримов в Java и играет важную роль при
параллельной обработке данных, так как именно он отвечает за разделение данных
на части для независимой обработки каждым потоком.

**_МЕТОДЫ SPLITITERATOR:_**

- **_long estimateSize()_** - возвращает количество элементов.
- **_tryAdvance(Consumer)_** - принимает функциональный интерфейс Consumer,
  определяющий действия, которые должны быть выполнены над текущим элементом.
- **_int characteristics()_** - возвращает набор характеристик текущего
  сплитератора.
- **_Spliterator<T> trySplit()_** - пытается разделить текущий сплитератор на
  два. Если операция успешна, то возвращает новый сплитератор, и уменьшает
  размер исходного сплитератора. Если разделение не возможно, то возвращает
  null.

**_ХАРАКТЕРИСТИКИ SPLITITERATOR_**

Spliterator обладает специальными характеристиками, сообщающими об
особенностях источника данных, из которого он был создан. Эти характеристики
помогают в оптимизации работы потока при выполнении терминальных операций.
Например, нет смысла выполнять сортировку уже отсортированной коллекции.

- ORDERED: указывает, что элементы имеют определенный порядок.
- DISTINCT: указывает, что каждый элемент уникален. Определяется по equals().
- SORTED: указывает, что элементы отсортированы.
- SIZED: указывает, что размер источника известен заранее.
- NONNULL: указывает, что ни один элемент не может быть null.
- IMMUTABLE: указывает, что элементы не могут быть модифицированы.
- CONCURRENT: указывает, что исходные данные могут быть модифицированы без
  воздействия на Spliterator.
- SUBSIZED: указывает, что размер разделенных Spliterator-ов также будет
  известен.

Каждая операция может менять флаги характеристик. Это важно, поскольку каждый
этап обработки данных будет знать об этих изменениях, что позволяет выполнить
оптимальные действия. Например, операция map() сбросит флаги SORTED и DISTINCT,
так как данные могут измениться, но всегда сохранит флаг SIZED, так как размер
потока не изменяется при выполнении map().

### ПАРАЛЛЕЛЬНОЕ ВЫПОЛНЕНИЕ

Для выполнения потоков в параллельном режиме можно использовать методы
parallelStream() или parallel(). Без явного вызова этих методов поток будет
выполняться последовательно. Для разделения коллекции на части, которые могут
быть обработаны отдельными потоками, Java использует метод
Spliterator.trySplit().

С точки зрения плана выполнения, параллельная обработка схожа с
последовательной, за исключением одного основного отличия. Вместо одного набора
связанных операций у нас будет несколько его копий, и каждый поток будет
применять эти операции к своему сегменту элементов. После завершения обработки
все результаты, полученные каждым потоком, объединяются в один общий результат.

## STREAM

Центральной концепцией Stream API является потоковые операции, представляющие
собой ряд последовательных действий, выполняемых над данными.

Основные свойства потоков:

1. Декларативность: Потоки в Java описывают, что должно быть сделано, а не
   конкретный способ его выполнения.
2. Ленивость: Это означает, что потоки не выполняют никакой работы, пока не
   будет
   вызвана терминальная операция.
3. Одноразовость: После того как терминальная операция была вызвана на потоке,
   этот
   поток больше не может быть использован. Если необходимо применить другую
   операцию к данным, потребуется новый поток.
4. Параллельность: Несмотря на то, что потоки в Java по умолчанию выполняются
   последовательно, их можно легко распараллелить.

## МЕТОДЫ STREAM API:

[//]: # ( 
ПРОМЕЖУТОЧНЫЕ:
- filter
- map
- flatMap
- peek
- sorted и sort
- limit
- skip
- distinct
ТЕРМИНАЛЬНЫЕ:
- void forEach
- Optional<T> findFirst
- Optional<T> findAny
- long count
- boolean allMatch
- boolean anyMatch
- boolean noneMatch
- Optional<T> reduce
- <R,A> R collect
- Optional<T> min
- Optional<T> max
)

### МЕТОДЫ СОЗДАНИЯ STREAM

#### ОТ КОЛЛЕКЦИИ

- Collection.stream():

```java
public static void main(String[] args) {
    Collection<Integer> list = new ArrayList<>();
    Stream<Integer> stream = list.stream();
}
```

#### ИЗ МАССИВА

- Arrays.stream()
- Stream.of(T ...values)

```java
public static void main(String[] args) {
    int[] arr = {1, 2, 3, 4, 5};
    Stream<Integer> stream = Arrays.stream(arr).boxed();
}
```

#### ИЗ СТРОКИ

- String.chars()

```java
public static void main(String[] args) {
    String str = "Hello";
    IntStream stream = str.chars();
}
```

#### ИЗ ФАЙЛА

- Files.lines()

```java
public static void main(String[] args) {
    Path path = Paths.get("file.txt");
    Stream stream = Files.lines(path);
}
```

### ГЕНЕРИРОВАНИЕ

Поток может быть создан с помощью метода Stream.generate(Supplier). Supplier
должен возвращать новое значение при каждом вызове.

    Stream stream = Stream.generate(() -> new Random().nextInt());

### Билдер

    Stream.Builder builder = Stream.builder();
    builder.add(1);
    builder.add(2);
    builder.add(3);
    Stream stream = builder.build();

### ПРОМЕЖУТОЧНЫЕ:

Промежуточные операции в потоках Java описываются декларативно с использованием
лямбда-выражений. Эти операции представляют собой своего рода "рецепт" обработки
данных. Однако стоит учесть, что они не выполняются немедленно после объявления.
В действительности, все промежуточные операции выполняются только при вызове
терминальной операции, которая запускает общую цепочку обработки. Важной
характеристикой промежуточных операций является то, что каждая из них возвращает
новый объект Stream. Это позволяет нам связывать несколько операций в одну
"цепочку" (Pipeline).

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
  в строку, список в объект, список списков в один список. используется для
  создания одного потока из множества потоков. Он принимает функцию в качестве
  аргумента, которая применяется к каждому элементу исходного потока. Эта
  функция принимает элемент исходного потока и возвращает новый поток.

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

        // Или
        List<List<Integer>> listOfLists = Arrays.asList(
                Arrays.asList(1, 2, 3),
                Arrays.asList(4, 5, 6),
                Arrays.asList(7, 8, 9)
        );
        Stream<Integer> flattenedStream = listOfLists.stream()
                .flatMap(Collection::stream);
        flattenedStream.forEach(System.out::println); // prints 1, 2, 3, 4, 5, 6, 7, 8, 9
    }
```

- **peek(Consumer<T> consumer)** — возвращает поток, содержащий все элементы
  исходного потока. Используется для просмотра элементов в текущем состоянии
  потока. создает новый поток, идентичный исходному, но с дополнительной
  операцией, применяемой к каждому элементу при его прохождении по конвейеру
  потока. В данном примере, метод peek() применяется к потоку чисел. Consumer,
  переданный в метод peek(), выводит каждый элемент на консоль. В процессе
  этого, каждый элемент, проходя по конвейеру потока, отображается на консоли,
  но сам поток остается неизменным. Можно использовать для записи логов:

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

- **takeWhile(Predicate)** - создает новый поток, содержащий элементы исходного
  потока до тех пор, пока они удовлетворяют указанному условию. Если первый
  элемент потока не соответствует предикату, новый поток будет пустым.

```java
 public static void main(String[] args) {
    List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
    List<Integer> takenNumbers = numbers.stream()
            .takeWhile(n -> n < 5)
            .collect(Collectors.toList());
    System.out.println(takenNumbers); // prints [1, 2, 3, 4]
} 
```

- **Stream.dropWhile()** - — это метод, который появился в Java 9 и позволяет
  пропускать элементы потока до тех пор, пока выполняется заданное условие. Как
  только условие перестаёт выполняться, поток продолжает работу с оставшимися
  элементами. dropWhile() полезен для работы с отсортированными данными, когда
  нужно пропустить первые элементы, не соответствующие заданному критерию, и
  продолжить работу с остальными.

```java
public static void main(String[] args) {
    List<Integer> integers = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
    // Пропустим все числа меньше 5 и возьмем оставшиеся
    List<Integer> result = integers.stream()
            .dropWhile(n -> n < 5)
            .toList();
    System.out.println(result);
    // -> [5, 6, 7, 8, 9]
}
```

- **IntStream boxed()** - возвращает поток, состоящий из элементов этого потока,
  каждый из которых упакован в целое число.

```java
 public static void main(String[] args) {
    IntStream stream = IntStream.range(3, 8);
    Stream<Integer> stream1 = stream.boxed();
    stream1.forEach(System.out::println);
} 
```

### ТЕРМИНАЛЬНЫЕ:

Сводят поток к результату. Результатом может быть новая коллекция, объект
некоторого класса, число. Промежуточные операции обязательно должны завершаться
терминальными, иначе они не выполнятся, так как просто не имеют смысла.  
Терминальные операции выводят конечный результат обработки потока. Они могут
включать в себя перебор элементов, подсчет элементов, сбор элементов в
коллекцию, поиск элементов и т.д.  
Именно терминальная операция запускает поток. После ее вызова происходит анализ
операций в пайплайне, и определяется эффективная стратегия его выполнения.

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

    // Или
    List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
    Optional<Integer> sum = numbers.stream().reduce((a, b) -> a + b);
    System.out.println(sum.get());
}
```

- **<R, A> R collect(Collector<? super T, A, R> collector)** - собирает элементы
  в коллекцию List, Set, Map или объект другого типа;

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

## Класс Collector

Интерфейс Collector инкапсулирует процесс комбинирования элементов потока в одну
итоговую структуру. Коллекторы можно использовать с различными методами потока,
такими как collect(), groupingBy(), joining(), partitioningBy() и др.

```java
public interface Collerctor<T, A, R> {
    Supplier<A> supplier();

    BiConsumer<A, T> accumulator();

    BinaryOperator<A> combiner();

    Function<A, R> finisher();

    Set<Collector.Characteristics> characteristics();
}
```

Класс Collectors содержит набор статических методов-коллекторов, которые
упрощают выполнение общих операций, таких как преобразование элементов в списки,
множества и другие структуры данных.

Вот некоторые наиболее популярные методы класса Collectors:

- toList(): Этот метод возвращает коллектор, который накапливает входные
  элементы в новый List.
- toSet(): Этот метод возвращает коллектор, который накапливает входные элементы
  в новый Set.
- joining(): Возвращает коллектор, который объединяет элементы потока в единую
  строку.
- counting(): Возвращает коллектор, который подсчитывает количество элементов в
  потоке.

Можно быстро реализовать метод collect(Collector<? super T, A, R> collector)
для сбора элементов в какую-то конкретную структуру.

```java
public interface Collerctor<T, A, R> {
    Stream<?> stream;
    List<?> list = stream.collect(Collectors.toList());

    //Коллектор выше аналогичен данному коду
    list =stream.collect(
            ()->new ArrayList<>(), // определяем структуру
            (list,t)->list.add(t), // определяем, как добавлять элементы
            (l1,l2)->l1.addAll(l2) // и как объединять две структуры в одну
            );
}
```

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

- **reducing(T identity, BinaryOperator\<T> op)** - коллектор, осуществляющий
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

- **reducing(U identity, Function\<? super T,? extends U> mapper,
  BinaryOperator\<U> op)** - аналогичное предыдущему действие. В примере ниже
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

- **groupingBy(Function\<? super T, ? extends K> classifier)** - коллектор
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

- **partitioningBy(Predicate\<? super T> predicate)** - коллектор разбиения
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

## Продвинутые советы и использование

### Возвращать Stream<T> вместо коллекций

```java
import java.util.stream.Stream;

class Group {
    private User[] users;

    public Stream<User> users() {
        return Arrays.sream(users);
    }
}

class Group {
    private Map<String, User> nameToUser;

    public Stream<User> users() {
        return nameToUser.values().sream();
    }
}

class Group {
    private List<User> users;

    public Stream<User> users() {
        return users.sream();
    }
}
```

# Задачи

## Посчитать сумму

```java
public static void main(String[] args) {
    int sum = list.stream().mapToInt(i -> i).sum();
    int sum2 = list.stream().reduce(0, (x, y) -> x + y);
    int sum3 = list.stream().reduce(0, Integer::sum);
    System.out.println(sum);
    System.out.println(sum2);
    System.out.println(sum3);
}
```

## Группировка элементов

Чтобы сгруппировать данные по какому-нибудь признаку, нам надо использовать
метод collect() и метод Collectors.groupingBy().

Группировка списка рабочих по их должности (деление на списки)
    
    Map<String, List<Worker>> map1 = workers.stream()
        .collect(Collectors.groupingBy(Worker::getPosition));
 
Группировка списка рабочих по их должности (деление на множества)
    
    Map<String, Set<Worker>> map2 = workers.stream()
                .collect(
                        Collectors.groupingBy(
                                Worker::getPosition, Collectors.toSet()
                        )
                );

Подсчет количества рабочих, занимаемых конкретную должность

    Map<String, Long> map3 = workers.stream()
                .collect(
                        Collectors.groupingBy(
                                Worker::getPosition, Collectors.counting()
                        )
                );

Группировка списка рабочих по их должности, при этом нас интересуют только имена

    Map<String, Set<String>> map4 = workers.stream()
                .collect(
                        Collectors.groupingBy(
                                Worker::getPosition,
                                Collectors.mapping(
                                        Worker::getName,
                                        Collectors.toSet()
                                )
                        )
                );

Расчет средней зарплаты для данной должности

    Map<String, Double> map5 = workers.stream()
    .collect(
          Collectors.groupingBy(
              Worker::getPosition,
              Collectors.averagingInt(Worker::getSalary)
          )
    );

Группировка списка рабочих по их должности, рабочие представлены только именами единой строкой

    Map<String, String> map6 = workers.stream()
          .collect(
              Collectors.groupingBy(
              Worker::getPosition,
                  Collectors.mapping(
                  Worker::getName,
                    Collectors.joining(", ", "{","}")
                  )
              )
          );

Группировка списка рабочих по их должности и по возрасту.

    Map<String, Map<Integer, List<Worker>>> collect = workers.stream()
        .collect(
          Collectors.groupingBy(
              Worker::getPosition,
          Collectors.groupingBy(Worker::getAge)
        )
    );
