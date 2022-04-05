package com.example.financefree.database;

import android.app.Application;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;

import com.example.financefree.database.daos.DaoBankAccount;
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

    /*
    public void clearDatabase() {
        db.daoBankAccount().deleteAll().blockingAwait(1, TimeUnit.SECONDS);
    }

    public void insert(BankAccount ba) {
        db.daoBankAccount().insertAll(ba)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .blockingAwait(1, TimeUnit.SECONDS);
    }

    public Observable<List<BankAccount>> getBankListObs() {
        return db.daoBankAccount().getAll()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .toObservable();
    }

    public BankAccount getBankAccount(long id) {
        return db.daoBankAccount().getBankAccount(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .blockingGet(new BankAccount());
    }

    public List<BankAccount> getAllBanks() {
        return db.daoBankAccount().getAll()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .blockingGet(new ArrayList<>());
    }

     */

    public void close() {db.close();}

}
