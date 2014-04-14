package com.gmail.mazinva.mathlogic;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Parser {

    // Needed to keep right associativity of implication
    private Stack<Expression> implicationList;

    public Parser() {
        implicationList = new Stack<Expression>();
    }

    public Expression parse(String s) throws MathLogicException {
        if (s.isEmpty()) throw new MathLogicException("String to parse is empty");
        Result<Expression> result = implication(s);
        implicationList.clear();
        return result.exp;
    }

    private Result implication(String s) throws MathLogicException {
        Result<Expression> current = conjunction(s);
        int localCounter = 0;

        while (current.rest.length() > 1) {
            char sign1 = current.rest.charAt(0);
            char sign2 = current.rest.charAt(1);
            if (sign1 != '-' || sign2 != '>') {
                break;
            }
            implicationList.add((Expression) current.exp);
            localCounter++;

            String next = current.rest.substring(2);
            current = conjunction(next);
        }

        if (!implicationList.isEmpty() && (localCounter > 0)) {
            implicationList.add((Expression) current.exp);
            Expression exp = implicationList.pop();
            for (int i = localCounter - 1; i > -1; i--) {
                exp = new Implication(implicationList.pop(), exp);
            }

            return new Result<Expression>(exp, current.rest);
        }

        return new Result<Expression>(current.exp, current.rest);
    }

    private Result conjunction(String s) throws MathLogicException {
        Result<Expression> current = disjunction(s);
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

        return new Result<Expression>(exp, current.rest);
    }

    private Result disjunction(String s) throws MathLogicException {
        Result<Expression> current = brackets(s);
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

    private Result brackets(String s) throws MathLogicException {
        if (s.length() == 0) {
            throw new MathLogicException("Error in \"" + s + "\"");
        }
        char ch = s.charAt(0);
        if (ch == '(') {
            Result r = implication(s.substring(1));
            if (!r.rest.isEmpty() && r.rest.charAt(0) == ')') {
                r.rest = r.rest.substring(1);
            } else {
                throw new MathLogicException("Error in \"" + s + "\". Most probably there is not closed bracket.");
            }
            return r;
        }

        return unaryOperations(s);
    }

    private Result unaryOperations(String s) throws MathLogicException {
        String next;
        Result<Expression> current;
        if (s.length() > 0 && s.charAt(0) == '!') {
            next = s.substring(1);
            current = brackets(next);
            return new Result<Expression>(new Negation((Expression) current.exp), current.rest);
        }

        if (s.length() > 1 && (s.charAt(0) == '@' || s.charAt(0) == '?') &&
            Character.isLetter(s.charAt(1)) && Character.isLowerCase(s.charAt(1))) {
            StringBuilder var = new StringBuilder();
            var.append(s.charAt(1));
            int i = 2;
            while (i < s.length() && Character.isDigit(s.charAt(i))) {
                var.append(s.charAt(i));
                i++;
            }

            next = s.substring(var.length() + 1); // @/? + var.length
            current = brackets(next);
            if (s.charAt(0) == '@') {
                return new Result<Expression>(new Forall(current.exp, var.toString()), current.rest);
            } else {
                return new Result<Expression>(new Exist(current.exp, var.toString()), current.rest);
            }
        }

        return predicate(s);
    }

    private Result predicate(String s) throws MathLogicException {
        StringBuilder res = new StringBuilder();
        int i = 0;
        if (s.length() > 0 && (Character.isLetter(s.charAt(0)) &&
                               Character.isUpperCase(s.charAt(0))) ) {
            res.append(s.charAt(0));
            i++;
            while (i < s.length() && Character.isDigit(s.charAt(i))) {
                res.append(s.charAt(i));
                i++;
            }
        }

        if (res.length() == 0) {
            throw new MathLogicException("Error in \"" + s + "\". Most probably some term is missed.");
        }

        s = s.substring(res.length());
        if (s.length() > 0 && s.charAt(0) == '(') {
            Result<List<Term>> result = parseSubTerms(s);
            return new Result<Expression>((new Predicate(res.toString(), result.exp)), result.rest);
        } else {
            return new Result<Expression>((new Predicate(res.toString())), s);
        }
    }

    private Result<List<Term>> parseSubTerms(String s) throws MathLogicException {
        if (s.length() > 0 &&  s.charAt(0) == '(') {
            Result r = coma(s.substring(1));
            if (r.rest.length() > 0 && r.rest.charAt(0) == ')') {
                r.rest = r.rest.substring(1);
                return r;
            } else {
                throw new MathLogicException("Error in \"" + s + "\". Most probably there is not closed bracket.");
            }
        }
        throw new MathLogicException("Error in \"" + s + "\". You did smth really wrong.");
    }

    // Coma takes List of terms, returns List of terms
    private Result<List<Term>> coma(String s) throws MathLogicException {
        Result<Term> current = bracketsInTerms(s);
        Term exp = current.exp;

        List<Term> terms = new ArrayList();
        terms.add(exp);

        while (current.rest.length() > 0) {
            char sign = current.rest.charAt(0);
            if (sign != ',') {
                break;
            }

            String next = current.rest.substring(1);
            current = bracketsInTerms(next);

            terms.add(current.exp);
        }

        return new Result<List<Term>>(terms, current.rest);
    }

    private Result<Term> bracketsInTerms(String s) throws MathLogicException {
        if (s.length() == 0) {
            throw new MathLogicException("Error in \"" + s + "\"");
        }
        char ch = s.charAt(0);
        if (ch == '(') {
            Result<List<Term>> r = coma(s.substring(1));
            if (r.rest.length() > 0 && r.rest.charAt(0) == ')') {
                r.rest = r.rest.substring(1);
            } else {
                throw new MathLogicException("Error in \"" + s + "\". Most probably there is not closed bracket.");
            }
            return new Result<Term>(new Term(r.exp), r.rest);
        }
        return terms(s);
    }

    private Result<Term> terms(String s) throws MathLogicException {
        StringBuilder res = new StringBuilder();
        int i = 0;

        if (s.length() > 0 && (Character.isLetter(s.charAt(0)) &&
            Character.isLowerCase(s.charAt(0))) ) {

            res.append(s.charAt(0));
            i++;
            while (i < s.length() && Character.isDigit(s.charAt(i))) {
                res.append(s.charAt(i));
                i++;
            }
        }

        if (res.length() == 0) {
            throw new MathLogicException("Error in \"" + s + "\". Most probably some term is missed.");
        }

        s = s.substring(res.length());

        Result<List<Term>> result;
        if (s.length() > 0 && s.charAt(0) == '(') {
            result = coma(s.substring(1));
            return new Result<Term>((new Term(res.toString(), result.exp)), result.rest.substring(1));
        } else {
            return new Result<Term>(new Term(res.toString()), s);
        }
    }

}
