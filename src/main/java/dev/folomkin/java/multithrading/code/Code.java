package dev.folomkin.java.multithrading.code;

public class Code {
    public static void main(String[] args) {
        WalkThread w = new WalkThread();
        Thread t = new Thread(new TalkThread());
        w.start();
        t.start();
    }
}

class WalkThread extends Thread {
    public void run() {
        try {
            for (int i = 0; i < 10; i++) {
                System.out.println("Walking " + i);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } finally {
            System.out.println("Finished: " + Thread.currentThread().getName());
        }

    }
}

class TalkThread implements Runnable {
    @Override
    public void run() {
        try {
            for (int i = 0; i < 10; i++) {
                System.out.println("Talking " + i);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } finally {
            System.out.println("Talking " + Thread.currentThread().getName());
        }
    }
}