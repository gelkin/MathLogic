package com.gmail.mazinva.mathlogic;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;


// todo: Brackets for terms, brackets for predicates
// todo: all exceptions == "Error in ..s.." and nothing more
public class Parser {

    // Needed to keep right associativity of implication
    private Stack<Expression> implicationList;

    public Parser() {
        implicationList = new Stack<Expression>();
    }

    public Expression parse(String s) throws MathLogicException {
        if (s.isEmpty()) throw new MathLogicException("String to parse is empty");
        Result<Expression> result = implication(s);
        // todo
        if (!result.rest.isEmpty()) {
            throw new MathLogicException("Couldn't parse whole string");
        }
        implicationList.clear();
        return result.exp;
    }

    private Result implication(String s) throws MathLogicException {
        Result<Expression> current = conjunction(s);
        int localCounter = 0;

        if (current.exp instanceof Term) {
            return current;
        }

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
            if (current.exp instanceof Term) {
                // todo orthodox out exception messages
                throw new MathLogicException("Implication had a bad argument in \'" + next + "\'");
            }
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

        if (exp instanceof Term) {
            return current;
        }

        while (current.rest.length() > 0) {
            char sign = current.rest.charAt(0);
            if (sign != '&') {
                break;
            }

            String next = current.rest.substring(1);
            current = disjunction(next);
            if (!(current.exp instanceof Term)) {
                exp = new Conjunction(exp, current.exp);
            } else {
                throw new MathLogicException("Disjunction had a bad argument\n");
            }
        }

        return new Result<Expression>(exp, current.rest);
    }

    private Result disjunction(String s) throws MathLogicException {
        Result<Expression> current = unaryOperations(s);
        Expression exp = current.exp;

        if (exp instanceof Term) {
            return current;
        }

        while (current.rest.length() > 0) {
            char sign = current.rest.charAt(0);
            if (sign != '|') {
                break;
            }

            String next = current.rest.substring(1);
            current = unaryOperations(next);
            if (!(current.exp instanceof Term)) {
                exp = new Disjunction(exp, current.exp);
            } else {
                throw new MathLogicException("Disjunction had a bad argument\n");
            }
        }

        return new Result(exp, current.rest);
    }

    private Result unaryOperations(String s) throws MathLogicException {
        String next;
        Result<Expression> current;
        if (s.length() > 0 && s.charAt(0) == '!') {
            next = s.substring(1);
            current = unaryOperations(next);
            if (current.exp instanceof Term) {
                throw new MathLogicException("Negation had a bad argument\n");
            } else {
                return new Result<Expression>(new Negation((Expression) current.exp), current.rest);
            }
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
            current = unaryOperations(next);
            if (current.exp instanceof Term) {
                throw new MathLogicException("Quantifier \'" + s.charAt(0) + "\' had a bad argument\n");
            } else {
                if (s.charAt(0) == '@') {
                    return new Result<Expression>(new Forall(current.exp, var.toString()), current.rest);
                } else {
                    return new Result<Expression>(new Exist(current.exp, var.toString()), current.rest);
                }
            }
        }

        return equality(s);
    }

    private Result equality(String s) throws MathLogicException {
        Result<Expression> current = plus(s);
        Expression exp = current.exp;

        if (!(exp instanceof Term)) {
            // todo: as (term1 = term2) - predicate, then ((term1 = term2) = term3) - shouldn't exist
            // todo: so 'while' -> 'if'
            return current;
        }

        if (current.rest.length() > 0 && (current.rest.charAt(0) == '=')) {
            String next = current.rest.substring(1);
            current = plus(next);
            if (current.exp instanceof Term) {
                exp = new Equality((Term) exp,  (Term) current.exp);
            } else {
                throw new MathLogicException("Equality had a bad argument\n");
            }
        }

        return new Result(exp, current.rest); // todo return new equlity or predicate
    }

    private Result plus(String s) throws MathLogicException {
        Result current = multiply(s);
        Expression exp = (Expression) current.exp;
        if (!(exp instanceof Term)) {
            return current;
        }

        while (current.rest.length() > 0) {
            char sign = current.rest.charAt(0);
            if (sign != '+') {
                break;
            }

            String next = current.rest.substring(1);
            current = multiply(next);
            if (current.exp instanceof Term) {
                exp = new Plus((Term) exp,  (Term) current.exp);
            } else {
                throw new MathLogicException("Plus had a bad argument\n");
            }
        }

        return new Result(exp, current.rest);
    }

    private Result multiply(String s) throws MathLogicException {
        Result current = increment(s);
        Expression exp = (Expression) current.exp;
        if (!(exp instanceof Term)) {
            return current;
        }

        while (current.rest.length() > 0) {
            char sign = current.rest.charAt(0);
            if (sign != '*') {
                break;
            }

            String next = current.rest.substring(1);
            current = increment(next);
            if (current.exp instanceof Term) {
                exp = new Multiply((Term) exp, (Term) current.exp);
            } else {
                throw new MathLogicException("Multiply had a bad argument\n");
            }

        }

        return new Result(exp, current.rest);
    }

