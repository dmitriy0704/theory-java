package dev.folomkin.design_patterns.patterns.gof.behavioral.observer;

public class JobSearch {
    public static void main(String[] args) {
        JavaDeveloperJobSite jobSite = new JavaDeveloperJobSite();

        jobSite.addVacancy("First Java position");
        jobSite.addVacancy("Second Java position");

        Observer firstSubscriber = new Subscriber("First developer");
        Observer secondSubscriber = new Subscriber("Second developer");

        jobSite.addObserver(firstSubscriber);
        jobSite.addObserver(secondSubscriber);

        jobSite.addVacancy("Third Java position");

        jobSite.removeVacancy("First Java position");
    }
}
