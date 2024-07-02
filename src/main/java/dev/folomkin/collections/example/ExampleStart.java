package dev.folomkin.collections.example;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExampleStart {
    public static void main(String[] args) {

        List<Integer> list = new ArrayList<>();

        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);

        Integer y = 3;
        Iterator<Integer> iterator = list.iterator();
//        while (iterator.hasNext()) {
//            System.out.println(iterator.next());
//        }

        for (Integer i : list) {
            System.out.println(i);
        }


        System.out.println(list);
    }
}
