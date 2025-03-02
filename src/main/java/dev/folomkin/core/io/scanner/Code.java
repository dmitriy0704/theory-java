package dev.folomkin.core.io.scanner;

import java.util.Scanner;

public class Code {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter a string: ");;
        int x = scanner.nextInt();
        int[] arr = new int[x];
        for (int i = 0; i < x; i++) {
            System.out.println("Please enter an integer: ");
        }
        System.out.println("Вы ввели" + x);
        scanner.close();
    }
}
