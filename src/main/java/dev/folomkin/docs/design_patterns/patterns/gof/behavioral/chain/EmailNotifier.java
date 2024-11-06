package dev.folomkin.docs.design_patterns.patterns.gof.behavioral.chain;

public class EmailNotifier extends Notifier {

    public EmailNotifier(int priority) {
        super(priority);
    }

    @Override
    protected void write(String message) {
        System.out.println("Sending email: " + message);
    }
}
