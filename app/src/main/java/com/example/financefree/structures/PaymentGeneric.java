package com.example.financefree.structures;

import android.os.Build;

import androidx.annotation.RequiresApi;

public class PaymentGeneric {
    public double amount;
    public long date;
    public String name;
    public long bankId;
    public char cType;
    public long id;

    public PaymentGeneric(double amount, long date, String name, long bankId, char cType, long id){
        this.amount = amount;
        this.date = date;
        this.name = name;
        this.bankId = bankId;
        this.cType = cType;
        this.id = id;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public PaymentGeneric() {
        this(0, DateParser.getLong(1,1,2000), "new payment", 0, 's', 0);
    }
}