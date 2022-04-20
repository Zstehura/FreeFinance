package com.example.financefree.structures;

import static org.junit.Assert.*;

import androidx.test.core.app.ApplicationProvider;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;

@RunWith(JUnit4.class)
public class TaxYearTest {

    @Test
    public void testBasicCalcs() throws TaxYear.YearNotFoundException, IOException, TaxYear.FilingNotFoundException {
        TaxYear taxYear1 = new TaxYear(2018, ApplicationProvider.getApplicationContext());

        // married-joint, should be
        assert taxYear1.getTaxOn(TaxYear.FILING_STATUS.get(0), 100000) == 0;


    }

    @Test
    public void setChildren() {
    }

    @Test
    public void getChildren() {
    }

    @Test
    public void getYear() {
    }
}