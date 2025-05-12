# Компараторы

Чтобы сортировать объекты в коллекции – нужно прописать правила их сравнения.
Для этого в классе, чьи объекты будут сортированы, должен быть интерфейс
Comparable или Comparator.

Comparable – естественная сортировка класса.  
Comparator – используется, когда в классе не реализован Comparable, либо
реализован, но с неподходящей нам логикой сравнения объектов.

Три важных свойства сравнивающей функции
Если сравнивающая функция не удовлетворяет этим свойствам, алгоритм может выдать
совершенно непредсказуемый результат.

- Рефлексивность – сравнение элемента с самим собой всегда возвращает 0
- Антисимметричность – сравнение A с B, и B с A должны дать разный знак.
- Транзитивность – если сравнение A с B, и B с C выдает одинаковый знак, то и
  сравнение A с C должно выдать такой же знак.

## _Comparable_

Интерфейс Comparable накладывает полный порядок на объекты каждого класса,
который его реализует.
Этот порядок называется естественный порядок класса, а метод compareTo() класса
называется его естественным методом сравнения.

Списки и массивы объектов, реализующих этот интерфейс, могут быть автоматически
отсортированы методами Collections.sort() и Arrays.sort().

Объекты, реализующие этот интерфейс, могут использоваться в качестве ключей в
SortedMap или как элементы в SortedSet без необходимости указания Компаратора.

Интерфейс java.lang.Comparable\<T> указывает, как два объекта должны
сравниваться в смысле упорядочения. Чтобы задать свой способ сравнения объектов,
нужно переопределить метод compareTo(), где прописать логику сравнения.
Этот интерфейс определяет один абстрактный
метод:

- int compareTo(T o) - возвращает отрицательное целое, ноль или положительное
  целое число, если данный объект меньше, равен или больше заданного.

```java
class Person implements Comparable<Person> {
    private String fullName;
    private Integer uuid;

    @Override
    public int compareTo(Person person) {
        return uuid - person.uuid;
    }
}
```

Метод compareTo(Object o) сравнивает этот объект с указанным объектом.
Возвращает отрицательное целое число, ноль или положительное целое число, если
этот объект меньше, равен или больше указанного объекта.
В предопределенном методе вместо выражения можно использовать его:

    @Override
    public int compareTo(Person person) {     
        return uuid.compareTo(person.uuid);
    }

Рекомендуется чтобы (x.compareTo(y)==0) был равен (x.equals(y))

Можно сравнивать по нескольким полям класса.
Например, если fullName совпадают – тогда сравнить по uuid

    @Override
    public int compareTo(Person person) {
        int fullNameComparator = fullName.compareTo(person.fullName);
        int uuidComparator = uuid.compareTo(person.uuid);
        return (fullNameComparator != 0 ? fullNameComparator : uuidComparator);
    }

Строго рекомендуется, чтобы метод compareTo() был совместимым с equals() и
hashCode() (наследуемых из java.lang.Object):

1. Если compareTo() возвращает ноль, то equals() должен воз вращать true.
2. Если equals() возвращает true, то hashCode() будет создавать то же int.

Все восемь классов-оболочек базовых типов (Byte, Short, Integer, Long, Float,
Double, Character и Boolean) реализуют интерфейс Comparable с методом
compareTo(), использующим порядок номеров.

__Пример Comparable<T>__

```java
public class ExampleStart {
    public static void main(String[] args) {
        // Сортировка и поиск "массива" строк Strings
        String[] array = {"Hello", "hello", "hi", "HI"};
        // Используем Comparable из String
        Arrays.sort(array);
        System.out.println(Arrays.toString(array));
        //Используем бинарный поиск,
        // для этого массив должен быть отсортирован.
        System.out.println(Arrays.binarySearch(array, "Hello"));
        System.out.println(Arrays.binarySearch(array, "HELLO"));
        // Сортировка и поиск в списке List из целых чисел
        List<Integer> list = new ArrayList<>();
        list.add(2);
        list.add(1);
        list.add(4);
        list.add(3);
        Collections.sort(list); //Используем Comparable для класса Integer
        System.out.println(list);
        System.out.println(binarySearch(list, 2));
    }
}
```

