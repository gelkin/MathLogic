package com.gmail.mazinva.mathlogic;

import com.gmail.mazinva.dedtheorempredicates.Main;
import java.util.ArrayList;
import java.util.List;

public class Forall extends AbstractQuantifier {

    public Forall(Expression expression) {
        super(expression);
    }

    public Forall(Expression expression, String connectingVar) {
        super(expression);
        var = connectingVar;
    }

    @Override
    public String toString() {
        return "@" + var + expression.toString();
    }

    public List<Pair> pathToFirstFreeEntry(String x) {
        return pathToFirstFreeEntryImpl(x, Main.FORALL);
    }

    public String toStringWithReplacedVar(Term term, String var) {
        return toStringWithReplacedVarImpl(term, var, "@");
    }
}
