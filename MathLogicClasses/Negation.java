package com.gmail.mazinva.MathLogic;

public class Negation extends AbstractUnaryOperation {
    public Negation(Expression expression) {
        super(expression);
    }

    @Override
    public String toString() {
        return "(!" + expression.toString() + ")";
    }
}
