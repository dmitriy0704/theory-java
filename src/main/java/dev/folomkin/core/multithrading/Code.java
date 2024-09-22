package dev.folomkin.core.multithrading;

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
        System.out.println(Code.t1.getState());
    }
}