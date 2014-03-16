package com.gmail.mazinva.MathLogic;

import com.gmail.mazinva.StatementsProving.LogConnects;

import java.util.ArrayList;
import java.util.List;

public class Implication extends AbstractBinaryOperation {
    public Implication(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    public String toString() {
        return "(" + left.toString() + "->" + right.toString() + ")";
    }

    public boolean isTrue() {
        return (!left.isTrue() || right.isTrue());
    }

    public List<String> getProof() throws MathLogicException {
    List<String> proof = new ArrayList<String>();
        proof.addAll(left.getProof());
        proof.addAll(right.getProof());

        boolean leftIsTrue = left.isTrue();
        boolean rightIsTrue = right.isTrue();
        if (leftIsTrue) {
            if (rightIsTrue) {
                proof.addAll(LogConnects.XthenY(left.toString(), right.toString()));
            } else {
                proof.addAll(LogConnects.XthenNotY(left.toString(), right.toString()));
            }
        } else {
            if (rightIsTrue) {
                proof.addAll(LogConnects.NotXthenY(left.toString(), right.toString()));
            } else {
                proof.addAll(LogConnects.NotXthenNotY(left.toString(), right.toString()));
            }
        }
        return proof;
    }
}
