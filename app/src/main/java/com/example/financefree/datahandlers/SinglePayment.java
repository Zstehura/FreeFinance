package com.example.financefree.datahandlers;

import com.example.financefree.structures.CustomDate;

import org.json.JSONException;
import org.json.JSONObject;

public class SinglePayment {
    public static final String AMOUNT = "amount";
    public static final String DATE = "date";
    public static final String NAME = "name";
    public static final String BANK_ID = "bank_id";
    public static final String NOTES = "notes";

    private double amount = 0;
    private CustomDate date = new CustomDate();
    private String bankId = "";
    private String name = "";
    private String notes = "";

    public SinglePayment() throws CustomDate.DateErrorException {}

    public SinglePayment(SinglePayment sp) throws CustomDate.DateErrorException {
        amount = sp.amount;
        date = new CustomDate(sp.getDate());
        bankId = sp.bankId;
        name = sp.name;
        notes = sp.notes;
    }

    public SinglePayment(JSONObject jsonObject) throws CustomDate.DateErrorException, JSONException {
        this();
        readJSON(jsonObject);
    }

    public void setAmount(double amount){this.amount = amount;}
    public void setDate(CustomDate date) {this.date = date;}
    public void setName(String name) {this.name = name;}
    public void setNotes(String notes) {this.notes = notes;}
    public void setBankId(String bankId){this.bankId = bankId;}
    public double getAmount() {return amount;}
    public CustomDate getDate() {return date;}
    public String getName(){return name;}
    public String getBankId(){return bankId;}
    public String getNotes(){return notes;}

    public void readJSON(JSONObject jsonObject) throws JSONException, CustomDate.DateErrorException {
        amount = jsonObject.getDouble(AMOUNT);
        date = new CustomDate(jsonObject.getString(DATE));
        name = jsonObject.getString(NAME);
        bankId = jsonObject.getString(BANK_ID);
        notes = jsonObject.getString(NOTES);
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject j = new JSONObject();
        j.put(AMOUNT,amount);
        j.put(DATE,date.toString());
        j.put(NAME,name);
        j.put(BANK_ID, bankId);
        j.put(NOTES,notes);
        return j;
    }

}
