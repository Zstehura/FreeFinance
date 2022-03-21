package com.example.financefree.structures;

import com.example.financefree.fileClasses.PaymentEdit;
import com.example.financefree.fileClasses.RecurringPayment;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public final class parseDate {
    private parseDate(){}

    public static long getLong(GregorianCalendar gc){
        GregorianCalendar epoch = new GregorianCalendar(1970, 0,1);
        return gc.compareTo(epoch);
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

    public static long genID() {
        Date d = new Date();
        return d.getTime();
    }

    public static boolean dateIncludedInRp(RecurringPayment rp, long date) {
        // Check edits, if its in there, then no need to calculate anything
        for(PaymentEdit edit: rp.edits.values()) {
            if(edit.newDate == date) return true;
            if(edit.editDate == date && edit.skip) return false;
        }

        // no help with edits, do calculations
        boolean included = false;
        GregorianCalendar gcActual = new GregorianCalendar(1970,0,1);
        GregorianCalendar gcCount = (GregorianCalendar) gcActual.clone();
        gcCount.add(Calendar.DAY_OF_MONTH, (int) rp.startDate);
        gcActual.add(Calendar.DAY_OF_MONTH, (int) date);
        long counter = rp.startDate;

        if(rp.frequencyType == RecurringPayment.FREQ_ON_DATE_MONTHLY) {
            if(getDay(date) == rp.frequencyNum) included = true;
        }
        else if(rp.frequencyType == RecurringPayment.FREQ_EVERY_NUM_DAYS) {
            while(counter < date) {
                counter += rp.frequencyNum;
            }
            if(counter == date) included = true;
        }
        else if(rp.frequencyType == RecurringPayment.FREQ_ON_DATE_NUM_MONTHS) {
            int nMnths = rp.frequencyNum / 100;
            int nDay = rp.frequencyNum % 100;
            boolean thisMonth = false;
            boolean afterThisMonth = false;
            while(!thisMonth && !afterThisMonth) {
                gcCount.add(Calendar.MONTH, nMnths);
                if(gcCount.get(Calendar.MONTH) == gcActual.get(Calendar.MONTH) &&
                        gcCount.get(Calendar.YEAR) == gcActual.get(Calendar.YEAR))
                    thisMonth = true;
                if(gcCount.after(gcActual)) afterThisMonth = true;
            }
            if(thisMonth && getDay(date) == nDay) included = true;

        }
        else if(rp.frequencyType == RecurringPayment.FREQ_ON_DOW_MONTHLY){
            int nDow = rp.frequencyNum % 100;
            int nWom = rp.frequencyNum / 100;

            if(gcActual.get(Calendar.DAY_OF_WEEK) == nDow) {
                GregorianCalendar gcDow = new GregorianCalendar(gcActual.get(Calendar.YEAR), gcActual.get(Calendar.MONTH), 1);
                while(gcDow.get(Calendar.DAY_OF_WEEK) != nDow) {
                    gcDow.add(Calendar.DAY_OF_MONTH, 1);
                }
                gcDow.add(Calendar.DAY_OF_WEEK, nWom * 7);
                if (gcDow.get(Calendar.DAY_OF_MONTH) == gcActual.get(Calendar.DAY_OF_MONTH)) included = true;
            }
        }

        return included;
    }

}
