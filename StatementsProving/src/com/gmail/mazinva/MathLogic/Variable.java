package com.gmail.mazinva.MathLogic;

import com.gmail.mazinva.StatementsProving.Main;
import com.gmail.mazinva.StatementsProving.StatementsProving;

import java.util.ArrayList;
import java.util.List;

public class Variable implements Expression {
    private String value;

    public Variable(String value) {
        if (StatementsProving.vars.get(value) == null) {
            StatementsProving.vars.put(value, true);
            StatementsProving.varByIndex.add(value);
        }
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    public boolean isTrue() {
        return StatementsProving.vars.get(value);
    }

    public List<String> getProof() {
        List<String> proof = new ArrayList<String>();
        if (isTrue()) {
            proof.add(value);
        } else {
            proof.add("!" + value);
        }
        return proof;
    }
}
