package com.gmail.mazinva.mathlogic;

public class MathLogicException extends Exception {
    String msg;

    public MathLogicException(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }
}
