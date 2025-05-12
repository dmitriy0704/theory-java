# Array

Массив в Java — это **упорядоченная коллекция элементов фиксированной длины**,
где все элементы имеют **одинаковый тип**. Он является низкоуровневой структурой
данных и реализован как **объект**, но работает эффективно по скорости.

## Устройство массива в Java

### Объявление и инициализация

```java
int[] arr = new int[5]; // массив на 5 элементов, все = 0 по умолчанию
int[] nums = {1, 2, 3, 4}; // массив с начальными значениями
```

### Что происходит "под капотом"?

1. **Выделение памяти:**
    - Java выделяет **непрерывный блок памяти** в куче (`heap`), достаточный для
      хранения `n` элементов типа `T`.
    - Например, `new int[5]` создаёт память на 5 * 4 байта (int = 4 байта).

2. **Сохранение метаданных:**
    - Массив в Java — это **объект**, у него есть поля:
        - `length` — длина массива
        - ссылки на элементы
    - Например, в памяти хранится объект с:
        - заголовком (служебная информация JVM)
        - полем `length`
        - массивом элементов

3. **Типизированность:**
    - Java строго следит за типами — нельзя хранить `String` в `int[]`.

### Индексация и доступ

- Доступ к элементу: `arr[0]`, `arr[i]`
- Все элементы индексируются с **нуля**
- Быстрый доступ: **время выполнения — O(1)**

### Ошибки, связанные с массивами

- **ArrayIndexOutOfBoundsException** — при попытке выйти за границы массива.
- **NullPointerException** — если работаешь с неинициализированным массивом (
  `arr = null`).

### Пример

```java
public class Main {
    public static void main(String[] args) {
        int[] numbers = new int[3];
        numbers[0] = 10;
        numbers[1] = 20;
        numbers[2] = 30;

        for (int i = 0; i < numbers.length; i++) {
            System.out.println(numbers[i]);
        }
    }
}
```

### Особенности

| Характеристика     | Значение                             |
|--------------------|--------------------------------------|
| Длина              | Фиксированная (неизменна)            |
| Тип                | Однородный (все одного типа)         |
| Производительность | Быстрый доступ, низкие накладные     |
| Расширение         | Только через создание нового массива |

## Реализация

### Реализация динамического массива: `MyArray`

```java
public class MyArray<T> {
    private T[] data;
    private int size;

    // Начальная вместимость
    private static final int INITIAL_CAPACITY = 10;

    public MyArray() {
        data = (T[]) new Object[INITIAL_CAPACITY]; // нельзя создать массив с дженериком напрямую
        size = 0;
    }

    // Добавление элемента в конец
    public void add(T element) {
        ensureCapacity();
        data[size++] = element;
    }

    // Получение элемента по индексу
    public T get(int index) {
        checkIndex(index);
        return data[index];
    }

    // Установка значения по индексу
    public void set(int index, T element) {
        checkIndex(index);
        data[index] = element;
    }

    // Удаление элемента по индексу
    public void remove(int index) {
        checkIndex(index);
        for (int i = index; i < size - 1; i++) {
            data[i] = data[i + 1];
        }
        data[--size] = null;
    }

    // Текущий размер массива
    public int size() {
        return size;
    }

    // Проверка индекса
    private void checkIndex(int index) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
    }

    // Увеличение вместимости
    private void ensureCapacity() {
        if (size == data.length) {
            int newCapacity = data.length * 2;
            T[] newData = (T[]) new Object[newCapacity];
            for (int i = 0; i < data.length; i++) {
                newData[i] = data[i];
            }
            data = newData;
        }
    }
}
```

### Пример использования

```java
public class Main {
    public static void main(String[] args) {
        MyArray<String> myArray = new MyArray<>();
        myArray.add("Apple");
        myArray.add("Banana");
        myArray.add("Cherry");

        System.out.println("Size: " + myArray.size()); // 3
        System.out.println(myArray.get(1)); // Banana

        myArray.remove(1); // удалим "Banana"
        System.out.println(myArray.get(1)); // Cherry
    }
}
```

### Что мы реализовали:

- **Динамический рост** массива при переполнении (`ensureCapacity`)
- **Быстрый доступ** к элементам по индексу
- **Удаление сдвигом** всех правых элементов
- **Обобщённость** (`<T>`) — работает с любыми типами

### Итератор и сортировка

