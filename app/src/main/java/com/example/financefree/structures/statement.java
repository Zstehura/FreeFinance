package com.example.financefree.structures;

public class statement {
    public statement(){
        bankId = 0;
        amount = 0;
        date = 0;
        bankName = "";
    }
    public statement(long bankId, double amount, long date, String bankName){
        this.bankId = bankId;
        this.amount = amount;
        this.date = date;
        this.bankName = bankName;
    }
    public long bankId;
    public double amount;
    public long date;
    public String bankName;
}
