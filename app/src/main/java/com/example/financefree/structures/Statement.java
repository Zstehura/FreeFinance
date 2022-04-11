package com.example.financefree.structures;

import com.example.financefree.database.entities.BankStatement;

public class Statement {
    public Statement(){
        bankId = 0;
        amount = 0;
        date = 0;
        bankName = "";
    }
    public Statement(BankStatement bs, String bankName){
        bankId = bs.bank_id;
        amount = bs.amount;
        date = bs.date;
        this.bankName = bankName;
    }
    public Statement(long bankId, double amount, long date, String bankName){
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
