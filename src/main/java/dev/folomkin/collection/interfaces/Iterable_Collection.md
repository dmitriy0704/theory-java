# Коллекции

## Введение

Коллекции — это хранилища или контейнеры, поддерживающие различные способы
накопления и упорядочения объектов с целью обеспечения возможностей эффективного
доступа к ним. Они представляют собой реализацию абстрактных структур данных,
поддерживающих три основные операции:

- добавление нового элемента в коллекцию;
- удаление элемента из коллекции;
- изменение элемента в коллекции.

Примером коллекции является _стек_ (структура LIFO — Last In First Out),
в котором всегда удаляется объект, вставленный последним. Для очереди
(структура FIFO — First In First Out) используется другое правило удаления:
всегда удаляется элемент, вставляемый первым. В абстрактных типах данных
существует несколько видов очередей: двусторонние очереди, кольцевые очереди,
обобщенные очереди, в которых запрещены повторяющиеся элементы. Стеки и очереди
могут быть реализованы как на базе массива, так и на базе связного списка.

Коллекции в языке Java объединены в библиотеке классов java.util и представляют
собой контейнеры для хранения и манипулирования объектами. До появления
Java 2 эта библиотека содержала классы только для работы с простейшими
структурами данных: Vector, Stack, Hashtable, BitSet, а также интерфейс
Enumeration для работы с элементами этих классов. Коллекции, появившиеся
в Java 2, представляют собой общую технологию хранения и доступа к объектам.
Скорость обработки коллекций повысилась по сравнению с предыдущей версией
языка за счет отказа от их потокобезопасности. Поэтому, если объект коллекции
может быть доступен из различных потоков, что наиболее естественно для
распределенных приложений, возможно использование коллекции из Java 1.

В Java 5 в новом пакете java.util.concurrent появились ограниченно
потокобезопасные коллекции, гарантирующие более высокую производительность
в многопоточной среде для конкурирующих потоков. Так как в коллекциях при
практическом программировании хранится набор ссылок на объекты одного типа,
следует обезопасить коллекцию от появления ссылок на другие, не разрешенные
логикой приложения типы. Такие ошибки при использовании нетипизированных
коллекций выявляются на стадии выполнения, что повышает трудозатраты на
исправление и верификацию кода. Поэтому, начиная с версии Java SE 5,
коллекции стали типизированными или generic.

Более удобным стал механизм работы с коллекциями, а именно:

- предварительное сообщение компилятору о типе ссылок, которые будут храниться в
  коллекции, при этом проверка осуществляется на этапе компиляции;
- отсутствие необходимости постоянно преобразовывать возвращаемые по
  ссылке объекты (тип Object) к требуемому типу.

Структура коллекций характеризует способ, с помощью которого программы Java
обрабатывают группы объектов. Так как Object — суперкласс для всех
классов, то в коллекции можно хранить объекты любого типа, кроме базовых.
Коллекции — это динамические массивы, связные списки, деревья, множества,
хэш-таблицы, стеки, очереди.

Все классы коллекций реализуют интерфейсы Serializable, Cloneable (кро-
ме WeakHashMap).

**Иерархия коллекций:**
![collection.jpg](/img/collection/collection.jpg)

## Интерфейсы коллекций

- Iterable
- Collection
- List
- Queue -> Dequeue
- Set -> SortedSet -> NavigableSet
- Map -> SortedMap

## Iterable

Корневой интерфейс, обеспечивает возможность перебирать элементы коллекции.
Объявляет один абстрактный метод _iterator()_ для извлечения объекта,
реализующего интерфейс Iterator.

### Iterator

Интерфейс Iterator\<E> используется для перебора элементов коллекции. Такой
объект позволяет осуществлять навигацию по содержимому коллекции
последовательно, элемент за элементом. Позиции итератора условно располагаются
в коллекции между элементами. В коллекции, состоящей из N элементов, существует
N+1 позиций итератора.

**_Основные методы:_**

- **E next()** - позволяет получить следующий элемент коллекции. Если следующий
  элемент коллекции отсутствует, то метод next() генерирует исключение
  NoSuchElementException;
- **boolean hasNext()** - проверяет наличие следующего элемента, а в случае его
  отсутствия (завершения коллекции) возвращает false. Итератор при этом остается
  неизменным;
- **void remove()** - удаляет текущий элемент, возвращенный последним вызовом
  метода next(). Если метод next() до вызова remove() не вызывался, то будет
  сгенерировано исключение IllegalStateException;

```java
public class Code {
    public static void main(String[] args) {
        final List<String> names = new ArrayList<>(List.of("Java", "Kotlin", "PHP"));
        final Iterator<String> iterator = names.iterator();
        while (iterator.hasNext()) {
            final String name = iterator.next();
            if ("Java".equals(name)) {
                iterator.remove();
            }
        }
        System.out.println(names);// -> [Kotlin, PHP]
    }
}
```

- **void forEachRemaining(Consumer\<? super E> action)** - выполняет действие
  над каждым оставшимся необработанным элементом коллекции.

