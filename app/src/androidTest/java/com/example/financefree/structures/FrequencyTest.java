package com.example.financefree.structures;

import static org.junit.Assert.*;

import android.util.Log;

import com.example.financefree.database.entities.PaymentEdit;
import com.example.financefree.database.entities.RecurringPayment;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RunWith(JUnit4.class)
public class FrequencyTest {
    RecurringPayment rp1, rp2, rp3, rp4, rp5, rp6, rp7;
    PaymentEdit pe11, pe21, pe31, pe41, pe51, pe61, pe71;
    PaymentEdit pe12, pe22, pe32, pe42, pe52, pe62, pe72;
    PaymentEdit pe13, pe23, pe33, pe43, pe53, pe63, pe73;
    PaymentEdit pe14, pe24, pe34, pe44, pe54, pe64, pe74;
    List<PaymentEdit> pel1, pel2, pel3, pel4, pel5, pel6, pel7;
    
    @Before
    public void init(){
        rp1 = Construction.makeRp("TestRp1", "", 1, 12, 0,
                DateParser.getLong(0,1,2022), DateParser.getLong(11,31,2022),
                500, 1);
        rp2 = Construction.makeRp("TestRp2", "", 1, 14, 1,
                DateParser.getLong(1,1,2022), DateParser.getLong(3,30,2022),
                500, 2);
        rp3 = Construction.makeRp("TestRp3", "", 1, 3, 2,
                DateParser.getLong(0,1,2022), DateParser.getLong(4,10,2022),
                500, 3);
        rp4 = Construction.makeRp("TestRp4", "", 1, 2, 3,
                DateParser.getLong(0,1,2022), DateParser.getLong(11,31,2022),
                500, 4);
        rp5 = Construction.makeRp("TestRp5", "", 1, 3, 4,
                DateParser.getLong(4,1,2022), DateParser.getLong(8,20,2022),
                500, 5);
        rp6 = Construction.makeRp("TestRp6", "", 1, 4, 5,
                DateParser.getLong(7,1,2022), DateParser.getLong(11,31,2022),
                500, 6);
        rp7 = Construction.makeRp("TestRp7", "", 1, 0, 6,
                DateParser.getLong(0,1,2022), DateParser.getLong(4,3,2022),
                500, 7);

        pe11 = Construction.makeEdit(rp1, DateParser.getLong(1,12,2022));
        pe11.skip = true;
        pe12 = Construction.makeEdit(rp1, DateParser.getLong(2,12,2022));
        pe12.new_date = DateParser.getLong(2,14,2022);
        pe13 = Construction.makeEdit(rp1, DateParser.getLong(3,12,2022));
        pe13.new_amount = 600;
        pe14 = Construction.makeEdit(rp1, DateParser.getLong(4,12,2022));
        pe14.new_bank_id = 2;
        pel1 = new ArrayList<>(Arrays.asList(pe11, pe12, pe13, pe14));

        pe21 = Construction.makeEdit(rp2, DateParser.getLong(1,1,2022));
        pe21.skip = true;
        pe22 = Construction.makeEdit(rp2, DateParser.getLong(2,15,2022));
        pe22.new_date = DateParser.getLong(2,14,2022);
        pe23 = Construction.makeEdit(rp2, DateParser.getLong(3,12,2022));
        pe23.new_amount = 600;
        pe24 = Construction.makeEdit(rp2, DateParser.getLong(3,26,2022));
        pe24.new_bank_id = 2;
        pel2 = new ArrayList<>(Arrays.asList(pe21, pe22, pe23, pe24));

        pe31 = Construction.makeEdit(rp3, DateParser.getLong(0,1,2022));
        pe31.skip = true;
        pe32 = Construction.makeEdit(rp3, DateParser.getLong(1,12,2022));
        pe32.new_date = DateParser.getLong(1,14,2022);
        pe33 = Construction.makeEdit(rp3, DateParser.getLong(2,5,2022));
        pe33.new_amount = 600;
        pe34 = Construction.makeEdit(rp3, DateParser.getLong(3,16,2022));
        pe34.new_bank_id = 2;
        pel3 = new ArrayList<>(Arrays.asList(pe31, pe32, pe33, pe34));

        pe41 = Construction.makeEdit(rp4, DateParser.getLong(0,1,2022));
        pe41.skip = true;
        pe42 = Construction.makeEdit(rp4, DateParser.getLong(2,1,2022));
        pe42.new_date = DateParser.getLong(2,4,2022);
        pe43 = Construction.makeEdit(rp4, DateParser.getLong(4,1,2022));
        pe43.new_amount = 600;
        pe44 = Construction.makeEdit(rp4, DateParser.getLong(6,1,2022));
        pe44.new_bank_id = 2;
        pel4 = new ArrayList<>(Arrays.asList(pe41, pe42, pe43, pe44));

        pe51 = Construction.makeEdit(rp5, DateParser.getLong(4,3,2022));
        pe51.skip = true;
        pe52 = Construction.makeEdit(rp5, DateParser.getLong(4,17,2022));
        pe52.new_date = DateParser.getLong(4,16,2022);
        pe53 = Construction.makeEdit(rp5, DateParser.getLong(5,21,2022));
        pe53.new_amount = 600;
        pe54 = Construction.makeEdit(rp5, DateParser.getLong(6,19,2022));
        pe54.new_bank_id = 2;
        pel5 = new ArrayList<>(Arrays.asList(pe51, pe52, pe53, pe54));
        
        pe61 = Construction.makeEdit(rp6, DateParser.getLong(7,24,2022));
        pe61.skip = true;
        pe62 = Construction.makeEdit(rp6, DateParser.getLong(8,14,2022));
        pe62.new_date = DateParser.getLong(8,16,2022);
        pe63 = Construction.makeEdit(rp6, DateParser.getLong(9,12,2022));
        pe63.new_amount = 600;
        pe64 = Construction.makeEdit(rp6, DateParser.getLong(11,14,2022));
        pe64.new_bank_id = 2;
        pel6 = new ArrayList<>(Arrays.asList(pe61, pe62, pe63, pe64));

        pe71 = Construction.makeEdit(rp7, DateParser.getLong(0,31,2022));
        pe71.skip = true;
        pe72 = Construction.makeEdit(rp7, DateParser.getLong(1,28,2022));
        pe72.new_date = DateParser.getLong(1,27,2022);
        pe73 = Construction.makeEdit(rp7, DateParser.getLong(2,31,2022));
        pe73.new_amount = 600;
        pe74 = Construction.makeEdit(rp7, DateParser.getLong(3,30,2022));
        pe74.new_bank_id = 2;
        pel7 = new ArrayList<>(Arrays.asList(pe71, pe72, pe73, pe74));
    }

