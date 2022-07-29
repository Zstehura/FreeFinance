package com.example.financefree;

import java.util.Objects;

public class MainCalculator {
    public static final int DEC =           -1;
    public static final int NONE =          10;
    public static final int ADD =           11;
    public static final int SUBTRACT =      12;
    public static final int DIVIDE =        13;
    public static final int MULTIPLY =      14;
    public static final int EXPONENT =      15;
    public static final int INVERT =        16;
    public static final int SQUARE_ROOT =   17;
    public static final int ERROR =         -10;
    public static final int EQUALS =        20;
    public static final int CLEAR =         21;

    private String strPrevEq = "";
    private String strNum1 = "";
    private String strNum2 = "";
    private int op = NONE;
    private String strAns = "";
    private String strPrevAns = "";

    public MainCalculator() {}

    public String getPrevEq() {return strPrevEq;}

    public String getAnswer() {return strAns;}

    public String getCurrentStatement() {
        if(strNum1.equals("")) return "0";
        if(op == ERROR) return "ERROR";

        String s = strNum1;
        if(op != NONE) {
            if(op == ADD) s += "+";
            else if(op == SUBTRACT) s += "-";
            else if(op == DIVIDE) s += "/";
            else if(op == MULTIPLY) s += "x";
            else if(op == EXPONENT) s += "^";
            else if(op == INVERT) s = "INV(" + s + ")";
            else if(op == SQUARE_ROOT) s = "SQRT(" + s + ")";
        }
        s += strNum2;

        return s;
    }

    public boolean send(int i) {
        if(i < 10) {
            if(i == DEC) addToNum(".");
            else addToNum(String.valueOf(i));
        }
        else if(i < 20) {
            setOp(i);
        }
        else if(i == EQUALS) {
            calc();
        }
        else if(i == CLEAR) {
            reset(true);
        }
        return op == ERROR || i == EQUALS || i == SQUARE_ROOT || i == INVERT;
    }

    private void setError() {
        strAns = "ERROR";
        op = ERROR;
    }

    private void addToNum(String s) {
        if(op != ERROR) {
            if(op == NONE) {
                strNum1 += s;
            }
            else {
                strNum2 += s;
            }
        }
    }

    private void reset(boolean hardReset) {
        if(!hardReset) {
            strPrevEq = getCurrentStatement();
            strPrevAns = strAns;
        }
        strNum1 = "";
        strNum2 = "";
        op = NONE;
        //strAns = "";
    }

    private void calc() {
        if(op == NONE || strNum1.equals("")) setError();
        else if(op != INVERT && op != SQUARE_ROOT && strNum2.equals("")) setError();

        double num1 = 0, num2 = 0;
        try {
            num1 = Double.parseDouble(strNum1);
            if(op != INVERT && op != SQUARE_ROOT)
                num2 = Double.parseDouble(strNum2);
        }
        catch (NumberFormatException e) {
            setError();
        }

        if(op == ADD) {
            strAns = String.valueOf(num1 + num2);
        }
        else if(op == SUBTRACT) {
            strAns = String.valueOf(num1 - num2);
        }
        else if(op == DIVIDE) {
            strAns = String.valueOf(num1 / num2);
        }
        else if(op == MULTIPLY) {
            strAns = String.valueOf(num1 * num2);
        }
        else if(op == EXPONENT) {
            strAns = String.valueOf(Math.pow(num1, num2));
        }
        else if(op == INVERT) {
            strAns = String.valueOf(1d / num1);
        }
        else if(op == SQUARE_ROOT) {
            strAns = String.valueOf(Math.sqrt(num1));
        }

        reset(false);
    }

    private void setOp(int i) {
        if(Objects.equals(strNum1, "")) strNum1 = strPrevAns;
        op = i;
        if(i == SQUARE_ROOT || i == INVERT) calc();
    }

}
