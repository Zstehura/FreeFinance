package com.example.financefree;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.financefree.fileClasses.BankAccount;
import com.example.financefree.fileClasses.PaymentEdit;
import com.example.financefree.fileClasses.RecurringPayment;
import com.example.financefree.fileClasses.SinglePayment;
import com.example.financefree.structures.parseDate;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class JSONTests {
    long baId, rp1Id, rp2Id;
    BankAccount ba;
    PaymentEdit pe1, pe2;
    RecurringPayment rp1, rp2;
    SinglePayment sp1, sp2;


    @Before
    public void initialize() {
        ba = new BankAccount();
        baId = parseDate.genID();
        ba.name = "First Bank";
        ba.notes = "This is a new bank with a lot of interesting things!";
        ba.statements.put(parseDate.getLong(2,2,2022), 200d);
        ba.statements.put(parseDate.getLong(2,15, 2022), 900d);

        rp1 = new RecurringPayment();
        do {
            rp1Id = parseDate.genID();
        } while(rp1Id == baId);
        rp1.name = "Paycheck";
        rp1.notes = "Example of a positive cashflow";
        rp1.frequencyType = RecurringPayment.FREQ_EVERY_NUM_DAYS;
        rp1.frequencyNum = 14;
        rp1.startDate = parseDate.getLong(1,28,2022);
        rp1.endDate = parseDate.getLong(1,31,2100);
        rp1.amount = 500d;
        rp1.bankId = baId;

        rp2 = new RecurringPayment();
        do{
            rp2Id = parseDate.genID();
        } while(rp2Id == baId || rp2Id == rp1Id);
        rp2.name = "Rent";
        rp2.notes = "Example of a negative cashflow";
        rp2.frequencyType = RecurringPayment.FREQ_ON_DATE_MONTHLY;
        rp2.frequencyNum = 20;
        rp2.amount = -850d;
        rp2.startDate = parseDate.getLong(1,1,2018);
        rp2.endDate = parseDate.getLong(5,15,2022);
        rp2.bankId = baId;

        pe1 = new PaymentEdit();
        pe1.editDate = parseDate.getLong(2,14,2022);
        pe1.newAmount = 515d;
        pe1.newDate = parseDate.getLong(2,18,2022);
        rp1.edits.put(pe1.editDate, pe1);

        pe2 = new PaymentEdit();
        pe2.editDate = 1;

    }

}
