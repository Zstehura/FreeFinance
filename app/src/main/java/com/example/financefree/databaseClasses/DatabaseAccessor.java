package com.example.financefree.databaseClasses;

import com.example.financefree.structures.CustomDate;

import java.util.LinkedList;
import java.util.List;

public final class DatabaseAccessor {

    private DatabaseAccessor(){}

    public static List<SinglePayment> getPaymentsOnDate(AppDatabase db, CustomDate date){
        List<SinglePayment> l;
        SinglePaymentDao spd = db.singlePaymentDao();
        RecurringPaymentDao rpd = db.recurringPaymentDao();
        PaymentEditDao ped = db.paymentEditDao();

        l = new LinkedList<>(spd.getAllByDate(date));

        for(RecurringPayment rp: rpd.getFromDate(date)){
            List<PaymentEdit> lpe = ped.getEditsByPayment(rp.rp_id);
            SinglePayment sp = new SinglePayment();
            boolean skip = false;
            boolean add = false;
            sp.amount = rp.amount;
            sp.bank_id = rp.bankId;
            sp.name = rp.name;
            for(PaymentEdit pe: lpe){
                if(pe.edit_date == date || pe.move_to_date == date) {
                    if (pe.action == PaymentEdit.ACTION_SKIP) {
                        skip = true;
                    } else if (pe.action == PaymentEdit.ACTION_CHANGE_AMOUNT) {
                        sp.amount = pe.new_amount;
                    } else if (pe.action == PaymentEdit.ACTION_MOVE_DATE) {
                        sp.date = new CustomDate(pe.move_to_date);
                        add = true;
                    } else {
                        ped.delete(pe);
                    }
                }
            }
            if(rp.frequencyType == RecurringPayment.EVERY && !skip){
                CustomDate cd = new CustomDate(rp.start);
                while(cd.before(date)){
                    cd.addDays(rp.frequency);
                }
                if(cd.equals(date)){
                    add = true;
                }
            }
            else if(!skip){
                if(date.get(CustomDate.DAY) == rp.frequency){
                    add = true;
                }
            }

            if(add && !skip) {
                sp.date = date;
                l.add(sp);
            }
        }


        return l;
    }

    public static List<BankStatement> getStatementsOnDate(AppDatabase db, CustomDate date){


        return new LinkedList<>();
    }


}
