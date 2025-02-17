# Наследование

Отношение между классами, при котором характеристики одного класса(суперкласса)
передаются другому классу (подклассу) без необходимости их повторного
определения, называется **_наследованием_**.

Подкласс наследует поля и методы суперкласса, используя ключевое слово extends.
Класс может также реализовать любое число интерфейсов, используя ключевое слово
implements. Подкласс имеет прямой доступ ко всем открытым переменным и методам
родительского класса, как будто они находятся в подклассе. Исключение составляют
члены класса, помеченные private (во всех случаях) и «по умолчанию» для
подкласса в другом пакете. В любом случае (даже если ключевое слово extends
отсутствует) класс автоматически наследует свойства суперкласса всех классов —
класса Object.

**_Перегрузка или статический полиморфизм_**  
Подкласс дополняет члены суперкласса своими полями и\или методами
и\или переопределяет методы суперкласса. Если имена методов совпадают,
а параметры различаются, то такое явление называется перегрузкой методов
(статическим полиморфизмом).

**_Переопределение или динамический полиморфизм_**  
Если же совпадают имена и параметры методов, то этот механизм называется
динамическим полиморфизмом. То есть в подклассе можно объявить (переопределить)
метод с тем же именем, списком параметров и возвращаемым значением, что и у
метода суперкласса.

Способность ссылки динамически определять версию
переопределенного метода в зависимости от переданного по ссылке типа объекта
называется **_полиморфизмом_**.

```java
class CardAction {
    public void doPayment(double amountPayment) {
        System.out.println("complete from debt card");
    }
}

class CreditCardAction extends CardAction {
    @Override
    public void doPayment(double amountPayment) { // override method
        System.out.println("complete from credit card");
    }

    public boolean checkCreditLimit() { // own method
        return true;
    }
}

public class Code {
    public static void main(String[] args) {
        CardAction action1 = new CardAction();
        CardAction action2 = new CreditCardAction();
        CreditCardAction cc = new CreditCardAction();
        // CreditCardAction cca = new CardAction(); // compile error: class cast
        action1.doPayment(15.5); // method of CardAction
        action2.doPayment(21.2); // polymorphic method: CreditCardAction
        // dc2.checkCreditLimit(); // compile error: non-polymorphic method
        ((CreditCardAction) action2).checkCreditLimit(); // ок
        cc.doPayment(7.0); // polymorphic method: CreditCardAction
        cc.checkCreditLimit(); // non-polymorphic method CreditCardAction
        ((CreditCardAction) action1).checkCreditLimit(); // runtime error: class cast
    }
}

```

Объект по ссылке action1 создается при помощи вызова конструктора класса
CardAction и, соответственно, при вызове метода doPayment() вызывается версия
метода из класса CardAction.
При создании объекта action2 ссылка типа CardAction инициализируется объектом
типа CreditCardAction.
При таком способе инициализации ссылка на суперкласс получает доступ к методам,
переопределенным в подклассе.
При объявлении совпадающих по сигнатуре (имя, тип, область видимости) полей в
суперклассе и подклассах их значения не переопределяются и никак не
пересекаются, т.е. существуют в одном объекте независимо друг от друга.
Для доступа к полям текущего объекта можно использовать указатель **this**, для
доступа к полям суперкласса — указатель **super**.

## Доступ к членам и наследование

Наследование класса не отменяет ограничение закрытого доступа. Таким образом,
даже если подкласс включает в себя все члены своего суперкласса, он не может
получить доступ к тем членам суперкласса, которые были объявлены закрытыми.

### Переменная типа суперкласса может ссылаться на объект подкласса

Ссылочной переменной типа суперкласса можно присваивать ссылку на
объект любого подкласса, производного от данного суперкласса.

```java
public class Box {
    double width;
    double height;
    double depth;

    // Конструктор применяемый для клонирования объекта
    Box(Box ob) {
        width = ob.width;
        height = ob.height;
        depth = ob.depth;
    }

    // Конструктор, используемый в случае указания всех размеров
    Box(double w, double h, double d) {
        width = w;
        height = h;
        depth = d;
    }

    public Box() {
    }

    double volume() {
        return width * height * depth;
    }
}

public class BoxWeight extends Box {
    double weight;

    BoxWeight(double w, double h, double d, double m) {
        weight = w;
        height = h;
        depth = d;
        width = m;
    }
}


public class RefDemo {
    public static void main(String[] args) {
        BoxWeight weightBox = new BoxWeight(3, 5, 7, 8);
        Box plainBox = new Box();
        double vol;

        vol = plainBox.volume();
        System.out.println("weightBox: " + vol);
        System.out.println("weightBox: " + weightBox.weight);
        System.out.println();

        plainBox = weightBox;

        vol = plainBox.volume();
        System.out.println("plainBox: " + vol);
        
        /* Следующий оператор ошибочен, потому что член weight в plainbox
        не определен. */

        // System.out.println("Bec plainbox равен " + plainbox.weight);
    }
}

```

