package dev.folomkin.design_patterns.patterns.gof.structural.decorator;

public class DeveloperDecorator implements Developer {

    private Developer developer;

    public DeveloperDecorator(Developer developer) {
        this.developer = developer;
    }

    @Override
    public String makeJob(){
        return developer.makeJob();
    }
}
