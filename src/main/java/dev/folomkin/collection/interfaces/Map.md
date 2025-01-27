# Интерфейс Map

Карта отображений — это объект, который хранит пару «ключ–значение».
Поиск объекта (значения) облегчается по сравнению с множествами за счет
того, что его можно найти по уникальному ключу. Уникальность объектов-ключей
должна обеспечиваться переопределением методов hashCode() и equals()
или реализацией интерфейсов Comparable, Comparator пользовательским классом.

`Map<K,V>` - принимает два дженерика типа K и V. Повторяющиеся элементы
запрещены. Часто используемые реализации включают HashMap, Hashtable и
LinkedHashMap. Их подинтерфейс SortedMap<K,V> моделирует упорядоченную и
отсортированную карту на основании ключа, реализованного в TreeMap.  
Интерфейс Map.Entry предназначен для извлечения ключей и значений карты с
помощью методов K getKey() и V getValue() соответственно. Вызов метода
V setValue(V value) заменяет значение, ассоциированное с текущим ключом.

**Почему Map не наследуется от Collection?**

Это связано с различиями в их целях и использовании. Интерфейс Collection
представляет собой общие методы для работы с группой объектов, таких как
добавление, удаление и проверка наличия элемента. Он ориентирован на работу с
коллекциями объектов, где каждый объект является элементом коллекции.
Интерфейс Map, с другой стороны, представляет собой отображение ключей на
значения. Он не рассматривает элементы коллекции как отдельные объекты, а
предоставляет доступ к значению, связанному с определенным ключом. Это более
общий и мощный подход, который не сводится к работе с отдельными элементами
коллекции.  
Интерфейс Map включает в себя методы для управления парами ключ-значение и
обеспечивает эффективный доступ к значениям по ключу. По этим причинам он не
является подтипом Collection. Однако, классы, реализующие интерфейс Map, часто
предоставляют методы, которые позволяют работать с элементами коллекции или
возвращают представление коллекции ключей, значений или записей (ключ-значение).
Таким образом, хотя Map и Collection предоставляют абстракции для работы с
группой объектов, они решают разные задачи, и поэтому не существует
иерархического отношения наследования между ними.
Map - коллекция пар "ключ-значение". Ключ всегда должен быть уникален,
значение нет.

## _Интерфейсы и реализации:_

**Интерфейсы:**

- `Map<K,V>` — отображает уникальные ключи и значения;
- `Map.Entry<K,V>` — описывает пару «ключ–значение»;
- `SortedMap<K,V>` — содержит отсортированные ключи и значения;
- `NavigableMap<K,V>` — добавляет новые возможности навигации и поиска по ключу.

**Классы:**

- `AbstractMap<K,V>` — реализует интерфейс Map<K,V>, является суперклассом
  для всех перечисленных карт отображений;
- `HashTable<K,V>` - реализация такой структуры данных, как хэш-таблица. Она не
  позволяет использовать null в качестве значения или ключа. Эта коллекция была
  реализована раньше, чем Java Collection Framework, но в последствии была
  включена в его состав. Как и другие коллекции из Java 1.0, Hashtable _является
  синхронизированной_ (почти все методы помечены как synchronized). Из-за этой
  особенности у неё имеются существенные проблемы с производительностью и,
  начиная с Java 1.2, в большинстве случаев рекомендуется использовать другие
  реализации интерфейса Map ввиду отсутствия у них синхронизации.
- `HashMap<K,V>` — коллекция является альтернативой Hashtable. Двумя основными
  отличиями от Hashtable являются то, что HashMap не синхронизирована и HashMap
  позволяет использовать null как в качестве ключа, так и значения. Так же как и
  Hashtable, данная коллекция не является упорядоченной: порядок хранения
  элементов зависит от хэш-функции. Добавление элемента выполняется за
  константное время O(1), но время удаления, получения зависит от распределения
  хэш-функции. В идеале является константным, но может быть и линейным O(n).
- `LinkedHashMap<K,V>` — образует дважды связанный список ключей. Этот
  механизм эффективен, только если превышен коэффициент загруженности карты при
  работе с кэш-памятью и др.  
  LinkedHashMap — это упорядоченная реализация хэш-таблицы. Здесь, в отличии от
  HashMap, порядок итерирования равен порядку добавления элементов. Данная
  особенность достигается благодаря двунаправленным связям между элементами
  (аналогично LinkedList). Но это преимущество имеет также и недостаток —
  увеличение памяти, которое занимает коллекция.
- `TreeMap<K,V>` — использует дерево, где ключи расположены в виде дерева
  поиска в определенном порядке;
