package dev.folomkin.java.oop.code;

public class Client implements Callback{
    public void call(int p) {
        System.out.println("Client called with " + p);
    }
}
