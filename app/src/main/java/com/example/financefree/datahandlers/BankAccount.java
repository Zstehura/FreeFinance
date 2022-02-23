package com.example.financefree.datahandlers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;


/*
 Class contains single account

 */

public class BankAccount {
    public static final String NOTES = "notes";
    public static final String ACCT_ID = "account_id";
    public static final String STATEMENTS = "statements";
    public static final String AMOUNT = "amount";
    public static final String DATE = "date";

    private String accountId;
    private String notes;
    private final Map<GregorianCalendar, Double> statements = new HashMap<>();

    public BankAccount(){}

    public void addStatement(GregorianCalendar cal, double amount){
        statements.put(cal, amount);
    }
    public void removeStatement(GregorianCalendar cal){
        statements.remove(cal);
    }
    public double getStatement(GregorianCalendar cal){
        if(statements.containsKey(cal)) return statements.get(cal);
        return 0;
    }
    public Map<GregorianCalendar, Double> getStatements() {return statements;}
    public void setAccountId(String accountId){this.accountId = accountId;}
    public void setNotes(String notes) {this.notes = notes;}
    public String getAccountId(){return accountId;}
    public String getNotes(){return notes;}


    public void readJSON(JSONObject jsonObject){
        try {
            notes = jsonObject.getString(NOTES);
            accountId = jsonObject.getString(ACCT_ID);
            JSONArray jsonArray = jsonObject.getJSONArray(STATEMENTS);
            for(int i = 0; i < jsonArray.length(); i++){
                JSONObject j = jsonArray.getJSONObject(i);
                GregorianCalendar cal = DateStringer.StringToCal(j.getString(DATE));
                statements.put(cal, j.getDouble(AMOUNT));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public JSONObject toJSON(){
        JSONObject j = new JSONObject();
        try {
            j.put(NOTES, notes);
            j.put(ACCT_ID, accountId);
            JSONArray jsonArray = new JSONArray();
            ArrayList<GregorianCalendar> gc = new ArrayList<>();
            gc.addAll(statements.keySet());
            Collections.sort(gc);
            for(GregorianCalendar cal: gc){
                JSONObject temp = new JSONObject();
                temp.put(DATE, DateStringer.CalToString(cal));
                temp.put(AMOUNT, statements.get(cal));
                jsonArray.put(temp);
            }
            j.put(STATEMENTS, jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return j;
    }

}
