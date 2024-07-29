# ___Коллекции___

__Иерархия колллекций:__
![collection.jpg](/img/collection.jpg)

## ___Iterable___

Объявляет один абстрактный метод _iterator()_ для извлечения объекта,
реализующего интерфейс Iterator.

## ___Iterator___

**Основные методы:**

- hasNext();
- next();
- remove();

__Пример кода:__

```java
public class ExampleStart {
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
}
```

## ___Интерфейс Collection___

Интерфейс Collection<E>, который принимает в качестве параметра дженерик типа
E (читается как «коллекция элементов типа E), является корневым интерфейсом
фреймворка «Коллекции». Он определяет общее поведение для всех классов,
например, устанавливает, как добавить или удалить элемент.

***Основные подинтерфейсы:***

- **List\<E>** - список, моделирует массив изменяемого размера, для которого
  разрешается доступ по индексу. Может содержать повторяющиеся элементы. Часто
  используемые реализации: ArrayList, LinkedList, Vector и Stack.
- **Queue\<E>** - очередь, моделирует очереди, организованные по принципу First
  In First Out(FIFO) - первым вошел - первым вышел. Подинтерфейс Deque<E>
  моделирует очереди, с котором можно работать с двух концов. Реализации
  включают PriorityQueue, ArrayDeque и LinkedList.
- **Set\<E>** - множество, моделирует математическое множество. Повторяющиеся
  элементы не допустимы. Часто используемые реализации: HashSet, LinkedHashSet.
  Подинтерфейс SortedSet<E> моделирует отсортированное упорядоченное множество
  элементов, реализованное TreeSet.

***Основные методы:***

- **int size()** - возвращает количество элементов данной колеекции
- **void clear()** - удаляет все элементы данной коллекции
- **boolean isEmpty()** - возвращает true, если коллекция не пуста
- **boolean add(E element)** - добавляет и возвращает true, если данная
  коллекция содержит заданный элемент;
- **boolean remove(Object element)** - Удаляет заданный элемент, если он имеется
  в коллекции;
- **boolean contains(Object element)** - Возвращает true, если коллекция
  содержит заданный элемент;

***Методы работы с другой коллекцией:***

- **boolean containsAll(Collection<?> c)** - Проверяет, содержит ли данная
  коллекция все элементы заданной коллекции, __**c**__ - коллекция элементы,
  которой будут проверяться на вхождение
- **boolean addAll(Collection<? extends E> c)** - метод добавляет все элементы
  указанной коллекции в конец коллекции Collection типа E или ее подтипам, c -
  коллекция, которая будет добавляться в конец исходной
- **boolean removeAll(Collection<?> c)** - метод для удаления всех элементов
  указанной коллекции
- **boolean retainsAll(Collection<?> c)** - оставляет только те элементы,
  которые содержатся в указанной коллекции **с**.

***Методы сравнения:***

- **boolean equals(Object o)**
- **int hashCode()**

***Методы работы с массивами:***

- **Object[] toArrays()** - возвращает массив, содержащий все элементы данной
  коллекции типа Object[];
- **<T>T[] toArray(T[] a)** - возвращает массив, содержащий все элементы данной
  коллекции заданного типа T; тип возвращаемого массива

## ___Интерфейс Map___

Map<K,V> - принимает два дженерика типа K и V. Повторящися элементы запрещены.
Часто используемые реализации включают HashMap, Hashtable и LinkedHashMap. Их
подинтерфейс SortedMap<K, V> моделирует упорядоченную и отсортированную карту на
основании ключа, реализованного в TreeMap.

**Почему Map не наследуется от Collection?**

Это связано с различиями в их целях и использовании. Интерфейс Collection
представляет собой общие методы для работы с группой объектов, таких как
добавление, удаление и проверка наличия элемента. Он ориентирован на работу с
коллекциями объектов, где каждый объект является элементом коллекции.
Интерфейс Map, с другой стороны, представляет собой отображение ключей на
значения. Он не рассматривает элементы коллекции как отдельные объекты, а
предоставляет доступ к значению, связанному с определенным ключом. Это более
общий и мощный подход, который не сводится к работе с отдельными элементами
коллекции.
Интерфейс Map включает в себя методы для управления парами ключ-значение и
обеспечивает эффективный доступ к значениям по ключу. По этим причинам он не
является подтипом Collection. Однако, классы, реализующие интерфейс Map, часто
предоставляют методы, которые позволяют работать с элементами коллекции или
возвращают представление коллекции ключей, значений или записей (ключ-значение).
Таким образом, хотя Map и Collection предоставляют абстракции для работы с
группой объектов, они решают разные задачи, и поэтому не существует
иерархического отношения наследования между ними.

