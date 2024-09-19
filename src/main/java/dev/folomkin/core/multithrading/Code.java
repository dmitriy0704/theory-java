package dev.folomkin.core.multithrading;

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
                    System.out.println("State: " + Thread.currentThread().getState());
                    Thread.sleep(1);
                } catch (InterruptedException exception) {
                    exception.printStackTrace();
                }
            }
        } finally {
            System.out.println(Thread.currentThread().getName() + " is over");
            System.out.println("State: " + Thread.currentThread().getState());
        }
    }
}


public class Code {
    public static void main(String[] args) {
        WalkThread walk = new WalkThread(); // -> Новый объект thread
        walk.start(); // -> старт потока

        Thread talk = new Thread(new TalkThread()); // -> Создание потока
        talk.start(); // -> старт потока


        // Или лямбда
        (new Thread(() ->
                System.out.println(
                        Thread.currentThread().getName() + " is over")
        )).start(); // -> Новый объект thread и его старт




    }
}