```java
public static void main(String[] args) {
    List<Integer> list = new ArrayList<>();
    list.add(1);
    list.add(2);
    list.add(3);
    list.add(4);

    Integer y = 3;
    Iterator<Integer> iterator = list.iterator();
    while (iterator.hasNext()) {
        System.out.println(iterator.next());
    }
    // или
    for (Integer i : list) {
        System.out.println(i);
    }
    System.out.println(list);
}
```

### LISTITERATOR

Для доступа к элементам списка может также использоваться специализированный
интерфейс `ListIterator<E>`, который позволяет получить доступ
сразу в необходимую позицию списка вызовом метода listIterator(int index) на
объекте списка. Интерфейс `ListIterator<E>` расширяет интерфейс `Iterator<E>`,
предназначен для обработки списков и их вариаций.

Методы:

- Наличие методов **E previous()**, **int previousIndex()** и
  **_boolean hasPrevious()_** обеспечивает обратную навигацию по списку.
- Метод **_int nextIndex()_** возвращает номер следующего итератора.
- Метод **_void add(E e)_** позволяет вставлять элемент в список текущей
  позиции.
- Вызов метода **_void set(E e)_** производит замену текущего элемента списка
  на объект, передаваемый методу в качестве параметра.

Внесение изменений в коллекцию методами коллекции после извлечения итератора
гарантирует генерацию исключения ConcurrentModificationException из пакета
java.util при попытке использовать итератор позднее модификации коллекции.

Параллельная или конкурентная модификация коллекции методами самой
коллекции и ее итератором приводит к неразрешимой проблеме и заканчивается
генерацией исключения практически всегда. Проблема заключается в том,
что итератор извлек N число позиций, а коллекция изменила число своих
экземпляров, и итератор перестал соответствовать коллекции. Если модификацию
коллекции и работу с итератором нужно выполнять из различных потоков,
то для решения такой задачи используются ограниченно потокобезопасные решения
для коллекций из пакета java.util.concurrent.

При создании класса, содержащего коллекцию элементов, возможны два
способа: агрегация коллекции в качестве поля класса или отношение HAS-A
и наследование от класса, представляющего коллекцию, или отношение IS-A.

HAS-A:

```java
public class OrderType implements Iterable<String> {
    private int orderId;
    private List<String> currencyNames = new ArrayList<>();

    /* SEK, DKK, NOK, CZK,  GBP, EUR, PLN */
    public OrderType(int orderId) {
        this.orderId = orderId;
    }

    public List<String> getCurrencyNames() {
        return List.copyOf(currencyNames);
    }

    // delegated method
    public boolean add(String e) {
        return currencyNames.add(e);
    }

    @Override
    public Iterator<String> iterator() {
        return currencyNames.iterator();
    }
}
```

Отношение IS-A записывается еще проще, но теряется имя currencyNames.
Класс OrderType теперь сам является списком

```java
public class OrderType extends ArrayList<String> {
    private int orderId;

    public OrderType(int orderId) {
        this.orderId = orderId;
    }
}
```

## Интерфейс Collection\<E>

Интерфейс `Collection<E>` расширяет интерфейс `Iterable`, который принимает в
качестве параметра дженерик типа E (читается как коллекция элементов типа E),
является корневым интерфейсом фреймворка «Коллекции». Он определяет общее
поведение для всех классов, например, устанавливает, как добавить или удалить
элемент.

**_Основные подинтерфейсы:_**

- `List<E>` - интерфейс, упорядоченная коллекция, может содержать дубликаты.
  Часто используемые реализации: ArrayList, LinkedList, Vector и Stack.
- `Queue<E>` - очередь, используется для хранения элементов, которые
  обрабатываются в определенном порядке. Организована по принципу First
  In First Out(FIFO) - первым вошел - первым вышел. Элементы добавляются в один
  конец, извлекаются из другого. Подинтерфейс Deque\<E> моделирует очереди,
  с котором можно работать с двух концов. Реализации включают PriorityQueue,
  ArrayDeque и LinkedList.
- `Set<E>` - коллекция, которая не может содержать дубликаты. Часто
  используемые реализации: HashSet, LinkedHashSet. Подинтерфейс SortedSet<E> -
  отсортированное упорядоченное множество элементов, реализованное TreeSet.
- `<Map<K,V>` - не наследуется от Collection, но является неотъемлемой частью
  JCF. Map хранит элементы в форме пар ключ-значение и не может содержать
  дубликаты ключей. Примеры реализации Map включают HashMap, TreeMap и
  LinkedHashMap.

**_ОСНОВНЫЕ МЕТОДЫ COLLECTION:_**

_ДОБАВЛЕНИЕ:_

- `boolean add(E element)` - добавляет и возвращает true, если данная
  коллекция содержит заданный элемент и false, если такой элемент уже есть
  в коллекции. Для List и Queue элементы добавляются в конец списка, для Set
  порядок не гарантируется, для Map добавление происходит в виде ключ-значение.
  `put(K,V)` - для Map.

```java
public static void main(String[] args) {
    List<String> list = new ArrayList<>();
    list.add("Java");

    Set<String> set = new HashSet<>();
    set.add("Java");

    Queue<String> queue = new LinkedList<>();
    queue.add("Java");

    Map<String, String> map = new HashMap<>();
    map.put("Key", "Java");
}
```

