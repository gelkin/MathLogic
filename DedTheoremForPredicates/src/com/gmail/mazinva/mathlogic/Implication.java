package com.gmail.mazinva.mathlogic;

import com.gmail.mazinva.dedtheorempredicates.Main;

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

    public Expression getLeft() {
        return left;
    }

    public Expression getRight() {
        return right;
    }

    public List<Pair> pathToFirstFreeEntry(String x) {
        List<Pair> resultPath = new ArrayList<Pair>();
        List<Pair> pathFromCurPos;

        pathFromCurPos = left.pathToFirstFreeEntry(x);
        if (pathFromCurPos != null) {
            resultPath.add(new Pair(Main.IMPLICATION, "left"));
            resultPath.addAll(pathFromCurPos);
            return resultPath;
        }

        pathFromCurPos = right.pathToFirstFreeEntry(x);
        if (pathFromCurPos != null) {
            resultPath.add(new Pair(Main.IMPLICATION, "right"));
            resultPath.addAll(pathFromCurPos);
            return resultPath;
        }

        return null;
    }

    public String toStringWithReplacedVar(Term term, String var) {
        return "(" + left.toStringWithReplacedVar(term, var) + "->"
                + right.toStringWithReplacedVar(term, var) + ")";
    }

    public boolean isFreeToReplace(Term term, String var) {
        return (left.isFreeToReplace(term, var) && right.isFreeToReplace(term, var));
    }
}
