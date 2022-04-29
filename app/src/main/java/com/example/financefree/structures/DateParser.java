package com.example.financefree.structures;

import android.util.Log;



import com.example.financefree.database.DatabaseManager;
import com.example.financefree.database.entities.PaymentEdit;
import com.example.financefree.database.entities.RecurringPayment;

import java.util.Calendar;
import java.util.GregorianCalendar;

public final class DateParser {
    private DateParser(){}

    
    public static long addToDate(long date, int n, int field) {
        if(field == Calendar.DAY_OF_MONTH || field == Calendar.DAY_OF_YEAR) {
            return date + n;
        }
        else if(field == Calendar.WEEK_OF_YEAR || field == Calendar.WEEK_OF_MONTH){
            return date + (7L * n);
        }
        else if(field == Calendar.MONTH) {
            GregorianCalendar gc = getCal(date);
            gc.add(Calendar.MONTH, n);
            return getLong(gc);
        }
        else if(field == Calendar.YEAR) {
            GregorianCalendar gc = getCal(date);
            gc.add(Calendar.YEAR, n);
            return getLong(gc);
        }
        return 0;
    }

    
    public static long getLong(GregorianCalendar gc){
        return gc.toInstant().getEpochSecond() / 60 / 60 / 24;
    }

    
    public static String getToday(){
        long n = getLong(new GregorianCalendar());
        return getString(n);
    }

    
    public static long dateNumDaysAgo(int num) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.add(Calendar.DAY_OF_MONTH, -1 * num);
        return getLong(gc);
    }

    
    public static long getLong(int month, int day, int year){
        GregorianCalendar gc = new GregorianCalendar(year, month, day);
        return getLong(gc);
    }

    
    public static long getLong(String s){
        String[] temp = s.split("/");
        GregorianCalendar gc = new GregorianCalendar(Integer.parseInt(temp[2]), Integer.parseInt(temp[0]) - 1, Integer.parseInt(temp[1]));
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

    public static int monthToInt(String sMonth) {
        switch (sMonth) {
            case "January":
                return Calendar.JANUARY;
            case "February":
                return Calendar.FEBRUARY;
            case "March":
                return Calendar.MARCH;
            case "April":
                return Calendar.APRIL;
            case "May":
                return Calendar.MAY;
            case "June":
                return Calendar.JUNE;
            case "July":
                return Calendar.JULY;
            case "August":
                return Calendar.AUGUST;
            case "September":
                return Calendar.SEPTEMBER;
            case "October":
                return Calendar.OCTOBER;
            case "November":
                return Calendar.NOVEMBER;
            case "December":
                return Calendar.DECEMBER;
        }
        return -1;
    }
    public static String monthToString(int iMonth) {
        switch (iMonth) {
            case Calendar.JANUARY:
                return "January";
            case Calendar.FEBRUARY:
                return "February";
            case Calendar.MARCH:
                return "March";
            case Calendar.APRIL:
                return "April";
            case Calendar.MAY:
                return "May";
            case Calendar.JUNE:
                return "June";
            case Calendar.JULY:
                return "July";
            case Calendar.AUGUST:
                return "August";
            case Calendar.SEPTEMBER:
                return "September";
            case Calendar.OCTOBER:
                return "October";
            case Calendar.NOVEMBER:
                return "November";
            case Calendar.DECEMBER:
                return "December";
        }
        return "Month?";
    }

    
    public static boolean dateIncludedInRp(RecurringPayment rp, long date) {
        // Check edits, if its in there, then no need to calculate anything
        boolean[] boolEdit = new boolean[2];
        Thread t = new Thread(() -> {
            for(PaymentEdit pe: DatabaseManager.getPaymentEditDao().getAllForRp(rp.rp_id)){
                if(pe.new_date == date) {
                    boolEdit[0] = true;
                    boolEdit[1] = true;
                }
                if(pe.edit_date == date && pe.new_date != 0){
                    boolEdit[0] = true;
                    boolEdit[1] = false;
                }
                if(pe.edit_date == date && pe.skip){
                    boolEdit[0] = true;
                    boolEdit[1] = false;
                }
            }
        });
        t.start();

        boolean freq = Frequency.occursOn(rp,date);
        try{t.join();}
        catch (InterruptedException e) {Log.e("DateParser", e.getMessage());}
        if(boolEdit[0]){
            return boolEdit[1];
        }
        else {
            return freq;
        }
    }

}
