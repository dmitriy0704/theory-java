# Алгоритмы. Стандартные и практические.

## Коллекции.

### Дубликаты.

**Есть ли дубликаты?**

```java
public class ExampleCode {
    public static void main(String[] args) {
        int[] arr = {1, 2, 4, 4, 5};
        boolean res = testCode(arr);
    }

    public static boolean testCode(int[] nums) {
        Set<Integer> set = new HashSet<>();
        for (int num : nums) {
            if (set.contains(num)) {
                return false;
            }
            set.add(num);
        }
        return true;
    }
}
```

**Показать дубликат**

```java
public class ExampleCode {
    public static void main(String[] args) {
        int[] arr = {1, 2, 4, 6, 7, 8, 4, 5};
        int res = testCode(arr);
        System.out.println(res);
    }

    public static int testCode(int[] nums) {
        Set<Integer> set = new HashSet<>();
        for (int num : nums) {
            if (set.contains(num)) {
                return num;
            }
            set.add(num);
        }
        return 0;
    }
}

```

**Умножение элементов массива**

```java
public static int test(int[] x) {
    return Arrays.stream(x).reduce(1, (a, b) -> a * b);
}
```

**Сортировка Set/Map?**

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