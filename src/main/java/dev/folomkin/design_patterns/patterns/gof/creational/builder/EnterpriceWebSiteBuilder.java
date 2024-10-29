package dev.folomkin.design_patterns.patterns.gof.creational.builder;

public class EnterpriceWebSiteBuilder extends WebSiteBuilder {
    @Override
    void buildName() {
        webSite.setName("Enterprice Web Site");
    }

    @Override
    void buildCms() {
        webSite.setCms(Cms.WORDPRESS);
    }

    @Override
    void buildPrice() {
        webSite.setPrice(10000);
    }
}
