package dev.folomkin.collection.code;

import java.util.*;
import java.util.stream.Stream;

public class Code {
    public static void main(String[] args) {
        ArrayList<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(10);
        list.add(3);
        list.add(3);
        list.add(4);
        list.add(5);
        list.add(6);
        list.add(7);

        Integer[] arr = list.toArray(new Integer[list.size()]);

        System.out.println(Arrays.toString(arr));


    }
}
