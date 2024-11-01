package dev.folomkin.design_patterns.patterns.gof.behavioral.template;

public abstract class WebSiteTemplate {
    public void showPage() {
        System.out.println("Header");
        showContent();
        System.out.println("Footer");
    }

    public abstract void showContent();
}
