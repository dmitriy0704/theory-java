# Снимок (Memento)

- это поведенческий паттерн проектирования,
  который позволяет сохранять и восстанавливать прошлые
  состояния объектов, не раскрывая подробностей
  ихреализации.

При этом Снимок не раскрывает подробностей реализации объектов, и клиент не
имеет доступа к защищённой информации объекта.

Применимость: Снимок на Java чаще всего реализуют с помощью сериализации. Но это
не единственный, да и не самый эффективный метод сохранения состояния объектов
во время выполнения программы.

Примеры Снимка в стандартных библиотеках Java:

Все реализации java.io.Serializable могут быть использованы как аналог Снимка.
Все реализации javax.faces.component.StateHolder

## Структура

**Классическая реализация на вложенных классах**

Классическая реализация паттерна полагается на механизм
вложенных классов.

![memento_structure_1.png](/img/design_pattern/design_patterns/memento_structure_1.png)

1. Создатель может производить снимки своего состояния, а
   также воспроизводить прошлое состояние, если подать в
   него готовыйснимок.
2. Снимок — это простой объект данных, содержащий состо-
   яние создателя. Надёжнее всего сделать объекты снимков
   неизменяемыми, передавая в них состояние только через
   конструктор.
3. Опекун должензнать,когдаделатьснимоксоздателяикогда
   его нужно восстанавливать.
   Опекун может хранить историю прошлых состояний созда-
   теля в виде стека из снимков. Когда понадобится отменить
   выполненную операцию, он возьмёт «верхний» снимок из
   стека и передаст его создателю для восстановления.
4. В данной реализации снимок — это внутренний класс по
   отношению к классу создателя. Именно поэтому он имеет
   полный доступ к полям и методам создателя, даже приват-
   ным.Сдругойстороны,опекуннеимеетдоступаниксосто-
   янию, ни к методам снимков и может всего лишь хранить
   ссылки на этиобъекты.

## Пример кода

Отмена в редакторе фигур

В данном графическом редакторе мы можем менять цвет и позицию фигур.
Пользователь может отменять или повторять действия, используя горячие клавиши.

Механизм отмены построен при помощи паттерна Снимок и Команды. Перед совершением
команды, редактор сохраняет в истории снимок своего состояния. Если нужно
отменить команду, редактор берет последнюю запись из истории и восстанавливает
состояние из снимка. Для последующей отмены берется команда дальше из истории.

Отмененные команды хранятся в истории пока пользователь не совершит какое-то
действие. Это нужно, чтобы иметь возможность повторять отменённые команды.

![memento_example.png](/img/design_pattern/design_patterns/memento_example.png)

Объекты команд выступают в роли опекунов и запраши-
вают снимки у редактора перед тем, как выполнить своё
действие. Если потребуется отменить операцию, команда
сможетвосстановитьсостояниередактора,используясохра-
нённыйснимок.

Приэтомснимокнеимеетпубличныхполей,поэтомудругие
объектынеимеютдоступакеговнутреннимданным.Сним-
кисвязанысопределённымредактором,которыйихсоздал.
Онижеивосстанавливаютсостояниесвоегоредактора.Это
позволяет программе иметь одновременно несколько объ-
ектовредакторов,например,разбитыхпоразнымвкладкам
программы.

