package com.gmail.mazinva.mathlogic;


import com.gmail.mazinva.dedtheorempredicates.Main;

import java.util.ArrayList;
import java.util.List;

public class Exist extends AbstractQuantifier {
    public Exist(Expression expression) {
        super(expression);
    }

    public Exist(Expression expression, String connectingVaraible) {
        super(expression);
        var = connectingVaraible;
    }

    @Override
    public String toString() {
        return "?" + var + expression.toString();
    }

    public List<Pair> pathToFirstFreeEntry(String x) {
        return pathToFirstFreeEntryImpl(x, Main.EXIST);
    }

    public String toStringWithReplacedVar(Term term, String var) {
        return toStringWithReplacedVarImpl(term, var, "?");
    }
}
