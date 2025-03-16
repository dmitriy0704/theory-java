package dev.folomkin.core.object;

import java.util.Objects;

public class Code extends Programmer {
    protected Code(String position, int salary) {
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
        Programmer programmer = (Programmer) o;
        if (salary != programmer.salary) return false;
        return Objects.equals(position, programmer.position);
    }


    @Override
    public int hashCode() {
        int result = position != null ? position.hashCode() : 0;
        result = 31 * result + salary;
        return result;
    }
}