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

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.observers.TestObserver;

@RunWith(AndroidJUnit4.class)
public class DBMgrTests {
    DatabaseManager dm;

    @Before
    public void init(){
        dm = new DatabaseManager(ApplicationProvider.getApplicationContext());
        //dm.clearDatabase();
    }

    @After
    public void close() {
        dm.close();
    }

    @Test
    public void testBankAccounts() {
        BankAccount ba = new BankAccount();
        ba.bank_id = 1;
        ba.name = "First Bank";
        ba.notes = "This is a new Bank";

        //dm.insert(ba);
        
        //List<BankAccount> l = dm.getAllBanks();
        //assert l != null;
        //assert l.size() == 1;
        //assert l.get(0).name.equals(ba.name);
    }

}
