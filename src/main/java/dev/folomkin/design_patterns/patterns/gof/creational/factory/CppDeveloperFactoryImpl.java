package dev.folomkin.design_patterns.patterns.gof.creational.factory;

public class CppDeveloperFactoryImpl implements DeveloperFactory {
    @Override
    public Developer createDeveloper() {
        return new CppDeveloper();
    }
}
