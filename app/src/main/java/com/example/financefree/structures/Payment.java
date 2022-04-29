package com.example.financefree.structures;


import androidx.annotation.NonNull;


import com.example.financefree.database.entities.PaymentEdit;
import com.example.financefree.database.entities.RecurringPayment;
import com.example.financefree.database.entities.SinglePayment;

import java.text.NumberFormat;

public class Payment {
    public double amount;
    public long date;
    public String name;
    public long bankId;
    public char cType;
    public long id;
    public String notes;

    public Payment(SinglePayment sp){
        amount = sp.amount;
        date = sp.date;
        name = sp.name;
        bankId = sp.bank_id;
        cType = 's';
        id = sp.sp_id;
        notes = sp.notes;
    }

    public Payment(RecurringPayment rp, long date) {
        amount = rp.amount;
        this.date = date;
        name = rp.name;
        bankId = rp.bank_id;
        cType = 'r';
        id = rp.rp_id;
        notes = rp.notes;
    }

    public Payment(PaymentEdit pe, String name, String notes){
        amount = pe.new_amount;
        date = pe.new_date;
        this.name = name;
        bankId = pe.new_bank_id;
        cType = 'r';
        id = pe.rp_id;
        this.notes = notes;
    }

    public Payment(double amount, long date, String name, long bankId, char cType, long id){
        this.amount = amount;
        this.date = date;
        this.name = name;
        this.bankId = bankId;
        this.cType = cType;
        this.id = id;
    }
    
    public Payment() {
        this(0, DateParser.getLong(1,1,2000), "new payment", 0, 's', 0);
    }

    @NonNull
    @Override
    public String toString() {
        NumberFormat f = NumberFormat.getCurrencyInstance();
        return "[ '" + name + "' | " +
                "id:" + id + " | " +
                "bankId:" + bankId + " | " +
                "type:" + cType + " | " +
                DateParser.getString(date) + " | " +
                f.format(amount) + " ]";
    }
}
