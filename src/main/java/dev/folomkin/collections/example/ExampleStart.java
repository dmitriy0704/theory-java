package dev.folomkin.collections.example;

import java.util.LinkedHashSet;
import java.util.Set;

public class ExampleStart {

    public static void main(String[] args) {
        Book book1 = new Book(1, "Анна Каренина");
        Book book1Dup = new Book(1, "Анна Каренина");
        Book book2 = new Book(2, "Война и мир");
        Book book3 = new Book(3, "Java для чайников");

        Set<Book> set = new LinkedHashSet<>();
        set.add(book1);
        set.add(book1Dup);
        set.add(book1);
        set.add(book3);
        set.add(null);
        set.add(null);
        set.add(book2);
        System.out.println(set);
    }
}

class Book {
    private int id;
    private String title;
    public Book(int id, String title) {
        this.id = id;
        this.title = title;
    }

    @Override
    public String toString() {
        return id + ":" + title;
    }

    //Две книги равны, если у них одинаковые id;
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Book)) {
            return false;
        }
        return this.id == ((Book) o).id;
    }

    //Два объекта равны, если они имеют одинаковый хеш-код.
    @Override
    public int hashCode() {
        return id;
    }
}
