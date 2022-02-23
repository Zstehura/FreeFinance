package com.example.financefree.datahandlers;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class DateStringer {
    public DateStringer(){}

    public static String CalToString(GregorianCalendar cal){
        if(cal == null) return "";
        int m = cal.get(Calendar.MONTH)+1;
        int d = cal.get(Calendar.DAY_OF_MONTH);
        int y = cal.get(Calendar.YEAR);
        return m + "/" + d + "/" + y;
    }

    public static GregorianCalendar StringToCal(String s){
        if(s.equals("")) return null;
        String[] temp = s.split("/");
        int m = Integer.parseInt(temp[0])-1;
        GregorianCalendar cal = new GregorianCalendar();
        cal.set(Calendar.MONTH, m);
        cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(temp[1]));
        cal.set(Calendar.YEAR, Integer.parseInt(temp[2]));

        return cal;
    }
}
