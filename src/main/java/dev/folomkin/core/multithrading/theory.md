# Многопоточность

## Класс Thread и интерфейс Runnable

Потоки — средство, которое помогает организовать одновременное выполнение
нескольких задач, каждой в независимом потоке. Потоки представляют собой
экземпляры классов, каждый из которых запускается и функционирует
самостоятельно, автономно (или относительно автономно) от главного потока
выполнения программы. Существует три способа создания и запуска потока: на
основе расширения класса Thread, реализации интерфейсов Runnable или Callable.

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

## Жизненный цикл потока

При выполнении программы объект класса Thread может быть в одном из
четырех основных состояний: «новый», «работоспособный», «неработоспособный» и
«пассивный». При создании потока он получает состояние «новый» **_(NEW)_** и не
выполняется. Для перевода потока из состояния «новый» в состояние
«работоспособный» **_(RUNNABLE)_** следует выполнить метод start(), который
вызывает метод run() — основной метод потока. Поток в этом состоянии либо
запущен, либо готов к выполнению. Однако он может ожидать выделения ресурсов,
таких как процессорное время или доступ к общему ресурсу. Как только необходимые
ресурсы будут доступны, поток может продолжить свое выполнение.

Поток может находиться в одном из состояний, соответствующих элементам
статически вложенного класса-перечисления Thread.State:

- **_NEW_** — поток создан, но еще не запущен -> Создан новый объект потока;
- **_RUNNABLE_** — поток выполняется -> Вызван метод start();
- **_BLOCKED_** — поток блокирован;
- **_WAITING_** — поток ждет окончания работы другого потока;
- **_TIMED_WAITING_** — поток некоторое время ждет окончания другого потока или
  просто в ожидании истечения времени;
- **_TERMINATED_** — поток завершен.

Потоки пребывают в нескольких состояниях. Поток может выполняться. Он
может быть готовым к запуску, как только получит время ЦП. Работающий по-
ток может быть приостановлен, в результате чего он временно прекращает свою
активность. Выполнение приостановленного потока затем можно возобновить,
позволяя ему продолжиться с того места , где он остановился. Поток может быть
заблокирован при ожидании ресурса. В любой момент работа потока может быть
прекращена, что немедленно останавливает его выполнение . После прекращения
работы выполнение потока не может быть возобновлено.

Получить текущее значение состояния потока можно вызовом метода **getState()**.

Поток переходит в состояние «неработоспособный» в режиме ожидания
**_(WAITING)_** вызовом методов **_join(), wait()_** или методов ввода/вывода,
которые предполагают задержку.

Для задержки потока на некоторое время (в миллисекундах) можно перевести его в
режим ожидания по времени **_(TIMED_WAITING)_** с помощью методов
**yield(),sleep(longmillis), join(long timeout) и wait(long timeout)**.

Вернуть потоку работоспособность после вызова метода suspend() можно методом
resume() (deprecated-метод), а после вызова метода wait() — методами notify()
или notifyAll().

Когда поток просыпается, ему необходимо изменить состояние монитора
объекта, на котором проходило ожидание. Для этого поток переходит в состояние
BLOCKED и только после этого возвращается в работоспособное состояние.

Поток переходит в «пассивное» состояние (TERMINATED), если вызваны методы
interrupt(), stop() (deprecated-метод) или метод run() завершил выполнение,
и запустить его повторно уже невозможно. После этого, чтобы запустить поток,
необходимо создать новый объект потока. Метод interrupt() успешно завершает
поток, если он находится в состоянии «работоспособный». Если же поток в этот
момент неработоспособен, например, находится в состоянии TIMED_WAITING,
то метод инициирует исключение InterruptedException. Чтобы это не происходило,
следует предварительно вызвать метод isInterrupted(), который проверит
возможность завершения работы потока. При разработке не следует использовать
методы принудительной остановки потока, так как возможны проблемы с закрытием
ресурсов и другими внешними объектами.

Deprecated-методы класса Thread: suspend(), resume(), stop() и некоторые
другие категорически запрещены к использованию, так как они не являются
в полной мере потокобезопасными.

![thread-state0.png](/img/threads/thread-state0.png)

### Из другого источника

![thread-state1.png](/img/threads/thread-state1.png)
**_NEW_** — состояние, когда поток создан и готов к использованию. Это
состояние,
когда мы еще не запустили поток.

