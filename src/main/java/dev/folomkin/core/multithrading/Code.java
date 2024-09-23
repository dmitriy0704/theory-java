package dev.folomkin.core.multithrading;

public class Code {
    public static void main(String[] args) throws InterruptedException {
        Thread t = Thread.currentThread();
        System.out.println("Текущий поток: " + t);
        // Изменяем имя потока:
        t.setName("My Thread");
        System.out.println("Имя потока после изменения : " + t);
        try {
            for (int i = 5; i > 0; i--) {
                System.out.println(i);
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            System.out.println("Главный поток прерван");
        }
    }
}
