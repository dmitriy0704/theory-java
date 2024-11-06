package dev.folomkin.docs.design_patterns.patterns.gof.structural.decorator;

public class Task {
    public static void main(String[] args) {
        Developer developer = new JavaTeamLead(
                new JavaSeniorDeveloper(new JavaDeveloper())
        );

        System.out.println(developer.makeJob());
    }
}
