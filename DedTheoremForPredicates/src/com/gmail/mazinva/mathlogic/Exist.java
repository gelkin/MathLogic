package com.gmail.mazinva.mathlogic;


import com.gmail.mazinva.dedtheorempredicates.Main;

import java.util.ArrayList;
import java.util.List;

public class Exist extends AbstractUnaryOperation {
    String var = "#";

    public Exist(Expression expression) {
        super(expression);
    }

    public Exist(Expression expression, String connectingVaraible) {
        super(expression);
        var = connectingVaraible;
    }

    public String getVar() {
        return  var;
    }

    @Override
    public String toString() {
        return "?" + var + expression.toString();
    }

    public Expression getExpression() {
        return expression;
    }

    public List<Pair> pathToFirstFreeEntry(String x) {
        List<Pair> resultPath = new ArrayList<Pair>();
        List<Pair> pathFromCurPos = expression.pathToFirstFreeEntry(x);

        if (!var.equals(x) && pathFromCurPos != null) {
            resultPath.add(new Pair(Main.EXIST, var));
            resultPath.addAll(pathFromCurPos);
            return resultPath;
        }

        return null;
    }

    public String toStringWithReplacedVar(Term term, String var) {
        if (this.var.equals(var)) {
            return toString();
        } else {
            return "?" + this.var + expression.toStringWithReplacedVar(term, var);
        }
    }

    public boolean isFreeToReplace(Term term, String var) {
        if (this.var.equals(term.toString()) && expression.pathToFirstFreeEntry(var) != null) {
            return false;
        } else {
            return expression.isFreeToReplace(term, var);
        }
    }
}
