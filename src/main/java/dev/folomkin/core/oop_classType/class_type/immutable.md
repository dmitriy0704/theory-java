# Immutable

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
