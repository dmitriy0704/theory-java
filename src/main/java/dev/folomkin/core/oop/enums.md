# Перечисления

Перечисление как подкласс класса Enum может содержать поля, конструкторы и
методы, реализовывать интерфейсы.  
Каждый элемент enum может использовать методы:

- **static enumType[] values()** - возвращает массив, содержащий все элементы в
  порядке их объявления;
- **static \<T extends Enum<T>>T valueOf(Class<T> enumType, String arg)** -
  создает элемент перечисления, соответсвующий заданному типу и значению
  передаваемой строки;
- **static enumType valueOf(String arg)** - создает элемент перечисления,
  соответсвующий передаваемой строке;
- **int ordinal()** - возвращает позицию элемента перечисления, начиная с нуля,
  это дает возможность сравнения элементов между собой перечисления на
  больше/меньше соответствующими операторами;
- **String name()** — возвращает имя элемента, так же как и toString();
- **int compareTo(T t)** — сравнивает элементы на больше, меньше либо равно

```java
public enum Transport {
    CAR, TRUCK, AIRPLANE
}

class Example {
    public static void main(String[] args) {
        Transport transport = Transport.AIRPLANE;
        String s = switch (transport) {
            case CAR -> "вы выбрали CAR";
            case TRUCK -> "вы выбрали TRUCK";
            case AIRPLANE -> "вы выбрали AIRPLANE";
            default -> "Вы выбрали другое";
        };
    }
}
```

Перечисление является классом, поэтому в его теле можно объявлять, кроме
методов, также поля и конструкторы. Все конструкторы вызываются автоматически
при инициализации любого из элементов. Конструктор не может быть объявлен со
спецификаторами public и protected, так как не вызывается явно и перечисление не
может быть суперклассом. Поля перечисления используются для сохранения
дополнительной информации, связанной с его элементом.  
Метод toString() реализован в классе Enum для вывода элемента в виде строки.
Если переопределить метод toString() в конкретной реализации перечисления, то
можно выводить не только значение элемента, но и значения его полей, то есть
предоставить полную информацию об объекте, как и определяется контрактом метода.
Перечисление может иметь конструкторы, переменные экземпляра и методы, а также
реализовывать интерфейсы.  
На перечисления накладывается целый ряд ограничений. Им запрещено:

- быть суперклассами;
- быть подклассами;
- быть абстрактными;
- быть параметризированными;
- создавать экземпляры, используя ключевое слово new.

## Методы values() и valueOf()

Все перечисления автоматически содержат в себе два предопределенных метода:
values() и valueOf() со следующими общими формами:

- public static _тип-переч_[] values() - возвращает массив, содержащий список
  констант перечисления;
- public static _тип-переч_ valueOf(String str) - возвращает константу
  перечисления, соответствующую строке str переданную в аргументе;

```java
package dev.folomkin.core.oop.type_class.enums;

public enum Transport {
    //  Использование конструктора, переменной экземпляра и метода перечисления
    CAR(100), TRUCK(90), AIRPLANE(950);
    private int speed; // переменная экземпляра;

    // Конструктор
    Transport(int s) { // Конструктор
        speed = s;
    }

    public int getSpeed() { // Метод
        return speed;
    }
}

class EnumDemo2 {
    public static void main(String[] args) {
        dev.folomkin.core.oop.enums.Transport transport;
        System.out.println("Все константы Transport");

        // Метод values;
        dev.folomkin.core.oop.enums.Transport[] transports = dev.folomkin.core.oop.enums.Transport.values();
        for (dev.folomkin.core.oop.enums.Transport t : transports) {
            System.out.println(t);
        }
        System.out.println();

        //  Метод valueOf;
        transport = dev.folomkin.core.oop.enums.Transport.valueOf("TRUCK");
        System.out.println("transport " + transport);
    }
}


```

## Конструкторы, методы, переменные экземпляра и перечисления

Каждая константа перечисления является объектом своего типа перечисления.
Таким образом, в случае определения конструктора для перечисления конструктор
будет вызываться при создании каждой константы перечисления. Кроме того,
каждая константа перечисления имеет собственную копию любых переменных
экземпляра, определенных перечислением.

