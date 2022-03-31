package com.example.financefree;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.financefree.database.entities.BankAccount;
import com.example.financefree.database.DatabaseManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class DBMgrTests {
    DatabaseManager dm;

    @Before
    public void init(){
        //dm = new DatabaseManager(ApplicationProvider.getApplicationContext());
    }

    @After
    public void close() {
        //dm.close();
    }

    @Test
    public void testBankAccounts() {
        BankAccount eba = new BankAccount();
        eba.bankId = 1;
        eba.name = "First Bank";
        eba.notes = "This is a new Bank";

        //dm.insertBankAccounts(eba);

        //assert dm.getAllBankAccounts().size() > 0;
    }

}