## ___Интерфейс List\<E>___

***Методы с указанием индекса:***

- **void add(int index, E element)** - добавляет
- **E set(int index, E element)** - заменяет
- **E get(int index)** - извлекает без удаления
- **E remove(int index)** - удаляет последний извлеченный
- **int indexOf(Object obj)** - возвращает индекс первого вхождения объекта obj
  в список. Если объект не найден, то возвращается -1
- **int lastIndexOf(Object obj)** - возвращает индекс последнего вхождения
  объекта obj в список. Если объект не найден, то возвращается -1
- **static \<E> List\<E> of(элементы)** - создает из набора элементов объект
  List
- **void sort(Comparator\<? super E> comp)** - сортирует список с помощью
  компаратора comp
- **ListIterator\<E> listIterator()** - возвращает объект ListIterator для
  обхода элементов списка

***Методы работы с диапазоном значений от fromIndex до toIndex(не включая):***

- **List\<E> subList(int start, int end)** - получает набор элементов, которые
  находятся в списке между индексами start и end

***Методы наследуемые из Collection:***

- **int size()**;
- **boolean isEmpty()**;
- **boolean add(E element)**;
- **boolean remove(Object o)**;
- **boolean contains(Object o)**;
- **void clear()**;

### ___ArrayList\<E>___

Массив с изменяющимся размером. Несинхронизированный.

***Конструкторы:***

- **ArrayList()** - создает пустой список, размером 10;
- **ArrayList(Collection<? extends E> c)** - создает список из указанной
  коллекции
- **ArrayList(int initialCapacity)** - создает пустой список с изначально
  указанным объемом

***Методы:***

- **boolean add(E e)** - добавляет указанный элемент в конец списка;
- **void add(int index, E e)** - вставляет указанный элемент на указанную
  позицию;
- **boolean addAll(Collection<? extends E> c)** – добавляет все элементы
  указанной коллекции в конец данного списка в порядке, определенном итератором
  Iterator данной коллекции;
- **boolean addAll(int index, Collection<? extends E> c)** – добавляет все
  элементы указанной коллекции в список, начиная с указанной позиции;
- **void clear()** – удаляет все элементы из списка;
- **Object clone()** – возвращает копию объекта с теми же значениями полей, что
  у данного экземпляра ArrayList;
- **boolean contains(Object o)** – возвращает true, если данный список содержит
  указанный элемент;
- **void ensureCapacity(int minCapacity)** - увеличивает объем данного
  экземпляра ArrayList, если это необходимо, чтобы удостовериться, что он может
  содержать, по меньшей мере, число элементов, указанное в аргументе
  minCapacity;
- **E get(int index)** – возвращает элемент, находящийся на указаной позиции в
  списке;
- **int indexOf(Object o)** – возвращает индекс первого вхождения указанного
  элемента данного списка или -1, если данный список не содержит указанный
  элемент;
- **boolean isEmpty()** – возвращает true, если данный список не содержит
  элементов;
- **Iterator<E> iterator()** – возвращает итератор для правильного прохода по
  списку;
- **int lastIndexOf(Object o)** – возвращает индекс последнего вхождения
  указанного элемента данного списка или -1, если список не содержит данный
  элемент;
- **ListIterator<E> listIterator()** – возвращает итератор списка (для прохода в
  правильной последовательности);
- **ListIterator\<E> listIterator(int index)** – возвращает итератор списка (для
  прохода в правильной последовательности), начиная с указанной позиции в
  списке;
- **E remove(int index)** – удаляет элемент из указанной позиции в списке;
- **boolean remove(Object o)** – удаляет первый встретившийся указанный элемент
  из списка, если он там имеется;
- **boolean removeAll(Collection<?> c)** – удаляет из списка все элементы,
  которые содержатся в указанной коллекции;
- **protected void removeRange(int fromIndex, int toIndex)** – удаляет из списка
  все элементы, чей индекс находится от fromIndex, включительно, до toIndex, не
  включая его;
