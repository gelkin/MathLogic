package com.gmail.mazinva.MathLogic;

import com.gmail.mazinva.StatementsProving.LogConnects;

import java.util.ArrayList;
import java.util.List;

public class Negation extends AbstractUnaryOperation {
    public Negation(Expression expression) {
        super(expression);
    }

    @Override
    public String toString() {
        return ("!" + expression.toString());
    }

    public boolean isTrue() {
        return (!expression.isTrue());
    }

    public List<String> getProof() throws MathLogicException {
        List<String> proof = new ArrayList<String>();
        proof.addAll(expression.getProof());
        if (expression.isTrue()) {
            proof.addAll(LogConnects.negX(expression.toString()));
        } else {
            proof.addAll(LogConnects.negNotX(expression.toString()));
        }
        return proof;
    }
}
