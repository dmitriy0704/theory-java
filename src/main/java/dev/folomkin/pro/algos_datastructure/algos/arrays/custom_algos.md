# Алгоритмы массивов

## Перебор элементов массива

```java
public class Code {
    public static void main(String[] args) {
        int[] arr = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9};

        for (int i = 0, j = 1; i < arr.length; i++, j++) {
            arr[i] = j;
        }

        // Значения в прямом порядке
        for (int i = 0; i < arr.length; i++) {
            System.out.println(arr[i]);
        }
        System.out.println("----------------------");
        //Значения в обратном порядке
        for (int i = arr.length - 1; i >= 0; i--) {
            System.out.println(arr[i]);
        }
    }
}
```

## Проверка наличие элемента в массиве

```java
class Solution {
    public static void main(String[] args) {
        int[] arr = {10, 5, 6, 1, 3};
        System.out.println(search(arr, 5));
    }

    public static int search(int[] arr, int key) {
        int index = -1;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == key) {
                index = i;
                break;
            }
        }
        return index;
    }
}
```

## Переворачивание и перемешивание массива

**Переворачивание**

Вначале переменной i присваиваем индекс первого элемента, а переменной j —
индекс последнего элемента. На каждой итерации цикла будем увеличивать значение
переменной i и уменьшать значение переменной j. Цикл будет выполняться,
пока i меньше j. На каждой итерации цикла мы просто меняем местами значения
двух элементов массива, предварительно сохраняя значение в промежуточной
переменной tmp.

```java
public class Code {
    public static void main(String[] args) {
        int[] arr = {1, 2, 3, 4, 5};
        reverse(arr);
        System.out.println(Arrays.toString(arr)); // [5, 4, 3, 2, 1]
        reverse(arr);
        System.out.println(Arrays.toString(arr)); // [1, 2, 3, 4, 5]
    }

    public static void reverse(int[] arr) {
        int tmp = 0;
        for (int i = 0, j = arr.length - 1; i < j; i++, j--) {
            tmp = arr[i];
            arr[i] = arr[j];
            arr[j] = tmp;
        }
    }
}
```

**Перемешивание**

```java
public class Code {
    public static void main(String[] args) {
        int[] arr = {1, 2, 3, 4, 5};
        shuffle(arr);
        System.out.println(Arrays.toString(arr)); // [2, 4, 1, 5, 3]
        shuffle(arr);
        System.out.println(Arrays.toString(arr)); // [3, 1, 5, 2, 4]
    }

    public static void shuffle(int[] arr) {
        int tmp = 0, j = 0;
        for (int i = 0; i < arr.length; i++) {
            j = (int) (Math.random() * arr.length);
            if (i == j) continue;
            tmp = arr[i];
            arr[i] = arr[j];
            arr[j] = tmp;

        }
    }
}
```

## Заполнение массива случайными числами

```java
public class Code {
    public static void main(String[] args) {
        int[] arr = new int[5];
        fillRandom(arr, 101);
        System.out.println(Arrays.toString(arr));
        fillRandom(arr, 11);
        System.out.println(Arrays.toString(arr));
    }

    public static void fillRandom(int[] arr, int n) {
        Random r = new Random();
        for (int i = 0; i < arr.length; i++) {
            arr[i] = r.nextInt(n);
        }
    }
}
```

## Копирование элементов из одного массива в другой

`System.arraycopy()`
Этот статический метод класса System создан специально для копирования массива.
Копирует массив из указанного исходного массива, начиная с указанной позиции, в
указанную позицию целевого массива.

```java
System.arraycopy(array1, 0,array2, 0,array1.length);

```

## Умножение элементов массива

```java
public static int test(int[] x) {
    return Arrays.stream(x).reduce(1, (a, b) -> a * b);
}
```

## Дубликаты в массивах

Для поиска дубликатов в массиве на Java можно использовать несколько подходов.
Один из самых простых и эффективных способов — использовать HashSet, который
позволяет хранить уникальные элементы и быстро проверять наличие дубликатов.

Вот пример алгоритма поиска дубликатов в массиве с использованием HashSet:

```java
import java.util.HashSet;

public class DuplicateFinder {
    public static void main(String[] args) {
        int[] array = {1, 2, 3, 4, 5, 2, 6, 7, 3, 8};

        findDuplicates(array);
    }

    public static void findDuplicates(int[] array) {
        HashSet<Integer> seen = new HashSet<>();
        HashSet<Integer> duplicates = new HashSet<>();

        for (int num : array) {
            // Если элемент уже был добавлен в seen, добавляем его в duplicates
            if (!seen.add(num)) {
                duplicates.add(num);
            }
        }

        // Выводим найденные дубликаты
        if (duplicates.isEmpty()) {
            System.out.println("Дубликатов не найдено.");
        } else {
            System.out.println("Найденные дубликаты: " + duplicates);
        }
    }
}
```

Объяснение кода:

1. Импортируем HashSet: Мы используем HashSet для хранения уникальных элементов
   и дубликатов.
    2. Метод findDuplicates: Этот метод принимает массив целых чисел и ищет
       дубликаты.
        1. Создаем два HashSet: seen для хранения уникальных элементов и
           duplicates
           для хранения найденных дубликатов.
        2. Проходим по каждому элементу массива. Если элемент уже существует в
           seen,
           добавляем его в duplicates.
3. Вывод результатов: Если duplicates пуст, выводим сообщение о том, что
   дубликатов не найдено. В противном случае выводим найденные дубликаты.

Пример вывода:
Для массива {1, 2, 3, 4, 5, 2, 6, 7, 3, 8} программа выведет:

Найденные дубликаты: [2, 3]
Этот алгоритм имеет временную сложность O(n) и пространственную сложность O(n),
что делает его эффективным для поиска дубликатов в массиве.

**Проверить наличие**

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

## Сумма всех элементов массива

```java

public class Main {
    public static void main(String[] args) {
        int[] arr = new int[]{10, 6, 4, 23, 87, 1, 12, 1004};
        System.out.println(arrSum(arr));
    }

    static int arrSum(int[] arr) {
        int sum = 0;
        for (int i = 0; i < arr.length; i++) {
            sum += arr[i];
        }
        return sum;
    }
}

//  или на js
const sumContiguousArray =

function(ary) {
    //get the last item
	const lastItem = ary[ary.length - 1];
    //Gauss's trick
    return lastItem * (lastItem + 1) / 2;
}
const nums =[1,2,3,4,5];
        const sumOfArray =

sumContiguousArray(nums);
```

## Нахождение элемента по индексу

```java
class GetIntegerArrayElementByIndex {
    public static void main(String[] args) {
        int[] integerArray = {10, 6, 4, 23, 87, 1, 12, 1004};
        int indexOfRequiredElement = 5;
        if (indexOfRequiredElement < integerArray.length) {
            int element = [indexOfRequiredElement];
            System.out.println(element);
        }
    }
}


// ======== //
public class Main {
    public static void main(String[] args) {
        int[] arr = new int[]{10, 6, 4, 23, 87, 1, 12, 1004};
        int index = 4;
        System.out.println(getElementFromIndex(arr, index));
    }

    static int getElementFromIndex(int[] arr, int index) {
        Optional<Integer> el = Optional.of(
                Arrays.stream(arr)
                        .filter(i -> i == index)
                        .findFirst()
                        .getAsInt()
        );
        return el.orElse(-1);
    }
}


```

## Нахождение индекса элемента(Линейный поиск)

```java
package dev.folomkin.algos_datastructure;

public class Code {
    public static void main(String[] args) {

        int[] arr = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        int index = getIndex(arr, 5);
        System.out.println(index);
    }

    static int getIndex(int[] arr, int target) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == target) {
                return i;
            }
        }
        return -1;
    }
}

```

## Нахождение максимального элемента в массиве

````java
class Demo {
    public static void main(String[] args) {
        int[] arr = {1, 2, 3, 4, 5};
        int max = findMax(arr);
    }

    int findMax(arr) {
        let max = arr[0];
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] > max) {
                max = arr[i];
            }
        }
        return max;
    }
}

````



# Многопоточность

## Посчитать сумму в потоке

```java
class ActionCallable implements Callable<Integer> {
    private List<Integer> integers;
    public ActionCallable(List<Integer> integers) {
        this.integers = integers;
    }
    @Override
    public Integer call() throws Exception {
        int sum = 0;
        for (Integer integer : integers) {
            sum += integer;
        }
        return sum;
    }
}


public class Code {
    public static void main(String[] args) {
        ActionCallable actionCallable = new ActionCallable(List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
        try {
            int res = actionCallable.call();
            System.out.println(res);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```