- **boolean retainAll(Collection<?> c)** – оставляет в данном списке только те
  элементы, которые содержатся в указанной коллекции;
- **E set(int index, E element)** – заменяет элемент на указанной позиции в
  списке на указанный элемент;
- **int size()** – возвращает количество элементов данного списка;
- **List\<E> subList(int fromIndex, int toIndex)** – возвращает представление
  данного списка в виде подсписка, начиная с элемента с индексом fromIndex,
  включительно, до элемента с индексом toIndex, не включая его;
- **Object[] toArray()** – возвращает массив всех элементов данного списка в
  правильной последовательности (т.е. от первого до последнего элемента);
- **\<T> T[] toArray(T[] a)** – возвращает массив, содержащий все элементы
  данного списка в правильной последовательности (от первого до последнего
  элемента); тип возвращенного массива при выполнении – тот же самый, что и у
  указанного массива;
- **void trimToSize()** – сокращает массив до размера данного экземпляра
  ArrayList, который и будет текущим размером списка (так как при удалении
  элементов размер списка не изменяется).

**Методы, унаследованные из класса java.util.AbstractList:**

- **boolean equals(Object o)** – сравнивает указанный объект с данным списком (
  на предмет равенства);
- **int hashCode()** – возвращает хэш-код для данного списка.

**Методы, унаследованные из класса java.util.AbstractCollection**

- **containsAll** – возвращает true, если данный список содержит все элементы
  указанной коллекции;
- **toString** – возвращает представление коллекции в виде строки символов.
  Строковое представление состоит из списка элементов коллекции в порядке, в
  котором они возвращаются итератором, список заключается в квадратные
  скобки («[]»), соседние элементы разделяются символами «, » (запятая и
  пробел). Элементы преобразуются в строку таким же образом, как и в
  “String.valueOf(Object).

**Методы, унаследованные из класса java.lang.Object:**

- **protected void finalize()** – генерирует исключение Throwable, этот метод
  вызывается, когда Java – сборщик мусора обнаруживает, что на объект нет
  ссылок; подкласс переопределяет метод finalize, чтобы запросить системные
  ресурсы или выполнить другие операции по очистке памяти;
- **public final Class<?> getClass()** – возвращает класс объекта во время
  выполнения, например,
- **public final void notify()** – «просыпается» один поток, который ожидает на
  «мониторе» данный объект; если несколько потоков ожидают данный объект, то для
  «просыпания» выбирается один из них;
- **public final void notifyAll()** – пробуждает все потоки;
- **public final void wait(long timeout)** – генерирует исключение
  InterruptedException. У метода wait() есть три вариации. Один метод wait()
  бесконечно ждет другой поток, пока не будет вызван метод notify() или
  notifyAll() на объекте. Другие две вариации метода wait() ставят текущий поток
  в ожидание на определенное время. По истечении этого времени поток просыпается
  и продолжает работу.

**Методы, унаследованные из интерфейса java.util.List:**

- **boolean containsAll(Collection<?> c)** – возвращает true, если данный список
  содержит все элементы данной коллекции;
- **boolean equals(Object o)** – сравнивает указанный объект со списком в смысле
  их равенства. Возвращает true тогда и только тогда, когда указанный объект
  является также списком, оба списка имеют одинаковый размер и все
  соответствующие пары элементов в обоих списках равны (Два элемента e1 и e2
  считаются равными, если (e1==null ? e2==null : e1.equals(e2)).)
- **int hashCode()** – возвращает значение хэш-кода для данного списка, хэш-код
  для списка определяется как результат следующих вычислений:

  int hashCode= 1;
  for(E e : list)
  hashCode = 31*hashCode + (e == null ? 0 : e.hashCode())

**Преобразование списка в массив - toArray():**

```java
public class ExampleStart {
    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        list.add("A");
        list.add("B");
        list.add("C");
        Object[] strArray = list.toArray();
        System.out.println(Arrays.toString(strArray));
        String[] strArray2 = list.toArray(new String[list.size()]);
        System.out.println(Arrays.toString(strArray2));
    }
}
```

**Представление массива в виде списка:**

```java
public class ExampleStart {
    public static void main(String[] args) {
        String[] strings = {"a", "b", "c", "d"};
        List<String> list = Arrays.asList(strings);
        List<String> list2 = Arrays.asList("fd", "fgfd", "gfgsd");
        System.out.println(list);
    }
}
```

