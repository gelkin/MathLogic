package com.gmail.mazinva.mathlogic;

import com.gmail.mazinva.proofcheckingarithmetics.Main;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Variable extends Term {
    private String value;

    public Variable(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public String toString() {
        return value;
    }

    public List<Pair> pathToFirstFreeEntry(String x) {
        if (value.equals(x)) {
            List<Pair> resultPath = new LinkedList<>();
            resultPath.add(new Pair(Main.VARIABLE, null));
            return resultPath;
        }

        return null;
    }

    public String toStringWithReplacedVar(Term term, String var) {
        if (this.value.equals(var)) {
            return term.toString();
        } else {
            return value;
        }
    }

    public boolean isFreeToReplace(Term term, String var) {
        return true;
    }
}
