package com.gmail.mazinva.mathlogic;

import com.gmail.mazinva.proofcheckingarithmetics.Main;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Brackets extends AbstUnArithOper {

    public Brackets(Term term) {
        super(term);
    }

    public String toString() {
        return "(" + term.toString() + ")";
    }

    public List<Pair> pathToFirstFreeEntry(String x) {
        List<Pair> pathFromCurPos = term.pathToFirstFreeEntry(x);
        if (pathFromCurPos != null) {
            ((LinkedList) pathFromCurPos).addFirst(new Pair(Main.BRACKETS, null));
            return pathFromCurPos;
        } else {
            return null;
        }
    }

    public String toStringWithReplacedVar(Term term, String var) {
        return "(" + this.term.toStringWithReplacedVar(term, var) + ")";
    }

    public boolean isFreeToReplace(Term term, String var) {
        return this.term.isFreeToReplace(term, var);
    }
}
