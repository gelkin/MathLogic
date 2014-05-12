package com.gmail.mazinva.mathlogic;

import com.gmail.mazinva.proofcheckingarithmetics.Main;

import java.util.ArrayList;
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
            resultPath.add(new Pair(Main.NEGATION, null));
            resultPath.addAll(pathFromCurPos);
            return resultPath;
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
