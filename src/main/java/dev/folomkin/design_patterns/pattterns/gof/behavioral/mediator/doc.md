# Посредник (Mediator)

- это поведенческий паттерн проектирования,
  который позволяет уменьшить связанность множества
  классов между собой, благодаря перемещению этих связей
  в один класс-посредник.

Посредник убирает прямые связи между отдельными компонентами, заставляя их
общаться друг с другом через себя.

Применимость: Пожалуй, самое популярное применение Посредника в Java-коде — это
связь нескольких компонентов GUI одной программы.

Примеры Посредника в стандартных библиотеках Java:

java.util.Timer (все методы scheduleXXX())
java.util.concurrent.Executor#execute()
java.util.concurrent.ExecutorService (методы invokeXXX() и submit())
java.util.concurrent.ScheduledExecutorService (все методы scheduleXXX())
java.lang.reflect.Method#invoke()

## Структура

![mediator_structure.png](/img/design_pattern/design_patterns/mediator_structure.png)

1. **Компоненты** - эторазнородныеобъекты,содержащиебиз-
   нес-логику программы. Каждый компонент хранит ссылку
   на объект посредника, но работает с ним только через
   абстрактныйинтерфейспосредников.Благодаряэтому,ком-
   понентыможноповторноиспользоватьвдругойпрограмме,
   связав их с посредником другоготипа.
2. **Посредник** определяет интерфейс для обмена информаци-
   ей с компонентами. Обычно хватает одного метода, чтобы
   оповещатьпосредникаособытиях,произошедшихвкомпо-
   нентах.Впараметрахэтогометодаможнопередаватьдета-
   лисобытия:ссылкунакомпонент,вкоторомонопроизошло,
   и любые другиеданные.
3. **Конкретный посредник** содержит код взаимодействия
   нескольких компонентов между собой. Зачастую этот объект
   нетолько хранит ссылки на все свои компоненты, но и сам
   их создаёт, управляя дальнейшим жизненным циклом.
4. **Компоненты** не должны общаться друг с другом напрямую.
   Если в компоненте происходит важное событие, он должен оповестить своего
   посредника, а тот сам решит — касается ли событие других компонентов, и стоит
   ли их оповещать.
   При этом компонент-отправитель не знает кто обработает его запрос, а
   компонент-получатель не знает кто его прислал.

## Пример кода

Редактор заметок

Этот пример показывает как организовать множество элементов интерфейса при
помощи посредника так, чтобы они не знали и не зависели друг от друга.

![mediator_example.png](/img/design_pattern/design_patterns/mediator_example.png)

