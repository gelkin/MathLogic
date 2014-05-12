package com.gmail.mazinva.mathlogic;

import java.io.*;
import java.util.StringTokenizer;

public class FastScanner {

    private BufferedReader br;
    private StringTokenizer st;
    private String line;

    public FastScanner(File f) {
        try {
            br = new BufferedReader(new FileReader(f));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    public String next() {
        try {
            if (st == null || !st.hasMoreTokens()) {
                if ((line = br.readLine()) != null) {
                    st = new StringTokenizer(line);
                } else {
                    return null;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return st.nextToken();
    }

    public String nextLine() {
        try {
            if ((line = br.readLine()) != null) {
                return line;
            } else {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
