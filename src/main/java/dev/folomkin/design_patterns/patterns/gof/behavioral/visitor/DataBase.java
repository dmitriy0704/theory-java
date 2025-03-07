package dev.folomkin.design_patterns.patterns.gof.behavioral.visitor;

public class DataBase implements ProjectElement {

    @Override
    public void beWrittenBy(Developer developer) {
        developer.create(this);
    }
}
