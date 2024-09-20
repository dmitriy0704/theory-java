
## Set\<E>

Интерфейс Set\<E> объявляет поведение коллекции, не допускающей дублирования
элементов. Интерфейс SortedSet\<E> наследует Set\<E> и объявляет поведение
набора, отсортированного в возрастающем порядке, заранее определенном для
класса. Интерфейс NavigableSet\<E> существенно облегчает поиск элементов,
например, расположенных рядом с заданным. Часто встречающиеся реализации HashSet
и LinkedHashSet. Подинтерфейс SortedSet содержит отсортированные упорядоченные
элементы. Его реализация - TreeSet.

***Методы:***

- **boolean add(E o)** - добавляет указанный элемент в коллекцию, если его еще
  нет;
- **boolean remove(Object o)** - удаляет указанный объект, если он есть
- **boolean contains(Object o)** - возвращает true, если объект присутствует в
  множестве
- **boolean addAll(Collections<? extends E> c)** - создает объединение множеств
- **boolean retainAll(Collection<?> c )** - создает пересечение множеств

Реализации интерфейса Set включают:

- **класс HashSet\<E>** - хранит элементы в хеш-таблице по хеш коду; HashSet
  является самой лучшей реализацией для Set;
- **LinkedHashSet\<E>** - элементы хранятся в виде двусвязного списка, что
  позволяет организовать упорядоченные итерации вставки и удаления; элементы
  хэшируются с использованием метода hashCode() и организованы в связный список
  в соответствии с порядком вставки. В отличие от HashSet, класс LinkedHashSet
  строит связный список с использованием хэш-таблицы для увеличения
  эффективности операций вставки и удаления элементов (за счет более сложной
  структуры). Этот класс поддерживает связный список элементов в том порядке, в
  котором они вставлялись, т.е. в порядке метода add();