### ___LinkedList\<E>___ - реализация класса для интерфейса List\<E>

LinkedList\<E> является реализацией двусвязного списка для интерфейса List
который работает эффективно как для вставки элементов, так и для удаления,
используя, как издержки, более сложную структуру. LinkedList\<E> также реализует
интерфейсы Queue\<E> и Deque\<E> и может работать с обоих концов очереди. Он
может работать с очередью как по принципу FIFO, так и по принципу LIFO.

### ___Vector\<E>___

Класс *Vector\<E>* реализует увеличивающийся или уменьшающийся массив объектов.
Как и массив, он содержит элементы, доступ к которым возможен по индексу. Однако
размер вектора может расти или уменьшаться в зависимости от добавления или
удаления элементов после того, как Vector уже был создан. Класс вектор является
синхронизированной реализацией интерфейса List.

***Конструкторы:***

- *Vector()* – создает пустой вектор таким образом, что внутренний массив имеет
  размер 10, а его стандартный инкремент объема равен нулю;
- *Vector(Collection<? extends E> c)* – создает вектор, содержащий элементы
  указанной коллекции в порядке, возвращенном итератором;
- *Vector(int initialCapacity)* – создает пустой вектор с указанным начальным
  объемом и со стандартным инкрементом объема, равным нулю;
- *Vector(int initialCapacity, int capacityIncrement)* – создает пустой вектор с
  указанными начальным объемом и инкрементом объема.

***Методы:***

- **void addElement(E obj)** – добавляет указанный компонент в конец вектора,
  увеличивая его размер на 1;
- **boolean removeElement(Object obj)** – удаляет первый, т.е. имеющий
  минимальный индекс, встретившийся аргумент из вектора;
- **void setElementAt(E obj, int index)** – вставляет элемент, представляющий
  собой указанный объект, на определенное индексом место в векторе;
- **public E elementAt(int index)** – возвращает элемент с указанным индексом;
  этот метод идентичен методу get(int), являющемуся частью интерфейса List;
- **public E firstElement()** – возвращает первый элемент (с индексом 0) данного
  вектора;
- **public E lastElement()** – возвращает последний элемент вектора;
- **public void insertElementAt(E obj,int index)** – вставляет указанный объект
  как элемент данного вектора на место, определенное индексом. Каждый элемент
  данного вектора с индексом, большим или равным указанному индексу, получает
  индекс, больший предыдущего на 1.

```
Vector v = new Vector(3, 2);
System.out.println("Начальный размер " + v.size());
System.out.println("Начальный объем " + v.capacity());
v.addElement(1);
v.addElement(2);
v.addElement(3);
v.addElement(4);
System.out.println("Новый объем " + v.capacity());
// Enumeration - доступ к серии элементов одновременно;
Enumeration e = v.elements();
while (e.hasMoreElements()) {
  System.out.println(e.nextElement());
}
```

**Различия между классами ArrayList и Vector:**

| ArrayList                                                                                            | Vector                                                                                                                                                               |
|------------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| ArrayList не синхронизирован                                                                         | Vector синхронизирован                                                                                                                                               |
| ArrayList увеличивает на 50% <br/> текущий размер массива, если число элементов превышает его размер | Vector увеличивает на 100% <br/> текущий размер массива, если число элементов превышает его размер                                                                   |
| ArrayList не является классом-наследником                                                            | Vector является классом наследником                                                                                                                                  |
| ArrayList является быстрым, потому что он не синхронизированный                                      | Vector является медленным, таккак он синхронизирован, т.е. при многопоточности он будет удерживать другие потоки до тех пор, пока не освободит от блокировки объект. |
| ArrayList использует интерфейс Iterator для прохода по элементам                                     | Vector использует интерфейс Enumeration для перемещения по элементам. Но может также использовать и интерфейс Iterator.                                              |

### ___Stack\<E>___

**Stack\<E>** – класс, определенный для структуры данных «стек», организованной
по принципу LIFO (last-in-first-out – последним вошел – первым вышел). Класс
Stack является наследником класса Vector, который является синхронизированным
массивом с изменяющимся размером.

***Методы:***

- **E void push(E element)** – помещает указанный элемент на вершину стека;
- **E pop()** – возвращает и удаляет элемент с вершины стека;
- **E peek()** – возвращает, но не удаляет;
- **boolean empty()** – проверяет, является ли стек пустым;
- **int search(Object obj)** – возвращает расстояние от указанного объекта до
  вершины стека (от 1 для вершины стека) или –1, если элемент не найден.

