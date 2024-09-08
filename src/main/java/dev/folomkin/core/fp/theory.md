# Лямбда выражения

## Введение

Ключевым аспектом для понимания реализации лямбда-выражений в Java являются две
конструкции: собственно лямбда - выражение и функциональный интерфейс. Начнем с
простого определения каждого из них.

**_Лямбда-выражение_** - анонимный (т.е. безымянный) метод. Используется для
реализации метода, определенного функциональным интерфейсом. Таким образом,
лямбда-выражение приводит к форме анонимного класса. Лямбда-выражения также
часто называют замыканиями.

**_Функциональный интерфейс_** это интерфейс, который содержит один и только
один абстрактный метод, обычно устанавливающий предполагаемое назначение
интерфейса. Соответственно функциональный интерфейс, как правило, представляет
одиночное действие. Например, стандартный интерфейс Runnable является
функциональным интерфейсом, поскольку в нем определен только один метод: run().
Следовательно, run() определяет действие Runnable. Кроме того, функциональный
интерфейс задает целевой тип лямбда-выражения. Важно понимать, что
лямбда-выражение может применяться только в контексте, в котором указан его
целевой тип. И еще один момент: функциональный интерфейс иногда называют
типом SAM, где SAM означает Single Abstract Method - единственный абстрактный
метод.

## Основы лямбда-выражений

Лямбда-выражение опирается на синтаксический элемент и операцию. Операцию иногда
называется лямбда-операцией или операцией стрелки и обозначается с помощью ->.
Она делит лямбда-выражение на две части. В левой части указываются любые
параметры, требующиеся в лямбда-выражении. В правой части находится тело
лямбда-выражения, которое определяет действия лямбда-выражения. В Java
определены два вида тела лямбда-выражения с **одиночным выражением** и с *
*блоком
кода**.

    // Код 
    double method() {
        return 98.6;
    }

    // можно заменить на
    () -> 98.6;

Когда лямбда-выражению необходим параметр, он указывается в списке параметров
слева от лямбда-операции:

    n -> (n % 2) == 0

## Блочные лямбда-выражения

Тело в лямбда-выражениях, состоящее из единственного выражения называется
**_телом-выражением_**, а лямбда-выражение с телом-выражением **_одиночным
лямбда-выражением_**. В теле-выражении код в правой части лямбда-операции
должен содержать одно выражение. Но иногда требуется более одного выражения.
Для обработки таких случаев в Java поддерживается второй вид лямбда-выражений,
где в правой части лямбда-операции находится блок кода, который может содержать
более одного оператора. Тело этого вида называется **_блочным_**.
Лямбда-выражения с блочными телами иногда называются
**_блочными лямбда-выражениями_**.

За исключением того, что блочные лямбда-выражения разрешают указывать несколько
операторов, они используются почти так же, как только что рассмотренные
одиночные лямбда-выражения. Тем не менее, есть одно ключевое отличие: вы обязаны
явно применять оператор return, чтобы возвратить значение. Поступать так
необходимо, потому что тело блочного лямбда-выражения не представляет одиночное
выражение.

Блочное лямбда-выражение используется с целью нахождения наименьшего
положительного делителя для значения типа int. В нем применяется интерфейс
NumericFunc с методом func(), который принимает один аргумент типа int и
возвращает результат типа int. Таким образом, NumericFunc поддерживает числовую
функцию для значений типа int.

```java
// Блочное лямбда выражение находит наименьший положительный делитель для значения типа int.
interface NumericFunc {
    int func(int n);
}

public class Code {
    public static void main(String[] args) {
        // Это блочное лямбда-выражение возвращает наименьший
        // положительный делитель для значения.
        NumericFunc numericFunc = (n) -> {
            int result = 1;
            // Получаем абсолютное значение n.
            n = n < 0 ? -n : n;
            for (int i = 2; i < n / i; i++) {
                if ((n % 1) == 0) {
                    result = i;
                    break;
                }
            }
            return result;
        };
        System.out.println("Наименьший делитель 12" + numericFunc.func(12));
        System.out.println("Наименьший делитель 11" + numericFunc.func(11));
    }
}
```

