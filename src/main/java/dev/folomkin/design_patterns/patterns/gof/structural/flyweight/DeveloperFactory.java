package dev.folomkin.design_patterns.patterns.gof.structural.flyweight;

import java.util.HashMap;
import java.util.Map;

public class DeveloperFactory {
    private static final Map<String, Developer> developers = new HashMap<>();

    public Developer getDeveloperBySpecialization(String specialization) {
        Developer developer = developers.get(specialization);
        if (developer == null) {
            developer = switch (specialization) {
                case "Java" -> new JavaDeveloper();
                case "Cpp" -> new CppDeveloper();
                default -> throw new IllegalStateException(
                        "Unexpected value: " + specialization
                );
            };
            developers.put(specialization, developer);
        }
        return developer;
    }
}
