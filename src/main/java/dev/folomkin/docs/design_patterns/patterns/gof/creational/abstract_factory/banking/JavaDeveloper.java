package dev.folomkin.docs.design_patterns.patterns.gof.creational.abstract_factory.banking;

import dev.folomkin.docs.design_patterns.patterns.gof.creational.abstract_factory.Developer;

public class JavaDeveloper implements Developer {

    @Override
    public void writeCode() {
        System.out.println("Java developer write code");
    }
}
