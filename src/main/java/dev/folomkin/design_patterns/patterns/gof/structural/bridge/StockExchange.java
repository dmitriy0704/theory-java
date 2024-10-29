package dev.folomkin.design_patterns.patterns.gof.structural.bridge;

public class StockExchange extends Program {
    public StockExchange(Developer developer) {
        super(developer);
    }

    @Override
    public void developerProgram() {
        System.out.println("Stock Exchange in progress...");
        developer.writeCode();
    }
}
