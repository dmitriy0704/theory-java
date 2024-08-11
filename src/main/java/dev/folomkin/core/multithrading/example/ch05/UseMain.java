package dev.folomkin.core.multithrading.example.ch05;

public class UseMain {
    public static void main(String[] args) {
        Thread mainThread = Thread.currentThread(); // Получить главный поток
        System.out.println("thread name: " + mainThread.getName());
        System.out.println("main thread priority: " + mainThread.getPriority());
        System.out.println();

        //Установить имя и приоритет
        mainThread.setName("main thread #1");
        mainThread.setPriority(Thread.NORM_PRIORITY);
    }
}
