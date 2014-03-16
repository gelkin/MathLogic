package com.gmail.mazinva.StatementsProving;

import com.gmail.mazinva.MathLogic.*;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class Main {

    FastScanner in;
    PrintWriter out;

    public void solve() throws MathLogicException {
        String inputString = in.next();
        if (inputString == null) {
            throw new MathLogicException("Empty input");
        }
        Expression phi = (new Parser()).parse(inputString);
        StatementsProving statementsProving= new StatementsProving(phi);
        List<String> resultProof = statementsProving.getProof();
        if (resultProof != null) {
            for (int i = 0; i < resultProof.size(); i++)
                out.println(DeductionTheorem.parse(resultProof.get(i)));
        }
    }

    public void run() {
        try {
            in = new FastScanner(new File("statementsProving.in"));
            out = new PrintWriter(new File("statementsProving.out"));

            solve();

            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MathLogicException e) {
            System.out.print(e.getMsg());
        }
    }

    public static void main(String[] args) {
        // You better don't use "A77" and "B77" as prop.
        // variables, as it is used as names in ./res - files
        new Main().run();
    }
 }


