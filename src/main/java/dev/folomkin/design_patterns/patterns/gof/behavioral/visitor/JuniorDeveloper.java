package dev.folomkin.design_patterns.patterns.gof.behavioral.visitor;

public class JuniorDeveloper implements Developer {

    @Override
    public void create(ProjectClass projectClass) {
        System.out.println("Writing poor code....");
    }

    @Override
    public void create(DataBase dataBase) {
        System.out.println("Drop database....");
    }

    @Override
    public void create(Test test) {
        System.out.println("Creating not reliable test...");
    }
}
