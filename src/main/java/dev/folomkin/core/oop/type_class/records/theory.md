# Типы классов

## Records

## Immutable и record

**Immutable**

Класс с модификатором final:

```java
public final class ImmutableType {
    private String name;
    private int id;

    public ImmutableType(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }
// equals, hashCode, toString
}
```

Такой объект от создания и до уничтожения не может быть изменен, что уменьшает
затраты на безопасность при использовании в конкурирующих операциях. Классов с
неизменяемым внутренним состоянием в стандартной реализации Java достаточно
много. Следует вспомнить класс String.  
Класс с поведением Immutable, тем не менее, может содержать методы для создания
объекта того же типа с внутренним состоянием, отличающимся от исходного, что
оправданно с точки зрения ресурсов, только если такие изменения происходят не
слишком часто.  
Если полем такого immutable класса необходимо объявить изменяемый тип, то
следует предусмотреть, чтобы соответствующий ему get-тер возвращал копию или
неизменяемый объект, а не ссылку на объект. Например, если поле такого класса
объявлено как

```code
List<String> strings;
```

то метод может выглядеть так:

```code
public List<String> getStrings(){
  String[] arr = {};
  return List.of(strings.toArray(arr))
}
```

**record**

Ключевое слово record представляет еще один способ создать класс с неизменяемыми
экземплярами. Этот класс является подклассом класса java.lang.Record и,
естественно, не может наследовать другой класс, а также сам не может выступать в
роли суперкласса. Реализация интерфейсов разрешается. Также класс record может
объявлять собственные методы.  
Такая запись гарантирует неизменяемость значений полей записи и избавляет
от необходимости создавать конструктор, методы equals(Object o), hashCode()
и toString(), которые для record генерируются автоматически. Вместо геттеров
генерируются методы с именем поля. В данном случае это name() и id().

```java
record ImmutableRec(String name, int id) {
  void method() {
  }
}
public class Example {
  public static void main(String[] args) {
    ImmutableRec immutableRec = new ImmutableRec("immutable", 1);
    System.out.println(immutableRec.id());
    System.out.println(immutableRec.name());
  }
}
```
