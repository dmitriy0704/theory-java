package dev.folomkin.docs.design_patterns.patterns.gof.creational.builder;

public class VisitCartWebSiteBuilder extends WebSiteBuilder {
    @Override
    void buildName() {
        webSite.setName("Visit Cart Web Site");
    }

    @Override
    void buildCms() {
        webSite.setCms(Cms.WORDPRESS);
    }

    @Override
    void buildPrice() {
        webSite.setPrice(500);
    }
}
