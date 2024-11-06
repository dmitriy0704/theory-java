package dev.folomkin.docs.design_patterns.patterns.gof.behavioral.interpreter;

import javax.naming.Context;

public interface Expression {
    boolean interpret(String context);
}