Здесь weightbox является ссылкой на объекты BoxWeight, а plainbox -
ссылкой на объекты Вох. Поскольку BoxWeight - подкласс Вох, переменной
plainЬox разрешено присваивать ссылку на объект weightbox.

Важно понимать, что именно тип ссылочной переменной, а не тип объекта,
на который она ссылается, определяет, к каким членам можно получать
доступ. Другими словами, когда ссылочной переменной типа суперкласса
присваивается ссылка на объект подкласса, то доступ имеется только к тем
частям объекта, которые определены в суперклассе. Вот почему перем енная
plainbox не может получить доступ к weight, даже есл и она ссылается на
объект BoxWeight. Если подумать, то в этом есть смысл, потому что суперклассу
ничего не известно о том, что к нему добавляет подкласс. Поэтому
последняя строка кода в предыдущем фрагменте закомментирована. Ссылка
Вох не может получить доступ к полю weight, т.к. в классе Вох оно не определено.

## Конструкторы и наследование

Конструктор — особого вида метод, который по имени автоматически вызывается при
создании экземпляра класса с помощью оператора new. При корректном
проектировании класса конструктор не должен выполнять никаких
других обязанностей, кроме инициализации полей класса и проверки
непротиворечивости конструирования объекта.

Свойства конструктора:

- Конструктор имеет то же имя, что и класс; вызывается не просто по имени,
  а только вместе с ключевым словом new при создании экземпляра класса.
- Конструктор не возвращает значение, но может иметь параметры и быть
  перегружаемым.
- Конструкторов в классе может быть несколько, но не менее одного.
- Если конструктор в классе явно не определен, то компилятор предоставляет
  конструктор по умолчанию без параметров.
- Если же конструктор с параметрами определен, то конструктор по умолчанию
  становится недоступным, и для его вызова необходимо явное объявление такого
  конструктора.
- Конструктор подкласса при его создании всегда наделяется возможностью
  вызова конструктора суперкласса. Этот вызов может быть явным или неявным и
  располагается только в первой строке кода конструктора подкласса.
- Если конструктору суперкласса нужно передать параметры, то необходим
  явный вызов из конструктора подкласса super(parameters).
- Конструктор может объявляться только со спецификаторами видимости:
  public, private, protected или по умолчанию.
- Конструктор не может быть объявлен как static, final, abstract, synchronized,
  native.
- Если к конструктору добавить возвращаемое значение, то он перестанет
  быть конструктором, а превратится в метод данного класса. Компилятор
  при этом выдаст предупреждение о том, что в классе присутствуют методы
  с таким же именем, как и класс, что является грубым нарушением соглашения о
  написании кода.

### Когда конструкторы выполняются

Когда иерархия классов создана, в каком порядке выполняются конструкторы
классов, образующих иерархию? Например, при наличии подкласса В и
суперкласса А конструктор А выполняется раньше конструктора В или наоборот?
Ответ заключается в том, что в иерархии классов конструкторы завершают
свое выполнение в порядке наследования от суперкласса к подклассу.
Кроме того, поскольку вызов super() должен быть первым оператором, выполняемым
в конструкторе подкласса, такой порядок остается тем же независимо
от того, применяется super() или нет. Если super() не используется,
то будет выполнен стандартный конструктор или конструктор без параметров
каждого суперкласса.

## Модификатор Final

Методы объявленные как final нельзя замещать в подклассах. От класса с
модификатором final нельзя создать подкласс. Константа может быть объявлена как
поле класса, но не проинициализирована. В этом случае она должна быть
проинициализирована в логическом блоке класса, заключенном в {}, или
конструкторе, но только в одном из указанных мест. Значение по умолчанию
константа получить не может в отличие от переменных класса. Константы могут быть
объявлены в методах как локальные или как параметры метода. В обоих случаях
значения таких констант изменять нельзя.

### Классы и методы final

Запрещено переопределять метод в порожденном классе, если в суперклассе он
объявлен со спецификатором final.

Применение final-методов также показательно при разработке конструктора класса.
Процесс инициализации экземпляра должен быть строго определен
и не подвергаться изменениям. Исключить подмену реализации метода, вызываемого в
конструкторе, следует объявлением метода как final, т.е. при этом
метод не может быть переопределен в подклассе. Подобное объявление гарантирует
обращение именно к этой реализации.

```java
class AutenticationService {
    public AutenticationService() {
        authenticate();
    }

    public final void authenticate() {
        //appeal to the database
    }
}
```

## Использование ключевого слова super

