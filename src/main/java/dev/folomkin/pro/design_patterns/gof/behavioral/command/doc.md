# Команда (Command)

- это поведенческий паттерн проектирования, который превращает запросы в
  объекты, позволяя передавать их как аргументы при вызове методов, ставить
  запросы в очередь, логировать их, а также поддерживать отмену операций.

Это позволяет откладывать выполнение команд, выстраивать их в очереди, а также
хранить историю и делать отмену.

Применимость: Паттерн можно часто встретить в Java-коде, особенно когда нужно
откладывать выполнение команд, выстраивать их в очереди, а также хранить историю
и делать отмену.

Примеры Команд в стандартных библиотеках Java:

Все реализации java.lang.Runnable

Все реализации javax.swing.Action

Признаки применения паттерна: Классы команд построены вокруг одного действия и
имеют очень узкий контекст. Объекты команд часто подаются в обработчики событий
элементов GUI. Практически любая реализация отмены использует принципа команд.

## Структура

![command_structure.png](/img/design_pattern/design_patterns/command_structure.png)

1. Отправитель хранит ссылку на объект команды и обращается к нему, когда нужно
   выполнить какое-то действие. Отправитель работает с командами только через их
   общий интерфейс. Он не знает, какую конкретно команду использует, так как
   получает готовый объект команды от клиента.
2. Команда описывает общий для всех конкретных команд интерфейс. Обычно здесь
   описан всего один метод для запуска команды.
3. Конкретные команды реализуют различные запросы, следуя общему интерфейсу
   команд. Обычно команда не делает всю работу самостоятельно, а лишь передаёт
   вызов получателю, которым является один из объектов бизнес-логики. Параметры,
   с которыми команда обращается к получателю, следует хранить в виде полей. В
   большинстве случаев объекты команд можно сделать неизменяемыми, передавая в
   них все необходимые параметры только через конструктор.
4. Получатель содержит бизнес-логику программы. В этой роли может выступать
   практически любой объект. Обычно команды перенаправляют вызовы получателям.
   Но иногда, чтобы упростить программу, вы можете избавиться от получателей,
   «слив» их код в классы команд.
5. Клиент создаёт объекты конкретных команд, передавая в них все необходимые
   параметры, среди которых могут быть и ссылки на объекты получателей. После
   этого клиент связывает объекты отправителей с созданными командами.

## Пример кода

**Команды текстового редактора и их отмена**

По реакции на действия пользователя, текстовый редактор создаёт объекты команд.
Каждая команда выполняет некоторое действие, а затем попадают в стек истории.

Теперь, чтобы выполнить отмену, мы берём последнюю команду из списка и выполняем
обратное действие либо восстанавливаем состояние редактора, сохранённое в этой
команде.

