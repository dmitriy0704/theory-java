package dev.folomkin.core.multithrading.code;

// Создание второго потока
class NewThread implements Runnable {
    Thread t;

    NewThread() {
        // -> Создать новый второй поток
        t = new Thread(this, "New Thread");
        System.out.println("Дочерний поток " + t);
    }

    //Точка входа для второго потока
    @Override
    public void run() {
        try {
            for (int i = 0; i < 10; i++) {
                System.out.println("Дочерний поток");
                Thread.sleep(500);
            }
        } catch (InterruptedException e) {
            System.out.println("Дочерний поток прерван");
        }
        System.out.println("Завершение дочернего потока");
    }
}

public class Code {
    public static void main(String[] args) {
        NewThread thread = new NewThread();
        thread.t.start();

        try {
            for (int i = 5; i > 0; i--) {
                System.out.println("Главный поток: " + i);
                Thread.sleep(1000);
            }
        } catch (InterruptedException e){
            System.out.println("Главный поток прерван");
        }
        System.out.println("Главный поток завершен");
    }
}
