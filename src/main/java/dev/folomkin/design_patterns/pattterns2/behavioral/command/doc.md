# Команда (Command)

- это поведенческий паттерн проектирования,
  который превращает запросы в объекты, позволяя
  передавать их как аргументы при вызове методов, ставить
  запросы в очередь, логировать их, а также поддерживать
  отмену операций.

## Структура

![command_structure.png](/img/design_pattern/design_patterns/command_structure.png)

1. Отправитель хранит ссылку на объект команды и обращается к нему, когда нужно
   выполнить какое-то действие. Отправитель работает с командами только через их
   общий интерфейс. Он не знает, какую конкретно команду использует, так как
   получает готовый объект команды от клиента.
2. Команда описывает общий для всех конкретных команд интерфейс. Обычно здесь
   описан всего один метод для запуска команды.
3. Конкретные команды реализуют различные запросы, сле-
   дуяобщемуинтерфейсукоманд.Обычнокоманданеделает
   всюработусамостоятельно,алишьпередаётвызовполуча-
   телю, которым является один из объектов бизнес-логики.
   Параметры,скоторымикомандаобращаетсякполучателю,
   следуетхранитьввидеполей.Вбольшинствеслучаевобъ-
   екты команд можно сделать неизменяемыми, передавая в
   них все необходимые параметры только черезконструктор.
4. Получатель содержит бизнес-логику программы. В этой
   роли может выступать практически любой объект. Обычно
   команды перенаправляют вызовы получателям. Но иногда,
   чтобыупроститьпрограмму,выможетеизбавитьсяотполу-
   чателей, «слив» их код в классыкоманд.
5. Клиент создаёт объекты конкретных команд, передавая в
   нихвсенеобходимыепараметры,средикоторыхмогутбыть
   и ссылки на объекты получателей. После этого клиент свя-
   зывает объекты отправителей с созданными командами.

## Пример

ВэтомпримерепаттернКоманда служитдляведенияисто-
рии выполненных операций, позволяя отменять их, если
потребуется.

![command_example.png](/img/design_pattern/design_patterns/command_example.png)

Пример реализации отмены в текстовом редакторе.

```java
public abstract class Command {
    public Editor editor;
    private String backup;

    Command(Editor editor) {
        this.editor = editor;
    }

    void backup() {
        backup = editor.textField.getText();
    }

    public void undo() {
        editor.textField.setText(backup);
    }

    public abstract boolean execute();
}


public class CommandHistory {
    private Stack<Command> history = new Stack<>();

    public void push(Command c) {
        history.push(c);
    }

    public Command pop() {
        return history.pop();
    }

    public boolean isEmpty() {
        return history.isEmpty();
    }
}


public class CopyCommand extends Command {

    public CopyCommand(Editor editor) {
        super(editor);
    }

    @Override
    public boolean execute() {
        editor.clipboard = editor.textField.getSelectedText();
        return false;
    }
}


public class CutCommand extends Command {

    public CutCommand(Editor editor) {
        super(editor);
    }

    @Override
    public boolean execute() {
        if (editor.textField.getSelectedText().isEmpty()) return false;

        backup();
        String source = editor.textField.getText();
        editor.clipboard = editor.textField.getSelectedText();
        editor.textField.setText(cutString(source));
        return true;
    }

    private String cutString(String source) {
        String start = source.substring(0, editor.textField.getSelectionStart());
        String end = source.substring(editor.textField.getSelectionEnd());
        return start + end;
    }
}


public class PasteCommand extends Command {

    public PasteCommand(Editor editor) {
        super(editor);
    }

    @Override
    public boolean execute() {
        if (editor.clipboard == null || editor.clipboard.isEmpty())
            return false;

        backup();
        editor.textField.insert(editor.clipboard, editor.textField.getCaretPosition());
        return true;
    }
}


public class Editor {
    public JTextArea textField;
    public String clipboard;
    private CommandHistory history = new CommandHistory();

    public void init() {
        JFrame frame = new JFrame("Text editor (type & use buttons, Luke!)");
        JPanel content = new JPanel();
        frame.setContentPane(content);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        textField = new JTextArea();
        textField.setLineWrap(true);
        content.add(textField);
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton ctrlC = new JButton("Ctrl+C");
        JButton ctrlX = new JButton("Ctrl+X");
        JButton ctrlV = new JButton("Ctrl+V");
        JButton ctrlZ = new JButton("Ctrl+Z");
        Editor editor = this;
        ctrlC.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                executeCommand(new CopyCommand(editor));
            }
        });
        ctrlX.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                executeCommand(new CutCommand(editor));
            }
        });
        ctrlV.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                executeCommand(new PasteCommand(editor));
            }
        });
        ctrlZ.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                undo();
            }
        });
        buttons.add(ctrlC);
        buttons.add(ctrlX);
        buttons.add(ctrlV);
        buttons.add(ctrlZ);
        content.add(buttons);
        frame.setSize(450, 200);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void executeCommand(Command command) {
        if (command.execute()) {
            history.push(command);
        }
    }

    private void undo() {
        if (history.isEmpty()) return;

        Command command = history.pop();
        if (command != null) {
            command.undo();
        }
    }
}


public class Demo {
    public static void main(String[] args) {
        Editor editor = new Editor();
        editor.init();
    }
}
```

## Шаги реализации

1. Создайте общий интерфейс команд и определите в нём
   метод запуска.
2. Один за другим создайте классы конкретных команд. В каж-
   дом классе должно быть поле для хранения ссылки на один
   или несколько объектов-получателей, которым команда
   будет перенаправлять основную работу.    
   Кроме этого, команда должна иметь поля для хранения
   параметров, которые нужны при вызове методов получателя. Значения всех этих
   полей команда должна получать через конструктор.   
   И, наконец, реализуйте основной метод команды, вызывая в нём те или иные методы получателя.
3. Добавьтевклассыотправителейполядляхранениякоманд. Обычно объекты-отправители принимают готовые объекты команд извне — через конструктор либо через сеттер поля команды.
4. Измените основной код отправителей так, чтобы они делегировали выполнение действия команде.
5. Порядок инициализации объектов должен выглядеть так:
    - Создаём объекты получателей.
    - Создаём объекты команд, связав их с получателями.
    - Создаём объекты отправителей, связав их с командами.


