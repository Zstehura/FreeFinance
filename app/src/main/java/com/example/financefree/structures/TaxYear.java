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

    private static final String TAX_FILE_NAME = "taxes.csv";
    private static final List<String> FILE_CONTENTS = new ArrayList<>();
    public static final List<String> TAX_TAGS = new ArrayList<>();

    private final Map<String, List<Bracket>> brackets = new HashMap<>();
    private final int year;

    public TaxYear(int year, Context context) throws IOException, YearNotFoundException {
        // get tax file
        if(FILE_CONTENTS.size() == 0){
            InputStream is = context.getAssets().open(TAX_FILE_NAME);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String s = br.readLine();
            for(String text: s.split(",")){
                if(!text.equals("")) TAX_TAGS.add(text);
            }

            while (s != null){
                FILE_CONTENTS.add(s);
                s = br.readLine();
            }
        }

        // initialize class variables
        this.year = year;
        for(String s: TAX_TAGS){
            brackets.put(s, new ArrayList<>());
        }
        boolean yearFound = false;

        // order goes: joint, separate, single, hoh
        for (int i = 0; i < FILE_CONTENTS.size(); i++){
            if(FILE_CONTENTS.get(i).substring(0,4).equals(String.valueOf(year))){
                // found it
                yearFound = true;
                int lineNumber = i;
                while(!FILE_CONTENTS.get(lineNumber).equals(",,,,,")){
                    List<String> nums = new ArrayList<>(Arrays.asList(FILE_CONTENTS.get(lineNumber).split(",")));
                    List<String> nextNums = new ArrayList<>(Arrays.asList(FILE_CONTENTS.get(lineNumber).split(",")));
                    for(int n = 0; n < TAX_TAGS.size(); n++){
                        double ll,ul,r;
                        ll = Double.parseDouble(nums.get(2 + n)) + 1;
                        if(ll == 1) ll = 0;
                        r = Double.parseDouble(nums.get(1));
                        if(nextNums.get(1).equals("")){
                            ul = -1;
                        }
                        else{
                            ul = Double.parseDouble(nextNums.get(2+n));
                        }
                        Bracket b = new Bracket(ll, ul, r);
                        brackets.get(TAX_TAGS.get(n)).add(b);
                    }
                    lineNumber++;
                }

            }
        }

        if(!yearFound) {
            throw new YearNotFoundException();
        }
    }

    public double getTaxOn(String fileAs, double income) throws FilingNotFoundException {
        double tax = 0;
        if(!brackets.containsKey(fileAs)) throw new FilingNotFoundException();
        for(Bracket b: brackets.get(fileAs)){
            if(income > b.lowerLim){
                if(b.upperLim == -1 || income < b.upperLim){
                    tax += (income - b.lowerLim) * b.rate;
                }
                else if(income >= b.upperLim) {
                    tax += b.getMaxAmnt();
                }
            }
        }

        return tax;
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
