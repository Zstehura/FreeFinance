package com.example.financefree.structures;

import static org.junit.Assert.*;

import androidx.test.core.app.ApplicationProvider;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;

@RunWith(JUnit4.class)
public class TaxYearTest {
    TaxYear taxYear;

    @Test
    public void test2018Calcs() throws TaxYear.YearNotFoundException, IOException, TaxYear.FilingNotFoundException {
        taxYear = new TaxYear(2018, ApplicationProvider.getApplicationContext());

        // married-joint
        assert getCalc(30000, TaxYear.FILING_STATUS.get(0), 600);
        assert getCalc(100000, TaxYear.FILING_STATUS.get(0), 8738.88);
        assert getCalc(150000, TaxYear.FILING_STATUS.get(0), 19598.66);
        assert getCalc(200000, TaxYear.FILING_STATUS.get(0), 30818.42);
        assert getCalc(400000, TaxYear.FILING_STATUS.get(0), 83698.10);
        assert getCalc(600000, TaxYear.FILING_STATUS.get(0), 152977.75);
        assert getCalc(1000000, TaxYear.FILING_STATUS.get(0), 300497.38);

        // married-separate
        assert getCalc(20000, TaxYear.FILING_STATUS.get(1), 800.00);
        assert getCalc(40000, TaxYear.FILING_STATUS.get(1), 3169.38);
        assert getCalc(80000, TaxYear.FILING_STATUS.get(1), 10899.16);
        assert getCalc(150000, TaxYear.FILING_STATUS.get(1), 27408.92);
        assert getCalc(200000, TaxYear.FILING_STATUS.get(1), 41848.60);
        assert getCalc(300000, TaxYear.FILING_STATUS.get(1), 76488.25);
        assert getCalc(500000, TaxYear.FILING_STATUS.get(1), 150247.88);

        // single
        assert getCalc(20000, TaxYear.FILING_STATUS.get(2), 800.00);
        assert getCalc(40000, TaxYear.FILING_STATUS.get(2), 3169.38);
        assert getCalc(80000, TaxYear.FILING_STATUS.get(2), 10899.16);
        assert getCalc(150000, TaxYear.FILING_STATUS.get(2), 27408.92);
        assert getCalc(200000, TaxYear.FILING_STATUS.get(2), 41848.60);
        assert getCalc(300000, TaxYear.FILING_STATUS.get(2), 76488.25);
        assert getCalc(1000000, TaxYear.FILING_STATUS.get(2), 331247.88);

        // head-of-house
        assert getCalc(30000, TaxYear.FILING_STATUS.get(3), 1200.00);
        assert getCalc(50000, TaxYear.FILING_STATUS.get(3), 3567.88);
        assert getCalc(80000, TaxYear.FILING_STATUS.get(3), 8187.66);
        assert getCalc(150000, TaxYear.FILING_STATUS.get(3), 24577.42);
        assert getCalc(200000, TaxYear.FILING_STATUS.get(3), 38537.10);
        assert getCalc(500000, TaxYear.FILING_STATUS.get(3), 142996.75);
        assert getCalc(750000, TaxYear.FILING_STATUS.get(3), 235136.38);
    }

    public boolean getCalc(double income, String status ,double expected) throws TaxYear.FilingNotFoundException {
        taxYear.setIncome(income);
        taxYear.setFileAs(status);
        double actual = taxYear.getTax();
        return actual == expected;
    }
}