package dev.folomkin.design_patterns.patterns.gof.creational.abstract_factory.website;

import dev.folomkin.design_patterns.patterns.gof.creational.abstract_factory.ProjectManager;

public class WebSitePM implements ProjectManager {
    @Override
    public void manageProject() {
        System.out.println("PM manage Banking Project");
    }
}
