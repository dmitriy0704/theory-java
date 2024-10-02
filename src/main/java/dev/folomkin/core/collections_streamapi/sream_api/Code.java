package dev.folomkin.core.collections_streamapi.sream_api;

import java.util.Arrays;
import java.util.stream.Stream;


public class Code {
    public static void main(String[] args) {

        Map<String, Set<Worker>> map2 = workers.stream()
                .collect(
                        Collectors.groupingBy(
                                Worker::getPosition, Collectors.toSet()
                        )
                );
    }
}
