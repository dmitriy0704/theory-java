# Типы классов

## Перечисления

Перечисление создается с применением ключевого слова enum.

Типобезопасные перечисления (typesafe enums) в Java представляют собой классы и
являются подклассами абстрактного класса java.lang. Enum. Вместо слова class при
описании перечисления используется слово enum. При этом объекты перечисления
инициализируются прямым объявлением без помощи оператора new. При инициализации
хотя бы одного перечисления происходит инициализация всех без исключения
оставшихся элементов данного перечисления.

Перечисление как подкласс класса Enum может содержать поля, конструкторы и
методы, реализовывать интерфейсы.  
Каждый элемент enum может использовать методы:

- **static enumType[] values()** - возвращает массив, содержащий все элементы в
  порядке их объявления;
- **static \<T extends Enum<T>>T valueOf(Class<T> enumType, String arg)** -
  создает
  элемент перечисления, соответсвующий заданному типу и значению передаваемой
  строки;
- **static enumType valueOf(String arg)** - создает элемент перечисления,
  соответсвующий передаваемой строке;
- **int ordinal()** - возвращает позицию элемента перечисления, начиная с нуля,
  это
  дает возможность сравнения элементов между собой перечисления на больше/меньше
  соответствующими операторами;
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

### Методы values() и valueOf()

Все перечисления автоматически содержат в себе два предопределенных метода:
values() и valueOf() со следующими общими формами:

- public static _тип-переч_[] values() - возвращает список констант
  перечисления;
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
    dev.folomkin.core.oop.class_type.enums.Transport transport;
    System.out.println("Все константы Transport");

    // Метод values;
    dev.folomkin.core.oop.class_type.enums.Transport[] transports = dev.folomkin.core.oop.class_type.enums.Transport.values();
    for (dev.folomkin.core.oop.class_type.enums.Transport t : transports) {
      System.out.println(t);
    }
    System.out.println();

    //  Метод valueOf;
    transport = dev.folomkin.core.oop.class_type.enums.Transport.valueOf("TRUCK");
    System.out.println("transport " + transport);
  }
}


```

### Конструкторы, методы, переменные экземпляра и перечисления

Что каждая константа перечисления является объектом своего типа перечисления.
Таким образом, в случае определения конструктора для перечисления конструктор
будет вызываться при создании каждой констан- ты перечисления. Кроме того,
каждая константа перечисления имеет собствен- ную копию любых переменных
экземпляра, определенных перечислением.

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


class EnumDemo3 {
  public static void main(String[] args) {
    dev.folomkin.core.oop.class_type.enums.Transport transport;
    System.out.println("Скорость равна " + dev.folomkin.core.oop.class_type.enums.Transport.CAR.getSpeed());
    //      Все скорости:
    for (dev.folomkin.core.oop.class_type.enums.Transport t : dev.folomkin.core.oop.class_type.enums.Transport.values()) {
      System.out.println(t.getSpeed());
    }
  }
}
```

