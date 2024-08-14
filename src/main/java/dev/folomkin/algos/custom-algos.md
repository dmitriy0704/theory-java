# Алгоритмы. Стандартные и практические.

## Массивы

### Проверка наличие элемента в массиве

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

### Переворачивание и перемешивание массива

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

### Заполнение массива случайными числами

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

### Копирование элементов из одного массива в другой

Так как в переменной сохраняется лишь ссылка на массив, это обстоятельство сле­
дует учитывать при использовании оператора присваивания.

```java

```

### Умножение элементов массива

```java
public static int test(int[] x) {
    return Arrays.stream(x).reduce(1, (a, b) -> a * b);
}
```

### Дубликаты в массивах

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
