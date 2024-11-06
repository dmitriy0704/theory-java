package dev.folomkin.design_patterns.patterns.gof.creational.builder;

public abstract class WebSiteBuilder {
    WebSite webSite;

    void createWebSite() {
        webSite = new WebSite();
    }

    abstract void buildName();

    abstract void buildCms();

    abstract void buildPrice();

    WebSite getWebSite() {
        return webSite;
    }
}
