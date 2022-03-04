package com.example.financefree.datahandlers;

import androidx.annotation.NonNull;

import com.example.financefree.structures.CustomDate;

import org.json.JSONException;
import org.json.JSONObject;

public class PaymentEdit implements Comparable<PaymentEdit> {
    public static final String ACTION = "action";
    public static final String EDIT_DATE = "edit_date";
    public static final String MOVE_DATE = "move_to_date";
    public static final String NEW_AMOUNT = "new_amount";
    public static final String ACTION_MOVE = "move";
    public static final String ACTION_SKIP = "skip";
    public static final String ACTION_CHANGE_AMNT = "change_amount";

    private String action = "";
    private CustomDate editDate = new CustomDate("1/1/2000");
    private CustomDate moveDate = new CustomDate("1/1/2000");
    private double newAmount = 0;

    public PaymentEdit() throws CustomDate.DateErrorException {action = ACTION_SKIP;}

    public PaymentEdit(PaymentEdit pe) throws CustomDate.DateErrorException {
        action = pe.action;
        editDate = new CustomDate(pe.editDate);
        moveDate = new CustomDate(pe.moveDate);
        newAmount = pe.newAmount;
    }


    public CustomDate getMoveDate() {return moveDate;}
    public void setMoveDate(CustomDate moveDate) {this.moveDate = moveDate;}
    public CustomDate getEditDate() {
        return editDate;
    }
    public void setEditDate(CustomDate editDate) {
        this.editDate = editDate;
    }
    public String getAction() {return action;}
    public void setAction(String action) {this.action = action;}
    public double getNewAmount(){return newAmount;}
    public void setNewAmount(double newAmount){this.newAmount = newAmount;}

    public JSONObject toJSONObject() throws JSONException {
        JSONObject j = new JSONObject();
        j.put(ACTION, action);
        j.put(EDIT_DATE, editDate.toString());
        if (action.equals(ACTION_MOVE)){
            j.put(MOVE_DATE, moveDate.toString());
        }
        else if(action.equals(ACTION_CHANGE_AMNT)){
            j.put(NEW_AMOUNT, newAmount);
        }
        return j;
    }
    public void readJSON(@NonNull JSONObject jsonObject) throws JSONException, CustomDate.DateErrorException {
        action = jsonObject.getString(ACTION);
        editDate = new CustomDate(jsonObject.getString(EDIT_DATE));
        if(action.equals(ACTION_MOVE)) {
            moveDate = new CustomDate(jsonObject.getString(MOVE_DATE));
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
        return editDate.compareTo(paymentEdit.editDate);
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
