package com.example.financefree;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// TODO: add bank connections
//      ADD Action ChangeAmount
public class RecurringPayment {

    public static final String SOURCE_ID = "source_id";
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
        public static final String ACTION_MOVE = "move";
        public static final String ACTION_SKIP = "skip";

        private String action;
        private GregorianCalendar editDate;
        private GregorianCalendar moveDate;

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

        public JSONObject toJSONObject(){
            JSONObject j = new JSONObject();
            try {
                j.put(ACTION, action);
                String temp = editDate.get(Calendar.MONTH) + "/" + editDate.get(Calendar.DAY_OF_MONTH) + "/" + (editDate.get(Calendar.YEAR)-2000);
                j.put(EDIT_DATE, temp);
                if (action.equals(ACTION_MOVE)){
                    temp = moveDate.get(Calendar.MONTH) + "/" + moveDate.get(Calendar.DAY_OF_MONTH) + "/" + (moveDate.get(Calendar.YEAR)-2000);
                    j.put(MOVE_DATE, temp);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return j;
        }
        public void readJSON(@NonNull JSONObject jsonObject){
            try {
                action = jsonObject.getString(ACTION);
                editDate = new GregorianCalendar();
                String[] temp = jsonObject.getString(EDIT_DATE).split("/");
                editDate.set(Calendar.MONTH, Integer.parseInt(temp[0]));
                editDate.set(Calendar.DAY_OF_MONTH, Integer.parseInt(temp[1]));
                editDate.set(Calendar.YEAR, Integer.parseInt(temp[2]) + 2000);
                if(action.equals(ACTION_MOVE)) {
                    moveDate = new GregorianCalendar();
                    temp = jsonObject.getString(MOVE_DATE).split("/");
                    moveDate.set(Calendar.MONTH, Integer.parseInt(temp[0]));
                    moveDate.set(Calendar.DAY_OF_MONTH, Integer.parseInt(temp[1]));
                    moveDate.set(Calendar.YEAR, Integer.parseInt(temp[2]) + 2000);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    private String frequencyType;
    private int frequency;
    private double amount;
    private GregorianCalendar start;
    private GregorianCalendar end;
    private String notes;
    private String bankId;
    private Map<GregorianCalendar, PaymentEdit> edits;

    public RecurringPayment() {
        edits = new HashMap<>();
    }

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
    public void setAmount(double amount) {this.amount = amount;}
    public void setStart(GregorianCalendar start) {this.start = start;}
    public void setEnd(GregorianCalendar end) {this.end = end;}
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

    @RequiresApi(api = Build.VERSION_CODES.O)
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

    public void readJSON(JSONObject jsonObject) {
        try {
            JSONArray jsonArray = new JSONArray(jsonObject.getJSONArray(EDITS));
            for(int i = 0; i < jsonArray.length(); i++){
                PaymentEdit temp = new PaymentEdit();
                temp.readJSON(jsonArray.getJSONObject(i));
                edits.put(temp.getEditDate(),temp);
            }
            amount = jsonObject.getDouble(AMOUNT);
            start = new GregorianCalendar();
            String[] temp = jsonObject.getString(START).split("/");
            start.set(Calendar.MONTH, Integer.parseInt(temp[0]));
            start.set(Calendar.DAY_OF_MONTH, Integer.parseInt(temp[1]));
            start.set(Calendar.YEAR, Integer.parseInt(temp[2]) + 2000);
            end = new GregorianCalendar();
            temp = jsonObject.getString(END).split("/");
            end.set(Calendar.MONTH, Integer.parseInt(temp[0]));
            end.set(Calendar.DAY_OF_MONTH, Integer.parseInt(temp[1]));
            end.set(Calendar.YEAR, Integer.parseInt(temp[2]) + 2000);
            frequencyType = jsonObject.getString(FREQ_TYPE);
            frequency = jsonObject.getInt(FREQUENCY);
            notes = jsonObject.getString(NOTES);
            bankId = jsonObject.getString(SOURCE_ID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public JSONObject toJSONObject() {
        JSONObject j = new JSONObject();
        try {
            JSONArray ja = new JSONArray();
            for(PaymentEdit pe: edits.values()){
                ja.put(pe.toJSONObject());
            }
            j.put(EDITS, ja);
            j.put(AMOUNT, amount);
            String temp = start.get(Calendar.MONTH) + "/" + start.get(Calendar.DAY_OF_MONTH) + "/" + (start.get(Calendar.YEAR)-2000);
            j.put(START, temp);
            temp = end.get(Calendar.MONTH) + "/" + end.get(Calendar.DAY_OF_MONTH) + "/" + (end.get(Calendar.YEAR)-2000);
            j.put(END, temp);
            j.put(FREQ_TYPE, frequencyType);
            j.put(FREQUENCY, frequency);
            j.put(NOTES,notes);
            j.put(SOURCE_ID,bankId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return j;
    }

}