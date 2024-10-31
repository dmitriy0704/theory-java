package dev.folomkin.design_patterns.patterns.gof.behavioral.chain;

public class SimpleReportNotifier extends Notifier{
    public SimpleReportNotifier(int priority) {
        super(priority);
    }

    @Override
    protected void write(String message) {
System.out.println("Notifying using simple report: " + message);
    }
}
