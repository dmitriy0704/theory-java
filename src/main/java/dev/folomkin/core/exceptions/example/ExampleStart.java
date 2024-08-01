package dev.folomkin.core.exceptions.example;


import java.io.IOException;

class NonIntResultException extends Exception {
    int n;
    int d;
    NonIntResultException(int i, int j) {
        n = i;
        d = j;
    }
}


class ExampleStart {

    public static char prompt(String str) throws IOException {
        System.out.print(str + " : ");
        return (char) System.in.read();
    }

    public static void main(String[] args) {
        char ch;
        try {
            ch = prompt("Введите букву: ");
        } catch (IOException e) {
            System.out.println("Возникло исключение ввода-вывода");
            ch = 'X';
        }
        System.out.println("Вы нажали клавишу " + ch);
    }
}


