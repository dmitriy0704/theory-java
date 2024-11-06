package dev.folomkin.design_patterns.patterns.gof.behavioral.memento;

public class SaveProjectRunner {

    public static void main(String[] args) {
        Project project = new Project();
        GithubRepo githubRepo = new GithubRepo();

        System.out.println("Create project. Version 1.0");
        project.setVersionAndDate("1.0");

        System.out.println(project);

        System.out.println("saving project to Github...");
        githubRepo.setSave(project.save());

        System.out.println("Updating project. Version 1.1");

        System.out.println("Writing poor code...");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        project.setVersionAndDate("Version 1.1");
        System.out.println(project);

        System.out.println("something went wrong...");

        System.out.println("Rolling back project to Version 1.0");
        project.load(githubRepo.getSave());

        System.out.println("Project after rollback: ");

        System.out.println(project);
    }
}
