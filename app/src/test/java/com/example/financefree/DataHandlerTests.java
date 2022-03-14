package com.example.financefree;

import com.example.financefree.structures.parseDate;

import org.junit.Test;

public class DataHandlerTests {

    @Test
    public void testDate() {
        long days = parseDate.getLong(1,1,2001);
        assert days > 0;
    }
}
