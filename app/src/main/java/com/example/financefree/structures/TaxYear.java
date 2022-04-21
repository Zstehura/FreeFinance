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

// TODO: Create tests for this class
//          write tax relevant functions in DB mgr

@SuppressWarnings({"ConstantConditions", "SpellCheckingInspection"})
public class TaxYear {
    public static class YearNotFoundException extends Exception {}
    public static class FilingNotFoundException extends Exception {}

    private static final String BRACKET_FILE_NAME = "income_brackets.csv";
    private static final String STANDARD_DEDUCTION_FILE_NAME = "standard_deductions.csv";
    private static final List<String> BRACKET_CONTENTS = new ArrayList<>();
    private static final List<String> DEDUCTION_CONTENTS = new ArrayList<>();
    public static final List<String> FILING_STATUS = new ArrayList<>(Arrays.asList("married-joint","married-separate","single","head-of-house"));

    private final Map<String, Double> stdDeduction = new HashMap<>();   // goes before tax
    private final Map<String, List<Bracket>> brackets = new HashMap<>();

    private final int year;

    public TaxYear(int year, Context context) throws IOException, YearNotFoundException {
        // get tax file
        this.year = year;
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

    public double getTaxOn(String fileAs, double income) throws FilingNotFoundException {
        double tax = 0;
        if(!brackets.containsKey(fileAs)) throw new FilingNotFoundException();
        double iActual = income - stdDeduction.get(fileAs);
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
        if(tax > 0) {
            tax *= 100;
            long temp = Math.round(tax);
            tax = ((double) temp / 100);
            return tax;
        }
        else return 0;
    }

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
