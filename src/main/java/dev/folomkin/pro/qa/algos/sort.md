# Сортировка

Алгоритмы сортировки — это методы, позволяющие упорядочить элементы в массиве
или коллекции. В Java они широко используются и оптимизированы в стандартных
библиотеках. Рассмотрим основные алгоритмы сортировки, их характеристики,
преимущества и недостатки.

---

## 1. **Сортировка слиянием (Merge Sort)**

**Принцип работы:**
Алгоритм использует метод **разделяй и властвуй**. Он рекурсивно делит массив
пополам, сортирует каждую половину и затем сливает отсортированные половины в
один отсортированный массив.

**Временная сложность:**

- **O(n log n)** — для всех случаев (лучший, худший, средний).

**Преимущества:**

- Хорошо работает с большими данными.
- Сложность **O(n log n)** гарантируется всегда.
- Стабильный алгоритм (сохраняет порядок одинаковых элементов).

**Недостатки:**

- Требует дополнительной памяти для слияния, что делает его менее эффективным
  для маленьких массивов.

**Пример реализации:**

```java
public class MergeSort {
    public static void mergeSort(int[] arr) {
        if (arr.length < 2) {
            return;
        }

        int mid = arr.length / 2;
        int[] left = new int[mid];
        int[] right = new int[arr.length - mid];

        System.arraycopy(arr, 0, left, 0, mid);
        System.arraycopy(arr, mid, right, 0, arr.length - mid);

        mergeSort(left);
        mergeSort(right);

        merge(arr, left, right);
    }

    private static void merge(int[] arr, int[] left, int[] right) {
        int i = 0, j = 0, k = 0;

        while (i < left.length && j < right.length) {
            if (left[i] <= right[j]) {
                arr[k++] = left[i++];
            } else {
                arr[k++] = right[j++];
            }
        }

        while (i < left.length) {
            arr[k++] = left[i++];
        }

        while (j < right.length) {
            arr[k++] = right[j++];
        }
    }

    public static void main(String[] args) {
        int[] arr = {38, 27, 43, 3, 9, 82, 10};
        mergeSort(arr);
        System.out.println(Arrays.toString(arr));
    }
}
```

---

## 2. **Быстрая сортировка (Quick Sort)**

**Принцип работы:**
Алгоритм выбирает **опорный элемент**, и разделяет массив на два подмассива —
один с элементами меньше опорного, второй — с элементами больше. После этого
рекурсивно применяется та же логика к подмассивам.

**Временная сложность:**

- **O(n log n)** — средний случай
- **O(n²)** — худший случай (если всегда выбирается самый плохой опорный
  элемент)

**Преимущества:**

- Очень быстрый алгоритм для большинства входных данных.
- Нет необходимости в дополнительной памяти (в отличие от сортировки слиянием).

**Недостатки:**

- Может работать медленно на уже отсортированных или почти отсортированных
  данных.
- Не является стабильным.

**Пример реализации:**

```java
public class QuickSort {
    public static void quickSort(int[] arr, int low, int high) {
        if (low < high) {
            int pivotIndex = partition(arr, low, high);
            quickSort(arr, low, pivotIndex - 1);
            quickSort(arr, pivotIndex + 1, high);
        }
    }

    private static int partition(int[] arr, int low, int high) {
        int pivot = arr[high];
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (arr[j] <= pivot) {
                i++;
                swap(arr, i, j);
            }
        }
        swap(arr, i + 1, high);
        return i + 1;
    }

    private static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    public static void main(String[] args) {
        int[] arr = {38, 27, 43, 3, 9, 82, 10};
        quickSort(arr, 0, arr.length - 1);
        System.out.println(Arrays.toString(arr));
    }
}
```

---

## 3. **Сортировка пузырьком (Bubble Sort)**

**Принцип работы:**
Алгоритм проходит по массиву, сравнивает соседние элементы и меняет их местами,
если они идут в неправильном порядке. Это повторяется, пока весь массив не
станет отсортированным.

