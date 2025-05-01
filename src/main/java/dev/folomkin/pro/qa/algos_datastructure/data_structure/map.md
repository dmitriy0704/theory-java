# Map

## HashMap

- Пары ключ-значение, быстрая работа: `O(1)` в среднем.

HashMap чуть лучше про то как устроен и сложности методов

### **Устройство**

HashMap в Java — это структура данных, которая реализует интерфейс Map и
использует хеш-таблицу для хранения пар "ключ-значение". Она обеспечивает
быстрый доступ к элементам по ключу, что делает её одной из самых популярных
реализаций Map.

**Основные характеристики HashMap**

1. Ключи и значения: HashMap хранит данные в виде пар "ключ-значение". Каждый
   ключ должен быть уникальным, но значения могут повторяться.
2. Неупорядоченность: Элементы в HashMap не имеют определенного порядка. Порядок
   вставки не сохраняется.
3. Допускает null: HashMap позволяет использовать один null в качестве ключа и
   любое количество null в качестве значений.
4. Не синхронизирован: HashMap не является потокобезопасным. Если несколько
   потоков одновременно изменяют его, необходимо использовать внешнюю
   синхронизацию.

**Как работает HashMap**

1. Хеширование<br>
   Когда вы добавляете пару "ключ-значение" в HashMap, ключ проходит через
   хеш-функцию, которая вычисляет хеш-код для этого ключа. Хеш-код — это целое
   число, которое используется для определения индекса (или бакета) в массиве
   (хеш-таблице), где будет храниться значение.

```java
int hash = key.hashCode();
int index = hash % array.length; // Определяем индекс в массиве
```

2. Создание бакета: Если по этому индексу еще нет других элементов, создается
   новый бакет. В Java это обычно реализуется как связный список или дерево.
3. Добавление элементов: Если по этому индексу уже есть элементы (из-за
   коллизий), новая пара "ключ-значение" добавляется в существующий бакет. В
   случае связного списка новый элемент добавляется в конец списка, а если
   используется сбалансированное дерево (например, Red-Black Tree), то элемент
   будет вставлен в соответствующее место дерева.

4. Обработка коллизий<br>
   Когда два ключа имеют одинаковый хеш-код и попадают в один и тот же индекс
   массива, HashMap создает связный список (или дерево) для хранения всех пар "
   ключ-значение", которые имеют одинаковый индекс. HashMap использует метод
   цепочек (chaining) для обработки этих коллизий. В Java 8 и выше, если
   количество элементов в цепочке превышает определенный порог (обычно 8),
   HashMap преобразует связный список в сбалансированное дерево (например,
   Red-Black Tree) для улучшения производительности.

5. Резервирование места<br>
   Когда количество элементов в HashMap превышает определенный порог (обычно 75%
   от текущей емкости), происходит увеличение емкости:
6. Время доступа<br>
   В среднем время доступа к элементам по ключу составляет O(1) благодаря
   использованию хеширования. Однако в худшем случае (например, если все
   элементы попадают в одну цепочку) время доступа может составлять O(n). Чтобы
   избежать этого, важно правильно выбирать размер начального массива и
   коэффициент загрузки.
7. Получение значения по ключу сначала вычисляется его хеш-код. Затем
   определяется индекс массива. Если по этому индексу есть несколько элементов (
   из-за коллизий), HashMap будет перебирать элементы в связанном списке или
   дереве до тех пор, пока не найдет нужный ключ.

**Бакеты**

В контексте HashMap в Java "бакеты" (или "ведра") — это структуры данных,
которые используются для хранения пар "ключ-значение". Бакеты помогают
организовать данные в HashMap и обеспечивают эффективный доступ к ним. Давайте
рассмотрим, как это работает.

**Причины Колизий**

1. Ограниченное количество индексов<br>
2. Хеш-функция<br>
3. Ограниченная длина хеша<br>
4. Сходство ключей<br>

Причины, почему два ключа могут иметь одинаковый хеш-код:<br>

