package com.example.financefree.database;

import android.app.Application;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.AndroidViewModel;

import com.example.financefree.database.daos.DaoBankAccount;
import com.example.financefree.database.daos.DaoBankStatement;
import com.example.financefree.database.daos.DaoPaymentEdit;
import com.example.financefree.database.daos.DaoRecurringPayment;
import com.example.financefree.database.daos.DaoSinglePayment;
import com.example.financefree.database.entities.BankAccount;
import com.example.financefree.database.entities.BankStatement;
import com.example.financefree.database.entities.PaymentEdit;
import com.example.financefree.database.entities.RecurringPayment;
import com.example.financefree.database.entities.SinglePayment;
import com.example.financefree.structures.DateParser;
import com.example.financefree.structures.Frequency;
import com.example.financefree.structures.Payment;
import com.example.financefree.structures.Statement;

import java.util.ArrayList;
import java.util.List;


@SuppressWarnings("ResultOfMethodCallIgnored")
public class DatabaseManager extends AndroidViewModel {
    private static AppDatabase db;

    public DatabaseManager(Application application) {
        super(application);
        db = AppDatabase.getDatabase(application.getApplicationContext());
    }

    public static DaoBankAccount getBankAccountDao(){
        return db.daoBankAccount();
    }
    public static DaoBankStatement getBankStatementDao() {return db.daoBankStatement();}
    public static DaoRecurringPayment getRecurringPaymentDao() {return db.daoRecurringPayment();}
    public static DaoSinglePayment getSinglePaymentDao() {return db.daoSinglePayment();}
    public static DaoPaymentEdit getPaymentEdit() {return db.daoPaymentEdit();}

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static List<Statement> getStatementsForDay(long date) {
        List<Statement> list = new ArrayList<>();
        Thread t = new Thread(() ->{
            for(BankAccount ba: db.daoBankAccount().getAll()){
                Statement s;
                BankStatement bs = db.daoBankStatement().getBanksStatementForDate(ba.bank_id, date);
                long mostRecent = 0;
                double cAmount = 0;
                if(bs == null) {
                    for(BankStatement bsInner: db.daoBankStatement().getBanksStatements(ba.bank_id)){
                        if(bsInner.date > mostRecent && bsInner.date <= date){
                            mostRecent = bsInner.date;
                            cAmount = bsInner.amount;
                        }
                    }

                    while(mostRecent <= date){
                        for(Payment p: getPaymentsForDay(mostRecent)){
                            if(p.bankId == ba.bank_id){
                                cAmount += p.amount;
                            }
                        }
                        mostRecent++;
                    }

                    s = new Statement(ba.bank_id, cAmount, date, ba.name );
                }
                else{
                    s = new Statement(bs, ba.name);
                }
                list.add(s);
            }
        });

        t.start();
        try{t.join(1000);}
        catch (InterruptedException e) {Log.e("DatabaseManager", e.getMessage());}

        return list;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static List<Payment> getPaymentsForDay(long date) {
        List<Payment> list = new ArrayList<>();
        Thread t = new Thread(() -> {
            for(SinglePayment sp: db.daoSinglePayment().getAllOnDate(date)){
                list.add(new Payment(sp));
            }

            for(RecurringPayment rp: db.daoRecurringPayment().getRpsOnDate(date)){
                List<PaymentEdit> pel = db.daoPaymentEdit().getEditsForRpOnDate(date, rp.rp_id);
                Payment p = new Payment(rp, date);
                boolean skip = false, addOn = false;
                if(pel != null) {
                    for(PaymentEdit pe: pel) {
                        if(pe.edit_date == date && pe.skip) skip = true;
                        else if(pe.new_date == date || !(pe.edit_date == date)) {
                            addOn = true;
                            p.bankId = pe.new_bank_id;
                            p.amount = pe.new_amount;
                        }
                    }
                }
                if(addOn && !skip) {
                    list.add(p);
                }
                else if(!skip && (new Frequency(rp)).occursOn(date)) {
                    list.add(p);
                }
            }
        });
        t.start();
        try{ t.join();}
        catch (InterruptedException e) {Log.e("DatabaseManager", e.getMessage());}

        return list;
    }

    public void close() {db.close();}
}
