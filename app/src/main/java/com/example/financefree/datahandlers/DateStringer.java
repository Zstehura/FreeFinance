package com.example.financefree.datahandlers;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class DateStringer {
    public DateStringer(){}

    public static String CalToString(GregorianCalendar cal){
        if(cal == null) return "";
        return cal.get(Calendar.MONTH) + "/" + cal.get(Calendar.DAY_OF_MONTH) + "/" + cal.get(Calendar.YEAR);
    }

    public static GregorianCalendar StringToCal(String s){
        if(s.equals("")) return null;

        String[] temp = s.split("/");
        GregorianCalendar cal = new GregorianCalendar();
        cal.set(Calendar.MONTH, Integer.parseInt(temp[0]));
        cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(temp[1]));
        cal.set(Calendar.YEAR, Integer.parseInt(temp[2]));

        return cal;
    }
}
