# Многопоточность

## Введение

Существуют два разных типа многозадачности: на основе процессов и на основе
потоков. Важно понимать разницу между ними. Многим читателям больше знакома
многозадачность, основанная на процессах. По сути, ___процесс___ - это
программа,
которая выполняется. Таким образом, многозадачность на основе процессов является
функциональным средством, которое позволяет вашему компьютеру запускать две
или большее число программ параллельно. Например, многозадачность на основе
процессов позволяет запускать компилятор Java одновременно с использованием
текстового редактора или посещением веб-сайта. В многозадачности, основанной на
процессах, программа представляет собой наименьшую единицу кода, которая может
координироваться планировщиком.  
В многозадачной среде на основе потоков поток является наименьшей единицей
координируемого кода, т.е. одна программа может выполнять две или более задач
одновременно. Например, текстовый редактор может форматировать текст
одновременно с его выводом на печать, если эти два действия выполняются двумя
отдельными потоками. Хотя программы на Java задействуют многозадачные среды,
основанные на процессах, многозадачность на основе процессов не находится под
контролем Java. Остается многозадачность на основе потоков.  
Потоки пребывают в нескольких состояниях. Поток может _**выполняться**_. Он
может быть
_**готовым к запуску**_, как только получит время ЦП. Работающий поток может
быть
_**приостановлен**_, в результате чего он временно прекращает свою активность.
Выполнение приостановленного потока затем можно _**возобновить**_, позволяя ему
продолжиться с того места, где он остановился. Поток может быть
_**заблокирован**_ при
ожидании ресурса. В любой момент работа потока может быть _**прекращена**_, что
немедленно останавливает его выполнение. После прекращения работы выполнение
потока не может быть возобновлено.  
Наряду с многозадачностью, основанной на потоках, возникает потребность в особом
виде функционального средства, называемого _**синхронизацией**_, которое
позволяет координировать выполнение потоков четко определенными способами. В
Java имеется полная подсистема, предназначенная для синхронизации.

## Класс Thread и интерфейс Runnable

Многопоточная система Java построена на базе класса Thread,его методов и
дополняющего интерфейса Runnable. Оба типа находятся в пакете java.lang. Класс
Thread инкапсулирует поток выполнения. Для создания нового потока понадобится
либо расширить класс Thread, либо реализовать интерфейс Runnable.

### Методы Thread, помогающие управлять потоками

- **final String getName()** - получает имя потока;
- **final int getPriority()** - получает приоритет потока;
- **final boolean isAlive()** - проверяет выполняется ли поток;
- **final void join()** - ожидает прекращения работы потока;
- **void run()** - устанавливает точку входа в поток;
- **static void sleep(long millisecond)** - приостанавливает выполнение потока
  на
  заданное время;
- **void start()** - запускает поток вызовом его метода run();

### Создание потока (src/ch01)

Поток создается путем создания объекта типа Thread. Класс Thread инкапсулирует
исполняемый объект. Как уже упоминалось, в Java предусмотрены два способа
создания исполняемых объектов:

- реализация интерфейса Runnable;
- расширение класса Thread.

Интерфейс Runnable абстрагирует единицу исполняемого кода. Поток можно
конструировать для любого объекта, реализующего Runnable. В интерфейсе
Runnable определен всего лишь один метод по имени run() со следующим
объявлением:

    public void run()

Внутрь метода run() помещается код, который основывает новый поток. Важно
понимать, что run() может вызывать другие методы, использовать другие классы
и объявлять переменные в точности, как это делает главный поток. Единственное
отличие заключается в том, что метод run() устанавливает точку входа для другого
параллельного потока выполнения в программе. Этот поток завершится, когда
управление возвратится из run().
После создания класса, реализующего интерфейс Runnable,внутри него создается
объект типа Thread. В классе Thread определено несколько конструкторов. Вот
тот, который будет применяться:

    Thread(Runnable threadOb)

В приведенном конструкторе threadOb является экземпляром класса, реализующего
интерфейс Runnable. Он определяет, где начнется выполнение потока. После
создания новый поток не будет запущен, пока не вызовется его метод start() ,
объявленный в классе Thread. По существу start ( ) инициирует вы-
зов run(). Метод start() показан ниже:

    void start

**Способ создания потока с помощью расширения интерфейса Runnable**:

```java
class MyThread implements Runnable {
    String thrdName;

    MyThread(String name) {
        thrdName = name;
    }

    //Точка входа в поток
    public void run() {                 //<-- Здесь потоки начинают выполняться
        System.out.println("Поток " + thrdName + " запущен");
        try {
            for (int count = 0; count < 10; count++) {
                Thread.sleep(400);
                System.out.println("В потоке " + thrdName + " значение count: " + count);
            }
        } catch (InterruptedException exception) {
            System.out.println("Поток " + thrdName + " прерван");
        }
        System.out.println("Поток " + thrdName + " завершен");
    }
}

class UseThread {
    public static void main(String[] args) {
        System.out.println("Главный поток запущен");
        //Сначала конструируем объект MyThread
        MyThread mt = new MyThread("Child # 1"); // <-- Создаем исполняемый объект
        // Затем конструируем поток из этого объекта
        Thread newThread = new Thread(mt); // <-- Сконструируем поток на основе этого объекта;
        newThread.start(); // <-- Запускаем поток на выполнение;
        for (int i = 0; i < 50; i++) {
            System.out.println(".");
            try {
                Thread.sleep(100);
            } catch (InterruptedException exception) {
                System.out.println("Главный поток прерван");
            }
            System.out.println("Главный поток завершен");
        }
    }
}
```

Улучшения:

```java

// Изменения MyThread. В этой версии MyThread
// объект Thread создается при вызове его конструктора
// и сохраняется в переменной экземпляра по имени thrd.
// Здесь также устанавливается имя потока и предоставляется
// фабричный метод для создания и запуска потока.
class MyThread implements Runnable {
    Thread thrd; // <-- В thrd хранится ссылка на поток;

    // Сконструировать новый поток и назначить ему имя;
    MyThread(String name) {
        thrd = new Thread(this, name); // <-- При создании потоку назначается имя;
    }

    // Фабричный метод, который создает и запускает поток;
    public static MyThread createAndStart(String name) {
        MyThread thread = new MyThread(name);
        thread.thrd.start(); // Запустить поток // <-- Начать выполнение потока;
        return thread;
    }

    //Точка входа в поток
    public void run() {                 //<-- Здесь потоки начинают выполняться
        System.out.println("Поток " + thrd.getName() + " запущен");

        try {
            for (int count = 0; count < 10; count++) {
                Thread.sleep(400);
                System.out.println("В потоке " + thrd.getName() + " значение count: " + count);
            }
        } catch (InterruptedException exception) {
            System.out.println("Поток " + thrd.getName() + " прерван");
        }
        System.out.println("Поток " + thrd.getName() + " завершен");
    }
}

class UseThread {
    public static void main(String[] args) {
        System.out.println("Главный поток запущен");

        //Создаем и запускаем поток
        MyThread mt = MyThread.createAndStart("Child # 1"); // <-- Создаем исполняемый объект


        for (int i = 0; i < 50; i++) {
            System.out.println(".");
            try {
                Thread.sleep(100);
            } catch (InterruptedException exception) {
                System.out.println("Главный поток прерван");
            }
            System.out.println("Главный поток завершен");
        }
    }
}
```

**Создание потока расширением класса Thread:**

Когда класс расширяет Thread, в нем должен быть переопределен метод run(),
который является точкой входа в новый поток. Он также должен вызвать start(),
чтобы начать выполнение нового потока.

```java
class MyThreadExtThread extends Thread {
    MyThreadExtThread(String name) {
        super(name);
    }

    //Точка входа в поток;
    public void run() {
        System.out.println("Поток " + getName() + " запущен");
        try {
            for (int count = 0; count < 10; count++) {
                Thread.sleep(400);
                System.out.println("Поток: " + getName() + " count() " + count);
            }
        } catch (InterruptedException exception) {
            System.out.println("Поток " + getName() + "прерван");
        }
        System.out.println("Поток " + getName() + "Завершен");
    }

    public static MyThreadExtThread createAndStart(String name) {
        MyThreadExtThread thread = new MyThreadExtThread(name);
        thread.start();
        return thread;
    }
}

class ExtendThread {
    public static void main(String[] args) {
//        System.out.println("Главный поток запушен");
//        MyThreadExtThread mt = new MyThreadExtThread("Child # 1");
        MyThreadExtThread mte = MyThreadExtThread.createAndStart("Child #1");
//        mt.start();
        for (int i = 0; i < 50; i++) {
            System.out.println(".");
            try {
                Thread.sleep(100);
                System.out.println("Главный поток запущен");
            } catch (InterruptedException exception) {
                System.out.println("Главный поток прерван");
            }
        }
        System.out.println("Главный поток завершен");
    }
}
```

### Создание нескольких потоков

