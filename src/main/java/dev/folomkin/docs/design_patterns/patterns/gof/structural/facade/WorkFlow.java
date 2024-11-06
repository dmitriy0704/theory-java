package dev.folomkin.docs.design_patterns.patterns.gof.structural.facade;

public class WorkFlow {

    Job job = new Job();
    BugTracker bugTracker = new BugTracker();
    Developer developer = new Developer();

    public void solvedProblem() {
        job.doJob();
        bugTracker.startSprint();
        developer.doJobBeforeDeadline(bugTracker);
    }



}
