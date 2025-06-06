# ООП

Объектно-ориентированное программирование (ООП) в Java — это парадигма
программирования, основанная на концепции объектов, которые объединяют данные (
поля) и методы (поведение) для работы с этими данными. Java является одним из
самых популярных языков, поддерживающих ООП, и реализует его основные принципы:
**инкапсуляцию**, **наследование**, **полиморфизм** и **абстракцию**. Рассмотрим
их подробнее:

---

### 1. **Основные принципы ООП в Java**

#### Инкапсуляция

- **Суть**: Скрытие внутренней реализации объекта и предоставление доступа к
  данным только через публичные методы (геттеры и сеттеры).
- **Как реализуется в Java**:
    - Поля класса объявляются с модификатором `private`, чтобы ограничить прямой
      доступ.
    - Для доступа к полям создаются публичные методы (`public getX()` и
      `public setX()`).
    - Пример:
      ```java
      public class Person {
          private String name;
          private int age;
  
          public String getName() {
              return name;
          }
  
          public void setName(String name) {
              this.name = name;
          }
  
          public int getAge() {
              return age;
          }
  
          public void setAge(int age) {
              if (age >= 0) {
                  this.age = age;
              }
          }
      }
      ```
    - В этом примере данные `name` и `age` скрыты, а доступ к ним контролируется
      через методы, что позволяет, например, проверять корректность возраста.

#### Наследование

- **Суть**: Возможность одному классу (потомку) наследовать свойства и методы
  другого класса (родителя).
- **Как реализуется в Java**:
    - Используется ключевое слово `extends`.
    - Java поддерживает только **однократное наследование** (класс может
      наследовать только один родительский класс).
    - Пример:
      ```java
      public class Animal {
          public void eat() {
              System.out.println("This animal eats food.");
          }
      }
  
      public class Dog extends Animal {
          public void bark() {
              System.out.println("Woof!");
          }
      }
  
      public class Main {
          public static void main(String[] args) {
              Dog dog = new Dog();
              dog.eat(); // Унаследованный метод
              dog.bark(); // Собственный метод
          }
      }
      ```
    - Класс `Dog` наследует метод `eat()` от класса `Animal`.

#### Полиморфизм

- **Суть**: Возможность объектам одного типа вести себя по-разному в зависимости
  от их реализации или контекста.
- **Как реализуется в Java**:
    - **Статический полиморфизм** (перегрузка методов): методы с одинаковым
      именем, но разными параметрами.
    - **Динамический полиморфизм** (переопределение методов): использование
      аннотации `@Override` для изменения поведения метода родительского класса.
    - Пример:
      ```java
      public class Animal {
          public void makeSound() {
              System.out.println("Some generic animal sound");
          }
      }
  
      public class Cat extends Animal {
          @Override
          public void makeSound() {
              System.out.println("Meow!");
          }
      }
  
      public class Main {
          public static void main(String[] args) {
              Animal animal = new Cat(); // Полиморфизм
              animal.makeSound(); // Выведет "Meow!"
          }
      }
      ```
    - Переменная типа `Animal` ссылается на объект `Cat`, и вызывается метод
      `makeSound()` класса `Cat`.

#### Абстракция

- **Суть**: Сокрытие сложной реализации и предоставление только необходимого
  интерфейса для работы с объектом.
- **Как реализуется в Java**:
    - Используются **абстрактные классы** (`abstract class`) и **интерфейсы** (
      `interface`).
    - Абстрактный класс может содержать как реализованные, так и абстрактные
      методы (без реализации).
    - Интерфейсы содержат только сигнатуры методов (до Java 8) или могут
      включать методы по умолчанию (`default`) и статические методы.
    - Пример:
      ```java
      public interface Vehicle {
          void start(); // Абстрактный метод
          default void stop() {
              System.out.println("Vehicle stopped.");
          }
      }
  
      public class Car implements Vehicle {
          @Override
          public void start() {
              System.out.println("Car started.");
          }
      }
  
      public class Main {
          public static void main(String[] args) {
              Vehicle car = new Car();
              car.start(); // Выведет "Car started."
              car.stop(); // Выведет "Vehicle stopped."
          }
      }
      ```

