# Компоновщик (Composite)

- это структурный паттерн проектирования, который позволяет сгруппировать
  множество объектов в древовидную структуру, а затем работать с ней так, как
  будто это единичный объект.

Компоновщик давно стал синонимом всех задач, связанных с построением дерева
объектов. Все операции компоновщика основаны на рекурсии и «суммировании»
результатов на ветвях дерева.

**Применимость**: Паттерн Компоновщик встречается в любых задачах, которые
связаны с построением дерева. Самый простой пример — составные элементы GUI,
которые тоже можно рассматривать как дерево.

Примеры Компоновщиков в стандартных библиотеках Java:

java.awt.Container#add(Component) (обычно применим для компонентов Swing)

javax.faces.component.UIComponent#getChildren() (обычно применим для JSF UI)

**Признаки применения паттерна**: Если из объектов строится древовидная
структура, и со всеми объектами дерева, как и с самим деревом работают через
общий интерфейс.

## Структура

![composite_structure.png](/img/design_pattern/design_patterns/composite_structure.png)

1. Компонент определяет общий интерфейс для простых и составных компонентов
   дерева.
2. Лист — это простой компонент дерева, не имеющий ответвлений. Из-за того, что
   им некому больше передавать выполнение, классы листьев будут содержать
   большую часть полезного кода.
3. Контейнер (или композит) — это составной компонент дерева. Он содержит набор
   дочерних компонентов, но ничего не знает об их типах. Это могут быть как
   простые компоненты-листья, так и другие компоненты-контейнеры. Но это не
   является проблемой, если все дочерние компоненты следуют единому интерфейсу.
   Методы контейнера переадресуют основную работу своим дочерним компонентам,
   хотя и могут добавлять что-то своё к результату.
4. Клиент работает с деревом через общий интерфейс компонентов.
   Благодаря этому, клиенту не важно, что перед ним находится — простой или
   составной компонент дерева.

## Пример кода

**Простые и составные графические фигуры**

В этом примере Компоновщик помогает реализовать вложенные геометрические фигуры.

Этот пример показывает как можно работать со сложными геометрическими фигурами,
составленными из простых так, как будто они сами являются простыми.

![composite_example.png](/img/design_pattern/design_patterns/composite_example.png)

Класс CompoundGraphic может содержать любое количество подфигур, включая такие
же контейнеры, как он сам. Контейнер реализует те же методы, что и простые
фигуры. Но, вместо непосредственного действия, он передаёт вызовы всем вложенным
компонентам, используя рекурсию. Затем он как бы «суммирует» результаты всех
вложенных фигур.

Клиентский код работает со всеми фигурами через общий интерфейс фигур и не
знает, что перед ним — простая фигура или составная. Это позволяет клиентскому
коду работать с деревьями объектов любой сложности, не привязываясь к конкретным
классам объектов, формирующих дерево.

