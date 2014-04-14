package com.gmail.mazinva.mathlogic;

import java.util.List;

public interface Expression {
    @Override
    public String toString();

    public List<Pair> pathToFirstFreeEntry(String x);

    /**
     *
     * @param term
     * @param var
     * @return Expression in string representation with free entries of @var
     *         replaced by @term
     */
    public String toStringWithReplacedVar(Term term, String var);

    /**
     * Checks if @term free for substitution (instead of)/for var
     *
     * @param term
     * @param var
     * @return
     */
    public boolean isFreeToReplace(Term term, String var);
}