## Лямбда-выражения и захват переменных

Переменные, определенные в объемлющей области действия лямбда-выражения,
доступны внутри лямбда-выражения. Лямбда-выражение может задействовать
переменную экземпляра или статическую переменную, определенную в объемлющем
классе. Лямбда-выражение также имеет доступ к ссылке this(явно и неявно),
которая ссылается на вызывающий экземпляр класса, включающего лямбда-выражение.
Таким образом, лямбда-выражение может получать либо устанавливать значение
переменной экземпляра или статической переменной и вызывать метод, определенный
в объемлющем классе.

Когда в лямбда-выражении используется локальная переменная из его объемлющей
области видимости, то возникает особая ситуация, называемая захватом переменной.
В таком случае лямбда-выражение может работать только с локальными переменными,
которые являются фактически финальными. Фактически финальная переменная
представляет собой переменную, значение которой не меняется после ее первого
присваивания. Явно объявлять такую переменную как final нет никакой
необходимости, хотя поступать так не будет ошибкой. (Параметр this объемлющей
области видимости автоматически будет фактически финальным, а лямбда-выражения
не имеют собственной ссылки this.)
Локальная переменная из объемлющей области не может быть модифицирована
лямбда-выражением, поскольку в таком случае исчез бы ее статус фактически
финальной, из-за чего она стала бы незаконной для захвата.

```java
// Пример захвата локальной переменной из объемлющей области видимости ,
interface MyFunc {
    int func(int n);
}

class Code {
    public static void main(String[] args) {
        // Локальная переменная, которая может быть захвачена,
        int num = 10;
        MyFunc myLambda = (n) -> {
            // Использовать num подобным образом разрешено.
            // Переменная num не модифицируется,
            int v = num + n;
            // Однако следующая строка кода недопустима из-за того, что в ней
            // предпринимается попытка модифицировать значение num.
            // num++;
            return v;
        };
        // Использовать лямбда-выражение. Отобразится число 18.
        System.out.println(myLambda.func(8));
    }
    // Следующая строка кода тоже вызовет ошибку, потому что в ней
    // устраняется статус переменной num как фактически финальной.
    // num = 9;
}
```

В комментариях указано, что переменная num является фактически финальной и
потому может применяться внутри myLambda. Именно по этой причине оператор
printlnO выводит число 18. Когда метод func() вызывается с аргументом 8,
значение v внутри лямбда- выражения устанавливается путем сложения переменной
num (равной 10)и значения , переданного в п (равного 8). В итоге func()
возвращает 18. Такая работа объясняется тем, что num не изменяется после
инициализации. Однако если значение num будет изменено внутри лямбда-выражения
или за его пределами, то переменная num утратит свой статус фактически
финальной, что вызовет ошибку и программа не скомпилируется.

### Генерация исключений в лямбда-выражениях

Лямбда-выражение может генерировать исключение. Тем не менее, если инициируется
проверяемое исключение, то оно должно быть совместимым с исключением или
исключениями, которые перечислены в конструкции throws абстрактного метода в
функциональном интерфейсе. Например, если лямбда-выражение генерирует исключение
IOException, то в конструкции throws абстрактного метода в функциональном
интерфейсе должно быть указано IOException.

```java
interface MyIOAction {
    boolean ioAction(Reader rdr) throws IOException;
}

class Code {
    public static void main(String[] args) {

        // Это блочное лямбда-выражение может сгенерировать исключение IOException.
        // Следовательно, IOException должно быть указано в конструкции throws
        // метода ioAction() в MyAction.
        MyIOAction myIOAction = (rdr) -> {  // <-- Это лямбда выражение
            // может сгенерировать исключение

            int ch = rdr.read();            // может сгенерировать исключение
            // IOException;
            // ...
            return true;
        };
    }
}
```

