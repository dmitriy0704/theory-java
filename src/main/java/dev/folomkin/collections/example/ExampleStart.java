package dev.folomkin.collections.example;

import java.util.*;

public class ExampleStart {
    public static void main(String[] args) {

        Stack<String> stack = new Stack<>();
        stack.push("A");
        stack.push("B");
        stack.push("C");
        stack.push("D");
        stack.push("E");

        System.out.println(stack);
        System.out.println(stack.peek());
        System.out.println(stack);
    }
}
