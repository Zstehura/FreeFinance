package com.example.financefree;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.financefree.database.entities.BankAccount;
import com.example.financefree.database.DatabaseManager;
import com.example.financefree.database.entities.BankStatement;
import com.example.financefree.database.entities.PaymentEdit;
import com.example.financefree.database.entities.RecurringPayment;
import com.example.financefree.database.entities.SinglePayment;
import com.example.financefree.structures.DateParser;
import com.example.financefree.structures.Payment;
import com.example.financefree.structures.Statement;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

@RunWith(AndroidJUnit4.class)
public class DBMgrTests {

    DatabaseManager dm;

    static long baId, rp1Id, rp2Id;

    static BankAccount ba;
    static BankStatement bs1, bs2;
    static PaymentEdit pe1, pe2;
    static RecurringPayment rp1, rp2;
    static SinglePayment sp1, sp2;

    @Before
    public void init(){
        dm = new DatabaseManager(ApplicationProvider.getApplicationContext());
        dm.clearDatabase();

        ba = new BankAccount();
        ba.name = "First Bank";
        ba.notes = "This is a new bank with a lot of interesting things!";
        baId = DatabaseManager.getBankAccountDao().insert(ba);

        bs1 = new BankStatement();
        bs1.date = DateParser.getLong(2,2,2022);
        bs1.amount = 250d;
        bs1.bank_id = baId;
        DatabaseManager.getBankStatementDao().insert(bs1);

        bs2 = new BankStatement();
        bs2.date = DateParser.getLong(2,15, 2022);
        bs2.amount = 900d;
        bs2.bank_id = baId;
        DatabaseManager.getBankStatementDao().insert(bs2);

        // Should come up on    2/14 & 2/28
        // amounts              0      500
        // After edit:          2/18 & 2/28
        //                      515    500
        rp1 = new RecurringPayment();
        rp1.name = "Paycheck";
        rp1.notes = "Example of a positive cashflow";
        rp1.type_option = 2;
        rp1.frequency_number = 2;
        rp1.start_date = DateParser.getLong(1,28,2022);
        rp1.end_date = DateParser.getLong(1,31,2100);
        rp1.amount = 500d;
        rp1.bank_id = baId;
        rp1Id = DatabaseManager.getRecurringPaymentDao().insert(rp1);

        // Should come up on    2/20
        // amount               0
        // after edit           2/19
        //                      -850
        rp2 = new RecurringPayment();
        rp2.name = "Rent";
        rp2.notes = "Example of a negative cashflow";
        rp2.type_option = 0;
        rp2.frequency_number = 20;
        rp2.amount = -850d;
        rp2.start_date = DateParser.getLong(1,1,2018);
        rp2.end_date = DateParser.getLong(5,15,2022);
        rp2.bank_id = baId;
        rp2Id = DatabaseManager.getRecurringPaymentDao().insert(rp2);

        pe1 = new PaymentEdit();
        pe1.edit_date = DateParser.getLong(2,14,2022);
        pe1.new_amount = 515d;
        pe1.new_date = DateParser.getLong(2,18,2022);
        pe1.rp_id = rp1Id;
        pe1.new_bank_id = rp1.bank_id;
        DatabaseManager.getPaymentEditDao().insert(pe1);

        pe2 = new PaymentEdit();
        pe2.edit_date = DateParser.getLong(2,20,2022);
        pe2.new_amount = rp2.amount;
        pe2.new_date = DateParser.getLong(2,19,2022);
        pe2.rp_id = rp2Id;
        pe2.new_bank_id = rp2.bank_id;
        DatabaseManager.getPaymentEditDao().insert(pe2);

        // 2/10 = -30
        sp1 = new SinglePayment();
        sp1.date = DateParser.getLong(2,10,2022);
        sp1.amount = -30d;
        sp1.name = "Date night";
        sp1.notes = "Going to a restaurant";
        sp1.bank_id = baId;
        DatabaseManager.getSinglePaymentDao().insert(sp1);

        // 2/5 = -120
        sp2 = new SinglePayment();
        sp2.notes = "My old one broke, oops";
        sp2.name = "New phone";
        sp2.amount = -120d;
        sp2.date = DateParser.getLong(2,5,2022);
        sp2.bank_id = baId;
        DatabaseManager.getSinglePaymentDao().insert(sp2);
    }

