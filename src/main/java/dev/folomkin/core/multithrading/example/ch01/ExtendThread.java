package dev.folomkin.core.multithrading.example.ch01;

class MyThreadExtThread extends Thread {
    MyThreadExtThread(String name) {
        super(name);
    }

    //Точка входа в поток;
    public void run() {

      System.out.println("Поток " + getName() + " запущен");

        try {
            for (int count = 0; count < 10; count++) {
                Thread.sleep(400);
                System.out.println("Поток: " + getName() + " count() " + count);
            }
        } catch (InterruptedException exception) {
            System.out.println("Поток " + getName() + "прерван");
        }
        System.out.println("Поток " + getName() + "Завершен");
    }

    public static MyThreadExtThread createAndStart(String name) {
        MyThreadExtThread thread = new MyThreadExtThread(name);
        thread.start();
        return thread;
    }
}

class ExtendThread {
    public static void main(String[] args) {
//        System.out.println("Главный поток запушен");
//        MyThreadExtThread mt = new MyThreadExtThread("Child # 1");

        MyThreadExtThread mte = MyThreadExtThread.createAndStart("Child #1");
//        mt.start();

        for (int i = 0; i < 50; i++) {
            System.out.println(".");

            try {
                Thread.sleep(100);
                System.out.println("Главный поток запущен");
            } catch (InterruptedException exception) {
                System.out.println("Главный поток прерван");
            }
        }
        System.out.println("Главный поток завершен");
    }
}
