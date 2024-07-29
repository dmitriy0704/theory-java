package dev.folomkin.theory.collections.example;

import java.util.*;

public class ExampleStart {
    public static void main(String[] args) {

        Queue<String> collection = new LinkedList<>();
        collection.add("A");
        collection.add("C");
        collection.add("C");
        System.out.println(collection.element());
        System.out.println(collection);
    }
}
