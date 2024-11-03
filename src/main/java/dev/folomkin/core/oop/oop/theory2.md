# Интерфейсы и аннотации

## Интерфейсы

Назначение интерфейса — описание или спецификация функциональности, которую
должен реализовывать каждый класс, его имплементирующий. Класс, реализующий
интерфейс, предоставляет к использованию объявленный интерфейс в виде набора
public-методов в полном объеме.

Интерфейсы подобны абстрактным классам, хотя и не являются классами. В языке
Java существует три вида интерфейсов:

- интерфейсы, определяющие функциональность для классов посредством описания
  методов, но не их реализации;
- функциональные интерфейсы, специфицирующие в одном абстрактном методе свое
  применение;
- интерфейсы, реализация которых автоматически придает классу определенные
  свойства. К последним относятся, например, интерфейсы Cloneable,
  Serializable, Externalizable, отвечающие за клонирование и сохранение
  объекта (сериализацию) в информационном потоке соответственно.

Общее определение интерфейса имеет вид:

    [public] interface Name [extends NameOtherInterface,…, NameN] {
        // constants, methods
    }

**_В Java 8 разрешено объявлять неабстрактные и статические методы в интерфейсах.
Интерфейсы по-прежнему могут содержать абстрактные методы._**

Все объявленные в интерфейсе абстрактные методы автоматически трактуются как
public abstract, а все поля — как public static final, даже если они так
не объявлены.  
Интерфейсы могут объявлять статические методы.  
В интерфейсах могут объявляться методы с реализацией с ключевым словом default.
Эти методы могут быть public или private.

Класс может реализовывать любое число интерфейсов, указываемых через запятую
после ключевого слова implements, дополняющего определение класса. После этого
**_класс обязан реализовать все абстрактные методы_**, полученные им от
интерфейсов, или объявить себя абстрактным классом.

```java
interface AccountAction {
    boolean openAccount();

    boolean closeAccount();

    void blocking();

    default void unBlocking() {
    }

    double depositInCash(int accountId, int amount);

    boolean withdraw(int accountId, int amount);

    boolean convert(double amount);

    boolean transfer(int accountId, double amount);
}
```

В интерфейсе обозначены, но не реализованы, действия, которые может производить
клиент со своим счетом. Реализация не представлена из-за возможности различного
способа выполнения действия в конкретной ситуации.

Если по каким-либо причинам для данного класса не предусмотрена реализация
метода или его реализация нежелательна, рекомендуется генерация непроверяемого
исключения в теле метода, а именно:

```java
public boolean blocking() {
        throw new UnsupportedOperationException();
}
```

В интерфейсе не могут быть объявлены поля без инициализации, интерфейс в
качестве полей содержит только public static final константы.

Множественное наследование между интерфейсами допустимо. Классы, в свою
очередь, интерфейсы только реализуют. Класс может наследовать один
суперкласс и реализовывать произвольное число интерфейсов.

Класс, который реализует интерфейс, должен предоставить полную реализацию всех
абстрактных методов, объявленных в интерфейсе.

