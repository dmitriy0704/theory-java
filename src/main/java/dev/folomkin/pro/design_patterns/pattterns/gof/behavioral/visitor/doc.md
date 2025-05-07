# Посетитель (Visitor)

- это поведенческий паттерн проектирования, позволяющий добавлять в
  программу новые операции, не изменяя классы объектов, над которыми эти
  операции могут выполняться.

Применимость: Посетитель нечасто встречается в Java-коде из-за своей сложности и
нюансов реализазации.

Примеры Посетителей в стандартных библиотеках Java:

javax.lang.model.element.AnnotationValue и AnnotationValueVisitor
javax.lang.model.element.Element и ElementVisitor
javax.lang.model.type.TypeMirror и TypeVisitor
java.nio.file.FileVisitor и SimpleFileVisitor
javax.faces.component.visit.VisitContext и VisitCallback

## Структура

![visitor_structure.png](/img/design_pattern/design_patterns/visitor_structure.png)

1. Посетитель описывает общий интерфейс для всех типов посетителей. Он объявляет
   набор методов, отличающихся типом входящего параметра, которые нужны для
   запуска операции для всех типов конкретных элементов. В языках,
   поддерживающих перегрузку методов, эти методы могут иметь одинаковые имена,
   но типы их параметров должны отличаться.
2. Конкретные посетители реализуют какое-то особенное поведение для всех типов
   элементов, которые можно подать через методы интерфейса посетителя.
3. Элемент описывает метод принятия посетителя.Этотметод должен иметь
   единственный параметр, объявленный с типом интерфейса посетителя.
4. Конкретные элементы реализуютметодыпринятия посетителя. Цель этого метода —
   вызвать тот метод посещения, который соответствует типу этого элемента. Так
   посетитель узнает, с каким именно элементом он работает.
5. Клиентом зачастую выступает коллекция или сложный составной объект, например,
   дерево Компоновщика. Зачастую клиент не привязан к конкретным классам
   элементов, работая с ними через общий интерфейс элементов.

## Пример кода

Сериализация объектов в XML

В нашем примере классы геометрических фигур не могут сами экспортировать своё
состояние в XML. Представьте, что у вас нет доступа к их коду.

Однако с помощью Посетителя мы можем прикрутить любое поведение к этой
иерархии (с оговоркой, что в ней будет реализован метод accept).

> ![visitor_example.png](/img/design_pattern/design_patterns/visitor_example.png)
> Пример организации экспорта объектов в XML через отдельный класс-посетитель.

