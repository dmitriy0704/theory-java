# Состояние (State)

- это поведенческий паттерн проектирования,
  который позволяет объектам менять поведение в
  зависимости от своего состояния. Извне создаётся
  впечатление, что изменился классобъекта.

Поведения, зависящие от состояния, переезжают в отдельные классы. Первоначальный
класс хранит ссылку на один из таких объектов-состояний и делегирует ему работу.

Применимость: Паттерн Состояние часто используют в Java для превращения в
объекты громоздких стейт-машин, построенных на операторах switch.

Примеры Состояния в стандартных библиотеках Java:

javax.faces.lifecycle.LifeCycle#execute() (контролируемый из FacesServlet:
поведение зависит от текущей фазы (состояния) JSF)
Признаки применения паттерна: Методы класса делегируют работу одному вложенному
объекту.

## Структура

![state_structure.png](/img/design_pattern/design_patterns/state_structure.png)

1. Контекст хранит ссылку на объект состояния и делегирует ему часть работы,
   зависящей от состояний. Контекст работает с этим объектом через общий
   интерфейс состояний.
   Контекст должен иметь метод для присваивания ему нового объекта-состояния.
2. Состояние описывает общий интерфейс для всех конкретных состояний.
3. Конкретные состояния реализуют поведения, связанные с
   определённым состоянием контекста. Иногда приходится создавать целые иерархии
   классов состояний, чтобы обобщить дублирующий код.
   Состояние может иметь обратную ссылку на объект контекста. Через неё не
   только удобно получать из контекста нужную информацию, но и осуществлять
   смену его состояния.
4. И контекст, и объекты конкретных состояний могут решать, когда и какое
   следующее состояние будет выбрано. Чтобы переключить состояние, нужно подать
   другой объект-состояние в контекст.

## Пример кода

**Аудиоплеер**


> ![state_example.png](/img/design_pattern/design_patterns/state_example.png)
> _Пример изменение поведения проигрывателя с помощью состояний._

В этом примере паттерн Состояние изменяет функциональность одних и тех же
элементов управления музыкальным проигрывателем, в зависимости от того, в каком
состоянии находится сейчас проигрыватель.

Объект проигрывателя содержит объект-состояние, которому и делегирует основную
работу. Изменяя состояния, можно менять то, как ведут себя элементы управления
проигрывателя.

Основной класс плеера меняет своё поведение в зависимости от того, в каком
состоянии находится проигрывание.

