package com.example.financefree;

import com.example.financefree.structures.DateParser;

import org.junit.Test;

import java.util.GregorianCalendar;

public class StructuresTests {

    @Test
    public void testDate() {
        long days = DateParser.getLong(0,31,1970);
        assert days > 0;
        assert days == 30;

        GregorianCalendar gc = new GregorianCalendar(1970,0,31);
        days = DateParser.getLong(gc);
        assert days > 0;
        assert days == 30;
    }
}
