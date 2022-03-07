package com.example.financefree.structures;

public class payment {
    public double amount;
    public long date;
    public String name;
    public long bankId;

    public payment(double amount, long date,String name, long bankId){
        this.amount = amount;
        this.date = date;
        this.name = name;
        this.bankId = bankId;
    }
    public payment() {
        this(0, parseDate.getLong(1,1,2000), "new payment", 0);
    }
}
