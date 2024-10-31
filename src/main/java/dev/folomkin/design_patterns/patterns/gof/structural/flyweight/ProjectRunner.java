package dev.folomkin.design_patterns.patterns.gof.structural.flyweight;

import java.util.ArrayList;
import java.util.List;

public class ProjectRunner {
    public static void main(String[] args) {
        DeveloperFactory factory = new DeveloperFactory();

        List<Developer> developers = new ArrayList<>();

        developers.add(factory.getDeveloperBySpecialization("Java"));
        developers.add(factory.getDeveloperBySpecialization("Java"));
        developers.add(factory.getDeveloperBySpecialization("Java"));
        developers.add(factory.getDeveloperBySpecialization("Java"));
        developers.add(factory.getDeveloperBySpecialization("Java"));
        developers.add(factory.getDeveloperBySpecialization("Java"));
        developers.add(factory.getDeveloperBySpecialization("Cpp"));
        developers.add(factory.getDeveloperBySpecialization("Cpp"));
        developers.add(factory.getDeveloperBySpecialization("Cpp"));
        developers.add(factory.getDeveloperBySpecialization("Cpp"));
        developers.add(factory.getDeveloperBySpecialization("Cpp"));
        developers.add(factory.getDeveloperBySpecialization("Cpp"));

        for (Developer developer : developers) {
            developer.writeCode();
        }
    }
}
