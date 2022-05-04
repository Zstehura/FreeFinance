package com.example.financefree.structures;


import android.util.Log;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RunWith(JUnit4.class)
public class LoanCalculatorTest {

    private LoanCalculator lc;

    @Before
    public void init(){
        lc = new LoanCalculator();
    }

    @Test
    public void testPayment() {
        double p = lc.calcPayment();
        assert p == 554.74;

        lc.setPrinciple(10000);
        p = lc.calcPayment();
        assert p == 221.9;

        lc.setApr(0.15);
        p = lc.calcPayment();
        assert p == 278.31;

        lc.setNumMonths(60);
        p = lc.calcPayment();
        assert p == 237.90;

        lc.setNumMonths(DateParser.getLong("1/1/2022"), DateParser.getLong("1/1/2026"));
        lc.setApr(0.06);
        lc.setPrinciple(10645.08);
        p = lc.calcPayment();
        assert p == 250;
    }

    @Test
    public void testPrinciple() {
        double p = lc.calcPrinciple();
        assert p == 11266.53;

        lc.setPayment(337.48);
        p = lc.calcPrinciple();
        assert p == 15208.92;

        lc.setApr(0.15);
        p = lc.calcPrinciple();
        assert p == 12126.16;

        lc.setNumMonths(60);
        p = lc.calcPrinciple();
        assert p == 14185.83;

        lc.setNumMonths(DateParser.getLong("1/1/2022"), DateParser.getLong("1/1/2026"));
        lc.setApr(0.06);
        lc.setPayment(250.0);
        p = lc.calcPrinciple();
        assert p == 10645.08;
    }

    @Test
    public void testNumMonths() {
        int nm = lc.calcTermLen();
        assert nm == 116;

        lc.setPayment(337.48);
        nm = lc.calcTermLen();
        assert nm == 82;

        lc.setApr(0.15);
        nm = lc.calcTermLen();
        assert nm == 210;

        lc.setPrinciple(60000);
        nm = lc.calcTermLen();
        assert nm == -1;

        lc.setPrinciple(10000);
        nm = lc.calcTermLen();
        assert nm == 37;

        lc.setPrinciple(10645.08);
        lc.setApr(0.06);
        lc.setPayment(250.0);
        nm = lc.calcTermLen();
        assert nm == 48;
    }

    @Test
    public void testBalance() {
        List<Double> expected = new ArrayList<>(Arrays.asList(
                24510.36, 24019.45, 23527.26, 23033.79, 22539.03, 22042.99, 21545.65,
                21047.02, 20547.09, 20045.86, 19543.32, 19039.47, 18534.31, 18027.84, 17520.05,
                17010.94, 16500.50, 15988.73, 15475.63, 14961.19, 14445.41, 13928.29, 13409.82,
                12890.00, 12368.83, 11846.30, 11322.41, 10797.16, 10270.54, 9742.55, 9213.18,
                8682.43, 8150.30, 7616.78, 7081.88, 6545.58, 6007.89, 5468.80, 4928.30, 4386.39,
                3843.07, 3298.34, 2752.19, 2204.62, 1655.62, 1105.19, 553.33, 0.00
        ));
        lc.calcPayment();
        for(int i = 0; i < 48; i++){
            double d = lc.getBalanceAtMonth(i+1);
            Log.d("LoanCalcTest", "Expected: " + expected.get(i) + " | Received: " + d);
            Assert.assertEquals(expected.get(i), d,0.011);
        }
    }
}