    @After
    public void close() {
        //dm.clearDatabase();
        dm.close();
    }

    @Test
    public void testBankAccounts() {
        List<BankAccount> l = DatabaseManager.getBankAccountDao().getAll();
        assert l != null;
        assert l.size() == 1;
        assert l.get(0).name.equals(ba.name);
    }

    @Test
    public void testPaymentGetter() {
        List<Payment> pActual205 = DatabaseManager.getPaymentsForDay(DateParser.getLong(2,5,2022));
        assert pActual205.size() == 1;
        assert pActual205.get(0).amount == -120d;
        assert pActual205.get(0).name.equals(sp2.name);
        assert pActual205.get(0).bankId == baId;
        assert pActual205.get(0).cType == 's';

        // 2/10 = -30
        // 's' noId
        List<Payment> pActual210 = DatabaseManager.getPaymentsForDay(DateParser.getLong(2,10,2022));
        assert pActual210.size() == 1;
        assert pActual210.get(0).amount == -30d;
        assert pActual210.get(0).name.equals(sp1.name);
        assert pActual210.get(0).bankId == baId;
        assert pActual210.get(0).cType == 's';

        // 2/14 = 0
        List<Payment> pActual214 = DatabaseManager.getPaymentsForDay(DateParser.getLong(2,14,2022));
        assert pActual214.size() == 0;

        // 2/18 = 515
        // 'r'  rp1Id
        List<Payment> pActual218 = DatabaseManager.getPaymentsForDay(DateParser.getLong(2,18,2022));
        assert pActual218.size() == 1;
        assert pActual218.get(0).amount == 515d;
        assert pActual218.get(0).name.equals(rp1.name);
        assert pActual218.get(0).bankId == baId;
        assert pActual218.get(0).cType == 'r';
        assert pActual218.get(0).id == rp1Id;

        // 2/19 = -850
        // 'r'  rp2Id
        List<Payment> pActual219 = DatabaseManager.getPaymentsForDay(DateParser.getLong(2,19,2022));
        assert pActual219.size() == 1;
        assert pActual219.get(0).amount == -850d;
        assert pActual219.get(0).name.equals(rp2.name);
        assert pActual219.get(0).bankId == baId;
        assert pActual219.get(0).cType == 'r';
        assert pActual219.get(0).id == rp2Id;

        // 2/20 = 0
        List<Payment> pActual220 = DatabaseManager.getPaymentsForDay(DateParser.getLong(2,20,2022));
        assert pActual220.size() == 0;

        // 2/28 = 500
        List<Payment> pActual228 = DatabaseManager.getPaymentsForDay(DateParser.getLong(2,28,2022));
        assert pActual228.size() == 1;
        assert pActual228.get(0).amount == 500d;
        assert pActual228.get(0).name.equals(rp1.name);
        assert pActual228.get(0).bankId == baId;
        assert pActual228.get(0).cType == 'r';
        assert pActual228.get(0).id == rp1Id;
    }


