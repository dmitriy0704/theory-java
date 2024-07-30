package dev.folomkin.core.oop.example;

class Post<K extends Number, V>{
    public K postId;
    public V message;
}

public class ExampleStart {
    public static void main(String[] args) {
        Post<Short, String> post = new Post<Short, String>();

    }
}