```java
package dev.folomkin.core.multithrading.example;

// Создание нескольких потоков
class MyThread implements Runnable {
    Thread thrd; // <-- В thrd хранится ссылка на поток;

    // Конструктор нового потока;
    MyThread(String name) {
        thrd = new Thread(this, name); // <-- При создании потоку назначается имя;
    }

    // Фабричный метод, который создает и запускает поток;
    public static MyThread createAndStart(String name) {
        MyThread thread = new MyThread(name);
        thread.thrd.start(); // Запустить поток // <-- Начать выполнение потока;
        return thread;
    }


    //Точка входа в поток
    public void run() {                 //<-- Здесь потоки начинают выполняться
        System.out.println("Поток " + thrd.getName() + " запущен");

        try {
            for (int count = 0; count < 10; count++) {
                Thread.sleep(400);
                System.out.println("В потоке " + thrd.getName() + " значение count: " + count);
            }
        } catch (InterruptedException exception) {
            System.out.println("Поток " + thrd.getName() + " прерван");
        }
        System.out.println("Поток " + thrd.getName() + " завершен");
    }
}

class MoreThread {
    public static void main(String[] args) {
        System.out.println("Главный поток запущен");

        //Создать и запускаем поток
        MyThread mt1 = MyThread.createAndStart("Child # 1"); // <-- Создаем исполняемый объект
        MyThread mt2 = MyThread.createAndStart("Child # 2"); // <-- Создаем исполняемый объект
        MyThread mt3 = MyThread.createAndStart("Child # 3"); // <-- Создаем исполняемый объект

        for (int i = 0; i < 50; i++) {
            System.out.println(".");
            try {
                Thread.sleep(100);
            } catch (InterruptedException exception) {
                System.out.println("Главный поток прерван");
            }
            System.out.println("Главный поток завершен");
        }
    }
}
```

### Выяснение, завершен ли поток

**Метод isAlive()** проверяет запушен ли поток;

```java
package dev.folomkin.core.multithrading.example;

// Создание нескольких потоков
class MyThread implements Runnable {
    Thread thrd; // <-- В thrd хранится ссылка на поток;

    // Конструктор нового потока;
    MyThread(String name) {
        thrd = new Thread(this, name); // <-- При создании потоку назначается имя;
    }

    // Фабричный метод, который создает и запускает поток;
    public static MyThread createAndStart(String name) {
        MyThread thread = new MyThread(name);
        thread.thrd.start(); // Запустить поток // <-- Начать выполнение потока;
        return thread;
    }


    //Точка входа в поток
    public void run() {                 //<-- Здесь потоки начинают выполняться
        System.out.println("Поток " + thrd.getName() + " запущен");

        try {
            for (int count = 0; count < 10; count++) {
                Thread.sleep(400);
                System.out.println("В потоке " + thrd.getName() + " значение count: " + count);
            }
        } catch (InterruptedException exception) {
            System.out.println("Поток " + thrd.getName() + " прерван");
        }
        System.out.println("Поток " + thrd.getName() + " завершен");
    }
}

class MoreThread {
    public static void main(String[] args) {
        System.out.println("Главный поток запущен");

        //Создать и запускаем поток
        MyThread mt1 = MyThread.createAndStart("Child # 1"); // <-- Создаем исполняемый объект
        MyThread mt2 = MyThread.createAndStart("Child # 2"); // <-- Создаем исполняемый объект
        MyThread mt3 = MyThread.createAndStart("Child # 3"); // <-- Создаем исполняемый объект

        do {
            System.out.println(".");
            try {
                Thread.sleep(100);
            } catch (InterruptedException exception) {
                System.out.println("Главный поток прерван");
            }
        } while (
                mt1.thrd.isAlive() ||
                        mt2.thrd.isAlive() ||
                        mt3.thrd.isAlive()
        ); // <-- Ожидать пока не завершатся все потоки;

        System.out.println("Главный поток завершен");
    }
}

```

**Метод join()**

Этот метод ожидает завершения потока, на котором он вызывается. Его имя
происходит от концепции вызывающего потока, ожидающего до тех пор, пока
указанный поток не присоединится к нему. Дополнительные формы метода join()
позволяют указывать максимальное время ожидания завершения указанного потока.

### Приоритеты потоков(src:ch02)

Задаются с помощью метода **final void setPriority(int level)**

В аргументе level указывается новая настройка приоритета для вызывающего
потока. Значение level должно находиться в диапазоне от MIN PRIORITY до MAX
PRIORITY. В настоящее время эти значения равны 1 и 10 соответственно. Чтобы
вернуть потоку стандартный приоритет, необходимо указать значение N0RM_PRI0RITY,
которое в настоящее время равно 5. Упомянутые приоритеты определены как
статические финальные переменные внутри Thread.

