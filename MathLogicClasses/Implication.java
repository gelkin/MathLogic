package com.gmail.mazinva.MathLogic;

public class Implication extends AbstractBinaryOperation {
    public Implication(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    public String toString() {
        return "(" + left.toString() + "->" + right.toString() + ")";
    }
}
