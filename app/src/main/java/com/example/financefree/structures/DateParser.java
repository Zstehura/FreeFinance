package com.example.financefree.structures;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.financefree.database.DatabaseManager;
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
    public static String getToday(){
        long n = getLong(new GregorianCalendar());
        return getString(n);
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

    @RequiresApi(api = Build.VERSION_CODES.O)
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

    @RequiresApi(api = Build.VERSION_CODES.O)
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
