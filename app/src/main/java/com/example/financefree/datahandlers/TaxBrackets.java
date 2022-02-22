package com.example.financefree.datahandlers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class TaxBrackets {
    public static final String YEAR = "year";
    public static final String SINGLE = "single";
    public static final String HEAD_OF_HOUSE = "head_of_household";
    public static final String MARRIED_JOINT = "married_joint";
    public static final String MARRIED_SEP = "married_separate";
    public static final String UPPER_LIMIT = "upper_limit";
    public static final String LOWER_LIMIT = "lower_limit";
    public static final String PERCENTAGE = "percentage";

    public class Bracket{
        private final Vector<Double> ul;
        private final Vector<Double> ll;
        private final Vector<Double> p;

        public Bracket(JSONArray jsonArray) {
            ul = new Vector<>(); ll = new Vector<>(); p = new Vector<>();
            for(int i = 0; i < jsonArray.length(); i++) {
                try {
                    JSONObject temp = jsonArray.getJSONObject(i);
                    ul.add(temp.getDouble(UPPER_LIMIT));
                    ll.add(temp.getDouble(LOWER_LIMIT));
                    p.add(temp.getDouble(PERCENTAGE));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        public int length(){return ul.size();}
        public double getUpperLim(int i){return ul.get(i);}
        public double getLowerLim(int i){return ll.get(i);}
        public double getPct(int i){return p.get(i);}
    }

    private int year;
    private final Map<String, Bracket> bracket = new HashMap<>();

    public TaxBrackets(){}

    public void readJSON(JSONObject jsonObject) throws JSONException {
        year = jsonObject.getInt(YEAR);
        bracket.put(SINGLE, new Bracket(jsonObject.getJSONArray(SINGLE)));
        bracket.put(MARRIED_SEP, new Bracket(jsonObject.getJSONArray(MARRIED_SEP)));
        bracket.put(MARRIED_JOINT, new Bracket(jsonObject.getJSONArray(MARRIED_JOINT)));
        bracket.put(HEAD_OF_HOUSE, new Bracket(jsonObject.getJSONArray(HEAD_OF_HOUSE)));
    }

    public Bracket getBracket(String type) {
        return bracket.get(type);
    }
    public int getYear(){return year;}
}
