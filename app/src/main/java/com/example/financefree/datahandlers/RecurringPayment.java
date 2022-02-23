package com.example.financefree.datahandlers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;


public class RecurringPayment implements Comparable<RecurringPayment> {

    public static final String PAYMENT_ID = "pay_id";
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

    private String paymentId = "new_payment";
    private String frequencyType = ON;
    private int frequency = 1;
    private double amount = 0;
    private GregorianCalendar start = new GregorianCalendar();
    private GregorianCalendar end = new GregorianCalendar();
    private String notes = "";
    private String bankId = "";
    private final List<PaymentEdit> edits = new ArrayList<>();

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
        edits.add(paymentEdit);
    }
    public List<PaymentEdit> getEdits(){
        return edits;
    }
    public void removeEdit(GregorianCalendar date){
        edits.remove(date);
    }


    // read/write functions
    public void readJSON(JSONObject jsonObject) throws JSONException {
        JSONArray jsonArray = jsonObject.getJSONArray(EDITS);
        for(int i = 0; i < jsonArray.length(); i++){
            PaymentEdit temp = new PaymentEdit();
            temp.readJSON(jsonArray.getJSONObject(i));
            edits.add(temp);
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
        Collections.sort(edits);
        for(PaymentEdit pe: edits){
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

    public List<SinglePayment> getDatesFromMonth(int month, int year) {
        List<GregorianCalendar> l = new ArrayList<>();
        List<SinglePayment> singlePaymentList = new ArrayList<>();
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
            SinglePayment sp = new SinglePayment();
            boolean skip = false;
            sp.setAmount(amount);
            sp.setNotes(notes);
            sp.setDate(d);
            sp.setName(paymentId);
            sp.setBankId(bankId);
            for (PaymentEdit pe: edits) {

                if(pe.getEditDate().equals(d)) {
                    switch (pe.getAction()) {
                        case PaymentEdit.ACTION_MOVE:
                            sp.setDate(pe.getMoveDate());
                            break;
                        case PaymentEdit.ACTION_SKIP:
                            // not going to add this
                            skip = true;
                            break;
                        case PaymentEdit.ACTION_CHANGE_AMNT:
                            sp.setAmount(pe.getNewAmount());
                            break;
                    }
                }
            }

            if(!skip) singlePaymentList.add(sp);
        }

        return singlePaymentList;
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

    @Override
    public int compareTo(RecurringPayment recurringPayment) {
        if(!start.equals(recurringPayment.start)){
            return start.compareTo(recurringPayment.start);
        } else if(!paymentId.equals(recurringPayment.paymentId)) {
            return PaymentEdit.stringCompare(paymentId, recurringPayment.paymentId);
        } else if(amount != recurringPayment.amount){
            return (int)amount - (int)recurringPayment.amount;
        }
        return 0;
    }
}