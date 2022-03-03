package com.example.financefree.databaseClasses;


import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {BankAccount.class, Statement.class, RecurringPayment.class, PaymentEdit.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract BankAccountDao bankAccountDao();
    public abstract BankStatementDao bankStatementDao();
    public abstract RecurringPaymentDao recurringPaymentDao();
}