1. **Ограниченное пространство значений**: Хеш-функция преобразует объект в
   целое число (хеш-код), и поскольку количество возможных объектов значительно
   больше, чем количество возможных целых чисел, разные объекты могут быть
   преобразованы в одно и то же значение.
2. **Алгоритм хеширования**: Хеш-функции не идеальны и могут создавать коллизии.
   Например, если два объекта имеют одинаковые значения для всех полей, которые
   участвуют в вычислении хеш-кода, они будут иметь одинаковый хеш-код.
3. **Пользовательские классы**: Если вы создаете собственный класс и
   переопределяете метод `hashCode()`, вы можете случайно создать коллизии, если
   не будете учитывать все важные поля объекта.

### Сложности методов

В среднем, операция добавления, удаления и поиска элемента по ключу в HashMap
имеют
временную сложность O(1).

Проверка наличия значения (containsValue), итерация по элементам - O(n).

Однако, в худшем случае, когда все элементы попадают в
одну корзину, они будут связаны в связный список или дерево, и операция может
занимать время O(n), где n - количество элементов в корзине. Таким образом,
сложность операций в HashMap зависит от количества коллизий и хеш-функции.<br>
В среднем, сложность выборки элемента также составляет O(1), но в худшем случае
может достигать O(n).

## TreeMap

- Ключи сортируются по естественному порядку или `Comparator`.
- Операции — `O(log n)`

`TreeMap` — это структура данных в Java, которая реализует интерфейс `Map` и *
*хранит пары ключ-значение в отсортированном порядке по ключам**. Внутри он
устроен как **красно-черное дерево** (Red-Black Tree), точно так же, как и
`TreeSet`, только вместо одного значения хранится **ключ и связанное с ним
значение**.

---

## 🧱 Внутреннее устройство `TreeMap`

### 📦 Основная структура:

```java
public class TreeMap<K, V> extends AbstractMap<K, V>
        implements NavigableMap<K, V>, Cloneable, java.io.Serializable {

    private transient Entry<K, V> root;
    ...
}
```

- Внутренне `TreeMap` реализован как **бинарное дерево поиска** с
  балансировкой — **красно-черное дерево**.
- Каждый узел дерева содержит:
    - `K key`
    - `V value`
    - `Entry<K,V> left`, `right`, `parent`
    - `boolean color` (RED/BLACK)

---

## ⚙️ Как работают основные операции

### 1. **`put(K key, V value)`**

- Ключ сравнивается с уже существующими ключами с помощью `compareTo()` или
  `Comparator`.
- Если такой ключ уже есть — значение перезаписывается.
- Если нет — создается новый узел и вставляется.
- Затем дерево **перебалансируется** по правилам красно-черного дерева.

> 🔁 Сложность: **O(log n)**

---

### 2. **`get(K key)`**

- Ключ сравнивается с другими в дереве.
- Поиск идёт по правилам бинарного дерева поиска (лево <, право >).
- Возвращается связанное значение или `null`.

> 🔎 Сложность: **O(log n)**

---

### 3. **`remove(K key)`**

- Ключ находится в дереве, удаляется соответствующий узел.
- Дерево **перестраивается и перебалансируется**.

> ❌ Сложность: **O(log n)**

---

### 4. **Навигационные методы (`NavigableMap`)**

TreeMap поддерживает удобные методы:

| Метод            | Что делает                   |
|------------------|------------------------------|
| `firstKey()`     | Первый (наименьший) ключ     |
| `lastKey()`      | Последний (наибольший) ключ  |
| `higherKey(k)`   | Следующий ключ после `k`     |
| `lowerKey(k)`    | Предыдущий ключ до `k`       |
| `ceilingKey(k)`  | Первый `≥ k`                 |
| `floorKey(k)`    | Последний `≤ k`              |
| `subMap(k1, k2)` | Подмножество от `k1` до `k2` |

---

## ⏱ Временная сложность операций

