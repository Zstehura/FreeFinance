package com.example.financefree.fileClasses;

import org.json.JSONException;
import org.json.JSONObject;

public class SinglePayment {
    private static final String DATE_KEY = "date";
    private static final String NAME_KEY = "name";
    private static final String NOTES_KEY = "notes";
    private static final String AMOUNT_KEY = "amount";

    public long date;
    public String name;
    public String notes;
    public double amount;

    public SinglePayment(){}
    public SinglePayment(JSONObject jsonObject) throws JSONException {
        readJSON(jsonObject);
    }

    public void readJSON(JSONObject jsonObject) throws JSONException {
        date = jsonObject.getLong(DATE_KEY);
        name = jsonObject.getString(NAME_KEY);
        notes = jsonObject.getString(NOTES_KEY);
        amount = jsonObject.getDouble(AMOUNT_KEY);
    }
    public JSONObject toJSON() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(DATE_KEY,date);
        jsonObject.put(NAME_KEY,name);
        jsonObject.put(NOTES_KEY,notes);
        jsonObject.put(AMOUNT_KEY,amount);
        return jsonObject;
    }
}
