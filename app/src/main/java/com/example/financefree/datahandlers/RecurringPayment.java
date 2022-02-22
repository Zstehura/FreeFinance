package com.example.financefree.datahandlers;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class RecurringPayment {

    public static final String PAYMENT_ID = "id";
    public static final String BANK_ID = "bank_id";
    public static final String NOTES = "notes";
    public static final String FREQ_TYPE = "frequency_type";
    public static final String FREQUENCY = "frequency";
    public static final String ON = "on";
    public static final String EVERY = "every";
    public static final String AMOUNT = "amount";
    public static final String START = "start";
    public static final String END = "end";
    public static final String EDITS = "edits";

    public class PaymentEdit {
        public static final String ACTION = "action";
        public static final String EDIT_DATE = "edit_date";
        public static final String MOVE_DATE = "move_to_date";
        public static final String NEW_AMOUNT = "new_amount";
        public static final String ACTION_MOVE = "move";
        public static final String ACTION_SKIP = "skip";
        public static final String ACTION_CHANGE_AMNT = "change_amount";

        private String action;
        private GregorianCalendar editDate;
        private GregorianCalendar moveDate;
        private double newAmount;

        public PaymentEdit() {action = ACTION_SKIP;}


        public GregorianCalendar getMoveDate() {return moveDate;}
        public void setMoveDate(GregorianCalendar moveDate) {this.moveDate = moveDate;}
        public GregorianCalendar getEditDate() {
            return editDate;
        }
        public void setEditDate(GregorianCalendar editDate) {
            this.editDate = editDate;
        }
        public String getAction() {return action;}
        public void setAction(String action) {this.action = action;}
        public double getNewAmount(){return newAmount;}
        public void setNewAmount(double newAmount){this.newAmount = newAmount;}

        public JSONObject toJSONObject() throws JSONException {
            JSONObject j = new JSONObject();
            j.put(ACTION, action);
            j.put(EDIT_DATE, DateStringer.CalToString(editDate));
            if (action.equals(ACTION_MOVE)){
                j.put(MOVE_DATE, DateStringer.CalToString(moveDate));
            }
            else if(action.equals(ACTION_CHANGE_AMNT)){
                j.put(NEW_AMOUNT, newAmount);
            }
            return j;
        }
        public void readJSON(@NonNull JSONObject jsonObject) throws JSONException {
            action = jsonObject.getString(ACTION);
            editDate = DateStringer.StringToCal(jsonObject.getString(EDIT_DATE));
            if(action.equals(ACTION_MOVE)) {
                moveDate = DateStringer.StringToCal(jsonObject.getString(MOVE_DATE));
            }
            else if(action.equals(ACTION_CHANGE_AMNT)){
                newAmount = jsonObject.getDouble(NEW_AMOUNT);
            }
        }

    }

    private String paymentId;
    private String frequencyType;
    private int frequency;
    private double amount;
    private GregorianCalendar start;
    private GregorianCalendar end;
    private String notes;
    private String bankId;
    private final Map<GregorianCalendar, PaymentEdit> edits = new HashMap<>();

    // Constructor
    public RecurringPayment() {}

    // Accessors
    public void setBankId(String bankId){this.bankId = bankId;}
    public void setNotes(String notes){this.notes = notes;}
    public void setFrequency(String frequencyType, int frequency){
        if(frequencyType.equals(ON) || frequencyType.equals(EVERY)) {
            this.frequencyType = frequencyType;
            this.frequency = frequency;
        }
        else {
            this.frequencyType = "";
            this.frequency = 0;
        }
    }
    public void setPaymentId(String paymentId){this.paymentId = paymentId;}
    public void setAmount(double amount) {this.amount = amount;}
    public void setStart(GregorianCalendar start) {this.start = start;}
    public void setEnd(GregorianCalendar end) {this.end = end;}
    public String getPaymentId(){return paymentId;}
    public String getBankId(){return bankId;}
    public String getNotes(){return notes;}
    public String getFrequencyType(){return frequencyType;}
    public int getFrequency() {return frequency;}
    public double getAmount() {return this.amount;}
    public GregorianCalendar getStart() {return this.start;}
    public GregorianCalendar getEnd() {return this.end;}

    public void addEdit(PaymentEdit paymentEdit){
        edits.put(paymentEdit.getEditDate(), paymentEdit);
    }
    public PaymentEdit getEdit(GregorianCalendar date){
        return edits.get(date);
    }
    public void removeEdit(GregorianCalendar date){
        edits.remove(date);
    }


    // read/write functions
    public void readJSON(JSONObject jsonObject) throws JSONException {
        JSONArray jsonArray = new JSONArray(jsonObject.getJSONArray(EDITS));
        for(int i = 0; i < jsonArray.length(); i++){
            PaymentEdit temp = new PaymentEdit();
            temp.readJSON(jsonArray.getJSONObject(i));
            edits.put(temp.getEditDate(),temp);
        }
        amount = jsonObject.getDouble(AMOUNT);
        start = DateStringer.StringToCal(jsonObject.getString(START));
        end = DateStringer.StringToCal(jsonObject.getString(END));
        frequencyType = jsonObject.getString(FREQ_TYPE);
        frequency = jsonObject.getInt(FREQUENCY);
        notes = jsonObject.getString(NOTES);
        bankId = jsonObject.getString(BANK_ID);
        paymentId = jsonObject.getString(PAYMENT_ID);
    }

    public JSONObject toJSONObject() throws JSONException {
        JSONObject j = new JSONObject();
        JSONArray ja = new JSONArray();
        for(PaymentEdit pe: edits.values()){
            ja.put(pe.toJSONObject());
        }
        j.put(EDITS, ja);
        j.put(AMOUNT, amount);
        j.put(START, DateStringer.CalToString(start));
        j.put(END, DateStringer.CalToString(end));
        j.put(FREQ_TYPE, frequencyType);
        j.put(FREQUENCY, frequency);
        j.put(NOTES,notes);
        j.put(BANK_ID,bankId);
        j.put(PAYMENT_ID,paymentId);
        return j;
    }

    public List<GregorianCalendar> getDatesFromMonth(int month, int year) {
        List<GregorianCalendar> l = new ArrayList<>();
        if (frequencyType.equals(ON)) {
            // Occurs on given date every month
            GregorianCalendar c = new GregorianCalendar(year, month, frequency);
            l.add(c);
        } else {
            GregorianCalendar c = new GregorianCalendar();
            c.set(year,month,1);
            c.add(Calendar.MONTH, 1);
            GregorianCalendar temp = new GregorianCalendar();
            temp.setTime(start.getTime());
            while(temp.before(c)){
                if(temp.get(Calendar.MONTH) == month && temp.get(Calendar.YEAR) == year){
                    l.add(temp);
                }
                temp.add(Calendar.DAY_OF_MONTH, frequency);
            }
        }

        for(GregorianCalendar d: l){
            if (edits.containsKey(d)) {
                PaymentEdit paymentEdit = edits.get(d);
                l.remove(d);
                assert paymentEdit != null;
                if(paymentEdit.getAction().equals(PaymentEdit.ACTION_MOVE)){
                    l.add(paymentEdit.getMoveDate());
                }
            }
        }

        return l;
    }

    public int getNumPayments(int nYear) {
        GregorianCalendar cal = start;
        if(frequencyType.equals(ON)) start.set(Calendar.DAY_OF_MONTH, frequency);
        int n = 0;
        while(cal.get(Calendar.YEAR) <= nYear && cal.before(end)){
            if(cal.get(Calendar.YEAR) == nYear) n++;

            if(frequencyType.equals(ON)) cal.add(Calendar.MONTH, 1);
            else cal.add(Calendar.DATE, frequency);
        }

        return n;
    }
    public double getAnnualTotal(int nYear) {
        return getNumPayments(nYear) * amount;
    }
}