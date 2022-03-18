package com.example.financefree.fileClasses;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class BankAccount {
    private static final String STATEMENTS_KEY = "statements";
    private static final String DATE_KEY = "date";
    private static final String AMOUNT_KEY = "amount";
    private static final String NAME_KEY = "name";
    private static final String NOTES_KEY = "notes";

    public Map<Long, Double> statements;
    public String name;
    public String notes;

    public BankAccount(){}

    public BankAccount(JSONObject json) throws JSONException {
        readJSON(json);
    }

    public void readJSON(JSONObject jsonObject) throws JSONException {
        name = jsonObject.getString(NAME_KEY);
        notes = jsonObject.getString(NOTES_KEY);
        JSONArray ja = jsonObject.getJSONArray(STATEMENTS_KEY);
        for(int i = 0; i < ja.length(); i++){
            JSONObject j = ja.getJSONObject(i);
            statements.put(j.getLong(DATE_KEY), j.getDouble(AMOUNT_KEY));
        }
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(NAME_KEY, name);
        jsonObject.put(NOTES_KEY, notes);
        JSONArray ja = new JSONArray();
        for(long l: statements.keySet()){
            JSONObject j = new JSONObject();
            j.put(DATE_KEY, l);
            j.put(AMOUNT_KEY, statements.get(l));
            ja.put(j);
        }
        jsonObject.put(STATEMENTS_KEY, ja);
        return jsonObject;
    }
}
