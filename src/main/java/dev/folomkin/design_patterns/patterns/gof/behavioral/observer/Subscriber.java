package dev.folomkin.design_patterns.patterns.gof.behavioral.observer;

import java.util.List;

public class Subscriber implements Observer {
    String name;

    public Subscriber(String name) {
        this.name = name;
    }

    @Override
    public void handleEvent(List<String> vacancies) {
        System.out.println("Dear " + name + "\n. We have changes to vacancies"
                           + vacancies + "\n=============================\n");
    }
}
