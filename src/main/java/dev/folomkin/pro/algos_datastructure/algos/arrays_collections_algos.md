# Алгоритмы фреймворка «Коллекции»

Фреймворк «Коллекции» имеет два класса утилит – java.util.
Arrays и java.util.Collections, которые содержат часто используемые
алгоритмы, такие как сортировка и поиск на массивах и коллекциях.

## Arrays

Массив – это ссылочный тип в Java. Он может содержать как пе-
ременные базовых типов, так и объекты. В Java определены девять
типов массивов – по одному на каждый из базовых типов (byte,
short, int, long, float, double, char, boolean) и один для Object.

### Сравнение массивов - Arrays.equals():

- public static boolean equals(int[] a1, int[] a2);

### Копирование массивов - Arrays.copyOf и Arrays.copyOfRange.

- public static int[] copyOf(int[] original, int[] newLength) - оригинальный
  размер и новая длина.
  Если новая длина меньше, то массив усекается, если больше, то массив
  дополняется нулями.

```java
public class Code {
    public static void main(String[] args) {
        int[] arr1 = {1, 2, 3, 4, 5}, arr2;
        arr2 = Arrays.copyOf(arr1, 7);
        System.out.println(Arrays.toString(arr1));
        System.out.println(Arrays.toString(arr2));
    }
}
```

- public static int[] copyOfRange(int[] original, int from, int to) - копирует
  часть массива от from до to, заполняя нулями если to превосходит длину.

```java
public class Code {
    public static void main(String[] args) {
        int[] arr1 = {1, 2, 3, 4, 5}, arr2;
        arr2 = Arrays.copyOfRange(arr1, 3, arr1.length);
        System.out.println(Arrays.toString(arr1));  // [1, 2, 3, 4, 5]
        System.out.println(Arrays.toString(arr2));  // [4, 5]
    }
}
```

### Заполнение массива - Arrays.fill

- public static int[] fill(int[], int value)
- public static int[] fill(int[], int from, int to, int value)

### Печать массива - Arrays.toString()

- public String toString(int[] arr);

### Преобразование массива в список - Arrays.asList

- public static <T> List<T> asList(T[] a)sort

## java.util.Collections

- public static \<T extends Comparable\<? super T>> void sort(List\<T> list) -
  сортирует список по возрастанию. Объекты должны реализовывать интерфейс
  Comparable.
- public static \<T> void sort(List<T> list, Comparator <? super T> c) -
  сортирует список в порядке определенном компаратором.

Методы sort применимы только к List, а не Queue и Map. Но SortedSet(TreeSet) и
SortedMap(TreeMap) сортируются автоматически.

```java
public class ExampleCode {
    public static void main(String[] args) {
        String[] init = {"Один", "Два", "Три", "Один", "Два", "Три"};
        List<String> list = new ArrayList<>(Arrays.asList(init));
        System.out.println("Начальное значение списка " + list);
        //Сортировка списка
        Collections.sort(list);
        System.out.println("Список после сортировки: " + list);
    }
}
```

## Collections

Класс java.util.Collections содержит большое количество статических методов,
предназначенных для манипулирования коллекциями.

- \<T> void copy(List\<? super T> dest, List\<? extends T> src) — копирует все
  элементы из одного списка в другой;
- boolean disjoint(Collection<?> c1, Collection<?> c2) — возвращает true, если
  коллекции не содержат одинаковых элементов;
- \<T> List \<T> emptyList(), \<K, V> Map \<K, V> emptyMap(), \<T> Set \<T>
  emptySet() — возвращают пустой список, карту отображения и множество
  соответственно;
- \<T> void fill(List\<? super T> list, T obj) — заполняет список заданным
  элементом;
- int frequency(Collection\<?> c, Object o) — возвращает количество вхождений в
  коллекцию заданного элемента;
- \<T> boolean replaceAll(List\<T> list, T oldVal, T newVal) — заменяет все
  заданные элементы новыми;
- void reverse(List<?> list) — «переворачивает» список;
- void rotate(List<?> list, int distance) — Изменяет очередь элементов в
  указанном List на указанном промежутке;
- void shuffle(List<?> list) — перетасовывает элементы списка;
- singleton(T o), singletonList(T o), singletonMap(K key, V value) — создают
  множество, список и карту отображения, позволяющие добавлять только один
  элемент;