```java
//->  editor

//-> editor/Editor.java: Редактор

// Класс создателя должен иметь специальный метод, который
// сохраняет состояние создателя в новом объекте-снимке.
public class Editor extends JComponent {
    private Canvas canvas;
    private CompoundShape allShapes = new CompoundShape();
    private History history;

    public Editor() {
        canvas = new Canvas(this);
        history = new History();
    }

    public void loadShapes(Shape... shapes) {
        allShapes.clear();
        allShapes.add(shapes);
        canvas.refresh();
    }

    public CompoundShape getShapes() {
        return allShapes;
    }

    public void execute(Command c) {
        history.push(c, new Memento(this));
        c.execute();
    }

    public void undo() {
        if (history.undo())
            canvas.repaint();
    }

    public void redo() {
        if (history.redo())
            canvas.repaint();
    }

    public String backup() {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(this.allShapes);
            oos.close();
            return Base64.getEncoder().encodeToString(baos.toByteArray());
        } catch (IOException e) {
            return "";
        }
    }

    public void restore(String state) {
        try {
            byte[] data = Base64.getDecoder().decode(state);
            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
            this.allShapes = (CompoundShape) ois.readObject();
            ois.close();
        } catch (ClassNotFoundException e) {
            System.out.print("ClassNotFoundException occurred.");
        } catch (IOException e) {
            System.out.print("IOException occurred.");
        }
    }
}

//->  editor/Canvas.java: Холст

class Canvas extends java.awt.Canvas {
    private Editor editor;
    private JFrame frame;
    private static final int PADDING = 10;

    Canvas(Editor editor) {
        this.editor = editor;
        createFrame();
        attachKeyboardListeners();
        attachMouseListeners();
        refresh();
    }

    private void createFrame() {
        frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel contentPanel = new JPanel();
        Border padding = BorderFactory.createEmptyBorder(PADDING, PADDING, PADDING, PADDING);
        contentPanel.setBorder(padding);
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        frame.setContentPane(contentPanel);

        contentPanel.add(new JLabel("Select and drag to move."), BorderLayout.PAGE_END);
        contentPanel.add(new JLabel("Right click to change color."), BorderLayout.PAGE_END);
        contentPanel.add(new JLabel("Undo: Ctrl+Z, Redo: Ctrl+R"), BorderLayout.PAGE_END);
        contentPanel.add(this);
        frame.setVisible(true);
        contentPanel.setBackground(Color.LIGHT_GRAY);
    }

    private void attachKeyboardListeners() {
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0) {
                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_Z:
                            editor.undo();
                            break;
                        case KeyEvent.VK_R:
                            editor.redo();
                            break;
                    }
                }
            }
        });
    }

    private void attachMouseListeners() {
        MouseAdapter colorizer = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() != MouseEvent.BUTTON3) {
                    return;
                }
                Shape target = editor.getShapes().getChildAt(e.getX(), e.getY());
                if (target != null) {
                    editor.execute(new ColorCommand(editor, new Color((int) (Math.random() * 0x1000000))));
                    repaint();
                }
            }
        };
        addMouseListener(colorizer);

        MouseAdapter selector = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() != MouseEvent.BUTTON1) {
                    return;
                }

                Shape target = editor.getShapes().getChildAt(e.getX(), e.getY());
                boolean ctrl = (e.getModifiers() & ActionEvent.CTRL_MASK) == ActionEvent.CTRL_MASK;

                if (target == null) {
                    if (!ctrl) {
                        editor.getShapes().unSelect();
                    }
                } else {
                    if (ctrl) {
                        if (target.isSelected()) {
                            target.unSelect();
                        } else {
                            target.select();
                        }
                    } else {
                        if (!target.isSelected()) {
                            editor.getShapes().unSelect();
                        }
                        target.select();
                    }
                }
                repaint();
            }
        };
        addMouseListener(selector);


        MouseAdapter dragger = new MouseAdapter() {
            MoveCommand moveCommand;

            @Override
            public void mouseDragged(MouseEvent e) {
                if ((e.getModifiersEx() & MouseEvent.BUTTON1_DOWN_MASK) != MouseEvent.BUTTON1_DOWN_MASK) {
                    return;
                }
                if (moveCommand == null) {
                    moveCommand = new MoveCommand(editor);
                    moveCommand.start(e.getX(), e.getY());
                }
                moveCommand.move(e.getX(), e.getY());
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() != MouseEvent.BUTTON1 || moveCommand == null) {
                    return;
                }
                moveCommand.stop(e.getX(), e.getY());
                editor.execute(moveCommand);
                this.moveCommand = null;
                repaint();
            }
        };
        addMouseListener(dragger);
        addMouseMotionListener(dragger);
    }

    public int getWidth() {
        return editor.getShapes().getX() + editor.getShapes().getWidth() + PADDING;
    }

    public int getHeight() {
        return editor.getShapes().getY() + editor.getShapes().getHeight() + PADDING;
    }

    void refresh() {
        this.setSize(getWidth(), getHeight());
        frame.pack();
    }

    public void update(Graphics g) {
        paint(g);
    }

    public void paint(Graphics graphics) {
        BufferedImage buffer = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D ig2 = buffer.createGraphics();
        ig2.setBackground(Color.WHITE);
        ig2.clearRect(0, 0, this.getWidth(), this.getHeight());

        editor.getShapes().paint(buffer.getGraphics());

        graphics.drawImage(buffer, 0, 0, null);
    }
}


//-> history

//->  history/History.java: История использует снимки и команды
public class History {
    private List<Pair> history = new ArrayList<Pair>();
    private int virtualSize = 0;

    private class Pair {
        Command command;
        Memento memento;

        Pair(Command c, Memento m) {
            command = c;
            memento = m;
        }

        private Command getCommand() {
            return command;
        }

        private Memento getMemento() {
            return memento;
        }
    }

    public void push(Command c, Memento m) {
        if (virtualSize != history.size() && virtualSize > 0) {
            history = history.subList(0, virtualSize - 1);
        }
        history.add(new Pair(c, m));
        virtualSize = history.size();
    }

    public boolean undo() {
        Pair pair = getUndo();
        if (pair == null) {
            return false;
        }
        System.out.println("Undoing: " + pair.getCommand().getName());
        pair.getMemento().restore();
        return true;
    }

    public boolean redo() {
        Pair pair = getRedo();
        if (pair == null) {
            return false;
        }
        System.out.println("Redoing: " + pair.getCommand().getName());
        pair.getMemento().restore();
        pair.getCommand().execute();
        return true;
    }

    private Pair getUndo() {
        if (virtualSize == 0) {
            return null;
        }
        virtualSize = Math.max(0, virtualSize - 1);
        return history.get(virtualSize);
    }

    private Pair getRedo() {
        if (virtualSize == history.size()) {
            return null;
        }
        virtualSize = Math.min(history.size(), virtualSize + 1);
        return history.get(virtualSize - 1);
    }
}

//->  history/Memento.java: Снимок

public class Memento {
    private String backup;
    private Editor editor;

    public Memento(Editor editor) {
        this.editor = editor;
        this.backup = editor.backup();
    }

    public void restore() {
        editor.restore(backup);
    }
}


//->  commands

//-> commands/Command.java: Интерфейс команд

public interface Command {
    String getName();

    void execute();
}


//-> commands/ColorCommand.java: Команда смены цвета
public class ColorCommand implements Command {
    private Editor editor;
    private Color color;

    public ColorCommand(Editor editor, Color color) {
        this.editor = editor;
        this.color = color;
    }

    @Override
    public String getName() {
        return "Colorize: " + color.toString();
    }

    @Override
    public void execute() {
        for (Shape child : editor.getShapes().getSelected()) {
            child.setColor(color);
        }
    }
}


//->  commands/MoveCommand.java: Команда перемещения


public class MoveCommand implements Command {
    private Editor editor;
    private int startX, startY;
    private int endX, endY;

    public MoveCommand(Editor editor) {
        this.editor = editor;
    }

    @Override
    public String getName() {
        return "Move by X:" + (endX - startX) + " Y:" + (endY - startY);
    }

    public void start(int x, int y) {
        startX = x;
        startY = y;
        for (Shape child : editor.getShapes().getSelected()) {
            child.drag();
        }
    }

    public void move(int x, int y) {
        for (Shape child : editor.getShapes().getSelected()) {
            child.moveTo(x - startX, y - startY);
        }
    }

    public void stop(int x, int y) {
        endX = x;
        endY = y;
        for (Shape child : editor.getShapes().getSelected()) {
            child.drop();
        }
    }

    @Override
    public void execute() {
        for (Shape child : editor.getShapes().getSelected()) {
            child.moveBy(endX - startX, endY - startY);
        }
    }
}


//->  shapes: Разнообразные фигуры
//-> shapes/Shape.java

public interface Shape extends Serializable {
    int getX();

    int getY();

    int getWidth();

    int getHeight();

    void drag();

    void drop();

    void moveTo(int x, int y);

    void moveBy(int x, int y);

    boolean isInsideBounds(int x, int y);

    Color getColor();

    void setColor(Color color);

    void select();

    void unSelect();

    boolean isSelected();

    void paint(Graphics graphics);
}

//->  shapes/BaseShape.java
public abstract class BaseShape implements Shape {
    int x, y;
    private int dx = 0, dy = 0;
    private Color color;
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
    public void drag() {
        dx = x;
        dy = y;
    }

    @Override
    public void moveTo(int x, int y) {
        this.x = dx + x;
        this.y = dy + y;
    }

    @Override
    public void moveBy(int x, int y) {
        this.x += x;
        this.y += y;
    }

    @Override
    public void drop() {
        this.x = dx;
        this.y = dy;
    }

    @Override
    public boolean isInsideBounds(int x, int y) {
        return x > getX() && x < (getX() + getWidth()) &&
                y > getY() && y < (getY() + getHeight());
    }

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public void setColor(Color color) {
        this.color = color;
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


//-> shapes/Circle.java

public class Circle extends BaseShape {
    private int radius;

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

    @Override
    public void paint(Graphics graphics) {
        super.paint(graphics);
        graphics.drawOval(x, y, getWidth() - 1, getHeight() - 1);
    }
}

//->  shapes/Dot.java


public class Dot extends BaseShape {
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

//->  shapes/Rectangle.java

public class Rectangle extends BaseShape {
    private int width;
    private int height;

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

//->  shapes/CompoundShape.java

public class CompoundShape extends BaseShape {
    private List<Shape> children = new ArrayList<>();

    public CompoundShape(Shape... components) {
        super(0, 0, Color.BLACK);
        add(components);
    }

    public void add(Shape component) {
        children.add(component);
    }

    public void add(Shape... components) {
        children.addAll(Arrays.asList(components));
    }

    public void remove(Shape child) {
        children.remove(child);
    }

    public void remove(Shape... components) {
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
        for (Shape child : children) {
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
        for (Shape child : children) {
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
        for (Shape child : children) {
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
        for (Shape child : children) {
            int childsRelativeY = child.getY() - y;
            int childHeight = childsRelativeY + child.getHeight();
            if (childHeight > maxHeight) {
                maxHeight = childHeight;
            }
        }
        return maxHeight;
    }

    @Override
    public void drag() {
        for (Shape child : children) {
            child.drag();
        }
    }

    @Override
    public void drop() {
        for (Shape child : children) {
            child.drop();
        }
    }

    @Override
    public void moveTo(int x, int y) {
        for (Shape child : children) {
            child.moveTo(x, y);
        }
    }

    @Override
    public void moveBy(int x, int y) {
        for (Shape child : children) {
            child.moveBy(x, y);
        }
    }

    @Override
    public boolean isInsideBounds(int x, int y) {
        for (Shape child : children) {
            if (child.isInsideBounds(x, y)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void setColor(Color color) {
        super.setColor(color);
        for (Shape child : children) {
            child.setColor(color);
        }
    }

    @Override
    public void unSelect() {
        super.unSelect();
        for (Shape child : children) {
            child.unSelect();
        }
    }

    public Shape getChildAt(int x, int y) {
        for (Shape child : children) {
            if (child.isInsideBounds(x, y)) {
                return child;
            }
        }
        return null;
    }

    public boolean selectChildAt(int x, int y) {
        Shape child = getChildAt(x, y);
        if (child != null) {
            child.select();
            return true;
        }
        return false;
    }

    public List<Shape> getSelected() {
        List<Shape> selected = new ArrayList<>();
        for (Shape child : children) {
            if (child.isSelected()) {
                selected.add(child);
            }
        }
        return selected;
    }

    @Override
    public void paint(Graphics graphics) {
        if (isSelected()) {
            enableSelectionStyle(graphics);
            graphics.drawRect(getX() - 1, getY() - 1, getWidth() + 1, getHeight() + 1);
            disableSelectionStyle(graphics);
        }

        for (Shape child : children) {
            child.paint(graphics);
        }
    }
}


//-> Demo.java: Клиентский код

public class Demo {
    public static void main(String[] args) {
        Editor editor = new Editor();
        editor.loadShapes(
                new Circle(10, 10, 10, Color.BLUE),

                new CompoundShape(
                        new Circle(110, 110, 50, Color.RED),
                        new Dot(160, 160, Color.RED)
                ),

                new CompoundShape(
                        new Rectangle(250, 250, 100, 100, Color.GREEN),
                        new Dot(240, 240, Color.GREEN),
                        new Dot(240, 360, Color.GREEN),
                        new Dot(360, 360, Color.GREEN),
                        new Dot(360, 240, Color.GREEN)
                )
        );
    }
}

```

