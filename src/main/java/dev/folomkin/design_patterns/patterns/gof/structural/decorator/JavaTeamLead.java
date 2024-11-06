package dev.folomkin.design_patterns.patterns.gof.structural.decorator;

public class JavaTeamLead extends DeveloperDecorator {

    public JavaTeamLead(Developer developer) {
        super(developer);
    }

    public String sendWeekReport() {
        return " Week report. ";
    }

    @Override
    public String makeJob() {
        return super.makeJob() + sendWeekReport();
    }
}
