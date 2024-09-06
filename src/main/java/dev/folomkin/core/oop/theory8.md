# Функциональные интерфейсы

## Методы default и static в интерфейсах

В Java 8 разрешено объявлять неабстрактные и статические методы в интерфейсах.
Интерфейсы по-прежнему могут содержать абстрактные методы.

```java
interface Service {
    default void anOperation() { // -> public
        System.out.println("Service anOperation");
        this.method();
    }

    private void method() { // -> default not required
        System.out.println("Private method");
    }

    static void action() { // -> public
        System.out.println("Service static action");
    }

    int define(int x1, int y1); // public abstract

    void load(); // -> public abstract
}
```

При реализации классом такого интерфейса реализуются только абстрактные методы,
default-методы могут переопределяться при необходимости.

Статический метод вызывается классическим способом, без реализации содержащего
его интерфейса:

    Service.action();

Для вызова default-метода нужно предоставить реализацию интерфейса.

```java
interface Service {
    // ...
}

public class ServiceImpl implements Service {
    @Override
    public int define(int x, int y) {
        return x + y;
    }

    @Override
    public void load() {
        System.out.println("load()");
    }
}
```

Тогда вызов методов интерфейса Service можно представить в виде:

```java
interface Service {
    // ...
}

public class ServiceImpl implements Service {
    // ...
}

public class ServiceMain {
    public static void main(String[] args) {
        Service.action(); // static method
        ServiceImpl service = new ServiceImpl();
        service.define(1, 2);
        service.load();
        service.anOperation(); // default method
    }
}
```

Появление методов по умолчанию в интерфейсах разрешило множественное
наследование поведения, что не так уж редко встречается в практическом
программировании.
Однако если класс реализует два интерфейса с default-методами, сигнатуры которых
совпадают, то компилятор выдаст сообщение об ошибке, так как невозможно будет
определить принадлежность метода при его вызове на объекте класса. При
реализации классом методы интерфейса равноправны.

Единственным решением методов с одинаковыми сигнатурами при реализации
интерфейсов будет обязательное переопределение метода.

## Функциональные интерфейсы

Функциональный интерфейс должен иметь один единственный абстрактный метод и
любое число статических и default-методов. Для объявления такого интерфейса
используется аннотация **@FunctionalInterface**. Интерфейс, помеченный этой
аннотацией, предполагает его использование в виде лямбда-выражения, которое
предлагает более лаконичный синтаксис при создании функций-объектов.

В Java объявлено достаточно интерфейсов с одним абстрактным методом, но
не помеченных аннотацией @FunctionalInterface. К таким интерфейсам относятся
Comparable, Runnable, Callable, Comparator и другие. Интерфейсы без аннотации
@FunctionalInterface желательно использовать как обычную абстракцию с
ключевым словом implements. Основные функциональные интерфейсы размещены в
пакете java.util.function.

Пример функционального интерфейса и его реализации в виде лямбда-выражения:

```java

@FunctionalInterface
interface ShapeService {
    double perimeter(double a, double b);
}

class RectangleService implements ShapeService {
    @Override
    public double perimeter(double a, double b) {
        return 2 * (a + b);
    }
}

class Code {
    public static void main(String[] args) {
        ShapeService shapeService = new ShapeService() {
            @Override
            public double perimeter(double a, double b) {
                return 2 * (a + b);
            }
        }; // -> анонимный класс можно представить в виде лямбды:
        // ->  ShapeService shapeService = (a, b) -> 2 * (a + b);
    }
}
```

Эволюция в лямбде начинается с того, что опускается конструктор анонимного
класса и имя метода интерфейса. Так как метод единственный в интерфейсе, то и
его имя можно не упоминать. Параметры метода отделяются от тела метода
оператором «->»

```java
ShapeService rectangleService = (double a, double b) -> {
    return 2 * (a + b);
};
```

Если тело метода состоит из одного оператора, то и фигурные скобки можно
опустить.

```java
ShapeService rectangleService = (double a, double b) -> 2 * (a + b);
```

Типы параметров метода также можно опустить, так как предполагается,
что они известны из сигнатуры единственного абстрактного метода.

```java
ShapeService rectangleService = (a, b) -> 2 * (a + b);
```

Применение лямбда-выражений становится возможным только для функциональных
интерфейсов из-за единственного абстрактного метода, так как исчезает
необходимость явно указывать имя метода.

```java
class ActionType {
    private double x;
    private double y;

    public ActionType(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double action(ShapeService service) {
        return 10 * service.perimeter(x, y);
    }
}

// Тогда обращение к функциональному интерфейсу будет следующим:

double result = new ActionType(3, 5).action((a, b) -> (a + b) * 4);
```

Функция как объект создается во время выполнения. В функцию передается объект, в
который «завернута» функция.

Все представленные функциональные интерфейсы можно разделить на четыре группы:
**Function<T,R>, Predicate<T>, Consumer<T> и Supplier<T>.**

### Интерфейс Predicate

