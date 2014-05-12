
package com.gmail.mazinva.proofcheckingarithmetics;

import com.gmail.mazinva.mathlogic.*;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static final String IMPLICATION = "Implication";
    public static final String CONJUCNTION = "Conjunction";
    public static final String DISJUCNTION = "Disjunction";
    public static final String EQUALITY = "Equality";
    public static final String NEGATION = "Negation";
    public static final String FORALL = "Forall";
    public static final String EXIST = "Exist";
    public static final String INCREMENT = "Increment";
    public static final String BRACKETS = "Brackets";
    public static final String PREDICATE = "Predicate";
    public static final String TERMWITHARGS = "TermWithArgs";
    public static final String VARIABLE = "Variable";
    public static final String PLUS = "Plus";
    public static final String MULTIPLY= "Multiply";

    public static final String INCORRECT_INPUT = "Вывод некорректен начиная с формулы номер ";
    public static final String CORRECT_PROOF_MSG = "Доказательство корректно.";

    private FastScanner in;
    private PrintWriter out;

    private List<Expression> inputProof = new ArrayList<>();

    private void solve() throws IOException, MathLogicException {
        StringBuilder incorrectInputMsg = new StringBuilder();
        /*
        String inputString = in.nextLine();
        Expression exp = (new Parser()).parse(inputString);
        System.out.println(exp.toString());
        */

        //
        String inputString;
        // Read proof
        while ((inputString = in.nextLine()) != null) {
            inputProof.add((new Parser()).parse(inputString));
        }

        // Check proof
        Expression curExp;
        String curLine;
        String tmp;
        for (int i = 0; i < inputProof.size(); i++) {
            curExp = inputProof.get(i);
            curLine = curExp.toString();

            // 1 case:
            if (Axioms.doesMatchAxioms(curExp)) {
                continue;
            }

            // if error with freedom for subst occurred
            if (Axioms.getFlag()) {
                incorrectInputMsg.append(INCORRECT_INPUT);
                incorrectInputMsg.append(i + 1);
                incorrectInputMsg.append(": " + Axioms.getErrorMsg());
                break;
            }

            // 2 case:
            // if (curLine <- M.P. lineJ & lineK), where lineK = lineJ -> curLine
            String lineJ = isModusPonensOfTwoLines(curLine, i);
            if (lineJ != null) {
                continue;
            }

            // 3 case:
            // if curLine = a -> @b, having a -> b
            lineJ = isApplicationOfForallRule(curExp);
            if (lineJ == null) {
                continue;
            } else if (!lineJ.equals("NOT OK")) {
                incorrectInputMsg.append(INCORRECT_INPUT);
                incorrectInputMsg.append(i + 1);
                incorrectInputMsg.append(": " + lineJ);
                break;
            }

            // 4 case:
            // if curLine = ?a -> b, having a -> b
            lineJ = isApplicationOfExistRule(curExp);
            if (lineJ == null) {
                continue;
            } else if (!lineJ.equals("NOT OK")) {
                incorrectInputMsg.append(INCORRECT_INPUT);
                incorrectInputMsg.append(i + 1);
                incorrectInputMsg.append(": " + lineJ);
                break;
            }

            incorrectInputMsg.append(INCORRECT_INPUT);
            incorrectInputMsg.append(i + 1);
            break;
        }

        if (incorrectInputMsg.length() > 0) {
            out.println(incorrectInputMsg);
        } else {
            out.println(CORRECT_PROOF_MSG);
        }
        //
    }

    private String isModusPonensOfTwoLines(String curLine, int k) {
        String lineJ;
        String requiredString;
        for (int i = 0; i < k; i++) {
            lineJ = inputProof.get(i).toString();
            requiredString = "(" + lineJ + "->" + curLine + ")";
            for (int j = 0; j < k; j++) {
                if (requiredString.equals(inputProof.get(j).toString())) {
                    return lineJ;
                }
            }
        }

        return null;
    }

    private String isApplicationOfForallRule(Expression curExp) throws MathLogicException {
        String var;
        if (curExp instanceof Implication) {
            Expression right = ((Implication) curExp).getRight();
            if (right instanceof Forall) {
                var = ((Forall) right).getVar();
            } else {
                return "NOT OK";
            }
        } else {
            return "NOT OK";
        }

        String curLine = curExp.toString();
        String requiredString;
        Expression expJ;

        for (int i = 0; i < inputProof.size(); i++) {
            expJ = inputProof.get(i);
            if (expJ instanceof Implication) {
                requiredString = parse("(" + ((Implication) expJ).getLeft().toString() + "->@" +
                                            var + ((Implication) expJ).getRight().toString() + ")");
                if (requiredString.equals(curLine)) {

                    // if (exp or alpha has free entries of var)
                    return isVarFreeInExpression(((Implication) curExp).getLeft(), var);
                }
            }
        }
        return "NOT OK";
    }

    private String isApplicationOfExistRule(Expression curExp) throws MathLogicException {
        String var = "";
        if (curExp instanceof Implication) {
            Expression left = ((Implication) curExp).getLeft();
            if (left instanceof Exist) {
                var = ((Exist) left).getVar();
            } else {
                return "NOT OK";
            }
        } else {
            return "NOT OK";
        }

        String curLine = curExp.toString();
        String requiredString;
        Expression expJ;

        for (int i = 0; i < inputProof.size(); i++) {
            expJ = inputProof.get(i);
            if (expJ instanceof Implication) {
                requiredString = parse("(?" + var + ((Implication) expJ).getLeft().toString() +
                                            "->" + ((Implication) expJ).getRight().toString() + ")");
                if (requiredString.equals(curLine)) {

                    // if (exp or alphaExp has free entries of var)
                    return isVarFreeInExpression(((Implication) curExp).getRight(), var);
                }
            }
        }
        return "NOT OK";
    }

    // if ("exp" or "alphaExp" has free entries of var)
    private String isVarFreeInExpression(Expression exp, String var) {
        if (exp.pathToFirstFreeEntry(var) != null) {
            return ("используется правило с квантором по" +
                    " переменной " + var + " входящей" +
                    " свободно в допущение " + exp.toString());
        }

        /*
        if (alphaExp.pathToFirstFreeEntry(var) != null) {
            return ("используется правило с квантором по" +
                    " переменной " + var + " входящей" +
                    " свободно в допущение " + alpha);
        }
        */
        return null;
    }

    private String parse(String curLine) throws MathLogicException {
        return (new Parser()).parse(curLine).toString();
    }

    private void run() {
        try {
            in = new FastScanner(new File("proof_checking_arithmetics.in"));
            out = new PrintWriter(new File("proof_checking_arithmetics.out"));

            solve();

            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MathLogicException me) {
            System.out.println(me.getMsg());
        }
    }

    public static void main(String[] args) {
        new Main().run();
    }

}
