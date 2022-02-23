package com.example.financefree.datahandlers;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.GregorianCalendar;

public class PaymentEdit implements Comparable<PaymentEdit> {
    public static final String ACTION = "action";
    public static final String EDIT_DATE = "edit_date";
    public static final String MOVE_DATE = "move_to_date";
    public static final String NEW_AMOUNT = "new_amount";
    public static final String ACTION_MOVE = "move";
    public static final String ACTION_SKIP = "skip";
    public static final String ACTION_CHANGE_AMNT = "change_amount";

    private String action = "";
    private GregorianCalendar editDate = null;
    private GregorianCalendar moveDate = null;
    private double newAmount = 0;

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

    @Override
    public int compareTo(PaymentEdit paymentEdit) {
        if(paymentEdit.editDate == this.editDate){
            return stringCompare(action, paymentEdit.action);
        }
        return editDate.getTime().compareTo(paymentEdit.editDate.getTime());
    }

    public static int stringCompare(String str1, String str2)    {
        int l1 = str1.length();
        int l2 = str2.length();
        int lmin = Math.min(l1, l2);
        for (int i = 0; i < lmin; i++) {
            int str1_ch = (int)str1.charAt(i);
            int str2_ch = (int)str2.charAt(i);
            if (str1_ch != str2_ch) {
                return str1_ch - str2_ch;
            }
        }
        // Edge case for strings like
        // String 1="Geeks" and String 2="Geeksforgeeks"
        if (l1 != l2) {
            return l1 - l2;
        }
        // If none of the above conditions is true,
        // it implies both the strings are equal
        else {
            return 0;
        }
    }
}
