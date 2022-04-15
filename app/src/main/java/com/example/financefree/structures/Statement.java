package com.example.financefree.structures;

import com.example.financefree.database.entities.BankStatement;

import java.text.NumberFormat;

public class Statement {
    public Statement(){
        bankId = 0;
        amount = 0;
        date = 0;
        bankName = "";
        isCalculated = false;
    }
    public Statement(BankStatement bs, String bankName){
        bankId = bs.bank_id;
        amount = bs.amount;
        date = bs.date;
        id = bs.statement_id;
        this.bankName = bankName;
        //isCalculated = false;
    }
    public Statement(long bankId, double amount, long date, String bankName, long id) {
        this.id = id;
        this.bankId = bankId;
        this.amount = amount;
        this.date = date;
        this.bankName = bankName;
        //isCalculated = true;
    }
    public long id;
    public long bankId;
    public double amount;
    public long date;
    public String bankName;
    public boolean isCalculated;

    @Override
    public String toString(){
        NumberFormat f = NumberFormat.getCurrencyInstance();
        StringBuilder s = new StringBuilder();
        s.append("[ '").append(bankName).append("' | ")
                .append("id:").append(bankId).append(" | ")
                .append(DateParser.getString(date)).append(" | ")
                .append(f.format(amount)).append(" | ")
                .append("Calculated:").append(isCalculated).append(" ]");
        return s.toString();
    }
}
