package dev.folomkin.design_patterns.patterns.gof.behavioral.chain;

public class BugTracker {
    public static void main(String[] args) {
        Notifier reportNotifier = new SimpleReportNotifier(Priority.ROUTINE);
        Notifier emailNotifier = new EmailNotifier(Priority.IMPORTANT);
        Notifier smsNotifier = new SMSNotifier(Priority.ASAP);

        reportNotifier.setNextNotifier(emailNotifier);
        emailNotifier.setNextNotifier(smsNotifier);

        reportNotifier.notifyManager("Everything is Ok ", Priority.ROUTINE);
        reportNotifier.notifyManager("Something went wrong ", Priority.IMPORTANT);
        reportNotifier.notifyManager("Houston, we've had a problem here!!! ", Priority.ASAP);



    }
}
