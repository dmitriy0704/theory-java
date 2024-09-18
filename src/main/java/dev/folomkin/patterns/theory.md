# Шаблоны проектирования

## Отношения классов:

![i1.png](/img/design_pattern/class_relations/i1.png)

Агрегация (aggregation) — описывает связь «часть»–«целое», в котором «часть»
может существовать отдельно от «целого». Ромб указывается со стороны «целого».

![i1.png](/img/design_pattern/class_relations/i2.png)

Композиция (composition) — подвид агрегации, в которой «части» не могут
существовать отдельно от «целого».

![i1.png](/img/design_pattern/class_relations/i3.png)

Зависимость (dependency) — изменение в одной сущности (независимой) может влиять
на состояние или поведение другой сущности (зависимой). Со стороны стрелки
указывается независимая сущность.

![i1.png](/img/design_pattern/class_relations/i4.png)

Обобщение (generalization) — отношение наследования или реализации интерфейса.
Со стороны стрелки находится суперкласс или интерфейс.

## Виды паттернов:

- Behavioral (B) - поведенческие;
- Creational (C) - порождающие;
- Structural (S) - структурные;

### Поведенческие:

- **_Хранитель (memento)_**
- **_Цепочка обязанностей (chain of responsibility)_**
- Наблюдатель (observer)
- Команда (command)
- Состояние (state)
- Интерпретатор (interpreter)
- Стратегия (strategy)
- Итератор (iterator)
- Шаблонный метод (template method) -
- Посетитель (visitor)
- Посредник (mediator)

### Порождающие:

- Строитель (builder)
- Фабричный метод (factory method)
- Абстрактная фабрика (abstract factory)
- Прототип (prototype)
- Одиночка (singleton)

### Структурные:

- Адаптер (adapter)
- Мост (bridge)
- Компоновщик (composite)
- Декоратор (decorator)
- Фасад (facade)
- Приспособленец (flyweight)
- Прокси (proxy)

## Шаблоны подробно

### Одиночка (Singleton, порождающий)

В этом шаблоне можно создать только один экземпляр класса. Даже если создано
несколько ссылочных переменных, все они будут указывать на один и тот же объект.

```java
class Probe {
    // Instance variables
    // Important methods

    // Конструктор приватный, чтобы не создавалось несколько объектов
    private Probe() {
        // Initialize variables here
    }

    //Каждый раз, когда вызывается этот метод, он возвращает один и тот же
    // объект. Таким образом, шаблон способен блокировать создание нескольких
    // объектов.
    private static Probe getInstance(Probe probe) {
        if (probe == null)
            probe = new Probe();
        return probe;
    }
}
```

### Наблюдатель (observer, поведенческий)

Один объект отслеживает изменения другого.

Пример реализации:

Допустим, вы подписаны на страницу в социальной сети.
Всякий раз, когда на этой странице добавляется новая запись,
вы хотели бы получать уведомления об этом.

Итак, в том случае, если один объект (страница) выполняет действие
(добавляет пост), другой объект (подписчик) получает уведомление.

Этот сценарий может быть реализован с помощью шаблона наблюдателя.
Давайте создадим класс страницы и интерфейс подписчика.

На странице могут быть разные типы подписчиков: обычный пользователь, рекрутер
и официальное лицо.
У нас будет класс для каждого типа подписчика, и все классы будут
реализовывать интерфейс подписчика.

Здесь класс страницы - это тема, а подписчики - классы наблюдателей.
Если тема меняет своё состояние (страница добавляет новую запись),
все наблюдатели, то есть подписчики, получают уведомление.

Класс страницы будет содержать следующие методы:

- registerFollower() - Этот метод регистрирует новых подписчиков.
- notifyFollowers() - Этот метод уведомляет всех подписчиков о том, что на
  странице появилась новая запись.
- getLatestPost() и addNewPost() - получатель и установщики для последней записи
  на странице.

С другой стороны, интерфейс последователя имеет только один метод update(),
который будет переопределен типами последователей, реализующими этот
интерфейс, также называемыми конкретными наблюдателями.

```java
interface Follower {
    // Метод вызывается, когда на странице появляется новая запись
    void update();
}

class Page {
    private ArrayList<Follower> followers;
    String latestPost;

    public Page() {
        followers = new ArrayList<>();
    }

    public void registerFollower(Follower f) {
        followers.add(f);
    }

    public void notifyFollowers() {
        for (int i = 0; i < followers.size(); i++) {
            Follower follower = followers.get(i);
            follower.update();
        }
    }

    public String getLatestPost() {
        return latestPost;
    }

    public void addNewPost(String post) {
        this.latestPost = post;
        notifyFollowers();
    }
}
```

В этом классе у нас есть список всех подписчиков. Когда новый подписчик хочет
перейти на страницу, он вызывает метод registerFollower(). latestPost содержит
новую запись, добавленную страницей.  
Когда добавляется новая запись, вызывается метод notifyFollowers(), где он
перебирает каждого подписчика и уведомляет их, вызывая метод update().

Первый подписчик:

```java
class User implements Follower {
    Page page;

    public User(Page page) {
        this.page = page;
        page.registerFollower(this);
    }

    public void update() {
        System.out.println("Latest post seen by a normal user: " + page.getLatestPost());
    }
}
```
Когда создаётся новый пользовательский объект, он выбирает страницу, на которую
хочет перейти, и регистрируется для неё. Когда страница добавляет новую запись,
пользователь получает уведомление с помощью метода update().

Ещё два класса, которые будут следить за страницей:

```java
class Recruiter implements Follower {
    String company;
    Page page;

    public Recruiter(Page page) {
        this.page = page;
        page.registerFollower(this);
    }

    public void update() {
        System.out.println("Latest post seen by a normal user: " + page.getLatestPost());
    }
}

class Official implements Follower {
    String designation;

    Page page;

    public Official(Page page) {
        this.page = page;
        page.registerFollower(this);
    }

    public void update() {
        System.out.println("Latest post seen by a normal user: " + page.getLatestPost());
    }
}

```

Итог работы:
```java
package dev.folomkin.patterns;

import java.util.ArrayList;

// Интерфейс подписчика на страницу
interface Follower {
  // Метод вызывается, когда на странице появляется новая запись
  void update();
}


class User implements Follower {
  Page page;

  public User(Page page) {
    this.page = page;
    page.registerFollower(this);
  }

  public void update() {
    System.out.println("Latest post seen by a normal user: " + page.getLatestPost());
  }
}

class Recruiter implements Follower {
  String company;
  Page page;

  public Recruiter(Page page) {
    this.page = page;
    page.registerFollower(this);
  }

  public void update() {
    System.out.println("Latest post seen by a normal user: " + page.getLatestPost());
  }
}

class Official implements Follower {
  String designation;

  Page page;

  public Official(Page page) {
    this.page = page;
    page.registerFollower(this);
  }

  public void update() {
    System.out.println("Latest post seen by a normal user: " + page.getLatestPost());
  }
}


class Page {
  private ArrayList<Follower> followers;
  String latestPost;

  public Page() {
    followers = new ArrayList<>();
  }

  public void registerFollower(Follower f) {
    followers.add(f);
  }

  public void notifyFollowers() {
    for (int i = 0; i < followers.size(); i++) {
      Follower follower = followers.get(i);
      follower.update();
    }
  }

  public String getLatestPost() {
    return latestPost;
  }

  public void addNewPost(String post) {
    this.latestPost = post;
    notifyFollowers();
  }
}

public class Code {
  public static void main(String[] args) {
    Page page = new Page();
    page.addNewPost("I am feeling lucky!");
    //-> Страница создана, но никто не подписан

    User user = new User(page);
    page.addNewPost("It's a beautiful day!");
    //-> User подписан и получает уведомление
    
    Recruiter recruiter = new Recruiter(page);
    Official official = new Official(page);
    page.addNewPost("Ready to go for a run!!");
    //-> Остальные подписаны и уведомлены
  }
}
```


### Стратегия (strategy, поведенческий)

```java
package dev.folomkin.patterns;


interface Diveable {
    void dive();
}

class DiveBehaviour implements Diveable {
    public void dive() {
        // Implementation here
    }
}

class NoDiveBehaviour implements Diveable {
    public void dive() {
        // Implementation here
    }
}

abstract class Boat {
    Diveable diveable;

    void sway() {
        // ...
    }

    void roll() {
        // ...
    }

    abstract void present();

    public void performDive() {
        diveable.dive();
    }
}

class FishBoat extends Boat {
    public FishBoat() {
        diveable = new NoDiveBehaviour();
    }

    void present() {
        // ...
    }
}

class DinghyBoat extends Boat {
    public DinghyBoat() {
        diveable = new NoDiveBehaviour();
    }

    void present() {
        // ...
    }
}

class SubBoat extends Boat {
    // ...
    public SubBoat() {
        diveable = new DiveBehaviour();
    }

    void present() {
        // ...
    }
    // ...
}

public class Code {
    public static void main(String[] args) {
        Boat fishBoat = new FishBoat();
        fishBoat.performDive();
        Boat subBoat = new SubBoat();
        subBoat.performDive();
    }
}
```


### Декоратор (decorator, структурный)

```java
package dev.folomkin.patterns;

abstract class Car {
    abstract void build();
}

abstract class CarModifications extends Car {
    Car car;

    public CarModifications(Car car) {
        this.car = car;
    }
}

class Spoiler extends CarModifications {
    public Spoiler(Car car) {
        super(car);
    }

    public void build() {
        car.build();
        addSpoiler();
    }

    void addSpoiler() {
        System.out.println("Spoiler built");
    }
}

class Ford extends Car {
    public void build() {
        System.out.println("Ford built");
    }
}

class Audi extends Car {
    public void build() {
        System.out.println("Audi built");
    }
}

public class Code {
    public static void main(String[] args) {
        Car audi = new Audi();
        Car audiWithSpoiler = new Spoiler(audi);
        audiWithSpoiler.build();
    }
}
```


## Фабричный метод (factory method, порождающий)

