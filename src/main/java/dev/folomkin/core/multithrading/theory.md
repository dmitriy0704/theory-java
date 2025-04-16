# МНОГОПОТОЧНОСТЬ

[//]: # (Список заголовков для оглавления: 
        #titile0: Класс Thread и интерфейс Runnable
        #titile1: Жизненный цикл потока
        #titile2: Приоритеты потоков
        #titile3: Синхронизация
        #titile4:
)

**_<a id="titlelist">ОГЛАВЛЕНИЕ:</a>_**

1. [Жизненный цикл потока](#title0)
2. [Приоритеты потоков](#title1)
3. [Синхронизация](#title2)
4. [Обмен сообщениями](#title3)
5. [Класс Thread и интерфейс Runnable](#title4)
5. [Главный поток](#title5)


## Создание потока

### Расширение класса Thread:

```java
package dev.folomkin.core.multithrading.code;

public class Code {
    public static void main(String[] args) {
        WalkThread t = new WalkThread();
        t.start();
    }
}

class WalkThread extends Thread {
    public void run() {
        try {
            for (int i = 0; i < 10; i++) {
                System.out.println("Walking " + i);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } finally {
            System.out.println("Finished: " + Thread.currentThread().getName());
        }

    }
}
```

```java
public class Code {
  public static void main(String[] args) {
    Thread t = new Thread(new TalkThread());
    t.start();
  }
}

class TalkThread implements Runnable {
    @Override
    public void run() {
        try {
            for (int i = 0; i < 10; i++) {
                System.out.println("Talking " + i);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } finally {
            System.out.println("Talking " + Thread.currentThread().getName());
        }
    }
```


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
- thread.join();
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
    // Текущий поток : Thread [ main , 5 , ma i п ]
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
        NewThread nt = new NewThread(); //-> Создание нового потока
        nt.t.start();                   //-> Запуск нового потока
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
Передача this в качестве первого аргумента указывает на то, что новый
поток должен вызвать метод run() для данного объекта. Внутри main() вызывается
start(), который запускает поток выполнения, начиная с метода
run(). Это приводит к тому, что цикл for дочернего потока начинает свою
работу. Затем главный поток входит в цикл for. Оба потока продолжают работать,
совместно используя ЦП в одноядерных системах, пока их цикл не
завершится. 

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
  возвращает true, если поток на котором он был вызван, еще запущен.
- **final void join() throws InterruptedException** - Этот метод ожидает
  завершения потока, на котором он вызывается. Его имя происходит от
  концепции вызывающего потока, ожидающего до тех пор, пока указанный поток не
  присоединится к нему. Дополнительные формы метода join() позволяют указывать
  максимальное время ожидания завершения указанного потока.   
  В потоке1 вызывается поток2.join() и это переводит поток1 в состояние
  ожидания.  
  Метод join() в контексте многопоточного программирования на Java позволяет
  приостановить выполнение текущего потока до тех пор, пока поток, к которому
  применён данный метод, не закончит свою работу. Текущий поток продолжит своё
  выполнение только после того, как указанный поток будет завершён.

  Thread t1 = new Thread(() -> {/* Здесь может быть ваш код */});
  t1.start();
  t1.join(); // Ожидаем завершения потока t1
  // Основные действия начнут выполняться после завершения потока t1!

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
заданный момент времени. В заданный момент времени владеть монитором может
только один поток. Когда поток получает блокировку, то говорят, что он _входит_
в монитор. Все другие потоки, пытающиеся войти в заблокированный монитор,
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
входить в метод call(), пока он используется другим потоком. Тогда вывод будет:
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

```java
// Некорректная реализация производителя и потребителя
class Q {
    int n;

    synchronized int get() {
        System.out.println("Получено: " + n);
        return n;
    }

    synchronized void put(int n) {
        this.n = n;
        System.out.println("Отправлено: " + n);
    }
}

class Producer implements Runnable {
    Q q;
    Thread t;

    public Producer(Q q) {
        this.q = q;
        t = new Thread(this, "Производитель");
    }

    public void run() {
        int i = 0;
        while (true) {
            q.put(++i);
        }
    }
}

class Consumer implements Runnable {
    Q q;
    Thread t;

    public Consumer(Q q) {
        this.q = q;
        t = new Thread(this, "Потребитель");
    }

    public void run() {
        while (true) {
            q.get();
        }
    }
}

public class Code {
    public static void main(String[] args) {
        Q q = new Q();
        Producer p = new Producer(q);
        Consumer c = new Consumer(q);

        // Запускаем потоки
        p.t.start();
        c.t.start();
        System.out.println("Нажми <CTRL+C> чтобы остановить программу");
    }

    //-> Отправлено: ...
    // Получено: ...
    // Получено: ... 
    // Получено: ... 
    // Получено: ... 
}
```

```java
// Корректная реализация производителя и потребителя
class Q {
    int n;
    boolean valueSet = false;

    synchronized int get() {
        while (!valueSet) {
            try {
                wait();
            } catch (InterruptedException e) {
                System.out.println("Перехвачено исключение InterruptedException");
                e.printStackTrace();
            }
        }

        System.out.println("Получено: " + n);
        valueSet = false;
        notify();
        return n;
    }

    synchronized void put(int n) {
        while (valueSet) {
            try {
                wait();
            } catch (InterruptedException e) {
                System.out.println("Перехвачено исключение InterruptedException");
            }
        }
        this.n = n;
        valueSet = true;
        System.out.println("Отправлено: " + n);
        notify();
    }
}

class Producer implements Runnable {
    Q q;
    Thread t;

    public Producer(Q q) {
        this.q = q;
        t = new Thread(this, "Производитель");
    }

    public void run() {
        int i = 0;
        while (true) {
            q.put(++i);
        }
    }
}

class Consumer implements Runnable {
    Q q;
    Thread t;

    public Consumer(Q q) {
        this.q = q;
        t = new Thread(this, "Потребитель");
    }

    public void run() {
        while (true) {
            q.get();
        }
    }
}

public class Code {
    public static void main(String[] args) {
        Q q = new Q();
        Producer p = new Producer(q);
        Consumer c = new Consumer(q);

        // Запускаем потоки
        p.t.start();
        c.t.start();
        System.out.println("Нажми <CTRL+C> чтобы остановить программу");
    }
}
//-> Отправлено: ...
// Получено: ...
// Отправлено: ...
// Получено: ... 
// Отправлено: ...
// Получено: ... 
// Отправлено: ...
// Получено: ... 

```

### Взаимоблокировка

Взаимоблокировка возникает в ситуации, которая возникает, когда два потока имеют
циклическую зависимость от пары синхронизированных объектов.

Например, пусть один поток входит в монитор объекта Х, а другой поток - в
монитор объекта У. Если поток в Х попытается вызвать какой-то синхронизированный
метод на объекте У, то он вполне ожидаемо заблокируется. Тем не менее, если
поток в У в свою очередь попытается вызвать какой-либо синхронизированный метод
на объекте Х, то он будет ожидать вечно, т.к. для доступа к Х ему пришлось бы
освободить собственную блокировку на У, чтобы первый поток мог завершиться.

Взаимоблокировка - трудная для отладки ошибка по двум причинам.

- В целом взаимоблокировка случается очень редко, если два потока правильно
  распределяют время.
- Она способна вовлечь более двух потоков и двух синхронизированных объектов.   
  (То есть взаимоблокировка может возникнуть из-за более запутанной
  последовательности событий, чем только что описанная.)

## <a id="title12">ПРИОСТАНОВКА, ВОЗОБНОВЛЕНИЕ И ОСТАНОВ ПОТОКОВ</a>

[Наверх](#titlelist)

Временами полезно приостанавл и вать выпол нение потока. Например,
отдельный поток можно применять для отображени я времени суток. Есл и
пользовател ю н е нужны часы, тогда его поток можно приостанов ить. В л ю­
бом случае приостанов ить поток довол ьно просто. После приостанов ки пере­
запустит ь поток тоже легко.

Механиз мы приостановки, останова и воз обновления потоков различают­
ся между ранними версиями Java, такими как Java 1.0, и более современными
версиями, начиная с Java 2. До выхода Java 2 в программе использ овал ись
методы suspend () , resume () и stop ( ) , которые определены в классе Thread
и предназ начены для приостановки, возобновления и останова выполнения
потока. Хотя упомянутые методы кажутся разумным и удобным подходом
к управлению выполнением потоков, они не должны применяться в новых
программах на Java и вот по чему. Несколько лет наз ад в версии Java 2 метод
suspend () класса Th read был объявлен нерекомендуемым. Так поступили
из-за того, что suspend () мог иногда становиться причиной серьез ных си­
стемных отказов. Предположим, что поток з аблокировал крити чески важные
структ уры данных. Есл и этот поток приостановить в данной то чке, то бло­
кировки не освободятся. Другие потоки, ожидающие такие ресурсы, могут
попасть во вз аимоблокировку.

Метод resume () тоже не рекомендуется использовать. Проблем он не вы­
зывает, но е го нельзя применять без дополняющего метода suspend () .
Метод stop () класса Th read тоже объявлен нерекомендуемым в Java 2.
При чина в том, что и ногда он мог приводить к серьез ным системным отка­
зам. Например, пусть поток выполняет з апись в крити чески важную структу­
ру данных и з авершил внесение только части нужных изменений. Если такой
поток будет остановлен в этот момент, тогда структ ура данных может остать­
ся в поврежденном состоянии. Дело в том, что метод stop () выз ывает ос­
вобождение любой блокировки, удерживаемой выз ывающим потоком. Таким
образом, поврежденные данные могут быть использованы другим потоком,
ожидающим той же блокировки.

Поскольку применять методы suspend ( ) , resume () или stop () для управ­
ления потоком теперь нельзя, вам может показ аться, что больше не существу­
ет какого-либо способа для приостановки, перез апуска или завершения рабо­
ты потока. К с частью, это не так. Вз амен поток должен быть спроектирован
таким образом, чтобы метод run () периоди чески проверял, должен ли поток
приостанавливать, возобновлять или полностью останавливать собственное
выполнение. Как правило, з адача решается путем установления флаговой п е­
ременной, которая указ ывает состояние выполнения потока. Пока флаговая
переменная установлена в состояние "работает'; метод run () должен продол­
жать работу, чтобы поз волить потоку выполняться. Если флаговая перемен­
ная установлена в состояние "приостановить'; то поток должен быть приоста­
новлен. Есл и она установлена в состояние "остановить'; тогда поток должен
завершить работ у. Конечно, существует множество способов написания тако­
го кода, но главный принцип будет одинаковым во всех программах.

```java
// Приостановка и возобновление современным способом .
class NewThread implements Runnable {
    String name; // имя потока
    Thread t;
    boolean suspendFlag;

    NewThread(String threadname) {
        name = threadname;
        t = new Thread(this, name);
        System.out.println("Hoвый поток: " + t);
        suspendFlag = false;
    }

    // Это точка входа для потока.
    public void run() {
        try {
            for (int i = 15; i > 0; i--) {
                System.out.println(name + " · " + i);
                Thread.sleep(200);
                synchronized (this) {
                    while (suspendFlag) {
                        wait();
                    }
                }
            }
        } catch (InterruptedException е) {
            System.out.println(name + " прерван.");
        }
        System.out.println(name + " завершается. ");
    }

    synchronized void mysuspend() {
        suspendFlag = true;
    }

    synchronized void myresume() {
        suspendFlag = false;
        notify();
    }
}

public class Code {
    public static void main(String[] args) {
        NewThread ob1 = new NewThread(" One ");
        NewThread ob2 = new NewThread(" Two");
        ob1.t.start(); //запускаем поток
        ob2.t.start(); // запускаем поток
        try {
            Thread.sleep(1000);
            ob1.mysuspend();
            System.out.println(" Пpиocтaнoвкa потока One ");
            Thread.sleep(1000);
            ob1.myresume();
            System.out.println(" Bозобновление потока One ");
            ob2.mysuspend();
            System.out.println(" Пpиocтaнoвкa пот ока Two ");
            Thread.sleep(1000);
            ob2.myresume();
            System.out.println(" Boзoбнoвлeниe потока Two ");
        } catch (InterruptedException е) {
            System.out.println(" Главный поток прерван ");
        }
//Ожидать завершения потоков.

        try {
            System.out.println(" Oжидaниe завершения потоков . ");
            ob1.t.join();
            ob2.t.join();

        } catch (InterruptedException е) {
            System.out.println("Главный поток прерван");
            System.out.println("Глaвный поток завершается . ");
        }
    }
}

```

## <a id="title13">ПОЛУЧЕНИЕ СОСТОЯНИЯ ПОТОКА</a>

[Наверх](#titlelist)

Получить состояние потока можно методом: `Thread.State.getState()`.

Значения возвращающие методом getState():

- BLOCKED - поток приостановил работу, потому что ожидает получения блокировки
- NEW - поток еще не начал выполнение
- RUNNABLE - поток выполняется или ждет время ЦП
- TERMINATED - поток завершил выполнение
- TIMED_WAITING - Поток приостановил выполнение на указанный период
  времени, например, при вызове sleep(). Поток переходит в это состояние также
  в случае вызова версии wait() или join(), принимающей значение тайм-аута
- WAITING - Поток приостановил выполнение из-за того, что ожидает
  возникновения некоторого действия. Например, он находится в состоянии WAITING
  по причине вызова версии wait() или join(), не принимающей значение тайм-аута

## <a id="title14">ИСПОЛЬЗОВАНИЕ ФАБРИЧНЫХ МЕТОДОВ ДЛЯ СОЗДАНИЯ И ЗАПУСКА ПОТОКА</a>

[Наверх](#titlelist)

В ряде случаев нет необходимости отделять создание потока от запуска его
на выполнение. Другими словами, иногда удобно создать и запустить поток
одновременно. Один из способов это сделать предусматривает применение
статического фабричного метода. Фабричный метод возвращает объект класса.
Как правило, фабричные методы являются статическими методами класса.

Они используются по разным причинам, например, для установки объекта в
начальное состояние перед работой с ним, для настройки определенного типа
объекта или временами для обеспечения многократного использования объекта.
Что касается создания и запуска потока, то фабричный метод создаст поток,
вызовет метод start() на потоке и возвратит ссылку на поток. При таком
подходе вы можете создавать и запускать поток с помощью одного вызова
метода, тем самым упрощая свой код. Скажем, добавление в класс NewThread
из приведенной в начале главы программы ThreadDemo показанного дал ее
фабричного метода позволит создавать и запускать поток за один шаг:

    // Фабричный метод, который создает и запускает поток
    public static NewThread createAndStart(){
      NewThread myThrd = new NewThread();
      myThread.t.start();
      return.myThread;
    }

С применением метода createAndStart() следующий фрагмент кода:

    NewThread nt = new NewThread ( );  //создать новый поток
    nt.t.start();  //запустить поток

можно заменить так:

    NewThread nt = NewThread.createAndStart();

```java
package dev.folomkin.core.multithrading;

// Создание потока
class NewThread implements Runnable {
  Thread t;

  NewThread() {
    // Создание потока
    t = new Thread(this, "Demo thread");
    System.out.println("Дочерний поток: " + t);
  }

  public static dev.folomkin.core.multithrading.code.NewThread createNewThread() {
    dev.folomkin.core.multithrading.code.NewThread thread = new dev.folomkin.core.multithrading.code.NewThread();
    thread.t.start();
    return thread;
  }

  // Это точка входа для потока.
  public void run() {
    try {
      for (int i = 15; i > 0; i--) {
        System.out.println("Дочерний поток" + " · " + i);
        Thread.sleep(500);
      }
    } catch (InterruptedException е) {
      System.out.println("Дочерний поток" + " прерван.");
    }
    System.out.println("Дочерний поток" + " завершается. ");
  }
}

public class Code {
  public static void main(String[] args) {
    dev.folomkin.core.multithrading.code.NewThread nt = dev.folomkin.core.multithrading.code.NewThread.createNewThread();

    // Или new NewThread().t.start();

    try {
      for (int i = 5; i > 0; i--) {
        System.out.println("Главный поток: " + i);
        Thread.sleep(1000);
      }
    } catch (InterruptedException e) {
      System.out.println("Главный поток прерван");
      e.printStackTrace();
    }
  }
}
```

ExecutorService

> 9. ExecutorService

`ExecutorService` в Java — это интерфейс, который является частью пакета
`java.util.concurrent` и предоставляет высокоуровневый механизм для управления
потоками. Он позволяет выполнять асинхронные задачи, управлять пулом потоков и
упрощает работу с многопоточностью.

### Основные характеристики `ExecutorService`:

1. **Управление потоками**: `ExecutorService` управляет пулом потоков, что
   позволяет повторно использовать потоки для выполнения задач, тем самым
   уменьшая накладные расходы на создание и уничтожение потоков.

2. **Асинхронное выполнение**: Вы можете отправлять задачи на выполнение и
   продолжать выполнять другие операции, не дожидаясь завершения этих задач.

3. **Разные типы задач**: Вы можете отправлять как `Runnable`, так и `Callable`
   задачи. `Callable` позволяет возвращать результат и обрабатывать исключения.

4. **Управление жизненным циклом**: `ExecutorService` предоставляет методы для
   управления жизненным циклом пула потоков, такие как `shutdown()` и
   `shutdownNow()`, которые позволяют корректно завершить выполнение задач.

5. **Фабрики для создания экземпляров**: Для создания экземпляров
   `ExecutorService` обычно используются статические методы класса `Executors`,
   такие как:
  - `Executors.newFixedThreadPool(int nThreads)`: создает пул фиксированного
    размера.
  - `Executors.newCachedThreadPool()`: создает пул, который создает новые
    потоки по мере необходимости, но повторно использует ранее созданные
    потоки.
  - `Executors.newSingleThreadExecutor()`: создает пул с одним потоком.

### Пример использования:

Вот простой пример использования `ExecutorService`:

```java
public class ExecutorServiceExample {
    public static void main(String[] args) {
        // Создаем ExecutorService с фиксированным количеством потоков
        ExecutorService executor = Executors.newFixedThreadPool(3);

        // Отправляем задачи на выполнение
        for (int i = 0; i < 5; i++) {
            final int taskId = i;
            executor.submit(() -> {
                System.out.println("Task " + taskId + " is running in "
                        + Thread.currentThread().getName());
                try {
                    Thread.sleep(1000); // Имитация работы
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }

        // Завершаем ExecutorService
        executor.shutdown();
    }
}
```

В этом примере создается пул из трех потоков, и в него отправляются пять задач.
Каждая задача выполняется в одном из доступных потоков пула.

**Заключение**

___

## Что такое задачи?

В контексте `ExecutorService` и многопоточности в Java, под "задачей"
подразумевается единица работы, которую необходимо выполнить. Задачи могут быть
представлены в виде объектов, реализующих интерфейсы `Runnable` или `Callable`.

### Основные характеристики задач:

1. **Единица работы**: Задача — это конкретная работа или операция, которую
   нужно выполнить. Это может быть что угодно: от простого вычисления до сложной
   обработки данных.

2. **Асинхронность**: Задачи могут выполняться асинхронно, что позволяет
   основному потоку продолжать выполнение других операций, не дожидаясь
   завершения задачи.

3. **Возврат результата**:
  - **Runnable**: Задачи, реализующие интерфейс `Runnable`, не возвращают
    результат. Метод `run()` этого интерфейса имеет тип `void`.
  - **Callable**: Задачи, реализующие интерфейс `Callable`, могут возвращать
    результат и могут выбрасывать проверяемые исключения. Метод `call()` этого
    интерфейса возвращает значение.

4. **Управление состоянием**: Каждая задача может находиться в одном из
   нескольких состояний (например, ожидает выполнения, выполняется или
   завершена). Это состояние можно отслеживать с помощью объекта типа `Future`,
   который возвращается при отправке задачи на выполнение.

### Примеры задач

Вот несколько примеров задач:

- **Вычислительные задачи**: Например, задача по вычислению факториала числа или
  сложению чисел в массиве.
- **Задачи ввода-вывода**: Чтение данных из файла или отправка HTTP-запроса.
- **Обработка данных**: Например, фильтрация списка объектов или преобразование
  данных.

### Пример использования задач

Вот пример создания и выполнения задач с использованием интерфейсов `Runnable` и
`Callable`:

```java
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class TaskExample {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(2);

        // Задача Runnable
        Runnable runnableTask = () -> {
            System.out.println("Выполнение задачи Runnable");
        };

        // Задача Callable
        Callable<Integer> callableTask = () -> {
            System.out.println("Выполнение задачи Callable");
            return 42; // Возвращаем результат
        };

        // Отправляем задачи на выполнение
        executor.submit(runnableTask); // Для Runnable
        Future<Integer> future = executor.submit(callableTask); // Для Callable

        try {
            // Получаем результат выполнения задачи Callable
            Integer result = future.get();
            System.out.println("Результат задачи Callable: " + result);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();
        }
    }
}
```

### Объяснение примера:

1. **Создание пула потоков**: Мы создаем пул из двух потоков с помощью метода
   `Executors.newFixedThreadPool(2)`.

2. **Определение задач**:
  - Мы определяем задачу типа `Runnable`, которая просто выводит сообщение.
  - Мы также определяем задачу типа `Callable`, которая выводит сообщение и
    возвращает значение 42.

3. **Отправка задач на выполнение**: Мы отправляем обе задачи на выполнение
   через метод `submit()`. Для задачи типа `Runnable` мы просто вызываем метод
   без ожидания результата, а для задачи типа `Callable` мы получаем объект типа
   `Future`.

4. **Получение результата**: Мы используем метод `get()` объекта `Future`, чтобы
   получить результат выполнения задачи типа `Callable`.

5. **Завершение работы пула**: В конце мы вызываем метод `shutdown()`, чтобы
   корректно завершить работу пула потоков.

### Заключение

Таким образом, под "задачей" в контексте многопоточности и использования
`ExecutorService` понимается конкретная работа или операция, которую необходимо
выполнить асинхронно. Задачи могут быть реализованы через интерфейсы `Runnable`
и `Callable`, что позволяет гибко управлять выполнением кода в многопоточной
среде.


___

### Асинхронные задачи

**Асинхронные задачи** — это задачи, которые выполняются независимо от основного
потока выполнения программы. Это означает, что основной поток может продолжать
свою работу, не дожидаясь завершения асинхронной задачи. Асинхронное выполнение
позволяет улучшить отзывчивость приложений, особенно в тех случаях, когда
требуется выполнение длительных операций, таких как сетевые запросы, операции
ввода-вывода или сложные вычисления.

### Основные характеристики асинхронных задач:

1. **Независимость**: Асинхронные задачи могут выполняться в фоновом режиме,
   позволяя основному потоку продолжать выполнение других операций. Это особенно
   полезно в пользовательских интерфейсах, где блокировка основного потока может
   привести к зависанию приложения.

2. **Обработка результатов**: После завершения асинхронной задачи можно
   обработать результаты выполнения. В Java для этого часто используются объекты
   `Future` и `Callable`, которые позволяют получить результат выполнения задачи
   или обработать исключения.

3. **Параллелизм**: Асинхронные задачи могут выполняться параллельно с другими
   задачами, что позволяет более эффективно использовать ресурсы системы и
   сокращать общее время выполнения.

4. **Управление временем ожидания**: Асинхронные задачи могут быть настроены на
   выполнение с тайм-аутами или отменой, что позволяет избежать зависания
   приложения при выполнении долгих операций.

### Примеры использования асинхронных задач:

1. **Сетевые запросы**: При выполнении HTTP-запросов к удаленному серверу можно
   использовать асинхронные задачи для того, чтобы не блокировать основной поток
   приложения во время ожидания ответа.

2. **Долгие вычисления**: Если приложение выполняет сложные вычисления (
   например, обработка больших объемов данных), эти операции можно вынести в
   асинхронные задачи, чтобы основной поток оставался отзывчивым.

3. **Работа с файлами**: Чтение и запись файлов может занять значительное время;
   использование асинхронных задач позволяет избежать блокировки
   пользовательского интерфейса во время этих операций.

### Пример в Java:

Вот пример использования асинхронной задачи с помощью `ExecutorService` и
`Callable`:

```java
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class AsyncTaskExample {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newSingleThreadExecutor();

        // Создаем асинхронную задачу
        Callable<Integer> task = () -> {
            Thread.sleep(2000); // Имитация долгой работы
            return 42; // Возвращаем результат
        };

        Future<Integer> future = executor.submit(task);

        // Основной поток продолжает выполнять другие операции
        System.out.println("Основной поток продолжает работу...");

        try {
            // Получаем результат выполнения асинхронной задачи
            Integer result = future.get(); // Это блокирует поток до получения результата
            System.out.println("Результат асинхронной задачи: " + result);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();
        }
    }
}
```

В этом примере создается асинхронная задача, которая имитирует длительную
работу (с помощью `Thread.sleep`). Основной поток продолжает выполнение и
выводит сообщение на экран. После этого он ожидает завершения асинхронной задачи
и получает результат.

### Заключение

Асинхронные задачи являются важным инструментом для разработки отзывчивых и
эффективных приложений. Они позволяют выполнять длительные операции без
блокировки основного потока выполнения и обеспечивают более плавный
пользовательский опыт.
___
`Callable` — это функциональный интерфейс в Java, который представляет собой
задачу, которая может быть выполнена асинхронно и возвращает результат. Он
является частью пакета `java.util.concurrent` и часто используется в сочетании с
`ExecutorService` для выполнения задач в фоновом режиме.

### Основные характеристики `Callable`:

1. **Возврат результата**: В отличие от интерфейса `Runnable`, который не
   возвращает результат (метод `run()` имеет тип `void`), метод `call()`
   интерфейса `Callable` возвращает значение. Это позволяет получать результат
   выполнения задачи.

2. **Обработка исключений**: Метод `call()` может выбрасывать проверяемые
   исключения (checked exceptions), что позволяет обрабатывать ошибки,
   возникающие во время выполнения задачи. В случае с `Runnable`, все исключения
   должны обрабатываться внутри метода `run()`.

3. **Использование с `Future`**: Когда задача, реализующая интерфейс `Callable`,
   отправляется на выполнение через `ExecutorService`, она возвращает объект
   типа `Future`. Этот объект позволяет проверять статус выполнения задачи и
   получать результат после ее завершения.

### Синтаксис

Интерфейс `Callable` имеет следующий синтаксис:

```java

@FunctionalInterface
public interface Callable<V> {
    V call() throws Exception;
}
```

Где `<V>` — это тип результата, который будет возвращен методом `call()`.

### Пример использования

Вот пример использования интерфейса `Callable` вместе с `ExecutorService`:

```java
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class CallableExample {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(2);

        // Создаем задачу, реализующую Callable
        Callable<Integer> task = () -> {
            // Имитация длительной работы
            Thread.sleep(2000);
            return 42; // Возвращаем результат
        };

        // Отправляем задачу на выполнение
        Future<Integer> future = executor.submit(task);

        try {
            // Получаем результат выполнения задачи
            Integer result = future.get(); // Это блокирует поток до получения результата
            System.out.println("Результат асинхронной задачи: " + result);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();
        }
    }
}
```

### Объяснение примера:

1. **Создание пула потоков**: Мы создаем пул из двух потоков с помощью метода
   `Executors.newFixedThreadPool(2)`.

2. **Определение задачи**: Мы определяем задачу, реализующую интерфейс
   `Callable`, которая имитирует длительную работу (с помощью метода
   `Thread.sleep`) и возвращает значение 42.

3. **Отправка задачи на выполнение**: Задача отправляется на выполнение через
   метод `submit()`, который возвращает объект типа `Future`.

4. **Получение результата**: Мы используем метод `get()` объекта `Future`, чтобы
   получить результат выполнения задачи. Этот метод блокирует текущий поток до
   тех пор, пока задача не завершится.

5. **Обработка исключений**: Мы обрабатываем возможные исключения, которые могут
   возникнуть при выполнении задачи или при получении результата.

6. **Завершение работы пула**: В конце мы вызываем метод `shutdown()`, чтобы
   корректно завершить работу пула потоков.

### Заключение

Интерфейс `Callable` является мощным инструментом для создания асинхронных задач
в Java, позволяя разработчикам выполнять фоновые операции и получать результаты
их выполнения. Он обеспечивает более гибкий подход по сравнению с интерфейсом
`Runnable`, особенно когда требуется обработка результатов или исключений.
___

## Задачи и потоки

Задачи и потоки в контексте многопоточности в Java тесно связаны между собой,
поскольку задачи представляют собой единицы работы, которые выполняются в
потоках. Давайте рассмотрим, как именно они связаны:

### 1. **Определение потоков и задач**

- **Поток**: Поток — это отдельная последовательность выполнения в программе.
  Каждый поток имеет свой собственный стек вызовов и может выполняться
  параллельно с другими потоками. В Java потоки создаются с помощью класса
  `Thread` или через интерфейсы, такие как `Runnable` и `Callable`.

- **Задача**: Задача — это конкретная работа или операция, которую нужно
  выполнить. В Java задачи могут быть реализованы через интерфейсы `Runnable` (
  для задач без результата) и `Callable` (для задач с результатом).

### 2. **Выполнение задач в потоках**

Когда вы отправляете задачу на выполнение в `ExecutorService`, она будет
выполнена в одном из потоков пула потоков. Вот как это работает:

- **Создание пула потоков**: При создании экземпляра `ExecutorService`, вы
  определяете пул потоков, который будет использоваться для выполнения задач.
  Например, с помощью метода `Executors.newFixedThreadPool(n)` создается пул из
  `n` потоков.

- **Отправка задач**: Когда вы отправляете задачу на выполнение (например, через
  метод `submit()`), `ExecutorService` выбирает один из доступных потоков из
  пула для выполнения этой задачи.

- **Асинхронное выполнение**: Задачи могут выполняться асинхронно, что означает,
  что основной поток программы может продолжать выполнение других операций, не
  дожидаясь завершения задачи.

### 3. **Параллелизм и многопоточность**

Использование задач и потоков позволяет реализовать параллелизм:

- **Параллельное выполнение**: Если у вас есть несколько задач, которые могут
  выполняться одновременно (например, обработка данных или выполнение сетевых
  запросов), вы можете отправить их на выполнение в пул потоков. Пул будет
  распределять задачи между доступными потоками, что позволяет выполнять их
  параллельно.

- **Управление ресурсами**: Пулы потоков помогают управлять ресурсами более
  эффективно. Вместо создания нового потока для каждой задачи (что может быть
  затратным по времени и ресурсам), вы можете повторно использовать существующие
  потоки для выполнения новых задач.

### 4. **Пример связи задач и потоков**

Вот пример кода, который демонстрирует связь между задачами и потоками:

```java
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TaskAndThreadExample {
    public static void main(String[] args) {
        // Создаем пул из 3 потоков
        ExecutorService executor = Executors.newFixedThreadPool(3);

        // Определяем несколько задач
        Runnable task1 = () -> {
            System.out.println("Задача 1 выполняется в потоке: " + Thread.currentThread().getName());
        };

        Runnable task2 = () -> {
            System.out.println("Задача 2 выполняется в потоке: " + Thread.currentThread().getName());
        };

        Runnable task3 = () -> {
            System.out.println("Задача 3 выполняется в потоке: " + Thread.currentThread().getName());
        };

        // Отправляем задачи на выполнение
        executor.submit(task1);
        executor.submit(task2);
        executor.submit(task3);

        // Завершаем работу пула
        executor.shutdown();
    }
}
```

### Объяснение примера:

1. **Создание пула**: Мы создаем пул из трех потоков.

2. **Определение задач**: Мы определяем три задачи типа `Runnable`, каждая из
   которых выводит имя потока, в котором она выполняется.

3. **Отправка задач**: Мы отправляем все три задачи на выполнение через метод
   `submit()`. Пул распределяет эти задачи между доступными потоками.

4. **Вывод информации о потоке**: Каждая задача выводит имя потока, который ее
   выполняет. Это демонстрирует, что одна и та же задача может выполняться в
   разных потоках.

5. **Завершение работы пула**: В конце мы вызываем метод `shutdown()`, чтобы
   корректно завершить работу пула.

### Заключение

Таким образом, задачи и потоки являются основными компонентами многопоточного
программирования в Java. Задачи представляют собой единицы работы, которые
необходимо выполнить, а потоки — это механизмы выполнения этих задач.
Использование пуль потоков позволяет эффективно управлять выполнением множества
задач одновременно, обеспечивая параллелизм и оптимизацию использования ресурсов
системы.
___

### Future

Объект `Future`, возвращаемый интерфейсом `Callable` в Java, представляет собой
результат асинхронной операции. Он предоставляет методы для проверки состояния
выполнения задачи, получения результата и обработки возможных исключений,
которые могут возникнуть во время выполнения задачи. Давайте рассмотрим основные
аспекты объекта `Future` более подробно.

### Основные характеристики объекта `Future`

1. **Асинхронный результат**: Объект `Future` позволяет вам получить результат
   выполнения задачи, которая была отправлена на выполнение в пул потоков или
   другой механизм многопоточности. Поскольку задача выполняется асинхронно, вы
   можете продолжать выполнение других операций в основном потоке, не дожидаясь
   завершения задачи.

2. **Методы для управления состоянием**:
  - `isDone()`: Возвращает `true`, если задача завершена (независимо от того,
    была ли она выполнена успешно или завершилась с ошибкой).
  - `isCancelled()`: Возвращает `true`, если задача была отменена до ее
    завершения.
  - `cancel(boolean mayInterruptIfRunning)`: Пытается отменить выполнение
    задачи. Если задача уже выполняется и параметр `mayInterruptIfRunning`
    равен `true`, то выполнение задачи может быть прервано.

3. **Получение результата**:
  - `get()`: Блокирует текущий поток до тех пор, пока задача не завершится, и
    возвращает результат выполнения задачи. Если задача завершилась с
    исключением, метод `get()` выбросит это исключение.
  - `get(long timeout, TimeUnit unit)`: Блокирует текущий поток до тех пор,
    пока задача не завершится или не истечет указанный таймаут.

### Пример использования объекта Future

Вот пример кода, который демонстрирует использование интерфейса `Callable` и
объекта `Future`:

```java
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FutureExample {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(2);

        // Определяем задачу Callable
        Callable<Integer> task = () -> {
            // Имитация длительной операции
            Thread.sleep(2000);
            return 42; // Возвращаем результат
        };

        // Отправляем задачу на выполнение
        Future<Integer> future = executor.submit(task);

        try {
            // Получаем результат выполнения задачи
            Integer result = future.get(); // Это блокирует текущий поток до получения результата
            System.out.println("Результат: " + result);
        } catch (InterruptedException e) {
            System.err.println("Задача была прервана");
        } catch (ExecutionException e) {
            System.err.println("Ошибка при выполнении задачи: " + e.getCause());
        } finally {
            executor.shutdown();
        }
    }
}
```

### Объяснение примера:

1. **Создание пула потоков**: Мы создаем пул из двух потоков с помощью метода
   `Executors.newFixedThreadPool(2)`.

2. **Определение задачи**: Мы определяем задачу типа `Callable`, которая
   имитирует длительную операцию (например, задержку на 2 секунды) и возвращает
   значение 42.

3. **Отправка задачи**: Мы отправляем задачу на выполнение через метод
   `submit()`, который возвращает объект типа `Future`.

4. **Получение результата**: Мы вызываем метод `get()` у объекта `Future`, чтобы
   получить результат выполнения задачи. Этот вызов блокирует текущий поток до
   тех пор, пока задача не завершится.

5. **Обработка исключений**: Мы обрабатываем возможные исключения:
  - `InterruptedException`: Выбрасывается, если текущий поток был прерван во
    время ожидания результата.
  - `ExecutionException`: Выбрасывается, если задача завершилась с ошибкой; в
    этом случае мы можем получить причину ошибки через метод `getCause()`.

6. **Завершение работы пула**: В конце мы вызываем метод `shutdown()`, чтобы
   корректно завершить работу пула потоков.

### Заключение

Объект `Future` является важным компонентом многопоточного программирования в
Java, позволяя управлять асинхронными задачами и получать результаты их
выполнения. Он предоставляет удобные методы для проверки состояния задач и
обработки результатов или ошибок, что делает его полезным инструментом для
работы с параллельными вычислениями.
___
`ExecutorService` является мощным инструментом для работы с многопоточностью в
Java, позволяя разработчикам легко управлять выполнением асинхронных задач и
оптимизировать использование ресурсов системы.
 ___

ExecutorService – это интерфейс Java, который предоставляет удобный способ
управления потоками исполнения. Он позволяет создавать пул потоков исполнения и
выполнять задачи в этих потоках.

Существует несколько ключевых реализаций ExecutorService:

ThreadPoolExecutor
ScheduledThreadPoolExecutor
CachedThreadPoolExecutor
ForkJoinPool

### **ThreadPoolExecutor**

ThreadPoolExecutor – это реализация интерфейса ExecutorService в Java, который
предоставляет пул потоков исполнения для выполнения задач в фоновом режиме.

ThreadPoolExecutor позволяет создать и настроить пул потоков исполнения с
определенным количеством потоков, а также управлять очередью задач. Он может
быть использован для выполнения задач в фоновом режиме, что может улучшить
производительность и реактивность вашего приложения.

Давайте рассмотрим основные компоненты ThreadPoolExecutor и пример их
использования.

**Пул потоков исполнения**

ThreadPoolExecutor управляет пулом потоков исполнения, которые используются для
выполнения задач. При создании ThreadPoolExecutor вы указываете минимальное и
максимальное количество потоков, которые могут быть использованы для выполнения
задач, а также время жизни потока.

**Очередь задач**

ThreadPoolExecutor хранит очередь задач, которые ожидают выполнения в пуле
потоков исполнения. По умолчанию используется очередь с ограниченной ёмкостью,
но вы можете создать свою собственную реализацию очереди задач, если требуется.

**Обработчик задач**

ThreadPoolExecutor определяет способ обработки задач, которые не могут быть
выполнены из-за переполнения очереди или ошибки в выполнении задачи. По
умолчанию используется обработчик, который выбрасывает исключение, но вы можете
создать свой собственный обработчик задач.

**Жизненный цикл**

ThreadPoolExecutor имеет свой жизненный цикл, который может быть управляем через
методы start(), shutdown() и awaitTermination(). Вы можете использовать эти
методы для запуска и остановки ThreadPoolExecutor и ожидания завершения
выполнения всех задач.

**Пример использования**

Один из наиболее ярких примеров использования ThreadPoolExecutor – это
выполнение параллельной обработки изображений. Предположим, у вас есть большой
набор изображений, которые нужно обработать, например, изменить их размер,
добавить водяной знак и сохранить в другой формат.

```java


public class ImageProcessor {
    private final ThreadPoolExecutor executor;

    public ImageProcessor() {
        // Создаем ThreadPoolExecutor с фиксированным количеством потоков
        this.executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(4);
    }

    public void processImages(List<File> imageFiles) {
        // Добавляем задачи обработки каждого изображения в ThreadPoolExecutor
        for (File imageFile : imageFiles) {
            executor.execute(new ImageTask(imageFile));
        }

        // Останавливаем выполнение задач и закрываем ThreadPoolExecutor
        executor.shutdown();
    }

    private static class ImageTask implements Runnable {
        private final File imageFile;

        public ImageTask(File imageFile) {
            this.imageFile = imageFile;
        }

        @Override
        public void run() {
            // Код для обработки изображения
            System.out.println("Обработка изображения: " + imageFile.getName());
        }
    }

}
```

Пример класса-клиента:

```java
public class ImageProcessorDemo {
    public static void main(String[] args) {
// Создаем ImageProcessor
        ImageProcessor imageProcessor = new ImageProcessor();
// Создаем список файлов изображений, которые нужно обработать
        List<File> imageFiles = new ArrayList<>();
        imageFiles.add(new File("/path/to/image1.jpg"));
        imageFiles.add(new File("/path/to/image2.jpg"));
        imageFiles.add(new File("/path/to/image3.jpg"));
        imageFiles.add(new File("/path/to/image4.jpg"));
// Обрабатываем изображения
        imageProcessor.processImages(imageFiles);
    }
}


```

**ScheduledThreadPoolExecutor**

ScheduledThreadPoolExecutor – это реализация интерфейса ScheduledExecutorService
в Java, который предоставляет пул потоков исполнения для выполнения задач в
определенный момент времени или с определенной периодичностью.

ScheduledThreadPoolExecutor позволяет создать и настроить пул потоков исполнения
с определенным количеством потоков, а также управлять очередью задач. Он может
быть использован для выполнения регулярных задач в фоновом режиме, что может
улучшить производительность и реактивность вашего приложения.

Давайте рассмотрим основные компоненты ScheduledThreadPoolExecutor и пример их
использования.

**Пул потоков исполнения**

ScheduledThreadPoolExecutor управляет пулом потоков исполнения, которые
используются для выполнения задач. При создании ScheduledThreadPoolExecutor вы
указываете минимальное и максимальное количество потоков, которые могут быть
использованы для выполнения задач, а также время жизни потока.

**Очередь задач**

ScheduledThreadPoolExecutor хранит очередь задач, которые ожидают выполнения в
пуле потоков исполнения. По умолчанию используется очередь с ограниченной
ёмкостью, но вы можете создать свою собственную реализацию очереди задач, если
требуется.

**Обработчик задач**

ScheduledThreadPoolExecutor определяет способ обработки задач, которые не могут
быть выполнены из-за переполнения очереди или ошибки в выполнении задачи. По
умолчанию используется обработчик, который выбрасывает исключение, но вы можете
создать свой собственный обработчик задач.

**Жизненный цикл**

ScheduledThreadPoolExecutor имеет свой жизненный цикл, который может быть
управляем через методы start(), shutdown() и awaitTermination(). Вы можете
использовать эти методы для запуска и остановки ScheduledThreadPoolExecutor и
ожидания завершения выполнения всех задач.

**Регулярные задачи**

ScheduledThreadPoolExecutor предоставляет методы для выполнения регулярных задач
в определенный момент времени или с определенной периодичностью. Вы можете
использовать методы schedule() и scheduleAtFixedRate() для запуска задач в
определенное время и с определенной периодичностью соответственно.

**Пример использования**

Один из наиболее ярких примеров использования ScheduledThreadPoolExecutor – это
выполнение регулярных задач в определенный промежуток времени. Рассмотрим пример
резервного копирования файлов с определенной периодичностью.

Вот пример кода, который использует ScheduledThreadPoolExecutor для выполнения
регулярных задач резервного копирования файлов:

```java


public class BackupService {
    private final ScheduledExecutorService scheduler;

    public BackupService() {
        // Создаем ScheduledThreadPoolExecutor с 1 потоком
        this.scheduler = Executors.newScheduledThreadPool(1);
    }

    public void startBackup(String sourceDir, String destDir, long period, TimeUnit timeUnit) {
        // Создаем новую задачу BackupTask
        BackupTask task = new BackupTask(sourceDir, destDir);

        // Запускаем задачу каждый период времени с использованием ScheduledThreadPoolExecutor
        scheduler.scheduleAtFixedRate(task, 0, period, timeUnit);
    }

    public void stopBackup() {
        // Останавливаем выполнение задач и закрываем пул потоков исполнения
        scheduler.shutdown();
    }

    private static class BackupTask implements Runnable {
        private final String sourceDir;
        private final String destDir;

        public BackupTask(String sourceDir, String destDir) {
            this.sourceDir = sourceDir;
            this.destDir = destDir;
        }

        @Override
        public void run() {
            // Код для резервного копирования файлов
            System.out.println("Резервное копирование файлов...");
        }
    }

}

```

Пример класса-клиента:

```java



public class BackupServiceDemo {
    public static void main(String[] args) {
// Создаем BackupService
        BackupService backupService = new BackupService();

        // Запускаем резервное копирование каждые 5 секунд
        backupService.startBackup("/path/to/source", "/path/to/destination", 5, TimeUnit.SECONDS);

        // Ждем 20 секунд
        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Останавливаем выполнение резервного копирования
        backupService.stopBackup();
    }

}

```

**CachedThreadPoolExecutor**

CachedThreadPoolExecutor – это реализация интерфейса ExecutorService в Java,
который предоставляет пул потоков исполнения для выполнения задач в фоновом
режиме.

CachedThreadPoolExecutor автоматически масштабирует количество потоков
исполнения в зависимости от количества задач, которые нужно выполнить. Если
задачи поступают слишком быстро и текущее количество потоков исполнения не может
справиться с ними, CachedThreadPoolExecutor создает новый поток исполнения,
чтобы обеспечить выполнение задачи. Если задачи не поступают достаточно быстро и
текущее количество потоков исполнения избыточно, CachedThreadPoolExecutor
автоматически удаляет потоки исполнения, чтобы уменьшить нагрузку на систему.

Давайте рассмотрим основные компоненты CachedThreadPoolExecutor и пример их
использования.

**Пул потоков исполнения**

CachedThreadPoolExecutor управляет пулом потоков исполнения, которые
используются для выполнения задач. При создании CachedThreadPoolExecutor не
нужно указывать количество потоков, так как он автоматически масштабируется в
зависимости от количества задач.

**Очередь задач**

CachedThreadPoolExecutor не хранит очередь задач, которые ожидают выполнения в
пуле потоков исполнения. Вместо этого он немедленно создает новые потоки
исполнения для каждой задачи, если текущее количество потоков исполнения не
может справиться с нагрузкой.

**Обработчик задач**

CachedThreadPoolExecutor определяет способ обработки задач, которые не могут
быть выполнены из-за ошибки в выполнении задачи. По умолчанию используется
обработчик, который выбрасывает исключение, но вы можете создать свой
собственный обработчик задач.

**Жизненный цикл**

CachedThreadPoolExecutor имеет свой жизненный цикл, который может быть управляем
через методы start(), shutdown() и awaitTermination(). Вы можете использовать
эти методы для запуска и остановки CachedThreadPoolExecutor и ожидания
завершения выполнения всех задач.

**Пример использования**

Рассмотрим пример, в котором мы будем обрабатывать множество задач с помощью
CachedThreadPoolExecutor. Предположим, у нас есть список файлов, которые нужно
скопировать с одного места на другое.

```java



public class FileCopyManager {
    private final ExecutorService executor;

    public FileCopyManager() {
        this.executor = Executors.newCachedThreadPool();
    }

    public void copyFiles(List<File> sourceFiles, List<File> destFiles) {
        for (int i = 0; i < sourceFiles.size(); i++) {
            FileCopyTask task = new FileCopyTask(sourceFiles.get(i), destFiles.get(i));
            executor.execute(task);
        }
        executor.shutdown();
    }

    private static class FileCopyTask implements Runnable {
        private final File sourceFile;
        private final File destFile;

        public FileCopyTask(File sourceFile, File destFile) {
            this.sourceFile = sourceFile;
            this.destFile = destFile;
        }

        @Override
        public void run() {
            // Копирование файла
            System.out.println("Копирование файла " + sourceFile.getName() + " в " + destFile.getName());
        }
    }

}

```

Пример класса-клиента:

```java



public class FileCopyManagerDemo {
    public static void main(String[] args) {
        List<File> sourceFiles = Arrays.asList(new File("source/file1.txt"), new File("
                source / file2.txt"), new File(" source / file3.txt"));
                List < File > destFiles = Arrays.asList(new File("destination/file1.txt"), new
                        File("destination/file2.txt"), new File("destination/file3.txt"));

        FileCopyManager copyManager = new FileCopyManager();
        copyManager.copyFiles(sourceFiles, destFiles);
    }

}

```

**ForkJoinPool**

ForkJoinPool – это реализация ExecutorService в Java, которая используется для
параллельного выполнения задач. Эти задачи могут быть разбиты на более мелкие
подзадачи. Она позволяет использовать принцип “разделяй и властвуй” для более
эффективного использования многопроцессорных и многопоточных систем.

_**Основные компоненты ForkJoinPool:**_

**Пул потоков исполнения**

ForkJoinPool управляет пулом потоков исполнения, которые используются для
выполнения задач. Количество потоков исполнения в пуле задается при создании
экземпляра ForkJoinPool. Каждый поток в пуле имеет свой собственный стек
вызовов, что позволяет ForkJoinPool улучшить производительность в случае, когда
задачи могут быть разбиты на более мелкие подзадачи.

**Разбиение и объединение задач:**

ForkJoinPool поддерживает работу с задачами типа RecursiveAction и
RecursiveTask, которые представляют собой рекурсивно делимые задачи без
возвращаемого значения и с возвращаемым значением соответственно. Когда
ForkJoinPool получает задачу типа RecursiveTask, он разбивает ее на более мелкие
подзадачи, выполняет их параллельно в разных потоках и объединяет результаты
выполнения в единую итоговую задачу.

**Обработка ошибок:**

ForkJoinPool имеет механизм обработки ошибок, который позволяет определить, как
следует обрабатывать ошибки, возникающие во время выполнения задач. По
умолчанию, если задача вызывает исключение, то ForkJoinPool прерывает выполнение
всех задач и выбрасывает исключение ForkJoinTask.ExceptionalCompletion. Однако,
этот механизм может быть настроен на более гибкую обработку ошибок.

**Жизненный цикл:**

ForkJoinPool имеет свой жизненный цикл, который может быть управляем через
методы start(), shutdown() и awaitTermination(). Вы можете использовать эти
методы для запуска и остановки ForkJoinPool и ожидания завершения выполнения
всех задач.

**Пример использования**

Одним из ярких примеров использования ForkJoinPool является задача вычисления
факториала числа с помощью рекурсии.

Вот пример кода, который использует ForkJoinPool для вычисления факториала
числа:

```java



public class FactorialCalculator extends RecursiveTask<Integer> {
    private final int number;

    public FactorialCalculator(int number) {
        this.number = number;
    }

    @Override
    protected Integer compute() {
        if (number <= 1) {
            return 1;
        } else {
            FactorialCalculator subTask = new FactorialCalculator(number - 1);
            subTask.fork();
            return number * subTask.join();
        }
    }

    public static void main(String[] args) {
        ForkJoinPool pool = ForkJoinPool.commonPool();
        FactorialCalculator task = new FactorialCalculator(10);
        int result = pool.invoke(task);
        System.out.println(result);
    }

}

```

Здесь мы создаем класс FactorialCalculator, который наследуется от класса
RecursiveTask. Метод compute() переопределен для вычисления факториала числа.

Внутри метода compute() мы проверяем, является ли число меньше или равным 1.
Если да, то возвращаем 1. В противном случае мы создаем новый экземпляр
FactorialCalculator с числом, на 1 меньше, чем исходное число. Затем мы
запускаем эту подзадачу с помощью метода fork() и ждем его завершения с помощью
метода join(). Затем мы возвращаем результат умножения числа на результат
выполнения подзадачи.

В методе main() мы создаем ForkJoinPool и вызываем метод invoke() с нашей
задачей FactorialCalculator. Этот метод блокируется до тех пор, пока задача не
завершится, и возвращает результат.

Запустив этот код, мы получим результат – факториал числа 10, который
равен 3628800.
