package dev.folomkin.pro.collection;

import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

public class Code {
    public static void main(String[] args) {

        List<String> list = Arrays.asList("a","b","c");
        ListIterator<String> iterator = list.listIterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }
        while (iterator.hasPrevious()) {
            System.out.println(iterator.previous());
        }
    }
}