Поскольку вызов read() может привести к генерации исключения IOException,
конструкция throws метода ioAction() в функциональном интерфейсе MylOAction
должна включать IOException. В противном случае программа не скомпилируется,
потому что лямбда - выражение больше не будет совместимым с ioAction().

# Методы default и static в интерфейсах

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

# Функциональные интерфейсы

Как упоминалось ранее, функциональный интерфейс представляет собой такой
интерфейс, в котором определен только один абстрактный метод.
Не все методы интерфейса должны быть абстрактными. Начиная с JDK 8, интерфейс
может иметь один или несколько стандартных методов, которые не являются
абстрактными, как и закрытые и статические методы интерфейса. В результате
теперь метод интерфейса будет абстрактным только в том случае, если для него не
определена реализация. Это означает, что функциональный интерфейс может включать
стандартные, статические или закрытые методы, но во всех случаях он должен иметь
один и только один абстрактный метод. Поскольку нестандартные, нестатические,
незакрытые методы интерфейса неявно абстрактны, нет нужды применять модификатор
abstract (правда, при желании его можно указывать).

Пример:

    interface MyValue{
       double getValue();
    }

В данном случае метод getValue ( ) является неявно абстрактным и единственным
методом, определенным в MyValue. Таким образом, MyValue функциональный
интерфейс, функция которого определяется getValue().

Пример:

```java
interface MyValue {
    double getValue();
}

public class Code {
    public static void main(String[] args) {
//        MyValue myValue = new MyValue() {
//            public double getValue() {
//                return Math.random();
//            }
//        };
        // Можно заменить на
        MyValue myValue = () -> Math.random();
        // Или
        MyValue myValue = Math::random;
    }
}
```

Когда лямбда-выражение встречается в контексте целевого типа, автоматически
создается экземпляр класса, который реализует функциональный интерфейс, а
лямбда-выражение определяет поведение абстрактного метода, объявленного в
функциональном интерфейсе. При вызове данного метода через цель лямбда-выражение
выполняется. Таким образом, лямбда-выражение предоставляет способ трансформации
кодового сегмента в объект.

Если лямбда-выражение принимает один или несколько параметров, то абстрактный
метод в функциональном интерфейсе тоже должен принимать такое же количество
параметров.

```java
interface MyValue {
    double getValue(double d);
}

public class Code {
    public static void main(String[] args) {
        MyValue myValue = (d) -> 1.0 / d;
        System.out.println(myValue.getValue(4.0));
    }
}
```

Здесь getValue() реализуется лямбда-выражением, на которое ссылается myPval, и
возвращает обратную величину аргумента. В данном случае значение 4.0 передается
методу getValue(), который возвращает 0.25.

```java
package dev.folomkin.core.fp;


// Функциональный интерфейс
interface MyValue {
    double getValue();
}

// Функциональный интерфейс
interface MyParamValue {
    double getValue(double value);
}


public class Code {
    public static void main(String[] args) {
        MyValue myVal; // Ссылка на функциональный интерфейс

        // Здесь лямбда-выражение представляет собой константное выражение.
        // Когда оно присваивается myVal, конструируется экземпляр класса,
        // где лямбда-выражение реализует метод getValue() из MyValue.

        // Интерфейс реализуется с помощью анонимного класса
//        myVal = new MyValue() {
//            public double getValue() {
//                return 42.0;
//            }
//        };

        // Который можно записать как лямбда выражение:
        myVal = () -> 42.0; // <- Простое лямбда-выражение

        // Вызвать метод getValue(), предоставляемый ранее присвоенным лямбда-выражением
        System.out.println(myVal.getValue());

        // Создать параметризованное лямбда-выражение и присвоить
        // его ссылке MyParamValue. Это лямбда-выражение возвращает
        // обратную величину переданного ему аргумента.
        MyParamValue myParamVal = (value) -> 1 / value; // Лямбда-выражение имеющее параметры

        // Вызываем getValue через ссылку myPval
        System.out.println(myParamVal.getValue(4.0));
        System.out.println(myParamVal.getValue(8.0));

        // Лямбда-выражение должно быть совместимым с методом, определенным
        // в функциональном интерфейсе. Таким образом, следующий код недопустим:
        // myVal = О -> "three"; // Ошибка! Типы String и double не совместимы !
        // myPval = () > Math.random(); // Ошибка! Требуется параметр!
    }
}

```

