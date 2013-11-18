package com.gmail.mazinva.MathLogic;

public class Conjunction extends AbstractBinaryOperation {
    public Conjunction(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    public String toString() {
        return "(" + left.toString() + "&" + right.toString() + ")";
    }
}
