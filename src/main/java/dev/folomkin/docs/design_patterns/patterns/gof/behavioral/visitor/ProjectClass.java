package dev.folomkin.docs.design_patterns.patterns.gof.behavioral.visitor;

public class ProjectClass implements ProjectElement {
    @Override
    public void beWrittenBy(Developer developer) {
        developer.create(this);
    }
}
