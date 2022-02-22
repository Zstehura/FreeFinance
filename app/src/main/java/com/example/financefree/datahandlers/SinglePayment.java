package com.example.financefree.datahandlers;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class SinglePayment {
    public static final String AMOUNT = "amount";
    public static final String DATE = "date";
    public static final String NAME = "name";
    public static final String BANK_ID = "bank_id";
    public static final String NOTES = "notes";

    private double amount = 0;
    private GregorianCalendar date = new GregorianCalendar();
    private String bankId = "";
    private String name = "";
    private String notes = "";

    public SinglePayment(){}

    public void setAmount(double amount){this.amount = amount;}
    public void setDate(GregorianCalendar date) {this.date = date;}
    public void setName(String name) {this.name = name;}
    public void setNotes(String notes) {this.notes = notes;}
    public void setBankId(String bankId){this.bankId = bankId;}
    public double getAmount() {return amount;}
    public GregorianCalendar getDate() {return date;}
    public String getName(){return name;}
    public String getBankId(){return bankId;}
    public String getNotes(){return notes;}

    public void readJSON(JSONObject jsonObject) throws JSONException {
        amount = jsonObject.getDouble(AMOUNT);
        date = DateStringer.StringToCal(jsonObject.getString(DATE));
        name = jsonObject.getString(NAME);
        bankId = jsonObject.getString(BANK_ID);
        notes = jsonObject.getString(NOTES);
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject j = new JSONObject();
        j.put(AMOUNT,amount);
        j.put(DATE,DateStringer.CalToString(date));
        j.put(NAME,name);
        j.put(BANK_ID, bankId);
        j.put(NOTES,notes);
        return j;
    }

}
