package com.gmail.mazinva.mathlogic;

import com.gmail.mazinva.proofcheckingarithmetics.Main;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class TermWithArgs extends Term {
    private String value;
    private List<Term> subTerms;

    public TermWithArgs(String value, List<Term> subTerms) {
        this.value = value;
        this.subTerms = subTerms;
    }

    public String getValue() {
        return value;
    }

    public List<Term> getSubTerms() {
        return subTerms;
    }

    @Override
    public String toString() {
        // Supposed, that subTerms.size() > 0
        if (subTerms.size() == 0) {
            System.err.println("ERROR. No subterms in TermWithArgs.toString() =(");
        }

        StringBuilder res = new StringBuilder();
        res.append(value + "(");
        res.append(subTerms.get(0).toString());
        for (int i = 1; i < subTerms.size(); i++) {
            res.append(",");
            res.append(subTerms.get(i).toString());
        }
        res.append(")");
        return res.toString();
    }

    public List<Pair> pathToFirstFreeEntry(String x) {
        for (int i = 0; i < subTerms.size(); i++) {
            List<Pair> pathFromCurPos = subTerms.get(i).pathToFirstFreeEntry(x);
            if (pathFromCurPos != null) {
                ((LinkedList) pathFromCurPos).addFirst(new Pair(Main.TERMWITHARGS, new Pair(value, i)));
                return pathFromCurPos;
            }
        }

        return null;
    }

    public String toStringWithReplacedVar(Term term, String var) {
        StringBuilder res = new StringBuilder();
        res.append(value + "(");
        res.append(subTerms.get(0).toStringWithReplacedVar(term, var));
        for (int i = 1; i < subTerms.size(); i++) {
            res.append(",");
            res.append(subTerms.get(i).toStringWithReplacedVar(term, var));
        }
        res.append(")");
        return res.toString();
    }

    public boolean isFreeToReplace(Term term, String var) {
        return true;
    }
}
