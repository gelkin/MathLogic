
package com.gmail.mazinva.dedtheorempredicates;

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
    public static final String NEGATION = "Negation";
    public static final String FORALL = "Forall";
    public static final String EXIST = "Exist";
    public static final String PREDICATE = "Predicate";
    public static final String TERM = "Term";

    public static final String INCORRECT_INPUT = "Вывод некорректен начиная с формулы номер ";

    private FastScanner in;
    private PrintWriter out;

    private List<Expression> inputProof = new ArrayList<>();
    private List<String> outputProof = new ArrayList();
    private List<String> forallProof = new ArrayList<>();
    private List<String> existProof = new ArrayList<>();
    private String[] conditions;
    private Expression alphaExp;
    private String alpha;
    private String beta;

    private void solve() throws IOException, MathLogicException {
        StringBuilder incorrectInputMsg = new StringBuilder();

        loadForallProof();
        loadExistProof();

        String inputString;
        // Read head string
        if ((inputString = in.nextLine()) != null) {
            initConditions(inputString);
        }

        // Read proof
        while ((inputString = in.nextLine()) != null) {
            inputProof.add((new Parser()).parse(inputString));
        }

        // Write a head string
        StringBuilder headString = new StringBuilder();
        if (conditions.length > 0) {
            headString.append(conditions[0]);
        }
        for (int i = 1; i < conditions.length; i++) {
            headString.append("," + conditions[i]);
        }
        headString.append("|-(" + alpha + "->" + beta + ")");

        // Write a new proof
        Expression curExp;
        String curLine;
        String tmp;
        for (int i = 0; i < inputProof.size(); i++) {
            curExp = inputProof.get(i);
            curLine = curExp.toString();

            // 1 case:
            if ((Axioms.doesMatchAxioms(curExp)) || ((conditions.length > 0)
                 && isCondition(curLine))) {
                tmp = curLine + "->" + alpha + "->" + curLine;
                outputProof.add(tmp);
                tmp = curLine;
                outputProof.add(tmp);
                tmp = alpha + "->" + curLine;
                outputProof.add(tmp);

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
            if (curLine.equals(alpha)) {
                // Axiom #1
                tmp = alpha + "->((" + alpha + "->" + alpha + ")->" + alpha + ")";
                outputProof.add(tmp);
                // Axiom #1
                tmp = alpha + "->(" + alpha + "->" + alpha + ")";
                outputProof.add(tmp);
                // Axiom #2
                tmp = "(" + alpha + "->(" + alpha + "->" + alpha + "))->" +
                        "(" + alpha + "->((" + alpha + "->" + alpha + ")->" + alpha + "))->" +
                        "(" + alpha + "->" + alpha + ")";
                outputProof.add(tmp);
                // M.P. 2 & 3
                tmp = "(" + alpha + "->((" + alpha + "->" + alpha + ")->" + alpha + "))->" +
                        "(" + alpha + "->" + alpha + ")";
                outputProof.add(tmp);
                // M.P. 1 & 4
                tmp =  "(" + alpha + "->" + alpha + ")";
                outputProof.add(tmp);

                continue;
            }

            // 3 case:
            // if (curLine <- M.P. lineJ & lineK), where lineK = lineJ -> curLine
            String lineJ = isModusPonensOfTwoLines(curLine, i);
            if (lineJ != null) {
                // Axiom #2 (n-th line)
                tmp = "(" + alpha + "->" + lineJ + ")->" +
                        "(" + alpha + "->" + lineJ + "->" + curLine + ")->" +
                        "(" + alpha + "->" + curLine + ")";
                outputProof.add(tmp);
                // M.P. j & n
                tmp = "(" + alpha + "->" + lineJ + "->" + curLine + ")->" +
                        "(" + alpha + "->" + curLine + ")";
                outputProof.add(tmp);
                // M.P. n & (n+1)
                tmp = "(" + alpha + "->" + curLine + ")";
                outputProof.add(tmp);

                continue;
            }

            // 4 case:
            // if curLine = a -> @b, having a -> b
            lineJ = isApplicationOfForallRule(curExp);
            if (lineJ == null) {
                // exp == left -> @var(right)
                String left = ((Implication) curExp).getLeft().toString();
                String right = ((Forall) ((Implication) curExp).getRight()).getExpression().toString();
                String var = ((Forall) ((Implication) curExp).getRight()).getVar();
                insertQuantifierProof(forallProof, left, right, var); // insert proof here
                continue;
            } else if (!lineJ.equals("NOT OK")) {
                incorrectInputMsg.append(INCORRECT_INPUT);
                incorrectInputMsg.append(i + 1);
                incorrectInputMsg.append(": " + lineJ);
                break;
            }

            // 5 case:
            // if curLine = ?a -> b, having a -> b
            lineJ = isApplicationOfExistRule(curExp);
            if (lineJ == null) {
                // exp == left -> @var(right)
                String left = ((Exist) ((Implication) curExp).getLeft()).getExpression().toString();
                String right = ((Implication) curExp).getRight().toString();
                String var = ((Exist) ((Implication) curExp).getLeft()).getVar();
                insertQuantifierProof(existProof, left, right, var); // insert proof here
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
            out.println(headString);
            for (int i = 0; i < outputProof.size(); i++) {
                out.println(parse(outputProof.get(i)));
            }
        }

    }

    private static final String RES_PATH = "./res/";
    private FastScanner inProof;
    private void loadForallProof() throws MathLogicException {
        String path = "forall.in";
        inProof = new FastScanner(new File(RES_PATH + path));
        String s;
        while ((s = inProof.next()) != null) {
            forallProof.add(parse(s));
        }
    }

    private void loadExistProof() throws MathLogicException {
        String path = "exist.in";
        inProof = new FastScanner(new File(RES_PATH + path));
        String s;
        while ((s = inProof.next()) != null) {
            existProof.add(parse(s));
        }
    }

    private void initConditions(String headString) throws MathLogicException {
        String[] x = headString.split(", ");

        conditions = new String[x.length - 1];
        for (int i = 0; i < (x.length - 1); i++) {
            conditions[i] = parse(x[i]);
        }

        String[] y = x[x.length - 1].split("\\|-");

        alphaExp = (new Parser()).parse(y[0]);
        alpha = parse(y[0]);
        beta = parse(y[1]);
    }

    private boolean isCondition(String curLine) {
        for (int i = 0; i < conditions.length; i++) {
            if (curLine.equals(conditions[i])) {
                return true;
            }
        }
        return false;
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

        if (alphaExp.pathToFirstFreeEntry(var) != null) {
            return ("используется правило с квантором по" +
                    " переменной " + var + " входящей" +
                    " свободно в допущение " + alpha);
        }
        return null;
    }

    private static final String A = "A77";
    private static final String B = "B77";
    private static final String C = "C77";
    private static final String VAR = "v77";
    private void insertQuantifierProof(List<String> proof, String left, String right, String var) {
        String s;
        for (int i = 0; i < proof.size(); i++) {
            s = proof.get(i);
            // replace with actual values
            s = s.replace(A, alpha);
            s = s.replace(B, left);
            s = s.replace(C, right);
            s = s.replace(VAR, var);
            // write it to output
            outputProof.add(s);
        }
    }

    private String parse(String curLine) throws MathLogicException {
        return (new Parser()).parse(curLine).toString();
    }

    private void run() {
        try {
            in = new FastScanner(new File("ded_theorem_predicates.in"));
            out = new PrintWriter(new File("ded_theorem_predicates.out"));

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
