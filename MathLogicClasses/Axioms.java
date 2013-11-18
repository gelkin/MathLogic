package com.gmail.mazinva.MathLogic;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Axioms {
    private static Pattern[] listOfAxioms = {
        /* 1 */ Pattern.compile(
            "^[\\(]{2}([A-Z|\\->|&|\\||!|\\(|\\)]+)\\)->[\\(]{2}([A-Z|\\->|&|\\||!|\\(|\\)]+)\\)->\\(\\1[\\)]{3}$"),
        /* 2 */ Pattern.compile(
            "^[\\(]{3}([A-Z|\\->|&|\\||!|\\(|\\)]+)\\)->\\(([A-Z|\\->|&|\\||!|\\(|\\)]+)[\\)]{2}->[\\(]{3}\\1\\)->" +
                    "[\\(]{2}\\2\\)->\\(([A-Z|\\->|&|\\||!|\\(|\\)]+)[\\)]{3}->[\\(]{2}\\1\\)->\\(\\3[\\)]{4}$"),
        /* 3 */ Pattern.compile(
            "^[\\(]{2}([A-Z|\\->|&|\\||!|\\(|\\)]+)\\)->[\\(]{2}([A-Z|\\->|&|\\||!|\\(|\\)]+)\\)->[\\(]{2}\\1\\)&\\(\\2[\\)]{4}$"),
        /* 4 */ Pattern.compile(
            "^[\\(]{3}([A-Z|\\->|&|\\||!|\\(|\\)]+)\\)&\\(([A-Z|\\->|&|\\||!|\\(|\\)]+)[\\)]{2}->\\(\\1[\\)]{2}$"),
        /* 5 */ Pattern.compile(
            "^[\\(]{3}([A-Z|\\->|&|\\||!|\\(|\\)]+)\\)&\\(([A-Z|\\->|&|\\||!|\\(|\\)]+)[\\)]{2}->\\(\\2[\\)]{2}$"),
        /* 6 */ Pattern.compile(
            "^[\\(]{2}([A-Z|\\->|&|\\||!|\\(|\\)]+)\\)->[\\(]{2}\\1\\)\\|\\(([A-Z|\\->|&|\\||!|\\(|\\)]+)[\\)]{3}$"),
        /* 7 */ Pattern.compile(
            "^[\\(]{2}([A-Z|\\->|&|\\||!|\\(|\\)]+)\\)->[\\(]{2}([A-Z|\\->|&|\\||!|\\(|\\)]+)\\)\\|\\(\\1[\\)]{3}$"),
        /* 8 */ Pattern.compile(
            "^[\\(]{3}([A-Z|\\->|&|\\||!|\\(|\\)]+)\\)->\\(([A-Z|\\->|&|\\||!|\\(|\\)]+)[\\)]{2}->[\\(]{3}([A-Z|\\->|&|\\||!|\\(|\\)]+)\\)" +
                    "->\\(\\2[\\)]{2}->[\\(]{3}\\1\\)\\|\\(\\3[\\)]{2}->\\(\\2[\\)]{4}$"),
        /* 9 */ Pattern.compile(
            "^[\\(]{3}([A-Z|\\->|&|\\||!|\\(|\\)]+)\\)->\\(([A-Z|\\->|&|\\||!|\\(|\\)]+)[\\)]{2}->[\\(]{3}\\1\\)" +
                    "->[\\(]{2}!\\(\\2[\\)]{4}->[\\(]{2}!\\(\\1[\\)]{4}$"),
        /* 10 */ Pattern.compile("^[\\(]{2}!\\(!\\(([A-Z|\\->|&|\\||!|\\(|\\)]+)[\\)]{3}->\\(\\1[\\)]{2}$")
    };

    public static boolean doesMatchAxioms(String expression) {
        Matcher matcher;
        for (int i = 0; i < listOfAxioms.length; i++) {
            matcher = listOfAxioms[i].matcher(expression);
            if (matcher.matches()) {
                return true;
            }
        }
        return false;
    }

}