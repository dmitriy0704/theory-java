package dev.folomkin.docs.design_patterns.patterns.gof.behavioral.strategy;

public class Reading implements Activity {
    @Override
    public void justDoIt() {
        System.out.println("Reading book...");
    }
}