---

### 2. **Ключевые элементы ООП в Java**

#### Классы и объекты

- **Класс** — это шаблон, описывающий свойства (поля) и поведение (методы)
  объектов.
- **Объект** — экземпляр класса, созданный с помощью оператора `new`.
- Пример:
  ```java
  public class Book {
      String title;
      int pages;

      public Book(String title, int pages) {
          this.title = title;
          this.pages = pages;
      }

      public void displayInfo() {
          System.out.println("Title: " + title + ", Pages: " + pages);
      }
  }

  public class Main {
      public static void main(String[] args) {
          Book book = new Book("Java Programming", 500);
          book.displayInfo(); // Выведет "Title: Java Programming, Pages: 500"
      }
  }
  ```

#### Модификаторы доступа

- Управляют видимостью полей, методов и классов:
    - `public`: доступно всем.
    - `protected`: доступно в пакете и в подклассах.
    - `default` (package-private): доступно только в пределах пакета.
    - `private`: доступно только внутри класса.

#### Конструкторы

- Специальные методы для создания объектов. Имя совпадает с именем класса, нет
  возвращаемого типа.
- Пример:
  ```java
  public class Student {
      String name;

      public Student(String name) {
          this.name = name;
      }
  }
  ```

#### Статические элементы

- Модификатор `static` делает поле или метод принадлежащим классу, а не объекту.
- Пример:
  ```java
  public class Counter {
      static int count = 0;

      public Counter() {
          count++;
      }

      public static int getCount() {
          return count;
      }
  }
  ```

#### Перегрузка и переопределение методов

- **Перегрузка**: методы с одинаковым именем, но разными параметрами (
  статический полиморфизм).
- **Переопределение**: изменение реализации метода родительского класса в
  подклассе с помощью `@Override`.

---

### 3. **Преимущества ООП в Java**

- **Модульность**: код организован в классы, что упрощает поддержку и
  масштабирование.
- **Повторное использование**: наследование и интерфейсы позволяют
  переиспользовать код.
- **Гибкость**: полиморфизм упрощает расширение функциональности.
- **Безопасность**: инкапсуляция защищает данные от несанкционированного
  доступа.

---

### 4. **Пример комплексного использования ООП**

```java
public abstract class Shape {
    public abstract double getArea(); // Абстрактный метод
}

public class Circle extends Shape {
    private double radius;

    public Circle(double radius) {
        this.radius = radius;
    }

    @Override
    public double getArea() {
        return Math.PI * radius * radius;
    }
}

public class Rectangle extends Shape {
    private double width, height;

    public Rectangle(double width, double height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public double getArea() {
        return width * height;
    }
}

public class Main {
    public static void main(String[] args) {
        Shape circle = new Circle(5);
        Shape rectangle = new Rectangle(4, 6);

        System.out.println("Circle area: " + circle.getArea()); // Площадь круга
        System.out.println("Rectangle area: " + rectangle.getArea()); // Площадь прямоугольника
    }
}
```

Этот пример демонстрирует абстракцию, наследование и полиморфизм: класс
`Shape` — абстрактный, `Circle` и `Rectangle` наследуют его, а метод `getArea()`
переопределяется для разных фигур.

---

### 5. **Полезные особенности Java для ООП**

- **Интерфейсы с `default` методами** (с Java 8): позволяют добавлять реализацию
  методов в интерфейсы.
- **Абстрактные классы**: используются, когда нужно частично реализовать
  функциональность.
- **Перечисления (`enum`)**: для создания типов с фиксированным набором
  значений.
- **Класс `Object`**: все классы в Java неявно наследуются от `Object`, что дает
  доступ к методам, таким как `toString()`, `equals()`, `hashCode()`.

---

Если нужно углубиться в какой-то аспект (например, шаблоны проектирования,
интерфейсы или работа с коллекциями в ООП), дайте знать!