    @Test
    public void testStatementGetter() {
        List<Statement> s;
        long d = DateParser.getLong(2,1,2022);
        int n = 0;

        // 2/1
        s = DatabaseManager.getStatementsForDay(d);
        if(!s.get(n).bankName.equals(ba.name)) n = 1;
        assert s.get(n).bankName.equals(ba.name);
        assert s.get(n).bankId == baId;
        assert s.get(n).date == d;

        // 2/2   =250   = 250
        d++;
        s = DatabaseManager.getStatementsForDay(d);
        assert s.get(n).bankName.equals(ba.name);
        assert s.get(n).bankId == baId;
        assert s.get(n).date == d;
        assert s.get(n).amount == 250;

        // 2/3
        d++;
        s = DatabaseManager.getStatementsForDay(d);
        assert s.get(n).bankName.equals(ba.name);
        assert s.get(n).bankId == baId;
        assert s.get(n).date == d;
        assert s.get(n).amount == 250;

        // 2/4
        d++;
        s = DatabaseManager.getStatementsForDay(d);
        assert s.get(n).bankName.equals(ba.name);
        assert s.get(n).bankId == baId;
        assert s.get(n).date == d;
        assert s.get(n).amount == 250;

        // 2/5   -120   = 130
        d++;
        s = DatabaseManager.getStatementsForDay(d);
        assert s.get(n).bankName.equals(ba.name);
        assert s.get(n).bankId == baId;
        assert s.get(n).date == d;
        assert s.get(n).amount == 130;

        // 2/6
        d++;
        s = DatabaseManager.getStatementsForDay(d);
        assert s.get(n).bankName.equals(ba.name);
        assert s.get(n).bankId == baId;
        assert s.get(n).date == d;
        assert s.get(n).amount == 130;

        // 2/7
        d++;
        s = DatabaseManager.getStatementsForDay(d);
        assert s.get(n).bankName.equals(ba.name);
        assert s.get(n).bankId == baId;
        assert s.get(n).date == d;
        assert s.get(n).amount == 130;

        // 2/8
        d++;
        s = DatabaseManager.getStatementsForDay(d);
        assert s.get(n).bankName.equals(ba.name);
        assert s.get(n).bankId == baId;
        assert s.get(n).date == d;
        assert s.get(n).amount == 130;

        // 2/9
        d++;
        s = DatabaseManager.getStatementsForDay(d);
        assert s.get(n).bankName.equals(ba.name);
        assert s.get(n).bankId == baId;
        assert s.get(n).date == d;
        assert s.get(n).amount == 130;

        // 2/10  -30    = 100
        d++;
        s = DatabaseManager.getStatementsForDay(d);
        assert s.get(n).bankName.equals(ba.name);
        assert s.get(n).bankId == baId;
        assert s.get(n).date == d;
        assert s.get(n).amount == 100;

        // 2/11
        d++;
        s = DatabaseManager.getStatementsForDay(d);
        assert s.get(n).bankName.equals(ba.name);
        assert s.get(n).bankId == baId;
        assert s.get(n).date == d;
        assert s.get(n).amount == 100;

        // 2/12
        d++;
        s = DatabaseManager.getStatementsForDay(d);
        assert s.get(n).bankName.equals(ba.name);
        assert s.get(n).bankId == baId;
        assert s.get(n).date == d;
        assert s.get(n).amount == 100;

        // 2/13
        d++;
        s = DatabaseManager.getStatementsForDay(d);
        assert s.get(n).bankName.equals(ba.name);
        assert s.get(n).bankId == baId;
        assert s.get(n).date == d;
        assert s.get(n).amount == 100;

        // 2/14  +0     = 100
        d++;
        s = DatabaseManager.getStatementsForDay(d);

        assert s.get(n).bankName.equals(ba.name);
        assert s.get(n).bankId == baId;
        assert s.get(n).date == d;
        assert s.get(n).amount == 100;

        // 2/15  =900   = 900
        d++;
        s = DatabaseManager.getStatementsForDay(d);
        assert s.get(n).bankName.equals(ba.name);
        assert s.get(n).bankId == baId;
        assert s.get(n).date == d;
        assert s.get(n).amount == 900;

        // 2/16
        d++;
        s = DatabaseManager.getStatementsForDay(d);
        assert s.get(n).bankName.equals(ba.name);
        assert s.get(n).bankId == baId;
        assert s.get(n).date == d;
        assert s.get(n).amount == 900;

        // 2/17
        d++;
        s = DatabaseManager.getStatementsForDay(d);
        assert s.get(n).bankName.equals(ba.name);
        assert s.get(n).bankId == baId;
        assert s.get(n).date == d;
        assert s.get(n).amount == 900;

        // 2/18  +515   =1415
        d++;
        s = DatabaseManager.getStatementsForDay(d);
        assert s.get(n).bankName.equals(ba.name);
        assert s.get(n).bankId == baId;
        assert s.get(n).date == d;
        assert s.get(n).amount == 1415;

        // 2/19  -850   = 565
        d++;
        s = DatabaseManager.getStatementsForDay(d);
        assert s.get(n).bankName.equals(ba.name);
        assert s.get(n).bankId == baId;
        assert s.get(n).date == d;
        assert s.get(n).amount == 565;

        // 2/20  +0     = 565
        d++;
        s = DatabaseManager.getStatementsForDay(d);
        assert s.get(n).bankName.equals(ba.name);
        assert s.get(n).bankId == baId;
        assert s.get(n).date == d;
        assert s.get(n).amount == 565;

        // 2/21
        d++;
        s = DatabaseManager.getStatementsForDay(d);
        assert s.get(n).bankName.equals(ba.name);
        assert s.get(n).bankId == baId;
        assert s.get(n).date == d;
        assert s.get(n).amount == 565;

        // 2/22
        d++;
        s = DatabaseManager.getStatementsForDay(d);
        assert s.get(n).bankName.equals(ba.name);
        assert s.get(n).bankId == baId;
        assert s.get(n).date == d;
        assert s.get(n).amount == 565;

        // 2/23
        d++;
        s = DatabaseManager.getStatementsForDay(d);
        assert s.get(n).bankName.equals(ba.name);
        assert s.get(n).bankId == baId;
        assert s.get(n).date == d;
        assert s.get(n).amount == 565;

        // 2/24
        d++;
        s = DatabaseManager.getStatementsForDay(d);
        assert s.get(n).bankName.equals(ba.name);
        assert s.get(n).bankId == baId;
        assert s.get(n).date == d;
        assert s.get(n).amount == 565;

        // 2/25
        d++;
        s = DatabaseManager.getStatementsForDay(d);
        assert s.get(n).bankName.equals(ba.name);
        assert s.get(n).bankId == baId;
        assert s.get(n).date == d;
        assert s.get(n).amount == 565;

        // 2/26
        d++;
        s = DatabaseManager.getStatementsForDay(d);
        assert s.get(n).bankName.equals(ba.name);
        assert s.get(n).bankId == baId;
        assert s.get(n).date == d;
        assert s.get(n).amount == 565;

        // 2/27
        d++;
        s = DatabaseManager.getStatementsForDay(d);
        assert s.get(n).bankName.equals(ba.name);
        assert s.get(n).bankId == baId;
        assert s.get(n).date == d;
        assert s.get(n).amount == 565;

        // 2/28  +500   =1065
        d++;
        s = DatabaseManager.getStatementsForDay(d);
        assert s.get(n).bankName.equals(ba.name);
        assert s.get(n).bankId == baId;
        assert s.get(n).date == d;
        assert s.get(n).amount == 1065;

        // 2/29
        d++;
        s = DatabaseManager.getStatementsForDay(d);
        assert s.get(n).bankName.equals(ba.name);
        assert s.get(n).bankId == baId;
        assert s.get(n).date == d;
        assert s.get(n).amount == 1065;

        // 2/30
        d++;
        s = DatabaseManager.getStatementsForDay(d);
        assert s.get(n).bankName.equals(ba.name);
        assert s.get(n).bankId == baId;
        assert s.get(n).date == d;
        assert s.get(n).amount == 1065;
    }

}