### Сортировка объектов

```java
class AddressBookEntry implements Comparable<AddressBookEntry> {
    private String name, address, phone;

    public AddressBookEntry(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int compareTo(AddressBookEntry another) {
        return this.name.compareToIgnoreCase(another.name);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof AddressBookEntry)) {
            return false;
        }
        return this.name.equalsIgnoreCase(((AddressBookEntry) obj).name);
    }

    @Override
    public int hashCode() {
        return name.length();
    }
}

public class ExampleStart {
    public static void main(String[] args) {
        AddressBookEntry addr1 = new AddressBookEntry("петр");
        AddressBookEntry addr2 = new AddressBookEntry("ПАВЕЛ");
        AddressBookEntry addr3 = new AddressBookEntry("Сергей");
        AddressBookEntry addr4 = new AddressBookEntry("Олег");
        AddressBookEntry addr5 = new AddressBookEntry("Алексей");
        TreeSet<AddressBookEntry> set = new TreeSet<>();
        set.add(addr1);
        set.add(addr2);
        set.add(addr3);
        set.add(addr4);
        set.add(addr5);
        System.out.println(set);
        System.out.println(set.floor(addr2));
        System.out.println(set.lower(addr2));
        System.out.println(set.headSet(addr2));
        System.out.println(set.tailSet(addr2));
    }
}
```

## interface Comparator\<T>

Comparator используется, когда в классе не реализован, либо реализован с
неподходящей логикой интерфейс Comparable, от чего нельзя сравнить объекты
нужным образом.

Comparator представляет собой обобщенный интерфейс, объявленный
следующим образом: `interface Comparator\<T>`

Можно создать отдельный класс компаратор, реализовав в нем Comparator.
Такой класс будет содержать нужную логику сравнения и его можно будет
использовать в аргументах методов, которые требуют Компаратор.

Сравнение чисел делаем через Integer.compare
Или метод Comparator.comparingInt

```java
public class PersonUuidComparator implements Comparator<Person> {

    @Override
    public int compare(Person p1, Person p2) {
        return Integer.compare(p1.uuid - p2.uuid);
    }
}
```

Можно создать несколько Компараторов для класса и использовать нужный.

```java
public class PersonNameComparator implements Comparator<Person> {

    @Override
    public int compare(Person p1, Person p2) {
        return p1.name.compareTo(p2.uuid);
    }
}
```

Можно применять сразу несколько компараторов по принципу приоритета
thenComparing()

```java
var comparator = Comparator.comparing(Person::getName)
        .thenComparingInt(Person::getUuid);

Comparator<Person> personComparator = new PersonNameComparator()
        .thenComparing(new PersonUuidComparator());

```

Comparator – функциональный интерфейс, его можно задать лямбда выражением.
Статический метод Comparator.comparing принимает функцию ключа сортировки и
возвращает компаратор для типа, содержащего ключ сортировки.

```java
class Demo {
    void demo

    {
        Comparator<Person> personUuidComparator = Comparator.comparing(Person::getUuid);
        List<Person> people = new ArrayList<Person>();

        people.sort((person1, person2) -> person1.uuid - person2.uuid());
        people.sort((person1, person2) -> person1.uuid.compareTo(person2.uuid()));
    }
}
```

Для сортировки в обратном порядке reversed()

```java
Comparator<Person> uuidComparator =
        (person1, person2) -> person1.getUuid().compareTo(person2.getUuid());

people.

sort(uuidComparator.reversed());
```

### **Методы:**

- int compare(T obj1, Т obj2) - Здесь obj1 и obj2 - сравниваемые объекты. Обычно
  метод compare()
  возвращает ноль, если объекты равны, положительное значение, если obj1
  больше obj2, и отрицательное значение, если obj1 меньше obj2. Метод может
  сгенерировать исключение ClassCastException, если типы объектов
  несовместимы для сравнения. Реализация compare() позволяете изменить
  способ упорядочения объектов.
