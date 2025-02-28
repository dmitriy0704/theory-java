# Optional

Класс Optional представляет собой оболочку для любого объектного типа. Помогает
работать в ситуации, когда объект возвращает null.

```java
class Order {
    long id;
    String name;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Order(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Order{" + "id=" + id + ", name='" + name + '\'' + '}';
    }
}

public class ExampleStart {
    public static void main(String[] args) {
        List<Order> orders = new ArrayList<>();
        orders.add(new Order(1L, "Alex"));
        orders.add(new Order(2L, "Bob"));
        orders.add(new Order(3L, "Charles"));
        Optional<Order> o = findById(orders, 4l);
        System.out.println(o);
    }

    public static Optional<Order> findById(List<Order> orders, long id) {
    
        // Без Optional:      Order order = null;

        List<Order> result = orders
                .stream().filter(o -> id == o.getId())
                .toList();

//        Без Optional:
//        if (result.size() != 0) {
//            order = result.get(0);
//        }

        Optional<Order> optionalOrder =
                result.size() != 0
                        ? Optional.of(result.get(0))
                        : Optional.empty();

        return optionalOrder;
    }
}
```

**Методы:**

- **static \<T> Optional<T> empty()** - создает пустой объект;
- **static \<T> Optional\<T> of(T value)** - создает объект с ненулевым
  содержимым;
- **static \<T> Optional\<T> ofNullable(T value)** - создает объект, содержимое,
  которого может быть null;
- **boolean equals(Object optional)** - возвращает true, если вызывающий объект
  равен optional;
- **\<T> get()** - извлекает объект из оболочки, генерирует
  NullPointerException, если оболочка пуста;
- **boolean isEmpty** - возвращает true, если объект не содержит значение;
- **boolean isPresent** - возвращает true, если объект содержит значение;
- **void ifPresent(Consumer\<? super T> action)** - если объект присутствует -
  выполняет над ним действие;
- **void ifPresentOrElse(Consumer\<? super T> action, Runnable emptyAction)** -
  если объект присутствует - выполняет над ним действие action, если
  отсутствует - emptyAction;
- **T orElse(T other)** - возвращает объект other;
- **T orElseGet(Supplier\<? extends T> getFunc)** - если вызывающий объект
  содержит значение, то оно возвращается. В противном случае возвращается
  значение полученное из getFunc;
- **T orElseThrow(), \<T extends Throwable> T orElseThrow(Supplier\<? extends X>
  exception)** - генерирует исключение NoSuchElements или заданное исключение;
- **Optional\<T> filter(Predicate\<? super T> condition)** - Возвращает
  экземпляр Optional, который содержит то же самое значение, что и вызывающий
  объект, если это значение удовлетворяет условию, указанному в condition. В
  противном случае возвращается пустой объект;
- **U Optional\<U> flatMap(Function\<? super T, Optional\<U>> mapFunc)** -
  Применяет функцию сопоставления, указанную в mapFunc, к вызывающему объекту,
  если этот объект содержит значение, и возвращает результат. В противном случае
  возвращается пустой объект;
- **U Optional\<U> map(Function\<? super T, ? extends U>> mapFunc)** - Применяет
  функцию сопоставления, указанную в mapFunc, к вызывающему объекту, если
  этот объект содержит значение, и возвращает результат. В противном случае
  возвращается пустой объект;
- **\<Х extends Throwable> Т orElseThrow (Supplier\<? extends Х> excFunc) throws
  Х extends Throwable Stream\<T> stream()** - Возвращает поток данных, который
  содержит значение вызывающего объекта. Если значение отсутствует, тогда поток
  не будет содержать значений;