```java
public class Priority implements Runnable {
    int count;
    Thread thread;
    static boolean stop = false;
    static String currentName;

    //Конструктор нового потока
    Priority(String name) {
        thread = new Thread(this, name);
        count = 0;
        currentName = name;
    }

    //Точка входа в поток
    public void run() {
        System.out.println("Поток " + thread.getName() + " запущен");
        do {
            count++;
            if (currentName.compareTo(thread.getName()) != 0) {
                currentName = thread.getName();
                System.out.println("В потоке " + currentName);
            }
        } while (stop == false && count < 10000000);
        stop = true;
        System.out.println("\nПоток " + thread.getName() + " завершен");
    }
}

class PriorityDemo {
    public static void main(String[] args) {
        Priority mt1 = new Priority("Высокий приоритет");
        Priority mt2 = new Priority("Низкий приоритет");
        Priority mt3 = new Priority("Нормальный приоритет #1");
        Priority mt4 = new Priority("Нормальный приоритет #2");
        Priority mt5 = new Priority("Нормальный приоритет #3");

        //Назначить приоритеты
        mt1.thread.setPriority(Thread.NORM_PRIORITY + 2);
        mt2.thread.setPriority(Thread.NORM_PRIORITY - 2);
        // Для остальных потоков приоритеты остаются прежними - NORM_PRIORITY

        //Запустить потоки
        mt1.thread.start();
        mt2.thread.start();
        mt3.thread.start();
        mt4.thread.start();
        mt5.thread.start();

        try {
            mt1.thread.join();
            mt2.thread.join();
            mt3.thread.join();
            mt4.thread.join();
            mt5.thread.join();
        } catch (InterruptedException e) {
            System.out.println("Главный поток прерван");
        }
        System.out.println("\nПоток с высоким приоритетом досчитал до " + mt1.count);
        System.out.println("\nПоток с низким приоритетом досчитал до " + mt2.count);
        System.out.println("\nПоток 1 с нормальным приоритетом досчитал до " + mt3.count);
        System.out.println("\nПоток 2 с нормальным приоритетом досчитал до " + mt4.count);
        System.out.println("\nПоток 3 с нормальным приоритетом досчитал до " + mt5.count);
    }
}

```

### Синхронизация

#### (src:ch03)

В случае применения нескольких потоков иногда необходимо координировать действия
двух или большего числа потоков. Процесс, посредством которого достигается такая
цель, называется _**синхронизацией**_.  
Основой синхронизации в Java является принцип монитора, который контролирует
доступ к объекту. Монитор реализует концепцию блокировки. Когда объект
заблокирован одним потоком, никакой другой поток не может получить доступ к
этому объекту. Когда поток завершается, объект деблокируется и становится
доступным другому потоку.  
Существуют два способа синхронизации кода. Оба способа предполагают
использование ключевого слова synchronized.

#### Использование синхронизированных методов

Синхронизировать доступ к методу можно с помощью ключевого слова synchronized.
При вызове синхронизированного метода вызывающий поток входит в монитор объекта,
который затем блокирует объект. Пока объект заблокирован, никакой другой поток
не может войти в данный метод или в любой другой синхронизированный метод,
определенный в классе объекта. Когда поток возвращается из метода, монитор
разблокирует объект, позволяя использовать его следующим потоком.

```java
class SumArray {
    private int sum;

    synchronized int sumArray(int[] nums) { // <-- Метод синхронизирован
        sum = 0; // сбросить sum;
        for (int num = 0; num < nums.length; num++) {
            sum += nums[num];
            System.out.println("Промежуточная сумма в потоке " +
                    Thread.currentThread().getName() + " равна " + sum);
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                System.out.println("Поток прерван");
            }
        }
        return sum;
    }
}

class MyThread implements Runnable {
    Thread thrd;
    static SumArray sa = new SumArray();
    int[] a;
    int answer;

    //Конструктор нового потока;
    MyThread(String name, int[] nums) {
        thrd = new Thread(this, name);
        a = nums;
    }

    //Фабричный метод, который создает и запускает поток
    public static MyThread createAndStartThread(String name, int[] nums) {
        MyThread thread = new MyThread(name, nums);
        thread.thrd.start();
        return thread;
    }

    //Точка входа в поток
    public void run() {
        int sum;
        System.out.println("Поток " + thrd.getName() + " запущен");
        answer = sa.sumArray(a);
        System.out.println("Сумма в потоке " + thrd.getName() + "равна " + answer);
        System.out.println("Поток " + thrd.getName() + " завершен");
    }
}

class Sync {
    public static void main(String[] args) {
        int[] a = {1, 2, 3, 4, 5};
        MyThread mt1 = MyThread.createAndStartThread("Child #1", a);
        MyThread mt2 = MyThread.createAndStartThread("Child #2", a);

        try {
            mt1.thrd.join();
            mt2.thrd.join();
        } catch (InterruptedException e) {
            System.out.println("Главный поток прерван");
        }
    }
}

```

