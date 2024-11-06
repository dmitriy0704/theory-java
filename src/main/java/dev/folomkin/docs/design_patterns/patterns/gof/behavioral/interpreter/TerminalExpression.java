package dev.folomkin.docs.design_patterns.patterns.gof.behavioral.interpreter;

public class TerminalExpression implements Expression {

    private String data;


    public TerminalExpression(String data) {
        this.data = data;
    }

    @Override
    public boolean interpret(String context) {
        if (data.equals(context)) {
            return true;
        }
        return false;
    }
}
