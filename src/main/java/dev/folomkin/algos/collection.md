# Алгоритмы коллекций.

Коллекции содержат два класса утилит:

- java.util.Arrays;
- java.util.Collections;

## **java.util.Arrays**

### Сортировка - Arrays.sort():

- static void sort(int[] arr) - сортировка заданного массива по возрастанию;
- static void sort(int[] arr, int fromIndex, int toIndex) - сортировка части
  массива от fromI(включительно) до toI(не включая);
- static\<T> void sort(T[] arr, Comparator \<? super T> c);
- static\<T> void sort(T[] arr, int fromIndex, int toIndex, Comparator \<? super
  T> c);

__Comparator и Comparable__

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

#### ___Comparable\<T>___

Интерфейс java.lang.Comparable\<T> указывает, как два объекта должны
сравниваться
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

#### ___Comparator\<T>___

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

# Алгоритмы коллекций

## Упорядочение, сортировка и поиск

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

### Поиск - Arrays.binarySearch

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

### Синхронизированные Collection, List, Set и Map.

Несинхронизированные:

- ArrayList;
- HashSet;
- HashMap;

Синхронизированные:

- Vector;
- HashTable;

Для создания синхронизированного Collection, List, Set, SortedSet, Map,
SortedMap используется статический метод Collection.synchronizedXxx:

- public static <T> Collection<T> synchronizedCollection(Collection<T> c);
- public static <T> List<T> synchronizedList(List<T> list);
- ....

В соответствии со спецификацией JDK API, «чтобы гарантиро- вать последовательный
доступ, критичным является то, что всякий доступ к исходному списку
осуществляется через возвращенный список и что пользователь вручную
синхронизирует возвращенный список, проходя по нему».

```java
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class ExampleCode {
    public static void main(String[] args) {
        List list = Collections.synchronizedList(new ArrayList<>());
        synchronized (list) { // следует организовать синхронизированный блок
            Iterator iterator = list.iterator();
            while (iterator.hasNext()) {
                iterator.next();
            }
        }
    }
}
```

**Сортировка Set/Map**

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