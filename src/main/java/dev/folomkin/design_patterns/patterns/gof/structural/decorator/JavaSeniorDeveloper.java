package dev.folomkin.design_patterns.patterns.gof.structural.decorator;

public class JavaSeniorDeveloper extends DeveloperDecorator {

    public JavaSeniorDeveloper(Developer developer) {
        super(developer);
    }

    public String makeCodeReview() {
        return " Make code review.";
    }

    @Override
    public String makeJob() {
        return super.makeJob() + makeCodeReview();
    }
}