**_RUNNABLE_** — состояние, в которое поток переходит после того, как мы его
запустили. В этом состоянии поток выполняет свою работу, т.е. логику, которую мы
определили.

**_WAITING_** — состояние ожидания, в которое поток может перейти во время
своего выполнения. Есть три состояния ожидания — **_BLOCKED_**, **_WAITING_**,
**_TIMED_WAITING_**.
Например, когда поток пытается получить монитор объекта, он входит в состояние
блокировки — **_BLOCKED_**; когда поток ожидает выполнения другого потока, тогда
поток переходит в состояние ожидания — **_WAITING_**, а когда поток ожидает
только определённое количество времени для выполнения другого потока, поток
входит в состояние — **_TIMED_WAITING_**. Поток возвращается в состояние —
**_RUNNABLE_**, как только другие потоки выполнили или освободили монитор
объекта. Поток может бесконечно менять свое состояние из состояния —
**_RUNNABLE_** в состояние — **_WAITING_** и наоборот.

**_DEAD_** / **_TERMINATED_** — состояние, в которое поток переходит после
завершения
выполнения или в случае возникновения исключений. Поток после выполнения не
может быть запущен снова. Если мы попытаемся запустить поток в состоянии —
Dead / Terminated, мы получим исключение IllegalStateException.

## Перечисление TimeUnit

Представляет различные единицы измерения времени. В TimeUnit реализован ряд
методов по преобразованию между единицами измерения и по управлению операциями
ожидания в потоках в этих единицах. Используется для информирования методов,
работающих со временем, о том, как интерпретировать заданный параметр времени.

Перечисление TimeUnit может представлять время в семи размерностях-значениях:
NANOSECONDS, MICROSECONDS, MILLISECONDS, SECONDS, MINUTES, HOURS, DAYS.

Кроме методов преобразования единиц времени, представляют интерес методы
управления потоками:

- void timedWait(Object obj, long timeout) — выполняет метод wait(long time) для
  объекта obj класса Object, используя заданные единицы измерения;
- void timedJoin(Thread thread, long timeout) — выполняет метод join(long time)
  на потоке thread, используя заданные единицы измерения;
- void sleep(long timeout) — выполняет метод sleep(long time) класса Thread,
  используя заданные единицы измерения. Например:

      TimeUnit.MINUTES.sleep(42);

## Интерфейс Callable

При работе с многопоточностью в Java, часто приходится сталкиваться с задачей
создания отдельного потока. Для этого в Java существуют два основных
интерфейса — Runnable и Callable. Чтобы понять, в каких случаях лучше
использовать тот или иной интерфейс, необходимо знать их основные различия.
Интерфейс Runnable был представлен в Java с самого начала и его метод run() не
возвращает результат и не может бросить проверяемое исключение. Это делает
Runnable удобным для простых задач, которые не требуют возвращения результата
или обработки исключений. Например, если требуется просто запустить какую-то
операцию в отдельном потоке.

```java
  public static void main(String[] args) {
    Runnable runnable = () -> {
        System.out.println("Running in a separate thread");
    };
    new Thread(runnable).start();
}
```

Интерфейс Callable же был введен в Java 5 и представляет собой более
универсальный инструмент для работы с потоками. Метод call() этого интерфейса
возвращает результат и может бросить проверяемое исключение. Это делает Callable
более подходящим для сложных задач, где требуется обработка исключений и
возвращение результата.

    public interface Callable;{
        V call() throws Exception;
    }

```java
public static void main(String[] args) {
    Callable<Integer> callable = () -> 42;
    FutureTask<Integer> futureTask = new FutureTask<>(callable);
    new Thread(futureTask).start();
}
```

Таким образом, выбор между Runnable и Callable зависит от конкретной задачи.
Если требуется выполнить простую операцию в отдельном потоке без возвращения
результата и обработки исключений, то лучше использовать Runnable. Если же
требуется выполнить более сложную операцию с обработкой исключений и
возвращением результата, то стоит выбирать Callable.

Интерфейс Callable<V> представляет поток, возвращающий значение вызывающему
потоку. Определяет один метод V call() throws Exception, в код реализации
которого и следует поместить решаемую задачу. Результат выполнения
метода V call() может быть получен после окончания работы через экземпляр
класса Future<V>, методами V get() или V get(long timeout, TimeUnit unit).
Эти методы останавливают выполнение потока, в котором они вызваны, поэтому
вызывать их следует в момент, когда закончится выполнение потока Callable.