```java
package dev.folomkin.design_patterns.pattterns.structural.composite;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// ->  shapes
// ->  shapes/Shape.java: Общий интерфейс фигур

/**
 * Общий интерфейс компонентов. 
 */
interface Shape {
   int getX();

   int getY();

   int getWidth();

   int getHeight();

   void move(int x, int y);

   boolean isInsideBounds(int x, int y);

   void select();

   void unSelect();

   boolean isSelected();

   void paint(Graphics graphics);
}

// ->  shapes/BaseShape.java: Абстрактная фигура с базовым функционалом
abstract class BaseShape implements dev.folomkin.design_patterns.pattterns.gof.structural.composite.Shape {
   public int x;
   public int y;
   public Color color;
   private boolean selected = false;

   BaseShape(int x, int y, Color color) {
      this.x = x;
      this.y = y;
      this.color = color;
   }

   @Override
   public int getX() {
      return x;
   }

   @Override
   public int getY() {
      return y;
   }

   @Override
   public int getWidth() {
      return 0;
   }

   @Override
   public int getHeight() {
      return 0;
   }

   @Override
   public void move(int x, int y) {
      this.x += x;
      this.y += y;
   }

   @Override
   public boolean isInsideBounds(int x, int y) {
      return x > getX() && x < (getX() + getWidth()) &&
              y > getY() && y < (getY() + getHeight());
   }

   @Override
   public void select() {
      selected = true;
   }

   @Override
   public void unSelect() {
      selected = false;
   }

   @Override
   public boolean isSelected() {
      return selected;
   }

   void enableSelectionStyle(Graphics graphics) {
      graphics.setColor(Color.LIGHT_GRAY);

      Graphics2D g2 = (Graphics2D) graphics;
      float[] dash1 = {2.0f};
      g2.setStroke(new BasicStroke(1.0f,
              BasicStroke.CAP_BUTT,
              BasicStroke.JOIN_MITER,
              2.0f, dash1, 0.0f));
   }

   void disableSelectionStyle(Graphics graphics) {
      graphics.setColor(color);
      Graphics2D g2 = (Graphics2D) graphics;
      g2.setStroke(new BasicStroke());
   }


   @Override
   public void paint(Graphics graphics) {
      if (isSelected()) {
         enableSelectionStyle(graphics);
      } else {
         disableSelectionStyle(graphics);
      }

      // ...
   }
}

// ->  shapes/Dot.java: Точка

/**
 * Простой компонент.
 */

class Dot extends dev.folomkin.design_patterns.pattterns.gof.structural.composite.BaseShape {
   private final int DOT_SIZE = 3;

   public Dot(int x, int y, Color color) {
      super(x, y, color);
   }

   @Override
   public int getWidth() {
      return DOT_SIZE;
   }

   @Override
   public int getHeight() {
      return DOT_SIZE;
   }

   @Override
   public void paint(Graphics graphics) {
      super.paint(graphics);
      graphics.fillRect(x - 1, y - 1, getWidth(), getHeight());
   }
}

// ->  shapes/Circle.java: Круг

/**
 * Компоненты могут расширять другие компоненты.
 * */

class Circle extends dev.folomkin.design_patterns.pattterns.gof.structural.composite.BaseShape {
   public int radius;

   public Circle(int x, int y, int radius, Color color) {
      super(x, y, color);
      this.radius = radius;
   }

   @Override
   public int getWidth() {
      return radius * 2;
   }

   @Override
   public int getHeight() {
      return radius * 2;
   }

   public void paint(Graphics graphics) {
      super.paint(graphics);
      graphics.drawOval(x, y, getWidth() - 1, getHeight() - 1);
   }
}

// -> shapes/Rectangle.java: Четырёхугольник
class Rectangle extends dev.folomkin.design_patterns.pattterns.gof.structural.composite.BaseShape {
   public int width;
   public int height;

   public Rectangle(int x, int y, int width, int height, Color color) {
      super(x, y, color);
      this.width = width;
      this.height = height;
   }

   @Override
   public int getWidth() {
      return width;
   }

   @Override
   public int getHeight() {
      return height;
   }

   @Override
   public void paint(Graphics graphics) {
      super.paint(graphics);
      graphics.drawRect(x, y, getWidth() - 1, getHeight() - 1);
   }
}

// ->  shapes/CompoundShape.java: Составная фигура

/**
 * Контейнер содержит операции добавления/удаления дочерних 
 * компонентов. Все стандартные операции интерфейса компонентов 
 * он делегирует каждому из дочерних компонентов.
 */
class CompoundShape extends dev.folomkin.design_patterns.pattterns.gof.structural.composite.BaseShape {
   protected List<dev.folomkin.design_patterns.pattterns.gof.structural.composite.Shape> children = new ArrayList<>();

   public CompoundShape(dev.folomkin.design_patterns.pattterns.gof.structural.composite.Shape... components) {
      super(0, 0, Color.BLACK);
      add(components);
   }

   public void add(dev.folomkin.design_patterns.pattterns.gof.structural.composite.Shape component) {
      children.add(component);
   }

   public void add(dev.folomkin.design_patterns.pattterns.gof.structural.composite.Shape... components) {
      children.addAll(Arrays.asList(components));
   }

   public void remove(dev.folomkin.design_patterns.pattterns.gof.structural.composite.Shape child) {
      children.remove(child);
   }

   public void remove(dev.folomkin.design_patterns.pattterns.gof.structural.composite.Shape... components) {
      children.removeAll(Arrays.asList(components));
   }

   public void clear() {
      children.clear();
   }

   @Override
   public int getX() {
      if (children.size() == 0) {
         return 0;
      }
      int x = children.get(0).getX();
      for (dev.folomkin.design_patterns.pattterns.gof.structural.composite.Shape child : children) {
         if (child.getX() < x) {
            x = child.getX();
         }
      }
      return x;
   }

   @Override
   public int getY() {
      if (children.size() == 0) {
         return 0;
      }
      int y = children.get(0).getY();
      for (dev.folomkin.design_patterns.pattterns.gof.structural.composite.Shape child : children) {
         if (child.getY() < y) {
            y = child.getY();
         }
      }
      return y;
   }

   @Override
   public int getWidth() {
      int maxWidth = 0;
      int x = getX();
      for (dev.folomkin.design_patterns.pattterns.gof.structural.composite.Shape child : children) {
         int childsRelativeX = child.getX() - x;
         int childWidth = childsRelativeX + child.getWidth();
         if (childWidth > maxWidth) {
            maxWidth = childWidth;
         }
      }
      return maxWidth;
   }

   @Override
   public int getHeight() {
      int maxHeight = 0;
      int y = getY();
      for (dev.folomkin.design_patterns.pattterns.gof.structural.composite.Shape child : children) {
         int childsRelativeY = child.getY() - y;
         int childHeight = childsRelativeY + child.getHeight();
         if (childHeight > maxHeight) {
            maxHeight = childHeight;
         }
      }
      return maxHeight;
   }

   @Override
   public void move(int x, int y) {
      for (dev.folomkin.design_patterns.pattterns.gof.structural.composite.Shape child : children) {
         child.move(x, y);
      }
   }

   @Override
   public boolean isInsideBounds(int x, int y) {
      for (dev.folomkin.design_patterns.pattterns.gof.structural.composite.Shape child : children) {
         if (child.isInsideBounds(x, y)) {
            return true;
         }
      }
      return false;
   }

   @Override
   public void unSelect() {
      super.unSelect();
      for (dev.folomkin.design_patterns.pattterns.gof.structural.composite.Shape child : children) {
         child.unSelect();
      }
   }

   public boolean selectChildAt(int x, int y) {
      for (dev.folomkin.design_patterns.pattterns.gof.structural.composite.Shape child : children) {
         if (child.isInsideBounds(x, y)) {
            child.select();
            return true;
         }
      }
      return false;
   }


   /**
    *  1. Для каждого дочернего компонента:
    *  - Отрисовать компонент.
    *  - Определить координаты максимальной границы.
    *  2. Нарисовать пунктирную границу вокруг всей области.
    */

   @Override
   public void paint(Graphics graphics) {
      if (isSelected()) {
         enableSelectionStyle(graphics);
         graphics.drawRect(getX() - 1, getY() - 1, getWidth() + 1, getHeight() + 1);
         disableSelectionStyle(graphics);
      }

      for (dev.folomkin.design_patterns.pattterns.gof.structural.composite.Shape child : children) {
         child.paint(graphics);
      }
   }
}


// -> editor
// ->  editor/ImageEditor.java: Редактор фигур


/**
 * Приложение работает единообразно как с единичными
 * компонентами, так и с целыми группами компонентов.
 *
 */

class ImageEditor {
   private EditorCanvas canvas;
   private dev.folomkin.design_patterns.pattterns.gof.structural.composite.CompoundShape allShapes = new dev.folomkin.design_patterns.pattterns.gof.structural.composite.CompoundShape();

   public ImageEditor() {
      canvas = new EditorCanvas();
   }

   public void loadShapes(dev.folomkin.design_patterns.pattterns.gof.structural.composite.Shape... shapes) {
      allShapes.clear();
      allShapes.add(shapes);
      canvas.refresh();
   }

   private class EditorCanvas extends Canvas {
      JFrame frame;

      private static final int PADDING = 10;

      EditorCanvas() {
         createFrame();
         refresh();
         addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
               allShapes.unSelect();
               allShapes.selectChildAt(e.getX(), e.getY());
               e.getComponent().repaint();
            }
         });
      }

      void createFrame() {
         frame = new JFrame();
         frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
         frame.setLocationRelativeTo(null);

         JPanel contentPanel = new JPanel();
         Border padding = BorderFactory.createEmptyBorder(PADDING, PADDING, PADDING, PADDING);
         contentPanel.setBorder(padding);
         frame.setContentPane(contentPanel);

         frame.add(this);
         frame.setVisible(true);
         frame.getContentPane().setBackground(Color.LIGHT_GRAY);
      }

      public int getWidth() {
         return allShapes.getX() + allShapes.getWidth() + PADDING;
      }

      public int getHeight() {
         return allShapes.getY() + allShapes.getHeight() + PADDING;
      }

      void refresh() {
         this.setSize(getWidth(), getHeight());
         frame.pack();
      }

      public void paint(Graphics graphics) {
         allShapes.paint(graphics);
      }
   }
}


public class CompositeExample {
   public static void main(String[] args) {
      dev.folomkin.design_patterns.pattterns.gof.structural.composite.ImageEditor editor = new dev.folomkin.design_patterns.pattterns.gof.structural.composite.ImageEditor();

      editor.loadShapes(
              new dev.folomkin.design_patterns.pattterns.gof.structural.composite.Circle(10, 10, 10, Color.BLUE),

              new dev.folomkin.design_patterns.pattterns.gof.structural.composite.CompoundShape(
                      new dev.folomkin.design_patterns.pattterns.gof.structural.composite.Circle(110, 110, 50, Color.RED),
                      new dev.folomkin.design_patterns.pattterns.gof.structural.composite.Dot(160, 160, Color.RED)
              ),

              new dev.folomkin.design_patterns.pattterns.gof.structural.composite.CompoundShape(
                      new dev.folomkin.design_patterns.pattterns.gof.structural.composite.Rectangle(250, 250, 100, 100, Color.GREEN),
                      new dev.folomkin.design_patterns.pattterns.gof.structural.composite.Dot(240, 240, Color.GREEN),
                      new dev.folomkin.design_patterns.pattterns.gof.structural.composite.Dot(240, 360, Color.GREEN),
                      new dev.folomkin.design_patterns.pattterns.gof.structural.composite.Dot(360, 360, Color.GREEN),
                      new dev.folomkin.design_patterns.pattterns.gof.structural.composite.Dot(360, 240, Color.GREEN)
              )
      );
   }
}

```

