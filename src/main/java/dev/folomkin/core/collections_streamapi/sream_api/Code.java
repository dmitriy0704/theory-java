package dev.folomkin.core.collections_streamapi.sream_api;

import java.util.List;

public class Code {
    public static void main(String[] args) {

        List<String> strings = List.of("Java Python Java JavaScript PHP".split("\\s+"));

        strings.stream()
                .distinct()
                .forEach(System.out::println);
    }
}
