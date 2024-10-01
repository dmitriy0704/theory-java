package dev.folomkin.core.collections_streamapi.sream_api;

import java.util.List;

public class Code {
    public static void main(String[] args) {
        List<String> list = List.of("one", "two", "thee");
        list.stream()
                .filter(s -> {
                    System.out.println("filter: " + s);
                    return s.length() <= 3;
                })
                .map(s1 -> {
                    System.out.println("map: " + s1);
                    return s1.toUpperCase();
                })
                .sorted()
                .forEach(x -> {
                    System.out.println("forEach: " + x);
                });
    }
}