- \<T extends Comparable\<? super T>> void sort(List\<T> list),
- \<T> void sort(List\<T> list, Comparator\<? super T> c) — сортировка списка
  естественным порядком и с использованием Comparable или Comparator
  соответственно;
- void swap(List\<?> list, int i, int j) — меняет местами элементы списка,
  стоящие на заданных позициях;
- \<T> List\<T> unmodifiableList(List<? extends T> list) — возвращает ссылку
  на список с запрещением его модификации. Аналогичные методы есть для всех
  коллекций.
- nCopies(int n, T o) - Возвращает неизменяемый List, состоящий из n копий
  указанного объекта.

```java
public static void main(String[] args) {
    List<Integer> list = new ArrayList();
    Collections.addAll(list, 1, 2, 3, 4, 5);
    Collections.shuffle(list);
    System.out.println(list);
    Collections.sort(list);
    System.out.println(list);
    Collections.reverse(list);
    System.out.println(list);
    Collections.rotate(list, 3);
    System.out.println(list);
    System.out.println("min: " + Collections.min(list));
    System.out.println("max: " + Collections.max(list));
    List<Integer> singletonList = Collections.singletonList(777);
    System.out.println(singletonList);
//singletonList.add(21); // runtime error
}
```

## Упорядочение, сортировка и поиск

### Упорядочение

Для упорядочения используются способы:

- коллекция или массив с использованием методов Collections.sort() или
  Arrays.sort(), упорядочивающих по заданной спецификации.

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

### Сортировка

#### Стандартные методы сортировки Java

##### Collections.sort()

```java
public class Demo {
    void demo() {
        List<ObjectName> list = new ArrayList<ObjectName>();
        Collections.sort(list, new Comparator<ObjectName>() {
            public int compare(ObjectName o1, ObjectName o2) {
                return o1.toString().compareTo(o2.toString());
            }
        });
    }
}
```

### Arrays.sort()

```java
public class Demo {
    void demo() {
        ObjectName[] arr = new ObjectName[10];
        Arrays.sort(arr, new Comparator<ObjectName>() {
            public int compare(ObjectName o1, ObjectName o2) {
                return o1.toString().compareTo(o2.toString());
            }
        });
    }
}
```

#### Arrays

- static void sort(int[] arr) - сортировка заданного массива по возрастанию;
- static void sort(int[] arr, int fromIndex, int toIndex) - сортировка части
  массива от fromI(включительно) до toI(не включая);
- static\<T> void sort(T[] arr, Comparator \<? super T> c);
- static\<T> void sort(T[] arr, int fromIndex, int toIndex, Comparator \<? super
  T> c);

#### Collections

Для Map можно привести ключи/значения к виду Collection, переложить в новый List
и отсортировать с помощью Collections.sort. То же делается с Set. Этот метод
конечно же неэффективный, так как потребует полного копирования содержимого.
Эффективный способ – хранить данные уже отсортированными. Для таких реализаций
созданы интерфейсы-наследники SortedSet и SortedMap.
Реализации SortedSet дают линейный порядок множества. Элементы упорядочены по
возрастанию. Порядок либо натуральный (элементы реализуют интерфейс Comparable),
либо его определяет переданный в конструктор Comparator.
Этот интерфейс добавляет методы получения подмножества от указанного элемента (
tailSet), до элемента (headSet), и между двумя (subSet). Подмножество включает
нижнюю границу, не включает верхнюю.
SortedSet расширяется интерфейсом NavigableSet для итерации по порядку,
получения ближайшего снизу (floor), сверху (ceiling), большего (higher) и
меньшего (lower) заданному элемента.
Все те же правила применяются к элементам SortedMap/NavigableMap относительно их
ключей.
Основными реализациями являются TreeSet и TreeMap. Внутри это
самобалансирующиеся красно-чёрные деревья. Их структура и способ балансировки –
вопрос достойный отдельного поста. Другая любопытная реализация из
java.util.concurrent – ConcurrentSkipListMap.

### Поиск

#### Бинарный поиск - Arrays.binarySearch

Массив должен быть отсортирован до применения поиска.

**Методы:**

Для базовых типов:

- static int binarySearch(int[] a, int key)
- static int binarySearch(int[] a, int fromIndex, int toIndex, int key)

Для объектов, реализация Comparable:

- static int binarySearch(Object[] a, int Object)
- static int binarySearch(Object[] a, int fromIndex, int toIndex, Object key)