```java
public enum Transport {
    //  Использование конструктора, переменной экземпляра и метода перечисления
    CAR(100), TRUCK(90), AIRPLANE(950);

    private int speed; // переменная экземпляра;

    // Конструктор
    Transport(int s) { // Конструктор
        speed = s;
    }

    public int getSpeed() { // Метод
        return speed;
    }
}


class EnumDemo3 {
    public static void main(String[] args) {
        Transport transport;
        System.out.println("Скорость равна " + Transport.CAR.getSpeed());
        //      Все скорости:
        for (Transport t : Transport.values()) {
            System.out.println(t.getSpeed());
        }
    }
}
```

## Перечисления унаследованы от Enum

Методы:

- ordinal - позиция константы в списке;
- final int compareTo (enum-type е ) - Порядковые номера двух констант одного и
  того же перечисления можно
  сравнивать. Здесь enum-type задает тип перечисления, а е представляет собой
  константу,
  сравниваемую с вызывающей константой. Не забывайте, что вызывающая
  константа и е должны относиться к одному и тому же перечислению.
  Если вызывающая константа имеет порядковый номер меньше е, то метод
  compareTo ( ) возвращает отрицательное значение. Если два порядковых номера
  совпадают, возвращается ноль. Если вызывающая константа имеет порядковый
  номер больше е, тогда возвращается положительное значение.
- equals() - Константу перечисления можно сравнивать на предмет равенства с
  любым
  другим объектом, используя метод equals ( ) , который переопределяет
  метод equals ( )

---


В Java, доступ к свойствам внутри enum осуществляется так же, как и к членам
обычного класса. Для этого необходимо определить переменные-члены (свойства)
внутри enum, а затем получить доступ к ним, используя экземпляр enum. Также,
можно получить доступ к значениям enum, используя методы values() и valueOf(), а
также ordinal() для получения порядкового номера.

```java
public enum Day {
    MONDAY("Понедельник", 1),
    TUESDAY("Вторник", 2),
    WEDNESDAY("Среда", 3),
    THURSDAY("Четверг", 4),
    FRIDAY("Пятница", 5),
    SATURDAY("Суббота", 6),
    SUNDAY("Воскресенье", 7);

    private final String russianName;
    private final int dayNumber;

    Day(String russianName, int dayNumber) {
        this.russianName = russianName;
        this.dayNumber = dayNumber;
    }

    public String getRussianName() {
        return russianName;
    }

    public int getDayNumber() {
        return dayNumber;
    }

    public static void main(String[] args) {
        Day today = Day.MONDAY;
        System.out.println("Сегодня: " + today.getRussianName() + ", номер дня: " + today.getDayNumber()); // Вывод: Сегодня: Понедельник, номер дня: 1
        System.out.println("Все дни недели: ");
        for (Day day : Day.values()) {
            System.out.println(day.getRussianName() + " - " + day.getDayNumber());
        }
        // Получение enum по значению (строка)
        Day someDay = Day.valueOf("TUESDAY");
        System.out.println("Получен день: " + someDay.getRussianName()); // Вывод: Получен день: Вторник
        // Получение порядкового номера enum
        System.out.println("Порядковый номер WEDNESDAY: " + Day.WEDNESDAY.ordinal()); // Вывод: Порядковый номер WEDNESDAY: 2

    }
}
```

Разъяснение:

1. Определение enum:
   Перечисление Day объявлено с использованием ключевого слова enum.
2. Поля enum:
   Внутри enum определены поля russianName и dayNumber.
3. Конструктор enum:
   Конструктор enum инициализирует значения полей при создании экземпляров enum.
4. Методы доступа:
   Определены геттеры getRussianName() и getDayNumber() для доступа к значениям
   полей.
5. Использование:
   В методе main создается экземпляр Day.MONDAY, и доступ к свойствам
   осуществляется через today.getRussianName() и today.getDayNumber().
6. Метод values():
   Метод values() возвращает массив всех возможных значений enum.
7. Метод valueOf():
   Метод valueOf() позволяет получить enum по его имени, представленному в виде
   строки.
8. Метод ordinal():
   Метод ordinal() возвращает порядковый номер enum, начиная с 0.
   В целом, enum в Java предоставляют удобный способ работы с ограниченным
   набором предопределенных значений, а также возможность хранить и получать
   доступ к дополнительной информации, связанной с этими значениями, как это
   делается с обычными классами.