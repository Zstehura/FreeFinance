package com.example.financefree.structures;

import android.annotation.SuppressLint;
import android.util.Log;

import com.example.financefree.database.DatabaseManager;
import com.example.financefree.database.entities.PaymentEdit;
import com.example.financefree.database.entities.RecurringPayment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public final class DateParser {
    @SuppressLint("SimpleDateFormat")
    private static final SimpleDateFormat SDF_MON = new SimpleDateFormat("MMMM");
    @SuppressLint("SimpleDateFormat")
    private static final SimpleDateFormat SDF_DOW = new SimpleDateFormat("EEEE");

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

    public static int getDow(String s){
        GregorianCalendar gc = new GregorianCalendar();
        while(gc.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
            gc.add(Calendar.DAY_OF_WEEK, 1);
        }
        for(int i = 1; i <= 7; i++) {
            if(SDF_DOW.format(gc.getTime()).contains(s)) return i;
        }

        return -1;
    }
    public static String getDow(int n){
        GregorianCalendar gc = new GregorianCalendar();
        while(gc.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
            gc.add(Calendar.DAY_OF_WEEK, 1);
        }
        for(int i = 1; i <= 7; i++) {
            if(gc.get(Calendar.DAY_OF_WEEK) == n) return SDF_DOW.format(gc.getTime());
        }

        return "DayOfWeek?";
    }

    public static List<String> getDowList() {
        List<String> l = new ArrayList<>();
        GregorianCalendar gc = new GregorianCalendar();
        while(gc.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
            gc.add(Calendar.DAY_OF_WEEK, 1);
        }
        for(int i = 0; i < 12; i++){
            l.add(SDF_DOW.format(gc.getTime()));
            gc.add(Calendar.MONTH, 1);
        }
        return l;
    }

    public static List<String> getMonthList() {
        List<String> l = new ArrayList<>();
        GregorianCalendar gc = new GregorianCalendar(2000, 0, 15);
        for(int i = 0; i < 12; i++){
            l.add(SDF_MON.format(gc.getTime()));
            gc.add(Calendar.DAY_OF_WEEK, 1);
        }
        return l;
    }

    public static int monthToInt(String sMonth) {
        GregorianCalendar c = new GregorianCalendar(2000,0,15);
        for(int i = 0; i < 12; i++){
            if(SDF_MON.format(c.getTime()).contains(sMonth)) return i;
            c.add(Calendar.MONTH, 1);
        }

        return -1;
    }
    public static String monthToString(int iMonth) {
        GregorianCalendar c = new GregorianCalendar(2000,iMonth,15);
        String s = SDF_MON.format(c.getTime());
        return s;
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
