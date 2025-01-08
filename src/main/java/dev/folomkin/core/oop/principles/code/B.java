package dev.folomkin.core.oop.principles.code;

public class B extends A {
    int i;

    B(int a, int b) {
        super.i = a;
        i = b;
    }

    void show() {
        System.out.println("суперкласс: " + super.i);
        System.out.println("подкласс: " + i);
    }
}
