package com.example.financefree.datahandlers;

import org.json.JSONException;
import org.json.JSONObject;

public class Bill {
    public static final String BILL_ID = "bill_id";
    public static final String DEFAULT_AMOUNT = "default_amount";

    private String billId = "New Bill";
    private double defaultAmount = 0;

    public Bill() {}
    public Bill(Bill bill){
        billId = bill.billId;
        defaultAmount = bill.defaultAmount;
    }

    public void setBillId(String billId) {this.billId = billId;}
    public void setDefaultAmount(double defaultAmount) {this.defaultAmount = defaultAmount;}
    public String getBillId() {return billId;}
    public double getDefaultAmount(){return defaultAmount;}

    public void readJSON(JSONObject jsonObject) throws JSONException {
        billId = jsonObject.getString(BILL_ID);
        defaultAmount = jsonObject.getDouble(DEFAULT_AMOUNT);
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(BILL_ID, billId);
        jsonObject.put(DEFAULT_AMOUNT, defaultAmount);
        return jsonObject;
    }

}
