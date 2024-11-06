package dev.folomkin.design_patterns.patterns.gof.structural.facade;

public class Developer {

    public void doJobBeforeDeadline(BugTracker bugTracker) {
        if(bugTracker.isActiveSprint()){
            System.out.println("Developer solving problem...");
        } else {
            System.out.println("Developer reading Habr...");
        }
    }
}
