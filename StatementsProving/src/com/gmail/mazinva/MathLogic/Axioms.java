package com.gmail.mazinva.MathLogic;

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

    public static boolean doesMatchAxioms(String expression) {
        Matcher matcher;
        for (int i = 0; i < listOfAxioms.length; i++) {
            matcher = listOfAxioms[i].matcher(expression);
            if (matcher.matches()) {
                // System.out.println("Axiom #" + (i + 1));
                return true;
            }
        }
        return false;
    }
}
