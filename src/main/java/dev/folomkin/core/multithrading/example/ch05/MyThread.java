package dev.folomkin.core.multithrading.example.ch05;

public class MyThread implements Runnable {
    Thread thread;
    boolean suspended; // <-- Когда true поток приостанавливается
    boolean stopped; // <-- Когда true поток останавливается

    MyThread(String name) {
        thread = new Thread(this, name);
        suspended = false;
        stopped = false;
    }

    // Фабричный метод, который создает и запускает поток
    public static MyThread createAndStart(String name) {
        MyThread myThread = new MyThread(name);
        myThread.thread.start();
        return myThread;
    }

    // Точка входа в поток
    public void run() {
        System.out.println("Поток " + thread.getName() + " запущен");

        try {
            for (int i = 0; i < 1000; i++) {
                System.out.print(i + " ");
                if ((i % 10) == 0) {
                    System.out.println();
                    Thread.sleep(250);
                }
                // Использовать блок synchronized для
                // проверки для проверки значений suspended и stopped
                synchronized (this) { // <-- В блоке проверяется значение  suspended и stopped
                    if (suspended) {
                        wait();
                    }
                    if (stopped) {
                        break;
                    }
                }
            }
        } catch (InterruptedException exc) {
            System.out.println("Поток " + thread.getName() + " прерван");
        }
        System.out.println("Поток " + thread.getName() + "  завершен");
    }

    // Останавливаем поток
    synchronized void mystop() {
        stopped = true;

        // Следующие операторы гарантируют,
        // что приостановленный поток может быть остановлен
        suspended = false;
        notify();
    }

    // Приостанавливает поток
    synchronized void mysuspend() {
        suspended = false;
        notify();
    }

    // Восстанавливает выполнение потока
    synchronized void myresume() {
        suspended = false;
        notify();

    }

}

class Suspend {
    public static void main(String[] args) {
        MyThread mt1 = MyThread.createAndStart("MyThread");
        try {
            Thread.sleep(1000); // Позволить потоку ob1 начать выполнение
            mt1.mysuspend();
            System.out.println("Приостановка потока");
            Thread.sleep(1000);
            mt1.myresume();
            System.out.println("Возобновление выполнения потока");
            Thread.sleep(1000);
            mt1.mysuspend();
            System.out.println("Приостановка потока");
            Thread.sleep(1000);
            mt1.mysuspend();
            System.out.println("Останов потока");
            mt1.mystop();
        } catch (InterruptedException exc) {
            System.out.println("Главный поток прерван");
        }
        // Ожидать завершения работы потока
        try {
            mt1.thread.join();
        } catch (InterruptedException exc) {
            System.out.println("Главный поток прерван");
        }
        System.out.println("Главный поток завершен");
    }
}