Первый класс —SumArray. Он содержит метод sumArray (), подсчитывающий сумму
элементов целочисленного массива. Второй класс, MyThread, задействует
статический объект типа SumArray для получения суммы элементов целочисленного
массива. Объект имеет имя sa и поскольку он статический, существует только одна
его копия, совместно используемая всеми экземплярами MyThread. Наконец, третий
класс, Sync,создает два потока, каждый из которых вычисляет сумму элементов
целочисленного массива.  
Внутри sumArray() вызывается метод sleep() , чтобы намеренно разрешить
переключение задач, если оно возможно. Так как метод sumArray() синхронизирован,
он может применяться одновременно только одним потоком. Таким образом,
когда второй дочерний поток начинает выполнение, он не входит в sumArray() до
тех пор, пока первый дочерний поток не закончит с ним работу. В результате
гарантируется получение корректного результата.

Итог:

- Синхронизированный метод создается предварением его объявления
  ключевым словом synchronized.
- Для любого заданного объекта после вызова синхронизированного метода объект
  блокируется, и никакие синхронизированные методы на том же объекте не могут
  применяться другим потоком выполнения.
- Другие потоки, пытающиеся вызвать используемый синхронизированный объект,
  перейдут в состояние ожидания, пока объект не будет разблокирован.
- Когда поток покидает синхронизированный метод, объект деблокируется.

### Оператор synchronized

    synchronized(objRef) {
         // операторы , подлежащие синхронизации
    }

Здесь objRef — это ссылка на синхронизируемый объект. Блок synchronized
гарантирует, что вызов синхронизированного метода, который является членом
класса objRef, произойдет только после того, как текущий поток успешно войдет
в монитор objRef.

```java
class SumArray {
    private int sum;

    int sumArray(int[] nums) { // <-- Метод синхронизирован
        sum = 0; // сбросить sum;
        for (int num = 0; num < nums.length; num++) {
            sum += nums[num];
            System.out.println("Промежуточная сумма в потоке " +
                    Thread.currentThread().getName() + " равна " + sum);
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                System.out.println("Поток прерван");
            }
        }
        return sum;
    }
}

class MyThread implements Runnable {
    Thread thrd;
    static SumArray sa = new SumArray();
    int[] a;
    int answer;

    //Конструктор нового потока;
    MyThread(String name, int[] nums) {
        thrd = new Thread(this, name);
        a = nums;
    }

    //Фабричный метод, который создает и запускает поток
    public static MyThread createAndStartThread(String name, int[] nums) {
        MyThread thread = new MyThread(name, nums);
        thread.thrd.start();
        return thread;
    }

    //Точка входа в поток
    public void run() {
        int sum;
        System.out.println("Поток " + thrd.getName() + " запущен");
        synchronized (sa) {
            answer = sa.sumArray(a);
        }
        System.out.println("Сумма в потоке " + thrd.getName() + "равна " + answer);
        System.out.println("Поток " + thrd.getName() + " завершен");
    }
}

class Sync {
    public static void main(String[] args) {
        int[] a = {1, 2, 3, 4, 5};
        MyThread mt1 = MyThread.createAndStartThread("Child #1", a);
        MyThread mt2 = MyThread.createAndStartThread("Child #2", a);
        try {
            mt1.thrd.join();
            mt2.thrd.join();
        } catch (InterruptedException e) {
            System.out.println("Главный поток прерван");
        }
    }
}

```

### Взаимодействие между потоками с использованием notify(), wait() и notifyAll()

Методы wait(), notify() и notifyAll() являются частью всех объектов,
поскольку они реализованы классом Object. Указанные методы должны вызываться
только из контекста synchronized. Они используются следующим образом. Когда
выполнение потока временно блокируется, он вызывает метод wait(), что
приводит к переводу потока в спящий режим, а монитор для данного объекта
освобождается, позволяя другому потоку использовать этот объект. Позже спящий
поток пробуждается, когда другой поток входит в тот же монитор и вызывает
метод notify() или notifyAll().

Вызов notify() возобновляет работу одного ожидающего потока. Вызов notifyAll()
уведомляет все потоки, а планировщик определяет, какой поток получает доступ к
объекту.


