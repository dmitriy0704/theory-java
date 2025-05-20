# Легковес (Flyweight)

— это структурный паттерн, который экономит память, благодаря разделению общего
состояния, вынесенного в один объект, между множеством объектов. Это структурный
паттерн проектирования, который позволяет вместить бóльшее количество объектов в
отведённую оперативную память. Легковес экономит память, разделяя общее
состояние объектов между собой, вместо хранения одинаковых данных в каждом
объекте.

Легковес позволяет экономить память, кешируя одинаковые данные, используемые в
разных объектах.

**Применимость**: Весь смысл использования Легковеса — в экономии памяти.
Поэтому, если в приложении нет такой проблемы, то вы вряд ли найдёте там примеры
Легковеса.

Примеры Легковеса в стандартных библиотеках Java:  
java.lang.Integer#valueOf(int) (а также Boolean, Byte, Character, Short, Long и
BigDecimal)

**Признаки применения паттерна**: Легковес можно определить по создающим методам
класса, которые возвращают закешированные объекты, вместо создания новых.

Неизменяемые данные объекта принято называть «внутренним состоянием». Все
остальные данные - это «внешнее состояние».

## Структура

![flyweight_structure.png](/img/design_pattern/design_patterns/flyweight_structure.png)

1. Вы всегда должны помнить о том, что Легковес применяется в программе, имеющей
   громадное количество одинаковых объектов. Этих объектов должно быть так
   много, чтобы они не помещались в доступную оперативную память без
   ухищрений. Паттерн разделяет данные этих объектов на две части - легковесы и
   контексты.
2. Легковес содержит состояние, которое повторялось во множестве первоначальных
   объектов. Один и тот же легковес можно использовать в связке со множеством
   контекстов. Состояние, хранящееся здесь, называется внутренним,
   а то, которое он получает извне - внешним.
3. Контекст содержит «внешнюю» часть состояния, уникальную для каждого объекта.
   Контекст связан с одним из объектов - легковесов, хранящих оставшееся
   состояние.
4. Поведение оригинального объекта чаще всего оставляют в
   Легковесе, передавая значения контекста через параметры
   методов. Тем не менее, поведение можно поместить и в контекст, используя
   легковес как объект данных.
5. Клиент вычисляет или хранит контекст, то есть внешнее
   состояние легковесов. Для клиента легковесы выглядят как
   шаблонные объекты, которые можно настроить во время
   использования, передав контекст через параметры.
6. Фабрика легковесов управляет созданием и повторным
   использованием легковесов. Фабрика получает запросы, в
   которых указано желаемое состояние легковеса. Если легковес с таким
   состоянием уже создан, фабрика сразу его возвращает, а если нет - создаёт
   новый объект.

## Пример кода

Отрисовка леса

В этом примере мы создадим и нарисуем лес (1.000.000 деревьев)! Каждому дереву
соответствует свой объект, имеющий некоторое состояние (координаты, текстура и
прочее). Такая программа хоть и работает, но ест слишком много памяти.

Много деревьев имеют одинаковые свойства (название, текстуру, цвет). Потому мы
можем применить паттерн Легковес и закешировать эти свойства в отдельных
объектах TreeType. Теперь вместо хранения этих данных в миллионах объектов
деревьев Tree, мы будем ссылаться на один из нескольких объектов-легковесов.

Клиенту даже необязательно знать обо всём этом. Фабрика легковесов TreeType сама
позаботится о создании нового типа дерева, если будет запрошено дерево с
какими-то уникальными параметрами.