- `WeakHashMap\<K,V>` — позволяет механизму сборки мусора удалять из карты
  значения по ключу, ссылка на который вышла из области видимости приложения;

## Map:


- **_Map_**
    - **HashTable\<K,V>** - реализация синхронизированной хеш-таблицы для
      интерфейса Map\<K,V>, который не допускает null ключей или значения для
      наследуемых методов;
    - **LinkedHashMap\<K,V>** - хэш-таблица со свойствами связанного списка для
      улучшения методов вставки и удаления;
    - **HashMap\<K,V>** - реализация хэш-таблицы, методы не синхронизированы;
    - **EnumMap\<K,V>** - реализация хэш-таблицы в виде перечислений;
- **_SortedMap_**
    - **TreeMap\<K,V>** - реализация SortedMap в виде дерева;

**Представления карты**

- **Set\<K> keySet()** - возвращает представление карты в виде множества ключей;
- **Collection\<V> values()** - возвращает представление карты в виде коллекции
  значений;
- **Set entrySet()** - представление в виде множества пар ключ-значения;

___Методы Map\<K,V>:___

- **boolean containsKey(Object key)** - проверяет наличие ключа;
- **boolean containsValue(Object value)** - проверяет наличие значения;
- **V get(Object key)** - возвращает значение указанного ключа или null;
- **V put(K key, V value)** - помещает в коллекцию новый объект с ключом k и
  значением v. Если в коллекции уже есть объект с подобным ключом, то он
  перезаписывается. После добавления возвращает предыдущее значение для ключа k,
  если он уже был в коллекции. Если же ключа еще не было в коллекции, то
  возвращается значение null.
- **default V putIfAbsent(K key, V value)** — добавляет ключ-значение, только
  если key не существует;
- **default V compute(K key, BiFunction<? super K, ? super V, ? extends V>
  remappingFunction)** — помещает ключ key и вычисляет значение value при
  добавлении в вызывающую карту;
- **default V computeIfAbsent(K key, Function<? super K, ? super V>
  mappingFunction)** — помещает ключ key и значение value в вызывающую карту,
  если пары с таким ключом не существует, если ключ существует, то за- мена не
  производится;
- **default V computeIfPresent(K key, BiFunction<? super K, ? super V, ? extends
  V> remappingFunction)** — заменяет значение value в вызывающей карте, если
  ключ с таким значением существует, если же пары с таким ключом не существует,
  то вставка пары не производится;
- **void putAll(Map <? extends K, ? extends V> m)** — помещает карту m в
  вызывающую карту;
- **V remove(Object key)** — удаляет пару «ключ–значение» по ключу key;
- **void clear()** — удаляет все пары из вызываемой карты;
- **boolean containsKey(Object key)** — возвращает true, если вызывающая карта
  содержит key как ключ;
- **boolean containsValue(Object value)** — возвращает true, если вызывающая
  карта содержит value как значение;
- **Set<K> keySet()** — возвращает множество ключей;
- **Set<Map.Entry<K, V>> entrySet()** — возвращает множество, содержащее
  значения карты в виде пар «ключ–значение»;
- **Collection<V> values()** — возвращает коллекцию, содержащую значения
  карты;
- **static <K, V> Map<K, V> copyOf(Map <? extends K,? extends V> map)** —
  копирует исходную карту в немодифицируемую новую карту;
- **static <K, V> Map<K, V> of(parameters)** — перегруженный метод для со-
  здания неизменяемых карт на основе переданных в метод параметров;
- **default void forEach(BiConsumer<? super K, ? super V> action)** — выполняет
  действие над каждым элементом Map.

В коллекциях, возвращаемых тремя последними методами, можно только удалять
элементы, добавлять нельзя. Данное ограничение обуславливается параметризацией
возвращаемого методами значения.

Наличие ключа можно проверить так:

```java
public static void main(String[] args) {
    if (!hashMap.containsKey("Пряник")) {
        // replacement will not happen if the key exists
        hashMap.put("Пряник", 4);
    }
}
```

## Интерфейс Map.Entry

Интерфейс Map.Entry в Java используется для работы с элементами Map. Он
представляет пару ключ-значение и имеет методы, которые позволяют получить и
изменить ключ и значение пары.

Методы Map.Entry<K, V>:

- K getKey() — возвращает ключ текущего входа;
- V getValue() — возвращает значение текущего входа;
- V setValue(V obj) — устанавливает значение объекта obj в текущем входе.

