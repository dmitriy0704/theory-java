package dev.folomkin.docs.design_patterns.patterns.gof.behavioral.visitor;

public class Test implements ProjectElement {
    @Override
    public void beWrittenBy(Developer developer) {
        developer.create(this);
    }
}
