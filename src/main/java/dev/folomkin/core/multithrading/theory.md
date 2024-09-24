# МНОГОПОТОЧНОСТЬ

[//]: # (Список заголовков для оглавления: 
        #titile0: Класс Thread и интерфейс Runnable
        #titile1: Жизненный цикл потока
        #titile2:
        #titile3:
        #titile4:
)

**_<a id="titlelist">ОГЛАВЛЕНИЕ:</a>_**

1. [Класс Thread и интерфейс Runnable](#title0)
2. [Жизненный цикл потока](#title1)
3. [Приоритеты потоков](#title2)
3. [Синхронизация](#title3)

## <a id="title0">ЖИЗНЕННЫЙ ЦИКЛ ПОТОКА</a>

[Наверх](#titlelist)

![thread-state0.png](/img/threads/thread-state0.png)

Поток готов к запуску когда он создается и запускается путем вызова метода
**start()** в классе Thread. Когда в Thread вызывается **sleep()** или
**wait()** поток переходит в неработоспособное состояние.

Состояния хранятся как статические перечисления в классе Thread.

Состояния потока

- **_NEW_** - Это состояние представляет собой недавно созданный поток, который
  еще не начал свое выполнение. В этом состоянии поток готов к планированию
  операционной системой, но ему не назначено никакого процессорного времени.
- **_RUNNABLE_** - Поток в этом состоянии либо запущен, либо готов к выполнению.
  Однако он может ожидать выделения ресурсов, таких как процессорное время или
  доступ к общему ресурсу. Как только необходимые ресурсы будут доступны, поток
  может продолжить свое выполнение.
- **_BLOCKED_** - В этом состоянии поток ожидает получения блокировки монитора
  (monitor lock), которая необходима для ввода или повторного ввода
  синхронизированного блока или метода. Поток остается в этом состоянии до тех
  пор, пока monitor lock не станет доступен.
- **_WAITING_** - Когда поток находится в этом состоянии, он ожидает, пока
  какой-либо другой поток выполнит определенное действие без каких-либо
  временных ограничений. Это может произойти, например, когда один поток ожидает
  сигнала от другого для продолжения своего выполнения.
- **_TIMED_WAITING_** - он похож на **WAITING**, но есть некоторые отличия. Это
  состояние возникает, когда один поток ожидает от другого выполнения
  определенного действия, но только в течение определенного периода времени.
- **_TERMINATED_** - состояние, когда поток завершил свое выполнение. Это
  означает, что он завершил назначенную ему задачу и больше не активен. Важно
  отметить, что как только поток достигает этого состояния, его нельзя
  перезапустить или возобновить работу.

**_ПОЛУЧЕНИЕ СОСТОЯНИЯ ПОТОКА:_**

### NEW

```java
class State implements Runnable {
    public void run() {
    }
}

public class Code {
    public static void main(String[] args) {
        Runnable runnable = new State();
        Thread thread = new Thread(runnable);
        System.out.println("Thread state: " + thread.getState());
        // -> Thread state: NEW
    }
}
```

В многопоточной среде планировщик потоков(Thread-Scheduler) (который является
частью JVM) выделяет фиксированное количество времени для каждого потока. Таким
образом, он выполняется в течение определенного периода времени, затем передает
управление другим выполняемым потокам.

### RUNNABLE

Когда мы создаем новый поток и вызываем для него метод start(), он переходит из
состояния NEW в состояние RUNNABLE. Потоки в этом состоянии либо запущены, либо
готовы к запуску, но они ожидают выделения ресурсов системой.

```java
class State implements Runnable {
    public void run() {
    }
}

public class Code {
    public static void main(String[] args) {
        Runnable runnable = new State();
        Thread thread = new Thread(runnable);
        thread.start();
        System.out.println("Thread state: " + thread.getState());
        // -> Thread state: RUNNABLE
    }
}
```

Теперь метод t.getState() вероятнее всего в консоль выведет "RUNNABLE".
Почему вероятнее всего? Дело в том, что когда наш элемент управления достигнет
t.getState(), мы не всегда можем быть уверены, что он будет находиться в
состоянии **_RUNNABLE_**. Это связано с тем, что в некоторых случаях элемент
может быть немедленно запланирован планировщиком потоков
(**_Thread-Scheduler_**) и завершить своё выполнение. Именно в таких ситуациях
возможны другие результаты.

### BLOCKED

Поток переходит в состояние BLOCKED, когда ожидает блокировки монитора(monitor
lock) и пытается получить доступ к разделу кода, который заблокирован каким-либо
другим потоком.

```java
class State implements Runnable {
    @Override
    public void run() {
        commonResource();
    }

    public static synchronized void commonResource() {
        while (true) {
        }
    }
}

public class Code {
    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(new State());
        Thread t2 = new Thread(new State());
        t1.start();
        t2.start();
        Thread.sleep(1000);
        System.out.println(t2.getState()); // -> BLOCKED
        System.exit(0);
    }
}
```

_**Разбор кода:**_

1. Мы создали два разных потока – t1 и t2
2. t1 запускается и вводит синхронизированный метод commonResource(); это
   означает, что только один поток может получить к нему доступ; все остальные
   последующие потоки, которые попытаются получить доступ к этому методу, будут
   заблокированы от дальнейшего выполнения до тех пор, пока текущий не завершит
   обработку.
3. Когда t1 входит в этот метод, он сохраняется в бесконечном цикле while; Это
   сделано для имитации интенсивной обработки, чтобы все остальные потоки не
   могли войти в этот метод.
4. Теперь, когда мы запускаем t2, он пытается ввести метод commonResource(), к
   которому уже обращается t1, таким образом, t2 будет сохранен в состоянии
   BLOCKED.
5. Вызовем t2.getState() и получим результат "BLOCKED"

### WAITING

Поток находится в состоянии WAITING, когда он ожидает, пока какой-либо другой
поток выполнит определенное действие. Согласно JavaDocs, любой поток может войти
в это состояние, вызвав любой из этих трех методов:

- object.wait()
- thread.join() - в потоке1 вызывается поток2.join() и это переводит поток1 в
  состояние ожидания;
- LockSupport.park()

```java
public class Code implements Runnable {
    public static Thread t1;

    public static void main(String[] args) throws InterruptedException {
        t1 = new Thread(new Code());
        t1.start();
    }

    @Override
    public void run() {
        Thread t2 = new Thread(new State());
        t2.start();
        try {
            t2.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

class State implements Runnable {
    @Override
    public void run() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println(Code.t1.getState()); // -> WAITING
    }
}
```

Разбор кода:

1. Мы создали и запустили t1
2. t1 создает t2 и запускает его
3. Пока продолжается работа t2, мы вызываем t2.join(), это переводит t1 в
   состояние ожидания(WAITING), пока t2 не завершит выполнение.
4. Поскольку t1 ожидает завершения t2, мы вызываем t1.getState() из t2 и
   получаем результат "WAITING"

### TIMED_WAITING

Поток находится в состоянии TIMED_WAITING, когда он ожидает, пока другой поток
выполнит определенное действие в течение заданного промежутка времени.

Пять способов перевести поток в это состояние:

1. thread.sleep(long millis)
2. wait(int timeout) or wait(int timeout, int nanos)
3. thread.join(long millis)
4. LockSupport.parkNanos
5. LockSupport.parkUntil

```java
public class Code {
    public static void main(String[] args) throws InterruptedException {
        State runnable = new State();
        Thread thread = new Thread(runnable);
        thread.start();
        Thread.sleep(1000);
        System.out.println(thread.getState());
        // -> TIMED_WAITING
    }
}

class State implements Runnable {
    @Override
    public void run() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
```

Поток создается и переводится в спящее состояние на 5сек.
Результат - TIMED_WAITING

### TERMINATED

Состояние мертвого потока. Поток находится в состоянии TERMINATED, когда он
завершил выполнение, либо прерван.

```java
public class Code implements Runnable {
    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(new Code());
        t1.start();
        Thread.sleep(1000);
        System.out.println(t1.getState()); //-> TERMINATED
    }

    @Override
    public void run() {

    }
}
```

Запускается поток t1, но метод Thread.sleep(1000) дает время, для завершения t1,
вследствие чего, эта программа выдает нам в результате "TERMINATED".

## <a id="title1">ПРИОРИТЕТЫ ПОТОКОВ</a>

[Наверх](#titlelist)

Каждому потоку в Java назначается приоритет, который определяет способ
обработки данного потока в сравнении с другими потоками. Приоритеты
потоков представляют собой целые числа, определяющие относительный приоритет
одного потока над другим. Приоритет потока применяется для принятия
решения о том, когда переключаться с одного работающего потока на другой.
Прием называется переключением контекста. Правила, которые определяют,
когда происходит переключение контекста:

- _Поток может добровольно передать управление_. Это происходит при явной
  уступке очереди, приостановке или блокировке. В таком сценарии
  проверяются все остальные потоки, и поток с наивысшим приоритетом, готовый к
  выполнению, получает ЦП.
- _Поток может быть вытеснен потоком с более высоким приоритетом_.
  В таком случае поток с более низким приоритетом, который не уступает
  ЦП, просто вытесняется - независимо от того, что он делает - потоком с более
  высоким приоритетом. По сути, как только поток с более
  высоким приоритетом желает запуститься, он это делает. Прием называется
  вытесняющей многозадачностью.

## <a id="title2">СИНХРОНИЗАЦИЯ</a>

[Наверх](#titlelist)

Для синхронизации между процессами используется монитор. Как только поток входит
в монитор остальные потоки ждут пока монитор освободится. Как только поток
оказывается внутри синхронизированного метода, остальные потоки не могут
вызывать какой-либо другой синхронизированный метод для того же объекта.

## <a id="title3">ОБМЕН СООБЩЕНИЯМИ</a>

[Наверх](#titlelist)

Java предоставляет нескольким потокам ясный и экономичный способ взаимодействия
друг с другом через вызовы предопределенных методов, которые есть у всех
объектов. Система обмена сообщениями Java позволяет потоку войти в
синхронизированный метод объекта и затем ожидать, пока какой-то другой поток
явно не уведомит ero о выходе.

## <a id="title4">КЛАСС THREAD И ИНТЕРФЕЙС RUNNABLE</a>

[Наверх](#titlelist)

Потоки — средство, которое помогает организовать одновременное выполнение
нескольких задач, каждой в независимом потоке. Потоки представляют собой
экземпляры классов, каждый из которых запускается и функционирует
самостоятельно, автономно (или относительно автономно) от главного потока
выполнения программы. Существует три способа создания и запуска потока: на
основе расширения класса Thread, реализации интерфейсов Runnable или Callable.
Класс Thread инкапсулирует поток выполнения.

**МЕТОДЫ КЛАССА THREAD:**

- getName() - имя потока
- getPriority() - приоритет
- isAlive() - запущен ли поток
- join() - ожидает прекращения работы потока
- run() - точка входа в поток
- sleep() - приостановка потока
- start() - запуск потока вызовом его метода run()

```java
class WalkThread extends Thread {
    public void run() {
        try {
            for (int i = 0; i < 7; i++) {
                System.out.println("Walking " + i);
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } finally {
            System.out.println(Thread.currentThread().getName() + " is over");
        }
    }
}

class TalkThread implements Runnable {
    @Override
    public void run() {
        try {
            for (int i = 0; i < 7; i++) {
                System.out.println("Talking -> " + i);
                try {
                    Thread.sleep(1);
                } catch (InterruptedException exception) {
                    exception.printStackTrace();
                }
            }
        } finally {
            System.out.println(Thread.currentThread().getName() + " is over");
        }
    }
}

public class Code {
    public static void main(String[] args) {
        WalkThread walk = new WalkThread(); // -> Новый объект thread
        walk.start(); // -> старт потока

        Thread talk = new Thread(new TalkThread()); // -> Создание потока 
        talk.start(); // -> старт потока

//      // Или лямбда
//      (new Thread(() ->
//              System.out.println(
//                      Thread.currentThread().getName() + " is over")
//      )).start(); // -> Новый объект thread и его старт

        // Или так
//      Runnable runnable = ()-> System.out.println("Thread started");
//      new Thread(runnable).start();

    }
}
```

Запуск двух потоков для объектов классов WalkThread непосредственно
и TalkThread через инициализацию экземпляра Thread приводит к выводу
строк: Walk n и Talk -->n. Порядок вывода, как правило, различен при
нескольких запусках приложения.

В конце работы каждого потока происходит вызов Thread.currentThread().getName(),
обеспечивающий вывод на консоль имени потока, в котором произошел вызов. В
данном случае это будут строки Thread-1 и Thread-2. Имена даются потокам по
умолчанию либо с помощью метода setName(String name)или конструктора потока.
Статический метод currentThread() дает доступ к потоку, в котором он вызван.

Интерфейс Runnable не имеет метода start(), а только единственный метод run().
Поэтому для запуска такого потока, как TalkThread, следует создать
экземпляр класса Thread с передачей экземпляра TalkThread его конструктору.
Однако при прямом вызове метода run() поток не запустится, выполнится только
тело самого метода.

## <a id="title5">ГЛАВНЫЙ ПОТОК</a>

[Наверх](#titlelist)

Главный поток запускается при запуске программы. Из него порождаются остальные
потоки. Часто должен завершаться последним.

Управлять главным потоком можно с помощью объекта Thread и метода
currentThread(): `static Thread currentThread ( )`

```java
public static void main(String[] args) throws InterruptedException {
    Thread t = Thread.currentThread();
    System.out.println("Текущий поток: " + t);
    // Изменяем имя потока:
    t.setName("My Thread");
    System.out.println("Имя потока после изменения : " + t);
    try {
        for (int i = 5; i > 0; i--) {
            System.out.println(i);
            Thread.sleep(1000);
        }
    } catch (InterruptedException e) {
        System.out.println("Главный поток прерван");
    }
    // ->
    // Текущий пото к : Thread [ mai n , 5 , ma i п ]
    // После изменения имени : Thread [ My Thread , 5 , maiп ]
    // 5
    // 4
    // 3
    // 2
    // 1   

}
```

В программе ссылка на текущий (в данном случае главный) поток получается вызовом
currentThread() и сохраняется в локальной переменной t. Затем программа
отображает информацию о потоке, вызывает setName() для изменения внутреннего
имени потока и повторно отображает информацию о потоке. Далее выполняется
обратный отсчет от пяти до единицы с паузой, составляющей одну секунду, между
выводом строк. Пауза достигается методом sleep(). Аргумент функции sleep()
задает период задержки в миллисекундах. Обратите внимание на блок try/ catch,
внутрь которого помещен цикл. Метод sleep() в Thread может сгенерировать
исключение InterruptedException в случае, если какой-то другой поток пожелает
прервать этот спящий поток. В данном примере просто вводится сообщение, если
поток прерывается, но в реальной программе пришлось бы поступать по-другому.

По порядку отображается имя потока, его приоритет и имя его группы. По умолчанию
именем главного потока является main. Его приоритет равен 5 - стандартному
значению, и main также будет именем группы потоков, к которой принадлежит
текущий поток. Группа потоков представляет собой структуру данных, которая
управляет состоянием набора потоков в целом. После изменения имени потока,
переменная t снова выводится. На этот раз отображается новое имя потока.

## <a id="title6">СОЗДАНИЕ ПОТОКА</a>

[Наверх](#titlelist)

Поток создается путем создания экземпляр объекта типа Thread. В языке Java
предусмотрены два способа:

- можно реализовать интерфейс Runnable;
- можно расширить класс Thread.

### Реализация интерфейса Runnable

Самый простой способ создать поток - создание класса, реализующего интерфейс
Runnable, который абстрагирует единицу исполняемого кода. Можно создать поток
для любого объекта, реализующего Runnable. Для реализации Runnable в классе
понадобится реализовать только один метод с именем run(): `public void run()`

Внутрь метода run() помещается код, который основывает новый поток. Метод run()
может вызывать другие методы, использовать другие классы и объявлять переменные
в точности, как это делает главный поток. Единственное отличие заключается в
том, что метод run() устанавливает точку входа для другого параллельного потока
выполнения в программе. Этот поток завершится, когда управление возвратится из
run().

После создания класса, реализующего интерфейс Runnable, внутри него создается
объект типа Thread. В классе Thread определено несколько конструкторов:

`Thread(Runnable threadOb, String threadName)`

В приведенном конструкторе threadOb является экземпляром класса, реализующего
интерфейс Runnable. Он определяет, где начнется выполнение потока. Имя нового
потока определяется параметром threadName.

Для запуска нового потока вызывается метод start().

```java
class NewThread implements Runnable { //-> Создание второго потока
    Thread t;

    NewThread() {
        t = new Thread(this, "Demo Thread");
        // -> Первый параметр this для того что бы новый поток вызвал метод
        // run() для данного объекта
        System.out.println("Дочерний поток: " + t);
    }

    public void run() { //-> Точка входа для второго потока
        try {
            for (int i = 5; i > 0; i--) {
                System.out.println("Дочерний поток: " + i);
                Thread.sleep(500);
            }
        } catch (InterruptedException exception) {
            System.out.println("Дочерний поток прерван");
        }
        System.out.println("Завершение дочернего потока");
    }
}

public class Code {
    public static void main(String[] args) throws InterruptedException {
        NewThread nt = new NewThread(); // Создание нового потока
        nt.t.start();                   // Запуск нового потока
        try {
            for (int i = 5; i > 0; i--) {
                System.out.println("Главный поток: " + i);
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            System.out.println("Главный поток прерван");
        }
        System.out.println("Завершение главного потока");
    }
}
```

### Расширение класса Thread

```java
class NewThread extends Thread { //-> Создание второго потока
    NewThread() {
        super("Demo Thread");
        // -> Конструктор расширяющего класса вызывает конструктор класса Thread
        // и передает имя ноаого потока
        System.out.println("Дочерний поток: " + this);
    }

    public void run() { //-> Точка входа для второго потока
        try {
            for (int i = 5; i > 0; i--) {
                System.out.println("Дочерний поток: " + i);
                Thread.sleep(500);
            }
        } catch (InterruptedException exception) {
            System.out.println("Дочерний поток прерван");
        }
        System.out.println("Завершение дочернего потока");
    }
}

public class Code {
    public static void main(String[] args) throws InterruptedException {
        NewThread nt = new NewThread(); // Создание нового потока
        nt.start();                   // Запуск нового потока
        try {
            for (int i = 5; i > 0; i--) {
                System.out.println("Главный поток: " + i);
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            System.out.println("Главный поток прерван");
        }
        System.out.println("Завершение главного потока");
    }
}
```

## <a id="title7">СОЗДАНИЕ МНОЖЕСТВА ПОТОКОВ</a>

[Наверх](#titlelist)

```java
package dev.folomkin.core.multithrading;

class NewThread implements Runnable {
    String name; // Имя потока
    Thread thread;

    NewThread(String threadName) {
        name = threadName;
        t = new Thread(this, name);
        System.out.println("Новый поток: " + t);
    }

    public void run() { // -> Точка входа для потока
        try {
            for (int i = 5; i > 0; i--) {
                System.out.println(name + " : " + i);
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            System.out.println(name + " прерван");
        }
        System.out.println(name + " завершен");
    }
}

public class Code {
    public static void main(String[] args) {
        NewThread nt1 = new NewThread("One");
        NewThread nt2 = new NewThread("Two");
        NewThread nt3 = new NewThread("Three");

        nt1.t.start();
        nt2.t.start();
        nt3.t.start();

        // Ожидаем окончания остальных потоков
        try {
            Thread.sleep(10000);
            // -> Засыпание на 10с гарантирует, 
            // что главный поток завершится последним 
        } catch (Exception e) {
            System.out.println("Главный поток прерван");
        }
        System.out.println("Main thread exiting");
    }
}

```

## <a id="title8">ИСПОЛЬЗОВАНИЕ МЕТОДОВ isAlive() И join()</a>

[Наверх](#titlelist)

Способы определения завершен ли поток:

- вызвать на потоке метод **final boolean isAlive()** из Thread. Метод
  возвращает
  true, если поток на котором он был вызван, еще запущен.
- **final void join() throws InterruptedException** - Этот метод ожидает
  завершения потока, на котором он вызывается. Его имя происходит от
  концепции вызывающего потока, ожидающего до тех пор, пока указанный поток не
  присоединится к нему. Дополнительные формы метода join() позволяют указывать
  максимальное время ожидания завершения указанного потока.

```java
class NewThread implements Runnable {
    String name; // Имя потока
    Thread t;

    NewThread(String threadName) {
        name = threadName;
        t = new Thread(this, name);
        System.out.println("Новый поток: " + t);
    }

    public void run() { // -> Точка входа для потока
        try {
            for (int i = 5; i > 0; i--) {
                System.out.println(name + " : " + i);
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            System.out.println(name + " прерван");
        }
        System.out.println(name + " завершен");
    }
}

public class Code {
    public static void main(String[] args) {
        NewThread nt1 = new NewThread("One");
        NewThread nt2 = new NewThread("Two");
        NewThread nt3 = new NewThread("Three");

        // Запустить потоки
        nt1.t.start();
        nt2.t.start();
        nt3.t.start();

        System.out.println("Поток One : " +
                (nt1.t.isAlive() ? "работает" : "не работает "));
        System.out.println("Поток Two : " +
                (nt1.t.isAlive() ? "работает" : "не работает "));
        System.out.println("Поток Three : " +
                (nt1.t.isAlive() ? "работает" : "не работает "));

        // Ожидаем окончания остальных потоков
        try {
            System.out.println("Ожидаем завершения потоков");
            nt1.t.join();
            nt2.t.join();
            nt3.t.join();
        } catch (Exception e) {
            System.out.println("Главный поток прерван");
        }

        System.out.println("Поток One : " +
                (nt1.t.isAlive() ? "работает" : "не работает "));
        System.out.println("Поток Two : " +
                (nt1.t.isAlive() ? "работает" : "не работает "));
        System.out.println("Поток Three : " +
                (nt1.t.isAlive() ? "работает" : "не работает "));

        System.out.println("Main thread exiting");
    }
}

```

## <a id="title9">ПРИОРИТЕТЫ ПОТОКОВ</a>

[Наверх](#titlelist)

Приоритеты потоков используются планировщиком потоков для принятия решения о
том, когда следует разрешить выполнение каждого потока. Теоретически в течение
заданного периода времени потоки с более высоким приоритетом получают больше
времени ЦП, чем потоки с более низким приоритетом. На практике количество
времени ЦП, которое получает поток, часто зависит от нескольких факторов помимо
ero приоритета. (Например, способ реализации многозадачности в ОС может повлиять
на относительную доступность времени ЦП.) Поток с более высоким приоритетом
может также вытеснять поток с более низким приоритетом. Скажем, когда
выполняется поток с более низким приоритетом и возобновляется выполнение потока
с более высоким приоритетом (например, происходит его выход из режима сна или
ожидания ввода-вывода), он вытесняет поток с более низким приоритетом.

Для установки приоритета потока применяется метод setPriority(), который
является членом класса Thread: `final void setPriority(int level)`

## <a id="title10">СИНХРОНИЗАЦИЯ</a>

[Наверх](#titlelist)

Доступ к ресурсу только одного потока в один момент времени.

Монитор - объект который обеспечивает блокировку используемого ресурса в
заданный
момент времени. В заданный момент времени владеть монитором может только
один поток. Когда поток получает блокировку, то говорят, что он _входит_ в
монитор. Все другие потоки, пытающиеся войти в заблокированный монитор,
будут приостановлены до тех пор, пока первый поток не выйдет из монитора.
Говорят, что эти другие потоки ожидают монитор. Поток, владеющий монитором,
может при желании повторно войти в тот же самый монитор.

Синхронизировать код можно одним из двух способов, предполагающих
использование ключевого слова synchronized.

### Использование синхронизированных методов

Синхронизацию в Java обеспечить легко, потому что все объекты имеют собственные
связанные с ними неявные мониторы. Чтобы войти в монитор объекта, понадобится
лишь вызвать метод, модифицированный с помощью ключевого слова synchronized.
Пока поток находится внутри синхронизированного метода, все другие потоки,
пытающиеся вызвать его (или любой другой синхронизированный метод) на том же
экземпляре, должны ожидать. Чтобы выйти из монитора и передать управление
объектом следующему ожидающему потоку, владелец монитора просто возвращает
управление из синхронизированного метода.

```java
class CallMe {
    void call(String msg) {
        System.out.print("[ ");
        System.out.print(msg);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            System.out.println("Прерван");
        }
        System.out.print(" ]");
    } // -> [ Hello[ Synchronized[ World ] ] ]
}

class Caller implements Runnable {
    String msg;
    CallMe target;
    Thread t;

    public Caller(CallMe targ, String s) {
        target = targ;
        msg = s;
        t = new Thread(this);
    }

    public void run() {
        target.call(msg);
    }
}

public class Code {
    public static void main(String[] args) {
        CallMe target = new CallMe();
        Caller ob1 = new Caller(target, "Hello");
        Caller ob2 = new Caller(target, "Synchronized");
        Caller ob3 = new Caller(target, "World");

        // Запускаем потоки
        ob1.t.start();
        ob2.t.start();
        ob3.t.start();

        // Ожидаем завершения потоков
        try {
            ob1.t.join();
            ob2.t.join();
            ob3.t.join();
        } catch (InterruptedException e) {
            System.out.println("Поток прерван");
        }
    }
}

```

Три потока соперничают за выполнение действия над объектом. Это состояние гонки.
Для исправления этой ситуации методу call() добавляется ключевое слово
**synchronized**. Ключевое слово synchronized н е позволяет остальным потокам
входить
в метод call(), пока он используется другим потоком. Тогда вывод будет:
`//-> [ Hello ][ World ][ Synchronized ]`

### Оператор synchronized

Чтобы синхронизировать доступ к объектам класса, не предназначенного для
многопоточного доступа, т.е. синхронизированные методы в классе не используются,
нужно поместить вызовы методов, определенных этим классом, внутрь блока
synchronized.

Общий вид:

    synchronized(objRef){
        // Операторы подлежащие синхронизации
    }
    где objRef - ссылка на синхронизируемый объект

Блок synchronized гарантирует, что вызов синхронизированного метода, который
является членом класса objRef, произойдет только после того, как текущий поток
успешно войдет в монитор objRef.

```java
class CallMe {
    void call(String msg) {
        // ...
    }
}

class Caller implements Runnable {
    String msg;
    CallMe target;
    Thread t;

    public Caller(CallMe targ, String s) {
        // ...
    }

    public void run() {
        synchronized (target) {
            target.call(msg);
        }
    }
}

public class Code {
    public static void main(String[] args) {
        // ...
    }
}

```

## <a id="title11">ВЗАИМОДЕЙСТВИЕ МЕЖДУ ПОТОКАМИ</a>

[Наверх](#titlelist)

В Java имеется элегантный механизм взаимодействия между процессами
с помощью методов wait(), notify() и notifyAll(). Указанные методы реализованы
как финальные в классе Object, поэтому они есть во всех классах. Все три метода
можно вызывать только из синхронизированного контекста.

- final void wait() throws InterruptedException - сообщает вызывающему потоку о
  необходимости уступить монитор и перейти в спящий режим, пока какой-то другой
  поток не войдет в тот же монитор и не вызовет notify() или notifyAll();
- final void notify() - пробуждает поток, который вызвал wait() на том же самом
  объекте.
- final void notifyAll() - пробуждает все потоки, которые вызвали wait() на том
  же самом объекте. Одно му из этих потоков будет предоставлен доступ.