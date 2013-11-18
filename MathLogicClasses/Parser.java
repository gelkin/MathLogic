package com.gmail.mazinva.MathLogic;

import java.util.Stack;

public class Parser {

    // Needed to keep right associativity of implication
    private Stack<Expression> implicationList;


    public Parser() {
        implicationList = new Stack<Expression>();
    }

    public Expression parse(String s) {
        // Expected that s is not empty
        Result result = implication(s);
        implicationList.clear();
        return result.exp;
    }

    private Result implication(String s) {
        Result current = conjunction(s);
        int localCounter = 0;

        while (current.rest.length() > 1) {
            char sign1 = current.rest.charAt(0);
            char sign2 = current.rest.charAt(1);
            if (sign1 != '-' || sign2 != '>') {
                break;
            }
            implicationList.add(current.exp);
            localCounter++;

            String next = current.rest.substring(2);
            current = conjunction(next);
        }

        if (!implicationList.isEmpty() && (localCounter > 0)) {
            implicationList.add(current.exp);
            Expression exp = implicationList.pop();
            for (int i = localCounter - 1; i > -1; i--) {
                exp = new Implication(implicationList.pop(), exp);
            }

            return new Result(exp, current.rest);
        }

        return new Result(current.exp, current.rest);
    }

    private Result conjunction(String s) {
        Result current = disjunction(s);
        Expression exp = current.exp;

        while (current.rest.length() > 0) {
            char sign = current.rest.charAt(0);
            if (sign != '&') {
                break;
            }

            String next = current.rest.substring(1);
            current = disjunction(next);

            exp = new Conjunction(exp, current.exp);
        }

        return new Result(exp, current.rest);
    }

    private Result disjunction(String s) {
        Result current = brackets(s);
        Expression exp = current.exp;

        while (current.rest.length() > 0) {
            char sign = current.rest.charAt(0);
            if (sign != '|') {
                break;
            }

            String next = current.rest.substring(1);
            current = brackets(next);

            exp = new Disjunction(exp, current.exp);
        }

        return new Result(exp, current.rest);
    }

    private Result brackets(String s) {
        char ch = s.charAt(0);
        if (ch == '(') {
            Result r = implication(s.substring(1));
            if (!r.rest.isEmpty() && r.rest.charAt(0) == ')') {
                r.rest = r.rest.substring(1);
            } else {
                System.err.println("Bracket is not closed");
            }
            return r;
        }

        return negationOrVariable(s);
    }

    private Result negationOrVariable(String s) {
        if (s.length() > 0 && s.charAt(0) == '!') {
            String next = s.substring(1);
            Result current = brackets(next);

            return new Result(new Negation(current.exp), current.rest);

        } else {
            String res = "";
            int i = 0;

            while (i < s.length() &&
                    (Character.isLetter(s.charAt(i)) && Character.isUpperCase(s.charAt(i)))) {

                res += s.charAt(i);
                i++;
            }

            if (res.isEmpty()) {
                System.err.println("Missed propositional variable");
            }

            return new Result((new Variable(res)), s.substring(res.length()));
        }
    }

}
