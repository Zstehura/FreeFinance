package com.example.financefree.structures;

import com.example.financefree.database.entities.BankAccount;
import com.example.financefree.database.entities.BankStatement;
import com.example.financefree.database.entities.PaymentEdit;
import com.example.financefree.database.entities.RecurringPayment;

public final class Construction {
    public static PaymentEdit getEdit(RecurringPayment rp, long date) {
        PaymentEdit pe = new PaymentEdit();
        pe.edit_date = date;
        pe.skip = false;
        pe.new_date = date;
        pe.new_bank_id = rp.bank_id;
        pe.rp_id = rp.rp_id;
        pe.new_amount = rp.amount;
        return pe;
    }

    public static BankStatement makeStatement(long bankId, long date, double amount){
        BankStatement bs = new BankStatement();
        bs.bank_id = bankId;
        bs.date = date;
        bs.amount = amount;
        return bs;
    }
}