- equals() проверяет, равен ли объект вызываемому компаратору:
  `boolean equals(object obj)`
  В obj указывается объект, подлежащий проверке на равенство. Метод возвращает
  true, если obj и вызывающий объект являются экземплярами реализации
  Comparator и используют одно и то же упорядочение, или false в
  противном случае.
- reversed() - можно получить компаратор, изменяющий упорядочение компаратора,
  для которого он вызывается, на противоположное:
  `default Comparator<T> reversed()`. Метод reversed ( ) возвращает компаратор с
  обратным порядком.
- `static <Т extends Comparable<? super Т>> Comparator<T> reverseOrder()` - Он
  возвращает компаратор, который изменяет естественный порядок
  элементов на противоположный.
- И наоборот, вызвав статический метод
  naturalOrder() можно получить компаратор, использующий естественный порядок:
  `static <Т extends Comparable<? super Т>> Comparator<T> naturalOrder()`
- Если необходим компаратор, способный обрабатывать значения null, тогда
  подойдут методы nullsFirst() и nullsLast():
  `static <Т> Comparator< T> nullsFirst (Comparator<? super Т> comp)`
  `static <Т> Comparator< T> nullsLast(Comparator<? super Т> comp)`
  Метод nullsFirst() возвращает компаратор, который трактует значения
  null как меньшие, чем другие значения. Метод nullsLast() возвращает
  компаратор, который трактует значения null как большие, чем другие значения.
  В том и другом случае, если два сравниваемых значения не равны null,
  то сравнение выполняет сотр. Если в сотр передается null, тогда все значения,
  отличающиеся от null, считаются эквивалентными.
- Другим стандартным методом является thenComparing ( ) . Он возвращает
  компаратор, который выполняет второе сравнение, когда результат первого
  сравнения указывает, что сравниваемые объекты равны. Таким образом, его
  можно применять для создания последовательности вида "сравнить по Х, затем
  сравнить по У': Скажем, при сравнении городов первое сравнение может
  иметь дело с их названиями, а второе - с их штатами. Метод thenComparing()
  имеет три формы.  
  Первая позволяет указывать второй компаратор, передавая экземпляр реализации
  Comparator:
  `default Comparator<T> thenComparing (Comparator<? super Т> thenByComp)`
  В thenByComp указывается компаратор, который вызывается, если первое
  сравнение возвращает признак равенства.    
  Следующие версии thenComparing() позволяют указывать стандартный
  функциональный интерфейс Function:

      default <U extends Comparable<? super U> Comparator<T> 
           thenComparing(Function< ? super T, ? extends U> getKey)

      default <U> Comparator<T> 
          thenComparing(Function< ? super Т, ? extends О> getKey, 
          Comparator< ? super О> keyComp)

  В обоих случаях getKey ссылается на функцию, получающую следующий
  ключ сравнения, который используется, если первое сравнение возвращает
  признак равенства. Во второй версии в keyComp указывается компаратор,
  применяемый для сравнения ключей.
- comparing() - возвращает компаратор, который получает свой ключ для сравнения
  из функции, передаваемой методу.

      static <Т, О extends Comparable<? super О>> Comparator<T>
      comparing(Function< ? super т, ? extends О> getKey)

      static <Т, О> Comparator<T>
      comparing(Function< ? super Т, ? extends О> getKey,
      Comparator<? super О> keyComp)

  В обеих версиях getKey ссылается на функцию, которая получает следующий
  ключ для сравнения. Во второй версии в keyComp указывается компаратор,
  используемый для сравнения ключей. Во всех методах getKey ссылается на
  функцию, получающую следующий ключ для сравнения.

### Использование компаратора

