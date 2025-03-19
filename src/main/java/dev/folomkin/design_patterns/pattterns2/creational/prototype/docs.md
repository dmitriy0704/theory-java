# Прототип(Prototype)

- это порождающий паттерн, который позволяет копировать объекты любой сложности
  без привязки к их конкретным классам.

Все классы—Прототипы имеют общий интерфейс. Поэтому вы можете копировать
объекты, не обращая внимания на их конкретные типы и всегда быть уверены, что
получите точную копию. Клонирование совершается самим объектом-прототипом, что
позволяет ему скопировать значения всех полей, даже приватных.

**Применимость**: Паттерн Прототип реализован в базовой библиотеке Java
посредством
интерфейса Cloneable.

Любой класс может реализовать этот интерфейс, чтобы позволить собственное
клонирование.

java.lang.Object#clone() (класс должен реализовать интерфейс
java.lang.Cloneable)

**Признаки применения паттерна:** Прототип легко определяется в коде по наличию
методов clone, copy и прочих.

## Структура

![prototype_structure.png](/img/design_pattern/design_patterns/prototype_structure.png)

1. Интерфейс прототипов описывает операции клонирования. В большинстве случаев —
   это единственный метод clone.
2. Конкретный прототип реализует операцию клонирования
   самого себя. Помимо банального копирования значений
   всех полей, здесь могут быть спрятаны различные сложности, о которых не
   нужно знать клиенту. Например, клонирование связанных объектов, распутывание
   рекурсивных зависимостей и прочее.
3. Клиент создаёт копию объекта, обращаясь к нему через общий интерфейс
   прототипов.

## Примеры кода