```java
public static void main(String[] args) {
    Map<String, Integer> map = new HashMap<>();
    map.put("Jeans", 40);
    map.put("T-Shirt", 35);
    map.put("Gloves", 42);
    map.compute("Shoes", (k, v) -> 77); // adding a pair
    System.out.println(map);
    // replacing value if key exists
    map.computeIfPresent("Shoes", (k, v) -> v + k.length());
    System.out.println(map);
    map.computeIfAbsent("Shoes", v -> 11);
    // adding a pair if the key is missing
    map.computeIfAbsent("Shoes_2", v -> 11);
    System.out.println(map);
}
```

## HashTable

Класс Hashtable<K, V> реализует интерфейс Map, обладает также несколькими
специфичными, по сравнению с другими коллекциями, методами:

- Enumeration<V> elements() — возвращает итератор для значений карты;
- Enumeration<K> keys() — возвращает итератор для ключей карты

```java
public static void main(String[] args) {
    Hashtable<String, Integer> table = new Hashtable<>();
    table.put("Jeans", 40); // adding a pair
    table.put("T-Shirt", 35);
    table.put("Gloves", 42);
    table.compute("Shoes", (k, v) -> 77); // adding a pair
    System.out.println(table);

    Enumeration<String> keys = table.keys();
    while (keys.hasMoreElements()) {
        System.out.println(keys.nextElement());
    }
    Enumeration<Integer> values = table.elements();
    while (values.hasMoreElements()) {
        System.out.println(values.nextElement());
    }
}
```

## HashMap

Базовым классом для всех отображений является абстрактный класс AbstractMap,
который реализует большую часть методов интерфейса Map. Наиболее
распространенным классом отображений является HashMap, который реализует
интерфейс Map и наследуется от класса AbstractMap.

Чтобы добавить или заменить элемент, используется метод put, либо replace, а
чтобы получить его значение по ключу - метод get. С помощью других методов
интерфейса Map также производятся другие манипуляции над элементами: перебор,
получение ключей, значений, удаление.

```java
public class Program {

    public static void main(String[] args) {
        Map<Integer, String> states = new HashMap<Integer, String>();
        states.put(1, "Germany");
        states.put(2, "Spain");
        states.put(4, "France");
        states.put(3, "Italy");

        // получим объект по ключу 2
        String first = states.get(2);
        System.out.println(first);
        // получим весь набор ключей
        Set<Integer> keys = states.keySet();
        // получить набор всех значений
        Collection<String> values = states.values();
        //заменить элемент
        states.replace(1, "Poland");
        // удаление элемента по ключу 2
        states.remove(2);
        // перебор элементов
        for (Map.Entry<Integer, String> item : states.entrySet()) {
            System.out.printf("Key: %d  Value: %s \n", item.getKey(), item.getValue());
        }
        Map<String, Person> people = new HashMap<String, Person>();
        people.put("1240i54", new Person("Tom"));
        people.put("1564i55", new Person("Bill"));
        people.put("4540i56", new Person("Nick"));

        for (Map.Entry<String, Person> item : people.entrySet()) {

            System.out.printf("Key: %s  Value: %s \n", item.getKey(), item.getValue().getName());
        }
    }
}

class Person {
    private String name;

    public Person(String value) {
        name = value;
    }

    String getName() {
        return name;
    }
}

////////
public class Code {
    public static void main(String[] args) {
        HashMap<String, Integer> map = new HashMap<>();
        map.put("a", 1);
        map.put("b", 2);
        map.put("c", 3);

        Set<Map.Entry<String, Integer>> entries = map.entrySet();
        for (Map.Entry<String, Integer> entry : entries) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }
}

```

## EnumMap

Класс EnumMap<K extends Enum<K>, V> в качестве ключа может принимать только
объекты, принадлежащие одному типу enum, который должен быть определен при
создании коллекции. Специально организован для обеспечения максимальной скорости
доступа к элементам коллекции.

```java
enum Country {
    RUSSIA, BELARUS, INDIA, CHINA, POLAND
}

public class Code {
    public static void main(String[] args) {
        EnumMap<Country, Integer> map = new EnumMap<Country, Integer>(Country.class);
        map.put(Country.POLAND, 3);
        map.put(Country.BELARUS, 4);
        map.forEach((k, v) -> System.out.println(k + ": " + v));

        // -> 
        // BELARUS: 4
        // POLAND: 3

    }
}
```

## WeakHashMap<K,V>

Класс WeakHashMap<K, V> хорошо работает в ситуациях, когда в процессе работы с
коллекцией некоторые объекты должны из нее гарантированно удаляться, но моменты
необходимости удаления и само удаление пары из коллекции могут отставать друг от
друга по времени.