```java
//-> components: Классы-коллеги

//->  components/Component.java

/**
 * Общий интерфейс компонентов.
 */
public interface Component {
    void setMediator(Mediator mediator);

    String getName();
}


//-> components/AddButton.java

/**
 * Конкретные компоненты никак не связаны между собой. У них есть только один
 * канал общения – через отправку уведомлений посреднику.
 */
public class AddButton extends JButton implements Component {
    private Mediator mediator;

    public AddButton() {
        super("Add");
    }

    @Override
    public void setMediator(Mediator mediator) {
        this.mediator = mediator;
    }

    @Override
    protected void fireActionPerformed(ActionEvent actionEvent) {
        mediator.addNewNote(new Note());
    }

    @Override
    public String getName() {
        return "AddButton";
    }
}


//->  components/DeleteButton.java

/**
 * Конкретные компоненты никак не связаны между собой. У них есть только один
 * канал общения – через отправку уведомлений посреднику.
 */
public class DeleteButton extends JButton implements Component {
    private Mediator mediator;

    public DeleteButton() {
        super("Del");
    }

    @Override
    public void setMediator(Mediator mediator) {
        this.mediator = mediator;
    }

    @Override
    protected void fireActionPerformed(ActionEvent actionEvent) {
        mediator.deleteNote();
    }

    @Override
    public String getName() {
        return "DelButton";
    }
}


//->  components/Filter.java

/**
 * Конкретные компоненты никак не связаны между собой. У них есть только один
 * канал общения – через отправку уведомлений посреднику.
 */
public class Filter extends JTextField implements Component {
    private Mediator mediator;
    private ListModel listModel;

    public Filter() {
    }

    @Override
    public void setMediator(Mediator mediator) {
        this.mediator = mediator;
    }

    @Override
    protected void processComponentKeyEvent(KeyEvent keyEvent) {
        String start = getText();
        searchElements(start);
    }

    public void setList(ListModel listModel) {
        this.listModel = listModel;
    }

    private void searchElements(String s) {
        if (listModel == null) {
            return;
        }

        if (s.equals("")) {
            mediator.setElementsList(listModel);
            return;
        }

        ArrayList<Note> notes = new ArrayList<>();
        for (int i = 0; i < listModel.getSize(); i++) {
            notes.add((Note) listModel.getElementAt(i));
        }
        DefaultListModel<Note> listModel = new DefaultListModel<>();
        for (Note note : notes) {
            if (note.getName().contains(s)) {
                listModel.addElement(note);
            }
        }
        mediator.setElementsList(listModel);
    }

    @Override
    public String getName() {
        return "Filter";
    }
}


//-> components/List.java

/**
 * Конкретные компоненты никак не связаны между собой. У них есть только один
 * канал общения – через отправку уведомлений посреднику.
 */
@SuppressWarnings("unchecked")
public class List extends JList implements Component {
    private Mediator mediator;
    private final DefaultListModel LIST_MODEL;

    public List(DefaultListModel listModel) {
        super(listModel);
        this.LIST_MODEL = listModel;
        setModel(listModel);
        this.setLayoutOrientation(JList.VERTICAL);
        Thread thread = new Thread(new Hide(this));
        thread.start();
    }

    @Override
    public void setMediator(Mediator mediator) {
        this.mediator = mediator;
    }

    public void addElement(Note note) {
        LIST_MODEL.addElement(note);
        int index = LIST_MODEL.size() - 1;
        setSelectedIndex(index);
        ensureIndexIsVisible(index);
        mediator.sendToFilter(LIST_MODEL);
    }

    public void deleteElement() {
        int index = this.getSelectedIndex();
        try {
            LIST_MODEL.remove(index);
            mediator.sendToFilter(LIST_MODEL);
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }
    }

    public Note getCurrentElement() {
        return (Note) getSelectedValue();
    }

    @Override
    public String getName() {
        return "List";
    }

    private class Hide implements Runnable {
        private List list;

        Hide(List list) {
            this.list = list;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(300);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                if (list.isSelectionEmpty()) {
                    mediator.hideElements(true);
                } else {
                    mediator.hideElements(false);
                }
            }
        }
    }
}

//->  components/SaveButton.java

/**
 * Конкретные компоненты никак не связаны между собой. У них есть только один
 * канал общения – через отправку уведомлений посреднику.
 */
public class SaveButton extends JButton implements Component {
    private Mediator mediator;

    public SaveButton() {
        super("Save");
    }

    @Override
    public void setMediator(Mediator mediator) {
        this.mediator = mediator;
    }

    @Override
    protected void fireActionPerformed(ActionEvent actionEvent) {
        mediator.saveChanges();
    }

    @Override
    public String getName() {
        return "SaveButton";
    }
}

//->  components/TextBox.java

/**
 * Конкретные компоненты никак не связаны между собой. У них есть только один
 * канал общения – через отправку уведомлений посреднику.
 */
public class TextBox extends JTextArea implements Component {
    private Mediator mediator;

    @Override
    public void setMediator(Mediator mediator) {
        this.mediator = mediator;
    }

    @Override
    protected void processComponentKeyEvent(KeyEvent keyEvent) {
        mediator.markNote();
    }

    @Override
    public String getName() {
        return "TextBox";
    }
}

//->  components/Title.java

/**
 * Конкретные компоненты никак не связаны между собой. У них есть только один
 * канал общения – через отправку уведомлений посреднику.
 */
public class Title extends JTextField implements Component {
    private Mediator mediator;

    @Override
    public void setMediator(Mediator mediator) {
        this.mediator = mediator;
    }

    @Override
    protected void processComponentKeyEvent(KeyEvent keyEvent) {
        mediator.markNote();
    }

    @Override
    public String getName() {
        return "Title";
    }
}


//->  mediator

//->  mediator/Mediator.java: Общий интерфейс посредника

/**
 * Общий интерфейс посредников.
 */
public interface Mediator {
    void addNewNote(Note note);

    void deleteNote();

    void getInfoFromList(Note note);

    void saveChanges();

    void markNote();

    void clear();

    void sendToFilter(ListModel listModel);

    void setElementsList(ListModel list);

    void registerComponent(Component component);

    void hideElements(boolean flag);

    void createGUI();
}

// ->  mediator/Editor.java: Конкретный посредник

/**
 * Конкретный посредник. Все связи между конкретными компонентами переехали в
 * код посредника. Он получает извещения от своих компонентов и знает как на них
 * реагировать.
 */


public class Editor implements Mediator {
    private Title title;
    private TextBox textBox;
    private AddButton add;
    private DeleteButton del;
    private SaveButton save;
    private List list;
    private Filter filter;

    private JLabel titleLabel = new JLabel("Title:");
    private JLabel textLabel = new JLabel("Text:");
    private JLabel label = new JLabel("Add or select existing note to proceed...");

    /**
     * Здесь происходит регистрация компонентов посредником.
     */
    @Override
    public void registerComponent(Component component) {
        component.setMediator(this);
        switch (component.getName()) {
            case "AddButton":
                add = (AddButton) component;
                break;
            case "DelButton":
                del = (DeleteButton) component;
                break;
            case "Filter":
                filter = (Filter) component;
                break;
            case "List":
                list = (List) component;
                this.list.addListSelectionListener(listSelectionEvent -> {
                    Note note = (Note) list.getSelectedValue();
                    if (note != null) {
                        getInfoFromList(note);
                    } else {
                        clear();
                    }
                });
                break;
            case "SaveButton":
                save = (SaveButton) component;
                break;
            case "TextBox":
                textBox = (TextBox) component;
                break;
            case "Title":
                title = (Title) component;
                break;
        }
    }

    /**
     * Разнообразные методы общения с компонентами.
     */
    @Override
    public void addNewNote(Note note) {
        title.setText("");
        textBox.setText("");
        list.addElement(note);
    }

    @Override
    public void deleteNote() {
        list.deleteElement();
    }

    @Override
    public void getInfoFromList(Note note) {
        title.setText(note.getName().replace('*', ' '));
        textBox.setText(note.getText());
    }

    @Override
    public void saveChanges() {
        try {
            Note note = (Note) list.getSelectedValue();
            note.setName(title.getText());
            note.setText(textBox.getText());
            list.repaint();
        } catch (NullPointerException ignored) {
        }
    }

    @Override
    public void markNote() {
        try {
            Note note = list.getCurrentElement();
            String name = note.getName();
            if (!name.endsWith("*")) {
                note.setName(note.getName() + "*");
            }
            list.repaint();
        } catch (NullPointerException ignored) {
        }
    }

    @Override
    public void clear() {
        title.setText("");
        textBox.setText("");
    }

    @Override
    public void sendToFilter(ListModel listModel) {
        filter.setList(listModel);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void setElementsList(ListModel list) {
        this.list.setModel(list);
        this.list.repaint();
    }

    @Override
    public void hideElements(boolean flag) {
        titleLabel.setVisible(!flag);
        textLabel.setVisible(!flag);
        title.setVisible(!flag);
        textBox.setVisible(!flag);
        save.setVisible(!flag);
        label.setVisible(flag);
    }

    @Override
    public void createGUI() {
        JFrame notes = new JFrame("Notes");
        notes.setSize(960, 600);
        notes.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        JPanel left = new JPanel();
        left.setBorder(new LineBorder(Color.BLACK));
        left.setSize(320, 600);
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        JPanel filterPanel = new JPanel();
        filterPanel.add(new JLabel("Filter:"));
        filter.setColumns(20);
        filterPanel.add(filter);
        filterPanel.setPreferredSize(new Dimension(280, 40));
        JPanel listPanel = new JPanel();
        list.setFixedCellWidth(260);
        listPanel.setSize(320, 470);
        JScrollPane scrollPane = new JScrollPane(list);
        scrollPane.setPreferredSize(new Dimension(275, 410));
        listPanel.add(scrollPane);
        JPanel buttonPanel = new JPanel();
        add.setPreferredSize(new Dimension(85, 25));
        buttonPanel.add(add);
        del.setPreferredSize(new Dimension(85, 25));
        buttonPanel.add(del);
        buttonPanel.setLayout(new FlowLayout());
        left.add(filterPanel);
        left.add(listPanel);
        left.add(buttonPanel);
        JPanel right = new JPanel();
        right.setLayout(null);
        right.setSize(640, 600);
        right.setLocation(320, 0);
        right.setBorder(new LineBorder(Color.BLACK));
        titleLabel.setBounds(20, 4, 50, 20);
        title.setBounds(60, 5, 555, 20);
        textLabel.setBounds(20, 4, 50, 130);
        textBox.setBorder(new LineBorder(Color.DARK_GRAY));
        textBox.setBounds(20, 80, 595, 410);
        save.setBounds(270, 535, 80, 25);
        label.setFont(new Font("Verdana", Font.PLAIN, 22));
        label.setBounds(100, 240, 500, 100);
        right.add(label);
        right.add(titleLabel);
        right.add(title);
        right.add(textLabel);
        right.add(textBox);
        right.add(save);
        notes.setLayout(null);
        notes.getContentPane().add(left);
        notes.getContentPane().add(right);
        notes.setResizable(false);
        notes.setLocationRelativeTo(null);
        notes.setVisible(true);
    }
}


//->  mediator/Note.java: Класс заметок

/**
 * Класс заметок.
 */
public class Note {
    private String name;
    private String text;

    public Note() {
        name = "New note";
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getName() {
        return name;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return name;
    }
}


//->  Demo.java: Код инициализации

/**
 * Демо-класс. Здесь всё сводится воедино.
 */
public class Demo {
    public static void main(String[] args) {
        Mediator mediator = new Editor();

        mediator.registerComponent(new Title());
        mediator.registerComponent(new TextBox());
        mediator.registerComponent(new AddButton());
        mediator.registerComponent(new DeleteButton());
        mediator.registerComponent(new SaveButton());
        mediator.registerComponent(new List(new DefaultListModel()));
        mediator.registerComponent(new Filter());

        mediator.createGUI();
    }
}

```

