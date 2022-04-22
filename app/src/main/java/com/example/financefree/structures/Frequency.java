package com.example.financefree.structures;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.example.financefree.database.entities.PaymentEdit;
import com.example.financefree.database.entities.RecurringPayment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Frequency {
    private static final String START_DATE_KEY = "start";
    private static final String END_DATE_KEY = "end";
    private static final String TYPE_KEY = "type";
    private static final String NUM_KEY = "num";

    private static final Map<Integer, String> TYPE_OPTIONS = new HashMap<>();

    public long startDate;
    public long endDate;
    public int typeOpt;
    public int iNum;

    public static Map<Integer, String> typeOptions(){
        if(TYPE_OPTIONS.size() < 5){
            TYPE_OPTIONS.clear();
            TYPE_OPTIONS.put(0, "On specific date every month");
            TYPE_OPTIONS.put(1, "Every number of Days");
            TYPE_OPTIONS.put(2, "Every number of Weeks");
            TYPE_OPTIONS.put(3, "Every number of Months");
            TYPE_OPTIONS.put(4, "Every month on the 1st and 3rd");
            TYPE_OPTIONS.put(5, "Every month on the 2nd and 4th");
            TYPE_OPTIONS.put(6, "On the last day of every month");
        }
        return TYPE_OPTIONS;
    }

    public Frequency(RecurringPayment rp){
        startDate = rp.start_date;
        endDate = rp.end_date;
        typeOpt = rp.type_option;
        iNum = rp.frequency_number;
    }

    public static int getDow(String s){
        if(s.equals("Sunday"))    return Calendar.SUNDAY;
        if(s.equals("Monday"))    return Calendar.MONDAY;
        if(s.equals("Tuesday"))   return Calendar.TUESDAY;
        if(s.equals("Wednesday")) return Calendar.WEDNESDAY;
        if(s.equals("Thursday"))  return Calendar.THURSDAY;
        if(s.equals("Friday"))    return Calendar.FRIDAY;
        if(s.equals("Saturday"))  return Calendar.SATURDAY;
        return -1;
    }
    public static String getDow(int n){
        if(n == Calendar.SUNDAY)    return "Sunday";
        if(n == Calendar.MONDAY)    return "Monday";
        if(n == Calendar.TUESDAY)   return "Tuesday";
        if(n == Calendar.WEDNESDAY) return "Wednesday";
        if(n == Calendar.THURSDAY)  return "Thursday";
        if(n == Calendar.FRIDAY)    return "Friday";
        if(n == Calendar.SATURDAY)  return "Saturday";
        return "";
    }

    @NonNull
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        if(typeOpt >= 4 && typeOpt <= 6) {
            s.append(typeOptions().get(typeOpt));
            if(typeOpt == 4 || typeOpt == 5){
                s.append(" ").append(getDow(iNum));
            }
        }
        else if(typeOpt >= 1){
            s.append("Every ");
            if(iNum != 1) s.append(iNum).append(" ");
            if(typeOpt == 1) s.append("day");
            else if(typeOpt == 2) s.append("week");
            else if(typeOpt == 3) s.append("month");
            if(iNum != 1) s.append("s");
        }
        else if(typeOpt == 0) {
            s.append("On the ").append(iNum);
            if(s.charAt(s.length()-1) == '1') s.append("st");
            else if(s.charAt(s.length()-1) == '2') s.append("nd");
            else if(s.charAt(s.length()-1) == '3') s.append("rd");
            else s.append("th");
            s.append(" of every month");
        }

        return s.toString();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static Payment occursOn(List<PaymentEdit> pel, RecurringPayment rp, long date){
        Payment p = new Payment(rp, date);
        boolean skip = false, addOn = false;
        if(pel != null) {
            for(PaymentEdit pe: pel) {
                if(pe.edit_date == date && (pe.skip || pe.new_date != date)) skip = true;
                else if(pe.new_date == date) {
                    addOn = true;
                    p.bankId = pe.new_bank_id;
                    p.amount = pe.new_amount;
                }
            }
        }
        if(addOn && !skip) {
            return p;
        }
        else if(!skip && Frequency.occursOn(rp, date)) {
            return p;
        }
        else return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static List<Payment> paymentsBetween(List<PaymentEdit> pel, RecurringPayment rp, long date1, long date2){
        List<Long> dateList = occurencesBetween(rp, date1, date2);
        List<Payment> paymentList = new ArrayList<>();
        for(long d: dateList){
            paymentList.add(new Payment(rp, d));
        }

        if(pel != null) {
            for(PaymentEdit pe: pel) {
                if(dateList.contains(pe.edit_date)){
                    for(int i = 0; i < dateList.size(); i++){
                        if(dateList.get(i) == pe.edit_date){
                            if(pe.skip) {
                                dateList.remove(i);
                                paymentList.remove(i);
                            }
                            else {
                                dateList.set(i, pe.new_date);
                                Payment p = new Payment(pe, rp.name, rp.notes);
                                paymentList.set(i, p);
                            }
                            break;
                        }
                    }
                }
            }
        }

        return paymentList;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static List<Long> occurencesBetween(List<PaymentEdit> pel, RecurringPayment rp, long date1, long date2){
        List<Long> dateList = occurencesBetween(rp, date1, date2);
        if(pel != null) {
            for(PaymentEdit pe: pel) {
                if(dateList.contains(pe.edit_date) && (pe.skip || pe.new_date != pe.edit_date)){
                    for(int i = 0; i < dateList.size(); i++){
                        if(dateList.get(i) == pe.edit_date){
                            if(pe.skip) dateList.remove(i);
                            else dateList.set(i, pe.new_date);
                            break;
                        }
                    }
                }
            }
        }

        return dateList;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static List<Long> occurencesBetween(RecurringPayment rp, long date1, long date2){
        List<Long> dateList = new ArrayList<>();

        //ensure the rp occurs in the range
        if(date2 < rp.start_date || date1 > rp.end_date) return dateList;
        long endThis = date2;
        if(rp.end_date < endThis) endThis = rp.end_date;
        
        if(rp.type_option == 0) {           // On specific date every month
            GregorianCalendar gc = DateParser.getCal(date1);
            long d = DateParser.getLong(gc.get(Calendar.MONTH), rp.frequency_number, gc.get(Calendar.YEAR));
            do {
                if(d >= date1) dateList.add(d);
                d = DateParser.addToDate(d, 1, Calendar.MONTH);
            } while(d < endThis);
        }
        else if(rp.type_option == 1) {      // Every number of Days
            long d = rp.start_date;
            while(d <= endThis) {
                if(d >= date1) dateList.add(d);
                d += rp.frequency_number;
            }
        }
        else if(rp.type_option == 2) {     // Every number of Weeks
            long d = rp.start_date;
            while (d <= endThis) {
                if(d >= date1) dateList.add(d);
                d = DateParser.addToDate(d, rp.frequency_number, Calendar.WEEK_OF_MONTH);
            }
        }
        else if(rp.type_option == 3) {     // Every number of Months
            long d = rp.start_date;
            while (d <= endThis) {
                if(d >= date1) dateList.add(d);
                d = DateParser.addToDate(d, rp.frequency_number, Calendar.MONTH);
            }
        }
        else if(rp.type_option == 4) {     // Every month on the 1st and 3rd
            GregorianCalendar gc = DateParser.getCal(rp.start_date);
            gc.set(Calendar.DAY_OF_MONTH, 1);
            while (DateParser.getLong(gc) <= endThis){
                while(gc.get(Calendar.DAY_OF_WEEK) != rp.frequency_number){
                    gc.add(Calendar.DAY_OF_MONTH, 1);
                }
                long d = DateParser.getLong(gc);
                if(d <= endThis && d >= date1) dateList.add(d);
                gc.add(Calendar.WEEK_OF_MONTH, 2);
                d = DateParser.getLong(gc);
                if(d <= endThis && d >= date1) dateList.add(d);
                gc.add(Calendar.MONTH, 1);
                gc.set(Calendar.DAY_OF_MONTH, 1);
            }
        }
        else if(rp.type_option == 5) {     // Every month on the 2nd and 4th
            GregorianCalendar gc = DateParser.getCal(rp.start_date);
            gc.set(Calendar.DAY_OF_MONTH, 8);
            while (DateParser.getLong(gc) <= endThis){
                while(gc.get(Calendar.DAY_OF_WEEK) != rp.frequency_number){
                    gc.add(Calendar.DAY_OF_MONTH, 1);
                }
                long d = DateParser.getLong(gc);
                if(d <= endThis && d >= date1) dateList.add(d);
                gc.add(Calendar.WEEK_OF_MONTH, 2);
                d = DateParser.getLong(gc);
                if(d <= endThis && d >= date1) dateList.add(d);
                gc.add(Calendar.MONTH, 1);
                gc.set(Calendar.DAY_OF_MONTH, 8);
            }
        }
        else if(rp.type_option == 6) {     // On the last day of every month
            GregorianCalendar gc = DateParser.getCal(rp.start_date);
            gc.add(Calendar.MONTH, 1);
            gc.set(Calendar.DAY_OF_MONTH, 1);
            gc.add(Calendar.DAY_OF_MONTH, -1);
            while(DateParser.getLong(gc) <= endThis) {
                long d = DateParser.getLong(gc);
                if(d >= date1 && d <= endThis)dateList.add(d);
                gc.add(Calendar.MONTH, 2);
                gc.set(Calendar.DAY_OF_MONTH, 1);
                gc.add(Calendar.DAY_OF_MONTH, -1);
            }
        }

        return dateList;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static boolean occursOn(RecurringPayment rp, long date){
        if(date < rp.start_date || date > rp.end_date) return false;

        if(rp.type_option == 0) {  // On specific date every month
            GregorianCalendar gc = DateParser.getCal(date);
            return gc.get(Calendar.DAY_OF_MONTH) == rp.frequency_number;
        }
        else if(rp.type_option == 1) {     // Every number of Days
            long count = rp.start_date;
            while(count < date) {
                count += rp. frequency_number;
            }
            return count == date;
        }
        else if(rp.type_option == 2) {     // Every number of Weeks
            GregorianCalendar gcCount = DateParser.getCal(rp.start_date);
            while(DateParser.getLong(gcCount) < date){
                gcCount.add(Calendar.WEEK_OF_YEAR, rp. frequency_number);
            }
            return DateParser.getLong(gcCount) == date;
        }
        else if(rp.type_option == 3) {     // Every number of Months
            GregorianCalendar gcCount = DateParser.getCal(rp.start_date);
            while(DateParser.getLong(gcCount) < date){
                gcCount.add(Calendar.MONTH, rp. frequency_number);
            }
            return DateParser.getLong(gcCount) == date;
        }
        else if(rp.type_option == 4) {     // Every month on the 1st and 3rd
            GregorianCalendar gc = DateParser.getCal(date);
            gc.set(Calendar.DAY_OF_MONTH, 1);
            while(gc.get(Calendar.DAY_OF_WEEK) != rp. frequency_number){
                gc.add(Calendar.DAY_OF_MONTH, 1);
            }
            if(DateParser.getLong(gc) == date) return true;
            gc.add(Calendar.WEEK_OF_MONTH, 2);
            return DateParser.getLong(gc) == date;
        }
        else if(rp.type_option == 5) {     // Every month on the 2nd and 4th
            GregorianCalendar gc = DateParser.getCal(date);
            gc.set(Calendar.DAY_OF_MONTH, 8);
            while(gc.get(Calendar.DAY_OF_WEEK) != rp. frequency_number){
                gc.add(Calendar.DAY_OF_MONTH, 1);
            }
            if(DateParser.getLong(gc) == date) return true;
            gc.add(Calendar.WEEK_OF_MONTH, 2);
            return DateParser.getLong(gc) == date;
        }
        else if(rp.type_option == 6) {     // On the last day of every month
            GregorianCalendar gc = DateParser.getCal(date);
            int m = gc.get(Calendar.MONTH);
            gc.add(Calendar.DAY_OF_MONTH, 1);
            return gc.get(Calendar.MONTH) != m;
        }

        return false;
    }
}
