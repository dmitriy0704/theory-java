package dev.folomkin.docs.design_patterns.patterns.gof.behavioral.chain;

public class SMSNotifier extends Notifier {
    public SMSNotifier(int priority) {
        super(priority);
    }

    @Override
    protected void write(String message) {
        System.out.println("sending sms: " + message);
    }
}