## ___Упорядочение, сортировка и поиск___

Для упорядочения используются способы:

- коллекция или массив с использованием методов Collections.sort() или
  Arrays.sort(),
  упорядочивающих по заданной спецификации.

```java
public class ExampleStart {
    public static void main(String[] args) {
        int[] numbers = {1, 3, 2, 45, 67, 8, 9, 10};
        // Отсортировать массив
        // Arrays.sort(numbers);
        System.out.println(Arrays.toString(numbers));
        List<Integer> numbersList = new ArrayList<>();
        for (int i = 0; i < numbers.length; i++) {
            numbersList.add(numbers[i]);
        }
        // Или коллекцию
        Collections.sort(numbersList);
        System.out.println(numbersList);
    }
}
```

- использование таких упорядоченных коллекций как SortedSet(TreeSet) и
  SortedMap(TreeMap).

Так же упорядочение можно реализовать:

- Заставить объекты реализовать интерфейс java.lang.Comparable и переопределить
  метод compareTo() для указания порядка сравнения двух объектов;
- создать объект Comparator с методом compare()

### ___Comparable\<T>___

Интерфейс java.lang.Comparable<T> указывает, как два объекта должны сравниваться
в смысле упорядочения. Этот интерфейс определяет один абстрактный метод:

- int compareTo(T o) - возвращает отрицательное целое, ноль или положительное
  целое число, если данный объект меньше, равен или больше заданного.

Этот способ ссылается на естественный порядок сравнения, и метод compareTo()
ссылается на метод естественного сравнения.
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

### ___Comparator\<T>___

***Метод:***

- int compare(T o1, T o2) - возвращает отрицательное целое, ноль или
  положительное целое, если объект меньше, равен или больше указанного.

```java
public class ExampleStart {
    public static class StringComparator implements Comparator<String> {
        @Override
        public int compare(String s1, String s2) {
            return s1.compareToIgnoreCase(s2);
        }
    }

    public static class IntegerComparator implements Comparator<Integer> {
        @Override
        public int compare(Integer s1, Integer s2) {
            return s1 % 10 - s2 % 10;
        }
    }

    public static void main(String[] args) {
        Comparator<String> compStr = new StringComparator();
        Comparator<Integer> compInt = new IntegerComparator();
        // Сортировка и поиск в массиве строк String
        String[] array = {"Hello", "Hi", "HI", "hello"};
        Arrays.sort(array, compStr);
        System.out.println(Arrays.toString(array));
    }
}
```

## ___Queue\<E> - интерфейсы и реализации___

**Queue** - это коллекция, элементы которой добавляются и удаляются по принципу
FIFO.
**Deque** - двусвязанная коллекция. Элементы добавляются и удалятся с обоих
концов.

***Методы:***  
**Вставка в конец очереди:**

- **boolean add(E e)** - вставляет элемент, переданный в параметре, в конец
  очереди, если есть место. Если емкость очереди ограничена и для вставки не
  осталось места, она возвращает исключение IllegalStateException. Функция
  возвращает true при успешной вставке. Метод add() идеален для случаев, когда
  вы уверены, что в очереди есть место, например, в LinkedList или
  PriorityQueue, которые не имеют ограничений по размеру. Этот метод будет
  подходящим решением в указанных условиях.
- **boolean offer(E e)** - вставляет элемент, в случае невозможности вставки
  возвращает
  false, не вызывая исключение.

**Извлечение элемента из начала очереди:**

- **E remove()** - возвращает и удаляет начало очереди, генерирует исключение
  NoSuchElement, если очередь пуста;
- **E element()** - возвращает, но не удаляет начало очереди, генерирует
  исключение NoSuchElement, если очередь пуста;
- **E poll()** - возвращает и удаляет начало очереди или null если очередь
  пуста;
- **E peek()** - извлекает и возвращает, но не удаляет начало очереди или null
  если очередь пуста;

## ___Deque\<E>___

***Методы:***

- **void addFirst(E e)** - добавляет элемент в начало двусторонней очереди;
- **void addLast(E e)** - добавляет элемент в конец двусторонней очереди;
- **boolean offerFirst(E e)** - вставляет элемент начало двусторонней очереди;
- **boolean offerLast(E e)** - вставляет элемент в конец двусторонней очереди;

