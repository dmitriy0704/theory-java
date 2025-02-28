package dev.folomkin.collection.code;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Code {
    public static void main(String[] args) {
        String[] arr = new String[]{"a", "b", "c", "d", "e", "f", "g", "h"};

        List<String> list = new ArrayList<>(Arrays.asList(arr));
        list.add(null);
        System.out.println(list);
    }
}
