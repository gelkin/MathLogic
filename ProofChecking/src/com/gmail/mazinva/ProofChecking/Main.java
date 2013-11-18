package com.gmail.mazinva.ProofChecking;

import com.gmail.mazinva.MathLogic.*;

import java.io.*;
import java.util.LinkedList;
import java.util.ListIterator;

public class Main {

    FastScanner in;
    PrintWriter out;

    public void solve() throws IOException {
        LinkedList<String> parsedProof = new LinkedList<String>();

        ListIterator<String> concatIt;
        ListIterator<String> compareIt;

        String inputString;
        String parsedString;
        String requiredString;

        boolean doesFit = false;
        int stringCounter = 0;

        while ((inputString = in.next()) != null) {
            stringCounter++;
            parsedString = (new Parser()).parse(inputString).toString();

            // Axioms:
            doesFit = Axioms.doesMatchAxioms(parsedString);

            if (doesFit) {
                parsedProof.add(parsedString);
                continue;
            }

            // Modus Ponens:
            concatIt = parsedProof.listIterator();
            mainLoop:
            while (concatIt.hasNext()) {
                requiredString = "(" + concatIt.next() + "->" + parsedString + ")";
                compareIt = parsedProof.listIterator();
                while (compareIt.hasNext()) {
                    // Can compare by "hashCode"
                    if (requiredString.equals(compareIt.next())) {
                        doesFit = true;
                        break mainLoop;
                    }
                }
            }

            if (doesFit) {
                parsedProof.add(parsedString);
            } else {
                break;
            }

        }

        if (doesFit) {
            out.print("Доказательство корректно.");
        }

        else {
            out.print("Доказательство некорректно начиная с высказывания № " + stringCounter + ".");
        }
    }

    public void run() {
        try {
            in = new FastScanner(new File("isproof.in"));
            out = new PrintWriter(new File("isproof.out"));

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