    @Test
    public void testPaymentsBetween() {

        // edits
        // skip 2/12/2022
        // 3/12/2022 -> 3/14/2022
        // amount=600 4/12/2022
        // bankId=2 5/12/2022
        List<Payment> l1 = Frequency.paymentsBetween(pel1, rp1, DateParser.getLong(11,31,2021), DateParser.getLong(0,1,2023));
        assert l1.get(0).date == DateParser.getLong(0,12,2022);
        assert l1.get(1).date == DateParser.getLong(2,14,2022);
        assert l1.get(2).date == DateParser.getLong(3,12,2022);
        assert l1.get(3).date == DateParser.getLong(4,12,2022);
        assert l1.get(4).date == DateParser.getLong(5,12,2022);
        assert l1.get(5).date == DateParser.getLong(6,12,2022);
        assert l1.get(6).date == DateParser.getLong(7,12,2022);
        assert l1.get(7).date == DateParser.getLong(8,12,2022);
        assert l1.get(8).date == DateParser.getLong(9,12,2022);
        assert l1.get(9).date == DateParser.getLong(10,12,2022);
        assert l1.get(10).date == DateParser.getLong(11,12,2022);
        assert l1.size() == 11;
        for(int i = 0; i < l1.size(); i++){
            assert i == 2 || l1.get(i).amount == 500;
            assert i == 3 || l1.get(i).bankId == 1;
        }
        assert l1.get(2).amount == 600;
        assert l1.get(3).bankId == 2;

        // edits
        // skip 2/1/2022
        // 3/15/2022 -> 3/14/2022
        // amount=600 4/12/2022
        // bankId=2 4/26/2022
        List<Payment> l2 = Frequency.paymentsBetween(pel2, rp2,DateParser.getLong(11,31,2021), DateParser.getLong(0,1,2023));
        assert l2.get(0).date == DateParser.getLong(1,15,2022);
        assert l2.get(1).date == DateParser.getLong(2,1,2022);
        assert l2.get(2).date == DateParser.getLong(2,14,2022);
        assert l2.get(3).date == DateParser.getLong(2,29,2022);
        assert l2.get(4).date == DateParser.getLong(3,12,2022);
        assert l2.get(5).date == DateParser.getLong(3,26,2022);
        assert l2.size() == 6;
        for(int i = 0; i < l2.size(); i++){
            assert i == 4 || l2.get(i).amount == 500;
            assert i == 5 || l2.get(i).bankId == 1;
        }
        assert l2.get(4).amount == 600;
        assert l2.get(5).bankId == 2;

        // edits
        // skip 1/1/2022
        // 2/12/2022 -> 2/14/2022
        // amount=600 3/5/2022
        // bankId=2 4/16/2022
        List<Payment> l3 = Frequency.paymentsBetween(pel3, rp3,DateParser.getLong(11,31,2021), DateParser.getLong(0,1,2023));
        assert l3.get(0).date == DateParser.getLong(0,22,2022);
        assert l3.get(1).date == DateParser.getLong(1,14,2022);
        assert l3.get(2).date == DateParser.getLong(2,5,2022);
        assert l3.get(3).date == DateParser.getLong(2,26,2022);
        assert l3.get(4).date == DateParser.getLong(3,16,2022);
        assert l3.get(5).date == DateParser.getLong(4,7,2022);
        assert l3.size() == 6;
        for(int i = 0; i < l3.size(); i++){
            assert i == 2 || l3.get(i).amount == 500;
            assert i == 4 || l3.get(i).bankId == 1;
        }
        assert l3.get(2).amount == 600;
        assert l3.get(4).bankId == 2;

        // edits
        // skip 1/1/2022
        // 3/1/2022 -> 3/4/2022
        // amount=600 5/1/2022
        // bankId=2 7/1/2022
        List<Payment> l4 = Frequency.paymentsBetween(pel4, rp4,DateParser.getLong(11,31,2021), DateParser.getLong(0,1,2023));
        assert l4.get(0).date == DateParser.getLong(2,4,2022);
        assert l4.get(1).date == DateParser.getLong(4,1,2022);
        assert l4.get(2).date == DateParser.getLong(6,1,2022);
        assert l4.get(3).date == DateParser.getLong(8,1,2022);
        assert l4.get(4).date == DateParser.getLong(10,1,2022);
        assert l4.size() == 5;
        for(int i = 0; i < l4.size(); i++){
            assert i == 1 || l4.get(i).amount == 500;
            assert i == 2 || l4.get(i).bankId == 1;
        }
        assert l4.get(1).amount == 600;
        assert l4.get(2).bankId == 2;

        // edits
        // skip 5/3/2022
        // 5/17/2022 -> 5/16/2022
        // amount=600 6/21/2022
        // bankId=2 7/19/2022
        List<Payment> l5 = Frequency.paymentsBetween(pel5, rp5,DateParser.getLong(11,31,2021), DateParser.getLong(0,1,2023));
        assert l5.get(0).date == DateParser.getLong(4,16,2022);
        assert l5.get(1).date == DateParser.getLong(5,7,2022);
        assert l5.get(2).date == DateParser.getLong(5,21,2022);
        assert l5.get(3).date == DateParser.getLong(6,5,2022);
        assert l5.get(4).date == DateParser.getLong(6,19,2022);
        assert l5.get(5).date == DateParser.getLong(7,2,2022);
        assert l5.get(6).date == DateParser.getLong(7,16,2022);
        assert l5.get(7).date == DateParser.getLong(8,6,2022);
        assert l5.get(8).date == DateParser.getLong(8,20,2022);
        assert l5.size() == 9;
        for(int i = 0; i < l5.size(); i++){
            assert i == 2 || l5.get(i).amount == 500;
            assert i == 4 || l5.get(i).bankId == 1;
        }
        assert l5.get(2).amount == 600;
        assert l5.get(4).bankId == 2;

        // edits
        // skip 8/24/2022
        // 9/14/2022 -> 9/16/2022
        // amount=600 10/12/2022
        // bankId=2 12/14/2022
        List<Payment> l6 = Frequency.paymentsBetween(pel6, rp6,DateParser.getLong(11,31,2021), DateParser.getLong(0,1,2023));
        assert l6.get(0).date == DateParser.getLong(7,10,2022);
        assert l6.get(1).date == DateParser.getLong(8,16,2022);
        assert l6.get(2).date == DateParser.getLong(8,28,2022);
        assert l6.get(3).date == DateParser.getLong(9,12,2022);
        assert l6.get(4).date == DateParser.getLong(9,26,2022);
        assert l6.get(5).date == DateParser.getLong(10,9,2022);
        assert l6.get(6).date == DateParser.getLong(10,23,2022);
        assert l6.get(7).date == DateParser.getLong(11,14,2022);
        assert l6.get(8).date == DateParser.getLong(11,28,2022);
        assert l6.size() == 9;
        for(int i = 0; i < l6.size(); i++){
            assert i == 3 || l6.get(i).amount == 500;
            assert i == 7 || l6.get(i).bankId == 1;
        }
        assert l6.get(3).amount == 600;
        assert l6.get(7).bankId == 2;

        // edits
        // skip 1/31/2022
        // 2/28/2022 -> 2/27/2022
        // amount=600 3/31/2022
        // bankId=2 4/30/2022
        List<Payment> l7 = Frequency.paymentsBetween(pel7, rp7,DateParser.getLong(11,31,2021), DateParser.getLong(0,1,2023));
        assert l7.get(0).date == DateParser.getLong(1,27,2022);
        assert l7.get(1).date == DateParser.getLong(2,31,2022);
        assert l7.get(2).date == DateParser.getLong(3,30,2022);
        assert l7.size() == 3;
        for(int i = 0; i < l7.size(); i++){
            assert i == 1 || l7.get(i).amount == 500;
            assert i == 2 || l7.get(i).bankId == 1;
        }
        assert l7.get(1).amount == 600;
        assert l7.get(2).bankId == 2;
    }

