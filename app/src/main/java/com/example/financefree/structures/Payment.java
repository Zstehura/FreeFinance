package com.example.financefree.structures;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.financefree.database.entities.RecurringPayment;
import com.example.financefree.database.entities.SinglePayment;

public class Payment {
    public double amount;
    public long date;
    public String name;
    public long bankId;
    public char cType;
    public long id;

    public Payment(SinglePayment sp){
        amount = sp.amount;
        date = sp.date;
        bankId = sp.bank_id;
        cType = 's';
        id = sp.sp_id;
    }

    public Payment(RecurringPayment rp, long date) {
        amount = rp.amount;
        this.date = date;
        name = rp.name;
        bankId = rp.bank_id;
        cType = 'r';
        id = rp.rp_id;
    }

    public Payment(double amount, long date, String name, long bankId, char cType, long id){
        this.amount = amount;
        this.date = date;
        this.name = name;
        this.bankId = bankId;
        this.cType = cType;
        this.id = id;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public Payment() {
        this(0, DateParser.getLong(1,1,2000), "new payment", 0, 's', 0);
    }
}
