package com.gmail.mazinva.statementsproving;

import com.gmail.mazinva.mathlogic.*;
import java.util.ArrayList;
import java.util.List;

public class DeductionTheorem {

    public static List<String> applyDedTheorem(List<String> conditions,
                                               List<String> proof) throws MathLogicException {
        List<String> newProof = proof;
        int size = conditions.size();
        String alpha;
        for (int i = size - 1; i >= 0; i--) {
            alpha = conditions.remove(i);
            newProof = applyDedTheoremOnce(conditions, alpha, newProof);
        }
        return newProof;
    }

    // Pre: conditions, alpha |- beta
    // Post: conditions |- (alpha -> beta)
    public static List<String> applyDedTheoremOnce(List<String> conditions,
                                                   String alpha,
                                                   List<String> proof) throws MathLogicException{
        List<String> newProof = new ArrayList<String>();
        String curLine;
        for (int i = 0; i < proof.size(); i++) {
            curLine = proof.get(i);

            // 1 case: curLine is axiom or belongs to conditions
            if((Axioms.doesMatchAxioms(curLine)) || ((conditions.size() > 0) &&
                                                      isCondition(curLine, conditions))) {

                newProof.add(parse(curLine + "->(" + alpha + "->" + curLine + ")"));
                newProof.add(parse(curLine));
                newProof.add(parse(alpha + "->" + curLine));

                continue;
            }

            // 2 case:
            if (curLine.equals(alpha)) {
                // Axiom #1
                newProof.add(parse(alpha + "->((" + alpha + "->" + alpha + ")->" + alpha + ")"));
                // Axiom #1
                newProof.add(parse(alpha + "->(" + alpha + "->" + alpha + ")"));
                // Axiom #2
                newProof.add(parse("(" + alpha + "->(" + alpha + "->" + alpha + "))->" +
                                                     "(" + alpha + "->((" + alpha + "->" + alpha + ")->" + alpha + "))->" +
                                                     "(" + alpha + "->" + alpha + ")"));
                // M.P. 2 & 3
                newProof.add(parse("(" + alpha + "->((" + alpha + "->" + alpha + ")->" + alpha + "))->" +
                                             "(" + alpha + "->" + alpha + ")"));

                // M.P. 1 & 4
                newProof.add(parse("(" + alpha + "->" + alpha + ")"));

                continue;
            }

            // 3 case:
            // if (curLine <- M.P. lineJ & lineK), where lineK = lineJ -> curLine
            String lineJ = isModusPonensOfTwoLines(curLine, proof, i);
            if (lineJ != null) {

                // Axiom #2 (n-th line)
                newProof.add(parse("(" + alpha + "->(" + lineJ + "))->" +
                                   "(" + alpha + "->((" + lineJ + ")->" + curLine + "))->" +
                                   "(" + alpha + "->" + curLine + ")"));
                // M.P. j & n
                newProof.add(parse("(" + alpha + "->((" + lineJ + ")->" + curLine + "))->" +
                                             "(" + alpha + "->" + curLine + ")"));
                // M.P. n & (n+1)1
                newProof.add(parse("(" + alpha + "->" + curLine + ")"));

                continue;
            }

            throw new MathLogicException("Statement \"" + curLine + "\" is not proved");
        }

        return newProof;
    }

    private static String isModusPonensOfTwoLines(String curLine, List<String> proof, int k)
                                                                 throws MathLogicException {
        String lineJ;
        String requiredString;
        for (int i = 0; i < k; i++) {
            lineJ = proof.get(i);
            requiredString = parse("(" + lineJ + "->" + curLine + ")");
            for (int j = 0; j < k; j++)
                if (requiredString.equals(proof.get(j))) {
                    return lineJ;
                }
        }

        return null;
    }

    private static boolean isCondition(String curLine, List<String> conditions) {
        for (int i = 0; i < conditions.size(); i++) {
            if (curLine.equals(conditions.get(i))) {
                return true;
            }
        }
        return false;
    }

    public static String parse(String curLine) throws MathLogicException {
        return (new Parser()).parse(curLine).toString();
    }
}
