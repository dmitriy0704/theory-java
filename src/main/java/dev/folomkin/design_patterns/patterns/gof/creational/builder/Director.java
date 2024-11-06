package dev.folomkin.design_patterns.patterns.gof.creational.builder;

public class Director {
    private WebSiteBuilder builder;

    public void setBuilder(WebSiteBuilder builder) {
        this.builder = builder;
    }

    WebSite buildWebSite() {
        builder.createWebSite();
        builder.buildName();
        builder.buildCms();
        builder.buildPrice();

        WebSite webSite = builder.getWebSite();
        return webSite;
    }
}
