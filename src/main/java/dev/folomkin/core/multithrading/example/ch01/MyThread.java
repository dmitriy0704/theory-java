package dev.folomkin.core.multithrading.example.ch01;

// Создание нескольких потоков
class MyThread implements Runnable {
    Thread thrd; // <-- В thrd хранится ссылка на поток;

    // Конструктор нового потока;
    MyThread(String name) {
        thrd = new Thread(this, name); // <-- При создании потоку назначается имя;
    }

    // Фабричный метод, который создает и запускает поток;
    public static MyThread createAndStart(String name) {
        MyThread thread = new MyThread(name);
        thread.thrd.start(); // Запустить поток // <-- Начать выполнение потока;
        return thread;
    }


    //Точка входа в поток
    public void run() {                 //<-- Здесь потоки начинают выполняться
        System.out.println("Поток " + thrd.getName() + " запущен");

        try {
            for (int count = 0; count < 10; count++) {
                Thread.sleep(400);
                System.out.println("В потоке " + thrd.getName() + " значение count: " + count);
            }
        } catch (InterruptedException exception) {
            System.out.println("Поток " + thrd.getName() + " прерван");
        }
        System.out.println("Поток " + thrd.getName() + " завершен");
    }
}

class JoinThreads {
    public static void main(String[] args) {
        System.out.println("Главный поток запущен");

        //Создать и запускаем поток
        MyThread mt1 = MyThread.createAndStart("Child #1"); // <-- Создаем исполняемый объект
        MyThread mt2 = MyThread.createAndStart("Child #2"); // <-- Создаем исполняемый объект
        MyThread mt3 = MyThread.createAndStart("Child #3"); // <-- Создаем исполняемый объект

        try {
            mt1.thrd.join();     //<-- Ожидать пока не завершится указанный поток
            System.out.println("Поток Child #1 присоединен");
            mt2.thrd.join();    //<-- Ожидать пока не завершится указанный поток
            System.out.println("Поток Child #2 присоединен");
            mt3.thrd.join();    //<-- Ожидать пока не завершится указанный поток
            System.out.println("Поток Child #3 присоединен");
        } catch (InterruptedException e) {
            System.out.println("Главный поток прерван");
        }

        System.out.println("Главный поток завершен");
    }
}
