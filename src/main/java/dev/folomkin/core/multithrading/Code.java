package dev.folomkin.core.multithrading;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeUnit;

class CommonResource implements AutoCloseable {
    private FileWriter fileWriter;

    public CommonResource(String file) throws IOException {
        fileWriter = new FileWriter(file, true);
    }

    public synchronized void writing(String info, int i) {
        try {
            fileWriter.append(info + i);
            System.out.print(info + i);
            TimeUnit.MILLISECONDS.sleep(new Random().nextInt(500));
            fileWriter.append("->" + info.charAt(0) + i + " ");
            System.out.print("->" + info.charAt(0) + i + " ");
        } catch (IOException | InterruptedException e) {
            System.err.print(e);
        }
    }

    @Override
    public void close() throws IOException {
        if (fileWriter != null) {
            fileWriter.close();
        }
    }
}


class UseFileThread extends Thread {
    private CommonResource resource;

    public UseFileThread(String name, CommonResource resource) {
        super(name);
        this.resource = resource;
    }

    public void run() {
        for (int i = 0; i < 5; i++) {
            resource.writing(this.getName(), i); // synchronized method call
        }
    }
}


public class Code {
    public static void main(String[] args) {

        try (CommonResource resource = new CommonResource("src/main/resources/test.txt")) {
            UseFileThread thread1 = new UseFileThread("First", resource);
            UseFileThread thread2 = new UseFileThread("Second", resource);
            thread1.start();
            thread2.start();
            TimeUnit.SECONDS.sleep(5);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
