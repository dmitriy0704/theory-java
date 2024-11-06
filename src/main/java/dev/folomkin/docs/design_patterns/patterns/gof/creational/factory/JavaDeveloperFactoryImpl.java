package dev.folomkin.docs.design_patterns.patterns.gof.creational.factory;

public class JavaDeveloperFactoryImpl implements DeveloperFactory {
    @Override
    public Developer createDeveloper() {
        return new JavaDeveloper();
    }
}