| Операция                  | Сложность    |
|---------------------------|--------------|
| `get()`                   | **O(log n)** |
| `put()`                   | **O(log n)** |
| `remove()`                | **O(log n)** |
| `firstKey()`, `lastKey()` | **O(log n)** |
| `iterator()` (по ключам)  | **O(n)**     |

---

## ✅ Пример использования

```java
import java.util.*;

public class Main {
    public static void main(String[] args) {
        TreeMap<String, Integer> scores = new TreeMap<>();

        scores.put("Alice", 90);
        scores.put("Bob", 85);
        scores.put("Charlie", 92);

        System.out.println(scores); // {Alice=90, Bob=85, Charlie=92}
        System.out.println("Bob's score: " + scores.get("Bob")); // 85

        System.out.println("First key: " + scores.firstKey()); // Alice
        System.out.println("Next after Bob: " + scores.higherKey("Bob")); // Charlie

        scores.remove("Alice");
        System.out.println(scores); // {Bob=85, Charlie=92}
    }
}
```

---

## 🔄 `TreeMap` vs `HashMap`

| Критерий             | `HashMap`        | `TreeMap`                            |
|----------------------|------------------|--------------------------------------|
| Порядок              | Нет              | Сортирует по ключам                  |
| Сложность `get()`    | O(1) (в среднем) | O(log n)                             |
| Сложность `put()`    | O(1)             | O(log n)                             |
| Поддержка диапазонов | ❌                | ✅ `subMap()`, `ceilingKey()` и т. д. |
| Null-ключи           | ✅ (один)         | ❌ (исключение)                       |

---

## 🧠 Вывод:

- `TreeMap` — выбор, когда нужно **держать ключи в порядке** и выполнять *
  *поиск, навигацию, диапазоны**.
- Использует **красно-черное дерево**: все операции выполняются за **O(log n)**.
- Не допускает `null` в качестве ключа, так как `compareTo()`/`Comparator` не
  работают с `null`.

Хочешь, я покажу, как реализовать упрощённую версию `TreeMap` вручную?

### РЕАЛИЗАЦИЯ

Конечно! Ниже я покажу, как реализовать **упрощённую версию `TreeMap`** в Java с
ключевыми возможностями:

- Добавление (`put`)
- Поиск (`get`)
- Упорядоченность по ключам (через `Comparable`)
- Простое бинарное дерево поиска (без балансировки)

> ⚠️ Это **упрощённая реализация** — без красно-черного дерева и
> перебалансировки, но с сохранением порядка по ключам.

---

## 🛠 Пример: `SimpleTreeMap<K, V>`

```java
public class SimpleTreeMap<K extends Comparable<K>, V> {

    private class Node {
        K key;
        V value;
        Node left, right;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    private Node root;

    // Добавление пары ключ-значение
    public void put(K key, V value) {
        root = put(root, key, value);
    }

    private Node put(Node node, K key, V value) {
        if (node == null) return new Node(key, value);

        int cmp = key.compareTo(node.key);
        if (cmp < 0) node.left = put(node.left, key, value);
        else if (cmp > 0) node.right = put(node.right, key, value);
        else node.value = value; // перезапись
        return node;
    }

    // Поиск значения по ключу
    public V get(K key) {
        Node node = root;
        while (node != null) {
            int cmp = key.compareTo(node.key);
            if (cmp < 0) node = node.left;
            else if (cmp > 0) node = node.right;
            else return node.value;
        }
        return null;
    }

    // Вывод всех ключей в отсортированном порядке
    public void printInOrder() {
        printInOrder(root);
    }

    private void printInOrder(Node node) {
        if (node != null) {
            printInOrder(node.left);
            System.out.println(node.key + " = " + node.value);
            printInOrder(node.right);
        }
    }
}
```

---

## ✅ Пример использования:

```java
public class Main {
    public static void main(String[] args) {
        SimpleTreeMap<String, Integer> map = new SimpleTreeMap<>();

        map.put("Charlie", 3);
        map.put("Alice", 1);
        map.put("Bob", 2);
        map.put("Bob", 22); // обновление

        System.out.println("Bob's value: " + map.get("Bob")); // 22

        System.out.println("All entries:");
        map.printInOrder();  // Выведет в порядке: Alice, Bob, Charlie
    }
}
```

---

## 🔍 Особенности этой реализации:

| Особенность             | Реализация                            |
|-------------------------|---------------------------------------|
| Упорядоченность         | ✅ По `Comparable<K>` (in-order обход) |
| Балансировка            | ❌ Нет (может деградировать до O(n))   |
| Вставка, поиск          | O(h), где h — высота дерева           |
| Поведение при дубликате | Значение обновляется (`put`)          |

---

Хочешь, я добавлю удаление (`remove`) или поддержку компаратора (
`Comparator<K>`) в эту реализацию?

## LinkedHashMap

- Сохраняет порядок вставки.

`LinkedHashMap` — это расширенная версия `HashMap` в Java, которая сохраняет *
*порядок вставки элементов** или **порядок доступа**. Она объединяет *
*быстроту `HashMap`** и **предсказуемый порядок обхода** как в списке.

---

## 📦 Что такое `LinkedHashMap`?

- Это реализация интерфейса `Map`, наследуется от `HashMap`.
- В отличие от обычного `HashMap`, `LinkedHashMap` сохраняет **порядок
  добавления** (или порядок использования, если задан `accessOrder = true`).

---

## ⚙️ Как он устроен внутри?

### 1. **Наследуется от `HashMap`**

```java
public class LinkedHashMap<K, V> extends HashMap<K, V> implements Map<K, V> {
    ...
}
```

### 2. **Внутренние связи: двусвязный список**

Каждая запись (`Entry`) в `HashMap` расширена до `LinkedHashMap.Entry`, и
содержит указатели:

```java
static class Entry<K, V> extends HashMap.Node<K, V> {
    Entry<K, V> before, after; // для связного списка
}
```

- `before`: предыдущий элемент
- `after`: следующий элемент

Этот список отслеживает **порядок элементов**.

---

## 🔄 Типы порядка

Когда создаётся `LinkedHashMap`, можно выбрать поведение:

```java
new LinkedHashMap<>(initialCapacity,loadFactor,accessOrder)
```

- `accessOrder = false` (по умолчанию) → **порядок вставки**
- `accessOrder = true` → **порядок доступа (LRU-подобный)**

---

## 🧠 Пример различий:

### 📥 `accessOrder = false` (вставка)

```java
LinkedHashMap<Integer, String> map = new LinkedHashMap<>();
map.

put(1,"A");
map.

put(2,"B");
map.

put(3,"C");

System.out.

println(map); // {1=A, 2=B, 3=C}
```

### 🔁 `accessOrder = true` (доступ)

```java
LinkedHashMap<Integer, String> map = new LinkedHashMap<>(16, 0.75f, true);
map.

put(1,"A");
map.

put(2,"B");
map.

put(3,"C");

map.

get(2); // доступ к элементу 2

System.out.

println(map); // {1=A, 3=C, 2=B}
```

---

## 🔧 Автоматическое удаление (LRU Cache)

Можно создать `LinkedHashMap`, который сам удаляет старые элементы:

```java
LinkedHashMap<Integer, String> lruCache = new LinkedHashMap<>(16, 0.75f, true) {
    protected boolean removeEldestEntry(Map.Entry<Integer, String> eldest) {
        return size() > 3; // оставляем только 3 последних элемента
    }
};
```

---

## ⏱ Временная сложность операций

| Операция               | Сложность                            |
|------------------------|--------------------------------------|
| `put`, `get`, `remove` | **O(1)**                             |
| `iteration`            | **O(n)** (в порядке вставки/доступа) 

---

## 🧪 Пример полного использования:

