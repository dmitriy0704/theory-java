package dev.folomkin.core.multithrading;

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
    }
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
       synchronized (target) {
           target.call(msg);
       }
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
