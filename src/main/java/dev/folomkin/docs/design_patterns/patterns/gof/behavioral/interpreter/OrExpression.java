package dev.folomkin.docs.design_patterns.patterns.gof.behavioral.interpreter;

public class OrExpression implements Expression {

    Expression e1, e2;

    public OrExpression(Expression e1, Expression e2) {
        this.e1 = e1;
        this.e2 = e2;
    }

    @Override
    public boolean interpret(String context) {
        return e1.interpret(context) || e2.interpret(context) ;
    }
}
