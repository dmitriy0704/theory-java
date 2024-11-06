package dev.folomkin.docs.design_patterns.patterns.gof.structural.decorator;

public class JavaDeveloper implements Developer {

    @Override
    public String makeJob() {
        return " Write Java code. ";
    }
}
