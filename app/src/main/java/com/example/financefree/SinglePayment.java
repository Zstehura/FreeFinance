package com.example.financefree;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class SinglePayment {
    public static final String AMOUNT = "amount";
    public static final String DATE = "date";
    public static final String NAME = "name";
    public static final String NOTES = "notes";

    private double amount;
    private GregorianCalendar date;
    private String name;
    private String notes;

    public SinglePayment(){}

    public void readJSON(JSONObject jsonObject) {
        try {
            amount = jsonObject.getDouble(AMOUNT);
            String[] temp = jsonObject.getString(DATE).split("/");
            date = new GregorianCalendar();
            date.set(Calendar.MONTH, Integer.parseInt(temp[0]));
            date.set(Calendar.DAY_OF_MONTH, Integer.parseInt(temp[1]));
            date.set(Calendar.YEAR, 2000 + Integer.parseInt(temp[2]));
            name = jsonObject.getString(NAME);
            notes = jsonObject.getString(NOTES);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public JSONObject toJSON() {
        JSONObject j = new JSONObject();
        try {
            j.put(AMOUNT,amount);
            String temp = date.get(Calendar.MONTH) + "/" + date.get(Calendar.DAY_OF_MONTH) + "/" + (date.get(Calendar.YEAR)-2000);
            j.put(DATE,date.get(Calendar.MONTH));
            j.put(NAME,name);
            j.put(NOTES,notes);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return j;
    }


}
