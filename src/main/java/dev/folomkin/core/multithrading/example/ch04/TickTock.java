package dev.folomkin.core.multithrading.example.ch04;

public class TickTock {
    String state; // <-- Хранит состояние часов;

    synchronized void tick(boolean running) {
        if (!running) { // Остановить часы;
            state = "ticket";
            notify(); // Уведомить ожидающие потоки;
            return;
        }
        System.out.println("Tick");
        state = "Ticket"; // Установить текущее состояние в "после 'тик'";
        notify(); // Позволить методу tock() выполняться: tick() уведомляет tock();
        try {
            while (!state.equals("ticket")) {
                wait(); // ожидать завершения работы метода tock(): tick() ожидает tock();
            }
        } catch (InterruptedException e) {
            System.out.println("Поток прерван");
        }
    }


    synchronized void tock(boolean running) {
        if (!running) { // остановить часы
            state = "tocked";
            notify(); // уведомить ожидающие потоки return;
            return;
        }
        System.out.println("Tock");
        state = "Tocked"; // Установить текущее состояние в "после 'так'";
        notify(); // позволить методу tick() выполняться    <-- tock()  уведомляет tick();
        try {
            while (!state.equals("ticked")) {
                wait(); // Ожидает завершения выполнения метода tick():  tock() ожидает tick();
            }
        } catch (InterruptedException e) {
            System.out.println("Поток прерван.");
        }
    }
}

class MyThread implements Runnable {
    Thread thrd;
    TickTock ttOb;

    // Конструктор нового потока
    MyThread(String name, TickTock tt) {
        thrd = new Thread(this, name);
        ttOb = tt;
    }

    //Фабричный метод, который создает и запускает поток
    public static MyThread createAndStart(String name, TickTock tt) {
        MyThread myThrd = new MyThread(name, tt);
        myThrd.thrd.start();
        return myThrd;
    }

    public void run() {
        if (thrd.getName().compareTo("Tick") == 0) {
            for (int i = 0; i < 5; i++) {
                ttOb.tick(true);
            }
            ttOb.tick(false);
        } else {
            for (int i = 0; i < 5; i++) {
                ttOb.tock(true);
            }
            ttOb.tock(false);
        }
    }
}

class ThreadCom {
    public static void main(String[] args) {
        TickTock tt = new TickTock();
        MyThread mt = new MyThread("Tick", tt);
        MyThread mt2 = new MyThread("Tock", tt);
        try {
            mt.thrd.join();
            mt2.thrd.join();
        } catch (InterruptedException e) {
            System.out.println("Поток прерван");
        }
    }
}