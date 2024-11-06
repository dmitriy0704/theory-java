package dev.folomkin.design_patterns.patterns.gof.behavioral.visitor;

public interface Developer {
    void create(ProjectClass projectClass);

    void create(DataBase dataBase);

    void create(Test test);
}