Ключевой аспект функционального интерфейса состоит в том, что его можно
использовать с любым лямбда-выражением, которое с ним совместимо.

```java
// Использование одного и того же функционального интерфейса с 
// тремя разными лямбда выражениями
interface NumericTest {
    boolean test(int n, int m);
}

public class Code {
    public static void main(String[] args) {
        // Является ли одно число делителем другого
        NumericTest numericTest1 = (n, d) -> (n % d) == 0;
        if (numericTest1.test(10, 2))
            System.out.println("2 является делителем 10");
        if (!numericTest1.test(10, 3))
            System.out.println("3 не является делителем 10");
        System.out.println();

        // Первый аргумент меньше второго
        NumericTest numericTest2 = (n, m) -> (n < m);
        if (numericTest2.test(2, 10))
            System.out.println("2 меньше 10");
        if (!numericTest2.test(10, 2))
            System.out.println("10 не меньше 2");
        System.out.println();

        // Абсолютные значения аргументов равны
        NumericTest numericTest3 = (n, m) -> (n < 0 ? -n : n) == (m < 0 ? -m : m);
        if (numericTest3.test(4, -4))
            System.out.println("Абсолютные значения 4 и -4 равны .");
        if (!numericTest3.test(4, 5))
            System.out.println("Абсолютные значения 4 и -5 не равны .");
        System.out.println();
    }
}
```

___

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

## Обобщенные функциональные интерфейсы

Само лямбда-выражение не может указывать параметры типа. Таким образом,
лямбда-выражение не может быть обобщенным. (Разумеется, из-за выведения типов
все лямбда-выражения обладают некоторыми “обобщенными” качествами). Однако
функциональный интерфейс, ассоциированный с лямбда-выражением, может быть
обобщенным. В таком случае целевой тип лямбда-выражения частично определяется
аргументом или аргументами типов, указанными при объявлении ссылки на
функциональный интерфейс.

```java

// Использование обобщенного функционального интерфейса;

// Обобщенный функциональный интерфейс с двумя параметрами,
// который возвращает результат типа boolean
interface SomeTest<T> {
    boolean test(T n, T m);
}

public class Code {
    public static void main(String[] args) {
        // Это лямбда выражение определяет, является
        // ли одно число типа Integer делителем другого
        SomeTest<Integer> isFactor = (n, d) -> (n % d) == 0;
        if (isFactor.test(10, 2))
            System.out.println("2 является делителем 10");

        // Это лямбда-выражение определяет, является ли одно число типа Double
        // делителем другого
        SomeTest<Double> isFactorD = (n, d) -> (n % d) == 0;
        if (isFactorD.test(10.0, 2.0))
            System.out.println("2 является делителем 10");


        // Это лямбда-выражение определяет, является
        // л и одна строка частью другой строки.
        SomeTest<String> isIn = (a, b) -> a.indexOf(b) != -1;
        String str = "Generic Functional Interface";

        System.out.println("Проверяемая строка: " + str);
        if (isIn.test(str, "face"))
            System.out.println("Строка 'face' найдена.");
        else
            System.out.println("Строка 'face' не найдена.");

    }
}
```

Обобщенный функциональный интерфейс SomeTest в общем виде:

    interface SomeTest <T> {
        boolean test(T n, T m);
    }

Здесь T указывает возвращаемый тип и тип параметра test(). Это означает, что он
совместим с любым лямбда-выражением, которое принимает два параметра и
возвращает результат типа boolean.