Пусть существует некоторый набор заказов. Заказ может находиться в
состоянии «обработан/необработан». Исходно коллекция содержит необработанные
заказы. Как только заказ изменяет статус на «обработан», его следует удалить
из коллекции. Разрешать всем пользователям коллекции удалять заказы, то
есть модифицировать коллекцию не очень разумно по причинам безопасности.
Коллекция WeakHashMap сама будет заботиться об удалении неактуальных
объектов, утративших ссылку на внешний ключ.

```java
class Order {
}

class Key {
    //  где класс Key содержит информацию об уникальном номере заказа и текущем статусе заказа.
    private int keyUnique;
    private boolean isProcessed;

    public Key(int keyUnique) {
        this.keyUnique = keyUnique;
    }

    public boolean isProcessed() {
        return isProcessed;
    }

    public void setProcessed(boolean processed) {
        isProcessed = processed;
    }
}

class CurrentOrders {
    private WeakHashMap<Key, Order> orders = new WeakHashMap<>();

    public Order put(Key key, Order value) {
        return orders.put(key, value);
    }

    public Order get(Object key) {
        return orders.get(key);
    }

    public int size() {
        return orders.size();
    }
}

public class Code {
    public static void main(String[] args) throws InterruptedException {
        CurrentOrders orders = new CurrentOrders();
        List<Key> keys = new ArrayList<>();
        keys.add(new Key(100));
        keys.add(new Key(220));
        keys.add(new Key(770));
        orders.put(keys.get(0), new Order());
        orders.put(keys.get(1), new Order());
        orders.put(keys.get(2), new Order());
        keys.get(1).setProcessed(true);
        final int size = keys.size();
        Iterator<Key> iterator = keys.iterator();
        while (iterator.hasNext()) {
            Key ordersKey = iterator.next();
            if (ordersKey.isProcessed()) {
                iterator.remove();
            }
        }
        System.out.println(orders.size());
        System.gc();
        Thread.sleep(1_000);
        System.out.println(orders.size());
    }
}
```

Чтобы сохранять ссылки на ключи, для них будет создан список. После этого
значение isProcessed у ключа будет изменено, он будет удален из списка ключей и,
как следствие, ключ из WeakHashMap утратит ссылку и станет целью для удаления
«сборщиком мусора». Удаление происходит не мгновенно, а только когда сработает
«сборщик мусора». И после этого число заказов уменьшится с 3 до 2.

## SortedMap

Интерфейс SortedMap расширяет Map и создает отображение, в котором все элементы
отсортированы в порядке возрастания их ключей.

**МЕТОДЫ:**

- K firstKey(): возвращает ключ первого элемента отображения
- K lastKey(): возвращает ключ последнего элемента отображения
- SortedMap<K, V> headMap(K end): возвращает отображение SortedMap, которые
  содержит все элементы оригинального SortedMap вплоть до элемента с ключом end
- SortedMap<K, V> tailMap(K start): возвращает отображение SortedMap, которые
  содержит все элементы оригинального SortedMap, начиная с элемента с ключом
  start
- SortedMap<K, V> subMap(K start, K end): возвращает отображение SortedMap,
  которые содержит все элементы оригинального SortedMap вплоть от элемента с
  ключом start до элемента с ключом end

## NavigableMap

Интерфейс NavigableMap расширяет интерфейс SortedMap и обеспечивает возможность
получения элементов отображения относительно других элементов.

**МЕТОДЫ:**

- Map.Entry<K, V> ceilingEntry(K key): возвращает элемент с наименьшим ключом k,
  который больше или равен ключу key (k >=key). Если такого ключа нет, то
  возвращается null.
- Map.Entry<K, V> floorEntry(K key): возвращает элемент с наибольшим ключом k,
  который меньше или равен ключу key (k <=key). Если такого ключа нет, то
  возвращается null.
- Map.Entry<K, V> higherEntry(K key): возвращает элемент с наименьшим ключом k,
  который больше ключа key (k >key). Если такого ключа нет, то возвращается
  null.
- Map.Entry<K, V> lowerEntry(K key): возвращает элемент с наибольшим ключом k,
  который меньше ключа key (k <key). Если такого ключа нет, то возвращается
  null.
- Map.Entry<K, V> firstEntry(): возвращает первый элемент отображения
- Map.Entry<K, V> lastEntry(): возвращает последний элемент отображения
- Map.Entry<K, V> pollFirstEntry(): возвращает и одновременно удаляет первый
  элемент из отображения
- Map.Entry<K, V> pollLastEntry(): возвращает и одновременно удаляет последний
  элемент из отображения
- K ceilingKey(K key): возвращает наименьший ключ k, который больше или равен
  ключу key (k >=key). Если такого ключа нет, то возвращается null.
