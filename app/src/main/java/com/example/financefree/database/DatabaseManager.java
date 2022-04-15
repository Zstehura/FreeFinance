package com.example.financefree.database;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
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
    private static final String PREF_KEY = "settings";
    private static final String MEM_LEN_KEY = "mem_length";
    private static AppDatabase db;
    private static SharedPreferences sharedPreferences;
    private static int memLength;

    public DatabaseManager(Application application) {
        super(application);
        db = AppDatabase.getDatabase(application.getApplicationContext());
        sharedPreferences = application.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE);
        if(sharedPreferences.contains(MEM_LEN_KEY)) memLength = sharedPreferences.getInt(MEM_LEN_KEY, 365);
        else setMemLength(365);
    }


    public static DaoBankAccount getBankAccountDao(){
        return db.daoBankAccount();
    }
    public static DaoBankStatement getBankStatementDao() {return db.daoBankStatement();}
    public static DaoRecurringPayment getRecurringPaymentDao() {return db.daoRecurringPayment();}
    public static DaoSinglePayment getSinglePaymentDao() {return db.daoSinglePayment();}
    public static DaoPaymentEdit getPaymentEditDao() {return db.daoPaymentEdit();}

    public static int getMemLength() {return memLength;}
    public static void setMemLength(int length) {
        memLength = length;
        SharedPreferences.Editor e = sharedPreferences.edit();
        e.putInt(MEM_LEN_KEY, length);
        e.apply();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static List<Statement> getStatementsForDay(long date) {
        List<Statement> list = new ArrayList<>();
        for(BankAccount ba: db.daoBankAccount().getAll()){
            Statement s;
            BankStatement bs = db.daoBankStatement().getBanksLastStatementForDate(ba.bank_id, date);
            long mostRecent;
            double cAmount;
            if(bs == null) {
                mostRecent = DateParser.dateNumDaysAgo(memLength);
                cAmount = 0;
            }
            else {
                mostRecent = bs.date;
                cAmount = bs.amount;
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
                assert bs != null;
                s = new Statement(bs, ba.name);
                s.isCalculated = false;
            }
            list.add(s);
        }

        return list;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static List<Payment> getPaymentsForDay(long date) {
        List<Payment> list = new ArrayList<>();
        for(SinglePayment sp: db.daoSinglePayment().getAllOnDate(date)){
            list.add(new Payment(sp));
        }

        for(RecurringPayment rp: db.daoRecurringPayment().getRpsOnDate(date)){
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

    public void clearDatabase(){
        db.daoBankStatement().deleteAll();
        db.daoPaymentEdit().deleteAll();
        db.daoRecurringPayment().deleteAll();
        db.daoBankAccount().deleteAll();
        db.daoSinglePayment().deleteAll();
    }
    public void close() {db.close();}
}
