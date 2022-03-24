package com.example.financefree.fileClasses;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RecurringPayment {
    private static final String NAME_KEY = "name";
    private static final String NOTES_KEY = "notes";
    private static final String START_DATE_KEY = "start";
    private static final String END_DATE_KEY = "end";
    private static final String AMOUNT_KEY = "amount";
    private static final String FREQUENCY_TYPE_KEY = "freq_type";
    private static final String FREQUENCY_NUM_KEY = "freq_num";
    private static final String BANK_ID_KEY = "bank_id";
    private static final String EDITS_KEY = "edits";

    public static final int FREQ_ON_DATE_MONTHLY = 1;
    public static final int FREQ_ON_DATE_NUM_MONTHS = 2;
    public static final int FREQ_ON_DOW_MONTHLY = 3;
    public static final int FREQ_EVERY_NUM_DAYS = 4;

    public String name;
    public String notes;
    public long startDate;
    public long endDate;
    public double amount;
    public long bankId;
    public int frequencyType;
    public int frequencyNum;
    public Map<Long, PaymentEdit> edits = new HashMap<>();

    public RecurringPayment() {}

    public RecurringPayment(JSONObject jsonObject) throws JSONException {
        readJSON(jsonObject);
    }

    public void readJSON(JSONObject jsonObject) throws JSONException {
        name = jsonObject.getString(NAME_KEY);
        notes = jsonObject.getString(NOTES_KEY);
        startDate = jsonObject.getLong(START_DATE_KEY);
        endDate = jsonObject.getLong(END_DATE_KEY);
        amount = jsonObject.getDouble(AMOUNT_KEY);
        bankId = jsonObject.getLong(BANK_ID_KEY);
        frequencyNum = jsonObject.getInt(FREQUENCY_NUM_KEY);
        frequencyType = jsonObject.getInt(FREQUENCY_TYPE_KEY);
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
        jsonObject.put(START_DATE_KEY, startDate);
        jsonObject.put(END_DATE_KEY, endDate);
        jsonObject.put(AMOUNT_KEY, amount);
        jsonObject.put(BANK_ID_KEY, bankId);
        jsonObject.put(FREQUENCY_NUM_KEY, frequencyNum);
        jsonObject.put(FREQUENCY_TYPE_KEY, frequencyType);
        JSONArray jsonArray = new JSONArray();
        for(PaymentEdit p: edits.values()){
            jsonArray.put(p.toJSON());
        }
        jsonObject.put(EDITS_KEY, jsonArray);

        return jsonObject;
    }
}
