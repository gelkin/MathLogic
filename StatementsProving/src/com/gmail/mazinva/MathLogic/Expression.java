package com.gmail.mazinva.MathLogic;

import java.util.List;

public interface Expression {
    @Override
    public String toString();

    public boolean isTrue();

    public List<String> getProof() throws MathLogicException;
}
