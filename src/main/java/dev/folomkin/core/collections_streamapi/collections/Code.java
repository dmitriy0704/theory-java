package dev.folomkin.core.collections_streamapi.collections;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

class Book {
    private int bookId;
    private String title;
    private String author;
}

public class Code {
    public static void main(String[] args) {
        Queue<String> queue = new PriorityQueue<>(Comparator.reverseOrder());
        queue.offer("J");
        queue.offer("A");
        queue.offer("V");
        queue.offer("A");

        while(!queue.isEmpty()) {
            System.out.println(queue.poll());
        }
    }
}