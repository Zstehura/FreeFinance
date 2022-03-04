package com.example.financefree.structures;

import androidx.annotation.NonNull;

import java.util.Calendar;
import java.util.GregorianCalendar;

//TODO: Write custom Date Class (stores day/month/year) with clone function

@SuppressWarnings("MagicConstant")
public class CustomDate implements Comparable<CustomDate>{
    @Override
    public int compareTo(CustomDate customDate) {
        GregorianCalendar gc1 = toCal(), gc2 = customDate.toCal();
        return gc1.compareTo(gc2);
    }

    public boolean before(CustomDate c) {
        int i = compareTo(c);
        return i < 0;
    }

    public static class DateErrorException extends Exception {}
    public static final int DAY = 1;
    public static final int MONTH = 2;
    public static final int YEAR = 3;

    private int nDay;
    private int nMonth;
    private int nYear;

    public CustomDate(int month, int day, int year) throws DateErrorException {
        GregorianCalendar gc = new GregorianCalendar(year, month - 1, day);
        if(gc.get(Calendar.DAY_OF_MONTH) != day || gc.get(Calendar.MONTH) != (month - 1) || gc.get(Calendar.YEAR) != year){
            throw new DateErrorException();
        }
        nDay = day;
        nMonth = month;
        nYear = year;
    }
    public CustomDate(String s) throws DateErrorException {
        String[] temp = s.split("/");
        int year = Integer.parseInt(temp[2]), month = Integer.parseInt(temp[0]), day = Integer.parseInt(temp[1]);
        GregorianCalendar gc = new GregorianCalendar(year, month - 1, day);
        if(gc.get(Calendar.DAY_OF_MONTH) != day || gc.get(Calendar.MONTH) != (month - 1) || gc.get(Calendar.YEAR) != year){
            throw new DateErrorException();
        }
        nDay = day;
        nMonth = month;
        nYear = year;
    }
    public CustomDate(GregorianCalendar gc){
        nYear = gc.get(Calendar.YEAR);
        nMonth = gc.get(Calendar.MONTH) + 1;
        nDay = gc.get(Calendar.DAY_OF_MONTH);
    }
    public CustomDate() throws DateErrorException {
        this(1,1,2001);
    }
    public CustomDate(CustomDate cd){
        nDay = cd.nDay;
        nMonth = cd.nMonth;
        nYear = cd.nYear;
    }

    public int get(int type){
        switch (type){
            case MONTH:
                return nMonth;
            case DAY:
                return nDay;
            case YEAR:
                return nYear;
            default:
                return 0;
        }
    }
    public void set(int type, int value) throws DateErrorException {
        GregorianCalendar gc;
        switch (type){
            case MONTH:
                if(value > 12 || value < 1) throw new DateErrorException();
                nMonth = value;
                break;
            case DAY:
                gc = new GregorianCalendar(nYear,nMonth-1,value);
                if(gc.get(Calendar.MONTH) != nMonth-1) throw new DateErrorException();
                nDay = value;
                break;
            case YEAR:
                gc = new GregorianCalendar(value,nMonth - 1,nDay);
                if(gc.get(Calendar.MONTH) != nMonth-1) throw new DateErrorException();
                nYear = value;
                break;
            default:
                throw new DateErrorException();
        }
    }

    public void addDays(int days){
        GregorianCalendar gc = new GregorianCalendar(nYear,nMonth - 1, nDay);
        gc.add(Calendar.DAY_OF_MONTH, days);
        nYear = gc.get(Calendar.YEAR);
        nMonth = gc.get(Calendar.MONTH)+1;
        nDay = gc.get(Calendar.DAY_OF_MONTH);
    }
    public void addMonths(int months){
        GregorianCalendar gc = new GregorianCalendar(nYear,nMonth - 1, nDay);
        gc.add(Calendar.MONTH, months);
        nYear = gc.get(Calendar.YEAR);
        nMonth = gc.get(Calendar.MONTH)+1;
        nDay = gc.get(Calendar.DAY_OF_MONTH);
    }

    @NonNull
    public String toString(){
        return nMonth + "/" + nDay + "/" + nYear;
    }
    public GregorianCalendar toCal(){return new GregorianCalendar(nYear,nMonth-1,nDay);}

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
