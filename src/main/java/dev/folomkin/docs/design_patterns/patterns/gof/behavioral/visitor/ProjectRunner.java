package dev.folomkin.docs.design_patterns.patterns.gof.behavioral.visitor;

public class ProjectRunner {
    public static void main(String[] args) {
        Project project = new Project();
        Developer junior = new JuniorDeveloper();
        Developer senior = new SeniorDeveloper();

        System.out.println("Junior in Action");
        project.beWrittenBy(junior);

        System.out.println("======================================");

        System.out.println("Senior in Action");
        project.beWrittenBy(senior);
    }
}