- **TreeSet\<E>** - также поддерживает подинтерфейсы NavigableSet и SortedSet;
  хранит элементы в виде структуры «дерево», в которой элементы отсортированы и
  управляемы; это эффективно для поиска, удаления и добавления элементов (оценка
  времени поиска – O(log(n)).

### _HashSet\<E>_

Класс HashSet<E> наследуется от абстрактного суперкласса AbstractSet<E>
и реализует интерфейс Set\<E>, используя хэш-таблицу для хранения коллекции.

Иерархия: java.util.AbstractCollection<E> -> java.util.AbstractSet<E> ->
java.util.HashSet<E>

Ключ хэш-код используется в качестве индекса хэш-таблицы для доступа
к объектам множества, что значительно ускоряет процессы поиска, добавления
и извлечения элемента. Скорость указанных процессов становится заметной
для коллекций с большим количеством элементов. Множество HashSet не
является сортированным. В таком множестве могут храниться элементы с
одинаковыми хэш-кодами в случае, если эти элементы не эквивалентны при
сравнении. Для грамотной организации HashSet стоит следить, чтобы реализации
методов equals() и hashCode() соответствовали правилам.

Конструкторы класса:

- HashSet()
- HashSet(Collection <? extends E> c)
- HashSet(int capacity)
- HashSet(int capacity, float loadFactor),  
  где capacity — число ячеек для хранения хэш-кодов.

```java
public static void main(String[] args) {
    HashSet<String> prog = new HashSet<>();
    prog.add("Java");
    prog.add("Kotlin");
    prog.add("PHP");
    prog.add("Python");
    prog.stream()
            .peek(System.out::println)
            .forEach(s -> System.out.println(" " + s.hashCode()));
    // -> 
    // Java
    //  2301506
    // PHP
    //  79192
    // Kotlin
    //  -2041707231
    // Python
    //  -1889329924
}
```

### LinkedHashSet\<E>

```java
public class ExampleStart {
    public static void main(String[] args) {
        Book book1 = new Book(1, "Анна Каренина");
        Book book1Dup = new Book(1, "Анна Каренина");
        Book book2 = new Book(2, "Война и мир");
        Book book3 = new Book(3, "Java для чайников");
        Set<Book> set = new LinkedHashSet<>();
        set.add(book1);
        set.add(book1Dup);
        set.add(book1);
        set.add(book3);
        set.add(null);
        set.add(null);
        set.add(book2);
        System.out.println(set);
    }
}

class Book {
    private int id;
    private String title;

    public Book(int id, String title) {
        this.id = id;
        this.title = title;
    }

    @Override
    public String toString() {
        return id + ":" + title;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Book)) {
            return false;
        }
        return this.id == ((Book) o).id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
```

### Интерфейсы NavigableSet\<E> и SortedSet\<E>

Объекты в __SortedSet__ сортируются естественным способом интерфейсом Comparable
или с помощью объекта Comparator или по добавлению методом add().

Интерфейс __NavigableSet\<E>__ является подинтерфейсом множества Set и объявляет
дополнительные методы навигации (нахождение ближайшего элемента):

- **Iterator \<E> descendingIterator()** - возвращает итератор для элементов
  данного множества в убывающем порядке;
- **Iterator \<E> iterator()** - возвращает итератор для элементов
  данного множества в возрастающем порядке;

**_Операции для отдельных элементов_**

- **E floor(E e)** - возвращает наибольший, меньший, равный заданному
  элементу/null(если такого нет) элемент множества;
- **E ceiling(E e)** - возвращает наименьший, больший, равный заданному, null(
  если такого нет) элемент множества;
- **E lower(E e)** - возвращает наибольший, строго меньший заданного, null(если
  такого нет) элемент множества;
- **E higher(E e)** - возвращает наименьший, строго заданного или null(если
  такого нет) элемент множества;

**_Операции над подмножеством_**

- **SortedSet\<E> headSet(E toElement)** - возвращает подмножество данного
  множества, состоящее из элементов строго меньших toElement;
- **SortedSet\<E> tailSet(E fromElement)** - возвращает подмножество данного
  множества, состоящее из элементов которые больше или равны fromElement;
- **SortedSet\<E> subSet(E fromElement, E toElement)** - возвращает подмножество
  данного множества, элементы которого находятся в диапазоне от fromElement
  включительно до toElement не включая;

### _Класс TreeSet\<E>_

Класс TreeSet<E> для хранения объектов использует бинарное (красно-черное)
дерево.

Иерархия наследования TreeSet:
java.util.AbstractCollection\<E> ->
java.util.AbstractSet\<E> ->
java.util.TreeSe\<E>

Конструкторы класса:

- TreeSet()
- TreeSet(Collection <? extends E> c)
- TreeSet(Comparator <? super E> c)
- TreeSet(SortedSet <E> s)

При добавлении объекта в дерево он сразу же размещается в необходимую позицию с
учетом сортировки. Сортировка происходит благодаря тому, что класс реализует
интерфейс SortedSet, где правило сортировки добавляемых элементов определяется в
самом классе, сохраняемом в множестве, который в большинстве случаев реализует
интерфейс Comparable. Обработка операций удаления и вставки объектов происходит
несколько медленнее, чем в хэш-множествах, где при любом числе элементов время
этих операций постоянно.

**Методы:**

- **E first() и E last()** - Извлечение первого и последнего (наименьшего и
  наибольшего) элементов:
- **subSet(E from, E to), tailSet(E from) и headSet(E to)** - извлечения
  определенной части множества.
- **Comparator <? super E> comparator()** - возвращает объект Comparator,
  используемый для сортировки объектов множества или null, если выполняется
  обычная сортировка.

```java
  public static void main(String[] args) {
    Set<String> set = Set.of("2Y", "8Y", "6Y", "5Y", "Y-");
    System.out.println(set);
    TreeSet<String> treeSet = new TreeSet<>(set);
    treeSet.add("Y-");
    System.out.println(treeSet);
    System.out.println(treeSet.last() + " " + treeSet.first());
}
```

Множество инициализируется списком и сортируется сразу же в процессе создания. С
помощью итератора элемент может быть найден и удален из множества. Для
множества, состоящего из обычных строк, используется лексикографическая
сортировка, задаваемая реализацией интерфейса Comparable, поэтому метод
comparator() возвращает null.

#### _Comparable_

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

#### Comparator

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

### EnumSet

Абстрактный класс EnumSet<E extends Enum<E>> наследуется от абстрактного класса
AbstractSet.  
java.util.AbstractCollection<E> ->
java.util.AbstractSet<E> ->
java.util.EnumSet<E>

Класс специально реализован для работы с типами enum.
Все элементы такой коллекции должны принадлежать единственному типу enum,
определенному явно или неявно.
Скорость выполнения операций над таким множеством очень высока, даже если в ней
участвует большое количество элементов. Основное назначение множества EnumSet<E>
— это выделение подмножества из полного набора элементов перечисления. Сами
способы создания объекта этого множества указывают на эту особенность. Создать
объект этого класса можно только с помощью статических методов.

**Методы**:

- **EnumSet<E> noneOf(Class<E> elemType)** - создает пустое множество
  нумерованных констант с указанным типом элемента,
- **allOf(Class<E> elementType)** - создает множество нумерованных констант,
  содержащее все элементы указанного типа.
- **of(E first, E… rest)** - создает множество, первоначально содержащее
  указанные элементы.
- **complementOf(EnumSet<E> s)** - создается множество, содержащее все элементы,
  которые отсутствуют в указанном множестве.
- **range(E from, E to)** - создает множество из элементов, содержащихся в
  диапазоне, определенном двумя элементами.

При передаче указанным методам в качестве параметра null будет сгенерирована
исключительная ситуация NullPointerException.