```java
import java.util.*;

public class Main {
    public static void main(String[] args) {
        LinkedHashMap<String, Integer> map = new LinkedHashMap<>(16, 0.75f, true);

        map.put("A", 1);
        map.put("B", 2);
        map.put("C", 3);

        map.get("A"); // доступ — меняет порядок

        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            System.out.println(entry.getKey() + " => " + entry.getValue());
        }
        // Выведет: B => 2, C => 3, A => 1 (порядок по доступу)
    }
}
```

---

## ✅ Вывод

| Свойство          | `HashMap`        | `LinkedHashMap`                 |
|-------------------|------------------|---------------------------------|
| Сохраняет порядок | ❌                | ✅                               |
| Тип порядка       | Нет              | Вставка или доступ              |
| Быстродействие    | O(1)             | O(1)                            |
| Используется для  | Быстрого доступа | LRU кэши, предсказуемый порядок |

---

Хочешь, я покажу, как вручную реализовать `MiniLinkedHashMap` на основе
`HashMap` и двусвязного списка?

### РЕАЛИЗАЦИЯ

Отлично! Ниже — **упрощённая реализация `LinkedHashMap`**, которую мы назовём
`MiniLinkedHashMap`. Она сохраняет:

- **вставку ключей в порядке** (как обычный `LinkedHashMap`)
- быстрые операции `get`, `put` за **O(1)** (на основе `HashMap`)
- порядок обхода с помощью **двусвязного списка**

---

## 🔧 `MiniLinkedHashMap<K, V>` (реализация)

```java
import java.util.*;

public class MiniLinkedHashMap<K, V> {
    private class Node {
        K key;
        V value;
        Node prev, next;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    private final Map<K, Node> map = new HashMap<>();
    private Node head, tail;

    // Добавление или обновление элемента
    public void put(K key, V value) {
        if (map.containsKey(key)) {
            Node node = map.get(key);
            node.value = value;
            return;
        }

        Node newNode = new Node(key, value);
        map.put(key, newNode);
        appendToTail(newNode);
    }

    // Получение значения по ключу
    public V get(K key) {
        Node node = map.get(key);
        return node != null ? node.value : null;
    }

    // Удаление по ключу
    public void remove(K key) {
        Node node = map.remove(key);
        if (node != null) removeNode(node);
    }

    // Печать всех пар ключ-значение в порядке вставки
    public void printInOrder() {
        Node current = head;
        while (current != null) {
            System.out.println(current.key + " = " + current.value);
            current = current.next;
        }
    }

    // Вспомогательные методы:
    private void appendToTail(Node node) {
        if (tail != null) {
            tail.next = node;
            node.prev = tail;
            tail = node;
        } else {
            head = tail = node;
        }
    }

    private void removeNode(Node node) {
        if (node.prev != null) node.prev.next = node.next;
        else head = node.next;

        if (node.next != null) node.next.prev = node.prev;
        else tail = node.prev;
    }
}
```

---

## ✅ Пример использования:

```java
public class Main {
    public static void main(String[] args) {
        MiniLinkedHashMap<String, Integer> map = new MiniLinkedHashMap<>();

        map.put("A", 1);
        map.put("B", 2);
        map.put("C", 3);

        map.put("B", 22); // обновляем значение

        System.out.println("B = " + map.get("B")); // 22

        map.remove("A");

        System.out.println("Map entries in insertion order:");
        map.printInOrder();  // Должно вывести: B = 22, C = 3
    }
}
```

---

## 🧠 Что эта реализация поддерживает:

| Возможность           | Реализовано?               |
|-----------------------|----------------------------|
| Сложность `get/put`   | ✅ O(1)                     |
| Сохранение порядка    | ✅ Вставка                  |
| Удаление элемента     | ✅ O(1)                     |
| `accessOrder`         | ❌ (можно добавить)         |
| `removeEldestEntry()` | ❌ (можно добавить для LRU) |

---

Хочешь, я покажу, как расширить этот класс до **LRU-кэша**, где удаляются самые
старые элементы при переполнении?

