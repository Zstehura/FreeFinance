package com.example.financefree.databaseClasses;

import com.example.financefree.structures.parseDate;
import com.example.financefree.structures.payment;
import com.example.financefree.structures.statement;

import java.util.LinkedList;
import java.util.List;

public final class DatabaseAccessor {

    private DatabaseAccessor(){}

    public static List<payment> getPaymentsOnDate(AppDatabase db, long date){
        List<payment> l = new LinkedList<>();
        SinglePaymentDao spd = db.singlePaymentDao();
        RecurringPaymentDao rpd = db.recurringPaymentDao();
        PaymentEditDao ped = db.paymentEditDao();

        for(SinglePayment sp: spd.getAllByDate(date)){
            payment p = new payment(sp.amount, sp.date,sp.name,sp.bank_id);
            l.add(p);
        }

        for(RecurringPayment rp: rpd.getFromDate(date)){
            List<PaymentEdit> lpe = ped.getEditsByPayment(rp.rp_id);
            payment p = new payment(rp.amount,date,rp.name,rp.bankId);
            boolean skip = false;
            boolean add = false;
            for(PaymentEdit pe: lpe){
                if(pe.edit_date == date || pe.move_to_date == date) {
                    if (pe.action == PaymentEdit.ACTION_SKIP) {
                        skip = true;
                    } else if (pe.action == PaymentEdit.ACTION_CHANGE_AMOUNT) {
                        p.amount = pe.new_amount;
                    } else if (pe.action == PaymentEdit.ACTION_MOVE_DATE) {
                        p.date = pe.move_to_date;
                        add = true;
                    } else {
                        ped.delete(pe);
                    }
                }
            }
            if(rp.frequencyType == RecurringPayment.EVERY && !skip){
                long cd = rp.startDate;
                while(cd < date){
                    cd += rp.frequency;
                }
                if(cd == date){
                    add = true;
                }
            }
            else if(!skip && parseDate.getDay(date) == rp.frequency){
                add = true;
            }

            if(add && !skip) {
                p.date = date;
                l.add(p);
            }
        }
        return l;
    }

    public static List<statement> getStatementsOnDate(AppDatabase db, long date){
        List<statement> l = new LinkedList<>();
        BankStatementDao bsd = db.bankStatementDao();
        BankAccountDao bad = db.bankAccountDao();

        List<BankAccount> bal = bad.getAll();
        for(BankAccount ba: bal){
            statement s = new statement(ba.bank_id, 0, date);
            long d = date;
            BankStatement bs;
            do{
                bs = bsd.getStatement(ba.bank_id, d);
                d--;
            } while(bs == null);
            // Get a starting off point
            s.amount = bs.amount;

            // add in relevant payments leading up to the date in question
            while(d < date){
                List<payment> paymentList = getPaymentsOnDate(db, d);
                for(payment p: paymentList){
                    if(p.bankId == s.bankId){
                        s.amount += p.amount;
                    }
                }
                d++;
            }
            l.add(s);
        }

        return l;
    }

    //TODO: Make taxbracket update
    //  getEstimatedIncomeTax
}
