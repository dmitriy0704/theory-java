package dev.folomkin.core.multithrading;


class State implements Runnable {
    public void run(){}
}

public class Code {
    public static void main(String[] args) {
        Runnable runnable = new State();
        Thread thread = new Thread(runnable);
        thread.start();
        System.out.println("Thread state: " + thread.getState());
    }
}
