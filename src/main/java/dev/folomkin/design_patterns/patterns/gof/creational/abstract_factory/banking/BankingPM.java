package dev.folomkin.design_patterns.patterns.gof.creational.abstract_factory.banking;

import dev.folomkin.design_patterns.patterns.gof.creational.abstract_factory.ProjectManager;

public class BankingPM implements ProjectManager {
    @Override
    public void manageProject() {
        System.out.println("PM manage Banking Project");
    }
}
