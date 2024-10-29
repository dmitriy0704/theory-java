package dev.folomkin.design_patterns.patterns.gof.creational.prototype;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Project implements Copyable {
    private int id;
    private String projectName;
    private String sourceCode;

    public Project(int id, String projectName, String sourceCode) {
        this.id = id;
        this.projectName = projectName;
        this.sourceCode = sourceCode;
    }

    @Override
    public Object copy() {
        return new Project(id, projectName, sourceCode);
    }
}
