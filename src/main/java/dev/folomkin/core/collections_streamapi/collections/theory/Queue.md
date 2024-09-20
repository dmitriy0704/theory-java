
## _Queue\<E>_

**Queue** - это коллекция, элементы которой добавляются и удаляются по принципу
FIFO.
**Deque** - двусвязанная коллекция. Элементы добавляются и удалятся с обоих
концов.

**Queue\<E> пример:**

```java
public class ExampleStart {
    public static void main(String[] args) {
        Queue<String> queue = new LinkedList<>();

        //offer(), возвращает true или false
        queue.add("Понедельник");
        queue.add("Вторник");
        boolean flag = queue.offer("Среда");
        System.out.println(queue + ", " + flag);

        //add(), генерирует исключение IllegalStateException
        try {
            queue.add("Четверг");
            queue.add("Пятница");
            queue.add("Воскресенье");
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
        System.out.println("Удаляем начало очереди: " + queue.peek()); // <- Тут просто выводим начало очереди.
        String head = null;
        try {
            // для удаления используем метод remove()
            head = (String) queue.remove();

            System.out.println("1) Изъяли " + head + " из очереди");
            System.out.println("Теперь новая голова: " + queue.element());
        } catch (NoSuchElementException e) {
            e.printStackTrace();
        }

        // Удаление элемента из начала очереди с помощью метода poll();
        head = (String) queue.poll();
        System.out.println("2) Изъяли " + head + " из очереди");
        System.out.println("Теперь новая голова: " + queue.peek());

        // Проверяем, содержит ли очередь данный объект
        System.out.println("Содержит ли очередь Воскресенье: "
                + queue.contains("Воскресенье"));
        System.out.println("Содержит ли очередь Понедельник: "
                + queue.contains("Понедельник"));

    }
}
```

**_Методы:_**  
**_Вставка в конец очереди:_**

- **boolean add(E e)** - вставляет элемент, в конец очереди, если есть место.
  Если места нет - возвращает исключение IllegalStateException. Функция
  возвращает true при успешной вставке. Метод add() идеален для случаев, когда
  вы уверены, что в очереди есть место, например, в LinkedList или
  PriorityQueue, которые не имеют ограничений по размеру. Этот метод будет
  подходящим решением в указанных условиях.
- **boolean offer(E e)** - вставляет элемент, в случае невозможности вставки
  возвращает false, не вызывая исключение.

**_Извлечение элемента из начала очереди:_**

- **E remove()** - возвращает и удаляет начало очереди, генерирует исключение
  NoSuchElement, если очередь пуста;
- **E element()** - возвращает, но не удаляет начало очереди, генерирует
  исключение NoSuchElement, если очередь пуста;
- **E poll()** - возвращает и удаляет начало очереди или null если очередь
  пуста;
- **E peek()** - извлекает и возвращает, но не удаляет начало очереди или null
  если очередь пуста;

```java
public static void main(String[] args) {
    Queue<String> queue = new LinkedList<>();
    queue.offer("Jeans");
    queue.add("Dress");
    queue.offer("Gloves");
    queue.offer("T-Shirt");

    queue.stream()
            .filter(s -> !s.endsWith("s"))
            .forEach(System.out::println);
    System.out.println("_____");
    queue.forEach(System.out::println);
}
```

### _PriorityQueue\<E>_

В такую очередь элементы добавляются не в порядке «живой очереди», а в порядке,
определяемом в классе, экземпляры которого содержатся в очереди. Сам порядок
следования задан реализацией интерфейсов Comparator<E> или Comparable<E>.
По умолчанию компаратор размещает элементы в очереди в порядке возрастания,
который можно изменить на убывание, если компаратору задать reverseOrder().

```java
    public static void main(String[] args) {
    Queue<String> queue = new PriorityQueue<>(Comparator.reverseOrder());
    queue.offer("J");
    queue.offer("A");
    queue.offer("V");
    queue.offer("A");

    while (!queue.isEmpty()) {
        System.out.println(queue.poll());
        // ->
        // V
        // J
        // A
        // A
    }
}
```

Если порядок в классе не определен, а именно, не реализован ни один из указанных
интерфейсов, то генерируется исключительная ситуация ClassCastException
при добавлении второго элемента в очередь. При добавлении первого элемента
компаратор не вызывается, так как сравнивать объект не с кем.

```
PriorityQueue<Order> orders = new PriorityQueue();
orders.add(new Order(546, 53.)); // ok
orders.add(new Order(146, 17.)); // runtime error
```

С другой стороны, класс String реализует интерфейс Comparable<E>, тогда будет:

```
PriorityQueue<String> orders = new PriorityQueue<String>();
orders.add(new String("Oracle")); // ok
orders.add(new String("Google")); // ok
```

Если попытаться заменить тип String на StringBuilder или StringBuffer, то
создать очередь PriorityQueue так просто не удастся, как и в случае добавления
объектов класса Order. Решением такой задачи будет создание необходимого
класса-компаратора или класса-оболочки с полем типа StringBuilder и реализацией
интерфейса Comparable<T>.

```java
public class StringBuilderWrapper implements Comparable<StringBuilder> {
    private StringBuilder content = new StringBuilder();

    @Override
    public int compareTo(StringBuilder o) {
        return content.toString().compareTo(o.toString());
    }
}
```

