package dev.folomkin.design_patterns.patterns.gof.behavioral.chain;

import lombok.Setter;

public abstract class Notifier {
    private int priority;
    @Setter
    private Notifier nextNotifier;

    public Notifier(int priority) {
        this.priority = priority;
    }

    public void notifyManager(String message, int level) {
        if (level >= priority) {
            write(message);
        }

        if (nextNotifier != null) {
            nextNotifier.notifyManager(message, level);
        }
    }

    protected abstract void write(String message);
}
