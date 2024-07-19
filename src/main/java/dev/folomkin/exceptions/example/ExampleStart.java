package dev.folomkin.exceptions.example;


import java.io.IOException;

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


