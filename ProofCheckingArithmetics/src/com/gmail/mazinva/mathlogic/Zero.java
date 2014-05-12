package com.gmail.mazinva.mathlogic;

import com.gmail.mazinva.proofcheckingarithmetics.Main;
import java.util.List;

public class Zero extends Term {
    private static final String value = "0";

    public String getValue() {
        return value;
    }

    public String toString() {
        return value;
    }

    public List<Pair> pathToFirstFreeEntry(String x) {
        return null;
    }

    public String toStringWithReplacedVar(Term term, String var) {
        return value;
    }

    public boolean isFreeToReplace(Term term, String var) {
        return true;
    }
}
