package com.gmail.mazinva.mathlogic;

import com.gmail.mazinva.proofcheckingarithmetics.Main;

import java.util.ArrayList;
import java.util.List;

public class Multiply extends AbstBinArithOper {

    public Multiply(Term left, Term right) {
        super(left, right);
    }

    public String toString() {
        String res = "";
        if (left instanceof Plus) {
            res += "(" + left.toString() + ")";
        } else {
            res += left.toString();
        }
        res += "*";
        if (right instanceof Plus) {
            res += "(" + right.toString() + ")";
        } else {
            res += right.toString();
        }
        return res;
    }

    public List<Pair> pathToFirstFreeEntry(String x) {
        return pathToFirstFreeEntryImpl(x, Main.MULTIPLY);
    }

    public String toStringWithReplacedVar(Term term, String var) {
        return toStringWithReplacedVarImpl(term, var, "*");
    }

    public boolean isFreeToReplace(Term term, String var) {
        return (left.isFreeToReplace(term, var) && right.isFreeToReplace(term, var));
    }
}
