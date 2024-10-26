package dev.folomkin.design_patterns.patterns.gof.creational.abstract_factory.website;

import dev.folomkin.design_patterns.patterns.gof.creational.abstract_factory.Developer;
import dev.folomkin.design_patterns.patterns.gof.creational.abstract_factory.ProjectManager;
import dev.folomkin.design_patterns.patterns.gof.creational.abstract_factory.ProjectTeamFactory;
import dev.folomkin.design_patterns.patterns.gof.creational.abstract_factory.Tester;

public class WebSIteTeamFactory implements ProjectTeamFactory {
    @Override
    public Developer createDeveloper() {
        return new PhpDeveloper();
    }

    @Override
    public Tester createTester() {
        return new ManualTester();
    }

    @Override
    public ProjectManager createProjectManager() {
        return new WebSitePM();
    }
}
