package dev.folomkin.design_patterns.patterns.gof.behavioral.template;

public class WelcomePage extends WebSiteTemplate {


    @Override
    public void showContent() {
        System.out.println("Welcome to Folomkin Design Patterns");
    }
}
