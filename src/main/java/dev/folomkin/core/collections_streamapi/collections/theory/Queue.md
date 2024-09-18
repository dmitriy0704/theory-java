## ___Queue\<E> - интерфейсы и реализации___

**Queue** - это коллекция, элементы которой добавляются и удаляются по принципу
FIFO.
**Deque** - двусвязанная коллекция. Элементы добавляются и удалятся с обоих
концов.

***Методы:***  
**Вставка в конец очереди:**

- **boolean add(E e)** - вставляет элемент, в конец очереди, если есть место.
  Если места нет - возвращает исключение IllegalStateException. Функция
  возвращает true при успешной вставке. Метод add() идеален для случаев, когда
  вы уверены, что в очереди есть место, например, в LinkedList или
  PriorityQueue, которые не имеют ограничений по размеру. Этот метод будет
  подходящим решением в указанных условиях.
- **boolean offer(E e)** - вставляет элемент, в случае невозможности вставки
  возвращает false, не вызывая исключение.

**Извлечение элемента из начала очереди:**

- **E remove()** - возвращает и удаляет начало очереди, генерирует исключение
  NoSuchElement, если очередь пуста;
- **E element()** - возвращает, но не удаляет начало очереди, генерирует
  исключение NoSuchElement, если очередь пуста;
- **E poll()** - возвращает и удаляет начало очереди или null если очередь
  пуста;
- **E peek()** - извлекает и возвращает, но не удаляет начало очереди или null
  если очередь пуста;

## ___Deque\<E>___

***Методы:***

- **void addFirst(E e)** - добавляет элемент в начало двусторонней очереди;
- **void addLast(E e)** - добавляет элемент в конец двусторонней очереди;
- **boolean offerFirst(E e)** - вставляет элемент начало двусторонней очереди;
- **boolean offerLast(E e)** - вставляет элемент в конец двусторонней очереди;

**Извлечение и удаление:**

- **E removeFirst(E e)** - извлекает и удаляет элемент из начала очереди,
  генерирует исключение, если очередь пуста;
- **E removeLast(E e)** - извлекает и удаляет элемент из конца очереди,
  генерирует исключение, если очередь пуста;
- **E pollFirst(E e)** - извлекает и удаляет элемент из начала очереди,
  возвращает null, если очередь пуста;
- **E pollFirst(E e)** - извлекает и удаляет элемент из конца очереди,
  возвращает null, если очередь пуста;

**Извлечение без удаления:**

- **E getFirst(E e)** - извлекает первый элемент очереди;
- **E getLast(E e)** - извлекает последний элемент очереди;
- **E peekFirst()** - извлекает, но не удаляет начало очереди или null если
  очередь пуста;
- **E peekLast()** - извлекает, но не удаляет начало очереди или null если
  очередь пуста;

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

### Реализации для Queue и Deque:

- **PriorityQueue\<E>** - очередь, в которой элементы находятся в заданном
  порядке, а не в соответствии с принципом FIFO;
- **ArrayDeque\<E>** - Queue или Deque, организованные как динамический массив,
  похожий на ArrayList;
- **LinkedList\<E>** - класс LinkedList<E> также реализует интерфейсы Queue<E> и
  Deque<E> в дополнение к интерфейсу List<E>, обеспечивая очередь или дек как
  структуру данных «двусвязный список»;

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