**Извлечение и удаление:**

- **E removeFirst(E e)** - извлекает и удаляет элемент из начала очереди,
  генерирует исключение, если очередь пуста;
- **E removeLast(E e)** - извлекает и удаляет элемент из конца очереди,
  генерирует исключение, если очередь пуста;
- **E pollFirst(E e)** - извлекает и удаляет элемент из начала очереди,
  возвращает null, если очередь пуста;
- **E pollFirst(E e)** - извлекает и удаляет элемент из конца очереди,
  возвращает null, если очередь пуста;

**Извлечение без удаления:**

- **E getFirst(E e)** - извлекает первый элемент очереди;
- **E getLast(E e)** - извлекает последний элемент очереди;
- **E peekFirst()** - извлекает, но не удаляет начало очереди или null если
  очередь пуста;
- **E peekLast()** - извлекает, но не удаляет начало очереди или null если
  очередь пуста;

**Deque методы кратко:**

![deque-methods.png](/img/deque-methods.png)

**Сравнение Queue и Deque методов**

| Queue Method | Equivalent Deque Method |
|--------------|-------------------------|
| add(e)       | addLast(e)              |
| offer(e)     | offerLast(e)            |
| remove()     | removeFirst()           |
| poll()       | pollFirst()             |
| element()    | getFirst()              |
| peek()       | peekFirst()             |

**Сравнение Stack и Deque методов**

| Stack Method | Equivalent Deque Method |
|--------------|-------------------------|
| push(e)      | addFirst(e)             |
| pop()        | removeFirst()           |
| peek()       | peekFirst()             |

### Реализации для Queue и Deque:

- **PriorityQueue\<E>** - очередь, в которой элементы находятся в заданном
  порядке, а не в соответствии с принципом FIFO;
- **ArrayDeque\<E>** - Queue или Deque, организованные как динамический массив,
  похожий на ArrayList;
- **LinkedList\<E>** - класс LinkedList<E> также реализует интерфейсы Queue<E> и
  Deque<E> в дополнение к интерфейсу List<E>, обеспечивая очередь или дек как
  структуру данных «двусвязный список»;

**Queue\<E> пример:**

```java
public class ExampleStart {
    public static void main(String[] args) {
        Queue<String> queue = new LinkedList<>();

        //offer(), возвращает true или false
        queue.add("Понедельник");
        queue.add("Вторник");
        boolean flag = queue.offer("Среда");
        System.out.println(queue + ", " + flag);

        //add(), генерирует исключение IllegalStateException
        try {
            queue.add("Четверг");
            queue.add("Пятница");
            queue.add("Воскресенье");
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
        System.out.println("Удаляем начало очереди: " + queue.peek()); // <- Тут просто выводим начало очереди.
        String head = null;
        try {
            // для удаления используем метод remove()
            head = (String) queue.remove();

            System.out.println("1) Изъяли " + head + " из очереди");
            System.out.println("Теперь новая голова: " + queue.element());
        } catch (NoSuchElementException e) {
            e.printStackTrace();
        }

        // Удаление элемента из начала очереди с помощью метода poll();
        head = (String) queue.poll();
        System.out.println("2) Изъяли " + head + " из очереди");
        System.out.println("Теперь новая голова: " + queue.peek());

        // Проверяем, содержит ли очередь данный объект
        System.out.println("Содержит ли очередь Воскресенье: "
                + queue.contains("Воскресенье"));
        System.out.println("Содержит ли очередь Понедельник: "
                + queue.contains("Понедельник"));

    }
}
```

## ___Set\<E>___

Интерфейс Set моделирует математическое множество, в котором нет одинаковых
элементов.

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

__Пример HashSet\<E>__

```java

public class ExampleStart {
    public static void main(String[] args) {
        Book book1 = new Book(1, "Война и мир");
        Book book1Dup = new Book(1, "Война и мир");
        Book book2 = new Book(2, "Анна Каренина");
        Book book3 = new Book(3, "Java для чайников");
        Set<Book> set1 = new HashSet<Book>();
        set1.add(book1);
        set1.add(book1Dup);
        set1.add(book1);
        set1.add(book3);
        set1.add(null);
        set1.add(book2);
        System.out.println(set1);
        set1.remove(book1);
        set1.remove(book3);
        System.out.println(set1);
        Set<Book> set2 = new HashSet<>();
        set2.add(book3);
        System.out.println(set2);
        set2.addAll(set1);
        System.out.println(set2);
        set2.remove(null);
        System.out.println(set2);
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

    //Две книги равны, если у них одинаковые id;
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Book)) {
            return false;
        }
        return this.id == ((Book) o).id;
    }

    //Два объекта равны, если они имеют одинаковый хеш-код.
    @Override
    public int hashCode() {
        return id;
    }

}
```