```java
package dev.folomkin.design_patterns.pattterns.behavioral.command;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Stack;

//-> ./commands
//-> ./ commands/Command.java: Абстрактная базовая команда

/**
 * Абстрактная команда задаёт общий интерфейс для конкретных
 * классов команд и содержит базовое поведение отмены операции.
 */


abstract class Command {
    public command.behavioral.gof.dev.folomkin.pro.design_patterns.Editor editor;
    private String backup;

    Command(command.behavioral.gof.dev.folomkin.pro.design_patterns.Editor editor) {
        this.editor = editor;
    }

    // Сохраняем состояние редактора.
    void backup() {
        backup = editor.textField.getText();
    }

    // Восстанавливаем состояние редактора.
    public void undo() {
        editor.textField.setText(backup);
    }


    // Главный метод команды остаётся абстрактным, чтобы каждая
    // конкретная команда определила его по-своему. Метод должен
    // возвратить true или false в зависимости о того, изменила
    // ли команда состояние редактора, а значит, нужно ли её
    // сохранить в истории.
    public abstract boolean execute();
}

//-> ./commands/CopyCommand.java: Команда копирования
// Конкретные команды.
class CopyCommand extends command.behavioral.gof.dev.folomkin.pro.design_patterns.Command {

    public CopyCommand(command.behavioral.gof.dev.folomkin.pro.design_patterns.Editor editor) {
        super(editor);
    }

    // Команда копирования не записывается в историю, так как
    // она не меняет состояние редактора
    @Override
    public boolean execute() {
        editor.clipboard = editor.textField.getSelectedText();
        return false;
    }
}

//-> ./commands/PasteCommand.java:
//-> Команда вставки

class PasteCommand extends command.behavioral.gof.dev.folomkin.pro.design_patterns.Command {

    public PasteCommand(command.behavioral.gof.dev.folomkin.pro.design_patterns.Editor editor) {
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

//-> ./commands/CutCommand.java:
//-> Команда вырезания
class CutCommand extends command.behavioral.gof.dev.folomkin.pro.design_patterns.Command {

    public CutCommand(command.behavioral.gof.dev.folomkin.pro.design_patterns.Editor editor) {
        super(editor);
    }

    // Команды, меняющие состояние редактора, сохраняют
    // состояние редактора перед своим действием и сигнализируют
    // об изменении, возвращая true.
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

//-> ./commands/CommandHistory.java:
// История команд
// Глобальная история команд — это стек.
class CommandHistory {
    private Stack<command.behavioral.gof.dev.folomkin.pro.design_patterns.Command> history = new Stack<>();

    // Последний зашедший...
    public void push(command.behavioral.gof.dev.folomkin.pro.design_patterns.Command c) {/// Добавить команду в конец массива-истории.
        history.push(c);
    }

    // ...выходит первым.
    public command.behavioral.gof.dev.folomkin.pro.design_patterns.Command pop() { /// Достать последнюю команду из массива-истории.
        return history.pop();
    }

    public boolean isEmpty() {
        return history.isEmpty();
    }
}

//-> ./editor
//-> ./editor/Editor.java:
// Оболочка текстового редактора

/**
 * Класс редактора содержит непосредственные операции над текстом.
 * Он отыгрывает роль получателя — команды делегируют ему свои действия.
 */
class Editor {
    public JTextArea textField;
    public String clipboard;
    private command.behavioral.gof.dev.folomkin.pro.design_patterns.CommandHistory history = new command.behavioral.gof.dev.folomkin.pro.design_patterns.CommandHistory();

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
        command.behavioral.gof.dev.folomkin.pro.design_patterns.Editor editor = this;
        ctrlC.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                executeCommand(new command.behavioral.gof.dev.folomkin.pro.design_patterns.CopyCommand(editor));
            }
        });
        ctrlX.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                executeCommand(new command.behavioral.gof.dev.folomkin.pro.design_patterns.CutCommand(editor));
            }
        });
        ctrlV.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                executeCommand(new command.behavioral.gof.dev.folomkin.pro.design_patterns.PasteCommand(editor));
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

    private void executeCommand(command.behavioral.gof.dev.folomkin.pro.design_patterns.Command command) {
        if (command.execute()) {
            history.push(command);
        }
    }

    private void undo() {
        if (history.isEmpty()) return;

        command.behavioral.gof.dev.folomkin.pro.design_patterns.Command command = history.pop();
        if (command != null) {
            command.undo();
        }
    }
}

//-> ./Demo.java:
// Клиентский код

public class Demo {
    public static void main(String[] args) {
        command.behavioral.gof.dev.folomkin.pro.design_patterns.Editor editor = new command.behavioral.gof.dev.folomkin.pro.design_patterns.Editor();
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
   И, наконец, реализуйте основной метод команды, вызывая в нём те или иные
   методы получателя.
3. Добавьтевклассыотправителейполядляхранениякоманд. Обычно объекты-отправители
   принимают готовые объекты команд извне — через конструктор либо через сеттер
   поля команды.
4. Измените основной код отправителей так, чтобы они делегировали выполнение
   действия команде.
5. Порядок инициализации объектов должен выглядеть так:
    - Создаём объекты получателей.
    - Создаём объекты команд, связав их с получателями.
    - Создаём объекты отправителей, связав их с командами.


