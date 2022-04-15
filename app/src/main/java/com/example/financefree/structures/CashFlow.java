package com.example.financefree.structures;

import java.util.ArrayList;
import java.util.List;

public class CashFlow {
    public static final String CASH_IN = "I'm being paid";
    public static final String CASH_OUT = "I'm paying them";
    private static final List<String> CHOICES = new ArrayList<>();
    static {
        CHOICES.add(CASH_IN);
        CHOICES.add(CASH_OUT);
    }

    private boolean cashIn;
    private double flow;

    public CashFlow(){
        cashIn = true;
        flow = 0;
    }
    public CashFlow(Payment p){
        setFlowMag(p.amount);
        cashIn = (p.amount >= 0);
    }

    public void setFlowMag(double num) {flow = Math.abs(num);}
    public double getFlow() {
        if(cashIn) return flow;
        else return (flow * -1);
    }
    public static List<String> getChoices() {return CHOICES;}
    public void setCashIn() {cashIn = true;}
    public void setCashOut() {cashIn = false;}
    public boolean getCashIn() {return cashIn;}
    public boolean getCashOut() {return !cashIn;}

    public void setCashFlow(int i){
        if(i >= 0 && i < CHOICES.size()) {
            cashIn = CHOICES.get(i).equals(CASH_IN);
        }
    }
}
