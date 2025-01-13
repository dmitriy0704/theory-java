package dev.folomkin.core.exceptions.example;

public class Code {
    public static void main(String[] args) {
        int a, d;

        try {
            d = 0;
            a = 42 / d;
            System.out.println("Не выведется");
        } catch (ArithmeticException e) {
            System.out.println("Деление на ноль");
        }
        System.out.println("После catch block");
    }
}
