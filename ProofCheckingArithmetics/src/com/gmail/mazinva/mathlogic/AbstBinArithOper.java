package com.gmail.mazinva.mathlogic;

import com.gmail.mazinva.proofcheckingarithmetics.Main;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstBinArithOper extends Term {
    protected Term left;
    protected Term right;

    public AbstBinArithOper(Term left, Term right) {
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
        return left.toStringWithReplacedVar(term, var) + operationSign
                + right.toStringWithReplacedVar(term, var);
    }
}
