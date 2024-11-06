package dev.folomkin.docs.design_patterns.patterns.gof.structural.proxy;

public class RealProject implements Project {

    private String url;

    public RealProject(String url) {
        this.url = url;
        load();
    }

    public void load() {
        System.out.println("Loading from " + url + "...");
    }

    @Override
    public void run() {
        System.out.println("Running project ..." + url);
    }
}
