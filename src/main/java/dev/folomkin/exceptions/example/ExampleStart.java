package dev.folomkin.exceptions.example;

public class ExampleStart {
    public static void main(String[] args) {
        ExceptionExample.example(7, 7);
    }
}

class ExceptionExample {
    static void example(int i, int y) {
        try {
            int[] arr = new int[5];
            arr[i] = y;
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }
    }
}