    @Test
    public void testOccurencesBetweenWithPE(){
        // edits
        // skip 2/12/2022
        // 3/12/2022 -> 3/14/2022
        // amount=600 4/12/2022
        // bankId=2 5/12/2022
        List<Long> l1 = Frequency.occurencesBetween(pel1, rp1, DateParser.getLong(11,31,2021), DateParser.getLong(0,1,2023));
        assert l1.get(0) == DateParser.getLong(0,12,2022);
        assert l1.get(1) == DateParser.getLong(2,14,2022);
        assert l1.get(2) == DateParser.getLong(3,12,2022);
        assert l1.get(3) == DateParser.getLong(4,12,2022);
        assert l1.get(4) == DateParser.getLong(5,12,2022);
        assert l1.get(5) == DateParser.getLong(6,12,2022);
        assert l1.get(6) == DateParser.getLong(7,12,2022);
        assert l1.get(7) == DateParser.getLong(8,12,2022);
        assert l1.get(8) == DateParser.getLong(9,12,2022);
        assert l1.get(9) == DateParser.getLong(10,12,2022);
        assert l1.get(10) == DateParser.getLong(11,12,2022);
        assert l1.size() == 11;

        // edits
        // skip 2/1/2022
        // 3/15/2022 -> 3/14/2022
        // amount=600 4/12/2022
        // bankId=2 4/26/2022
        List<Long> l2 = Frequency.occurencesBetween(pel2, rp2,DateParser.getLong(11,31,2021), DateParser.getLong(0,1,2023));
        assert l2.get(0) == DateParser.getLong(1,15,2022);
        assert l2.get(1) == DateParser.getLong(2,1,2022);
        assert l2.get(2) == DateParser.getLong(2,14,2022);
        assert l2.get(3) == DateParser.getLong(2,29,2022);
        assert l2.get(4) == DateParser.getLong(3,12,2022);
        assert l2.get(5) == DateParser.getLong(3,26,2022);
        assert l2.size() == 6;

        // edits
        // skip 1/1/2022
        // 2/12/2022 -> 2/14/2022
        // amount=600 3/5/2022
        // bankId=2 4/16/2022
        List<Long> l3 = Frequency.occurencesBetween(pel3, rp3,DateParser.getLong(11,31,2021), DateParser.getLong(0,1,2023));
        assert l3.get(0) == DateParser.getLong(0,22,2022);
        assert l3.get(1) == DateParser.getLong(1,14,2022);
        assert l3.get(2) == DateParser.getLong(2,5,2022);
        assert l3.get(3) == DateParser.getLong(2,26,2022);
        assert l3.get(4) == DateParser.getLong(3,16,2022);
        assert l3.get(5) == DateParser.getLong(4,7,2022);
        assert l3.size() == 6;

        // edits
        // skip 1/1/2022
        // 3/1/2022 -> 3/4/2022
        // amount=600 5/1/2022
        // bankId=2 7/1/2022
        List<Long> l4 = Frequency.occurencesBetween(pel4, rp4,DateParser.getLong(11,31,2021), DateParser.getLong(0,1,2023));
        assert l4.get(0) == DateParser.getLong(2,4,2022);
        assert l4.get(1) == DateParser.getLong(4,1,2022);
        assert l4.get(2) == DateParser.getLong(6,1,2022);
        assert l4.get(3) == DateParser.getLong(8,1,2022);
        assert l4.get(4) == DateParser.getLong(10,1,2022);
        assert l4.size() == 5;

        // edits
        // skip 5/3/2022
        // 5/17/2022 -> 5/16/2022
        // amount=600 6/21/2022
        // bankId=2 7/19/2022
        List<Long> l5 = Frequency.occurencesBetween(pel5, rp5,DateParser.getLong(11,31,2021), DateParser.getLong(0,1,2023));
        assert l5.get(0) == DateParser.getLong(4,16,2022);
        assert l5.get(1) == DateParser.getLong(5,7,2022);
        assert l5.get(2) == DateParser.getLong(5,21,2022);
        assert l5.get(3) == DateParser.getLong(6,5,2022);
        assert l5.get(4) == DateParser.getLong(6,19,2022);
        assert l5.get(5) == DateParser.getLong(7,2,2022);
        assert l5.get(6) == DateParser.getLong(7,16,2022);
        assert l5.get(7) == DateParser.getLong(8,6,2022);
        assert l5.get(8) == DateParser.getLong(8,20,2022);
        assert l5.size() == 9;

        // edits
        // skip 8/24/2022
        // 9/14/2022 -> 9/16/2022
        // amount=600 10/12/2022
        // bankId=2 11/14/2022
        List<Long> l6 = Frequency.occurencesBetween(pel6, rp6,DateParser.getLong(11,31,2021), DateParser.getLong(0,1,2023));
        assert l6.get(0) == DateParser.getLong(7,10,2022);
        assert l6.get(1) == DateParser.getLong(8,16,2022);
        assert l6.get(2) == DateParser.getLong(8,28,2022);
        assert l6.get(3) == DateParser.getLong(9,12,2022);
        assert l6.get(4) == DateParser.getLong(9,26,2022);
        assert l6.get(5) == DateParser.getLong(10,9,2022);
        assert l6.get(6) == DateParser.getLong(10,23,2022);
        assert l6.get(7) == DateParser.getLong(11,14,2022);
        assert l6.get(8) == DateParser.getLong(11,28,2022);
        assert l6.size() == 9;

        // edits
        // skip 1/31/2022
        // 2/28/2022 -> 2/27/2022
        // amount=600 3/31/2022
        // bankId=2 4/30/2022
        List<Long> l7 = Frequency.occurencesBetween(pel7, rp7,DateParser.getLong(11,31,2021), DateParser.getLong(0,1,2023));
        assert l7.get(0) == DateParser.getLong(1,27,2022);
        assert l7.get(1) == DateParser.getLong(2,31,2022);
        assert l7.get(2) == DateParser.getLong(3,30,2022);
        assert l7.size() == 3;
    }
    