Ключевое слово super имеет две основные формы. Первая вызывает конструктор
суперкласса, а вторая служит для доступа к члену суперкласса, который
был сокрыт членом подкласса.

### Использование ключевого слова super для вызова конструкторов суперкласса

Подкласс может вызывать конструктор, определенный в его суперклассе, с
применением следующей формы super:

    suреr(список-аргументов);

Здесь список-аргументов предназначен для указания любых аргументов,
необходимых конструктору в суперклассе. Вызов super() всегда должен
быть первым оператором, выполняемым внутри конструктора подкласса.

```java
public class BoxWeight extends Box {
    double weight;

    BoxWeight(double w, double h, double d, double m) {
        super(w, h, d);
        width = m;
    }
}

```

Конструктор BoxWeight() вызывает super() с аргументами w, h и d, что
приводит к вызову конструктора Вох, который инициализирует поля width,
height и depth с применением этих значений. Класс BoxWeight больше не
инициализирует указанные поля самостоятельно.
Ему нужно инициализировать только уникальное для него поле: weight. Таким
образом, появляется возможность при желании сделать поля width, height и depth в
классе Вох закрытыми.

```java
public class Box {
    private double width;
    private double height;
    private double depth;

    // Конструктор применяемый для клонирования объекта
    Box(Box ob) {
        width = ob.width;
        height = ob.height;
        depth = ob.depth;
    }

    // Конструктор, используемый в случае указания всех размеров
    Box(double w, double h, double d) {
        width = w;
        height = h;
        depth = d;
    }

    // Если размеры не указаны
    public Box() {
        width = -1;
        height = -1;
        depth = -1;
    }

    // Для кубической коробки
    Box(double len) {
        width = height = depth = len;
    }

    // Объем
    double volume() {
        return width * height * depth;
    }
}

public class BoxWeight extends Box {
    double weight; // вес коробки

    // Конструктор для клонирования объекта
    BoxWeight(BoxWeight ob) {
        super(ob);
        this.weight = ob.weight;
    }

    // Конструктор для всех параметров
    BoxWeight(double w, double h, double d, double m) {
        super(w, h, d);
        weight = m;
    }

    // Стандартный конструктор
    BoxWeight() {
        super();
        weight = -1;
    }

    // Для кубической коробки
    BoxWeight(double len, double m) {
        super(len);
        weight = m;
    }
}

public class DemoSuper {
    public static void main(String[] args) {
        BoxWeight boxWeight1 = new BoxWeight(10, 20, 15, 34);
        double vol;
        vol = boxWeight1.volume();
        System.out.println(vol);

    }
}

```

Здесь вызову super() передается объект типа BoxWeight, а не Вох.
Попрежнему вызывается конструктор Вох(Вох оЬ). Как упоминалось ранее,
переменная типа суперкласса может использоваться для ссылки на любой
объект, производный от этого класса, т.е. мы можем передавать конструктору
Вох объект BoxWeight. Конечно же, классу Вох известны только свои члены.

Когда подкласс вызывает super(), он вызывает конструктор своего
непосредственного суперкласса. Таким образом, super() всегда
ссылается на суперкласс непосредственно над вызывающим классом.
Это справедливо даже для многоуровневой иерархии. Кроме того, вызов
super() всегда должен быть первым оператором, выполняемым внутри
конструктора подкласса.

### Использование второй формы ключевого слова super

Вторая форма ключевого слова super действует примерно так же, за исключением
того, что всегда относится к суперклассу подкласса, в котором
задействована.

    suреr.член

Здесь член может быть либо методом, либо переменной экземпляра.
Вторая форма super наиболее применима в ситуациях, когда имена членов
подкласса скрывают члены с тем же именем в суперклассе.

```java
public class A {
    int i;
}

public class B extends A {
    int i;

    B(int a, int b) {
        super.i = a;
        i = b;
    }

    void show() {
        System.out.println("суперкласс: " + super.i);
        System.out.println("подкласс: " + i);
    }
}

public class DemoSuper {
    public static void main(String[] args) {
        B sub = new B(1, 2);
        sub.show();
    }
}

```

### this и super

Главное отличие между this и super в Java в том, что this представляет текущий
экземпляр класса, в то время как super - текущий экземпляр родительского класса.
Внутри класса для вызова своего конструктора без аргументов используется this(),
тогда как super() используется для вызова конструктора без аргументов, или как
его ещё называют, конструктора по умолчанию родительского класса.
Таким способом вызывать можно не только конструктор без аргументов, а и вообще
любой другой конструктор, передав ему соответствующие параметры.

