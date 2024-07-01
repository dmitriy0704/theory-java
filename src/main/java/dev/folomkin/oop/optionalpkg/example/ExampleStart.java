package dev.folomkin.oop.optionalpkg.example;

import java.util.Optional;

public class ExampleStart {
    public static void main(String[] args) {

        String name = "OptTest";
        Optional<String> opt = Optional.of(name);
        opt.ifPresent(System.out::println);


    }
}