- K floorKey(K key): возвращает наибольший ключ k, который меньше или равен
  ключу key (k <=key). Если такого ключа нет, то возвращается null.
- K lowerKey(K key): возвращает наибольший ключ k, который меньше ключа key (k <
  key). Если такого ключа нет, то возвращается null.
- K higherKey(K key): возвращает наименьший ключ k, который больше ключа key (
  k >key). Если такого ключа нет, то возвращается null.
- NavigableSet<K> descendingKeySet(): возвращает объект NavigableSet, который
  содержит все ключи отображения в обратном порядке
- NavigableMap<K, V> descendingMap(): возвращает отображение NavigableMap,
  которое содержит все элементы в обратном порядке
- NavigableSet<K> navigableKeySet(): возвращает объект NavigableSet, который
  содержит все ключи отображения
- NavigableMap<K, V> headMap(K upperBound, boolean incl): возвращает отображение
  NavigableMap, которое содержит все элементы оригинального NavigableMap вплоть
  от элемента с ключом upperBound. Параметр incl при значении true указывает,
  что элемент с ключом upperBound также включается в выходной набор.
- NavigableMap<K, V> tailMap(K lowerBound, boolean incl): возвращает отображение
  NavigableMap, которое содержит все элементы оригинального NavigableMap,
  начиная с элемента с ключом lowerBound. Параметр incl при значении true
  указывает, что элемент с ключом lowerBound также включается в выходной набор.
- NavigableMap<K, V> subMap(K lowerBound, boolean lowIncl, K upperBound, boolean
  highIncl): возвращает отображение NavigableMap, которое содержит все элементы
  оригинального NavigableMap от элемента с ключом lowerBound до элемента с
  ключом upperBound. Параметры lowIncl и highIncl при значении true включают в
  выходной набор элементы с ключами lowerBound и upperBound соответственно.

## TreeMap

Класс TreeMap<K, V> представляет отображение в виде дерева. Он наследуется от
класса AbstractMap и реализует интерфейс NavigableMap, а следовательно, также и
интерфейс SortedMap. Поэтому в отличие от коллекции HashMap в TreeMap все
объекты автоматически сортируются по возрастанию их ключей.

**Конструкторы:**

- TreeMap() - создает пустое отображение в виде дерева
- TreeMap(Map<? extends K, ? extends V> map) - создает дерево, в которое
  добавляет все элементы из отображения map
- TreeMap(SortedMap<K, ? extends V> smap) - создает дерево, в которое добавляет
  все элементы из отображения smap
- TreeMap(Comparator<? super K> comparator) - создает пустое дерево, где все
  добавляемые элементы впоследствии будут отсортированы компаратором.

```java
public class Program {
    public static void main(String[] args) {
        TreeMap<Integer, String> states = new TreeMap<Integer, String>();
        states.put(10, "Germany");
        states.put(2, "Spain");
        states.put(14, "France");
        states.put(3, "Italy");
        // получим объект по ключу 2
        String first = states.get(2);
        // перебор элементов
        for (Map.Entry<Integer, String> item : states.entrySet()) {
            System.out.printf("Key: %d  Value: %s \n", item.getKey(), item.getValue());
        }
        // получим весь набор ключей
        Set<Integer> keys = states.keySet();
        // получить набор всех значений
        Collection<String> values = states.values();
        // получаем все объекты, которые стоят после объекта с ключом 4
        Map<Integer, String> afterMap = states.tailMap(4);
        // получаем все объекты, которые стоят до объекта с ключом 10
        Map<Integer, String> beforeMap = states.headMap(10);
        // получим последний элемент дерева
        Map.Entry<Integer, String> lastItem = states.lastEntry();
        System.out.printf("Last item has key %d value %s \n", lastItem.getKey(), lastItem.getValue());
        Map<String, Person> people = new TreeMap<String, Person>();
        people.put("1240i54", new Person("Tom"));
        people.put("1564i55", new Person("Bill"));
        people.put("4540i56", new Person("Nick"));
        for (Map.Entry<String, Person> item : people.entrySet()) {
            System.out.printf("Key: %s  Value: %s \n", item.getKey(), item.getValue().getName());
        }
    }
}

class Person {
    private String name;

    public Person(String name) {
        this.name = name;
    }

    String getName() {
        return name;
    }
}
```

Кроме собственно методов интерфейса Map класс TreeMap реализует методы
интерфейса NavigableMap. Например, мы можем получить все объекты до или после
определенного ключа с помощью методов headMap и tailMap. Также мы можем получить
первый и последний элементы и провести ряд дополнительных манипуляций с
объектами.




