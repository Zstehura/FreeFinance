package com.example.financefree.structures;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.Calendar;
import java.util.GregorianCalendar;

public final class parseDate {
    private parseDate(){}

    public static long getLong(GregorianCalendar gc){
        GregorianCalendar epoch = new GregorianCalendar(1970, 0,1);
        return gc.compareTo(epoch);
    }

    public static long getLong(int month, int day, int year){
        GregorianCalendar gc = new GregorianCalendar(year, month, day);
        return getLong(gc);
    }

    public static String getString(long date){
        GregorianCalendar gc = new GregorianCalendar(1970,0,1);
        gc.add(Calendar.DAY_OF_MONTH, (int) date);
        String str = (gc.get(Calendar.MONTH)+1) + "/";
        str += gc.get(Calendar.DAY_OF_MONTH) + "/";
        str += gc.get(Calendar.YEAR);
        return str;
    }

    public static int getDay(long date){
        GregorianCalendar gc = new GregorianCalendar(1970,0,1);
        gc.add(Calendar.DAY_OF_MONTH, (int) date);
        return gc.get(Calendar.DAY_OF_MONTH);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static long genID() {
        GregorianCalendar gc = new GregorianCalendar();
        return gc.toInstant().getEpochSecond();
    }

}
