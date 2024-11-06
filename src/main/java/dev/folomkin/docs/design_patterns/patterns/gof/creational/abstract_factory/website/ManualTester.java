package dev.folomkin.docs.design_patterns.patterns.gof.creational.abstract_factory.website;

import dev.folomkin.docs.design_patterns.patterns.gof.creational.abstract_factory.Tester;

public class ManualTester implements Tester {
    @Override
    public void testCode() {
        System.out.println("QA Tester testing code");
     }
}