```java
// # Копирование графических фигур

// Рассмотрим пример реализации Прототипа без использования интерфейса Cloneable.

// ->  shapes: Список фигур

// ->  shapes/Shape.java: Общий интерфейс фигур

/**
 * Базовый прототип.
 */
public abstract class Shape {
    public int x;
    public int y;
    public String color;

    /**
     * Обычный конструктор.
     */
    public Shape() {
    }

    /**
     * Конструктор прототипа.
     */
    public Shape(Shape target) {
        if (target != null) {
            this.x = target.x;
            this.y = target.y;
            this.color = target.color;
        }
    }

    /**
     * Результатом операции клонирования всегда будет объект из иерархии классов Shape. 
     */
    public abstract Shape clone();

    @Override
    public boolean equals(Object object2) {
        if (!(object2 instanceof Shape)) return false;
        Shape shape2 = (Shape) object2;
        return shape2.x == x && shape2.y == y && Objects.equals(shape2.color, color);
    }
}


// ->  shapes/Circle.java: Простая фигура

/**
 * Конкретный прототип. Метод клонирования создаёт новый объект текущего класса,
 * передавая в его конструктор ссылку на собственный объект. Благодаря этому операция 
 * клонирования получается атомарной — пока не выполнится конструктор, нового объекта ещё не
 * существует. Но как только конструктор завершит работу, мы получим полностью готовый объект-клон,
 * а не пустой объект, который нужно ещё заполнить.
 */

public class Circle extends Shape {
    public int radius;

    public Circle() {
    }

    public Circle(Circle target) {
        /**
         * Вызов родительского конструктора нужен, чтобы скопировать потенциальные 
         * приватные поля, объявленныев родительском классе.
         */
        super(target);
        if (target != null) {
            this.radius = target.radius;
        }
    }

    @Override
    public Shape clone() {
        return new Circle(this);
    }

    @Override
    public boolean equals(Object object2) {
        if (!(object2 instanceof Circle) || !super.equals(object2))
            return false;
        Circle shape2 = (Circle) object2;
        return shape2.radius == radius;
    }
}

// ->  shapes/Rectangle.java: Фигура чуть сложнее

public class Rectangle extends Shape {
    public int width;
    public int height;

    public Rectangle() {
    }

    public Rectangle(Rectangle target) {
        super(target);
        if (target != null) {
            this.width = target.width;
            this.height = target.height;
        }
    }

    @Override
    public Shape clone() {
        return new Rectangle(this);
    }

    @Override
    public boolean equals(Object object2) {
        if (!(object2 instanceof Rectangle) || !super.equals(object2))
            return false;
        Rectangle shape2 = (Rectangle) object2;
        return shape2.width == width && shape2.height == height;
    }
}

// -> Demo.java: Пример клонирования


/**
 * Плюс Прототипа в том, что вы можете клонировать набор объектов, 
 * не зная их конкретные классы.
 * */

public class Demo {
    public static void main(String[] args) {
        List<Shape> shapes = new ArrayList<>();
        List<Shape> shapesCopy = new ArrayList<>();

        Circle circle = new Circle();
        circle.x = 10;
        circle.y = 20;
        circle.radius = 15;
        circle.color = "red";
        shapes.add(circle);

        Circle anotherCircle = (Circle) circle.clone();
        shapes.add(anotherCircle);

        Rectangle rectangle = new Rectangle();
        rectangle.width = 10;
        rectangle.height = 20;
        rectangle.color = "blue";
        shapes.add(rectangle);

        cloneAndCompare(shapes, shapesCopy);
    }

    private static void cloneAndCompare(List<Shape> shapes, List<Shape> shapesCopy) {

        /**
         *  Например, мы не знаем, какие конкретно объекты находятся внутри массива 
         *  shapes, так как он объявлен с типом Shape. Но благодаря полиморфизму, мы 
         *  можем клонировать все объекты «вслепую». Будет выполнен метод clone того класса,
         *  которым является этот объект.
         */

        for (Shape shape : shapes) {
            /**
             * Переменная shapesCopy будет содержать точные копии элементов
             * массива shapes.
             */
            shapesCopy.add(shape.clone());
        }

        for (int i = 0; i < shapes.size(); i++) {
            if (shapes.get(i) != shapesCopy.get(i)) {
                System.out.println(i + ": Shapes are different objects (yay!)");
                if (shapes.get(i).equals(shapesCopy.get(i))) {
                    System.out.println(i + ": And they are identical (yay!)");
                } else {
                    System.out.println(i + ": But they are not identical (booo!)");
                }
            } else {
                System.out.println(i + ": Shape objects are the same (booo!)");
            }
        }
    }
}



```

## Шаги реализации

1. Создайте интерфейс прототипов с единственным методом
   clone. Если у вас уже есть иерархия продуктов, метод клонирования можно
   объявить непосредственно в каждом из её классов.
2. Добавьте в классы будущих прототипов альтернативный
   конструктор, принимающий в качестве аргумента объект
   текущего класса. Этот конструктор должен скопировать из
   поданного объекта значения всех полей, объявленных в
   рамках текущего класса, а затем передать выполнение
   родительскому конструктору, чтобы тот позаботился о полях,
   объявленных в суперклассе.
3. Метод клонирования обычно состоит всего из одной строки: вызова оператора new
   с конструктором прототипа. Все классы, поддерживающие клонирование, должны
   явно определить метод clone, чтобы использовать собственный класс с
   оператором new. В обратном случае результатом клонирования станет объект
   родительского класса.
4. Опционально, создайте центральное хранилище прототипов. В нём удобно хранить
   вариации объектов, возможно, даже одного класса, но по-разному настроенных.
   Вы можете разместить это хранилище либо в новом фабричном классе, либо в
   фабричном методе базового класса прототипов. Такой фабричный метод должен на
   основании входящих аргументов искать в хранилище прототипов подходящий
   экземпляр, а затем вызывать его метод клонирования и возвращать полученный
   объект. Наконец, нужно избавиться от прямых вызовов конструкторов объектов,
   заменив их вызовами фабричного метода хранилища прототипов.



