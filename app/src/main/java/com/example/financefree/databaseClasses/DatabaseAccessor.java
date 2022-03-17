package com.example.financefree.databaseClasses;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import com.example.financefree.structures.parseDate;
import com.example.financefree.structures.payment;
import com.example.financefree.structures.statement;

import java.nio.channels.AsynchronousChannelGroup;
import java.util.LinkedList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DatabaseAccessor extends AndroidViewModel {

    private static AppDatabase db;

    public DatabaseAccessor(Application application){
        super(application);
        db = AppDatabase.getAppDatabase(application);
    }

    public static List<payment> getPaymentsOnDate(long date){
        List<payment> l = new LinkedList<>();
        SinglePaymentDao spd = db.singlePaymentDao();
        RecurringPaymentDao rpd = db.recurringPaymentDao();
        PaymentEditDao ped = db.paymentEditDao();

        for(SinglePayment sp: spd.getAllByDate(date)){
            payment p = new payment(sp.amount, sp.date,sp.name,sp.bank_id, 's', sp.sp_id);
            l.add(p);
        }

        for(RecurringPayment rp: rpd.getFromDate(date)){
            List<PaymentEdit> lpe = ped.getEditsByPayment(rp.rp_id);
            payment p = new payment(rp.amount,date,rp.name,rp.bankId, 'r', rp.rp_id);
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

    public static List<statement> getStatementsOnDate(long date){
        List<statement> l = new LinkedList<>();
        BankStatementDao bsd = db.bankStatementDao();
        BankAccountDao bad = db.bankAccountDao();
/*
        List<BankAccount> bal = bad.getAll();
        for(BankAccount ba: bal){
            statement s = new statement(ba.bank_id, 0, date, ba.accountName);
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
                List<payment> paymentList = getPaymentsOnDate(d);
                for(payment p: paymentList){
                    if(p.bankId == s.bankId){
                        s.amount += p.amount;
                    }
                }
                d++;
            }
            l.add(s);
        }
*/
        return l;
    }

    //public static List<Long> getBankIds(){
    //    Single<List<Long>> lTemp; //= db.bankAccountDao().getAllBankIDs().onErrorReturnItem(new LinkedList<>());
    //    return lTemp.blockingGet();
    //}

    //public static String getBankName(long id) {
    //    Single<String> str = db.bankAccountDao().getName(id).onErrorReturnItem("");
    //    return str.blockingGet();
    //}

    public static Flowable<List<BankAccount>> getBankAccounts() {return db.bankAccountDao().getAll();}
    public static Disposable insertBankAccounts(BankAccount... ba) {
        return Completable.fromRunnable(() -> {
            db.bankAccountDao().insertAll(ba);
        })
                .subscribeOn(Schedulers.io())
                .subscribe();
    }

    public static boolean clearData(long date) {
        BankAccountDao bad = db.bankAccountDao();
        BankStatementDao bsd = db.bankStatementDao();
        PaymentEditDao ped = db.paymentEditDao();
        RecurringPaymentDao rpd = db.recurringPaymentDao();
        SinglePaymentDao spd = db.singlePaymentDao();
        if(date == 0) {
            // bad.deleteAll();
            bsd.deleteAll();
            ped.deleteAll();
            rpd.deleteAll();
            spd.deleteAll();
        }
        else {
            bsd.deleteOlderThan(date);
            ped.deleteOlderThan(date);
            rpd.deleteOlderThan(date);
            spd.deleteOlderThan(date);
        }

        return true;
    }

    //TODO: Make taxbracket update
    //  getEstimatedIncomeTax
}