Поиск объектов-дженериков с использованием заданного Comparator:

- static \<T> int binarySearch(T[] a, T key, Comparator<? super T> c);
- static \<T> int binarySearch(T[] a, T key, int fromIndex, int toIndex,
  Comparator<? super T> c);

```java
public class ExampleCode {
    public static void main(String[] args) {
        int[] intArr = {30, 20, 5, 12, 55};
        //Сортировка
        Arrays.sort(intArr);
        System.out.println("Отсортированный массив:");
        for (int number : intArr) {
            System.out.println(number + " ");
        }
        //Значение для поиска:
        int searchVal = 12;
        int retVal = Arrays.binarySearch(intArr, searchVal);
        System.out.println("Индекс элемента: " + retVal);
    }
}

```

### Бинарный поиск - Collections.binarySearch()

- public static \<T> int binarySearch(List\<? extends Comparable<? super T>>
  list, T key);
- public static \<T> int binarySearch(List\<? extends T> list, T key, T key,
  Comparator<? super T> c);

### Поиск максимального/минимального элемента в коллекции

Возвращает максимальный/минимальный элемент коллекции в соответствии с
естественным порядком ее элементов:

- public static <T extends Object & Comparable<? super T>> T max(Collection<?
  extends T> c);
- public static <T extends Object & Comparable<? super T>> T min(Collection<?
  extends T> c);

Возвращает максимальный/минимальный элемент коллекции в соответствии с
порядком, указанным компаратором:

- public static <T> T max(Collection<Collection? extends T>c, Comparator<? super
  T> comp);
- public static <T> T min(Collection<Collection? extends T>c, Comparator<? super
  T> comp);

===================

## Алгоритмы работы с коллекциями

В Java для работы с коллекциями (из пакета `java.util`) используются различные
алгоритмы, предоставленные в основном классом `Collections` и другими утилитами.
Вот основные алгоритмы и методы для работы с коллекциями:

### 1. **Сортировка**

- **`Collections.sort(List<T> list)`**: Сортирует список в естественном
  порядке (для элементов, реализующих интерфейс `Comparable`).
- **`Collections.sort(List<T> list, Comparator<? super T> c)`**: Сортирует
  список с использованием пользовательского компаратора.
- **Пример**:
  ```java
  List<Integer> list = Arrays.asList(5, 2, 8, 1);
  Collections.sort(list); // [1, 2, 5, 8]
  ```

### 2. **Поиск**

- **`Collections.binarySearch(List<? extends Comparable<? super 
T>> list, T key)`**: Выполняет бинарный поиск в отсортированном списке.
  Возвращает индекс
  элемента или отрицательное значение, если элемент не найден.

- **
  `Collections.binarySearch(List<? extends T> list, T key, Comparator<? super T> c)`
  **: Бинарный поиск с использованием компаратора.
- **Примечание**: Список должен быть предварительно отсортирован.
- **Пример**:
  ```java
  List<Integer> list = Arrays.asList(1, 2, 5, 8);
  int index = Collections.binarySearch(list, 5); // 2
  ```

### 3. **Перемешивание**

- **`Collections.shuffle(List<?> list)`**: Случайно перемешивает элементы в
  списке.
- **`Collections.shuffle(List<?> list, Random rnd)`**: Перемешивание с заданным
  генератором случайных чисел.
- **Пример**:
  ```java
  List<Integer> list = Arrays.asList(1, 2, 3, 4);
  Collections.shuffle(list); // Например, [3, 1, 4, 2]
  ```

### 4. **Обратный порядок**

- **`Collections.reverse(List<?> list)`**: Изменяет порядок элементов в списке
  на обратный.
- **Пример**:
  ```java
  List<Integer> list = Arrays.asList(1, 2, 3);
  Collections.reverse(list); // [3, 2, 1]
  ```

### 5. **Заполнение**

- **`Collections.fill(List<? super T> list, T obj)`**: Заменяет все элементы
  списка указанным объектом.
- **Пример**:
  ```java
  List<String> list = new ArrayList<>(Arrays.asList("a", "b", "c"));
  Collections.fill(list, "x"); // [x, x, x]
  ```

### 6. **Копирование**

- **`Collections.copy(List<? super T> dest, List<? extends T> src)`**: Копирует
  элементы из исходного списка в целевой. Целевой список должен быть не короче
  исходного.