```java
// Сортировка в обратном порядке, compare:

class MyComparator implements Comparator<String> {
    @Override
    public int compare(String aStr, String bStr) {
        return bStr.compareTo(aStr);
    }
}


public class Code {
    public static void main(String[] args) {
        // Создать древовидный набор
        TreeSet<String> ts = new TreeSet<String>(new MyComparator());
        ts.add("C");
        ts.add("A");
        ts.add("B");
        ts.add("E");
        ts.add("D");
        ts.add("F");

        for (String s : ts) {
            System.out.println("el: " + s);
        }
        System.out.println();
    }
}


// reversed

class MyComparator implements Comparator<String> {
    @Override
    public int compare(String aStr, String bStr) {
        return aStr.compareTo(bStr);
    }
}

public class Code {
    public static void main(String[] args) {

        // Создать компаратор
        MyComparator comparator = new MyComparator();

        // Или
        Comparator<String> c = (s1, s2) -> s2.compareTo(s1);

        // Или
        TreeSet<String> ts = new TreeSet<String>((as1, as2) -> as2.compareTo(as1));

        TreeSet<String> ts = new TreeSet<String>(comparator.reversed());
        ts.add("C");
        ts.add("A");
        ts.add("B");
        ts.add("E");
        ts.add("D");
        ts.add("F");

        for (String s : ts) {
            System.out.println("el: " + s);
        }
        System.out.println();
    }
}

```

Внутри compare() метод compareTo() класса String сравнивает две
строки. Тем не менее, метод compareTo() вызывается на экземпляре bStr, а
не на aStr, приводя к обратному результату сравнения.

### Сортировка объектов

```java
// Использование компаратора для сортировки счетов по фамилии владельца
class MyComparator implements Comparator<String> {
    @Override
    public int compare(String aStr, String bStr) {
        int i, j, k;

        // Найти индекс, начинающийся с фамилии.
        i = aStr.lastIndexOf(' ');
        j = bStr.lastIndexOf(' ');
        k = aStr.substring(i).compareToIgnoreCase(bStr.substring(j));
        if (k == 0) // Фамилии совпадают, проверить полное имя
            return aStr.compareToIgnoreCase(bStr);
        else
            return k;
    }
}


public class Code {
    public static void main(String[] args) {

        // Создать древовидную карту.
        TreeMap<String, Double> map = new TreeMap<>(new MyComparator());

        // Поместить элементы в карту.
        map.put("John Doe", 3434.34);
        map.put("Tom Smith", 123.22);
        map.put("Jane Baker", 1378.00);
        map.put("Tod Hall", 99.22);
        map.put("Ralph Smith", -19.08);

        //  Получить набор элементов

        Set<Map.Entry<String, Double>> entries = map.entrySet();
        for (Map.Entry<String, Double> entry : entries) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
        System.out.println();

        // Пополнить счет клиента John Doe на 1000.
        double balance = map.get("John Doe");
        map.put("John Smith", balance + 1000);
        System.out.println("Hoвый баланс клиента John Doe : " + map.get("John Doe"));

    }
}

// ========


```java

class PhoneBookEntry {
    public String name, address, phone;

    public PhoneBookEntry(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}

public class ExampleStart {
    public static class PhoneBookComparator
            implements Comparator<PhoneBookEntry> {
        @Override
        public int compare(PhoneBookEntry p1, PhoneBookEntry p2) {
            return p2.name.compareToIgnoreCase(p1.name); //по убыванию от name;
        }
    }

