package dev.folomkin.design_patterns.patterns.gof.behavioral.command;

public class Database {
    public void insert() {
        System.out.println("Inserting a new record");
    }

    public void update() {
        System.out.println("Updating a record");
    }

    public void select() {
        System.out.println("Querying a record");
    }

    public void delete() {
        System.out.println("Deleting a record");
    }
}
