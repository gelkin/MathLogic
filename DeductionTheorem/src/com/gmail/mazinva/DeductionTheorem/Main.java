package com.gmail.mazinva.DeductionTheorem;

import com.gmail.mazinva.MathLogic.*;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.ListIterator;

public class Main {

    FastScanner in;
    PrintWriter out;

    LinkedList<String> inputProof = new LinkedList<String>();
    String[] conditions;
    String alpha;
    String beta;

    private void solve() throws IOException {
        String inputString;

        // Read a head string
        if ((inputString = in.next()) != null) {
            initConditions(inputString);
        }

        // Read a proof
        while ((inputString = in.next()) != null) {
            String parsedString = (new Parser()).parse(inputString).toString();
            inputProof.add(parsedString);
        }

        // Write a head string
        if (conditions.length > 0) {
            out.print(conditions[0]);
        }
        for (int i = 1; i < conditions.length; i++) {
            out.print("," + conditions[i]);
        }
        out.print("|-" + alpha + "->" + beta + "\n");

        // Write a new proof
        ListIterator<String> curLineIt = inputProof.listIterator();
        String curLine;
        while (curLineIt.hasNext()) {
            curLine = curLineIt.next();
            String tmp;
            // 1 case:
            if((Axioms.doesMatchAxioms(curLine)) || ((conditions.length > 0) &&
                                                          isCondition(curLine))) {
                // We can add it to proper List, but it's useless in this task,
                // so we print it directly in the "proof.out"

                tmp = curLine + "->" + alpha + "->" + curLine;
                out.print(makeParsed(tmp) + "\n");
                tmp = curLine;
                out.print(makeParsed(tmp) + "\n");
                tmp = alpha + "->" + curLine;
                out.print(makeParsed(tmp) + "\n");

                continue;
            }

            // 2 case:
            if (curLine.equals(alpha)) {
                // Axiom #1
                tmp = alpha + "->((" + alpha + "->" + alpha + ")->" + alpha + ")";
                out.print(makeParsed(tmp) + "\n");
                // Axiom #1
                tmp = alpha + "->(" + alpha + "->" + alpha + ")";
                out.print(makeParsed(tmp) + "\n");
                // Axiom #2
                tmp = "(" + alpha + "->(" + alpha + "->" + alpha + "))->" +
                      "(" + alpha + "->((" + alpha + "->" + alpha + ")->" + alpha + "))->" +
                      "(" + alpha + "->" + alpha + ")";
                out.print(makeParsed(tmp) + "\n");
                // M.P. 2 & 3
                tmp = "(" + alpha + "->((" + alpha + "->" + alpha + ")->" + alpha + "))->" +
                       "(" + alpha + "->" + alpha + ")";
                out.print(makeParsed(tmp) + "\n");
                // M.P. 1 & 4
                tmp =  "(" + alpha + "->" + alpha + ")";
                out.print(makeParsed(tmp) + "\n");

                continue;
            }

            // 3 case:
            // if (curLine <- M.P. lineJ & lineK), where lineK = lineJ -> curLine
            String lineJ = isModusPonensOfTwoLines(curLine);
            if (lineJ != null) {
                // Axiom #2 (n-th line)
                tmp = "(" + alpha + "->" + lineJ + ")->" +
                      "(" + alpha + "->" + lineJ + "->" + curLine + ")->" +
                      "(" + alpha + "->" + curLine + ")";
                out.print(makeParsed(tmp) + "\n");
                // M.P. j & n
                tmp = "(" + alpha + "->" + lineJ + "->" + curLine + ")->" +
                        "(" + alpha + "->" + curLine + ")";
                out.print(makeParsed(tmp) + "\n");
                // M.P. n & (n+1)
                tmp = "(" + alpha + "->" + curLine + ")";
                out.print(makeParsed(tmp) + "\n");

                continue;
            }

            // According to the CONDITION for this task, it won't happen:
            out.print("ERROR");
        }

    }

    private void initConditions(String headString) {
        String[] x = headString.split(",");
        conditions = new String[x.length - 1];
        for (int i = 0; i < (x.length - 1); i++) {
            conditions[i] = (new Parser()).parse(x[i]).toString();
        }

        String[] y = x[x.length - 1].split("\\|\\-");
        alpha = (new Parser()).parse(y[0]).toString();
        beta = (new Parser()).parse(y[1]).toString();
    }

    private boolean isCondition(String curLine) {
        for (int i = 0; i < conditions.length; i++) {
            if (curLine.equals(conditions[i])) {
                return true;
            }
        }
        return false;
    }

    private String isModusPonensOfTwoLines(String curLine) {
        ListIterator<String> concatIt = inputProof.listIterator();
        String lineJ;
        String requiredString;
        while (concatIt.hasNext()) {
            lineJ = concatIt.next();
            requiredString = "(" + lineJ + "->" + curLine + ")";
            ListIterator<String> compareIt = inputProof.listIterator();
            while (compareIt.hasNext()) {
                if (requiredString.equals(compareIt.next())) {
                    return lineJ;
                }
            }
        }

        return null;
    }
    
    private String makeParsed(String curLine) {
        return (new Parser()).parse(curLine).toString();
    }

    private void run() {
        try {
            in = new FastScanner(new File("dedtheorem.in"));
            out = new PrintWriter(new File("dedtheorem.out"));

            solve();

            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Main().run();
    }
}

