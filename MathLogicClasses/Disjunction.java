package com.gmail.mazinva.MathLogic;

public class Disjunction extends AbstractBinaryOperation {
    public Disjunction(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    public String toString() {
        return "(" + left.toString() + "|" + right.toString() + ")";
    }
}
