package dev.folomkin.design_patterns.patterns.gof.structural.bridge;

public class JavaDeveloper implements Developer {
    @Override
    public void writeCode() {
        System.out.println("Java Developer write Java code");
    }
}
