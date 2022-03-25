package com.example.financefree.fileClasses;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RecurringPayment {
    private static final String NAME_KEY = "name";
    private static final String NOTES_KEY = "notes";
    private static final String AMOUNT_KEY = "amount";
    private static final String FREQUENCY_KEY = "freq";
    private static final String BANK_ID_KEY = "bank_id";
    private static final String EDITS_KEY = "edits";

    public String name;
    public String notes;
    public double amount;
    public long bankId;
    public Frequency frequency;
    public Map<Long, PaymentEdit> edits = new HashMap<>();

    public RecurringPayment() {}

    public RecurringPayment(JSONObject jsonObject) throws JSONException {
        readJSON(jsonObject);
    }

    public void readJSON(JSONObject jsonObject) throws JSONException {
        name = jsonObject.getString(NAME_KEY);
        notes = jsonObject.getString(NOTES_KEY);
        amount = jsonObject.getDouble(AMOUNT_KEY);
        bankId = jsonObject.getLong(BANK_ID_KEY);
        frequency = new Frequency(jsonObject.getJSONObject(FREQUENCY_KEY));
        JSONArray jsonArray = jsonObject.getJSONArray(EDITS_KEY);
        for(int i = 0; i < jsonArray.length(); i++){
            JSONObject j = jsonArray.getJSONObject(i);
            PaymentEdit p = new PaymentEdit(j);
            edits.put(p.editDate, p);
        }
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(NAME_KEY, name);
        jsonObject.put(NOTES_KEY, notes);
        jsonObject.put(AMOUNT_KEY, amount);
        jsonObject.put(BANK_ID_KEY, bankId);
        jsonObject.put(FREQUENCY_KEY, frequency.toJSON());
        JSONArray jsonArray = new JSONArray();
        for(PaymentEdit p: edits.values()){
            jsonArray.put(p.toJSON());
        }
        jsonObject.put(EDITS_KEY, jsonArray);

        return jsonObject;
    }
}