Перед извлечением результатов работы потока Callable можно проверить,
завершилась ли задача успешно или была отменена, методами isDone() и
isCancelled() соответственно.

```java
class ActionCallable implements Callable<Integer> {
    private List<Integer> integers;

    public ActionCallable(List<Integer> integers) {
        this.integers = integers;
    }

    @Override
    public Integer call() throws Exception {
        int sum = 0;
        for (Integer integer : integers) {
            sum += integer;
        }
        return sum;
    }
}

public class Code {
    public static void main(String[] args) {
        ActionCallable actionCallable = new ActionCallable(List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
        try {
            int res = actionCallable.call();
            System.out.println(res);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

## Future

Future хранит результат асинхронного вычисления. Вы можете запустить вычисление,
предоставив кому-либо объект Future, и забыть о нем. Владелец объекта Future
может получить результат, когда он будет готов.

Интерфейс Future имеет следующие методы.

- V get() throws ... - устанавливает блокировку до тех пор, пока не завершится
  вычисление
- V get(long timeout, TimeUnit unit) throws ... - генерирует исключение
  TimeoutException, если истекает таймаут до завершения вычислений. Если
  прерывается поток, выполняющий вычисление, оба метода генерируют исключение
  InterruptedException. Если вычисление уже завершено, get() немедленно
  возвращает управление.
- boolean isDone() - возвращает false, если вычисление продолжается, и true —
  если оно завершено.
- boolean isCancelled() - может прервать вычисление. Если вычисление еще не
  стартовало, оно отменяется и уже не будет запущено. Если же вычисление уже
  идет, оно прерывается в случае равенства true параметра mayInterrupt.

### FutureTask

Класс-оболочка FutureTask представляет собой удобный механизм для превращения
Callable одновременно в Future и Runnable, реализуя оба интерфейса.

## Execute

В Java 5 добавлен механизм управления заданиями, основанный на возможностях
интерфейса **Executor** и его наследников **ExecutorService**,
**ScheduledExecutorService**, включающих организацию запуска потоков и их
групп, а также способы их планирования, управления, отслеживания прогресса и
завершения асинхронных задач. Все эти задачи можно было сделать и раньше, но
это выполнялось либо самим программистом, либо с привлечением сторонних
библиотек. Поэтому был определен наиболее распространенный набор задач и
реализован в возможностях ExecutorService.

**Методы класса ExecutorService:**

- **execute(Runnable task)** - запускает традиционные потоки;
- **submit(Callable<T> task)** и **submit(Runnable task)** - запускают потоки
  как с возвращаемым значением, так и классические.
- **invokeAll(Collection<? extends Callable<T>> tasks)** - можно запустить
  несколько потоков;
- **shutdown()** - прекращает действие самого исполнителя после того, как все
  запущенные им ранее потоки отработают, и не даст запустить новые, сгенерировав
  при этом исключение RejectedExecutionException.
- **shutdownNow()** - останавливает работу сервиса и удаляет все запущенные на
  объекте ExecutorService задачи-потоки.

```java
class ActionCallable implements Callable<Integer> {
    private List<Integer> integers;

    public ActionCallable(List<Integer> integers) {
        this.integers = integers;
    }

    @Override
    public Integer call() throws Exception {
        int sum = 0;
        for (Integer integer : integers) {
            sum += integer;
        }
        return sum;
    }
}

