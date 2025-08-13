package dev.folomkin.java.oop.principles.code.inheritance;

public class A {
    int i, j;

    A(int a, int b) {
        i = a;
        j = b;
    }

    void show(){
        System.out.println("i = " + i + ", j = " + j);
    }
}
