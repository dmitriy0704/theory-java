package dev.folomkin.design_patterns.patterns.gof.behavioral.template;

public class NewsPage extends WebSiteTemplate {

    @Override
    public void showContent() {
        System.out.println("NewsPage");
    }
}