```java
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Arrays;
import java.util.Comparator;

public class MyArray<T> implements Iterable<T> {
    private T[] data;
    private int size;

    private static final int INITIAL_CAPACITY = 10;

    public MyArray() {
        data = (T[]) new Object[INITIAL_CAPACITY];
        size = 0;
    }

    public void add(T element) {
        ensureCapacity();
        data[size++] = element;
    }

    public T get(int index) {
        checkIndex(index);
        return data[index];
    }

    public void set(int index, T element) {
        checkIndex(index);
        data[index] = element;
    }

    public void remove(int index) {
        checkIndex(index);
        for (int i = index; i < size - 1; i++) {
            data[i] = data[i + 1];
        }
        data[--size] = null;
    }

    public int size() {
        return size;
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }

    private void ensureCapacity() {
        if (size == data.length) {
            int newCapacity = data.length * 2;
            data = Arrays.copyOf(data, newCapacity);
        }
    }

    // Добавляем итератор
    @Override
    public Iterator<T> iterator() {
        return new Iterator<>() {
            private int currentIndex = 0;

            @Override
            public boolean hasNext() {
                return currentIndex < size;
            }

            @Override
            public T next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                return data[currentIndex++];
            }
        };
    }

    // Добавляем метод сортировки
    public void sort(Comparator<? super T> comparator) {
        Arrays.sort(data, 0, size, comparator);
    }
}
```

### Пример использования с итератором и сортировкой

```java
import java.util.Comparator;

public class Main {
    public static void main(String[] args) {
        MyArray<String> myArray = new MyArray<>();
        myArray.add("Banana");
        myArray.add("Apple");
        myArray.add("Cherry");

        // Используем итератор
        System.out.println("Элементы массива:");
        for (String fruit : myArray) {
            System.out.println(fruit);
        }

        // Сортируем массив в лексикографическом порядке
        myArray.sort(String::compareTo);

        System.out.println("\nОтсортированный массив:");
        for (String fruit : myArray) {
            System.out.println(fruit);
        }

        // Обратный порядок сортировки
        myArray.sort(Comparator.reverseOrder());
        System.out.println("\nОтсортированный в обратном порядке:");
        for (String fruit : myArray) {
            System.out.println(fruit);
        }
    }
}
```

### Новые возможности:

1. **Итератор**:
    - Позволяет использовать цикл `for-each`.
    - Реализация интерфейса `Iterable`.

2. **Сортировка**:
    - Метод `sort` использует `Arrays.sort` с заданным компаратором.
    - Гибкость: можно сортировать в прямом и обратном порядке.

### Временная сложность в массиве

Временная сложность операций с **массивом** в Java (и вообще в
большинстве языков) зависит от того, **что именно ты делаешь**. Вот краткая
таблица и пояснение:

#### Временная сложность операций с массивом

| Операция                       | Временная сложность       |
|--------------------------------|---------------------------|
| Доступ по индексу (`arr[i]`)   | **O(1)** — мгновенно      |
| Изменение по индексу           | **O(1)**                  |
| Линейный поиск (`for`)         | **O(n)**                  |
| Вставка в конец (фикс. массив) | **O(1)**, если есть место |
| Вставка в начало/середину      | **O(n)**                  |
| Удаление из конца              | **O(1)**                  |
| Удаление из начала/середины    | **O(n)**                  |

### Почему такие сложности?

**O(1)** — Быстрые операции:

- **Доступ по индексу** реализуется через арифметику указателя:

  ```
  адрес_начала + индекс * размер_элемента
  ```
  JVM сразу "прыгает" по нужному адресу.

**O(n)** — Медленные операции:

- **Вставка/удаление в середине** требует **сдвига всех последующих элементов**:
  ```java
  for (int i = n; i > index; i--) {
      arr[i] = arr[i - 1]; // сдвигаем вправо
  }
  ```

Пример

```java
void demo() {
    int[] arr = new int[5];
    arr[0] = 1;           // O(1)
    arr[4] = 5;           // O(1)

    for (int x : arr) {   // O(n)
        System.out.println(x);
    }
}

```

Сравнение с `ArrayList`

| Операция           | `int[]`  | `ArrayList<Integer>`  |
|--------------------|----------|-----------------------|
| Доступ по индексу  | O(1)     | O(1)                  |
| Вставка в конец    | O(1)*    | O(1) амортизированное |
| Вставка в середину | O(n)     | O(n)                  |
| Удаление           | O(n)     | O(n)                  |
| Рост массива       | (нельзя) | (автоматически)       |

> `ArrayList` — это массив, который автоматически увеличивает размер (в 1.5–2
> раза при переполнении).
