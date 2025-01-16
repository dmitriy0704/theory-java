package dev.folomkin.core.multithrading.code;

import java.util.concurrent.Callable;

class Callme {
    synchronized void call(String msg) {
        System.out.print("[ " + msg);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            System.out.println("Thread was interrupted");
        }
        System.out.print(" ]");
    }
}

class Caller implements Runnable {
    String msg;
    Callme target;
    Thread t;

    public Caller(Callme targ, String s) {
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

public class Synch {
    public static void main(String[] args) {
        Callme target = new Callme();
        Caller caller = new Caller(target, "Hello");
        Caller caller2 = new Caller(target, "Synchronized");
        Caller caller3 = new Caller(target, "World");

        // -> Запуск потоков
        caller.t.start();
        caller2.t.start();
        caller3.t.start();

        // -> Ожидание окончания работы потоков

        try {
            caller.t.join();
            caller2.t.join();
            caller3.t.join();
        } catch (InterruptedException e) {
            System.out.println("Thread was interrupted");
        }
    }
}

