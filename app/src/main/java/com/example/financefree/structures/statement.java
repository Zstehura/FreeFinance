package com.example.financefree.structures;

public class statement {
    public statement(){
        bankId = 0;
        amount = 0;
        date = 0;
    }
    public statement(long bankId, double amount, long date){
        this.bankId = bankId;
        this.amount = amount;
        this.date = date;
    }
    public long bankId;
    public double amount;
    public long date;
}
