package com.gmail.mazinva.MathLogic;

public class Variable implements Expression {
    private String value;

    public Variable(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "(" + value + ")";
    }
}