```java
package dev.folomkin.design_patterns.pattterns.behavioral.state;


//-> ./states
//-> ./states/State.java: Общий интерфейс состояний

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Общий интерфейс всех состояний.
 */
abstract class State {
    dev.folomkin.pro.design_patterns.pattterns.gof.behavioral.state.Player player;

    /**
     * Контекст передаёт себя в конструктор состояния, чтобы состояние могло
     * обращаться к его данным и методам в будущем, если потребуется.
     */
    State(dev.folomkin.pro.design_patterns.pattterns.gof.behavioral.state.Player player) {
        this.player = player;
    }

    public abstract String onLock();

    public abstract String onPlay();

    public abstract String onNext();

    public abstract String onPrevious();
}

//->  ./states/LockedState.java: Состояние "заблокирован"

/**
 * Конкретные состояния реализуют методы абстрактного состояния по-своему.
 */
class LockedState extends dev.folomkin.pro.design_patterns.pattterns.gof.behavioral.state.State {

    LockedState(dev.folomkin.pro.design_patterns.pattterns.gof.behavioral.state.Player player) {
        super(player);
        player.setPlaying(false);
    }

    // При разблокировке проигрователя с заблокированными
    // клавишами он может принять одно из двух состояний.
    @Override
    public String onLock() {
        if (player.isPlaying()) {
            player.changeState(new dev.folomkin.pro.design_patterns.pattterns.gof.behavioral.state.ReadyState(player));
            return "Stop playing";
        } else {
            return "Locked...";
        }
    }

    @Override
    public String onPlay() {
        player.changeState(new dev.folomkin.pro.design_patterns.pattterns.gof.behavioral.state.ReadyState(player));
        return "Ready";
    }

    @Override
    public String onNext() {
        return "Locked...";
    }

    @Override
    public String onPrevious() {
        return "Locked...";
    }
}

//-> ./states/ReadyState.java: Состояние "готов"

/**
 * Они также могут переводить контекст в другие состояния.
 */
class ReadyState extends dev.folomkin.pro.design_patterns.pattterns.gof.behavioral.state.State {

    public ReadyState(dev.folomkin.pro.design_patterns.pattterns.gof.behavioral.state.Player player) {
        super(player);
    }

    @Override
    public String onLock() {
        player.changeState(new dev.folomkin.pro.design_patterns.pattterns.gof.behavioral.state.LockedState(player));
        return "Locked...";
    }

    @Override
    public String onPlay() {
        String action = player.startPlayback();
        player.changeState(new dev.folomkin.pro.design_patterns.pattterns.gof.behavioral.state.PlayingState(player));
        return action;
    }

    @Override
    public String onNext() {
        return "Locked...";
    }

    @Override
    public String onPrevious() {
        return "Locked...";
    }
}

//-> ./states/PlayingState.java: Состояние "проигрывание"

class PlayingState extends dev.folomkin.pro.design_patterns.pattterns.gof.behavioral.state.State {

    PlayingState(dev.folomkin.pro.design_patterns.pattterns.gof.behavioral.state.Player player) {
        super(player);
    }

    @Override
    public String onLock() {
        player.changeState(new dev.folomkin.pro.design_patterns.pattterns.gof.behavioral.state.LockedState(player));
        player.setCurrentTrackAfterStop();
        return "Stop playing";
    }

    @Override
    public String onPlay() {
        player.changeState(new dev.folomkin.pro.design_patterns.pattterns.gof.behavioral.state.ReadyState(player));
        return "Paused...";
    }

    @Override
    public String onNext() {
        return player.nextTrack();
    }

    @Override
    public String onPrevious() {
        return player.previousTrack();
    }
}

//-> ./ui
//-> ./ui/Player.java: Проигрыватель

/**
 * Проигрыватель выступает в роли контекста.
 */

class Player {
    private dev.folomkin.pro.design_patterns.pattterns.gof.behavioral.state.State state;
    private boolean playing = false;
    private List<String> playlist = new ArrayList<>();
    private int currentTrack = 0;

    public Player() {
        this.state = new dev.folomkin.pro.design_patterns.pattterns.gof.behavioral.state.ReadyState(this);
        setPlaying(true);
        for (int i = 1; i <= 12; i++) {
            playlist.add("Track " + i);
        }
    }

    // Другие объекты тоже должны иметь возможность заменять
    // состояние проигрывателя.
    public void changeState(dev.folomkin.pro.design_patterns.pattterns.gof.behavioral.state.State state) {
        this.state = state;
    }

    public dev.folomkin.pro.design_patterns.pattterns.gof.behavioral.state.State getState() {
        return state;
    }

    // Методы UI будут делегировать работу активному состоянию.
    public void setPlaying(boolean playing) {
        this.playing = playing;
    }

    public boolean isPlaying() {
        return playing;
    }

    public String startPlayback() {
        return "Playing " + playlist.get(currentTrack);
    }

    public String nextTrack() {
        currentTrack++;
        if (currentTrack > playlist.size() - 1) {
            currentTrack = 0;
        }
        return "Playing " + playlist.get(currentTrack);
    }

    public String previousTrack() {
        currentTrack--;
        if (currentTrack < 0) {
            currentTrack = playlist.size() - 1;
        }
        return "Playing " + playlist.get(currentTrack);
    }

    public void setCurrentTrackAfterStop() {
        this.currentTrack = 0;
    }
}

//-> ./ui/UI.java: GUI проигрывателя

class UI {
    private dev.folomkin.pro.design_patterns.pattterns.gof.behavioral.state.Player player;
    private static JTextField textField = new JTextField();

    public UI(dev.folomkin.pro.design_patterns.pattterns.gof.behavioral.state.Player player) {
        this.player = player;
    }

    public void init() {
        JFrame frame = new JFrame("Test player");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel context = new JPanel();
        context.setLayout(new BoxLayout(context, BoxLayout.Y_AXIS));
        frame.getContentPane().add(context);
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER));
        context.add(textField);
        context.add(buttons);

        // Контекст заставляет состояние реагировать на пользовательский ввод
        // вместо себя. Реакция может быть разной в зависимости от того, какое
        // состояние сейчас активно.
        JButton play = new JButton("Play");
        play.addActionListener(e -> textField.setText(player.getState().onPlay()));
        JButton stop = new JButton("Stop");
        stop.addActionListener(e -> textField.setText(player.getState().onLock()));
        JButton next = new JButton("Next");
        next.addActionListener(e -> textField.setText(player.getState().onNext()));
        JButton prev = new JButton("Prev");
        prev.addActionListener(e -> textField.setText(player.getState().onPrevious()));
        frame.setVisible(true);
        frame.setSize(300, 100);
        buttons.add(play);
        buttons.add(stop);
        buttons.add(next);
        buttons.add(prev);
    }
}

//-> ./Demo.java: Клиентский код

/**
 * Демо-класс. Здесь всё сводится воедино.
 */
public class Code {
    public static void main(String[] args) {
        dev.folomkin.pro.design_patterns.pattterns.gof.behavioral.state.Player player = new dev.folomkin.pro.design_patterns.pattterns.gof.behavioral.state.Player();
        dev.folomkin.pro.design_patterns.pattterns.gof.behavioral.state.UI ui = new dev.folomkin.pro.design_patterns.pattterns.gof.behavioral.state.UI(player);
        ui.init();
    }
}


```