Интерфейс SomeTest применяется для предоставления ссылки на три разных вида
лямбда - выражений. Первое лямбда-выражение использует тип Integer, второе — тип
Double, а третье — тип String. Таким образом, один и тот же функциональный
интерфейс может применяться для ссылки на лямбда-выражения isFactor, isFactorD и
isIn. Отличается только аргумент типа, передаваемый SomeTest.







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

### Function для Steam

#### R apply(T t):

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

Методы **_default Consumer\<T> andThen(Consumer<? super T> after)_** - позволяет
построить композицию из двух и более действий как одного целого:

```java
public static void main(String[] args) {
    String str = "as a- the-d -on and";
    String regex1 = "\\s";
    Consumer<String> consumer1 = s -> System.out.println(Arrays.toString(s.split(regex1)));
    String regex2 = "a";
    Consumer<String> consumer2 = consumer1
            .andThen(s -> System.out.println(Arrays.toString(s.split(regex2))));
    consumer2.accept(str);
}

// Первый consumer1 удалит из строки все знаки regex1, а второй — все знаки
// regex2.
// ->  [, s , - the-d -on , nd]

```

**BiConsumer<T t, U u>** объявляет метод void accept(T t, U u) с двумя
параметрами, что представляется как расширение возможностей Consumer.

```java
   public static void main(String[] args) {
    BiConsumer<String, String> greeter
            = (firstName, lastName) -> System.out.println("Hello " + firstName + " " + lastName);

    greeter.accept("James", "Smith");

    int r;
    BiConsumer<Integer, Integer> greeter2 = (i1, i2) -> System.out.println(i1 + i2);
    greeter2.accept(1, 2);

}
```

#### Consumer для Stream:

- forEach(Consumer<? super T> action);
- forEachOrdered(Consumer<? super T> action);
- peek(Consumer<? super T> action).

### Интерфейс Supplier

Интерфейс Supplier<T> возвращает новый объект типа T методом T get().
Предназначен для создания новых объектов. Метод get() единственный в интерфейсе.

Аналогичные интерфейсы предназначены для создания значений базовых типов:
BooleanSupplier и его метод boolean getAsBoolean();
DoubleSupplier и его метод double getAsDouble();
IntSupplier и его метод int getAsInt();
LongSupplier и его метод long getAsLong().

```java
public static void main(String[] args) {
    Supplier<StringBuilder> supplier = () -> new StringBuilder("java");
    StringBuilder builder = supplier.get();
    Supplier<int[]> supplier2 = () -> new int[10];
    int[] arr = supplier2.get();
}

// Фабрика для получения массивов
public static Supplier<int[]> buildArray(int size) {
    final int length = size > 0 ? size : 1;
    return () -> new int[length];
}

t() {
    int[] array = ArrayFactory.buildArray(10).get();
}
```

### Интерфейс Comparator

Интерфейс используется для сортировки наборов объектов конкретного типа по
правилам, определенного типа. Необходимо реализовать метод **int compare(T ob1,
T ob2)**, принимающий два объекта, для которых определяется возвращаемое целое
значение, знак которого и определяет правило сортировки.

Реализация метода equals() класса Object предоставляет возможность проверить,
эквивалентен один экземпляр другому или нет.
На практике очень часто возникает необходимость сравнения объектов на
больше/меньше либо равно. Метод boolean equals(Object obj),
также объявленный в интерфейсе Comparator<T>, рекомендуется переопределять,
если экземпляр класса будет использоваться для хранения информации.
Это необходимо для исключения противоречивой ситуации, когда для двух
объектов метод compare() возвращает 0, т.е. сообщает об их эквивалентности,
в то же время метод equals() для этих же объектов возвращает false,
так как данный метод не был никем определен и была использована его версия из
класса Object. Кроме того, наличие метода equals() обеспечивает корректную
работу метода семантического поиска и проверки на идентичность
contains(Object o), определенного в интерфейсе java.util.Collection,
а следовательно, реализованного в любой коллекции.

