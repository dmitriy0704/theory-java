package dev.folomkin.collections.example;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExampleStart {
    public static void main(String[] args) {

        List<String> list = new ArrayList<String>();
        list.add("Alpha");
        list.add("Beta");
        list.add("Charlie");
        System.out.println(list);
        Iterator<String> iterator = list.iterator();
        while (iterator.hasNext()) {
            String str = iterator.next();
            System.out.println(str);
        }

    }
}
