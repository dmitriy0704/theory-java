**Оглавление:**

- Память
    - Стек и куча

# Память

## Стек и куча

### Стек

Стек работает по схеме LIFO (последним вошел, первым вышел). Всякий раз, когда
вызывается новый метод, содержащий примитивные значения или ссылки на объекты,
то на вершине стека под них выделяется блок памяти. Из этого можно сделать
вывод, что стек хранит значения примитивных переменных, создаваемых в методах, а
также ссылки на объекты в куче на которые ссылается метод.

Когда метод завершает выполнение, блок памяти (frame), отведенный для его нужд,
очищается, и пространство становится доступным для следующего метода. При этом
поток выполнения программы возвращается к месту вызова этого метода с
последующим переходом к следующей строке кода.

Основные особенности стека:

- Он заполняется и освобождается по мере вызова и завершения новых методов;
- Переменные в стеке существуют до тех пор, пока выполняется метод в котором они
  были созданы;
- Если память стека будет заполнена, Java бросит исключение
  java.lang.StackOverFlowError;
- Доступ к этой области памяти осуществляется быстрее, чем к куче;
- Является потокобезопасным, поскольку для каждого потока создается свой
  отдельный стек;

### Куча

Эта область памяти используется для динамического выделения памяти для объектов
и классов JRE во время выполнения. Новые объекты всегда создаются в куче, а
ссылки на них хранятся в стеке.

Эти объекты имеют глобальный доступ и могут быть получены из любого места
программы.

Эта область памяти разбита на несколько более мелких частей, называемых
поколениями:

1. Young Generation — область где размещаются недавно созданные объекты. Когда
   она заполняется, происходит быстрая сборка мусора
2. Old (Tenured) Generation — здесь хранятся долгоживущие объекты. Когда объекты
   из Young Generation достигают определенного порога «возраста», они
   перемещаются в Old Generation
3. Permanent Generation — эта область содержит метаинформацию о классах и
   методах приложения, но начиная с Java 8 данная область памяти была
   упразднена.

Основные особенности кучи

- Когда эта область памяти полностью заполняется, Java бросает
  java.lang.OutOfMemoryError
- Доступ к ней медленнее, чем к стеку
- Эта память, в отличие от стека, автоматически не освобождается. Для сбора
  неиспользуемых объектов используется сборщик мусора
- В отличие от стека, куча не является потокобезопасной и ее необходимо
  контролировать, правильно синхронизируя код

Выполнение кода по шагам:

```java
class Person {
    int id;
    String name;

    public Person(int id, String name) {
        this.id = id;
        this.name = name;
    }
}

public class PersonBuilder {
    private static Person buildPerson(int id, String name) {
        return new Person(id, name);
    }

    public static void main(String[] args) {
        int id = 23;
        String name = "John";
        Person person = null;
        person = buildPerson(id, name);
    }
}
```

1. До начала выполнения метода main(), в стеке будет выделено пространство для
   хранения примитивов и ссылок этого метода:
    - примитивное значение id типа int будет храниться непосредственно в стеке;
    - ссылочная переменная name типа String будет создана в стеке, но сама
      строка "John" будет храниться в области, называемой String Pool (является
      частью Кучи);
    - ссылочная переменная person типа Person будет также создана в памяти
      стека, но будет указывать на объект, расположенный в куче;
2. Для вызова конструктора с параметрами Person (int, String) из метода main() в
   стеке, поверх предыдущего вызова метода main(), будет выделен блок памяти,
   который будет хранить:
    - this — ссылка на текущий объект;
    - примитивное значение id;
    - ссылочную переменную name типа String, которая указывает на объект строки
      из пула строк;
3. В методе main дополнительно вызывается метод buildPerson для которого будет
   выделен блок памяти в стеке поверх предыдущего вызова. Этот блок снова
   сохранит переменные способом, описанным выше.
4. Для вновь созданного объекта person типа Person все переменные будут
   сохранены в памяти кучи.




## Автоупаковка-автораспаковка. Оболочки типов.

К оболочкам типов относятся Double, Float, Long, Integer, Short, Byte, Character
и Boolean из пакета java.lang. Перечисленные классы предлагают широкий набор
методов, позволяющих полностью интегрировать примитивные типы в иерархию
объектов Java.
Все оболочки числовых типов унаследованы от абстрактного класса Number. В классе
Number определены методы, которые возвращают значение объекта каждого числового
типа:

- byte byteValue()
- double doubleValue()
- float floatValue()
- int intValue()
- long longValue()
- short shortValue()

Процесс инкапсуляции значения внутри объекта называется _упаковкой_.
Используется метод valueOf().
Процесс извлечения значения из оболочки типа называется _распаковкой_.
Используется метод intValue().

```java
class Wrap {
    public static void main(String[] args) {
        Intger iOb = new Integer.valueOf(100);
        //Вручную упаковать значение 100
        int i = iOb.intValue(); // Вручную распаковать значение из iOb
        System.out.println(i + " " + iOb); // выводит 100 100
    }
}
```

### Основы автоупаковки

Автоупаковка - это процесс, с помощью которого примитивный тип автоматически
инкапсулируется (упаковывается) в эквивалентную ему оболочку типа всякий раз,
когда требуется объект такого типа. Нет необходимости явно создавать объект.
Автораспаковка представляет собой процесс, при котором значение упакованного
объекта автоматически извлекается (распаковывается) из оболочки типа, когда
значение необходимо. Не придется вызывать методы вроде intValue() или
doubleValue().

    Integer i = 100; <-- Автоупковка

