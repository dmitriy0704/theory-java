# Алгоритмы. Стандартные и практические.

## Коллекции.
### Базовые алгоритмы коллекций.

Коллекции содержат два класса утилит:
- java.util.Arrays;
- java.util.Collections;


#### **java.util.Arrays**





























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