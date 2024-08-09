package dev.folomkin.core.multithrading.example.ch05;

public class MyThread implements Runnable{
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

    }

}