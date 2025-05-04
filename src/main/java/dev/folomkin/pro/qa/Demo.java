package dev.folomkin.pro.qa;


import java.util.Objects;


class Programmer {
    private final String position;
    private final int salary;

    protected Programmer(String position, int salary) {
        this.position = position;
        this.salary = salary;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Programmer that = (Programmer) o;

        if (salary != that.salary) return false;
        return Objects.equals(position, that.position);
    }

}

public class Demo extends Programmer {

    protected Demo(String position, int salary) {
        super(position, salary);
    }

    public static void main(String[] args) {
        Programmer programmer1 = new Programmer("Junior", 300);
        Programmer programmer2 = new Programmer("Junior", 300);

        //false
        System.out.println(programmer1 == programmer2);
        //true
        System.out.println(programmer1.equals(programmer2));

    }
}

