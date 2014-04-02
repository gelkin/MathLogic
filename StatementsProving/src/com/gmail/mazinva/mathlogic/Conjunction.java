package com.gmail.mazinva.mathlogic;

import com.gmail.mazinva.statementsproving.LogConnects;

import java.util.ArrayList;
import java.util.List;

public class Conjunction extends AbstractBinaryOperation {
    public Conjunction(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    public String toString() {
        return "(" + left.toString() + "&" + right.toString() + ")";
    }

    public boolean isTrue() {
        return (left.isTrue() && right.isTrue());
    }

    public List<String> getProof() throws MathLogicException {
        List<String> proof = new ArrayList<String>();
        proof.addAll(left.getProof());
        proof.addAll(right.getProof());

        boolean leftIsTrue = left.isTrue();
        boolean rightIsTrue = right.isTrue();
        if (leftIsTrue) {
            if (rightIsTrue) {
                proof.addAll(LogConnects.XandY(left.toString(), right.toString()));
            } else {
                proof.addAll(LogConnects.XandNotY(left.toString(), right.toString()));
            }
        } else {
            if (rightIsTrue) {
                proof.addAll(LogConnects.NotXandY(left.toString(), right.toString()));
            } else {
                proof.addAll(LogConnects.NotXandNotY(left.toString(), right.toString()));
            }
        }
        return proof;
    }
}
