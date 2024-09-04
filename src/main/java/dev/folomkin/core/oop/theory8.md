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

Предикаты применяются в stream с методами:

### boolean test(T t):

- в методе **filter(Predicate\<? super T>predicate)**,
  удаляющем из потока объекты, не удовлетворяющие условию предиката.

```java
class Code {
    public static void main(String[] args) {
        String[] arrayStr = {"as", "a", "the", " ", "d", "on", "and", ""};
        System.out.println(
                Arrays.stream(arrayStr)
                        .filter(s -> s.length() < 2)
                        .collect(Collectors.toList())
        );
    }
}
```

- Tерминальный метод anyMatch(Predicate<? super T> predicate) ищет в потоке хотя
  бы один объект, удовлетворяющий предикату, и только в этом случае
  возвратит истину.
- метод allMatch(Predicate<? super T> predicate) возвратит истину, если все
  объекты потока удовлетворяют условию.


### default Predicate<T> and(Predicate<? super T> other):

Метод - логическое И.

```java
class Code {
  public static void main(String[] args) {
    String[] arrayStr = {"as", "a", "the", " ", "d", "on", "and", ""};
    Predicate<String> predicate1 = s -> s.contains("a");
    System.out.println(Arrays.stream(arrayStr)
            .filter(predicate1.and(s -> s.contains("n")))
            .collect(Collectors.toList()));
    

  }
}
```