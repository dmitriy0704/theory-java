package dev.folomkin.pro.qa;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Demo {

    public static void main(String[] args) {

        System.out.println(

                Map.of(1, "one", 2, "two", 3, "three").entrySet().stream()

                        .filter(entry -> entry.getValue().length() > 3)

                        .map(Map.Entry::getKey)

                        .findFirst() .map(x -> "result = " + x)

                        .orElse("result = null")

        );
    }
}

