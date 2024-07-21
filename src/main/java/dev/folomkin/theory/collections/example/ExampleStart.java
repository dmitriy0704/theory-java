package dev.folomkin.theory.collections.example;

import java.util.ArrayList;
import java.util.Collection;

public class ExampleStart {
    public static void main(String[] args) {

        Collection<String> collection = new ArrayList<>();
        collection.add("A");
        collection.add("B");
        collection.add("C");

        System.out.println(collection.add("D"));
    }
}
