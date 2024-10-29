package dev.folomkin.design_patterns.patterns.gof.creational.builder;

public class WebSiteBuilder {
    public static void main(String[] args) {

        WebSite webSite = new WebSite();
        webSite.setName("Visit Card");
        webSite.setCms("WordPress");
        webSite.setPrice(500);

        System.out.println(webSite);
    }
}
