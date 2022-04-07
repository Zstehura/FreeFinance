package com.example.financefree.structures;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.financefree.database.entities.PaymentEdit;
import com.example.financefree.database.entities.RecurringPayment;

import java.util.Calendar;
import java.util.GregorianCalendar;

public final class DateParser {
    private DateParser(){}

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static long getLong(GregorianCalendar gc){
        return gc.toInstant().getEpochSecond() / 60 / 60 / 24;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static long dateNumDaysAgo(int num) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.add(Calendar.DAY_OF_MONTH, -1 * num);
        return getLong(gc);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static long getLong(int month, int day, int year){
        GregorianCalendar gc = new GregorianCalendar(year, month, day);
        return getLong(gc);
    }

    public static GregorianCalendar getCal(long date) {
        GregorianCalendar gc = new GregorianCalendar(1970, 0, 1);
        gc.add(Calendar.DAY_OF_MONTH, (int) date);
        return gc;
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
    public static boolean dateIncludedInRp(RecurringPayment rp, long date) {
        // Check edits, if its in there, then no need to calculate anything
        // for(PaymentEdit edit: rp.edits.values()) {
        //     if(edit.newDate == date) return true;
        //     if(edit.editDate == date && edit.newDate != 0) return false;
        //     if(edit.editDate == date && edit.skip) return false;
        // }

        // no help with edits, do calculations
        return false;// rp.frequency.occursOn(date);
    }

}
