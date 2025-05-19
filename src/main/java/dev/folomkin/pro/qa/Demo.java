package dev.folomkin.pro.qa;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Demo {

    public static void main(String[] args) {

        List<? extends Number> numbers = List.of(1.0, 2.0, 3.0);
        Number n = numbers.get(1);
        System.out.println(n);


        List<? super Integer> integers = new ArrayList<>();
        integers.add(1);

        Number m = integers.get(1);
        System.out.println(m);


    }
}