Предлагаемое решение универсально для любых пользовательских типов.
Применимо для создания упорядоченных множеств вида TreeSet<T>, которые
используют Comparable или Comparator для сортировки при добавлении экземпляра в
множество.

## Deque\<E>

Интерфейс Deque определяет «двунаправленную» очередь и, соответственно, методы
доступа к первому и последнему элементам двусторонней очереди.

Наличие методов pop и push позволяет классам, реализующим Deque,
действовать в качестве стека. В то же время, имеющийся функционал также
позволяет создавать двунаправленные очереди, что делает классы, применяющие
данный интерфейс, довольно гибкими.

***Методы:***

- **void addFirst(E e)** - добавляет элемент в начало очереди;
- **void addLast(E e)** - добавляет элемент в конец очереди;
- **boolean offerFirst(E obj)** - добавляет элемент obj в самое начало очереди.
  Если элемент удачно добавлен, возвращает true, иначе - false;
- **boolean offerLast(E obj)** - добавляет элемент obj в конец очереди. Если
  элемент удачно добавлен, возвращает true, иначе - false;
- **void push(E element)** - добавляет элемент в самое начало очереди

**Извлечение и удаление:**

- **E removeFirst()** - возвращает с удалением элемент из начала очереди. Если
  очередь пуста, генерирует исключение NoSuchElementException
- **E removeLast()** - возвращает с удалением элемент из конца очереди. Если
  очередь пуста, генерирует исключение NoSuchElementException
- **boolean removeFirstOccurrence(Object obj)** - удаляет первый встреченный
  элемент obj из очереди. Если удаление произошло, то возвращает true, иначе
  возвращает false.
- **boolean removeLastOccurrence(Object obj)** - удаляет последний встреченный
  элемент obj из очереди. Если удаление произошло, то возвращает true, иначе
  возвращает false.
- **E pollFirst()** - возвращает с удалением элемент из начала очереди. Если
  очередь пуста, возвращает значение null
- **E pollLast()** - возвращает с удалением последний элемент очереди. Если
  очередь пуста, возвращает значение null
- **E pop(**) - возвращает с удалением элемент из начала очереди. Если очередь
  пуста, генерирует исключение NoSuchElementException

**Извлечение без удаления:**

- **E getFirst(E e)** - возвращает без удаления элемент из головы очереди. Если
  очередь пуста, генерирует исключение NoSuchElementException;
- **E getLast(E e)** - возвращает без удаления последний элемент очереди. Если
  очередь пуста, генерирует исключение NoSuchElementException;
- E peekFirst(): возвращает без удаления элемент из начала очереди. Если очередь
  пуста, возвращает значение null
- E peekLast(): возвращает без удаления последний элемент очереди. Если очередь
  пуста, возвращает значение null

**Deque методы кратко:**

![deque-methods.png](/img/collection/deque-methods.png)

**Сравнение Queue и Deque методов**

| Queue Method | Equivalent Deque Method |
|--------------|-------------------------|
| add(e)       | addLast(e)              |
| offer(e)     | offerLast(e)            |
| remove()     | removeFirst()           |
| poll()       | pollFirst()             |
| element()    | getFirst()              |
| peek()       | peekFirst()             |

**Сравнение Stack и Deque методов**

| Stack Method | Equivalent Deque Method |
|--------------|-------------------------|
| push(e)      | addFirst(e)             |
| pop()        | removeFirst()           |
| peek()       | peekFirst()             |

**_Реализации для Queue и Deque:_**

- **PriorityQueue\<E>** - очередь, в которой элементы находятся в заданном
  порядке, а не в соответствии с принципом FIFO;
- **ArrayDeque\<E>** - Queue или Deque, организованные как динамический массив,
  похожий на ArrayList;
- **LinkedList\<E>** - класс LinkedList<E> также реализует интерфейсы Queue<E> и
  Deque<E> в дополнение к интерфейсу List<E>, обеспечивая очередь или дек как
  структуру данных «двусвязный список»;

### ArrayDeque\<E>

Класс ArrayDeque<E> быстрее, чем Stack<E>, если используется как стек,
и быстрее, чем LinkedList<E>, если используется в качестве очереди.

В классе ArrayDeque определены следующие конструкторы:

- ArrayDeque(): создает пустую очередь;
- ArrayDeque(Collection<? extends E> col): создает очередь, наполненную
  элементами из коллекции col;
- ArrayDeque(int capacity): создает очередь с начальной емкостью capacity. Если
  мы явно не указываем начальную емкость, то емкость по умолчанию будет равна
  16;

```java
public static void main(String[] args) {
  Deque<Integer> stack = new ArrayDeque<>();
  stack.push(1);
  stack.push(2);
  stack.push(3);
  while (!stack.isEmpty()) {
    System.out.println(stack.pop());
  }
  // -> Для стека:
  // 3
  // 2
  // 1

  Deque<Integer> queue = new ArrayDeque<>();
  queue.offer(11);
  queue.offer(22);
  queue.offer(33);
  while (!queue.isEmpty()) {
    System.out.println(queue.poll());
  }

  // -> Для очереди:
  // 11
  // 22
  // 33
}
```