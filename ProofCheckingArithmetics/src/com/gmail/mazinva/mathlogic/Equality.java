package com.gmail.mazinva.mathlogic;

import com.gmail.mazinva.proofcheckingarithmetics.Main;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Equality extends AbstractBinaryOperation {

    public Equality(Term left, Term right) {
        super(left, right);
    }

    public String toString() {
        return "(" + left.toString() + "=" + right.toString() + ")";
    }

    public Expression getLeft() {
        return left;
    }

    public Expression getRight() {
        return right;
    }

    public List<Pair> pathToFirstFreeEntry(String x) {
        List<Pair> pathFromCurPos;

        pathFromCurPos = left.pathToFirstFreeEntry(x);
        if (pathFromCurPos != null) {
            ((LinkedList) pathFromCurPos).addFirst(new Pair(Main.EQUALITY, "left"));
            return pathFromCurPos;
        }

        pathFromCurPos = right.pathToFirstFreeEntry(x);
        if (pathFromCurPos != null) {
            ((LinkedList) pathFromCurPos).addFirst(new Pair(Main.EQUALITY, "right"));
            return pathFromCurPos;
        }

        return null;
    }

    public String toStringWithReplacedVar(Term term, String var) {
        return "(" + left.toStringWithReplacedVar(term, var) + "="
                + right.toStringWithReplacedVar(term, var) + ")";
    }

    public boolean isFreeToReplace(Term term, String var) {
        return (left.isFreeToReplace(term, var) && right.isFreeToReplace(term, var));
    }
}
