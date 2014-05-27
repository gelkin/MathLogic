package com.gmail.mazinva.mathlogic;

import com.gmail.mazinva.dedtheorempredicates.Main;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Axioms {
    private static Pattern[] listOfAxioms = {
        /* 1 */ Pattern.compile(
            "^[\\(]{1}(.+)->[\\(]{1}(.+)->\\1[\\)]{2}$"),
        /* 2 */ Pattern.compile(
            "^[\\(]{2}(.+)->(.+)[\\)]{1}->[\\(]{2}\\1->" +
                    "[\\(]{1}\\2->(.+)[\\)]{2}->[\\(]{1}\\1->\\3[\\)]{3}$"),
        /* 3 */ Pattern.compile(
            "^[\\(]{1}(.+)->[\\(]{1}(.+)->[\\(]{1}\\1&\\2[\\)]{3}$"),
        /* 4 */ Pattern.compile(
            "^[\\(]{2}(.+)&(.+)[\\)]{1}->\\1[\\)]{1}$"),
        /* 5 */ Pattern.compile(
            "^[\\(]{2}(.+)&(.+)[\\)]{1}->\\2[\\)]{1}$"),
        /* 6 */ Pattern.compile(
            "^[\\(]{1}(.+)->[\\(]{1}\\1\\|(.+)[\\)]{2}$"),
        /* 7 */ Pattern.compile(
            "^[\\(]{1}(.+)->[\\(]{1}(.+)\\|\\1[\\)]{2}$"),
        /* 8 */ Pattern.compile(
            "^[\\(]{2}(.+)->(.+)[\\)]{1}->[\\(]{2}(.+)" +
                    "->\\2[\\)]{1}->[\\(]{2}\\1\\|\\3[\\)]{1}->\\2[\\)]{3}$"),
        /* 9 */ Pattern.compile(
            "^[\\(]{2}(.+)->(.+)[\\)]{1}->[\\(]{2}\\1->!\\2[\\)]{1}->!\\1[\\)]{2}$"),
        /* 10 */ Pattern.compile("^[\\(]{1}!!(.+)->\\1[\\)]{1}$")
    };

    // two field needed for reporting about errors
    private static boolean flag = false;
    private static String errorMsg = "";


    public static boolean doesMatchAxioms(Expression exp) {
        // Needed for showing error msg
        flag = false;
        errorMsg = "";

        String expAsString = exp.toString();
        Matcher matcher;
        for (int i = 0; i < listOfAxioms.length; i++) {
            matcher = listOfAxioms[i].matcher(expAsString);
            if (matcher.matches()) {
                return true;
            }
        }

        // Axiom #11:
        // @xA -> A[x := y], if y is free for subst in A
        if (doesMatchForallAxiom(exp)) {
            return true;
        }

        // Axiom #12
        // A[x := y] -> ?xA, if y is free for subst in A
        if (!flag && doesMatchExistAxiom(exp)) {
            return true;
        }

        return false;
    }

    public static boolean getFlag() {
        return flag;
    }

    public static String getErrorMsg() {
        return errorMsg;
    }

    // @var(phi) -> newPhi
    private static boolean doesMatchForallAxiom(Expression exp) {
        if (exp instanceof Implication) {
            if (((Implication) exp).left instanceof Forall) {
                String var = (((Forall) ((Implication) exp).left).var);
                Expression phi = (((Forall) ((Implication) exp).left).expression);
                Expression newPhi = ((Implication) exp).right;
                return areTwoExpEqualAfterSubst(phi, newPhi, var);
            }
        }
        return false;
    }

    // newPhi -> ?var(phi)
    private static boolean doesMatchExistAxiom(Expression exp) {
        if (exp instanceof Implication) {
            if (((Implication) exp).right instanceof Exist) {
                String var = (((Exist) ((Implication) exp).right).var);
                Expression phi = (((Exist) ((Implication) exp).right).expression);
                Expression newPhi = ((Implication) exp).left;
                return areTwoExpEqualAfterSubst(phi, newPhi, var);
            }
        }
        return false;
    }

    // phi - exp. before substitution
    // newPhi - (expected) phi after substitution
    // var - what to substitute
    private static boolean areTwoExpEqualAfterSubst(Expression phi,
                                                    Expression newPhi,
                                                    String var) {
        List<Pair> pathToFirstFreeEntry = phi.pathToFirstFreeEntry(var);
        if (pathToFirstFreeEntry == null) {
            return (newPhi.toString().equals(phi.toString()));
        }
        Term replacingTerm = getReplacingTerm(pathToFirstFreeEntry, newPhi);
        if (replacingTerm != null) {
            String expWithReplacedVar = phi.toStringWithReplacedVar(replacingTerm, var);
            if (expWithReplacedVar.equals(newPhi.toString())) {
                if (phi.isFreeToReplace(replacingTerm, var)) {
                    return true;
                } else {
                    flag = true;
                    errorMsg = ("терм " + replacingTerm.toString() +
                                " не свободен для подстановки в формулу " +
                                phi.toString() + " вместо переменной " + var);
                }
            }
        }
       return false;
    }

    // in statement A[x := y], this function will return "y"
    private static Term getReplacingTerm(List<Pair> path, Expression newPhi) {
        Expression exp = newPhi;
        for (int i = 0; i < path.size(); i++) {
            Pair expPair = path.get(i);
            switch (expPair.expressionType) {
                case Main.IMPLICATION:
                    if (exp instanceof Implication) {
                        if ("left".equals(expPair.additionalInfo)) {
                            exp = ((Implication) exp).left;
                        } else {
                            exp = ((Implication) exp).right;
                        }
                    } else {
                        return null;
                    }
                    continue;
                case Main.CONJUCNTION:
                    if (exp instanceof Conjunction) {
                        if ("left".equals(expPair.additionalInfo)) {
                            exp = ((Conjunction) exp).left;
                        } else {
                            exp = ((Conjunction) exp).right;
                        }
                    } else {
                        return null;
                    }
                    continue;
                case Main.DISJUCNTION:
                    if (exp instanceof Disjunction) {
                        if ("left".equals(expPair.additionalInfo)) {
                            exp = ((Disjunction) exp).left;
                        } else {
                            exp = ((Disjunction) exp).right;
                        }
                    } else {
                        return null;
                    }
                    continue;
                case Main.NEGATION:
                    if (exp instanceof Negation) {
                        exp = ((Negation) exp).expression;
                    } else {
                        return null;
                    }
                    continue;
                case Main.FORALL:
                    if (exp instanceof Forall &&
                        ((Forall) exp).var.equals(expPair.additionalInfo)) {

                            exp = ((Forall) exp).expression;
                    } else {
                        return null;
                    }
                    continue;
                case Main.EXIST:
                    if (exp instanceof Exist &&
                        ((Exist) exp).var.equals(expPair.additionalInfo)) {

                        exp = ((Exist) exp).expression;
                    } else {
                        return null;
                    }
                    continue;
                case Main.PREDICATE:
                    if (exp instanceof Predicate) {
                        Pair pair = (Pair) expPair.additionalInfo;
                        List<Term> subTerms = ((Predicate) exp).getSubTerms();

                        if (pair.expressionType.equals(((Predicate) exp).getValue()) &&
                            subTerms.size() > ((Integer) pair.additionalInfo)) {

                            exp = subTerms.get(((Integer) pair.additionalInfo));
                            continue;
                        } else {
                            return null;
                        }
                    }
                    return null;
                case Main.TERM:
                    if (exp instanceof Term) {

                        Pair pair = (Pair) expPair.additionalInfo;
                        List<Term> subTerms = ((Term) exp).getSubTerms();

                        if (pair == null) {
                            return (Term) exp; //  found it
                        }

                        if (subTerms == null) {
                            return null; // still have path but no subTerms in 'exp'
                        } else if (subTerms.size() > ((Integer) pair.additionalInfo)) {

                            if (("".equals(((Term) exp).getValue()) == // first condition
                                "Brackets".equals(pair.additionalInfo)
                                && subTerms.size() > ((Integer) pair.additionalInfo))) { // second condition

                                exp = subTerms.get(((Integer) pair.additionalInfo));
                                continue;
                            }
                        }
                    }
                    return null;
            }
        }
        return null;
    }
}