Интерфейс Predicate\<T> представляет метод **boolean test(T t)**, возвращающий
булево значение в зависимости от выполнения условия на объекте типа T.
Основная область применения: выбор\поиск\фильтрация элементов из stream
или коллекции по условию.

Пример:

```java
    Predicate<String> predicateStr = s -> s.length() < 2;
```

#### Predicate для Stream:

##### boolean test(T t):

- stream.filter(Predicate<? super T>predicate) - удаляет из списка объекты, не
  удовлетворяющие условию:

```java
public static void main(String[] args) {
    String[] arrayStr = {"as", "a", "the", " ", "d", "on", "and", ""};
    System.out.println(
            Arrays.stream(arrayStr)
                    .filter(str -> str.length() > 0)
                    .collect(Collectors.toList())
    );
// [the, and]
}
```

- stream.anyMatch(Predicate<? super T> predicate) - true, если хотя бы один
  объект из потока соответствует условию:

```java
    public static void main(String[] args) {
    String[] arrayStr = {"as", "a", "the", " ", "d", "on", "and", ""};
    System.out.println(
            Arrays.stream(arrayStr)
                    .anyMatch(String::isBlank)
    );
}
```

- stream.allMatch(Predicate<? super T> predicate) - все объекты должны
  удовлетворять условию:

```java
public static void main(String[] args) {
    int[] arrayInt = {1, 3, 5, 9, 17, 33, 65};
    System.out.println(
            Arrays.stream(arrayInt)
                    .allMatch(i -> i == 1)
    );
}
```

##### default Predicate\<T> or(Predicate<? super T> other) - логическое "ИЛИ".

- stream.filter(default Predicate\<T> or(Predicate<? super T> other)) - объект
  из потока должен соответствовать хотя бы одному условию;

```java
public static void main(String[] args) {
    int[] arrayInt = {1, 3, 5, 9, 17, 33, 65};
    System.out.println(
            Arrays.stream(arrayInt)
                    .filter(((IntPredicate) i -> i > 32).or(i -> i < 4))
                    .boxed()
                    .collect(Collectors.toList())
    );
}
```

##### default Predicate\<T> negate() - логическое отрицание предиката

- stream.filter() - выводит все объекты, которые не соответствуют условию:

```java
public static void main(String[] args) {
    int[] arrayInt = {1, 3, 5, 9, 17, 33, 65};
    String[] arrayStr = {"as", "a", "the", " ", "d", "on", "and", ""};
    System.out.println(
            Arrays.stream(arrayStr)
                    .filter(
                            ((Predicate<String>) s -> s.contains("and")).negate()
                    )
                    .collect(Collectors.toList())
    );
}
```

##### static Predicate<T> not(Predicate<? super T> target) - короткий вариант negate()

- stream.filter():

```java
public static void main(String[] args) {
    System.out.println(Arrays.stream(arrayStr)
            .filter(Predicate.not(s -> s.contains("and")))
            .collect(Collectors.toList()));
}
```

##### static Predicate<T> isEqual(Object targetRef) - возвращает предикат эквивалент метода equals() класса Object. Применяется для поиска точного совпадения объектов:

- stream.filter():

```java
public static void main(String[] args) {
    System.out.println(Arrays.stream(arrayStr)
            .filter(Predicate.isEqual("and"))
            .collect(Collectors.toList()));
}
```

- filter(Predicate<? super T> predicate);
- remove(Predicate<? super E> filter);
- allMatch(Predicate<? super T> predicate);
- noneMatch(Predicate<? super T> predicate);
- anyMatch(Predicate<? super T> predicate);
- takeWhile(Predicate<? super T> predicate);
- iterate(T seed, Predicate<? super T> hasNext, UnaryOperator<T> next).

В пакет java.util.function объявлен еще один интерфейс-предикат
BiPredicate\<T,U> c абстрактным методом boolean test(T t, U u).

    BiPredicate<String, Integer> biPredicate = (s, max) -> s.length() <= max;
    System.out.println(biPredicate.test("java", 7));

## Интерфейс Function

Основной абстрактный метод R apply(T t) принимает объект типа T и возвращает
объект типа R. Выполняет действие над объектом одного типа и возвратить объект
другого типа.

Пример:

    Function<String, Integer> fun1 = s -> s.length();// String to Integer
    Function<Integer, String> fun2 = i -> Integer.toBinaryString(i);//int to String

#### Function для Steam

##### R apply(T t):

- stream.map():

```java
public static void main(String[] args) {
    Function<String, Integer> fun1 = s -> s.length();// String to Integer
    Function<Integer, String> fun2 = i -> Integer.toBinaryString(i);//int to String
    String[] arrayStr = {"as", "a", "the", " ", "d", "on", "and", ""};
    int[] arrayInt = {1, 3, 5, 9, 17, 33, 65};
    System.out.println(
            Arrays.stream(arrayInt)
                    .map(s -> s * 2)
                    .boxed()
                    .collect(Collectors.toList())
    );

    System.out.println(
            Arrays.stream(arrayStr)
                    .map(fun1)
                    .collect(Collectors.toList())
    );
}
```

