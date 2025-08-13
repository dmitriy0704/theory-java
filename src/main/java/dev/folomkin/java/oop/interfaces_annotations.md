# Интерфейсы и аннотации

## Интерфейсы

До выхода JDK 8 интерфейс мог определять только "что'; но не "как': В версии 
JDK 8 ситуация изменилась. Начиная с JDK 8, к методу интерфейса можно добавлять 
стандартную реализацию. Кроме того, в JDK 8 также добавлены статические методы 
интерфейса, а начиная с JDK 9, интерфейс может включать закрытые методы. 
В результате теперь интерфейс может задавать какое-то поведение. Тем не менее, 
такие методы представляют собой то, что по существу является средствами 
специального назначения, и первоначальный замысел интерфейса по-прежнему 
остается. Поэтому, как правило, вы все еще будете часто создавать и 
использовать интерфейсы, в которых новые средства не применяются. 
Как показывает общая форма, внутри объявлений интерфейса можно объявлять
переменные. Они неявно являются final и static, т.е. не могут изменяться
реализующим классом. Они также должны быть инициализированы.
Все методы и переменные неявно открыты.

Если класс реализует более одного интерфейса, тогда интерфейсы отделяются
друг от друга запятыми. Если класс реализует два интерфейса, в которых
объявлен один и тот же метод, то тот же самый метод будет использоваться
клиентами любого из двух интерфейсов. Методы, реализующие интерфейс,
должны быть объявлены как public. Кроме того, сигнатура типов реализующего
метода должна в точности совпадать с сигнатурой типов, указанной в
определении интерфейса.

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
    default void unBlocking() {
    }
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
            action.computePerimeter(rectShape); // runtime exception.md
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }
    }
}
```

### Доступ к реализациям через ссылки на интерфейсы

Вы можете объявлять переменные как ссылки на объекты, в которых применяется
тип интерфейса, а не тип класса. С помощью переменной подобного
рода можно ссылаться на любой экземпляр любого класса, реализующего
объявленный интерфейс. При вызове метода через одну из таких ссылок корректная
версия будет вызываться на основе фактического экземпляра реализации
интерфейса, на который осуществляется ссылка. Это одна из ключевых
особенностей интерфейсов. Поиск метода, подлежащего выполнению,
производится динамически во время выполнения, что позволяет создавать
классы позже, чем код, вызывающий их методы. Диспетчеризация методов в
вызывающем коде возможна через интерфейс, не требуя каких-либо знаний
о "вызываемой стороне". Такой процесс аналогичен использованию ссылки на
супер класс для доступа к объекту подкласса.

```java
public interface Callback {
    void call(int param);
}

public class Client implements Callback {
    public void call(int p) {
        System.out.println("Client called with " + p);
    }
    public void nonIfaceMeth(){
      System.out.println("nonIfaceMeth");
    }
}

class Code {
    public static void main(String[] args) {
        Callback c = new Client();
        c.call(42);
    }
}

```

Переменная с объявлена с типом интерфейса Callback, хотя ей был присвоен 
экземпляр класса Client. Хотя переменную с можно использовать для доступа к 
методу callback(), через нее не удастся получить доступ ни к каким другим 
членам класса Client. Переменной ссылки на интерфейс известны только методы,
присутствующие в ее объявлении interface. Таким образом, переменную с нельзя 
применять для доступа к методу nonIfaceMeth(), поскольку он определен в Client, 
а не в Callback.

### Частичные реализации

Если класс включает интерфейс, но не полностью реализует методы, требуемые
этим интерфейсом, то такой класс должен быть объявлен абстрактным.
Этот абстрактный класс не реализует метод из интерфейса, но класс наследующий
этот класс обязан реализовать метод из интерфейса.

### Вложенные интерфейсы

Интерфейс может быть объявлен членом класса или друrого интерфейса.
Такой интерфейс называется членом-интерфейсом или вложенным интерфейсом.
Вложенный интерфейс может быть объявлен как public, private или
protected. Он отличается от интерфейса верхнего уровня, который должен
быть либо объявлен как public, либо использовать стандартный уровень доступа,
как было описано ранее. Когда вложенный интерфейс применяется
за пределами своей области видимости, то он должен быть уточнен именем
класса или интерфейса, членом которого является. Таким образом, вне класса
или интерфейса, где объявлен вложенный интерфейс, его и  мя должно быть
полностью уточненным.

```java
class A {
    public interface NestedIf {
        boolean isNotNegative(int x);
    }
}

class B implements A.NestedIf {
    @Override
    public boolean isNotNegative(int x) {
        return x < 0 ? false : true;
    }
}

class Demo {
    public static void main(String[] args) {
        A.NestedIf nif = new B();
        if (nif.isNotNegative(1O))
            System.out.println(" 10 не является отрицательным");
        if (nif.isNotNegative(-12))
            System.out.println("Это выводиться не будет");
    }
}

```

### Переменные в интерфейсах

Интерфейсы можно использовать для импортирования общих констант
в несколько классов, просто объявив интерфейс, который содержит переменные,
инициализированные нужными значениями. Когда такой интерфейс
включается в класс (т.е. когда интерфейс "реализуется"), имена всех этих
переменных будут находиться в области видимости как константы. Если интерфейс
не содержит методов, то любой класс, включающий такой интерфейс,
на самом деле ничего не реализует. Результат оказывается таким же, как если
бы класс импортировал константные поля в пространство имен класса как
переменные final.

### Стандартные методы интерфейса

Используя стандартный метод, метод интерфейса может предоставлять тело, а не
быть абстрактным.

### Методы default и static в интерфейсах

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

Статический метод вызывается классическим способом, без реализации содержащего
его интерфейса:

    Service.action();

Статические методы интерфейса не наследуются ни реализующим классом, ни
производными интерфейсами.

### Закрытые методы интерфейса

Интерфейс способен содержать закрытый метод, который может вызываться только
стандартным методом или другим закрытым методом, определенным в том же
интерфейсе. Поскольку закрытый метод интерфейса указан как private, его нельзя
использовать в коде вне интерфейса, в котором он определен. Такое ограничение
распространяется и на производные интерфейсы, потому что закрытый метод
интерфейса ими не наследуется.
Основное преимущество закрытого метода интерфейса заключается в том,
что он позволяет двум или более стандартным методам использовать общий
фрагмент кода, позволяя избежать дублирования кода.

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