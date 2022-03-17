package com.example.financefree;

import androidx.room.Room;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.financefree.databaseClasses.AppDatabase;
import com.example.financefree.databaseClasses.BankAccount;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.atomic.AtomicReference;

import io.reactivex.internal.observers.DisposableLambdaObserver;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;

@RunWith(AndroidJUnit4.class)
public class RxJavaTests {
    private AppDatabase db;

    @Before
    public void initializeDB() {
        db = Room.inMemoryDatabaseBuilder(
                InstrumentationRegistry.getInstrumentation().getContext(),
                AppDatabase.class)
                .build();
    }

    @After
    public void closeDB() {
        db.close();
    }


    @Test
    public void testBankAccount() {
        BankAccount ba = new BankAccount();
        ba.accountName = "New Account";
        ba.notes = "This is a new account";
        ba.bank_id = 1;



        Single<BankAccount> baActual = db.bankAccountDao().getById(ba.bank_id);
        baActual.test().assertComplete();
        assert baActual.blockingGet().accountName.equals(ba.accountName);
    }
}
