package dev.folomkin.core.collections_streamapi.sream_api;

import java.util.List;import java.util.stream.Collectors;public class Code {
    public static void main(String[] args) {

        List<Integer> integers = List.of(1,2,3,4,5,6,7,8,9);
        // Пропустим все числа меньше 5 и возьмем оставшиеся
        List<Integer> result =  integers.stream()
                .dropWhile(n -> n <5)
                .toList();
        System.out.println(result);

        // -> [5, 6, 7, 8, 9]

    }
}
