package dev.folomkin.design_patterns.patterns.gof.creational.abstract_factory.banking;

import dev.folomkin.design_patterns.patterns.gof.creational.abstract_factory.Developer;
import dev.folomkin.design_patterns.patterns.gof.creational.abstract_factory.ProjectManager;
import dev.folomkin.design_patterns.patterns.gof.creational.abstract_factory.ProjectTeamFactory;
import dev.folomkin.design_patterns.patterns.gof.creational.abstract_factory.Tester;

public class BankingTeamFactory implements ProjectTeamFactory {
    @Override
    public Developer createDeveloper() {
        return new JavaDeveloper();
    }

    @Override
    public Tester createTester() {
        return new QATester();
    }

    @Override
    public ProjectManager createProjectManager() {
        return new BankingPM();
    }
}
