package dev.folomkin.core.oop.code;


import java.util.Arrays;
import java.util.function.Predicate;
import java.util.stream.Collectors;

class Code {
    public static void main(String[] args) {
        String[] arrayStr = {"as", "a", "the", " ", "d", "on", "and", ""};



        System.out.println(
                Arrays.stream(arrayStr)
                        .filter(s -> s.length() < 2)
                        .collect(Collectors.toList())
        );

    }
}