- **Пример**:
  ```java
  List<Integer> src = Arrays.asList(1, 2, 3);
  List<Integer> dest = new ArrayList<>(Arrays.asList(0, 0, 0, 0));
  Collections.copy(dest, src); // dest: [1, 2, 3, 0]
  ```

### 7. **Минимальный и максимальный элементы**

- **`Collections.min(Collection<? extends T> coll)`**: Находит минимальный
  элемент в коллекции (для элементов, реализующих `Comparable`).
- **`Collections.max(Collection<? extends T> coll)`**: Находит максимальный
  элемент.
- **
  `Collections.min/max(Collection<? extends T> coll, Comparator<? super T> comp)`
  **: С использованием компаратора.
- **Пример**:
  ```java
  List<Integer> list = Arrays.asList(5, 2, 8, 1);
  int min = Collections.min(list); // 1
  int max = Collections.max(list); // 8
  ```

### 8. **Частота и подсчет**

- **`Collections.frequency(Collection<?> c, Object o)`**: Подсчитывает
  количество вхождений элемента в коллекции.
- **Пример**:
  ```java
  List<String> list = Arrays.asList("a", "b", "a", "c");
  int freq = Collections.frequency(list, "a"); // 2
  ```

### 9. **Проверка непересечения**

- **`Collections.disjoint(Collection<?> c1, Collection<?> c2)`**: Проверяет, нет
  ли общих элементов в двух коллекциях.
- **Пример**:
  ```java
  List<Integer> list1 = Arrays.asList(1, 2, 3);
  List<Integer> list2 = Arrays.asList(4, 5, 6);
  boolean disjoint = Collections.disjoint(list1, list2); // true
  ```

### 10. **Синхронизация**

- **`Collections.synchronizedCollection(Collection<T> c)`**: Создает
  синхронизированную (потокобезопасную) версию коллекции.
- **`Collections.synchronizedList(List<T> c)`**, *
  *`Collections.synchronizedSet(Set<T> c)`**, и т.д.
- **Пример**:
  ```java
  List<String> list = Collections.synchronizedList(new ArrayList<>());
  ```

### 11. **Неизменяемые коллекции**

- **`Collections.unmodifiableCollection(Collection<? extends T> c)`**: Создает
  неизменяемую обертку над коллекцией.
- Аналогичные методы: `unmodifiableList`, `unmodifiableSet`, `unmodifiableMap`,
  и т.д.
- **Пример**:
  ```java
  List<String> list = Collections.unmodifiableList(Arrays.asList("a", "b"));
  // list.add("c"); // UnsupportedOperationException
  ```

### 12. **Пакетные операции**

- **`List.replaceAll(UnaryOperator<E> operator)`**: Заменяет каждый элемент
  списка результатом применения функции.
- **`List.removeIf(Predicate<? super E> filter)`**: Удаляет элементы,
  удовлетворяющие условию.
- **Пример**:
  ```java
  List<Integer> list = new ArrayList<>(Arrays.asList(1, 2, 3, 4));
  list.replaceAll(x -> x * 2); // [2, 4, 6, 8]
  list.removeIf(x -> x > 5); // [2, 4]
  ```

### 13. **Stream API (Java 8+)**

Хотя это не часть `Collections`, Stream API предоставляет мощные функциональные
алгоритмы для работы с коллекциями:

- **Фильтрация**: `stream().filter(...)`
- **Сортировка**: `stream().sorted(...)`
- **Маппинг**: `stream().map(...)`
- **Сбор**: `stream().collect(...)`
- **Пример**:
  ```java
  List<Integer> list = Arrays.asList(1, 2, 3, 4);
  List<Integer> result = list.stream()
                             .filter(x -> x % 2 == 0)
                             .map(x -> x * 2)
                             .collect(Collectors.toList()); // [4, 8]
  ```

### 14. **Другие полезные методы**

- **`Collections.swap(List<?> list, int i, int j)`**: Меняет местами элементы по
  указанным индексам.
- **`Collections.rotate(List<?> list, int distance)`**: Циклически сдвигает
  элементы списка.
- **`Collections.nCopies(int n, T o)`**: Создает неизменяемый список из `n`
  копий элемента.
- **Пример**:
  ```java
  List<Integer> list = new ArrayList<>(Arrays.asList(1, 2, 3));
  Collections.swap(list, 0, 2); // [3, 2, 1]
  ```