```java
// С помощью лямбда-выражения можно привести базовую реализацию компаратора
// для строк по убыванию их длин.

public static void main(String[] args) {
    Comparator<String> comparator = (s1, s2) -> s2.length() - s1.length();
    String str = "and java course epam the rose lion wolf hero green white red white";
    Arrays.stream(str.split("\\s"))
            .sorted(comparator)
            .forEach(s -> System.out.printf("%s ", s));
}
```

Метод **sorted(Comparator<? super T> c)** интерфейса Stream<T> автоматически
вызывает метод **compare(T o1, T o2)** при сортировке элементов stream.
Также при сортировке списка методами: **static <T> void sort(List<T> list**,
**Comparator<? super T> c)** класса Collections и **void sort(Comparator<? super
E> c)** интерфейса List<T> необходим компаратор.

Как вариант реализации компаратора можно использовать статический метод
**comparing(Function<? super T, ? extends U> keyExtractor)** интерфейса
Comparator<T>.  
Кроме уже перечисленных методов, интерфейс Comparator<T> объявляет
еще некоторые необходимые методы:

- **default Comparator<T> reversed()** — примененный к уже созданному
  компаратору, делает сортировку в обратном направлении;
- **nullFirst(Comparator<? super T> comparator)** и **nullLast(Comparator<?
  super T> comparator)** — применяются к компаратору для размещения всех
  null в начале или конце списка при сортировке;
- **thenComparing(Comparator<? super E> other)**, **thenComparing( Function<?
  super T, ? extends U> keyExtractor)** — default-методы, позволяющие произвести
  сортировку в сортировке. Например, все строки отсортировать по возрастанию
  длины и далее все строки с одинаковыми длинами отсортировать по алфавиту.

```java
public static void main(String[] args) {
    Comparator<String> comparator = (s1, s2) -> s2.length() - s1.length();
    String str = "and java course epam the rose lion wolf hero green white red white";
    Arrays.stream(str.split("\\s"))
            .sorted(Comparator.comparing(String::length).thenComparing(String::compareTo))
            .forEach(s -> System.out.printf("%s ", s));
}
//-> and red the epam hero java lion rose wolf green white white course
```

Если в процессе использования необходимы сортировки по различным полям класса,
то реализацию компаратора можно вынести в отдельный класс.
Также реализация компаратора, являясь логической частью класса-сущности,
может быть его частью и представлена в виде статического внутреннего класса,
была популярна до появления перечислений:

```java
class Order {
    private long orderId;
    private double amount;

    // getters / setters
    // other code
    public static class IdComparator implements Comparator<Order> {
        @Override
        public int compare(Order o1, Order o2) {
            return Long.compare(o1.orderId, o2.orderId);
        }
    }

    public static class AmountComparator implements Comparator<Order> {
        @Override
        public int compare(Order o1, Order o2) {
            return Double.compare(o1.getAmount(), o2.getAmount());
        }
    }
}
```

Возможности перечислений и интерфейса Comparator<T> позволили
отойти от классической модели реализации компаратора и дать возможность
перечислению объявить компаратор в качестве поля. Перечисление теперь
оборачивает компаратор:

```java
public enum OrderComparatorFunctional {
    ID(Comparator.comparingLong(Order::getOrderId)),
    AMOUNT(Comparator.comparingDouble(Order::getAmount));
    private Comparator<Order> comparator;

    OrderComparatorFunctional(Comparator<Order> comparator) {
        this.comparator = comparator;
    }

    public Comparator<Order> getComparator() {
        return comparator;
    }
}

// ->  orders.sort(OrderComparatorFunctional.AMOUNT.getComparator());

// Возможен вариант полной ассоциации перечисления и компаратора. Для
// этого достаточно, чтобы класс-перечисление имплементировал интерфейс
// Comparator<T> и для каждого своего элемента предоставил реализацию метода
// compare():

public enum OrderComparatorClassic implements Comparator<Order> {
    ID {
        @Override
        public int compare(Order o1, Order o2) {
            return Long.compare(o1.getOrderId(), o2.getOrderId());
        }
    },
    AMOUNT {
        @Override
        public int compare(Order o1, Order o2) {
            return Double.compare(o1.getAmount(), o2.getAmount());
        }
    }
}

// -> orders.sort(OrderComparatorClassic.AMOUNT);

```



