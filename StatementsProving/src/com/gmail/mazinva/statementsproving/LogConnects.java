package com.gmail.mazinva.statementsproving;

import com.gmail.mazinva.mathlogic.FastScanner;
import com.gmail.mazinva.mathlogic.MathLogicException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/*
 This class contains methods that implement proofs of corresponding logical
 connectives, considering that X and Y are propositional variables
 1) X,Y |- X&Y
 2) X,!Y |- !(X&Y)
 ...
 5) X,Y |- X|Y
 6) X,!Y |- X|Y
 ...
 9) X,Y |- X->Y
 10) X,!Y |- !(X->Y)
 ...
 13) X |- !!X
 14) !X |- !X

 */


public class LogConnects {

    private static final String RES_PATH = "./res/";
    private static final String A = "A77";
    private static final String B = "B77";

    /*
     Conjunction
      */

    // 1) X,Y |- X&Y
    public static List<String> XandY(String X, String Y) throws MathLogicException {
        List<String> proof = new ArrayList<String>();
        // Axiom #3
        proof.add(DeductionTheorem.parse("((" + X + ")->((" + Y + ")->((" + X + ")&(" + Y + "))))"));
        // M.P. 1 2
        proof.add(DeductionTheorem.parse("((" + Y + ")->((" + X + ")&(" + Y + ")))"));
        // M.P. 3 4
        proof.add(DeductionTheorem.parse("((" + X + ")&(" + Y + "))"));

        return proof;
    }

    // 2) X,!Y |- !(X&Y)
    public static List<String> XandNotY(String X, String Y) throws MathLogicException {
        List<String> proof = new ArrayList<String>();
        // Axiom #1
        proof.add(DeductionTheorem.parse("(!(" + Y + ")->(((" + X + ")&(" + Y + "))->!(" + Y + ")))"));
        // M.P. 1 2
        proof.add(DeductionTheorem.parse("((" + X + ")&(" + Y + ")->!(" + Y + "))"));
        // Axiom #5
        proof.add(DeductionTheorem.parse("((" + X + ")&(" + Y + "))->(" + Y + ")"));
        // Axiom #9
        proof.add(DeductionTheorem.parse("((" + X + "&" + Y + ")->" + Y + ")->" +
                             "((" + X + "&" + Y + ")->!" + Y + ")->" +
                             "!(" + X + "&" + Y + ")"));
        // M.P. 4 5
        proof.add(DeductionTheorem.parse("((" + X + "&" + Y + ")->!" + Y + ")->" +
                             "!(" + X + "&" + Y + ")"));
        // M.P. 3 6
        proof.add(DeductionTheorem.parse("!(" + X + "&" + Y + ")"));

        return proof;
    }


    // 3) !X,Y |- !(X&Y)
    public static List<String> NotXandY(String X, String Y) throws MathLogicException {
        List<String> proof = new ArrayList<String>();
        // Axiom #1
        proof.add(DeductionTheorem.parse("!(" + X + ")->(" + X + "&" + Y + ")->!" + X));
        // M.P. 1 2
        proof.add(DeductionTheorem.parse("(" + X + "&" + Y + ")->!" + X));
        // Axiom #4
        proof.add(DeductionTheorem.parse("(" + X + "&" + Y + ")" + "->" + X));
        // Axiom #9
        proof.add(DeductionTheorem.parse("((" + X + "&" + Y + ")->" + X + ")->" +
                             "((" + X + "&" + Y + ")->!" + X + ")->" +
                             "!(" + X + "&" + Y + ")"));
        // M.P. 4 5
        proof.add(DeductionTheorem.parse("((" + X + "&" + Y + ")->!" + X + ")->" +
                                     "!(" + X + "&" + Y + ")"));
        // M.P. 3 6
        proof.add(DeductionTheorem.parse("!(" + X + "&" + Y + ")"));

        return proof;
    }

    // 4) !X,!Y |- !(X&Y)
    public static List<String> NotXandNotY(String X, String Y) throws MathLogicException {

        // Easy to see that this method would do the same as NotXandY 
        // and XandNotY methods. Let's choose XandNotY for this:
        return XandNotY(X, Y);
    }

    /*
     Disjunction
     */

    // 1) X,Y |- X|Y
    public static List<String> XorY(String X, String Y) throws MathLogicException {
        return XorNotY(X, Y);
    }

    // 2) X,!Y |- X|Y
    public static List<String> XorNotY(String X, String Y) throws MathLogicException {
        List<String> proof = new ArrayList<String>();
        // Axiom #6
        proof.add(DeductionTheorem.parse("(" + X + "->(" + X + "|" + Y + "))"));
        // M.P. 1 2
        proof.add(DeductionTheorem.parse("(" + X + "|" + Y + ")"));

        return proof;
    }

    // 3) !X,Y |- X|Y
    public static List<String> NotXorY(String X, String Y) throws MathLogicException {
        List<String> proof = new ArrayList<String>();
        // Axiom #7
        proof.add(DeductionTheorem.parse("(" + Y + "->(" + X + "|" + Y + "))"));
        // M.P. 1 2
        proof.add(DeductionTheorem.parse("(" + X + "|" + Y + ")"));

        return proof;
    }

    // 4) !X,!Y |- !(X|Y)
    public static List<String> NotXorNotY(String X, String Y) throws MathLogicException {
        return loadProof("!A,!Bthen!(AorB)", X, Y);
    }

    /*
     Implication
     */

    // 1) X,Y |- X->Y
    public static List<String> XthenY(String X, String Y) throws MathLogicException {
        return NotXthenY(X, Y);
    }

    // 2) !X,Y |- X->Y
    public static List<String> NotXthenY(String X, String Y) throws MathLogicException {
        List<String> proof = new ArrayList<String>();
        // Axiom #1
        proof.add(DeductionTheorem.parse("(" + Y + "->(" + X + "->" + Y + "))"));
        // M.P. 1 2
        proof.add(DeductionTheorem.parse("(" + X + "->" + Y + ")"));

        return proof;
    }

    // 3) X,!Y |- !(X->Y)
    public static List<String> XthenNotY(String X, String Y) throws MathLogicException {
        return loadProof("A,!Bthen!(A-_B)", X, Y);
    }

    // 4) !X,!Y |- (X->Y)
    public static List<String> NotXthenNotY(String X, String Y) throws MathLogicException {
        return loadProof("!A,!BthenA-_B", X, Y);
    }

    /*
     Negation
     */

    // 1) X |- !!X
    public static List<String> negX(String X) throws MathLogicException {
        return loadProof("Athen!!A", X);
    }

    // 2) !X |- !X
    public static List<String> negNotX(String X) {
        return new ArrayList<String>();
    }


    private static FastScanner in;
    public static List<String> loadProof(String path, String X, String Y) throws MathLogicException {
        in = new FastScanner(new File(RES_PATH + path));

        List<String> res = new ArrayList<String>();
        String s;
        while ((s = in.next()) != null) {
            s = s.replace(A, "(" + X + ")");
            s = s.replace(B, "(" + Y + ")");
            res.add(DeductionTheorem.parse(s));
        }
        return res;
    }

    public static List<String> loadProof(String path, String X) throws MathLogicException {
        return loadProof(path, X, X);
    }

}
