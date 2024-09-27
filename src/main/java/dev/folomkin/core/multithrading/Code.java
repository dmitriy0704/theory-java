package dev.folomkin.core.multithrading;

// Создание потока
class NewThread implements Runnable {
    Thread t;

    NewThread() {
        // Создание потока
        t = new Thread(this, "Demo thread");
        System.out.println("Дочерний поток: " + t);
    }

    public static NewThread createNewThread() {
        NewThread thread = new NewThread();
        thread.t.start();
        return thread;
    }

    // Это точка входа для потока.
    public void run() {
        try {
            for (int i = 15; i > 0; i--) {
                System.out.println("Дочерний поток" + " · " + i);
                Thread.sleep(500);
            }
        } catch (InterruptedException е) {
            System.out.println("Дочерний поток" + " прерван.");
        }
        System.out.println("Дочерний поток" + " завершается. ");
    }
}

public class Code {
    public static void main(String[] args) {
        NewThread nt = NewThread.createNewThread();

        try {
            for (int i = 5; i > 0; i--) {
                System.out.println("Главный поток: " + i);
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            System.out.println("Главный поток прерван");
            e.printStackTrace();
        }
    }
}
