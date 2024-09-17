package dev.folomkin.core.collections_streamapi.sream_api;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class Code {
    public static void main(String[] args) {
        List<String> strings = List.of("Java Python Go JavaScript PH".split("\\s+"));
        Map<Boolean, List<String>> boolLength = strings.stream()
                .collect(Collectors.partitioningBy(s -> s.length() < 3));
        System.out.println(boolLength);
    }
}
