package dev.folomkin.docs.design_patterns.patterns.gof.creational.prototype;

public class VersionControlRunner_Client {
    public static void main(String[] args) {
        Project master = new Project(1, "SuperProject",
                "SourceCode sourceCode = new SourceCode();");
        System.out.println(master);

        ProjectFactory factory = new ProjectFactory(master);
        Project masterClone = (Project) factory.cloneProject();

        System.out.println("\n=================================\n");
        System.out.println(masterClone);
    }
}
