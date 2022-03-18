package com.example.financefree.fileClasses;

import org.json.JSONException;
import org.json.JSONObject;

public class PaymentEdit {
    private static final String EDIT_DATE_KEY = "edit_date";
    private static final String NEW_AMOUNT_KEY = "new_amount";
    private static final String NEW_DATE_KEY = "new_date";
    private static final String NEW_BANK_ID_KEY = "new_bank";

    public long editDate;
    public double newAmount;
    public long newDate;
    public long newBankId;

    public PaymentEdit() {}

    public PaymentEdit(JSONObject jsonObject) throws JSONException {
        readJSON(jsonObject);
    }

    public void readJSON(JSONObject jsonObject) throws JSONException {
        editDate = jsonObject.getLong(EDIT_DATE_KEY);
        newAmount = jsonObject.getDouble(NEW_AMOUNT_KEY);
        newBankId = jsonObject.getLong(NEW_BANK_ID_KEY);
        newDate = jsonObject.getLong(NEW_DATE_KEY);
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(EDIT_DATE_KEY, editDate);
        jsonObject.put(NEW_AMOUNT_KEY, newAmount);
        jsonObject.put(NEW_DATE_KEY, newDate);
        jsonObject.put(NEW_BANK_ID_KEY, newBankId);

        return jsonObject;
    }
}