this и super в Java используются для обращения к переменным экземпляра класса и
его родителя. Вообще-то, к ним можно обращаться и без префиксов super и this, но
только если в текущем блоке такие переменные не перекрываются другими
переменными, т.е. если в нем нет локальных переменных с такими же именами, в
противном же случае использовать имена с префиксами придется обязательно, но это
не беда, т.к. в таком виде они даже более читабельны. Классическим примером
такого подхода является использование this внутри конструктора, который
принимает параметр с таким же именем, как и у переменной экземпляра.

### Схожесть this и super

1. И this, и super — это нестатические переменные, соответственно их нельзя
   использовать в статическом контексте, а это означает, что их нельзя
   использовать в методе main. Это приведет к ошибке во время компиляции "на
   нестатическую переменную this нельзя ссылаться из статического контекста". То
   же самое произойдет, если в методе main воспользоваться ключевым словом
   super.
2. И this, и super могут использоваться внутри конструкторов для вызова других
   конструкторов по цепочке,  this() и super() вызывают конструктор без
   аргументов наследующего и родительского классов соответственно.

```java
class A {
    A() {
        System.out.println("Конструктор без аргументов класса A");
    }

    A(String args) { // 3 - шаг
        System.out.println("Конструктор с одним аргументом класса A"); // <- отрабатывает
    }
}

class B extends A {

    B() {
        this(""); // 1 шаг - тут this("") - это конструктор в классе B - B(String args){} - вызывается он
        System.out.println("Конструктор без аргументов класса B");// 5 шаг - <- ход программы возвращается
    }

    B(String args) {
        super("");// 2 шаг - вызывается конструктор наследуемого класса с параметром
        System.out.println("Конструктор с одним аргументом класса B");// 4 шаг - <- отрабатывает по ходу программы
    }
}

public class Code {
    public static void main(String[] args) {
        B b = new B(); // 0 шаг - вызывается конструктор класса B без параметров
    }
}
```

3. Внутри конструктора this и super должны стоять выше всех других выражений, в
   самом начале, иначе компилятор выдаст сообщение об ошибке. Из чего следует,
   что в одном конструкторе не может быть одновременно и this(), и super().

### Различия this и super

1. Переменная this ссылается на текущий экземпляр класса, в котором она
   используется, тогда как super — на текущий экземпляр родительского класса.

2. Каждый конструктор при отсутствии явных вызовов других конструкторов неявно
   вызывает с помощью super() конструктор без аргументов родительского класса,
   при этом у вас всегда остается возможность явно вызвать любой другой
   конструктор с помощью либо this(), либо super().

### Использование super и this

Ключевое слово super применяется для обращения к конструктору суперкласса и для
доступа к полю или методу суперкласса.

    super(parameters); // вызов суперкласса конструктора
    super.id = 42; // ссылка на атрибут суперкласса
    super.getId(); // вызов метода суперклассa

Первая форма super применяется только в конструкторах для обращения к
конструктору суперкласса только в качестве первой строки кода конструктора и
только один раз.

Вторая форма super используется для доступа из подкласса к переменной id
суперкласса.

Третья форма специфична для Java и обеспечивает вызов из подкласса метода
суперкласса, что позволяет избежать рекурсивного вызова в случае, если
вызываемый с помощью super метод переопределен в данном подклассе.

Причем, если в суперклассе этот метод не определен, то будет осуществляться
поиск по цепочке наследования до тех пор, пока он не будет найден. Во всех
случаях с использованием super можно обратиться только к ближайшему суперклассу,
т.е. «перескочить» через суперкласс, чтобы обратиться к его суперклассу,
невозможно.

```java
class Point1D {
    private int x;

    public Point1D(int x) {
        this.x = x;
    }
}

class Point2D extends Point1D {
    private int y;

    public Point2D(int x, int y) {
        super(x);
        this.y = y;
    }
}

class Point3D extends Point2D {
    private int z;

    public Point3D(int x, int y, int z) {
        super(x, y);
        this.z = z;
    }

    public Point3D() {
        this(-1, -1, -1); // вызов конструктора с параметрами
    }
}
```

В классе Point3D второй конструктор для завершения инициализации объекта
обращается к первому конструктору. Такая конструкция применяется в случае, когда
в класс требуется добавить конструктор по умолчанию с обязательным
использованием уже существующего конструктора.

Ссылка this используется, если в методе объявлены локальные переменные с тем же
именем, что и переменные экземпляра класса. Локальная переменная имеет
преимущество перед полем класса и закрывает к нему доступ. Чтобы получить доступ
к полю класса, требуется воспользоваться явной ссылкой this перед именем поля,
так как поле класса является частью объекта, а локальная переменная нет.

Инструкция this() должна быть единственной в вызывающем конструкторе и быть
первой по счету выполняемой операцией, иначе возникает возможность вызова
нескольких конструкторов суперкласса или ветвления при обращении к конструктору
суперкласса. Компилятор выполнять подобные действия запрещает.