## Шаги реализации

1. Определите класс создателя, объекты которого должны
   создавать снимки своегосостояния.
2. Создайтеклассснимкаиопишитевнёмвсетежеполя,которые имеются в оригинальном
   классе-создателе.
3. Сделайте объекты снимков неизменяемыми. Они должны
   получать начальные значения только один раз, через свой
   конструктор.
4. Если ваш язык программирования это позволяет, сделайте
   классснимкавложеннымвкласссоздателя.Еслинет,извле-
   ките из класса снимка пустой интерфейс, который будет
   доступеностальнымобъектампрограммы.Впоследствиивы
   можетедобавитьвэтотинтерфейснекоторыевспомогатель-
   ные методы, дающие доступ к метаданным снимка, однако
   прямой доступ к данным создателя должен бытьисключён.
5. Добавьте в класс создателя метод получения снимков.
   Создательдолженсоздаватьновыеобъектыснимков,пере-
   давая значения своих полей черезконструктор.   
   Сигнатура метода должна возвращать снимки через огра-
   ниченныйинтерфейс,еслионувасесть.Самклассдолжен
   работать с конкретным классомснимка.
6. Добавьтевкласссоздателяметодвосстановленияизсним-
   ка.Чтокасаетсяпривязкиктипам,руководствуйтесьтойже
   логикой, что и в пункте4.
7. Опекуны, будь то история операций, объекты команд или
   нечтоиное,должнызнатьотом,когдазапрашиватьснимки
   у создателя, где их хранить и когда восстанавливать.
8. Связь опекунов с создателями можно перенести внутрь
   снимков. В этом случае каждый снимок будет привязан к
   своему создателю и должен будет сам восстанавливать его
   состояние.Ноэтобудетработатьлибоесликлассыснимков
   вложены в классы создателей, либо если создатели имеют
   соответствующие сеттеры для установки значений
   своихполей.