## Шаги реализации

1. Найдите группу тесно переплетённых классов, отвязав кото-
   рые друг от друга, можно получить некоторую пользу.
   Например, чтобы повторно использовать их код в другой
   программе.
2. Создайте общий интерфейс посредников и опишите в нём
   методы для взаимодействия с компонентами. В простейшем
   случае достаточно одного метода для получения оповещений от компонентов.
   Этот интерфейс необходим, если вы хотите повторно
   использовать классы компонентов для других задач. В этом
   случае всё, что нужно сделать — это создать новый класс
   конкретного посредника.
3. Реализуйтеэтотинтерфейсвклассеконкретногопосредни-
   ка.Поместитевнегополя,которыебудутсодержатьссылки
   на все объектыкомпонентов.
4. Вы можете пойти дальше и переместить код создания ком-
   понентоввкласспосредника,послечегоонможетнапоми-
   натьфабрику илифасад.
5. Компоненты тоже должны иметь ссылку на объект посред-
   ника.Связьмеждунимиудобнеевсегоустановить,подавая
   посредника в параметры конструкторакомпонентов.
6. Изменитекодкомпонентовтак,чтобыонивызывалиметод
   оповещенияпосредника,вместометодовдругихкомпонен-
   тов.Спротивоположнойстороны,посредникдолженвызы-
   вать методы нужного компонента, когда получает
   оповещение откомпонента.

