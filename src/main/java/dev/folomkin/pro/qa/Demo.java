package dev.folomkin.pro.qa;


import java.util.Objects;

class Demo1 {
    int i;
    int j;

    Demo1(int i, int j) {
        this.i = i;
        this.j = j;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Demo1 demo1 = (Demo1) o;
        return i == demo1.i && j == demo1.j;
    }

    @Override
    public int hashCode() {
        return Objects.hash(i, j);
    }
}

class Demo2 {
    int i;
    int j;

    Demo2(int i, int j) {
        this.i = i;
        this.j = j;
    }
}

public class Demo {
    public static void main(String[] args) {


        Demo1 demo1 = new Demo1(3, 4);
        Demo1 demo2 = new Demo1(3, 4);

        System.out.println(demo1.equals(demo2));
    }
}