    private Result increment(String s) throws MathLogicException {
        // todo
        Result current = brackets(s);
        Expression exp = (Expression) current.exp;
        if (current.exp instanceof Term) {
            if (current.rest.length() > 0 && current.rest.charAt(0) == '\'') {
                exp = new Increment((Term) exp);
                current.rest = current.rest.substring(1);
            }
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
            if (r.rest.length() > 0 && r.rest.charAt(0) == ')') {
                r.rest = r.rest.substring(1);
            } else {
                throw new MathLogicException("Error in \"" + s + "\". Most probably there is not closed bracket.");
            }

            if (r.exp instanceof Term) {
                if (r.rest.length() > 0 && r.rest.charAt(0) == '\'') {
                    r.exp = new Increment((Term) r.exp);
                    r.rest = r.rest.substring(1);
                } else {
                    // todo
                    // List<Term> subTerms = new ArrayList<Term>();
                    // subTerms.add((Term) r.exp);
                    //  - problem seems to be solved in 'bracketsInTerms'
                    r.exp = new Brackets((Term) r.exp); // r.exp -> (r.exp)
                }
            }

            return r;
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

        // didn't find predicate - maybe will find term
        if (res.length() == 0) {
            //todo System.out.println("didn't find predicate - maybe will find term");
            Result<Term> result = terms(s);
            return result;
        }

        /*
        if (res.length() == 0) {
            throw new MathLogicException("Error in \"" + s + "\". Most probably some term is missed.");
        }
        */

        s = s.substring(res.length());

        Result<List<Term>> result;
        if (s.length() > 0 && s.charAt(0) == '(') {
            result = comma(s.substring(1));
            if (result.rest.length() > 0 && result.rest.charAt(0) == ')') {
                return new Result<Predicate>(new Predicate(res.toString(), result.exp), result.rest.substring(1));
            } else {
                throw new MathLogicException("Error in \"" + s + "\"");
            }
        } else {
            return new Result<Predicate>(new Predicate(res.toString()), s);
        }
    }

    /* TODO
    private Result parseSubTerms(String s) throws MathLogicException {
        if (s.length() > 0 &&  s.charAt(0) == '(') {
            // todo: do not substr!!! -> as comma() returns Comma obj or some
            // todo: other obj
            // todo Result r = comma(s.substring(1));
            // todo Changed
            Result r = comma(s);
            if (r.exp instanceof Brackets) {
                List<Term> subTerms = new ArrayList<>();
                subTerms.add(((Brackets) r.exp).term);
                return new Result<List<Term>>(new Comma(subTerms), r.rest);
            } else {
                return r;
            }
            // todo
            if (r.rest.length() > 0 && r.rest.charAt(0) == ')') {
                r.rest = r.rest.substring(1);
                return r;
            } else {
                throw new MathLogicException("Error in \"" + s + "\". Most probably there is not closed bracket.");
            }
            //
        }
        throw new MathLogicException("Error in \"" + s + "\". You did smth really wrong.");
    }
    */

    private Result<Term> bracketsInTerms(String s) throws MathLogicException {
        if (s.length() == 0) {
            throw new MathLogicException("Error in \"" + s + "\"");
        }

        char ch = s.charAt(0);
        if (ch == '(') {
            Result<Term> r = plus(s.substring(1));
            if (r.rest.length() > 0 && r.rest.charAt(0) == ')') {
                r.rest = r.rest.substring(1);
            } else {
                throw new MathLogicException("Error in \"" + s + "\". Most probably there is not closed bracket.");
            }
            return new Result<Term>(new Brackets(r.exp), r.rest);
        }
        return terms(s);
    }

    private Result<Term> terms(String s) throws MathLogicException {
        StringBuilder res = new StringBuilder();
        int i = 0;

        // read Zero
        if (s.length() > 0 && s.charAt(0) == '0') {
            return new Result<Term>(new Zero(), s.substring(1));
        }

        // read variable
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
            result = comma(s.substring(1));
            if (result.rest.length() > 0 && result.rest.charAt(0) == ')') {
                return new Result<Term>(new TermWithArgs(res.toString(), result.exp), result.rest.substring(1));
            } else {
                throw new MathLogicException("Error in \"" + s + "\"");
            }
        } else {
            return new Result<Term>(new Variable(res.toString()), s);
        }
    }

    // Coma takes List of terms, returns List of terms
    private Result<List<Term>> comma(String s) throws MathLogicException {
        Result<Term> current = plus(s);
        Term exp = current.exp;

        List<Term> terms = new ArrayList();
        terms.add(exp);

        while (current.rest.length() > 0) {
            char sign = current.rest.charAt(0);
            if (sign != ',') {
                break;
            }

            String next = current.rest.substring(1);
            current = plus(next);

            terms.add(current.exp);
        }

        return new Result(terms, current.rest);
    }
}
