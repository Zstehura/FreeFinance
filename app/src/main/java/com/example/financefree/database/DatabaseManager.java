package com.example.financefree.database;

import android.app.Application;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;

import com.example.financefree.database.daos.DaoBankAccount;
import com.example.financefree.database.daos.DaoBankStatement;
import com.example.financefree.database.daos.DaoPaymentEdit;
import com.example.financefree.database.daos.DaoRecurringPayment;
import com.example.financefree.database.daos.DaoSinglePayment;
import com.example.financefree.database.entities.BankAccount;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

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
