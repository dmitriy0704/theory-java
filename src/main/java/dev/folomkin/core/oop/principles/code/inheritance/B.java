package dev.folomkin.core.oop.principles.code.inheritance;

public class B extends A {
    int k;

    B(int a, int b, int c) {
        super(a, b);
        k = c;
    }

    void show(String msg) {
        System.out.println("подкласс: msg: " + k);
    }
}
