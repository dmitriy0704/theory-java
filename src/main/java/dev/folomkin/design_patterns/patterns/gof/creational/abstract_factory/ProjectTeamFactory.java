package dev.folomkin.design_patterns.patterns.gof.creational.abstract_factory;

public interface ProjectTeamFactory {
    Developer createDeveloper();
    Tester createTester();
    ProjectManager createProjectManager();
}
