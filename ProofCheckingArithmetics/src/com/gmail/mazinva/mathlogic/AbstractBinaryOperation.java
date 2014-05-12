package com.gmail.mazinva.mathlogic;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractBinaryOperation implements Expression {
    protected Expression left;
    protected Expression right;

    public AbstractBinaryOperation(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    public abstract List<Pair> pathToFirstFreeEntry(String x);
    public abstract String toStringWithReplacedVar(Term term, String var);

    protected List<Pair> pathToFirstFreeEntryImpl(String x, String operationType) {
        List<Pair> resultPath = new ArrayList<Pair>();
        List<Pair> pathFromCurPos;

        pathFromCurPos = left.pathToFirstFreeEntry(x);
        if (pathFromCurPos != null) {
            resultPath.add(new Pair(operationType, "left"));
            resultPath.addAll(pathFromCurPos);
            return resultPath;
        }

        pathFromCurPos = right.pathToFirstFreeEntry(x);
        if (pathFromCurPos != null) {
            resultPath.add(new Pair(operationType, "right"));
            resultPath.addAll(pathFromCurPos);
            return resultPath;
        }

        return null;
    }

    protected String toStringWithReplacedVarImpl(Term term, String var, String operationSign) {
        return "(" + left.toStringWithReplacedVar(term, var) + operationSign
                + right.toStringWithReplacedVar(term, var) + ")";
    }
}
