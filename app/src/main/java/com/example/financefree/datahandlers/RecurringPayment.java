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
    private CustomDate start = new CustomDate();
    private CustomDate end = new CustomDate();
    private String notes = "";
    private String bankId = "";
    private final List<PaymentEdit> edits = new ArrayList<>();

    // Constructor
    public RecurringPayment() throws CustomDate.DateErrorException {}

    public RecurringPayment(RecurringPayment rp) throws CustomDate.DateErrorException {
        paymentId = rp.paymentId;
        frequencyType = rp.frequencyType;
        frequency = rp.frequency;
        amount = rp.amount;
        start = new CustomDate(rp.start);
        end = new CustomDate(rp.end);
        notes = rp.notes;
        bankId = rp.bankId;
        for(PaymentEdit pe: rp.edits){
            edits.add(new PaymentEdit(pe));
        }
    }

    public RecurringPayment(JSONObject jsonObject) throws CustomDate.DateErrorException, JSONException {
        this();
        readJSON(jsonObject);
    }

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
    public void setStart(CustomDate start) {this.start = start;}
    public void setEnd(CustomDate end) {this.end = end;}
    public String getPaymentId(){return paymentId;}
    public String getBankId(){return bankId;}
    public String getNotes(){return notes;}
    public String getFrequencyType(){return frequencyType;}
    public int getFrequency() {return frequency;}
    public double getAmount() {return this.amount;}
    public CustomDate getStart() {return this.start;}
    public CustomDate getEnd() {return this.end;}

    public void addEdit(PaymentEdit paymentEdit){
        edits.add(paymentEdit);
    }
    public List<PaymentEdit> getEdits(){
        return edits;
    }
    public void removeEdit(PaymentEdit pe){
        edits.remove(pe);
    }


    // read/write functions
    public void readJSON(JSONObject jsonObject) throws JSONException, CustomDate.DateErrorException {
        JSONArray jsonArray = jsonObject.getJSONArray(EDITS);
        for(int i = 0; i < jsonArray.length(); i++){
            PaymentEdit temp = new PaymentEdit();
            temp.readJSON(jsonArray.getJSONObject(i));
            edits.add(temp);
        }
        amount = jsonObject.getDouble(AMOUNT);
        start = new CustomDate(jsonObject.getString(START));
        end = new CustomDate(jsonObject.getString(END));
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
        j.put(START, start.toString());
        j.put(END, end.toString());
        j.put(FREQ_TYPE, frequencyType);
        j.put(FREQUENCY, frequency);
        j.put(NOTES,notes);
        j.put(BANK_ID,bankId);
        j.put(PAYMENT_ID,paymentId);
        return j;
    }

    public List<SinglePayment> getDatesFromMonth(int month, int year) throws CustomDate.DateErrorException {
        List<String> l = new ArrayList<>();
        List<SinglePayment> singlePaymentList = new ArrayList<>();
        if (frequencyType.equals(ON)) {
            // Occurs on given date every month
            CustomDate c = new CustomDate(month, frequency, year);
            l.add(c.toString());
        } else {
            CustomDate c = new CustomDate(month,1,year);
            c.addMonths(1);
            CustomDate temp = new CustomDate(start);
            while(temp.before(c)){
                if(temp.get(CustomDate.MONTH) == month && temp.get(CustomDate.YEAR) == year){
                    l.add(temp.toString());
                }
                temp.addDays(frequency);
            }
        }

        for(String d: l){
            SinglePayment sp = new SinglePayment();
            boolean skip = false;
            sp.setAmount(amount);
            sp.setNotes(notes);
            sp.setDate(new CustomDate(d));
            sp.setName(paymentId);
            sp.setBankId(bankId);
            for (PaymentEdit pe: edits) {

                if(pe.getEditDate().toString().equals(d)) {
                    switch (pe.getAction()) {
                        case PaymentEdit.ACTION_MOVE:
                            sp.setDate(new CustomDate(pe.getMoveDate()));
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

    public int getNumPayments(int nYear) throws CustomDate.DateErrorException {
        CustomDate cal = start;
        if(frequencyType.equals(ON)) start.set(CustomDate.DAY, frequency);
        int n = 0;
        while(cal.get(Calendar.YEAR) <= nYear && cal.before(end)){
            if(cal.get(Calendar.YEAR) == nYear) n++;

            if(frequencyType.equals(ON)) cal.addMonths(1);
            else cal.addDays(frequency);
        }

        return n;
    }
    public double getAnnualTotal(int nYear) throws CustomDate.DateErrorException {
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