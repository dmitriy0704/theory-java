package dev.folomkin.core.collections_streamapi.sream_api;

import java.util.ArrayList;
import java.util.List;

class Book {
    private String title;
    private String author;
    private int pages;

    public Book(String title, String author, int pages) {
        this.title = title;
        this.author = author;
        this.pages = pages;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    @Override
    public String toString() {
        return "Book{" +
                "title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", pages=" + pages +
                '}';
    }
}


public class Code {
    public static void main(String[] args) {
        List<Book> books = new ArrayList<>();
        books.add(new Book("Spring in Actions", "David", 480));
        books.add(new Book("Spring Pro", "Rob", 560));
        books.add(new Book("Spring QuickStart", "Spilke", 520));

        List<Book> listBooks = books
                .stream()
                .filter(b -> b.getPages() < 500)
                .map(b -> {
                    b.setTitle(b.getTitle().toUpperCase() );
                    return b;
                })
                .toList();
        listBooks.forEach(System.out::println);

        List<String> strings = List.of("as a the d on and".split("\\s"));
        strings.stream()
                .map(s -> s.length())
                .forEach(System.out::println);
    }
}
