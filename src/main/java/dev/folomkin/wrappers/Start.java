package dev.folomkin.wrappers;

public class Start {
    public static void main(String[] args) {

        // Сравнение классов:

        Integer value = Integer.valueOf(42);
        Integer value2 = Integer.valueOf(43);

        System.out.println(value2.compareTo(value));

        Integer value3 = Integer.parseInt("42");
        System.out.println(value3);



    }
}
