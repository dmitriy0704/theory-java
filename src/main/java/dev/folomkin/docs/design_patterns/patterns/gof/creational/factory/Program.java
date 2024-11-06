package dev.folomkin.docs.design_patterns.patterns.gof.creational.factory;

public class Program {
    public static void main(String[] args) {
        DeveloperFactory developerFactory = createDeveloperBySpeciality("Java");
        Developer developer = developerFactory.createDeveloper();
        developer.writeCode();
    }

    static DeveloperFactory createDeveloperBySpeciality(String speciality) {
        return switch (speciality) {
            case "Java" -> new JavaDeveloperFactoryImpl();
            case "cpp" -> new CppDeveloperFactoryImpl();
            default ->
                    throw new IllegalStateException("Unexpected value: " + speciality);
        };
    }
}