## Замыкания

Блок кода, представляющий собой лямбда-выражение вместе со значениями локальных
переменных и параметров метода, в котором он объявлен, называется замыканием,
или closure. Объект-функция создается во время исполнения, и применен может быть
уже после того как объект, его создавший, прекратит существование. Такая
ситуация требует, чтобы переменные, которые использует лямбда-выражение, не
могли быть изменены. Значения переменных фиксируются замыканием и изменены быть
не могут.

```java
    public static Function<String, Integer> build(String strNum) {
    int count = 1;
    return t -> {
        int res = Integer.parseInt(strNum + t) + count;
        return res;
    };
}
// Или
// return t -> Integer.valueOf(strNum + t) + count;
```

При формировании объекта функции Function, как возвращаемого значения
метода build(), значения параметров сохраняются, и функция будет
использовать эти зафиксированные значения strNum и count в тот момент, когда
произойдет ее вызов.

```java

class Code {
    public static void main(String[] args) {
        Function<String, Integer> function = build("100");
        int res = function.apply("77");
        System.out.println(res);
    }

    public static Function<String, Integer> build(String strNum) {
        int count = 1;
        return t -> {
            int res = Integer.parseInt(strNum + t) + count;
            System.out.println(t);
            System.out.println(res);
            return res;

            // Переменные count и strNum не должны изменяться
        };
    }
}
```

Эти переменные должны иметь константные значения, как если бы они были
объявлены как final. Отсюда и название — замыкание.
Замыкания не запрещают использования полей класса как статических, так
и нестатических.

```java
public class FunctionBuilder<T> {
    static int count = 1;

    public static Function<String, Integer> build(String strNum) {
        count++;
        return t -> Integer.valueOf(strNum + t) + ++count;
    }
}
```
# Ссылки на методы

С лямбда-выражениями связана одна важная возможность, которая называется ссылкой
на метод. Ссылка на метод предлагает способ обращения к методу, не инициируя его
выполнение. Она имеет отношение к лямбда-выражениям, поскольку тоже требует
контекста целевого типа, состоящего из совместимого функционального интерфейса.
При вычислении ссылки на метод также создается экземпляр функционального
интерфейса. Существует четыре типа ссылок на методы:

- Ссылка на статический метод
- Ссылка на метод экземпляра
- Ссылка на метод экземпляра конкретного объекта
- Ссылка на конструктор
___
Более короткая запись лямбда-выражения возможна в случае, если реализации
функционального интерфейса необходимо передать уже существующий метод без всяких
изменений.

Оператор видимости «::» отделяет метод от объекта или класса. Существуют три
варианта записи:

- **ContainingClass::staticMethodName** — ссылка на статический метод;
- **ContainingObject::instanceMethodName** — ссылка на нестатический метод
  конкретного объекта;
- **ContainingType::methodName** — ссылка на нестатический метод любого объекта
  конкретного типа.

Первые два варианта эквивалентны лямбда-выражению с параметрами методами.
В третьем варианте первый параметр становится целевым для метода:

    Comparator<Long> comparator = (l1, l2) -> l1.compareTo(l2);
    comparator = Long::compareTo;

В качестве объекта можно использовать ссылки this и super.
Кроме ссылки на метод, существуют также и ссылки на конструктор, где в качестве
имени метода указывается оператор new.

    Supplier<StringBuilder> supplier1 = StringBuilder::new;


## Ссылка на статические методы