```java
package dev.folomkin.design_patterns.pattterns.behavioral.visitor;

import java.util.ArrayList;
import java.util.List;

//-> ./shapes
//-> ./shapes/Shape.java: Общий интерфейс фигур

/**
 * Сложная иерархия элементов.
 */

interface Shape {
    void move(int x, int y);

    void draw();


    /**
     * Метод принятия посетителя должен быть реализован в каждом
     * элементе, а не только в базовом классе. Это поможет программе
     * определить, какой метод посетителя нужно вызвать, если вы не
     * знаете тип элемента.
     */

    String accept(dev.folomkin.pro.design_patterns.pattterns.gof.behavioral.visitor.Visitor visitor);
}

//-> ./shapes/Dot.java: Точка

class Dot implements dev.folomkin.pro.design_patterns.pattterns.gof.behavioral.visitor.Shape {
    private int id;
    private int x;
    private int y;

    public Dot() {
    }

    public Dot(int id, int x, int y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }

    @Override
    public void move(int x, int y) {
        // move shape
    }

    @Override
    public void draw() {
        // draw shape
    }

    @Override
    public String accept(dev.folomkin.pro.design_patterns.pattterns.gof.behavioral.visitor.Visitor visitor) {
        return visitor.visitDot(this);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getId() {
        return id;
    }
}

//-> ./shapes/Circle.java: Круг

class Circle extends dev.folomkin.pro.design_patterns.pattterns.gof.behavioral.visitor.Dot {
    private int radius;

    public Circle(int id, int x, int y, int radius) {
        super(id, x, y);
        this.radius = radius;
    }

    @Override
    public String accept(dev.folomkin.pro.design_patterns.pattterns.gof.behavioral.visitor.Visitor visitor) {
        return visitor.visitCircle(this);
    }

    public int getRadius() {
        return radius;
    }
}

//-> ./shapes/Rectangle.java:Четырёхугольник

class Rectangle implements dev.folomkin.pro.design_patterns.pattterns.gof.behavioral.visitor.Shape {
    private int id;
    private int x;
    private int y;
    private int width;
    private int height;

    public Rectangle(int id, int x, int y, int width, int height) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    @Override
    public String accept(dev.folomkin.pro.design_patterns.pattterns.gof.behavioral.visitor.Visitor visitor) {
        return visitor.visitRectangle(this);
    }

    @Override
    public void move(int x, int y) {
        // move shape
    }

    @Override
    public void draw() {
        // draw shape
    }

    public int getId() {
        return id;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}

//-> ./shapes/CompoundShape.java:
//-> Составная фигура


class CompoundShape implements dev.folomkin.pro.design_patterns.pattterns.gof.behavioral.visitor.Shape {
    public int id;
    public List<dev.folomkin.pro.design_patterns.pattterns.gof.behavioral.visitor.Shape> children = new ArrayList<>();

    public CompoundShape(int id) {
        this.id = id;
    }

    @Override
    public void move(int x, int y) {
        // move shape
    }

    @Override
    public void draw() {
        // draw shape
    }

    public int getId() {
        return id;
    }

    @Override
    public String accept(dev.folomkin.pro.design_patterns.pattterns.gof.behavioral.visitor.Visitor visitor) {
        return visitor.visitCompoundGraphic(this);
    }

    public void add(dev.folomkin.pro.design_patterns.pattterns.gof.behavioral.visitor.Shape shape) {
        children.add(shape);
    }
}

//-> ./visitor
//-> ./visitor/Visitor.java:
//-> Интерфейс посетителя

/**
 * Интерфейс посетителей должен содержать методы посещения
 * каждого элемента. Важно, чтобы иерархия элементов менялась
 * редко, так как при добавлении нового элемента придётся менять
 * всех существующих посетителей.
 */
interface Visitor {
    String visitDot(dev.folomkin.pro.design_patterns.pattterns.gof.behavioral.visitor.Dot dot);

    String visitCircle(dev.folomkin.pro.design_patterns.pattterns.gof.behavioral.visitor.Circle circle);

    String visitRectangle(dev.folomkin.pro.design_patterns.pattterns.gof.behavioral.visitor.Rectangle rectangle);

    String visitCompoundGraphic(dev.folomkin.pro.design_patterns.pattterns.gof.behavioral.visitor.CompoundShape cg);
}

//-> ./visitor/XMLExportVisitor.java:
//-> Конкретный посетитель

/**
 * Конкретный посетитель реализует одну операцию для всей
 * иерархии элементов. Новая операция = новый посетитель.
 * Посетитель выгодно применять, когда новые элементы
 * добавляются очень редко, а новые операции — часто.
 */
class XMLExportVisitor implements dev.folomkin.pro.design_patterns.pattterns.gof.behavioral.visitor.Visitor {

    public String export(dev.folomkin.pro.design_patterns.pattterns.gof.behavioral.visitor.Shape... args) {
        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>" + "\n");
        for (dev.folomkin.pro.design_patterns.pattterns.gof.behavioral.visitor.Shape shape : args) {
            sb.append(shape.accept(this)).append("\n");
        }
        return sb.toString();
    }

    // Экспорт id и координат центра точки.
    public String visitDot(dev.folomkin.pro.design_patterns.pattterns.gof.behavioral.visitor.Dot d) {
        return "<dot>" + "\n" +
                "    <id>" + d.getId() + "</id>" + "\n" +
                "    <x>" + d.getX() + "</x>" + "\n" +
                "    <y>" + d.getY() + "</y>" + "\n" +
                "</dot>";
    }

    // Экспорт id, кординат центра и радиуса окружности.
    public String visitCircle(dev.folomkin.pro.design_patterns.pattterns.gof.behavioral.visitor.Circle c) {
        return "<circle>" + "\n" +
                "    <id>" + c.getId() + "</id>" + "\n" +
                "    <x>" + c.getX() + "</x>" + "\n" +
                "    <y>" + c.getY() + "</y>" + "\n" +
                "    <radius>" + c.getRadius() + "</radius>" + "\n" +
                "</circle>";
    }

    // Экспорт id, кординат левого-верхнего угла, ширины и
    // высоты прямоугольника.
    public String visitRectangle(dev.folomkin.pro.design_patterns.pattterns.gof.behavioral.visitor.Rectangle r) {
        return "<rectangle>" + "\n" +
                "    <id>" + r.getId() + "</id>" + "\n" +
                "    <x>" + r.getX() + "</x>" + "\n" +
                "    <y>" + r.getY() + "</y>" + "\n" +
                "    <width>" + r.getWidth() + "</width>" + "\n" +
                "    <height>" + r.getHeight() + "</height>" + "\n" +
                "</rectangle>";
    }

    // Экспорт id составной фигуры, а также списка id
    // подфигур, из которых она состоит.
    public String visitCompoundGraphic(dev.folomkin.pro.design_patterns.pattterns.gof.behavioral.visitor.CompoundShape cg) {
        return "<compound_graphic>" + "\n" +
                "   <id>" + cg.getId() + "</id>" + "\n" +
                _visitCompoundGraphic(cg) +
                "</compound_graphic>";
    }

    private String _visitCompoundGraphic(dev.folomkin.pro.design_patterns.pattterns.gof.behavioral.visitor.CompoundShape cg) {
        StringBuilder sb = new StringBuilder();
        for (dev.folomkin.pro.design_patterns.pattterns.gof.behavioral.visitor.Shape shape : cg.children) {
            String obj = shape.accept(this);
            // Proper indentation for sub-objects.
            obj = "    " + obj.replace("\n", "\n    ") + "\n";
            sb.append(obj);
        }
        return sb.toString();
    }

}

//-> ./Demo.java:
//-> Клиентский код

/**
 * Приложение может применять посетителя к любому набору
 * объектов элементов, даже не уточняя их типы. Нужный метод
 * посетителя будет выбран благодаря проходу через метод accept
 */
public class Demo {
    public static void main(String[] args) {
        dev.folomkin.pro.design_patterns.pattterns.gof.behavioral.visitor.Dot dot = new dev.folomkin.pro.design_patterns.pattterns.gof.behavioral.visitor.Dot(1, 10, 55);
        dev.folomkin.pro.design_patterns.pattterns.gof.behavioral.visitor.Circle circle = new dev.folomkin.pro.design_patterns.pattterns.gof.behavioral.visitor.Circle(2, 23, 15, 10);
        dev.folomkin.pro.design_patterns.pattterns.gof.behavioral.visitor.Rectangle rectangle = new dev.folomkin.pro.design_patterns.pattterns.gof.behavioral.visitor.Rectangle(3, 10, 17, 20, 30);

        dev.folomkin.pro.design_patterns.pattterns.gof.behavioral.visitor.CompoundShape compoundShape = new dev.folomkin.pro.design_patterns.pattterns.gof.behavioral.visitor.CompoundShape(4);
        compoundShape.add(dot);
        compoundShape.add(circle);
        compoundShape.add(rectangle);

        dev.folomkin.pro.design_patterns.pattterns.gof.behavioral.visitor.CompoundShape c = new dev.folomkin.pro.design_patterns.pattterns.gof.behavioral.visitor.CompoundShape(5);
        c.add(dot);
        compoundShape.add(c);

        export(circle, compoundShape);
    }

    private static void export(dev.folomkin.pro.design_patterns.pattterns.gof.behavioral.visitor.Shape... shapes) {
        dev.folomkin.pro.design_patterns.pattterns.gof.behavioral.visitor.XMLExportVisitor exportVisitor = new dev.folomkin.pro.design_patterns.pattterns.gof.behavioral.visitor.XMLExportVisitor();
        System.out.println(exportVisitor.export(shapes));
    }
}

```

