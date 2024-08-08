package dev.folomkin.core.multithrading.example.ch03;

class SumArray {
    private int sum;

    int sumArray(int[] nums) { // <-- Метод синхронизирован
        sum = 0; // сбросить sum;
        for (int num = 0; num < nums.length; num++) {
            sum += nums[num];
            System.out.println("Промежуточная сумма в потоке " +
                    Thread.currentThread().getName() + " равна " + sum);
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                System.out.println("Поток прерван");
            }
        }
        return sum;
    }
}

class MyThread implements Runnable {
    Thread thrd;
    static SumArray sa = new SumArray();
    int[] a;
    int answer;

    //Конструктор нового потока;
    MyThread(String name, int[] nums) {
        thrd = new Thread(this, name);
        a = nums;
    }

    //Фабричный метод, который создает и запускает поток
    public static MyThread createAndStartThread(String name, int[] nums) {
        MyThread thread = new MyThread(name, nums);
        thread.thrd.start();
        return thread;
    }

    //Точка входа в поток
    public void run() {
        int sum;
        System.out.println("Поток " + thrd.getName() + " запущен");
        synchronized (sa) {
            answer = sa.sumArray(a);
        }
        System.out.println("Сумма в потоке " + thrd.getName() + "равна " + answer);
        System.out.println("Поток " + thrd.getName() + " завершен");
    }
}

class Sync {
    public static void main(String[] args) {
        int[] a = {1, 2, 3, 4, 5};
        MyThread mt1 = MyThread.createAndStartThread("Child #1", a);
        MyThread mt2 = MyThread.createAndStartThread("Child #2", a);

        try {
            mt1.thrd.join();
            mt2.thrd.join();
        } catch (InterruptedException e) {
            System.out.println("Главный поток прерван");
        }
    }
}
