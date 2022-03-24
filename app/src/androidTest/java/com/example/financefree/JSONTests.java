package com.example.financefree;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.financefree.fileClasses.BankAccount;
import com.example.financefree.fileClasses.DataManager;
import com.example.financefree.fileClasses.PaymentEdit;
import com.example.financefree.fileClasses.RecurringPayment;
import com.example.financefree.fileClasses.SinglePayment;
import com.example.financefree.structures.parseDate;
import com.example.financefree.structures.payment;
import com.example.financefree.structures.statement;

import org.json.JSONException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class JSONTests {
    static long baId, rp1Id, rp2Id;
    static BankAccount ba;
    static PaymentEdit pe1, pe2;
    static RecurringPayment rp1, rp2;
    static SinglePayment sp1, sp2;


    @Before
    public void initialize() throws JSONException, IOException {
        // For all tests, focus on the month of 2/2022 which is I think March
        DataManager.initData(InstrumentationRegistry.getInstrumentation().getTargetContext());

        ba = new BankAccount();
        ba.name = "First Bank";
        ba.notes = "This is a new bank with a lot of interesting things!";
        ba.statements.put(parseDate.getLong(2,2,2022), 250d);   // 2/2  = 250
        ba.statements.put(parseDate.getLong(2,15, 2022), 900d); // 2/15 = 900
        baId = DataManager.insertBankAccount(ba);

        // Should come up on    2/14 & 2/28
        // amounts              0      500
        // After edit:          2/18 & 2/28
        //                      515    500
        rp1 = new RecurringPayment();
        rp1.name = "Paycheck";
        rp1.notes = "Example of a positive cashflow";
        rp1.frequencyType = RecurringPayment.FREQ_EVERY_NUM_DAYS;
        rp1.frequencyNum = 14;
        rp1.startDate = parseDate.getLong(1,28,2022);
        rp1.endDate = parseDate.getLong(1,31,2100);
        rp1.amount = 500d;
        rp1.bankId = baId;
        rp1Id = DataManager.insertRecurringPayment(rp1);

        // Should come up on    2/20
        // amount               0
        // after edit           2/19
        //                      -850
        rp2 = new RecurringPayment();
        rp2.name = "Rent";
        rp2.notes = "Example of a negative cashflow";
        rp2.frequencyType = RecurringPayment.FREQ_ON_DATE_MONTHLY;
        rp2.frequencyNum = 20;
        rp2.amount = -850d;
        rp2.startDate = parseDate.getLong(1,1,2018);
        rp2.endDate = parseDate.getLong(5,15,2022);
        rp2.bankId = baId;
        rp2Id = DataManager.insertRecurringPayment(rp2);

        pe1 = new PaymentEdit();
        pe1.editDate = parseDate.getLong(2,14,2022);
        pe1.newAmount = 515d;
        pe1.newDate = parseDate.getLong(2,18,2022);
        rp1.edits.put(pe1.editDate, pe1);
        DataManager.updateRecurringPayment(rp1, rp1Id);

        pe2 = new PaymentEdit();
        pe2.editDate = parseDate.getLong(2,20,2022);
        pe2.newDate = parseDate.getLong(2,19,2022);
        rp2.edits.put(pe2.editDate, pe2);
        DataManager.updateRecurringPayment(rp2, rp2Id);

        // 2/10 = -30
        sp1 = new SinglePayment();
        sp1.date = parseDate.getLong(2,10,2022);
        sp1.amount = -30d;
        sp1.name = "Date night";
        sp1.notes = "Going to a restaurant";
        sp1.bankId = baId;
        DataManager.insertSinglePayment(sp1);

        // 2/5 = -120
        sp2 = new SinglePayment();
        sp2.bankId = baId;
        sp2.notes = "My old one broke, oops";
        sp2.name = "New phone";
        sp2.amount = -120d;
        sp2.date = parseDate.getLong(2,5,2022);
        DataManager.insertSinglePayment(sp2);
    }

    @After
    public void closeFiles() {
        DataManager.clearAllData();
        try {
            DataManager.close(InstrumentationRegistry.getInstrumentation().getTargetContext());
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testBankAccount() throws JSONException, IOException {
        BankAccount baActual = DataManager.getBankAccount(baId);
        assert baActual != null;
        assert baActual.name.equals(ba.name);
        assert baActual.notes.equals(ba.notes);
        assert baActual.statements.equals(ba.statements);
    }

    @Test
    public void testRecurringPayment() {
        // rp1
        RecurringPayment rpActual1 = DataManager.getRecurringPayment(rp1Id);
        assert rpActual1.name.equals(rp1.name);
        assert rpActual1.notes.equals(rp1.notes);
        assert rpActual1.frequencyType == rp1.frequencyType;
        assert rpActual1.frequencyNum == rp1.frequencyNum;
        assert rpActual1.startDate == rp1.startDate;
        assert rpActual1.endDate == rp1.endDate;
        assert rpActual1.amount == rp1.amount;
        assert rpActual1.bankId == rp1.bankId;
        
        // rp2
        RecurringPayment rpActual2 = DataManager.getRecurringPayment(rp2Id);
        assert rpActual2.name.equals(rp2.name);
        assert rpActual2.notes.equals(rp2.notes);
        assert rpActual2.frequencyType == rp2.frequencyType;
        assert rpActual2.frequencyNum == rp2.frequencyNum;
        assert rpActual2.startDate == rp2.startDate;
        assert rpActual2.endDate == rp2.endDate;
        assert rpActual2.amount == rp2.amount;
        assert rpActual2.bankId == rp2.bankId;
    }

    @Test
    public void testSinglePayment() {
        // sp1
        SinglePayment spActual1 = DataManager.getSinglePayments(sp1.date).get(0);
        assert spActual1.name.equals(sp1.name);
        assert spActual1.notes.equals(sp1.notes);
        assert spActual1.date == sp1.date;
        assert spActual1.amount == sp1.amount;
        assert spActual1.bankId == sp1.bankId;

        // sp2
        SinglePayment spActual2 = DataManager.getSinglePayments(sp2.date).get(0);
        assert spActual2.name.equals(sp2.name);
        assert spActual2.notes.equals(sp2.notes);
        assert spActual2.date == sp2.date;
        assert spActual2.amount == sp2.amount;
        assert spActual2.bankId == sp2.bankId;
    }

    @Test
    public void testPaymentGetter(){
        // 2/5  = -120
        // 's'  noId
        List<payment> pActual205 = DataManager.getPaymentsOnDate(parseDate.getLong(2,5,2022));
        assert pActual205.size() == 1;
        assert pActual205.get(0).amount == -120d;
        assert pActual205.get(0).name.equals(sp2.name);
        assert pActual205.get(0).bankId == baId;
        assert pActual205.get(0).cType == 's';

        // 2/10 = -30
        // 's' noId
        List<payment> pActual210 = DataManager.getPaymentsOnDate(parseDate.getLong(2,10,2022));
        assert pActual210.size() == 1;
        assert pActual210.get(0).amount == -30d;
        assert pActual210.get(0).name.equals(sp1.name);
        assert pActual210.get(0).bankId == baId;
        assert pActual210.get(0).cType == 's';

        // 2/14 = 0
        List<payment> pActual214 = DataManager.getPaymentsOnDate(parseDate.getLong(2,14,2022));
        assert pActual214.size() == 0;

        // 2/18 = 515
        // 'r'  rp1Id
        List<payment> pActual218 = DataManager.getPaymentsOnDate(parseDate.getLong(2,18,2022));
        assert pActual218.size() == 1;
        assert pActual218.get(0).amount == 515d;
        assert pActual218.get(0).name.equals(rp1.name);
        assert pActual218.get(0).bankId == baId;
        assert pActual218.get(0).cType == 'r';
        assert pActual218.get(0).id == rp1Id;

        // 2/19 = -850
        // 'r'  rp2Id
        List<payment> pActual219 = DataManager.getPaymentsOnDate(parseDate.getLong(2,19,2022));
        assert pActual219.size() == 1;
        assert pActual219.get(0).amount == -850d;
        assert pActual219.get(0).name.equals(rp2.name);
        assert pActual219.get(0).bankId == baId;
        assert pActual219.get(0).cType == 'r';
        assert pActual219.get(0).id == rp2Id;

        // 2/20 = 0
        List<payment> pActual220 = DataManager.getPaymentsOnDate(parseDate.getLong(2,20,2022));
        assert pActual220.size() == 0;

        // 2/28 = 500
        List<payment> pActual228 = DataManager.getPaymentsOnDate(parseDate.getLong(2,28,2022));
        assert pActual228.size() == 1;
        assert pActual228.get(0).amount == 500d;
        assert pActual228.get(0).name.equals(rp1.name);
        assert pActual228.get(0).bankId == baId;
        assert pActual228.get(0).cType == 'r';
        assert pActual228.get(0).id == rp1Id;
    }

    @Test
    public void testStatementGetter() {
        List<statement> s = null;
        long d = parseDate.getLong(2,1,2022);
        int n = 0;

        // 2/1
        s = DataManager.getStatementsOnDate(d);
        if(!s.get(n).bankName.equals(ba.name)) n = 1;
        assert s.get(n).bankName.equals(ba.name);
        assert s.get(n).bankId == baId;
        assert s.get(n).date == d;
        // assert s.get(n).amount == 0;

        // 2/2   =250   = 250
        d++;
        s = DataManager.getStatementsOnDate(d);
        assert s.get(n).bankName.equals(ba.name);
        assert s.get(n).bankId == baId;
        assert s.get(n).date == d;
        assert s.get(n).amount == 250;

        // 2/3
        d++;
        s = DataManager.getStatementsOnDate(d);
        assert s.get(n).bankName.equals(ba.name);
        assert s.get(n).bankId == baId;
        assert s.get(n).date == d;
        assert s.get(n).amount == 250;

        // 2/4
        d++;
        s = DataManager.getStatementsOnDate(d);
        assert s.get(n).bankName.equals(ba.name);
        assert s.get(n).bankId == baId;
        assert s.get(n).date == d;
        assert s.get(n).amount == 250;

        // 2/5   -120   = 130
        d++;
        s = DataManager.getStatementsOnDate(d);
        assert s.get(n).bankName.equals(ba.name);
        assert s.get(n).bankId == baId;
        assert s.get(n).date == d;
        assert s.get(n).amount == 130;

        // 2/6
        d++;
        s = DataManager.getStatementsOnDate(d);
        assert s.get(n).bankName.equals(ba.name);
        assert s.get(n).bankId == baId;
        assert s.get(n).date == d;
        assert s.get(n).amount == 130;

        // 2/7
        d++;
        s = DataManager.getStatementsOnDate(d);
        assert s.get(n).bankName.equals(ba.name);
        assert s.get(n).bankId == baId;
        assert s.get(n).date == d;
        assert s.get(n).amount == 130;

        // 2/8
        d++;
        s = DataManager.getStatementsOnDate(d);
        assert s.get(n).bankName.equals(ba.name);
        assert s.get(n).bankId == baId;
        assert s.get(n).date == d;
        assert s.get(n).amount == 130;

        // 2/9
        d++;
        s = DataManager.getStatementsOnDate(d);
        assert s.get(n).bankName.equals(ba.name);
        assert s.get(n).bankId == baId;
        assert s.get(n).date == d;
        assert s.get(n).amount == 130;

        // 2/10  -30    = 100
        d++;
        s = DataManager.getStatementsOnDate(d);
        assert s.get(n).bankName.equals(ba.name);
        assert s.get(n).bankId == baId;
        assert s.get(n).date == d;
        assert s.get(n).amount == 100;

        // 2/11
        d++;
        s = DataManager.getStatementsOnDate(d);
        assert s.get(n).bankName.equals(ba.name);
        assert s.get(n).bankId == baId;
        assert s.get(n).date == d;
        assert s.get(n).amount == 100;

        // 2/12
        d++;
        s = DataManager.getStatementsOnDate(d);
        assert s.get(n).bankName.equals(ba.name);
        assert s.get(n).bankId == baId;
        assert s.get(n).date == d;
        assert s.get(n).amount == 100;

        // 2/13
        d++;
        s = DataManager.getStatementsOnDate(d);
        assert s.get(n).bankName.equals(ba.name);
        assert s.get(n).bankId == baId;
        assert s.get(n).date == d;
        assert s.get(n).amount == 100;

        // 2/14  +0     = 100
        d++;
        s = DataManager.getStatementsOnDate(d);

        assert s.get(n).bankName.equals(ba.name);
        assert s.get(n).bankId == baId;
        assert s.get(n).date == d;
        assert s.get(n).amount == 100;

        // 2/15  =900   = 900
        d++;
        s = DataManager.getStatementsOnDate(d);
        assert s.get(n).bankName.equals(ba.name);
        assert s.get(n).bankId == baId;
        assert s.get(n).date == d;
        assert s.get(n).amount == 900;

        // 2/16
        d++;
        s = DataManager.getStatementsOnDate(d);
        assert s.get(n).bankName.equals(ba.name);
        assert s.get(n).bankId == baId;
        assert s.get(n).date == d;
        assert s.get(n).amount == 900;

        // 2/17
        d++;
        s = DataManager.getStatementsOnDate(d);
        assert s.get(n).bankName.equals(ba.name);
        assert s.get(n).bankId == baId;
        assert s.get(n).date == d;
        assert s.get(n).amount == 900;

        // 2/18  +515   =1415
        d++;
        s = DataManager.getStatementsOnDate(d);
        assert s.get(n).bankName.equals(ba.name);
        assert s.get(n).bankId == baId;
        assert s.get(n).date == d;
        assert s.get(n).amount == 1415;

        // 2/19  -850   = 565
        d++;
        s = DataManager.getStatementsOnDate(d);
        assert s.get(n).bankName.equals(ba.name);
        assert s.get(n).bankId == baId;
        assert s.get(n).date == d;
        assert s.get(n).amount == 565;

        // 2/20  +0     = 565
        d++;
        s = DataManager.getStatementsOnDate(d);
        assert s.get(n).bankName.equals(ba.name);
        assert s.get(n).bankId == baId;
        assert s.get(n).date == d;
        assert s.get(n).amount == 565;

        // 2/21
        d++;
        s = DataManager.getStatementsOnDate(d);
        assert s.get(n).bankName.equals(ba.name);
        assert s.get(n).bankId == baId;
        assert s.get(n).date == d;
        assert s.get(n).amount == 565;

        // 2/22
        d++;
        s = DataManager.getStatementsOnDate(d);
        assert s.get(n).bankName.equals(ba.name);
        assert s.get(n).bankId == baId;
        assert s.get(n).date == d;
        assert s.get(n).amount == 565;

        // 2/23
        d++;
        s = DataManager.getStatementsOnDate(d);
        assert s.get(n).bankName.equals(ba.name);
        assert s.get(n).bankId == baId;
        assert s.get(n).date == d;
        assert s.get(n).amount == 565;

        // 2/24
        d++;
        s = DataManager.getStatementsOnDate(d);
        assert s.get(n).bankName.equals(ba.name);
        assert s.get(n).bankId == baId;
        assert s.get(n).date == d;
        assert s.get(n).amount == 565;

        // 2/25
        d++;
        s = DataManager.getStatementsOnDate(d);
        assert s.get(n).bankName.equals(ba.name);
        assert s.get(n).bankId == baId;
        assert s.get(n).date == d;
        assert s.get(n).amount == 565;

        // 2/26
        d++;
        s = DataManager.getStatementsOnDate(d);
        assert s.get(n).bankName.equals(ba.name);
        assert s.get(n).bankId == baId;
        assert s.get(n).date == d;
        assert s.get(n).amount == 565;

        // 2/27
        d++;
        s = DataManager.getStatementsOnDate(d);
        assert s.get(n).bankName.equals(ba.name);
        assert s.get(n).bankId == baId;
        assert s.get(n).date == d;
        assert s.get(n).amount == 565;

        // 2/28  +500   =1065
        d++;
        s = DataManager.getStatementsOnDate(d);
        assert s.get(n).bankName.equals(ba.name);
        assert s.get(n).bankId == baId;
        assert s.get(n).date == d;
        assert s.get(n).amount == 1065;

        // 2/29
        d++;
        s = DataManager.getStatementsOnDate(d);
        assert s.get(n).bankName.equals(ba.name);
        assert s.get(n).bankId == baId;
        assert s.get(n).date == d;
        assert s.get(n).amount == 1065;

        // 2/30
        d++;
        s = DataManager.getStatementsOnDate(d);
        assert s.get(n).bankName.equals(ba.name);
        assert s.get(n).bankId == baId;
        assert s.get(n).date == d;
        assert s.get(n).amount == 1065;
    }

    @Test
    public void testReadWrite() throws JSONException, IOException {
        DataManager.close(InstrumentationRegistry.getInstrumentation().getTargetContext());
        DataManager.initData(InstrumentationRegistry.getInstrumentation().getTargetContext());

        testStatementGetter();
        testSinglePayment();
        testRecurringPayment();
        testPaymentGetter();
        testBankAccount();
    }

}