##### default <V> Function<V, R> compose(Function<? super V, ? extends T>before)

Возвращает составную функцию, которая сначала применяет функцию before к своему
входу, а затем применяет эту функцию к результату:

    Function<String, Integer> fun1 = s -> s.length();// String to Integer
    Function<Integer, String> fun2 = i -> Integer.toBinaryString(i);//int to String
    Function<Integer, Integer> fun3 = fun1.compose(fun2);
    Function<Integer, Integer> fun3 = fun1.compose(i -> Integer.toBinaryString(i));

Первой будет вызвана функция fun2, она преобразует число в его двоичное
представление 10100 в виде строки, затем на результат будет вызвана функция
fun2, которая вычислит длину строки. То есть на входе в композицию функций будет
передано число, и на выходе получится число. Если изменить функцию fun2 так,
чтобы она возвращала какой-либо другой тип данных, то на входе в композицию
функций будет число, а на выходе другой тип данных.

    System.out.println(fun1.compose(fun2).apply(17)); // -> 5

```java
public static void main(String[] args) {
    int[] arrayInt = {1, 3, 5, 9, 17, 33, 65};
    System.out.println(Arrays.stream(arrayInt)
            .boxed()
            .map(((Function<String, Integer>) s -> s.length())
                    .compose(i -> Integer.toBinaryString(i)))
            .collect(Collectors.toList()));
    // -> [1, 2, 3, 4, 5, 6, 7]
}
```

##### default <V> Function<T,V> andThen(Function<? super R, ? extends V> after)

Возвращает составную функцию, которая сначала применяет эту
функцию к своему входу, а затем применяет функцию after к результату.
Метод andThen() вызовет функции в порядке, обратном методу compose().

    Function<String, String> fun4 = fun1.andThen(fun2);

Или

    Function<String, String> fun4 = fun1.andThen(i -> Integer.toBinaryString(i));

Первой будет вызвана функция fun1, вычисляющая длину строки, а после — функция
fun2, преобразующая число в двоичное представление в виде строки.

    System.out.println(fun1.andThen(fun2).apply("java"));
    System.out.println(Arrays.stream(arrayStr)
      .map(((Function<String, Integer>)s -> s.length()).andThen(i -> 
              Integer.toBinaryString(i)))
      .collect(Collectors.toList()));

##### static <T> Function<T, T> identity()

Возвращает функцию, которая всегда возвращает свой входной аргумент.

- reduce(BinaryOperator<T> accumulator);
- sorted(Comparator<? super T> order);
- max(Comparator<? super T> comparator);
- min(Comparator<? super T> comparator);
- map(Function<? super T,? extends R> mapper);
- flatMap(Function<? super T,? extends Stream<? extends R>> mapper);
- iterate(T seed, UnaryOperator<T> t);
- mapToInt(ToIntFunction<? super T> mapper);
- toArray(IntFunction<A[]> generator).

#### Специализированные интерфейсы Function

**_BiFunction\<T, U, R>. Метод R apply(T t, U u)_**

```java
public static void main(String[] args) {
    BiFunction<Double, String, Integer> bi = (d, s) -> (Double.toString(d) + s).length();
    int length = bi.apply(1.23, "java");
    System.out.println(length);
}
```

**_ToIntFunction\<T>_**

Метод которого int applyAsInt(T t) принимает любой
тип данных, но должен возвращать значение типа int.

**_IntFunction\<R>_**

Метод R apply(int value) принимает значение типа int, но может
возвращать значение любого типа.

**_Интерфейс BinaryOperator<T, T>_**

Объявляет метод T apply(T t1, T t2), что соответствует обычному бинарному
оператору:

    BinaryOperator<String> binaryOperator = (s1, s2) -> s1 + s2.toUpperCase();
    System.out.println(binaryOperator.apply("oracle", "epam"));

**_Comparator\<T>_**

int compare(T o1, T o2) - функция, принимающая два объекта одного типа и
возвращающая
значение типа int.

### Интерфейс Consumer

Интерфейс Consumer\<T> представляет абстрактный метод **void accept(T t)**,
функция, принимающая объект типа T и выполняющая над ним некоторое действие.
Результат действия можно сохранить во внешнем объекте, например,
коллекции или вывести в поток вывода, например, в файл или на консоль.

```java
    public static void main(String[] args) {
        String str = "as a- the-d -on and";
        String regex = "-";
        Consumer<String> consumer = s -> System.out.println(Arrays.toString(s.split(regex)));
        consumer.accept(str);
        // -> [as a, the, d , on and]

        int[] arrInt = {1, 2, 3};
        Arrays.stream(arrInt)
                .forEach(i -> System.out.print(i * 2 + ", "));
        // -> [2, 4, 6];
    }
```

**Методы**:  
**_default Consumer\<T> andThen(Consumer<? super T> after)_**