## Шаги реализации

1. Убедитесь, что вашу бизнес-логику можно представить как
   древовидную структуру. Попытайтесь разбить её на простые
   компоненты и контейнеры. Помните, что контейнеры могут
   содержать как простые компоненты, так и другие вложенные контейнеры.
2. Создайте общий интерфейс компонентов, который объединит операции контейнеров
   и простых компонентов дерева.
   Интерфейс будет удачным, если вы сможете использовать
   его, чтобы взаимозаменять простые и составные компоненты без потери смысла.
3. Создайте класс компонентов-листьев, не имеющих дальнейших ответвлений. Имейте
   в виду, что программа может содержать несколько таких классов.
4. Создайте класс компонентов-контейнеров и добавьте в него
   массив для хранения ссылок на вложенные компоненты.
   Этот массив должен быть способен содержать как простые,
   так и составные компоненты, поэтому убедитесь, что он объявлен с типом
   интерфейса компонентов.

   Реализуйте в контейнере методы интерфейса компонентов, помня о том, что
   контейнеры должны делегировать основ- ную работу своим дочерним компонентам.

5. Добавьте операции добавления и удаления дочерних компонентов в класс
   контейнеров.

   Имейте в виду, что методы добавления/удаления дочерних компонентов можно
   поместить и в интерфейс компонентов. Да, это нарушит принцип разделения
   интерфейса, так как реализации методов будут пустыми в компонентах-листьях.
   Но зато все компоненты дерева станут действительно одинаковыми для клиента.