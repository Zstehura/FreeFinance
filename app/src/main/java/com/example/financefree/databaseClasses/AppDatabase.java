package com.example.financefree.databaseClasses;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {BankAccount.class, BankStatement.class, RecurringPayment.class, PaymentEdit.class, SinglePayment.class,
        TaxBracket.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public static final String DB_NAME = "financefree_data";
    private static AppDatabase instance;

    static AppDatabase getAppDatabase(Context context) {
        if(instance == null) {
            instance = Room.databaseBuilder(context, AppDatabase.class, DB_NAME).build();
        }
        return instance;
    }

    public abstract BankAccountDao bankAccountDao();
    public abstract BankStatementDao bankStatementDao();
    public abstract RecurringPaymentDao recurringPaymentDao();
    public abstract PaymentEditDao paymentEditDao();
    public abstract SinglePaymentDao singlePaymentDao();
    public abstract TaxBracketDao taxBracketDao();
}
