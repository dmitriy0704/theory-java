package dev.folomkin.collections.example;

import java.util.Arrays;
import java.util.Comparator;

public class ExampleStart {

    public static class StringComparator implements Comparator<String> {
        @Override
        public int compare(String s1, String s2) {
            return s1.compareToIgnoreCase(s2);
        }
    }

    public static class IntegerComparator implements Comparator<Integer> {
        @Override
        public int compare(Integer s1, Integer s2) {
            return s1 % 10 - s2 % 10;
        }
    }

    public static void main(String[] args) {
        Comparator<String> compStr = new StringComparator();
        Comparator<Integer> compInt = new IntegerComparator();

        // Сортировка и поиск в массиве строк String

        String[] array = {"Hello", "Hi", "HI", "hello"};
        Arrays.sort(array, compStr);
        System.out.println(Arrays.toString(array));

    }
}
