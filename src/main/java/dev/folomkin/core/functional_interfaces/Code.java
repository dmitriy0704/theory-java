package dev.folomkin.core.functional_interfaces;

import java.io.IOException;
import java.util.Objects;
import java.util.function.Consumer;

interface SomeFunc<T> {
    T func(T t);
}

class Code {
    public static void main(String[] args) {
        SomeFunc<String> s = (str) ->{
            System.out.println(str);
            return str;
        };

        SomeFunc<Integer> i = (num) -> num * num;
    }
}