package dev.folomkin.design_patterns.patterns.gof.structural.proxy;

public class ProjectRunner {
    public static void main(String[] args) {
        Project project = new ProxyProject("github");
project.run();
    }
}
