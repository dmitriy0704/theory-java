package dev.folomkin.core.multithrading.code;

// Создание второго потока
class NewThread implements Runnable {
    String name;
    Thread t;

    NewThread(String threadName) {
        // -> Создать новый второй поток
        name = threadName;
        t = new Thread(this, name);
        System.out.println("Новый поток " + t);
    }

    //Точка входа для второго потока
    @Override
    public void run() {
        try {
            for (int i = 0; i < 10; i++) {
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
        NewThread thread1 = new NewThread("Thread One");
        NewThread thread2 = new NewThread("Thread Two");
        NewThread thread3 = new NewThread("Thread Three");
        // -> Запустить потоки
        thread1.t.start();
        thread2.t.start();
        thread3.t.start();

        System.out.println("Поток one работает " + thread1.t.isAlive());
        System.out.println("Поток two работает " + thread2.t.isAlive());
        System.out.println("Поток three работает " + thread3.t.isAlive());

        // -> Ожидать завершения потоков

        try {
            System.out.println("Ожидание завершения потоков");
            thread1.t.join();
            thread2.t.join();
            thread3.t.join();
        } catch (InterruptedException e) {
            System.out.println("Главный поток прерван");
        }

        System.out.println("Поток one работает " + thread1.t.isAlive());
        System.out.println("Поток two работает " + thread2.t.isAlive());
        System.out.println("Поток three работает " + thread3.t.isAlive());


        System.out.println("Главный поток завершен");
    }
}
