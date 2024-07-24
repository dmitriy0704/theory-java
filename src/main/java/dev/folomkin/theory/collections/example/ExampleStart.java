package dev.folomkin.theory.collections.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class ExampleStart {
    public static void main(String[] args) {

        List<String> collection = new ArrayList<>();
        collection.add("A");
        collection.add("B");
        collection.add("C");
        System.out.println(collection.indexOf("C"));
        System.out.println(collection);
    }
}