__Пример для LinkedHashSet\<E>__

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

### ___Интерфейсы NavigableSet\<E> и SortedSet\<E>___

Объекты в __SortedSet__ сортируются естественным способом интерфейсом Comparable
или с помощью объекта Comparator или по добавлению методом add().

Интерфейс __NavigableSet\<E>__ является подинтерфейсом множества Set и объявляет
дополнительные методы навигации (нахождение ближайшего элемента):

- **Iterator \<E> descendingIterator():** - возвращает итератор для элементов
  данного множества в убывающем порядке;
- **Iterator \<E> iterator():** - возвращает итератор для элементов
  данного множества в возрастающем порядке;

___Операции для отдельных элементов___

- **E floor(E e)** - возвращает наибольший, меньший, равный заданному
  элементу/null(если такого нет) элемент множества;
- **E ceiling(E e)** - возвращает наименьший, больший, равный заданному, null(
  если такого нет) элемент множества;
- **E lower(E e)** - возвращает наибольший, строго меньший заданного, null(если
  такого нет) элемент множества;
- **E higher(E e)** - возвращает наименьший, строго заданного или null(если
  такого нет) элемент множества;

___Операции над подмножеством___

- **SortedSet\<E> headSet(E toElement)** - возвращает подмножество данного
  множества, состоящее из элементов строго меньших toElement;
- **SortedSet\<E> tailSet(E fromElement)** - возвращает подмножество данного
  множества, состоящее из элементов которые больше или равны fromElement;
- **SortedSet\<E> subSet(E fromElement, E toElement)** - возвращает подмножество
  данного множества, элементы которого находятся в диапазоне от fromElement
  включительно до toElement не включая;

### ___Класс TreeSet\<E> с Comparable___

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

### ___Класс TreeSet\<E> с Comparator___

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

## Map\<K,V>

**Map** - коллекция пар "ключ-значение". Ключ всегда должен быть уникален,
значение нет.

**Интерфейсы и реализации:**

- Map
    - HasMap\<K,V>
    - HashTable\<K,V>
    - EnumMap*\<K,V>
    - LinkedHashMap\<K,V>
- SortedMap
    - TreeMap\<K,V>

**Методы Map\<K,V>:**

- **V get(Object key)** - возвращает значение указанного ключа;
- **V put(K key, V value)** - связывает значение с ключом;
- **boolean containsKey(Object key)** - проверяет наличие ключа;
- **boolean containsValue(Object value)** - проверяет наличие значения;

**Представления карты**

- **Set\<K> keySet()** - возвращает представление карты в виде множества ключей;
- **Collection\<V> values()** - возвращает представление карты в виде коллекции
  значений;
- **Set entrySet()** - представление в виде множества пар ключ-значения;

**Реализации Map:**

- **HashMap\<K,V>** - реализация хэш-таблицы, методы не синхронизированы;
- **TreeMap\<K,V>** - реализация SortedMap в виде дерева;
- **LinkedHashMap\<K,V>** - хэш-таблица со свойствами связанного списка для
  улучшения методов вставки и удаления;
- **HashTable\<K,V>** - реализация синхронизированной хеш-таблицы для интерфейса
  Map\<K,V>, который не допускает null ключей или значения для наследуемых
  методов;

**Пример**

```java
public class ExampleStart {
    public static void main(String[] args) {
        HashMap<String, String> aMap = new HashMap<>();
        aMap.put("1", "Monday");
        aMap.put("2", "Tuesday");
        aMap.put("3", "Wednesday");
        String str1 = aMap.get("1");
        System.out.println(str1);
        String str2 = aMap.get("2");
        System.out.println(str2);
        String str3 = aMap.get("3");
        System.out.println(str3);
        Set<String> keys = aMap.keySet();
        for (String key : keys) {
            System.out.println(key + ": " + aMap.get(key));
        }
    }
}
```

**HashMap:**