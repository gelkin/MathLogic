package com.gmail.mazinva.mathlogic;

import com.gmail.mazinva.proofcheckingarithmetics.Main;

import java.util.ArrayList;
import java.util.List;

public class Equality extends AbstractBinaryOperation {

    public Equality(Term left, Term right) {
        super(left, right);
    }

    public String toString() {
        return "(" + left.toString() + "=" + right.toString() + ")";
    }

    public List<Pair> pathToFirstFreeEntry(String x) {
        List<Pair> resultPath = new ArrayList<Pair>();
        List<Pair> pathFromCurPos;

        pathFromCurPos = left.pathToFirstFreeEntry(x);
        if (pathFromCurPos != null) {
            resultPath.add(new Pair(Main.EQUALITY, "left"));
            resultPath.addAll(pathFromCurPos);
            return resultPath;
        }

        pathFromCurPos = right.pathToFirstFreeEntry(x);
        if (pathFromCurPos != null) {
            resultPath.add(new Pair(Main.EQUALITY, "right"));
            resultPath.addAll(pathFromCurPos);
            return resultPath;
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
