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

@SuppressWarnings("ALL")
public class BankAccount {
    public static final String NOTES = "notes";
    public static final String ACCT_ID = "account_id";
    public static final String STATEMENTS = "statements";
    public static final String AMOUNT = "amount";
    public static final String DATE = "date";

    private String accountId;
    private String notes;
    private final Map<String, Double> statements = new HashMap<>();

    public BankAccount(){}

    public BankAccount(BankAccount ba) {
        accountId = ba.accountId;
        notes = ba.notes;
        for(String cd: ba.statements.keySet()){
            statements.put(cd, ba.statements.get(cd));
        }
    }

    public BankAccount(JSONObject jsonObject){
        this();
        readJSON(jsonObject);
    }

    public void addStatement(CustomDate cal, double amount){
        statements.put(cal.toString(), amount);
    }
    public void removeStatement(CustomDate cal){
        statements.remove(cal);
    }
    public double getStatement(CustomDate cal){
        if(statements.containsKey(cal.toString())) return statements.get(cal.toString());
        return 0;
    }
    public Map<String, Double> getStatements() {return statements;}
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
                CustomDate cal = new CustomDate(j.getString(DATE));
                statements.put(cal.toString(), j.getDouble(AMOUNT));
            }

        } catch (JSONException | CustomDate.DateErrorException e) {
            e.printStackTrace();
        }
    }

    public JSONObject toJSON(){
        JSONObject j = new JSONObject();
        try {
            j.put(NOTES, notes);
            j.put(ACCT_ID, accountId);
            JSONArray jsonArray = new JSONArray();
            ArrayList<String> gc = new ArrayList<>(statements.keySet());
            Collections.sort(gc);
            for(String cal: gc){
                JSONObject temp = new JSONObject();
                temp.put(DATE, cal);
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
