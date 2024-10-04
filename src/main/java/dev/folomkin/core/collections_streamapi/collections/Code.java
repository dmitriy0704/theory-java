package dev.folomkin.core.collections_streamapi.collections;

import java.util.*;

public class Code {
    public static void main(String[] args) {
        final List<String> names = new ArrayList<>(List.of("Java", "Kotlin", "PHP"));


        Map<String, Integer> map = new HashMap<>();
        map.put("Java", 1);
        map.put("Kotlin", 2);
        map.put("PHP", 3);

        System.out.println(map.keySet());


        String res =toUpperCase("Методы");
        System.out.println(res);
    }

    public static String toUpperCase(String string) {
        return string.toUpperCase();
    }
}