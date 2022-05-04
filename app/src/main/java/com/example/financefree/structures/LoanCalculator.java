package com.example.financefree.structures;

import android.util.Log;

import java.util.GregorianCalendar;

public class LoanCalculator {

    // TODO: create tests
    //      write more options
    //      create fragment
    //      allow user to adjust settings
    //      already added in the chart library (https://weeklycoding.com/mpandroidchart/)

    private double principle;
    private int numMonths;
    private double apr;
    private double payment;

    public LoanCalculator(double principle, int numMonths,
                          double apr, double payment) {
        this.principle = principle;
        this.apr = apr;
        this.numMonths = numMonths;
        this.payment = payment;
    }

    public LoanCalculator() {
        this(25000, 48, 0.03125, 250);
    }

    public void setPrinciple(double principle){this.principle = principle;}
    public void setApr(double apr) { this.apr = apr; }
    public void setNumMonths(int numMonths) { this.numMonths = numMonths; }
    public void setPayment(double payment) { this.payment = payment; }
    public void setNumMonths(long startDate, long endDate) {
        long d = endDate - startDate;
        double y = d / 365d;
        numMonths = (int) Math.round(y * 12);
    }

    public double getBalanceAtMonth(int monthNum) {
        if(monthNum == numMonths) return 0;
        double bal = principle;

        for(int i = 0; i < monthNum; i++) {
            bal *= (1 + (apr / 12));
            bal -= payment;
        }
        return roundNum(bal);
    }

    public int calcTermLen() {
        // Formula:     log[ (PMT/i) / {(PMT/i) - PV ]
        //          n =   -------------------------
        //                      log(1 + i)

        if(payment < (principle * (apr / 12)))
            return -1;

        double pmt = payment;
        double pv = principle;
        double i = apr / 12;

        double t1 = Math.log((pmt / i) / ((pmt/i) - pv));
        double t2 = Math.log(1 + i);
        double nm = (t1 / t2);
        numMonths = (int) Math.round(nm);
        return numMonths;
    }

    public double calcPrinciple() {
        // Formula =>   PV = (PMT/i) * [1 - (1 / (1+i)^n]
        //                      t1          t2
        double pmt = payment;
        double i = apr / 12;
        double n = numMonths;
        double t1 = pmt / i;
        double t2 = 1 - (1 / Math.pow(1+i, n));
        principle = t1 * t2;
        principle = roundNum(principle);
        return principle;
    }

    public double calcPayment() {
        // Formula =>   PMT = [PV*i * (1+i)^n] / [(1+i)^n - 1]
        //                             t1                   t2
        //  PV = Principle Value
        //  PMT = Payment
        //  r = apr / termsPerYear
        //  i = Total payments (terms * termsPerYear)
        double pv = principle;
        double i = apr / 12;
        double n = numMonths;
        double t1 = pv * i * Math.pow(1+i, n);
        double t2 = Math.pow(1+i, n) - 1;
        payment = t1 / t2;
        payment = roundNum(payment);
        return payment;
    }

    private double roundNum(double num) {
        double n = num * 100;
        n = Math.round(n);
        n /= 100;
        return n;
    }

}
