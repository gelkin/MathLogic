package com.gmail.mazinva.mathlogic;

import com.gmail.mazinva.proofcheckingarithmetics.Main;

import java.util.ArrayList;
import java.util.LinkedList;
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
        List<Pair> pathFromCurPos;

        pathFromCurPos = left.pathToFirstFreeEntry(x);
        if (pathFromCurPos != null) {
            ((LinkedList) pathFromCurPos).addFirst(new Pair(operationType, "left"));
            return pathFromCurPos;
        }

        pathFromCurPos = right.pathToFirstFreeEntry(x);
        if (pathFromCurPos != null) {
            ((LinkedList) pathFromCurPos).addFirst(new Pair(operationType, "right"));
            return pathFromCurPos;
        }

        return null;
    }

    protected String toStringWithReplacedVarImpl(Term term, String var, String operationSign) {
        return left.toStringWithReplacedVar(term, var) + operationSign
                + right.toStringWithReplacedVar(term, var);
    }
}
