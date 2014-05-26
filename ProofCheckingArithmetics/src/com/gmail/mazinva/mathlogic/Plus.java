package com.gmail.mazinva.mathlogic;

import com.gmail.mazinva.proofcheckingarithmetics.Main;

import java.util.ArrayList;
import java.util.List;

public class Plus extends AbstBinArithOper {

    public Plus(Term left, Term right) {
        super(left, right);
    }

    public String toString() {
        return left.toString() + "+" + right.toString();
    }

    public List<Pair> pathToFirstFreeEntry(String x) {
        return pathToFirstFreeEntryImpl(x, Main.PLUS);
    }

    public String toStringWithReplacedVar(Term term, String var) {
        return toStringWithReplacedVarImpl(term, var, "+");
    }

    public boolean isFreeToReplace(Term term, String var) {
        return (left.isFreeToReplace(term, var) && right.isFreeToReplace(term, var));
    }
}
