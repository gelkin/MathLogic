package com.gmail.mazinva.StatementsProving;

import com.gmail.mazinva.MathLogic.Expression;
import com.gmail.mazinva.MathLogic.MathLogicException;

import java.util.*;

public class StatementsProving {

    private static final int START_INDEX = 0;
    private static final String FALSE_STATEMENT_MSG = "Высказывание ложно при ";

    // List of proposinal variables in "phi"
    public static Map<String, Boolean> vars = new LinkedHashMap<String, Boolean>();
    // Needed for convenient access to variables via indexes
    public static List<String> varByIndex = new ArrayList<String>();

    // Proof of "phi", returned in getProof()
    private List<String> globalProof;
    private Expression phi;
    private boolean isStatementTrue = true;

    public StatementsProving(Expression phi) {
        globalProof = new ArrayList<String>();
        this.phi = phi;
    }
    
    public List<String> getProof() throws MathLogicException {
        setIsStatementTrue(START_INDEX);
        if (!isStatementTrue) return globalProof;
        getProofForAllValues(START_INDEX);
        cutConditions();
        return globalProof;
    }

    public boolean isStatementTrue() {
        return isStatementTrue;
    }

    private void setIsStatementTrue(int k) {
        if (!isStatementTrue) return;

        if (k < vars.size()) {
            vars.put(varByIndex.get(k), true);
            setIsStatementTrue(k + 1);
            vars.put(varByIndex.get(k), false);
            setIsStatementTrue(k + 1);
        } else {
            if (!phi.isTrue()) {
                String msg = FALSE_STATEMENT_MSG;
                Iterator<String> it = vars.keySet().iterator();
                String s;
                if (it.hasNext()) {
                    s = it.next();
                    msg += (vars.get(s))? s + " = И": s + " = Л";
                }
                while (it.hasNext()) {
                    s = it.next();
                    msg += ", ";
                    msg += (vars.get(s))? s + " = И": s + " = Л";
                }

                globalProof.add(msg);
                isStatementTrue = false;
            }
        }
    }

    // Get proof for all possible values of variables
    private void getProofForAllValues(int k) throws MathLogicException {
        if (k < vars.size()) {
            vars.put(varByIndex.get(k), true);
            getProofForAllValues(k + 1);
            vars.put(varByIndex.get(k), false);
            getProofForAllValues(k + 1);
        } else {
            getProofForCurrentValues();
        }
    }

    private void getProofForCurrentValues() throws MathLogicException {
        List<String> proof = phi.getProof();
        // values == variables [x]A, where [x] == '!' if vars.get(A) == false;
        List<String> conditions = getCurrentValues(vars.keySet());
        globalProof.addAll(DeductionTheorem.applyDedTheorem(conditions, proof));
    }

    private List<String> getCurrentValues(Collection<String> strings) {
        List<String> newStrings = new ArrayList<String>(strings.size());
        Iterator<String> it = strings.iterator();
        String s;
        while (it.hasNext()) {
            s = it.next();
            if (!vars.get(s)) s = '!' + s;
            newStrings.add(s);
        }
        return newStrings;
    }

    private int cutConditionIndex;
    private void cutConditions() throws MathLogicException {
        cutConditionIndex = 0;
        for (int i = 0; i < varByIndex.size(); i++) {
            cutConditionIndex = i;
            cutConditionByIndex(i);
        }
    }

    private void cutConditionByIndex(int i) throws MathLogicException {
        if (i < vars.size()) {
            vars.put(varByIndex.get(i), true);
            cutConditionByIndex(i + 1);
            vars.put(varByIndex.get(i), false);
            cutConditionByIndex(i + 1);
        } else {
            cutConditionByIndexForFixedValues();
        }
    }

    private void cutConditionByIndexForFixedValues() throws MathLogicException {
        String alpha;
        String s;

        s = phi.toString();
        alpha = varByIndex.get(cutConditionIndex);
        for (int i = varByIndex.size() - 1; i > cutConditionIndex; i--) {
            if (vars.get(varByIndex.get(i))) {
                s = (varByIndex.get(i) + "->" + s);
            } else {
                s = ("!" + varByIndex.get(i) + "->" + s);
            }
        }

        globalProof.addAll(LogConnects.loadProof("A|!A", alpha));
        globalProof.add("(" + alpha + "|!" + alpha + ")");
        globalProof.add("(" + alpha + "->"  + s + ")->" +
                                "(!" + alpha + "->" + s + ")->" +
                                "((" + alpha + "|!" + alpha + ")->" + s + ")");

        globalProof.add("(!" + alpha + "->" + s + ")->" +
                                "((" + alpha + "|!" + alpha + ")->" + s + ")");
        globalProof.add("((" + alpha + "|!" + alpha + ")->" + s + ")");
        globalProof.add(s);
    }
}
