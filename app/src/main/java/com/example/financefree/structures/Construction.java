package com.example.financefree.structures;

import com.example.financefree.database.entities.BankAccount;
import com.example.financefree.database.entities.BankStatement;
import com.example.financefree.database.entities.PaymentEdit;
import com.example.financefree.database.entities.RecurringPayment;
import com.example.financefree.database.entities.SinglePayment;

public final class Construction {
    public static PaymentEdit makeEdit(RecurringPayment rp, long date) {
        PaymentEdit pe = new PaymentEdit();
        pe.edit_date = date;
        pe.skip = false;
        pe.new_date = date;
        pe.new_bank_id = rp.bank_id;
        pe.rp_id = rp.rp_id;
        pe.new_amount = rp.amount;
        return pe;
    }
    public static PaymentEdit makeEdit(long editDate, long bankId, double newAmount, long newDate, long rpId) {
        PaymentEdit pe = new PaymentEdit();
        pe.edit_date = editDate;
        pe.new_bank_id = bankId;
        pe.new_amount = newAmount;
        pe.new_date = newDate;
        pe.rp_id = rpId;
        return pe;
    }

    public static PaymentEdit makeEdit(long editDate, long bankId, double newAmount, long newDate, long rpId, long peId) {
        PaymentEdit pe = makeEdit(editDate, bankId, newAmount, newDate, rpId);
        pe.edit_date = peId;
        return pe;
    }

    public static BankStatement makeStatement(long bankId, long date, double amount){
        BankStatement bs = new BankStatement();
        bs.bank_id = bankId;
        bs.date = date;
        bs.amount = amount;
        return bs;
    }
    public static BankStatement makeStatement(long bankId, long date, double amount, long sId){
        BankStatement bs = makeStatement(bankId, date, amount);
        bs.statement_id = sId;
        return bs;
    }

    public static SinglePayment makeSp(long bankId, double amuont, long date) {
        SinglePayment sp = new SinglePayment();
        sp.bank_id = bankId;
        sp.amount = amuont;
        sp.date = date;
        return sp;
    }

    public static SinglePayment makeSp(long bankId, double amuont, long date, long spId) {
        SinglePayment sp = makeSp( bankId, amuont, date);
        sp.sp_id = spId;
        return sp;
    }
}
