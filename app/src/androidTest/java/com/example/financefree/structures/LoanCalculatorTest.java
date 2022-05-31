package com.example.financefree.structures;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class LoanCalculatorTest {

    @Test
    public void testPrinciple() {
        LoanCalculator lc = new LoanCalculator();
        lc.setApr(0.03125);
        lc.setNumMonths(48);
        lc.setPayment(250);
        double ret = lc.calcPrinciple();
        assert ret == 11266.53;

        lc.setApr(0.06);
        ret = lc.calcPrinciple();
        assert ret == 10645.08;

        lc.setNumMonths(60);
        ret = lc.calcPrinciple();
        assert ret == 12931.39;

        lc.setPayment(337.48);
        ret = lc.calcPrinciple();
        assert ret == 17456.34;
    }

    @Test
    public void testNumMonths() {
        LoanCalculator lc = new LoanCalculator();
        lc.setPrinciple(10645.08);
        lc.setApr(0.03125);
        lc.setPayment(337.48);
        int ret = lc.calcTermLen();
        assert ret == 33;

        lc.setApr(0.06);
        ret = lc.calcTermLen();
        assert ret == 34;

        lc.setPrinciple(12000);
        ret = lc.calcTermLen();
        assert ret == 39;

        lc.setPayment(250);
        ret = lc.calcTermLen();
        assert ret == 55;
    }

    @Test
    public void testPayment() {
        LoanCalculator lc = new LoanCalculator();
        lc.setPrinciple(10645.08);
        lc.setApr(0.03125);
        lc.setNumMonths(48);
        double ret = lc.calcPayment();
        assert ret == 236.21;

        lc.setApr(0.06);
        ret = lc.calcPayment();
        assert ret == 250;

        lc.setPrinciple(12000);
        ret = lc.calcPayment();
        assert ret == 281.82;

        lc.setNumMonths(DateParser.getLong("1/2/2020"), DateParser.getLong("1/2/2025"));
        ret = lc.calcPayment();
        assert ret == 231.99;
    }

    @Test
    public void testBalance() {
        LoanCalculator lc = new LoanCalculator(12000, 60, 0.06, 250);
        lc.calcPayment();   // payment is actually like 231.99

        Assert.assertEquals(11828.01, lc.getBalanceAtMonth(1), 0.02);
        Assert.assertEquals(11655.16, lc.getBalanceAtMonth(2), 0.02);
        Assert.assertEquals(11481.45, lc.getBalanceAtMonth(3), 0.02);
        Assert.assertEquals(11306.87, lc.getBalanceAtMonth(4), 0.02);
        Assert.assertEquals(11131.41, lc.getBalanceAtMonth(5), 0.02);
        Assert.assertEquals(10955.08, lc.getBalanceAtMonth(6), 0.02);
        Assert.assertEquals(10777.87, lc.getBalanceAtMonth(7), 0.02);
        Assert.assertEquals(10599.77, lc.getBalanceAtMonth(8), 0.02);
        Assert.assertEquals(10420.78, lc.getBalanceAtMonth(9), 0.02);
        Assert.assertEquals(10240.89, lc.getBalanceAtMonth(10), 0.02);
        Assert.assertEquals(10060.10, lc.getBalanceAtMonth(11), 0.02);
        Assert.assertEquals(9878.41, lc.getBalanceAtMonth(12), 0.02);
        Assert.assertEquals(9695.81, lc.getBalanceAtMonth(13), 0.02);
        Assert.assertEquals(9512.30, lc.getBalanceAtMonth(14), 0.02);
        Assert.assertEquals(9327.87, lc.getBalanceAtMonth(15), 0.02);
        Assert.assertEquals(9142.52, lc.getBalanceAtMonth(16), 0.02);
        Assert.assertEquals(8956.24, lc.getBalanceAtMonth(17), 0.02);
        Assert.assertEquals(8769.03, lc.getBalanceAtMonth(18), 0.02);
        Assert.assertEquals(8580.89, lc.getBalanceAtMonth(19), 0.02);
        Assert.assertEquals(8391.80, lc.getBalanceAtMonth(20), 0.02);
        Assert.assertEquals(8201.77, lc.getBalanceAtMonth(21), 0.02);
        Assert.assertEquals(8010.79, lc.getBalanceAtMonth(22), 0.02);
        Assert.assertEquals(7818.85, lc.getBalanceAtMonth(23), 0.02);
        Assert.assertEquals(7625.95, lc.getBalanceAtMonth(24), 0.02);
        Assert.assertEquals(7432.09, lc.getBalanceAtMonth(25), 0.02);
        Assert.assertEquals(7237.26, lc.getBalanceAtMonth(26), 0.02);
        Assert.assertEquals(7041.46, lc.getBalanceAtMonth(27), 0.02);
        Assert.assertEquals(6844.68, lc.getBalanceAtMonth(28), 0.02);
        Assert.assertEquals(6646.91, lc.getBalanceAtMonth(29), 0.02);
        Assert.assertEquals(6448.15, lc.getBalanceAtMonth(30), 0.02);
        Assert.assertEquals(6248.40, lc.getBalanceAtMonth(31), 0.02);
        Assert.assertEquals(6047.65, lc.getBalanceAtMonth(32), 0.02);
        Assert.assertEquals(5845.90, lc.getBalanceAtMonth(33), 0.02);
        Assert.assertEquals(5643.14, lc.getBalanceAtMonth(34), 0.02);
        Assert.assertEquals(5439.37, lc.getBalanceAtMonth(35), 0.02);
        Assert.assertEquals(5234.58, lc.getBalanceAtMonth(36), 0.02);
        Assert.assertEquals(5028.76, lc.getBalanceAtMonth(37), 0.02);
        Assert.assertEquals(4821.91, lc.getBalanceAtMonth(38), 0.02);
        Assert.assertEquals(4614.03, lc.getBalanceAtMonth(39), 0.02);
        Assert.assertEquals(4405.11, lc.getBalanceAtMonth(40), 0.02);
        Assert.assertEquals(4195.15, lc.getBalanceAtMonth(41), 0.02);
        Assert.assertEquals(3984.14, lc.getBalanceAtMonth(42), 0.02);
        Assert.assertEquals(3772.07, lc.getBalanceAtMonth(43), 0.02);
        Assert.assertEquals(3558.94, lc.getBalanceAtMonth(44), 0.02);
        Assert.assertEquals(3344.74, lc.getBalanceAtMonth(45), 0.02);
        Assert.assertEquals(3129.47, lc.getBalanceAtMonth(46), 0.02);
        Assert.assertEquals(2913.13, lc.getBalanceAtMonth(47), 0.02);
        Assert.assertEquals(2695.71, lc.getBalanceAtMonth(48), 0.02);
        Assert.assertEquals(2477.20, lc.getBalanceAtMonth(49), 0.02);
        Assert.assertEquals(2257.60, lc.getBalanceAtMonth(50), 0.02);
        Assert.assertEquals(2036.90, lc.getBalanceAtMonth(51), 0.02);
        Assert.assertEquals(1815.09, lc.getBalanceAtMonth(52), 0.02);
        Assert.assertEquals(1592.18, lc.getBalanceAtMonth(53), 0.02);
        Assert.assertEquals(1368.15, lc.getBalanceAtMonth(54), 0.02);
        Assert.assertEquals(1143.00, lc.getBalanceAtMonth(55), 0.02);
        Assert.assertEquals(916.73, lc.getBalanceAtMonth(56), 0.02);
        Assert.assertEquals(689.32, lc.getBalanceAtMonth(57), 0.02);
        Assert.assertEquals(460.78, lc.getBalanceAtMonth(58), 0.02);
        Assert.assertEquals(231.09, lc.getBalanceAtMonth(59), 0.02);
        Assert.assertEquals(0.00, lc.getBalanceAtMonth(60), 0.02);
    }

}
