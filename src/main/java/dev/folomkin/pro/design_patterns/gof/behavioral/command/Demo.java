package dev.folomkin.pro.design_patterns.gof.behavioral.command;


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
    public Editor editor;
    private String backup;

    Command(Editor editor) {
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
class CopyCommand extends Command {

    public CopyCommand(Editor editor) {
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

class PasteCommand extends Command {

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

//-> ./commands/CutCommand.java:
//-> Команда вырезания
class CutCommand extends Command {

    public CutCommand(Editor editor) {
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
    private Stack<Command> history = new Stack<>();

    // Последний зашедший...
    public void push(Command c) {/// Добавить команду в конец массива-истории.
        history.push(c);
    }

    // ...выходит первым.
    public Command pop() { /// Достать последнюю команду из массива-истории.
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

//-> ./Demo.java:
// Клиентский код

public class Demo {
    public static void main(String[] args) {
        Editor editor = new Editor();
        editor.init();
    }
}