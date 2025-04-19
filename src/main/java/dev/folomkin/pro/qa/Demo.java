package dev.folomkin.pro.qa;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;


public class Demo {
    public static void main(String[] args) {

        List<String> list = new ArrayList<>();
        list.add("A");
        list.add("B");
        list.add("C");
        list.add("D");

        Stream<String> stream = list.stream();
        stream.forEach(System.out::println);

        Collection<String> collection = list;
        collection.parallelStream().forEach(System.out::println);
    }
}

