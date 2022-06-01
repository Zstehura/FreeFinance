package com.example.financefree.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

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

@Database(entities = {BankAccount.class, BankStatement.class, PaymentEdit.class, RecurringPayment.class,
        SinglePayment.class},
        version = 5, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract DaoBankAccount daoBankAccount();
    public abstract DaoBankStatement daoBankStatement();
    public abstract DaoPaymentEdit daoPaymentEdit();
    public abstract DaoRecurringPayment daoRecurringPayment();
    public abstract DaoSinglePayment daoSinglePayment();

    private static AppDatabase INSTANCE;
    public static AppDatabase getDatabase(final Context context){
        if(INSTANCE == null || !INSTANCE.isOpen()) {
            synchronized (AppDatabase.class) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabase.class, "freefinance_data")
                        .fallbackToDestructiveMigration()
                        .build();
            }
        }
        return INSTANCE;
    }
}
