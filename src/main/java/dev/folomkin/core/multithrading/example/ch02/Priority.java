package dev.folomkin.core.multithrading.example.ch02;

public class Priority implements Runnable {
    int count;
    Thread thread;
    static boolean stop = false;
    static String currentName;

    //Конструктор нового потока
    Priority(String name) {
        thread = new Thread(this, name);
        count = 0;
        currentName = name;
    }

    //Точка входа в поток
    public void run() {
        System.out.println("Поток " + thread.getName() + " запущен");

        do {
            count++;
            if (currentName.compareTo(thread.getName()) != 0) {
                currentName = thread.getName();
                System.out.println("В потоке " + currentName);
            }
        } while (stop == false && count < 10000000);
        stop = true;
        System.out.println("\nПоток " + thread.getName() + " завершен");
    }
}

class PriorityDemo {
    public static void main(String[] args) {
        Priority mt1 = new Priority("Высокий приоритет");
        Priority mt2 = new Priority("Низкий приоритет");
        Priority mt3 = new Priority("Нормальный приоритет #1");
        Priority mt4 = new Priority("Нормальный приоритет #2");
        Priority mt5 = new Priority("Нормальный приоритет #3");

        //Назначить приоритеты
        mt1.thread.setPriority(Thread.NORM_PRIORITY + 2);
        mt2.thread.setPriority(Thread.NORM_PRIORITY - 2);
        // Для остальных потоков приоритеты остаются прежними - NORM_PRIORITY

        //Запустить потоки
        mt1.thread.start();
        mt2.thread.start();
        mt3.thread.start();
        mt4.thread.start();
        mt5.thread.start();

        try {
            mt1.thread.join();
            mt2.thread.join();
            mt3.thread.join();
            mt4.thread.join();
            mt5.thread.join();
        } catch (InterruptedException e) {
            System.out.println("Главный поток прерван");
        }
        System.out.println("\nПоток с высоким приоритетом досчитал до " + mt1.count);
        System.out.println("\nПоток с низким приоритетом досчитал до " + mt2.count);
        System.out.println("\nПоток 1 с нормальным приоритетом досчитал до " + mt3.count);
        System.out.println("\nПоток 2 с нормальным приоритетом досчитал до " + mt4.count);
        System.out.println("\nПоток 3 с нормальным приоритетом досчитал до " + mt5.count);
    }
}
