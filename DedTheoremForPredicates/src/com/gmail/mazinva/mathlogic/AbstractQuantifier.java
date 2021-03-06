package com.gmail.mazinva.mathlogic;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractQuantifier implements Expression {
    protected String var = "#";
    protected Expression expression;

    public AbstractQuantifier(Expression expression) {
        this.expression = expression;
    }

    public String getVar() {
        return var;
    }

    public Expression getExpression() {
        return expression;
    }

    public abstract List<Pair> pathToFirstFreeEntry(String x);
    public abstract String toStringWithReplacedVar(Term term, String var);

    protected List<Pair> pathToFirstFreeEntryImpl(String x, String operationType) {
        List<Pair> resultPath = new ArrayList<Pair>();
        List<Pair> pathFromCurPos = expression.pathToFirstFreeEntry(x);

        if (!var.equals(x) && pathFromCurPos != null) {
            resultPath.add(new Pair(operationType, var));
            resultPath.addAll(pathFromCurPos);
            return resultPath;
        }

        return null;
    }

    protected String toStringWithReplacedVarImpl(Term term, String var, String operationSign) {
        if (this.var.equals(var)) {
            return toString();
        } else {
            return operationSign + this.var + expression.toStringWithReplacedVar(term, var);
        }
    }

    public boolean isFreeToReplace(Term term, String var) {
        if (this.var.equals(var)) {
            return true;
        }
        if (this.var.equals(term.toString()) && expression.pathToFirstFreeEntry(var) != null) {
            return false;
        } else {
            return expression.isFreeToReplace(term, var);
        }
    }

}