Для ссылки на статический метод применяется следующий общий синтаксис,
предусматривающий указание имени класса перед именем метода:

    имя-класса::имя-метода

Имя класса отделяется от имени метода двойным двоеточием. Разделитель :: был
добавлен к языку в версии JDK 8 специально для этой цели. Ссылка на метод может
использоваться везде, где она совместима со своим целевым типом.

В приведенном коде сначала объявляется функциональный интерфейс
IntPredicate с методом test(), который принимает параметр типа int и
возвращает результат типа boolean. Таким образом, его можно применять для
проверки целочисленного значения на предмет соответствия заданному условию.
Затем создается класс по имени MyIntPredicates, где определяются три
статических метода, каждый из которых проверяет, удовлетворяет ли значение
заданному условию: isPrime(), isEven() и isPositive(). Метод isPrime()
проверяет, является ли число простым, метод isEven() является ли число четным, а
метод isPositive() — является ли число положительным.

Далее в классе Code создается метод numTest(), в первом параметре которого
передается ссылка на intPredicate. Во втором параметре указывается проверяемое
целое число. Внутри main() производятся три различных теста за счет
вызова numTest() и передачи ссылки на метод выполняемого теста.

```java
import java.io.IOException;
import java.io.Reader;

// Функциональный интерфейс для предикатов,
// работающими с целочисленными значениями
interface IntPredicate {
    boolean test(int n);
}

// В этом классе определены три статических метода,
// которые выполняют проверку целого числа на предмет
// соответствию условию.
class MyIntPredicates {

    // Статический метод, который возвращает true,
    // если число является простым
    static boolean isPrime(int n) {
        if (n < 2) return false;
        for (int i = 2; i <= n / i; i++) {
            if (n % i == 0) return false;
        }
        return true;
    }

    // Статический метод, который возвращает true,
    // если число является четным.
    static boolean isEven(int n) {
        return n % 2 == 0;
    }

    // Статический метод, который возвращает true,
    // если число является положительным.
    static boolean isPositive(int n) {
        return n > 0;
    }
}

class Code {
    // Типом первого параметра этого метода является
    // функциональный интерфейс. Таким образом, ему можно передавать
    // ссылку на любой экземпляр данного интерфейса, включая
    // созданный ссылкой на метод.
    static boolean numTest(IntPredicate p, int v) {
        return p.test(v);
    }

    public static void main(String[] args) throws IOException {
        boolean result;

        // Передать numTest() ссылку на метод isPrime.
        result = numTest(MyIntPredicates::isPrime, 17);
        if (result) System.out.println("17 is prime");

        // Передать numTestO ссылку на метод isEven.
        result = numTest(MyIntPredicates::isEven, 12);
        if (result) System.out.println("12 является четным.");

        // Передать numTestO ссылку на метод isPositive.
        result = numTest(MyIntPredicates::isPositive, 11);
        if (result) System.out.println("11 является положительным.");
    }
}

```

## Ссылки на методы экземпляра

Ссылка на метод конкретного экземпляра создается с помощью следующего базового
синтаксиса:

    объектная-ссылка : : имя-метода

Синтаксис ссылки на метод экземпляра подобен синтаксису, применяемому для ссылки
на статический метод, но вместо имени класса используется объектная ссылка.
Таким образом, метод, на который указывает ссылка, работает по отношению к
конструкции _объектная ссылка_.

Данный аспект иллюстрируется в приведенной ниже программе. В ней используется
тот же интерфейс IntPredicate и метод test(), что и в предыдущей программе, но
создается класс по имени MyIntNum, где хранится значение int и определяется
метод isFactor(), который выясняет, является ли переданное значение делителем
значения, сохраненного в экземпляре MyIntNum. Затем в методе main() создаются
два экземпляра MyIntNum, после чего вызывается numTest() с передачей ссылки на
метод isFactorO и проверяемое значение. В каждом случае ссылка на метод работает
относительно конкретного объекта.