_УДАЛЕНИЕ:_

- **boolean remove(Object element)** - удаляет первый найденный элемент, если
  он имеется в коллекции. Для Map элемент удаляется по ключу.
- **default boolean removeIf(Predicate<? super E> filter)** — удаляет все эле-
  менты коллекции в зависимости от условия.
- **boolean removeAll(Collection<E> c)** - удаление всех элементов содержащихся
  в Collection
- **boolean retainAll (Collection<?> с)** - удаляет все элементы кроме...

```java
public static void main(String[] args) {
    List<String> list = new ArrayList<>();
    list.remove("Java");

    Set<String> set = new HashSet<>();
    set.remove("Java");

    Queue<String> queue = new LinkedList<>();
    queue.remove("Java");

    Map<String, String> map = new HashMap<>();
    map.remove("Key");
}
```

_ПОИСК ЭЛЕМЕНТОВ:_

- **boolean contains(Object element)** - возвращает true, если коллекция
  содержит заданный элемент;
- **boolean containsKey/containsValue(Object element)** - для Map

```java
public class Code {
    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        Set<String> set = new HashSet<>();
        Queue<String> queue = new LinkedList<>();
        Map<String, String> map = new HashMap<>();

        boolean existList = list.contains("Java");
        boolean existSet = set.contains("Java");
        boolean existQueue = queue.contains("Java");
        boolean existMap = map.containsKey("Key");
    }
}
```

_РАЗМЕР КОЛЛЕКЦИИ:_

- **int size()** - возвращает количество элементов данной коллекции

```java
public class Code {
    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        Set<String> set = new HashSet<>();
        Queue<String> queue = new LinkedList<>();
        Map<String, String> map = new HashMap<>();

        int sizeOfList = list.size(); // Получаем размер списка
        int sizeOfSet = set.size(); // Получаем размер множества
        int sizeOfMap = map.size(); // Получаем размер map
    }
}


```

- **void clear()** - удаляет все элементы данной коллекции
- **boolean isEmpty()** - возвращает true, если коллекция не пуста

**_Методы работы с другой коллекцией:_**

- **boolean addAll(Collection<? extends E> c)** - метод добавляет все элементы
  указанной коллекции в конец коллекции Collection типа E или ее подтипам, c -
  коллекция, которая будет добавляться в конец исходной;
- **boolean containsAll(Collection<?> c)** - Проверяет, содержит ли данная
  коллекция все элементы заданной коллекции, __**c**__ - коллекция элементы,
  которой будут проверяться на вхождение;
- **boolean removeAll(Collection<?> c)** - метод для удаления всех элементов
  указанной коллекции
- **boolean retainsAll(Collection<?> c)** - оставляет только те элементы,
  которые содержатся в указанной коллекции **с**.

**_Методы сравнения:_**

- **boolean equals(Object o)**
- **int hashCode()**

**_Методы работы с массивами:_**

- **Object[] toArrays()** - копирует все элементы коллекции в массив, возвращает
  массив типа Object[];
- **<T>T[] toArray(T[] a)** - возвращает массив, содержащий все элементы данной
  коллекции заданного типа T; тип возвращаемого массива

**_Методы для Stream API:_**

- **default Stream <E> stream()** - преобразует коллекцию в stream объектов;
- **default Stream<E> parallelStream()** — преобразует коллекцию в параллельный
  stream объектов. Повышает производительность при работе с очень
  большими коллекциями на многоядерных процессорах;

- **void forEach(Consumer<? super T> action)**, **Iterator<T> iterator()** -
  унаследован от интерфейса Iterable<T>.

При работе с элементами коллекции применяются интерфейсы: Iterator<E>,
ListIterator<E>, Map.Entry<K, V> — для перебора коллекции и доступа к объектам
коллекции.

## Временная сложность коллекций

![collections_time.png](../../../../../../../img/collection/collections_time.png)


## Полезные методы Collections

```java
package dev.folomkin.collection.code;

import java.util.*;

class MyComparator implements Comparator<String> {

    @Override
    public int compare(String o1, String o2) {
        return o1.compareToIgnoreCase(o2);
    }
}

public class Code {
    public static void main(String[] args) {

        MyComparator comparator = new MyComparator();

        List<Integer> list = new LinkedList<>();
        list.add(-8);
        list.add(20);
        list.add(-20);
        list.add(8);

        // Создать компаратор с обратным порядком.
        Comparator<Integer> r = Collections.reverseOrder();

        // Сортировать список с использованием созданного компаратора.
        Collections.sort(list, r);

        System.out.print("Список отсортирован в обратном порядке: ");
        for (int i : list) {
            System.out.print(i + " ");
        }

        // Тасовать список.
        Collections.shuffle(list);

        // Отобразить рандомизированный список.
        System.out.print("Список перетасован: ");
        for (int i : list) {
            System.out.print(i + " ");
        }
        System.out.println();
        System.out.println(" Haимeньшee значение: " + Collections.min(list));
        System.out.println("Haибoльшee значение: " + Collections.max(list));


    }
}

```