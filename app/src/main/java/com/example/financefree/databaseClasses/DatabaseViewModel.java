package com.example.financefree.databaseClasses;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.room.RoomDatabase;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;


public class DatabaseViewModel extends AndroidViewModel {
    private final AppDatabase db;
    public DatabaseViewModel(@NonNull Application application) {
        super(application);
        db = AppDatabase.getAppDatabase(application);
    }

   // public Flowable<List<BankAccount>> getBankAccounts() {
   //     return db.bankAccountDao().getAll();
   // }

}
