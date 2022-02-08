package com.example.financefree;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

//TODO: CHANGE DATE ALGORITHM

public class BankStatement {
    public static final String AMOUNT = "amount";
    public static final String BANK_ID = "bank_id";
    public static final String DATE = "date";

    private double amount;
    private String bankId;
    private GregorianCalendar date;

    public BankStatement(){}

    public void readJSON(JSONObject jsonObject){
        try {
            amount = jsonObject.getDouble(AMOUNT);
            bankId = jsonObject.getString(BANK_ID);
            String[] temp = jsonObject.getString(DATE).split("/");
            date = new GregorianCalendar();
            date.set(Calendar.MONTH, Integer.parseInt(temp[0]));
            date.set(Calendar.DAY_OF_MONTH,Integer.parseInt(temp[1]));
            date.set(Calendar.YEAR,Integer.parseInt(temp[2]) + 2000);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public JSONObject toJSON(){
        JSONObject j = new JSONObject();
        try {
            j.put(AMOUNT,amount);
            j.put(BANK_ID,bankId);
            String temp = date.get(Calendar.MONTH) + "/" + date.get(Calendar.DAY_OF_MONTH) + "/" + (date.get(Calendar.YEAR)+2000);
            j.put(DATE,temp);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return j;
    }

}
