package dev.folomkin.docs.design_patterns.patterns.gof.behavioral.visitor;

public class Project implements ProjectElement {
    ProjectElement[] projectElements;

    public Project() {
        this.projectElements = new ProjectElement[]{
                new ProjectClass(),
                new DataBase(),
                new Test()
        };
    }

    @Override
    public void beWrittenBy(Developer developer) {
        for (ProjectElement projectElement : projectElements) {
            projectElement.beWrittenBy(developer);
        }
    }
}
