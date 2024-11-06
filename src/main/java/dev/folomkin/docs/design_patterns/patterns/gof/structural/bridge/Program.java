package dev.folomkin.docs.design_patterns.patterns.gof.structural.bridge;

public abstract class Program {
    protected Developer developer;

    public Program(Developer developer) {
        this.developer = developer;
    }

    public abstract void developerProgram();
}
