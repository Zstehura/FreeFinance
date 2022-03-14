package com.example.financefree;

import com.example.financefree.structures.parseDate;

import org.junit.Test;

public class DataHandlerTests {

    @Test
    public void testDate() {
        long days = parseDate.getLong(1,1,2001);
        assert days > 0;
    }
/*
    @Test
    public void singlePaymentTest() throws JSONException, IOException {
        SinglePayment sp = new SinglePayment();

        sp.setBankId("Bank of Someplace");
        sp.setName("New Payday!");
        sp.setDate(new CustomDate("2/24/2022"));
        sp.setAmount(231.51);
        sp.setNotes("I had a great day!");
        JSONObject jActual , jTest;

        jActual = SampleDataHandler.get(SampleDataHandler.SINGLE_PAY);
        jTest = sp.toJSON();

        assertEquals(jActual.toString(), jTest.toString());
        sp.readJSON(jTest);
        jTest = sp.toJSON();
        assertEquals(jActual.toString(),jTest.toString());
    }

    @Test
    public void bankAccountTest() throws JSONException, IOException {
        BankAccount ba = new BankAccount();
        ba.setAccountId("New Bank 1");
        ba.setNotes("this is a new bank");

        ba.addStatement(new CustomDate("3/4/2019"),1234.5);
        ba.addStatement(new CustomDate("5/6/2007"), 452.76);

        JSONObject jExpect, jActual;
        jExpect = SampleDataHandler.get(SampleDataHandler.BANK_ACC);
        jActual = ba.toJSON();
        assertEquals(jExpect.toString(),jActual.toString());

        ba = new BankAccount();
        ba.readJSON(jExpect);
        jActual = ba.toJSON();
        assertEquals(jExpect.toString(),jActual.toString());
    }

    @Test
    public void paymentEditTest() throws JSONException, IOException {
        PaymentEdit pe = new PaymentEdit();
        pe.setEditDate(new CustomDate("4/16/2022"));
        pe.setAction(PaymentEdit.ACTION_MOVE);
        pe.setMoveDate(new CustomDate("4/13/2022"));

        JSONObject jsonObject = SampleDataHandler.get(SampleDataHandler.RECUR_PAY);
        JSONArray ja = jsonObject.getJSONArray(RecurringPayment.EDITS);

        JSONObject jTemp = pe.toJSONObject();
        assertEquals(ja.getJSONObject(1).toString(), jTemp.toString());
        pe = new PaymentEdit();
        pe.readJSON(jTemp);
        jTemp = pe.toJSONObject();
        assertEquals(ja.getJSONObject(1).toString(),jTemp.toString());

        pe = new PaymentEdit();
        pe.setAction(PaymentEdit.ACTION_SKIP);
        pe.setEditDate(new CustomDate("2/16/2022"));

        jTemp = pe.toJSONObject();
        assertEquals(ja.getJSONObject(0).toString(), jTemp.toString());
        pe = new PaymentEdit();
        pe.readJSON(jTemp);
        jTemp = pe.toJSONObject();
        assertEquals(ja.getJSONObject(0).toString(), jTemp.toString());

        pe = new PaymentEdit();
        pe.setAction(PaymentEdit.ACTION_CHANGE_AMNT);
        pe.setEditDate(new CustomDate("4/16/2022"));
        pe.setNewAmount(2200.5);

        jTemp = pe.toJSONObject();
        assertEquals(ja.getJSONObject(2).toString(), jTemp.toString());
        pe = new PaymentEdit();
        pe.readJSON(jTemp);
        jTemp = pe.toJSONObject();
        assertEquals(ja.getJSONObject(2).toString(), jTemp.toString());
    }

    @Test
    public void recurringPaymentTest() throws JSONException, IOException {
        RecurringPayment rp = new RecurringPayment();
        rp.setAmount(220.5);
        rp.setBankId("New Bank 1");
        rp.setNotes("This is my payment for my car loan every month on the 16th");
        rp.setFrequency(RecurringPayment.ON, 16);
        rp.setPaymentId("Car Payment");
        rp.setStart(new CustomDate("5/14/2021"));
        rp.setEnd(new CustomDate("12/31/2099"));

        PaymentEdit edit = new PaymentEdit();
        edit.setEditDate(new CustomDate("4/16/2022"));
        edit.setAction(PaymentEdit.ACTION_MOVE);
        edit.setMoveDate(new CustomDate("4/13/2022"));
        rp.addEdit(edit);

        edit = new PaymentEdit();
        edit.setAction(PaymentEdit.ACTION_SKIP);
        edit.setEditDate(new CustomDate("2/16/2022"));
        rp.addEdit(edit);

        edit = new PaymentEdit();
        edit.setAction(PaymentEdit.ACTION_CHANGE_AMNT);
        edit.setEditDate(new CustomDate("4/16/2022"));
        edit.setNewAmount(2200.5);
        rp.addEdit(edit);

        JSONObject jExpected = SampleDataHandler.get(SampleDataHandler.RECUR_PAY);
        JSONObject jActual = rp.toJSONObject();
        assertEquals(jExpected.toString(), jActual.toString());
        rp = new RecurringPayment();
        rp.readJSON(jActual);
        jActual = rp.toJSONObject();
        assertEquals(jExpected.toString(), jActual.toString());
    }

    @Test
    public void taxBracketTest() throws JSONException, IOException {
        TaxBrackets tb = new TaxBrackets();
        TaxBrackets.Bracket br;
        tb.readJSON(SampleDataHandler.get(SampleDataHandler.TAX_BRACK));
        assertEquals(2021,tb.getYear());

        // SINGLE
        br = tb.getBracket(TaxBrackets.SINGLE);
        assertEquals(0.22, br.getPct(2), 0);
        assertEquals(9951, br.getLowerLim(1), 0);
        assertEquals(0.1, br.getPct(0),0);
        assertEquals(86375, br.getUpperLim(2), 0);
        assertEquals(164926,br.getLowerLim(4),0);
        assertEquals(523600,br.getUpperLim(5),0);
        assertEquals(-1,br.getUpperLim(6),0);

        // MARRIED - JOINT
        br = tb.getBracket(TaxBrackets.MARRIED_JOINT);
        assertEquals(19900,br.getUpperLim(0),0);
        assertEquals(0.1, br.getPct(0),0);
        assertEquals(19901,br.getLowerLim(1),0);
        assertEquals(172750, br.getUpperLim(2),0);
        assertEquals(0.24, br.getPct(3), 0);
        assertEquals(329851, br.getLowerLim(4),0);
        assertEquals(628300, br.getUpperLim(5), 0);
        assertEquals(0.37,br.getPct(6),0);

        //MARRIED - SEPARATE
        br = tb.getBracket(TaxBrackets.MARRIED_SEP);
        assertEquals(0,br.getLowerLim(0),0);
        assertEquals(0.12,br.getPct(1),0);
        assertEquals(86375,br.getUpperLim(2),0);
        assertEquals(86376,br.getLowerLim(3),0);
        assertEquals(0.32,br.getPct(4),0);
        assertEquals(209426,br.getLowerLim(5),0);
        assertEquals(314151,br.getLowerLim(6),0);

        // HEAD OF HOUSEHOLD
        br = tb.getBracket(TaxBrackets.HEAD_OF_HOUSE);
        assertEquals(14200,br.getUpperLim(0),0);
        assertEquals(54200,br.getUpperLim(1),0);
        assertEquals(54201,br.getLowerLim(2),0);
        assertEquals(164900,br.getUpperLim(3),0);
        assertEquals(209400,br.getUpperLim(4),0);
        assertEquals(0.35,br.getPct(5),0);
        assertEquals(523601,br.getLowerLim(6),0);

    }
/*
    @Test
    public void dataManagerTest() throws JSONException, IOException {
        //DataManager dm = new DataManager();

       // dm.readTaxBracket(SampleDataHandler.get(SampleDataHandler.TAX_BRACK));
        //dm.setFileAs(TaxBrackets.SINGLE);

        // Add bank accounts
        BankAccount ba = new BankAccount();
        ba.setAccountId("First Bank");
        ba.setNotes("This is the first bank");
        ba.addStatement(new CustomDate("1/1/2004"), 100d);
        ba.addStatement(new CustomDate("2/3/2004"), 200d);
        ba.addStatement(new CustomDate("2/8/2004"), 400d);
       // dm.setBankAccount(ba);
        ba = new BankAccount();
        ba.setAccountId("Second Credit Union"); // default
        ba.setNotes("Second account");
        ba.addStatement(new CustomDate("1/1/2004"), 1000d);
        ba.addStatement(new CustomDate("2/12/2004"),900d);
        ba.addStatement(new CustomDate("2/27/2004"), 300d);
       // dm.setBankAccount(ba);
        //dm.setDefaultBankId("First Bank");

        // Add Recurring Payments
        RecurringPayment rp = new RecurringPayment();
        PaymentEdit pe = new PaymentEdit();
        rp.setEnd(new CustomDate("12/31/2099"));
        rp.setStart(new CustomDate("1/23/2004"));
        rp.setFrequency(RecurringPayment.EVERY, 14);
        rp.setAmount(800);
        rp.setBankId("First Bank");
        rp.setPaymentId("Paycheck");
        rp.setNotes("Paycheck from Microcenter");
        pe.setAction(PaymentEdit.ACTION_CHANGE_AMNT);
        pe.setEditDate(new CustomDate("2/6/2004"));
        pe.setNewAmount(883.82);
        rp.addEdit(pe);
      //  dm.setRecurringPayment(rp);

        rp = new RecurringPayment();
        rp.setPaymentId("Car Payment");
        rp.setAmount(-220.6);
        rp.setNotes("Monthly car payment");
        rp.setBankId("Second Credit Union");
        rp.setFrequency(RecurringPayment.ON,15);
        rp.setStart(new CustomDate("3/4/2003"));
        rp.setEnd(new CustomDate("3/4/2013"));
        pe = new PaymentEdit();
        pe.setAction(PaymentEdit.ACTION_MOVE);
        pe.setEditDate(new CustomDate("2/15/2004"));
        pe.setMoveDate(new CustomDate("2/12/2004"));
        rp.addEdit(pe);
        pe = new PaymentEdit();
        pe.setAction(PaymentEdit.ACTION_CHANGE_AMNT);
        pe.setEditDate(new CustomDate("2/15/2004"));
        pe.setNewAmount(-320.6);
        rp.addEdit(pe);
        //dm.setRecurringPayment(rp);

        // Add Single Payments
        SinglePayment sp = new SinglePayment();
        sp.setAmount(-15);
        sp.setDate(new CustomDate("2/7/2004"));
        sp.setName("Spotify");
        sp.setBankId("First Bank");
        //dm.setSinglePayment(sp);
        sp = new SinglePayment();
        sp.setBankId("First Bank");
        sp.setName("Discover credit");
        sp.setAmount(-250);
        sp.setDate(new CustomDate("2/23/2004"));
        //dm.setSinglePayment(sp);

        JSONObject jExpected = SampleDataHandler.get(SampleDataHandler.DATA_MGR);
        //JSONObject jActual = dm.toJSON();

      //  assertEquals(jExpected.toString(), jActual.toString());
        //dm = new DataManager();
       // dm.readJSON(jActual);
        //jActual = dm.toJSON();
       // assertEquals(jExpected.toString(), jActual.toString());

        // TODO: Check individual functions

        // Check bank balance calculator
        Map<String, Double> mExpected = new HashMap<>(), mActual;
        // Start with First Bank
        //mActual = dm.getAccountBalances("First Bank",2,2004);
        CustomDate cd = new CustomDate("2/1/2004");
        mExpected.put(cd.toString(), 100.0); cd.addDays(1);   // 2/1 = 100
        mExpected.put(cd.toString(), 100.0); cd.addDays(1);   // 2/2 = 100
        mExpected.put(cd.toString(), 200.0); cd.addDays(1);   // 2/3 = 200
        mExpected.put(cd.toString(), 200.0); cd.addDays(1);   // 2/4 = 200
        mExpected.put(cd.toString(), 200.0); cd.addDays(1);   // 2/5 = 200
        mExpected.put(cd.toString(), 1083.82); cd.addDays(1); // 2/6 = 1083.82
        mExpected.put(cd.toString(), 1068.82); cd.addDays(1); // 2/7 = 1063.82
        mExpected.put(cd.toString(), 400.0); cd.addDays(1);   // 2/8 = 400
        moveUntil(mExpected, cd, 20, 400);           // 2/8 > 2/20
        mExpected.put(cd.toString(), 1200.0); cd.addDays(1);  // 2/20 = 1200
        moveUntil(mExpected,cd,23,1200);             // 2/20 > 2/23
        mExpected.put(cd.toString(), 950.0); cd.addDays(1);   // 2/23 = 950
        moveUntil(mExpected,cd,1,950);               // 2/23 > 2/28

        cd = new CustomDate(2,1,2004);
        while(cd.get(CustomDate.MONTH) != 3){
  //          assertEquals(mExpected.get(cd.toString()), mActual.get(cd.toString()), 0.001);
            cd.addDays(1);
        }


    }


    public void moveUntil(Map<String,Double> map, CustomDate cal, int day, double amount) {
        while(cal.get(CustomDate.DAY) != day){
            map.put(cal.toString(),amount);
            cal.addDays(1);
        }
    }

 */
}