## Шаги реализации

1. Определитесьсклассом,которыйбудетигратьрольконтек-
   ста.Этоможетбытькаксуществующийкласс,вкоторомуже
   естьзависимостьотсостояния,такиновыйкласс,есликод
   состояний размазан по несколькимклассам.
2. Создайте общий интерфейс состояний. Он должен описы-
   вать методы, общие для всех состояний, обнаруженных в
   контексте. Заметьте, что не всё поведение контекста нужно
   переносить в состояние, а только то, которое зависит от
   состояний.
3. Длякаждогофактическогосостояниясоздайтекласс,реали-
   зующий интерфейс состояния. Переместите код, связанный
   сконкретнымисостояниямивнужныеклассы.Вконцекон-
   цов,всеметодыинтерфейсасостояниядолжныбытьреали-
   зованы во всех классахсостояний.<br><br>
   Припереносеповеденияизконтекставыможетестолкнутьсястем,чтоэтоповедениезависитотприватныхполейили
   методовконтекста,ккоторымнетдоступаизобъектасосто-
   яния. Существует парочка способов обойти этупроблему.<br><br>
   Самый простой — оставить поведение внутри контекста,
   вызывая его из объекта состояния. С другой стороны, вы
   можетесделатьклассысостоянийвложеннымивкласскон-
   текста, и тогда они получат доступ ко всем приватным
   частям контекста. Но последний способ доступен только в
   некоторых языках программирования (например, Java, C#).
4. Создайте в контексте поле для хранения объектов-состо-
   яний, а также публичный метод для изменения значения
   этогополя.
5. Старые методы контекста, в которых находился зависимый
   от состояния код, замените на вызовы соответствующих
   методов объекта-состояния.
6. В зависимости от бизнес-логики, разместите код, который
   переключает состояние контекста либо внутри контекста,
   либо внутри классов конкретныхсостояний.