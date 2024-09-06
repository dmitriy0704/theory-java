package dev.folomkin.core.oop.code;


import java.util.Arrays;
import java.util.function.Consumer;

class Code {
    public static void main(String[] args) {
        String str = "as a- the-d -on and";
        String regex = "\\s";
        Consumer<String> consumer = s -> System.out.println(Arrays.toString(s.split(regex)));


        consumer.accept(str);

        int[] arrInt = {1, 2, 3};
        Arrays.stream(arrInt)
                .forEach(i -> System.out.print(i * 2 + ", "));

    }
}
