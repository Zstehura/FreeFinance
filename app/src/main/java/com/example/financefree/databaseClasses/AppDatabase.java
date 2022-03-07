package com.example.financefree.databaseClasses;


import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {BankAccount.class, BankStatement.class, RecurringPayment.class, PaymentEdit.class, SinglePayment.class,
        TaxBracket.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract BankAccountDao bankAccountDao();
    public abstract BankStatementDao bankStatementDao();
    public abstract RecurringPaymentDao recurringPaymentDao();
    public abstract PaymentEditDao paymentEditDao();
    public abstract SinglePaymentDao singlePaymentDao();
    public abstract TaxBracketDao taxBracketDao();
}
