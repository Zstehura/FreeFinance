package com.example.financefree.database;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import com.example.financefree.database.daos.DaoBankAccount;
import com.example.financefree.database.daos.DaoBankStatement;
import com.example.financefree.database.daos.DaoPaymentEdit;
import com.example.financefree.database.daos.DaoRecurringPayment;
import com.example.financefree.database.daos.DaoSinglePayment;



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

    public void close() {db.close();}
}
