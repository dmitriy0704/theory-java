package dev.folomkin.design_patterns.patterns.gof.creational.builder;

public class BuildWebSiteRunner_Client {
    public static void main(String[] args) {
        Director director = new Director();
        director.setBuilder(new VisitCartWebSiteBuilder());
        WebSite webSite = director.buildWebSite();
        System.out.println(webSite);


    }
}
