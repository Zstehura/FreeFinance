package com.example.financefree.database;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.AndroidViewModel;
import androidx.preference.Preference;

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
    public static DaoPaymentEdit getPaymentEditDao() {return db.daoPaymentEdit();}

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static List<Statement> getStatementsForDay(long date) {
        List<Statement> list = new ArrayList<>();
        for(BankAccount ba: db.daoBankAccount().getAll()){
            Statement s;
            BankStatement bs = db.daoBankStatement().getBanksLastStatementForDate(ba.bank_id, date);
            long mostRecent;
            double cAmount;

            if (bs != null){
                mostRecent = bs.date;
                cAmount = bs.amount;
            }
            else {
                mostRecent = DateParser.dateNumDaysAgo(365);
                cAmount =0;
            }

            if(mostRecent != date) {
                for (SinglePayment sp: db.daoSinglePayment().getAllForBankBetween(mostRecent, date, ba.bank_id)){
                    cAmount += sp.amount;
                }

                for(RecurringPayment rp: db.daoRecurringPayment().getRpsForBankBetween(mostRecent, date, ba.bank_id)){
                    List<PaymentEdit> pel = db.daoPaymentEdit().getEditsForRpBetween(mostRecent, date, rp.rp_id);
                    for(long d = mostRecent; d <= date; d++){
                        Payment p = Frequency.occursOn(pel, rp, d);
                        if(p != null) cAmount += p.amount;
                    }
                }
                s = new Statement(ba.bank_id, cAmount, date, ba.name,-1 );
                s.isCalculated = true;
            }
            else{
                s = new Statement(bs, ba.name);
                s.isCalculated = false;
            }
            list.add(s);
        }

        return list;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static List<Payment> getPaymentsForDay(long date) {
        final String TAG = "PaymentGetter";

        List<Payment> list = new ArrayList<>();
        for(SinglePayment sp: db.daoSinglePayment().getAllOnDate(date)){
            list.add(new Payment(sp));
        }
        List<RecurringPayment> recurringPaymentList = db.daoRecurringPayment().getRpsOnDate(date);
        for(RecurringPayment rp: recurringPaymentList){
            List<PaymentEdit> pel = db.daoPaymentEdit().getEditsForRpOnDate(date, rp.rp_id);
            Payment p = new Payment(rp, date);
            boolean skip = false, addOn = false;
            if(pel != null) {
                for(PaymentEdit pe: pel) {
                    if(pe.edit_date == date && (pe.skip || pe.new_date != date)) skip = true;
                    else if(pe.new_date == date) {
                        addOn = true;
                        p.bankId = pe.new_bank_id;
                        p.amount = pe.new_amount;
                    }
                }
            }


            if(addOn && !skip) {
                list.add(p);
            }
            else if(!skip && Frequency.occursOn(rp, date)) {
                list.add(p);
            }
        }

        return list;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void cleanUpDatabase(int memLength) {
        long d = DateParser.dateNumDaysAgo(memLength);
        db.daoRecurringPayment().deleteOlderThan(d);
        db.daoSinglePayment().deleteOlderThan(d);
        db.daoPaymentEdit().deleteOlderThan(d);
        db.daoBankStatement().deleteOlderThan(d);
    }

    public static void clearDatabase(){
        db.daoBankStatement().deleteAll();
        db.daoPaymentEdit().deleteAll();
        db.daoRecurringPayment().deleteAll();
        db.daoBankAccount().deleteAll();
        db.daoSinglePayment().deleteAll();
    }
    public void close() {db.close();}
}