```java
package dev.folomkin.core.oop.code;

import java.util.StringJoiner;

interface LineGroupAction {
    double computePerimeter(AbstractShape shape);
}

interface ShapeAction extends LineGroupAction {
    double computeSquare(AbstractShape shape);
}

class AbstractShape {
    private long shapeId;

    public long getShapeId() {
        return shapeId;
    }

    public void setShapeId(long shapeId) {
        this.shapeId = shapeId;
    }

}

class RectangleAction implements ShapeAction {

    @Override
    public double computeSquare(AbstractShape shape) {
        double square;
        if (shape instanceof Rectangle rectangle) {
            square = rectangle.getHeight() * rectangle.getWidth();
        } else {
            throw new IllegalArgumentException("Incompatible shape " + shape.getClass());
        }
        return square;
    }

    @Override
    public double computePerimeter(AbstractShape shape) {
        double perimeter;
        if (shape instanceof Rectangle rectangle) {
            perimeter = 2 * (rectangle.getWidth() + rectangle.getHeight());
        } else {
            throw new IllegalArgumentException("Incompatible shape " + shape.getClass());
        }
        return perimeter;
    }
}

class TriangleAction implements ShapeAction {
    @Override
    public double computeSquare(AbstractShape shape) {
        double square;
        if (shape instanceof RightTriangle triangle) {
            square = 1. / 2 * triangle.getSideA() * triangle.getSideB();
        } else {
            throw new IllegalArgumentException("Incompatible shape " + shape.getClass());
        }
        return square;
    }

    @Override
    public double computePerimeter(AbstractShape shape) {
        double perimeter = 0;
        if (shape instanceof RightTriangle triangle) {
            double a = triangle.getSideA();
            double b = triangle.getSideB();
            perimeter = a + b + Math.hypot(a, b);
        } else {
            throw new IllegalArgumentException("Incompatible shape " + shape.getClass());
        }
        return perimeter;
    }
}

//public abstract class PentagonAction implements ShapeAction {
//    @Override
//    public double computePerimeter(AbstractShape shape) {
//// code
//    }
//}

class Rectangle extends AbstractShape {
    private double height;
    private double width;

    public Rectangle(double height, double width) {
        this.height = height;
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public double getWidth() {
        return width;
    }


}

class RightTriangle extends AbstractShape {
    private double sideA;
    private double sideB;

    public RightTriangle(double sideA, double sideB) {
        this.sideA = sideA;
        this.sideB = sideB;
    }

    public double getSideA() {
        return sideA;
    }

    public void setSideA(double sideA) {
        this.sideA = sideA;
    }

    public double getSideB() {
        return sideB;
    }

    public void setSideB(double sideB) {
        this.sideB = sideB;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", RightTriangle.class.getSimpleName() + "[", "]")
                .add("sideA=" + sideA).add("sideB=" + sideB).toString();
    }
}

class Code {
    public static void main(String[] args) {
        ShapeAction action;
        try {
            Rectangle rectShape = new Rectangle(2, 5);
            action = new RectangleAction();
            System.out.println("Square rectangle: " + action.computeSquare(rectShape));
            System.out.println("Perimeter rectangle: " + action.computePerimeter(rectShape));
            RightTriangle trShape = new RightTriangle(3, 4);
            action = new TriangleAction();
            System.out.println("Square triangle: " + action.computeSquare(trShape));
            System.out.println("Perimeter triangle: " + action.computePerimeter(trShape));
            action.computePerimeter(rectShape); // runtime exception
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }
    }
}
```

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

### Параметризированный интерфейс

Сделать реализацию интерфейса удобной, менее подверженной ошибкам
и практически исключающей проверки на принадлежность типу можно достаточно
легко, если при описании интерфейса добавить параметризацию в виде:

```java
public interface ShapeGeneric<T extends AbstractShape> {
    double computeSquare(T shape);

    double computePerimeter(T shape);
}
```

Параметризованный тип T extends AbstractShape указывает, что в качестве
параметра методов может использоваться только подкласс AbstractShape, что
мало чем отличается от случая, когда тип параметра метода указывался явно.
Но когда дело доходит до реализации интерфейса, то указывается конкретный
тип объектов, являющихся подклассами AbstractShape, которые будут обрабатываться
методами данного класса, а в качестве параметра метода также прописывается тот
же самый конкретный тип:

```java
public class RectangleGeneric implements ShapeGeneric<Rectangle> {
    @Override
    public double computeSquare(Rectangle shape) {
        return shape.getHeight() * shape.getWidth();
    }

    @Override
    public double computePerimeter(Rectangle shape) {
        return 2 * (shape.getWidth() + shape.getHeight());
    }
}

public class TriangleGeneric implements ShapeGeneric<RightTriangle> {
    @Override
    public double computeSquare(RightTriangle shape) {
        return 0.5 * shape.getSideA() * shape.getSideB();
    }

    @Override
    public double computePerimeter(RightTriangle shape) {
        double a = shape.getSideA();
        double b = shape.getSideB();
        double perimeter = a + b + Math.hypot(a, b);
        return perimeter;
    }
}

public class GenericMain {
    public static void main(String[] args) {
        Rectangle rectangle = new Rectangle(2, 5);
        ShapeGeneric<Rectangle> rectangleGeneric = new RectangleGeneric();
        RightTriangle triangle = new RightTriangle(3, 4);
        ShapeGeneric<RightTriangle> triangleGeneric = new TriangleGeneric();
        System.out.println("Square rectangle: " + rectangleGeneric.computeSquare(rectangle));
        System.out.println("Perimeter rectangle: " + rectangleGeneric.computePerimeter(rectangle));
        System.out.println("Square triangle: " + triangleGeneric.computeSquare(triangle));
        System.out.println("Perimeter triangle: " + triangleGeneric.computePerimeter(triangle));
        // triangleGeneric.computePerimeter(rectangle); // compile error
    }
}

```

Применение параметризации при объявлении интерфейсов в данном случае
позволяет избавиться от лишних проверок и преобразований типов при реализации
непосредственно самого интерфейса и использовании созданных на их основе
классов.

## Аннотации

Аннотации — это метатеги, которые добавляются к коду и применяются
к объявлению пакетов, классов, конструкторов, методов, полей, параметров
и локальных переменных. Аннотации всегда обладают некоторой информацией
и связывают эти дополнительные данные и все перечисленные конструкции языка.
Фактически аннотации представляют собой их дополнительные модификаторы,
применение которых не влечет за собой изменений ранее созданного кода.
Аннотации позволяют избежать создания шаблонного кода во многих ситуациях,
активируя утилиты для его генерации из аннотаций в исходном коде.

В языке Java определено несколько встроенных аннотаций для разработки
новых аннотаций — @Retention, @Documented, @Target и @Inherited — из
пакета java.lang.annotation. Из других аннотаций выделяются — @Override,
@Deprecated и @SuppressWarnings — из пакета java.lang. Широкое использование
аннотаций в различных технологиях и фреймворках обуславливается
возможностью сокращения кода и снижения его связанности.

```java
@Target(ElementType.TYPE)
public @interface BaseAction {
    int level();

    String sqlRequest();
}
```

Ключевому слову interface предшествует символ @. Такая запись сообщает
компилятору об объявлении аннотации. В объявлении также есть два метода-члена:
int level(), String sqlRequest().
После объявления аннотации ее можно использовать для аннотирования объявлений
класса за счет объявления со значением ElementType.TYPE целевой аннотации
@Target. Объявление любого типа может быть аннотировано.
Даже к аннотации можно добавить аннотацию. Во всех случаях аннотация
предшествует объявлению.
Применяя аннотацию, нужно задавать значения для ее методов-членов, если при
объявлении аннотации не было задано значение по умолчанию. Далее приведен
фрагмент, в котором аннотация BaseAction сопровождает объявление класса:
```java
    @BaseAction(level = 2, sqlRequest = "SELECT name, phone FROM phonebook")
    public class Base {
      public void doAction() {
        Class<Base> clazz = Base.class;
        BaseAction action = (BaseAction) clazz.getAnnotation(BaseAction.class);
        System.out.println(action.level());
        System.out.println(action.sqlRequest());
      }
    }

```
Данная аннотация помечает класс Base. За именем аннотации, начинающимся с
символа @, следует заключенный в круглые скобки список инициализирующих
значений для методов-членов. Для того чтобы передать значение методучлену,
имени этого метода присваивается значение. Таким образом, в приведенном
фрагменте строка «SELECT name, phone FROM phonebook» присваивается
методу sqlRequest(), члену аннотации типа BaseAction. При этом в операции
присваивания после имени sqlRequest нет круглых скобок. Когда методу-члену
передается инициализирующее значение, используется только имя метода.
Следовательно, в данном контексте методы-члены выглядят как поля.
Все аннотации содержат только объявления методов, добавлять тела этим
методам не нужно, так как их реализует сам язык. Кроме того, эти методы не
могут содержать параметров секции throws и действуют скорее как поля.