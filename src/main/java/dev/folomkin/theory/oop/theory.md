# ООП

## Классы и объекты

*Спецификатор класса:*

- public
- (не ставится, но означает)friendly/private-package - класс доступен только в
  пакете
- final - класс не может иметь подклассов
- abstract - класс содержит абстрактные нереализованные методы, объект такого
  класса создать нельзя

*Уровни доступа для членов классов:*

- public - доступен везде
- private - переменная доступна в рамках класса
- protected - доступ только внутри пакета и подклассам в других пакетах

## Статические методы и поля

Статические поля являются переменными класса, но не объекта. Могут быть
использованы без создания экземпляра класса. Нестатические методы могут
обращаться к статическим полям.

**Свойства статических методов:**

- статические методы являются методами класса, не привязаны ни к какому объекту
  и не содержат указателя this на конкретный экземпляр, вызвавший метод.
- статические методы реализуют парадигму «раннего связывания», жестко
  определяющую версию метода на этапе компиляции.
- По причине недоступности указателя this статические поля и методы не могут
  обращаться к нестатическим полям и методам напрямую, так как они не «знают», к
  какому объекту относятся, да и сам экземпляр класса может быть не создан;
- Для обращения к статическим полям и методам достаточно имени класса, в котором
  они определены;
- Переопределение статических методов невозможно, так как обращение к
  статическому методу осуществляется посредством имени класса, которому они
  принадлежат;

## Optional

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

### **Методы:**

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

## Модификатор Final

Методы объявленные как final нельзя замещать в подклассах. От класса с
модификатором final нельзя создать подкласс.