    @Test
    public void testOccurencesBetweenNoPE() {
        // 0, "On specific date every month"
        // starts 1/1/2022
        // ends 12/31/2022
        // every month on the 12th
        List<Long> l1 = Frequency.occurencesBetween(rp1,DateParser.getLong(11,31,2021), DateParser.getLong(0,1,2023));
        assert l1.get(0) == DateParser.getLong(0,12,2022);
        assert l1.get(1) == DateParser.getLong(1,12,2022);
        assert l1.get(2) == DateParser.getLong(2,12,2022);
        assert l1.get(3) == DateParser.getLong(3,12,2022);
        assert l1.get(4) == DateParser.getLong(4,12,2022);
        assert l1.get(5) == DateParser.getLong(5,12,2022);
        assert l1.get(6) == DateParser.getLong(6,12,2022);
        assert l1.get(7) == DateParser.getLong(7,12,2022);
        assert l1.get(8) == DateParser.getLong(8,12,2022);
        assert l1.get(9) == DateParser.getLong(9,12,2022);
        assert l1.get(10) == DateParser.getLong(10,12,2022);
        assert l1.get(11) == DateParser.getLong(11,12,2022);
        assert l1.size() == 12;

        // 1, "Every number of Days"
        // starts 2/1/2022
        // ends 4/30/2022
        // every 14 days
        List<Long> l2 = Frequency.occurencesBetween(rp2,DateParser.getLong(11,31,2021), DateParser.getLong(0,1,2023));
        assert l2.get(0) == DateParser.getLong(1,1,2022);
        assert l2.get(1) == DateParser.getLong(1,15,2022);
        assert l2.get(2) == DateParser.getLong(2,1,2022);
        assert l2.get(3) == DateParser.getLong(2,15,2022);
        assert l2.get(4) == DateParser.getLong(2,29,2022);
        assert l2.get(5) == DateParser.getLong(3,12,2022);
        assert l2.get(6) == DateParser.getLong(3,26,2022);
        assert l2.size() == 7;

        // 2, "Every number of Weeks"
        // starts 1/1/2022
        // ends 5/10/2022
        // every 3 weeks
        List<Long> l3 = Frequency.occurencesBetween(rp3,DateParser.getLong(11,31,2021), DateParser.getLong(0,1,2023));
        assert l3.get(0) == DateParser.getLong(0,1,2022);
        assert l3.get(1) == DateParser.getLong(0,22,2022);
        assert l3.get(2) == DateParser.getLong(1,12,2022);
        assert l3.get(3) == DateParser.getLong(2,5,2022);
        assert l3.get(4) == DateParser.getLong(2,26,2022);
        assert l3.get(5) == DateParser.getLong(3,16,2022);
        assert l3.get(6) == DateParser.getLong(4,7,2022);
        assert l3.size() == 7;

        // 3, "Every number of Months"
        // starts 1/1/2022
        // ends 12/31/2022
        // every 2 months
        List<Long> l4 = Frequency.occurencesBetween(rp4,DateParser.getLong(11,31,2021), DateParser.getLong(0,1,2023));
        assert l4.get(0) == DateParser.getLong(0,1,2022);
        assert l4.get(1) == DateParser.getLong(2,1,2022);
        assert l4.get(2) == DateParser.getLong(4,1,2022);
        assert l4.get(3) == DateParser.getLong(6,1,2022);
        assert l4.get(4) == DateParser.getLong(8,1,2022);
        assert l4.get(5) == DateParser.getLong(10,1,2022);
        assert l4.size() == 6;

        // 4, "Every month on the 1st and 3rd"
        // starts 5/1/2022
        // ends 9/20/2022
        // on the 1st and 3rd Tuesday every month
        List<Long> l5 = Frequency.occurencesBetween(rp5,DateParser.getLong(11,31,2021), DateParser.getLong(0,1,2023));
        assert l5.get(0) == DateParser.getLong(4,3,2022);
        assert l5.get(1) == DateParser.getLong(4,17,2022);
        assert l5.get(2) == DateParser.getLong(5,7,2022);
        assert l5.get(3) == DateParser.getLong(5,21,2022);
        assert l5.get(4) == DateParser.getLong(6,5,2022);
        assert l5.get(5) == DateParser.getLong(6,19,2022);
        assert l5.get(6) == DateParser.getLong(7,2,2022);
        assert l5.get(7) == DateParser.getLong(7,16,2022);
        assert l5.get(8) == DateParser.getLong(8,6,2022);
        assert l5.get(9) == DateParser.getLong(8,20,2022);
        assert l5.size() == 10;

        // 5, "Every month on the 2nd and 4th"
        // starts 8/1/2022
        // ends 12/31/2022
        // on the 2nd and 3rd Wednesday of every month
        List<Long> l6 = Frequency.occurencesBetween(rp6,DateParser.getLong(11,31,2021), DateParser.getLong(0,1,2023));
        assert l6.get(0) == DateParser.getLong(7,10,2022);
        assert l6.get(1) == DateParser.getLong(7,24,2022);
        assert l6.get(2) == DateParser.getLong(8,14,2022);
        assert l6.get(3) == DateParser.getLong(8,28,2022);
        assert l6.get(4) == DateParser.getLong(9,12,2022);
        assert l6.get(5) == DateParser.getLong(9,26,2022);
        assert l6.get(6) == DateParser.getLong(10,9,2022);
        assert l6.get(7) == DateParser.getLong(10,23,2022);
        assert l6.get(8) == DateParser.getLong(11,14,2022);
        assert l6.get(9) == DateParser.getLong(11,28,2022);
        assert l6.size() == 10;

        // 6, "On the last day of every month"
        // starts 1/1/2022
        // ends 5/3/2022
        // last day of every month
        List<Long> l7 = Frequency.occurencesBetween(rp7,DateParser.getLong(11,31,2021), DateParser.getLong(0,1,2023));
        assert l7.get(0) == DateParser.getLong(0,31,2022);
        assert l7.get(1) == DateParser.getLong(1,28,2022);
        assert l7.get(2) == DateParser.getLong(2,31,2022);
        assert l7.get(3) == DateParser.getLong(3,30,2022);
        assert l7.size() == 4;
    }

    @Test
    public void testOccursOn() {

    }
}