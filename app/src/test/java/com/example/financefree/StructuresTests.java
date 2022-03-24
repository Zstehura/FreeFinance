package com.example.financefree;

import com.example.financefree.structures.parseDate;

import org.junit.Test;

import java.util.GregorianCalendar;

public class StructuresTests {

    @Test
    public void testDate() {
        long days = parseDate.getLong(0,31,1970);
        assert days > 0;
        assert days == 30;

        GregorianCalendar gc = new GregorianCalendar(1970,0,31);
        days = parseDate.getLong(gc);
        assert days > 0;
        assert days == 30;
    }
}