```java
package dev.folomkin.design_patterns.pattterns.structural.flyweght;


import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;
import java.util.ArrayList;
import java.util.List;


// trees/TreeType.java:

/**
 * Легковес, имеющий общее состояние нескольких деревьев.
 *
 * Этот класс-легковес содержит часть полей, которые описывают
 * деревья. Эти поля не уникальны для каждого дерева, в отличие, 
 * например, от координат: несколько деревьев могут иметь ту же 
 * текстуру.
 *
 * Поэтому мы переносим повторяющиеся данные в один-единственный 
 * объект и ссылаемся на него из множества отдельных деревьев.
 */
class TreeType {
    private String name;
    private Color color;
    private String otherTreeData;

    public TreeType(String name, Color color, String otherTreeData) {
        this.name = name;
        this.color = color;
        this.otherTreeData = otherTreeData;
    }

    /**
     * 1. Создать картинку данного типа, цвета и текстуры.
     * 2. Нарисовать картинку на холсте в позиции X, Y.
     */
    public void draw(Graphics g, int x, int y) {
        g.setColor(Color.BLACK);
        g.fillRect(x - 1, y, 3, 5);
        g.setColor(color);
        g.fillOval(x - 5, y - 10, 10, 10);
    }
}


// trees/TreeFactory.java:

/**
 * Фабрика деревьев. 
 * Фабрика легковесов решает, когда нужно создать новый
 * легковес, а когда можно обойтись существующим.
 */
class TreeFactory {
    static Map<String, flyweght.structural.gof.dev.folomkin.pro.design_patterns.TreeType> treeTypes = new HashMap<>();

    public static flyweght.structural.gof.dev.folomkin.pro.design_patterns.TreeType getTreeType(String name, Color color, String otherTreeData) {
        flyweght.structural.gof.dev.folomkin.pro.design_patterns.TreeType result = treeTypes.get(name);
        if (result == null) {
            result = new flyweght.structural.gof.dev.folomkin.pro.design_patterns.TreeType(name, color, otherTreeData);
            treeTypes.put(name, result);
        }
        return result;
    }
}


// trees
// trees/Tree.java:

/**
 * Объект, содержащий уникальное состояние дерева.
 *
 * Контекстный объект, из которого мы выделили легковес TreeType. 
 * В программе могут быть тысячи объектов Tree, так как накладные расходы 
 * на их хранение совсем небольшие — в памяти нужно держать всего три целых числа 
 * (две координаты и ссылка).
 */
class Tree {
    private int x;
    private int y;
    private flyweght.structural.gof.dev.folomkin.pro.design_patterns.TreeType type;

    public Tree(int x, int y, flyweght.structural.gof.dev.folomkin.pro.design_patterns.TreeType type) {
        this.x = x;
        this.y = y;
        this.type = type;
    }

    public void draw(Graphics g) {
        type.draw(g, x, y);
    }
}


// forest
// forest/Forest.java:

/**
 * GUI-лес, который рисует деревья.
 *
 * Классы Tree и Forest являются клиентами Легковеса. 
 * При желании их можно слить в один класс, если вам не 
 * нужно расширять класс деревьев далее.
 */
class Forest extends JFrame {
    private List<flyweght.structural.gof.dev.folomkin.pro.design_patterns.Tree> trees = new ArrayList<>();

    public void plantTree(int x, int y, String name, Color color, String otherTreeData) {
        flyweght.structural.gof.dev.folomkin.pro.design_patterns.TreeType type = flyweght.structural.gof.dev.folomkin.pro.design_patterns.TreeFactory.getTreeType(name, color, otherTreeData);
        flyweght.structural.gof.dev.folomkin.pro.design_patterns.Tree tree = new flyweght.structural.gof.dev.folomkin.pro.design_patterns.Tree(x, y, type);
        trees.add(tree);
    }

    @Override
    public void paint(Graphics graphics) {
        for (flyweght.structural.gof.dev.folomkin.pro.design_patterns.Tree tree : trees) {
            tree.draw(graphics);
        }
    }
}

public class Code {
    static int CANVAS_SIZE = 500;
    static int TREES_TO_DRAW = 1000000;
    static int TREE_TYPES = 2;

    public static void main(String[] args) {
        flyweght.structural.gof.dev.folomkin.pro.design_patterns.Forest forest = new flyweght.structural.gof.dev.folomkin.pro.design_patterns.Forest();
        for (int i = 0; i < Math.floor(TREES_TO_DRAW / TREE_TYPES); i++) {
            forest.plantTree(random(0, CANVAS_SIZE), random(0, CANVAS_SIZE),
                    "Summer Oak", Color.GREEN, "Oak texture stub");
            forest.plantTree(random(0, CANVAS_SIZE), random(0, CANVAS_SIZE),
                    "Autumn Oak", Color.ORANGE, "Autumn Oak texture stub");
        }
        forest.setSize(CANVAS_SIZE, CANVAS_SIZE);
        forest.setVisible(true);

        System.out.println(TREES_TO_DRAW + " trees drawn");
        System.out.println("---------------------");
        System.out.println("Memory usage:");
        System.out.println("Tree size (8 bytes) * " + TREES_TO_DRAW);
        System.out.println("+ TreeTypes size (~30 bytes) * " + TREE_TYPES + "");
        System.out.println("---------------------");
        System.out.println("Total: " + ((TREES_TO_DRAW * 8 + TREE_TYPES * 30) / 1024 / 1024) +
                "MB (instead of " + ((TREES_TO_DRAW * 38) / 1024 / 1024) + "MB)");
    }

    private static int random(int min, int max) {
        return min + (int) (Math.random() * ((max - min) + 1));
    }
}

```

![flyweight_example.png](/img/design_pattern/design_patterns/flyweight_example.png)

Легковес выделяет повторяющуюся часть состояния из
основного класса Tree и помещает его в дополнительный
класс TreeType.

Теперь, вместо хранения повторяющихся данных во всех
объектах, отдельные деревья будут ссылаться на несколько общих объектов,
хранящих эти данные. Клиент работает
с деревьями через фабрику деревьев, которая скрывает от
него сложность кеширования общих данных деревьев.

Таким образом, программа будет использовать намного
меньше оперативной памяти, что позволит отрисовать больше деревьев на экране на
том же железе.

## Шаги реализации

1. Разделите поля класса, который станет легковесом, на две части:
    - внутреннее состояние: значения этих полей одинаковы для большого числа
      объектов;
    - внешнее состояние(контекст): значения полей уникальны для каждого объекта.
2. Оставьте поля внутреннего состояния в классе, но убедитесь,
   что их значения неизменяемы. Эти поля должны инициализироваться только через
   конструктор.
3. Превратите поля внешнего состояния в параметры методов,
   где эти поля использовались. Затем удалите поля из класса.
4. Создайте фабрику, которая будет кешировать и повторно
   отдавать уже созданные объекты. Клиент должен запрашивать из этой фабрики
   легковеса с определённым внутренним состоянием, а не создавать егонапрямую.
5. Клиент должен хранить или вычислять значения внешнего состояния (контекст) и
   передавать его в методы объекта легковеса.