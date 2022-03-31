package com.example.financefree;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.financefree.database.entities.BankAccount;
import com.example.financefree.database.DatabaseManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import io.reactivex.rxjava3.observers.TestObserver;

@RunWith(AndroidJUnit4.class)
public class DBTest {
    DatabaseManager databaseManager;

    @Before
    public void init(){
        databaseManager = new DatabaseManager(ApplicationProvider.getApplicationContext());
    }

    @After
    public void close(){databaseManager.close();}

    @Test
    public void testBankAccount() {
        BankAccount ba = new BankAccount();
        ba.name = "New Bank";
        ba.notes = "This is a new bank!\nIsn't it swell?!";

        // databaseManager.daoBankAccount.insertAll(ba);

        TestObserver<List<BankAccount>> sub = new TestObserver<>();
        databaseManager.allBanksObs().subscribe(sub);
        sub.assertComplete().assertValueCount(1);
    }


}
