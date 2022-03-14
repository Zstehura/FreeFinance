package com.example.financefree;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.financefree.databaseClasses.AppDatabase;
import com.example.financefree.databaseClasses.BankAccount;
import com.example.financefree.databaseClasses.BankAccountDao;
import com.example.financefree.databaseClasses.BankStatement;
import com.example.financefree.databaseClasses.BankStatementDao;
import com.example.financefree.databaseClasses.PaymentEdit;
import com.example.financefree.databaseClasses.PaymentEditDao;
import com.example.financefree.databaseClasses.RecurringPayment;
import com.example.financefree.databaseClasses.RecurringPaymentDao;
import com.example.financefree.databaseClasses.SinglePayment;
import com.example.financefree.databaseClasses.SinglePaymentDao;
import com.example.financefree.databaseClasses.TaxBracket;
import com.example.financefree.databaseClasses.TaxBracketDao;
import com.example.financefree.structures.parseDate;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

@RunWith(AndroidJUnit4.class)
public class DatabaseTests {
    private TaxBracketDao tbd;
    private BankStatementDao bsd;
    private SinglePaymentDao spd;
    private RecurringPaymentDao rpd;
    private PaymentEditDao ped;
    private BankAccountDao bad;
    private AppDatabase db;


    @Before
    public void createDb(){
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
        tbd = db.taxBracketDao();
        bsd = db.bankStatementDao();
        spd = db.singlePaymentDao();
        rpd = db.recurringPaymentDao();
        ped = db.paymentEditDao();
        bad = db.bankAccountDao();
    }

    @After
    public void closeDb() throws IOException {
        db.close();
    }

    @Test
    public void readWriteBankAccount(){
        BankAccount ba = new BankAccount();
        ba.bank_id = -1;
        ba.accountName = "New Bank Account";
        ba.notes = "The first test bank account. Try making some changes or adding your own!";
        bad.insertAll(ba);

        BankAccount baActual = bad.getById(ba.bank_id);
        assert baActual.accountName.equals(ba.accountName) && ba.bank_id == baActual.bank_id;
    }

    @Test
    public void readWriteSinglePayment() {
        SinglePayment sp = new SinglePayment();
        sp.date = parseDate.getLong(3, 4, 2022);
        sp.name = "First payment - Netflix";
        sp.amount = -8.99;
        sp.bank_id = -2;
        sp.sp_id = -1;
        spd.insertAll(sp);
        SinglePayment spActual = spd.getPayment(sp.sp_id);

        assert spActual.name.equals(sp.name) && sp.sp_id == spActual.sp_id && sp.bank_id == spActual.bank_id;
    }

    @Test
    public void readWriteRecurringPayment() {
        RecurringPayment rp = new RecurringPayment();
        rp.name = "Example Paycheck";
        rp.frequencyType = RecurringPayment.EVERY;
        rp.frequency = 14;
        rp.startDate = parseDate.getLong(1, 7, 2022);
        rp.endDate = parseDate.getLong(1, 1, 2099);
        rp.amount = 500;
        rp.bankId = -2;
        rp.rp_id = -1;
        rp.notes = "This is a sample paycheck. It's set to repeat every 2 weeks, but you can change that to " +
                "however often you get paid!";
        rpd.insertAll(rp);

        RecurringPayment rpActual = rpd.getRecurringPayment(rp.rp_id);
        assert rp.rp_id == rpActual.rp_id && rp.name.equals(rpActual.name);
    }

    @Test
    public void readWriteBankStatement() {
        BankStatement bs = new BankStatement();
        bs.amount = 100;
        bs.bank_id = -2;
        bs.date = parseDate.getLong(3, 5, 2022);
        bs.s_id = -1;
        bsd.insertAll(bs);

        BankStatement bsActual = bsd.getStatement(bs.s_id);
        assert bs.s_id == bsActual.s_id && bs.amount == bsActual.amount;
    }

    @Test
    public void readWritePaymentEdit(){

        PaymentEdit pe = new PaymentEdit();
        pe.rp_id = -2;
        pe.edit_date = parseDate.getLong(3,18,2022);
        pe.action = PaymentEdit.ACTION_MOVE_DATE;
        pe.move_to_date = parseDate.getLong(3,17,2022);
        pe.edit_id = -1;
        ped.insertAll(pe);

        PaymentEdit peActual = ped.getEdit(pe.edit_id);
        assert pe.edit_id == peActual.edit_id && pe.action == peActual.action;
    }

}
