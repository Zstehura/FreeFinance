package com.example.financefree.structures;

import static org.junit.Assert.*;

import android.util.Log;

import com.example.financefree.database.entities.RecurringPayment;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.List;

@RunWith(JUnit4.class)
public class FrequencyTest {

    @Test
    public void testOccurencesBetweenNoPE() {
        RecurringPayment rp1 = Construction.makeRp("TestRp1", "", 1, 12, 0,
                DateParser.getLong(0,1,2022), DateParser.getLong(11,31,2022),
                500);
        RecurringPayment rp2 = Construction.makeRp("TestRp2", "", 1, 14, 1,
                DateParser.getLong(1,1,2022), DateParser.getLong(3,30,2022),
                500);
        RecurringPayment rp3 = Construction.makeRp("TestRp3", "", 1, 3, 2,
                DateParser.getLong(0,1,2022), DateParser.getLong(4,10,2022),
                500);
        RecurringPayment rp4 = Construction.makeRp("TestRp4", "", 1, 2, 3,
                DateParser.getLong(0,1,2022), DateParser.getLong(11,31,2022),
                500);
        RecurringPayment rp5 = Construction.makeRp("TestRp5", "", 1, 3, 4,
                DateParser.getLong(4,1,2022), DateParser.getLong(8,20,2022),
                500);
        RecurringPayment rp6 = Construction.makeRp("TestRp6", "", 1, 4, 5,
                DateParser.getLong(7,1,2022), DateParser.getLong(11,31,2022),
                500);
        RecurringPayment rp7 = Construction.makeRp("TestRp7", "", 1, 0, 6,
                DateParser.getLong(0,1,2022), DateParser.getLong(4,3,2022),
                500);

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