    public static void main(String[] args) {
        PhoneBookEntry addr1 = new PhoneBookEntry("петр");
        PhoneBookEntry addr2 = new PhoneBookEntry("ПАВЕЛ");
        PhoneBookEntry addr3 = new PhoneBookEntry("Сергей");
        PhoneBookEntry addr4 = new PhoneBookEntry("Олег");
        PhoneBookEntry addr5 = new PhoneBookEntry("Алексей");

        Comparator<PhoneBookEntry> comp = new PhoneBookComparator();
        TreeSet<PhoneBookEntry> set = new TreeSet<>(comp);
        set.add(addr1);
        set.add(addr2);
        set.add(addr3);
        set.add(addr4);
        set.add(addr5);
        System.out.println(set);
        Set<PhoneBookEntry> newSet = set.descendingSet();
        System.out.println(newSet);
    }
}

```

Класс компаратора TComp обеспечивает сравнение двух строк, содержащих
имена и фамилии. Сначала сравниваются фамилии, для чего в каждой
строке ищется индекс последнего пробела, а затем сравниваются подстроки
каждого элемента, начинающиеся в этой точке. В случаях, когда фамилии
эквивалентны, сравниваются имена. В итоге получается древовидная карта,
отсортированная по фамилии и в пределах фамилии по имени. Результат легко
заметить, потому что в выводе клиент Ralph Smith находится перед клиентом Тот
Smith.

Предыдущую программу можно переписать так, чтобы карта сортировалась
по фамилии и затем по имени. Такой подход предусматривает применение
метода thenComparing ( } . Вспомните, что метод thenComparing ()
позволяет указать второй компаратор, который будет использоваться, если
вызывающий компаратор возвращает признак равенства. Ниже показано, как
выглядит переделанная программа:

```java
// thenComparing

// Использование компаратора для сортировки счетов по фамилии владельца
class CompLastName implements Comparator<String> {
    @Override
    public int compare(String aStr, String bStr) {
        int i, j;

        // Найти индекс, начала фамилии.
        i = aStr.lastIndexOf(' ');
        j = bStr.lastIndexOf(' ');
        return aStr.substring(i).compareToIgnoreCase(bStr.substring(j));
    }
}

// Сортировать no полному имени, когда фамилии одинаковы.
class CompThenByFirstName implements Comparator<String> {
    @Override
    public int compare(String aStr, String bStr) {
        return aStr.compareToIgnoreCase(bStr);
    }
}


public class Code {
    public static void main(String[] args) {

        // Использовать thenComparing() для создания компаратора, который
        // сравнивает фамилии и затем полные имена, когда фамилии совпадают.
        CompLastName compLN = new CompLastName();
        Comparator<String> compLastThenFirst =
                compLN.thenComparing(new CompThenByFirstName());

        // Создать древовидную карту.
        TreeMap<String, Double> map = new TreeMap<>(compLastThenFirst);

        // Поместить элементы в карту.
        map.put("John Doe", 3434.34);
        map.put("Tom Smith", 123.22);
        map.put("Jane Baker", 1378.00);
        map.put("Tod Hall", 99.22);
        map.put("Ralph Smith", -19.08);

        //  Получить набор элементов
        Set<Map.Entry<String, Double>> entries = map.entrySet();
        for (Map.Entry<String, Double> entry : entries) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
        System.out.println();

        // Пополнить счет клиента John Doe на 1000.
        double balance = map.get("John Doe");
        map.put("John Smith", balance + 1000);
        System.out.println("Hoвый баланс клиента John Doe : " + map.get("John Doe"));

    }
}

```

CompLastNames, сравнивает только фамилии. Второй компаратор,
CompThenByFirstName, сравнивает полное имя, которое включает имя и фамилию,
начиная с имени.

Далее создается объект TreeMap:

        CompLastName compLN = new CompLastName();
        Comparator<String> compLastThenFirst =
        compLN.thenComparing(new CompThenByFirstName());

Основным компаратором является compLN, который представляет собой
экземпляр CompLastNames. Для него вызывается метод thenComparing () с
передачей экземпляра CompThenByFirstName. Результат присваивается компаратору
по имени compLastThenFirst, который применяется для конструирования
объекта TreeMap:

        TreeMap<String, Double> map = new TreeMap<>(compLastThenFirst);

Теперь всякий раз, когда фамилии сравниваемых элементов одинаковы,
для их упорядочивания используется полное имя, начиная с просто имени,
т.е. имена упорядочиваются по фамилии, а внутри фамилий - по имени.
