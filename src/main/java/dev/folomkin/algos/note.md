# Алгоритмы коллекций.

Коллекции содержат два класса утилит:

- java.util.Arrays;
- java.util.Collections;

#### **java.util.Arrays**

### Сортировка

**Arrays.sort():**

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

### ___Comparable\<T>___

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


### Поиск
#### Arrays.binarySearch

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
- static \<T> int binarySearch(T[] a, T key, int fromIndex, int toIndex, Comparator<? super T> c);

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