**Временная сложность:**

- **O(n²)** — худший и средний случай
- **O(n)** — лучший случай (если массив уже отсортирован)

**Преимущества:**

- Простой в реализации.
- Хорош для небольших массивов или почти отсортированных данных.

**Недостатки:**

- Неэффективен для больших данных из-за квадратичной сложности.

**Пример реализации:**

```java
public class BubbleSort {
    public static void bubbleSort(int[] arr) {
        int n = arr.length;
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (arr[j] > arr[j + 1]) {
                    // Swap arr[j] and arr[j + 1]
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
            }
        }
    }

    public static void main(String[] args) {
        int[] arr = {64, 34, 25, 12, 22, 11, 90};
        bubbleSort(arr);
        System.out.println(Arrays.toString(arr));
    }
}
```

---

## 4. **Сортировка вставками (Insertion Sort)**

**Принцип работы:**
Алгоритм делит массив на отсортированную и неотсортированную части. Он
поочередно перебирает элементы из неотсортированной части и вставляет их в
нужное место в отсортированную часть.

**Временная сложность:**

- **O(n²)** — худший и средний случай
- **O(n)** — лучший случай (если массив уже отсортирован)

**Преимущества:**

- Очень эффективен для почти отсортированных данных.
- Прост в реализации.

**Недостатки:**

- Не подходит для больших массивов.

**Пример реализации:**

```java
public class InsertionSort {
    public static void insertionSort(int[] arr) {
        for (int i = 1; i < arr.length; i++) {
            int key = arr[i];
            int j = i - 1;

            while (j >= 0 && arr[j] > key) {
                arr[j + 1] = arr[j];
                j = j - 1;
            }
            arr[j + 1] = key;
        }
    }

    public static void main(String[] args) {
        int[] arr = {12, 11, 13, 5, 6};
        insertionSort(arr);
        System.out.println(Arrays.toString(arr));
    }
}
```

---

## 5. **Сортировка выбором (Selection Sort)**

**Принцип работы:**
Алгоритм ищет минимальный (или максимальный) элемент в неотсортированной части
массива и меняет его местами с первым элементом неотсортированной части.

**Временная сложность:**

- **O(n²)** — для всех случаев.

**Преимущества:**

- Прост в реализации.
- Подходит для небольших массивов.

**Недостатки:**

- Неэффективен для больших массивов.

**Пример реализации:**

```java
public class SelectionSort {
    public static void selectionSort(int[] arr) {
        int n = arr.length;
        for (int i = 0; i < n - 1; i++) {
            int minIdx = i;
            for (int j = i + 1; j < n; j++) {
                if (arr[j] < arr[minIdx]) {
                    minIdx = j;
                }
            }
            int temp = arr[minIdx];
            arr[minIdx] = arr[i];
            arr[i] = temp;
        }
    }

    public static void main(String[] args) {
        int[] arr = {64, 25, 12, 22, 11};
        selectionSort(arr);
        System.out.println(Arrays.toString(arr));
    }
}
```

---

## 📊 Таблица сравнений алгоритмов сортировки

| Алгоритм          | Время (средний случай) | Время (худший случай) | Пространство | Стабильность |
|-------------------|------------------------|-----------------------|--------------|--------------|
| **QuickSort**     | O(n log n)             | O(n²)                 | O(log n)     | Нет          |
| **MergeSort**     | O(n log n)             | O(n log n)            | O(n)         | Да           |
| **BubbleSort**    | O(n)                   | O(n²)                 | O(1)         | Да           |
| **InsertionSort** | O(n)                   | O(n²)                 | O(1)         | Да           |
| **SelectionSort** | O(n²)                  | O(n²)                 | O(1)         | Нет          |

---

Если тебе нужно более подробно разобрать какой-то алгоритм или пример
использования, дай знать!