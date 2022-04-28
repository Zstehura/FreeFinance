package com.example.financefree.structures;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings({"ConstantConditions", "SpellCheckingInspection"})
public class TaxYear {
    public static class YearNotFoundException extends Exception {}
    public static class FilingNotFoundException extends Exception {}

    public static final int FIRST_YEAR_AVAILABLE = 2006;
    public static final int LAST_YEAR_AVAILABLE = 2021;

    private static final String BRACKET_FILE_NAME = "income_brackets.csv";
    private static final String STANDARD_DEDUCTION_FILE_NAME = "standard_deductions.csv";
    private static final List<String> BRACKET_CONTENTS = new ArrayList<>();
    private static final List<String> DEDUCTION_CONTENTS = new ArrayList<>();
    public static final List<String> FILING_STATUS = new ArrayList<>(Arrays.asList("married-joint","married-separate","single","head-of-house"));

    private final Map<String, Double> stdDeduction = new HashMap<>();   // goes before tax
    private final Map<String, List<Bracket>> brackets = new HashMap<>();

    private double income;
    private boolean usingStdDeduction;
    private List<Double> deductions = new ArrayList<>();
    private List<Double> credits = new ArrayList<>();
    private int year;
    private String fileAs;

    public TaxYear(int year, Context context) throws IOException, YearNotFoundException {
        // get tax file
        this.year = year;
        usingStdDeduction = true;
        if(BRACKET_CONTENTS.size() == 0) readFiles(context);

        // initialize class variables
        for(String s: FILING_STATUS){
            brackets.put(s, new ArrayList<>());
        }
        boolean yearFound = false;

        // order goes: joint, separate, single, hoh
        for (int i = 0; i < BRACKET_CONTENTS.size(); i++){
            String temp = BRACKET_CONTENTS.get(i).substring(0,4);
            if(BRACKET_CONTENTS.get(i).substring(0,4).equals(String.valueOf(year))){
                // found it
                yearFound = true;
                int lineNumber = i;
                while(!BRACKET_CONTENTS.get(lineNumber).equals(",,,,,")){
                    List<String> nums = new ArrayList<>(Arrays.asList(BRACKET_CONTENTS.get(lineNumber).split(",")));
                    List<String> nextNums = new ArrayList<>(Arrays.asList(BRACKET_CONTENTS.get(lineNumber + 1).split(",")));
                    for(int n = 0; n < FILING_STATUS.size(); n++){
                        double ll,ul,r;
                        ll = Double.parseDouble(nums.get(2 + n)) + 1;
                        if(ll == 1) ll = 0;
                        r = Double.parseDouble(nums.get(1));
                        if(nextNums.size() == 0){
                            ul = -1;
                        }
                        else{
                            ul = Double.parseDouble(nextNums.get(2+n));
                        }
                        Bracket b = new Bracket(ll, ul, r);
                        brackets.get(FILING_STATUS.get(n)).add(b);
                    }
                    lineNumber++;
                }
            }
        }

        if(!yearFound) throw new YearNotFoundException();
        yearFound = false;

        for(int i = 0; i < DEDUCTION_CONTENTS.size(); i++) {
            if(DEDUCTION_CONTENTS.get(i).substring(0,4).equals(String.valueOf(year))) {
                yearFound = true;
                List<String> heads = new ArrayList<>(Arrays.asList(DEDUCTION_CONTENTS.get(0).split(",")));
                List<String> nums = new ArrayList<>(Arrays.asList(DEDUCTION_CONTENTS.get(i).split(",")));
                for(int j = 1; j < nums.size(); j++){
                    stdDeduction.put(heads.get(j), Double.parseDouble(nums.get(j)));
                }
            }
        }

        if(!yearFound) {
            throw new YearNotFoundException();
        }
    }

    private void readFiles(Context context) throws IOException {
        InputStream isb = context.getAssets().open(BRACKET_FILE_NAME);
        InputStream isd = context.getAssets().open(STANDARD_DEDUCTION_FILE_NAME);
        BufferedReader brb = new BufferedReader(new InputStreamReader(isb));
        BufferedReader brd = new BufferedReader(new InputStreamReader(isd));
        String s = brb.readLine();
        while (s != null){
            BRACKET_CONTENTS.add(s);
            s = brb.readLine();
        }
        s = brd.readLine();
        while (s != null){
            DEDUCTION_CONTENTS.add(s);
            s = brd.readLine();
        }

        isb.close(); isd.close();
    }

    public double getTaxNoCreds() throws FilingNotFoundException {
        double tax = 0;
        if(!brackets.containsKey(fileAs)) throw new FilingNotFoundException();
        double iActual = income - getDeductionSum();

        for(Bracket b: brackets.get(fileAs)){
            if(iActual > b.lowerLim){
                if(b.upperLim == -1 || iActual < b.upperLim){
                    tax += (iActual - b.lowerLim) * b.rate;
                }
                else if(iActual >= b.upperLim) {
                    tax += b.getMaxAmnt();
                }
            }
        }
        if(tax > 0) return roundNum(tax);
        else return 0;
    }

    public double getTax() throws FilingNotFoundException {
        double tax = getTaxNoCreds();
        tax -= getCreditSum();
        if(tax > 0) {
            return roundNum(tax);
        }
        else return 0;
    }

    private double roundNum(double num) {
        double t1 = num * 100;
        long t2 = Math.round(t1);
        return ((double) t2 / 100);
    }

    public double getStdDeduction() { return stdDeduction.get(fileAs); }

    public double getDeductionSum() {
        double iActual = 0;
        if(!usingStdDeduction) {
            for(double d: deductions){ iActual += d;}
        }
        else{
            iActual += stdDeduction.get(fileAs);
        }
        return iActual;
    }
    public double getIncome(){ return income;}
    public double getCreditSum(){
        double c = 0;
        if(credits.size() > 0){
            for(double d: credits) {c += d;}
        }
        return c;
    }

    public boolean isUsingStdDeduction() {return usingStdDeduction;}

    public void setUsingStdDeduction(boolean usingStdDeduction) { this.usingStdDeduction = usingStdDeduction; }
    public void setCredits(List<Double> credits) { this.credits = credits; }
    public void setDeductions(List<Double> deductions) { this.deductions = deductions; }
    public void setFileAs(String fileAs) { this.fileAs = fileAs; }
    public void setIncome(double income) { this.income = income; }
    public void setYear(int year) { this.year = year; }

    public int getYear() {return year;}

    private static class Bracket {
        public Bracket(double lowerLim, double upperLim, double rate){
            this.lowerLim = lowerLim;
            this.upperLim = upperLim;
            this.rate = rate;
        }
        double lowerLim;
        double upperLim;
        double rate;

        public double getMaxAmnt(){
            if(upperLim < 0) return 0;
            return (upperLim - lowerLim) * rate;
        }
    }
}
