package dev.folomkin.core.multithrading.example.ch04;

public class TickTock {
    String state; // <-- Хранит состояние часов;

    synchronized void tick(boolean running) {
        if (!running) { // Остановить часы;
            state = "ticket";
            notify(); // Уведомить ожидающие потоки;
            return;
        }
        System.out.println("Tick");
        state = "Ticket"; // Установить текущее состояние в "после 'тик'";
        notify(); // Позволить методу tock() выполняться: tick() уведомляет tock();
        try {
            while (!state.equals("ticket")) {
                wait(); // ожидать завершения работы метода tock(): tick() ожидает tock();
            }
        } catch (InterruptedException e) {
            System.out.println("Поток прерван");
        }
    }


    synchronized void tock(boolean running) {
        if (!running) { // остановить часы
            state = "tocked";
            notify(); // уведомить ожидающие потоки return;
            return;
        }
        System.out.println("Tock");
        state = "Tocked"; // Установить текущее состояние в "после 'так'";
        notify(); // позволить методу tick() выполняться    <-- tock()  уведомляет tick();
        try{
            while (!state.equals("ticked")) {
                wait(); // Ожидает завершения выполнения метода tick():  tock() ожидает tick();
            }
        }
        catch (InterruptedException e) {
            System.out.println("Поток прерван.");
        }
    }
}

class