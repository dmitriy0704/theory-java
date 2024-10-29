package dev.folomkin.design_patterns.patterns.gof.structural.bridge;

public class BankSystem extends Program {

    protected BankSystem(Developer developer) {
        super(developer);
    }

    @Override
    public void developerProgram() {
        System.out.println("Bank System in progress...");
        developer.writeCode();
    }
}