public class Code {
    public static void main(String[] args) {
        List<Integer> list = IntStream.range(0, 1000).boxed().toList();
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<Integer> future = executor.submit(new ActionCallable(list));
        executor.shutdown();// останавливает службу, но не поток
        // executor.submit(new Thread()); /* попытка запуска приведет к выдаче исключения */
        // executor.shutdownNow(); // останавливает службу и все запущенные потоки
        try {
            System.out.println(future.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        // -> 499500
    }
}
```

При использовании ExecutorService метод shutdown() обязателен к вызову, иначе
приложение не завершит свою работу.

Вызов метода **boolean awaitTermination(long timeout, TimeUnit unit)**
останавливает поток, в котором вызван, и по истечении времени возвращает true,
если все потоки, запущенные объектом ExecutorService, завершили свою работу, или
false — если нет.

Статические методы класса Executors определяют правила запуска потоков:

- **newSingleThreadExecutor()** позволяет исполнителю запускать только один
  поток,
- **newFixedThreadPool(int numThreads)** — число потоков не более, чем
  указано в параметре numThreads, ставя другие потоки в очередь ожидания
  окончания уже запущенных потоков,
- **newScheduledThreadPool()** — запуск по расписанию.

## Механизм Fork\Join

В Java 7 появилась еще одна имплементация ExecutorService класс планировщик
потоков ForkJoinPool с поддержкой аппаратного параллелизма для запуска
задач, реализующих функционал абстрактного класса ForkJoinTask, но также
способный запускать потоки, не являющиеся ForkJoinTask. Планировщик
ForkJoinPool распределяет задания между запущенными потоками. Реализуется
стратегия «разделяй и властвуй»: очень большая задача делится на более
мелкие подзадачи, которые, в свою очередь, могут делиться на еще более
мелкие, рекурсивно, до тех пор, пока не будет получена маленькая задача, которая
уже может решаться непосредственно. Далее весь пул мелких задач выполняется
параллельно на процессорных ядрах. Результаты каждой задачи
«объединяются» при получении общего результата. Все работающие потоки,
если для них нет заданий, пытаются найти и выполнить задачи, созданные другими
активными потоками. Этот механизм еще называют «воровство работы»,
что позволяет использовать ограниченное число потоков более производительно по
сравнению с другими реализациями ExecutorService. Все потоки, созданные
ForkJoinPool, запущены как потоки-демоны.  
Алгоритм стратегии можно условно представить в виде:

      if (размер_задачи < пороговое_значение) {
            1. решить задачу напрямую, так как она мелкая
      } else {
         1. разбить задачу на подзадачи
         2. рекурсивно решить каждую задачу
         3. объединить результаты
      }

Механизм Fork\Join упрощает создание потоков, а также использует несколько
процессоров, за счет чего сокращается время вычислений и повышается
производительность приложения в целом при решении крупных задач, таких как
обработка больших массивов данных. Этот механизм позволяет управлять большим
числом задач и относительно небольшим числом потоков, создаваемых и
контролируемых в ForkJoinPool.

**Методы ForkJoinTask:**

- **ForkJoinTask <V> fork()** — отправляет задачу для асинхронного выполнения.
  Этот метод возвращает текущий объект ForkJoinTask, а вызывающий его поток
  продолжает работать;
- **V join()** — ожидает выполнения задачи и возвращает результат;
- **V invoke()** — объединяет fork() и join() в одном вызове. Он запускает
  задачу, ожидает ее завершения и возвращает результат.
- **static invokeAll()** - для запуска нескольких задач одновременно.

Класс **RecursiveTask\<V>** для задач, которые возвращают значения, и по своей
сути аналогичен интерфейсу Callable\<E> с его методом E call(). Определен
абстрактный метод **protected V compute()**, не предназначенный для внешних
вызовов и содержащий алгоритм задачи и условия создания и запуска подзадач.

```java
class SumRecursiveTask extends RecursiveTask<Long> {
    private List<Long> longList;
    private int begin;
    private int end;
    public static final long THRESHOLD = 10_000;

    public SumRecursiveTask(List<Long> longList) {
        this(longList, 0, longList.size());
    }

    private SumRecursiveTask(List<Long> longList, int begin, int end) {
        this.longList = longList;
        this.begin = begin;
        this.end = end;
    }

    @Override
    protected Long compute() {
        int length = end - begin;
        long result = 0;
        if (length <= THRESHOLD) {
            for (int i = begin; i < end; i++) {
                result += longList.get(i);
            }
        } else {
            int middle = begin + length / 2;
            SumRecursiveTask taskLeft = new SumRecursiveTask(longList, begin, middle);
            taskLeft.fork(); // run async
            SumRecursiveTask taskRight = new SumRecursiveTask(longList, middle, end);
            taskRight.fork();//or compute()
            Long leftSum = taskLeft.join();
            Long rightSum = taskRight.join();
            result = leftSum + rightSum;
        }
        return result;
    }
}

public class Code {
    public static void main(String[] args) {
        int end = 1_000_000;
        List<Long> numbers = LongStream.range(0, end)
                .boxed()
                .collect(Collectors.toList());
        ForkJoinTask<Long> task = new SumRecursiveTask(numbers);
        long result = new ForkJoinPool().invoke(task);
        System.out.println(result);
    }
}
```

## Параллелизм

В Java 8 вместе со Stream API было введено понятие параллелизма, которое
иcпользует ForkJoinPool неявно. Задача обработки будет выполняться параллельно
вызовом метода parallel() сразу же после создания stream или создавая его
непосредственно методом parallelStream().

```java
    public static void main(String[] args) {
    long result;
    result = LongStream.range(0, 1_000_000_000)
            .boxed()
            .parallel()
            .map(x -> x / 7)
            .peek(v -> System.out.println(Thread.currentThread().getName()))
            .reduce((x, y) -> x + (int) (3 * Math.sin(y)))
            .get();
    System.out.println(result);

    // -> ForkJoinPool.commonPool-worker-8
    //    ForkJoinPool.commonPool-worker-11

}
```

Если убрать вызов метода parallel(), то в процессе выполнения будет выводить
только имя основного потока main, и никакого разделения на подзадачи
не произойдет.  
Пользовательский пул потоков, созданный на основе Runnable или Callable,
непосредственно сделать выполняемым параллельно не существует возможности,
однако его можно обернуть в ForkJoinPool.

```java
public static void main(String[] args) {
    long sec = System.currentTimeMillis();
    Callable<Integer> task = () -> IntStream.range(0, 1_000_000_000)
            .boxed()
            .parallel()
            .map(x -> x / 3)
            .peek(th ->
                    System.out.println(Thread.currentThread().getName()))
            .reduce((x, y) -> x + (int) (3 * Math.sin(y)))
            .get();
    ForkJoinPool pool = new ForkJoinPool(8);// 8 processors
    try {
        int result = pool.submit(task).get();
        System.out.println(result);
    } catch (InterruptedException | ExecutionException e) {
        e.printStackTrace();
    }
    System.out.println((System.currentTimeMillis() - sec) / 1000.);
}
```

Таким образом, параллельный поток объявляет ForkJoin в качестве родительского,
а не обычный поток. Вследствие чего ForkJoinPool.commonPool не используется.
Параллельные потоки, запущенные в разных частях приложения, будут конкурировать
между собой, поэтому следует ограничивать размеры пула.

## Timer и поток TimerTask

Некоторые виды задач требуют периодичности выполнения. Например, рассылка
почтовых уведомлений или сообщений об истечении срока действия пароля, лицензии.
Уведомления о совершении периодичных платежей типа абонентской платы,
квартплаты и др. Запуск на выполнение задач в заданное время или с определенной
периодичностью представляет собой типичное использование потоков.
В задаче контроля и устранения утечек из пулов соединений с БД поток-таймер
может выполнять несколько различных задач. Основная — проверка целостности пула:
если число активных соединений не совпадает с заявленным размером пула, то
таймер может добавить новые соединения. Если стратегия пула зависит от его
загруженности, то таймер может управлять числом активных
соединений, а именно: закрывать часть соединений при низкой нагрузке
на БД и увеличивать их число при повышении нагрузки, не выходя при этом за
предельные значения размеров пула.

Для запуска потоков по расписанию в пакете java.util — абстрактный класс
TimerTask, имплементирующий интерфейс Runnable. Планировкой выполнения потоков
занимается класс java.util Timer, методы которого работают с объектом TimerTask.
Для запуска потоков используются методы schedule(params)
и scheduleAtFixedRate(params).

```java
public class TimerCounter extends TimerTask {
    private static int i;

    @Override
    public void run() {
        System.out.print(++i);
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("\t" + i);
    }
}

public class TimerMain {
    public static void main(String[] args) {
        Timer timer = new Timer();
        timer.schedule(new TimerCounter(), 100, 3000);
    }
}
```

Поток TimerCounter стартует через 100 миллисекунд и будет запускаться
на выполнение каждые три секунды. Прервать выполнение задания можно методом
cancel() класса Timer. Удалить все прерванные задания из очереди таймера —
методом purge().

## Управление приоритетами

Потоку можно назначить приоритет от 1 (константа Thread.MIN_PRIORITY)
до 10 (Thread.MAX_PRIORITY) с помощью метода setPriority(int newPriority).
Получить значение приоритета потока можно с помощью метода getPriority().

```java
public static void main(String[] args) {
    Thread walkMin = new Thread(new WalkThread(), "Min");
    Thread talkMax = new Thread(new TalkThread(), "Max");
    walkMin.setPriority(Thread.MIN_PRIORITY);
    talkMax.setPriority(Thread.MAX_PRIORITY);
    talkMax.start();
    walkMin.start();
}
```

## Управление потоками

Приостановить (задержать) выполнение потока можно с помощью метода
static void sleep(int millis) класса Thread. Поток переходит в состояние
TIMED_WAITING. Иной способ состоит в вызове метода static void yield(),
который отдает квант времени другому потоку, не мешая самому потоку работать и
предоставляя возможность виртуальной машине переместить его в конец очереди
других потоков.

**_Метод join()_** блокирует работу потока, в котором он вызван до тех пор, пока
не будет закончено выполнение вызывающего метод потока или не истечет
время ожидания при обращении к методу join(long timeout). Такие же результаты
позволяет получить замена метода join() класса Thread на метод
timedJoin(Thread thread, long timeout) перечисления TimeUnit.

```java
class JoinThread extends Thread {
    public void run() {
        System.out.println("Start");
        try {
            TimeUnit.MILLISECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("End");
    }
}

public class Code {
    public static void main(String[] args) {
        new Thread(() -> {
            System.out.println("start 1");
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("end 1");
        }).start();
        JoinThread thread = new JoinThread();
        thread.start();
        try {
            thread.join(); // or join(100)
            // or //TimeUnit.MILLISECONDS.timedJoin(thread0, 100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("end of " + Thread.currentThread().getName());
    }
}
```

Несмотря на вызов метода join() для потока thread, поток, стартовавший с помощью
лямбда-выражения, будет независимо работать, в отличие от потока main,
который сможет продолжить свое выполнение только по завершении потока thread.

Вызов статического метода yield() для исполняемого потока должен приводить к
приостановке потока на некоторый квант времени, чтобы другие потоки
могли выполнять свои действия. В некоторых ситуациях поток может отдавать
квант времени самому себе или вообще ничего не делать.

## Потоки-демоны

Потоки-демоны работают в фоновом режиме вместе с программой, но представляют
собой функциональность, которая не является важной для основной логики
программы. Если какой-либо процесс может выполняться на фоне работы основных
потоков выполнения, а его деятельность заключается в косвенном обслуживании
основных потоков приложения, то такой процесс может быть запущен как
поток-демон. С помощью метода setDaemon(boolean value), вызванного вновь
созданным потоком до его запуска, можно определить поток-демон. Метод boolean
isDaemon() позволяет определить, является ли указанный поток демоном или нет.

```java
class CustomThread extends Thread {
    public void run() {
        try {
            if (isDaemon()) {
                System.out.println("start of daemon thread");
                TimeUnit.MILLISECONDS.sleep(10);
            } else {
                System.out.println("start of normal thread");
            }
        } catch (InterruptedException e) {
            System.err.print(e);
        } finally {
            if (!isDaemon()) {
                System.out.println("normal thread completion");
            } else {
                System.out.println("daemon thread completion");
            }
        }
    }
}

public class Code {
    public static void main(String[] args) {
        CustomThread normal = new CustomThread();
        CustomThread daemon = new CustomThread();
        daemon.setDaemon(true);
        daemon.start();
        normal.start();
        System.out.println("End of main");
    }
}
```

Поток-демон (из-за вызова метода sleep(10)) не успел выполнить свой код до
завершения основного потока приложения, связанного с методом main(). Базовое
свойство потоков-демонов заключается в возможности основного потока приложения
завершить выполнение потока-демона (в отличие от обычных потоков) с окончанием
кода метода main(), не обращая внимания на то, что поток-демон еще работает.
Если уменьшать время задержки потока-демона, то он может успеть завершить свое
выполнение до окончания работы основного потока.

## Потоки и исключения

В процессе функционирования потоки являются в общем случае независимыми друг от
друга. Прямым следствием такой независимости будет корректное продолжение работы
потока main после аварийной остановки запущенного из него потока после генерации
исключения.

```java
public static void main(String[] args) {
    new Thread(() -> {
        if (Boolean.TRUE) {
            throw new RuntimeException();
        }
        System.out.println("end of Thread");
    }).start();
    try {
        TimeUnit.MILLISECONDS.sleep(20);
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
    System.out.println("end of main thread");
}
```

## Атомарные типы и модификатор volatile

Все данные приложения находятся в основном хранилище данных. При
запуске нового потока создается копия хранилища и именно ею пользуется этот
поток. Изменения, произведенные в копии, могут не сразу находить отражение в
основном хранилище, и наоборот. Для получения актуального значения следует
прибегнуть к синхронизации. Наиболее простым приемом будет объявление поля
класса с модификатором volatile. Данный модификатор вынуждает потоки производить
действия по фиксации изменений достаточно быстро. То есть другой
заинтересованный поток, скорее всего, получит доступ к уже измененному значению.
Для базовых типов до 32 бит этого достаточно. При использовании со ссылкой на
объект синхронизировано будет только значение самой ссылки, а не объект, на
который она ссылается. Синхронизация ссылки будет эффективной в случае, если
она указывает на перечисление, так как все элементы перечисления существуют
в единственном экземпляре. Решением проблемы с доступом к одному экземпляру
из разных потоков является блокирующая синхронизация. Модификатор volatile
обеспечивает неблокирующую синхронизацию.

Атомарные классы созданы для организации неблокирующих структур данных. Классы
атомарных переменных AtomicInteger, AtomicLong, AtomicBoolean, AtomicReference и
др. фактически являются оболочкой для volatile.

## Методы synchronized

Нередко возникает ситуация, когда несколько потоков имеют доступ к некоторому
объекту, проще говоря, пытаются использовать общий ресурс и начинают мешать друг
другу. Более того, они могут повредить этот общий ресурс. Например, когда два
потока записывают информацию в объект (поток, файл и т.д.). Для контролирования
процесса записи может использоваться разделение ресурса с применением ключевого
слова synchronized.

В качестве примера будет рассмотрен процесс записи информации в файл
двумя конкурирующими потоками. В методе main() классa SynchroMain создаются
два потока. В этом же методе создается экземпляр класса CommonResource,
содержащий поле типа FileWriter, связанное с файлом на диске. Экземпляр
CommonResource передается в качестве параметра обоим потокам. Первый поток
записывает строку методом writing() в экземпляр класса CommonResource.
Второй поток также пытается сделать запись строки в тот же самый объект
CommonResource. Во избежание одновременной записи такие методы объявляются
как synchronized. Синхронизированный метод изолирует объект, после чего
он становится недоступным для других потоков. Изоляция снимается, когда поток
полностью выполнит соответствующий метод. Другой способ снятия изоляции —
вызов метода wait() из изолированного метода — будет рассмотрен позже.

В примере продемонстрирован вариант синхронизации файла для защиты от
одновременной записи информации в файл двумя различными потоками.

```java
class CommonResource implements AutoCloseable {
    private FileWriter fileWriter;

    public CommonResource(String file) throws IOException {
        fileWriter = new FileWriter(file, true);
    }

    public synchronized void writing(String info, int i) {
        try {
            fileWriter.append(info + i);
            System.out.print(info + i);
            TimeUnit.MILLISECONDS.sleep(new Random().nextInt(500));
            fileWriter.append("->" + info.charAt(0) + i + " ");
            System.out.print("->" + info.charAt(0) + i + " ");
        } catch (IOException | InterruptedException e) {
            System.err.print(e);
        }
    }

    @Override
    public void close() throws IOException {
        if (fileWriter != null) {
            fileWriter.close();
        }
    }
}

class UseFileThread extends Thread {
    private CommonResource resource;

    public UseFileThread(String name, CommonResource resource) {
        super(name);
        this.resource = resource;
    }

    public void run() {
        for (int i = 0; i < 5; i++) {
            resource.writing(this.getName(), i); // synchronized method call
        }
    }
}


public class Code {
    public static void main(String[] args) {
        try (CommonResource resource = new CommonResource("src/main/resources/test.txt")) {
            UseFileThread thread1 = new UseFileThread("First", resource);
            UseFileThread thread2 = new UseFileThread("Second", resource);
            thread1.start();
            thread2.start();
            TimeUnit.SECONDS.sleep(5);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        // -> 
        // First0->F0 Second0->S0 First1->F1 Second1->S1 Second2->S2
        // Second3->S3 First2->F2 Second4->S4 First3->F3 First4->F4

        // видно, что метод writing() всегда отрабатывает полностью.
    }
}

```

Код построен таким образом, что при отключении синхронизации метода
writing(), в случае его вызова одним потоком, другой поток может вклиниться
и произвести запись своей информации, несмотря на то, что метод не завершил
запись, инициированную первым потоком.

## Инструкция synchronized

Синхронизировать объект можно не только при помощи методов с соответствующим
модификатором, но и при помощи синхронизированного блока кода. В этом случае
происходит блокировка объекта, указанного в инструкции
synchronized(reference){/*code*/}, и он становится недоступным для других
синхронизированных методов и блоков.

