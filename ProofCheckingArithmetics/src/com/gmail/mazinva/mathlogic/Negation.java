package com.gmail.mazinva.mathlogic;

import com.gmail.mazinva.proofcheckingarithmetics.Main;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Negation extends AbstractUnaryOperation {
    public Negation(Expression expression) {
        super(expression);
    }

    @Override
    public String toString() {
        return ("!" + expression.toString());
    }

    public List<Pair> pathToFirstFreeEntry(String x) {
        List<Pair> resultPath = new ArrayList<Pair>();
        List<Pair> pathFromCurPos = expression.pathToFirstFreeEntry(x);

        if (pathFromCurPos != null) {
            ((LinkedList) pathFromCurPos).addFirst(new Pair(Main.NEGATION, null));
            return pathFromCurPos;
        }

        return null;
    }

    public String toStringWithReplacedVar(Term term, String var) {
        return "!" + expression.toStringWithReplacedVar(term, var);
    }

    public boolean isFreeToReplace(Term term, String var) {
        return expression.isFreeToReplace(term, var);
    }
}