## Шаги реализации

1. Создайте интерфейс посетителя и объявите в нём методы
   «посещения» для каждого класса элемента, который суще-
   ствует впрограмме.
2. Опишите интерфейс элементов. Если вы работаете с уже
   существующими классами, то объявите абстрактный метод
   принятия посетителей в базовом классе иерархии
   элементов.
3. Реализуйтеметодыпринятиявовсехконкретныхэлементах.
   Онидолжныпереадресовыватьвызовытомуметодупосети-
   теля,вкоторомтиппараметрасовпадаетстекущимклассом
   элемента.
4. Иерархияэлементовдолжназнатьтолькообазовоминтер-
   фейсе посетителей. С другой стороны, посетители будут
   знать обо всех классахэлементов.
5. Для каждого нового поведения создайте конкретный класс
   посетителя. Приспособьте это поведение для работы со
   всемитипамиэлементов,реализоваввсеметодыинтерфей-
   сапосетителей.
   Выможетестолкнутьсясситуацией,когдапосетителюнужен
   будет доступ к приватным полям элементов. В этом случае
   вы можете либо раскрыть доступ к этим полям, нарушив
   инкапсуляцию элементов, либо сделать класс посетителя
   вложенным в класс элемента, если вам повезло писать на
   языке, который поддерживает вложенностьклассов.
6. Клиентбудетсоздаватьобъектыпосетителей,азатемпере-
   давать их элементам, используя методпринятия.