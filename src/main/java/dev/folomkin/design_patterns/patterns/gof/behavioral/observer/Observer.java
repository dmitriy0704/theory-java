package dev.folomkin.design_patterns.patterns.gof.behavioral.observer;

import java.util.List;

public interface Observer {
    void handleEvent(List<String> vacancies);
}
