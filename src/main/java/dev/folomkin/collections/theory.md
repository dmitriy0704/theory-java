# ___Коллекции___

__Иерархия колллекций:__

    Iterable
        - Collections
            - List
            - Set
            - Queue

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

## ___Интерфейс Collections___

**Основные подинтерфейсы:**

- **List\<E>** - список - моделирует массив изменяемого размера, для которого
  разрешаается доступ по индексу.
  Может содержать повторяющиеся элементы. Часто используемые реализации:
  ArrayList, LinkedList, Vector и Stack.
- **Set\<E>** - множество, моделирует математическое множество.
  Повторяющиеся элементы не допустимы.
  Часто используемые реализации: HashSet, LinkedHashSet. Подинтерфейс
  SortedSet<E> моделирует отсортированное упорядоченное множество элементов,
  реализованное TreeSet.
- **Queue\<E>** - очередь - моделирует очереди, организованные по принципу
  First In First Out(FIFO) - первым вошел - первым вышел. Подинтерфейс Deque<E>
  моделирует очереди, с котороми можно работать с двух концов.
  Реализации включают PriorityQueue, ArrayDeque и LinkedList.

**Основные методы:**

- **int size()** - возвращает количество элементов данной колеекции
- **void clear()** - удаляет все элементы данной коллекции
- **boolean isEmpty()** - возвращает true, если коллекция не пуста
- **boolean add(E element)** - подтверждает. что данная коллекция содержит
  заданный
  элемент
- **boolean remove(Object element)** - Удаляет заданный элемент, если он имеется
  в
  коллекции
- **boolean contains(Object element)** - Возвращает true, если коллекция
  содержит
  заданный элемент

**Методы работы с другой коллекцией:**

- **boolean containsAll(Collection<?> c)** - Проверяет, содержит ли данная
  коллекция
  все элементы заданной коллекции, __**c**__ - коллекция элементы, которой будут
  проверяться на вхождение
- **boolean addAll(Collection<? extends E> c)** - метод добавляет все элементы
  указанной коллекции в конец коллекции Collection типа E или ее подтипам, c -
  коллекция, которая будет добавляться в конец исходной
- **boolean removeAll(Collection<?> c)** - метод для удаления всех элементов
  указанной коллекции
- **boolean retainsAll(Collection<?> c)** - оставляет только те элементы,
  которые
  содержатся в указанной коллекции __**с**__

**Методы сравнения:**

- **boolean equals(Object o)**
- **int hashCode()**

**Методы работы с массивами:**

- **Object[] toArrays()** - возвращает массив, содержащий все элементы данной
  коллекции типа Object[];
- **<T>T[] toArray(T[] a)** - возвращает массив, содержащий все элементы данной
  коллекции заданного типа T; тип возвращаемого массива

## ___Интерфейс Map___

Map<K,V> - принимает два дженерика типа K и V. Повторящися элементы запрещены.
Часто используемые реализации включают HashMap, Hashtable и LinkedHashMap. Их
подинтерфейс SortedMap<K, V> моделирует упорядоченную и отсортированную карту на
основании ключа, реализованного в TreeMap.

### ___Интерфейс List\<E>___

**Методы с указанием индекса:**

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

**Методы работы с дипазоном значений от fromIndex до toIndex(не включая):**

- **List\<E> subList(int start, int end)** - получает набор элементов, которые
  находятся в списке между индексами start и end

**Методы наследуемые из Collection:**

- **int size()**;
- **boolean isEmpty()**;
- **boolean add(E element)**;
- **boolean remove(Object o)**;
- **boolean contains(Object o)**;
- **void clear()**;

### ___ArrayList\<E>___

Массив с изменяющимся размером. Несинхронизированный.  

**Констукторы:**
- **ArrayList** - создает пустой список, размером 10;
- **ArrayList(Collection<? extends E> c)** - создает список из указанной коллекции
- **ArrayList(int initialCapacity)** - создает пустой список с изначально указанным объемом 