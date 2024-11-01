package dev.folomkin.design_patterns.patterns.gof.behavioral.visitor;

public class SeniorDeveloper implements Developer {

    @Override
    public void create(ProjectClass projectClass) {
        System.out.println("Rewriting code after Junior....");
    }

    @Override
    public void create(DataBase dataBase) {
        System.out.println("Fixing database....");
    }

    @Override
    public void create(Test test) {
        System.out.println("Creating reliable test.... Fixing test....");
    }
}
