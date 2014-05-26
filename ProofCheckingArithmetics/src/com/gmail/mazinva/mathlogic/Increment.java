package com.gmail.mazinva.mathlogic;

import com.gmail.mazinva.proofcheckingarithmetics.Main;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Increment extends AbstUnArithOper {

    public Increment(Term term) {
        super(term);
    }

    public String toString() {
        if (term instanceof AbstBinArithOper) {
            return ("(" + term.toString() + ")\'");
        }
        return (term.toString() + "\'");
    }

    public List<Pair> pathToFirstFreeEntry(String x) {
        List<Pair> pathFromCurPos = term.pathToFirstFreeEntry(x);

        if (pathFromCurPos != null) {
            ((LinkedList) pathFromCurPos).addFirst(new Pair(Main.INCREMENT, null));
            return pathFromCurPos;
        }

        return null;
    }

    public String toStringWithReplacedVar(Term term, String var) {
        if (this.term instanceof AbstBinArithOper) {
            return ("(" + this.term.toStringWithReplacedVar(term, var) + ")\'");
        }

        // Foo-case
        if (term instanceof AbstBinArithOper) {
            if (this.term instanceof Variable && this.term.toString().equals(var)) {
                return ("(" + this.term.toStringWithReplacedVar(term, var) + ")\'");
            }
        }
        return (this.term.toStringWithReplacedVar(term, var) + "\'");
    }

    public boolean isFreeToReplace(Term term, String var) {
        return this.term.isFreeToReplace(term, var);
    }
}
