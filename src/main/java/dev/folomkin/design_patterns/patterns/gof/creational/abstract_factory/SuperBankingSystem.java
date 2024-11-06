package dev.folomkin.design_patterns.patterns.gof.creational.abstract_factory;

import dev.folomkin.design_patterns.patterns.gof.creational.abstract_factory.banking.BankingTeamFactory;
import dev.folomkin.design_patterns.patterns.gof.creational.abstract_factory.website.WebSIteTeamFactory;

public class SuperBankingSystem {
    public static void main(String[] args) {
        ProjectTeamFactory projectTeamFactoryBanking = new BankingTeamFactory();
        ProjectTeamFactory projectTeamFactoryWebSite = new WebSIteTeamFactory();

        ProjectTeamFactory projectTeamFactory = new BankingTeamFactory();

        Developer developer = projectTeamFactory.createDeveloper();
        Tester tester = projectTeamFactory.createTester();
        ProjectManager projectManager = projectTeamFactory.createProjectManager();



    }
}
