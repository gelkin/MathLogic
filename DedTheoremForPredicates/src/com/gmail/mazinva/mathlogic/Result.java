package com.gmail.mazinva.mathlogic;

public class Result<T> {
    public T exp;
    public String rest;

    public Result(T exp, String rest) {
        this.exp = exp;
        this.rest = rest;
    }
}
