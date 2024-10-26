package dev.folomkin.design_patterns.patterns.gof.creational.abstract_factory.banking;

import dev.folomkin.design_patterns.patterns.gof.creational.abstract_factory.Tester;

public class QATester implements Tester {
    @Override
    public void testCode() {
        System.out.println("QA Tester testing code");
     }
}

