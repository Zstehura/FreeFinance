package com.example.financefree.fileClasses;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.financefree.structures.parseDate;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

public class Frequency {
    private static final String START_DATE_KEY = "start";
    private static final String END_DATE_KEY = "end";
    private static final String TYPE_KEY = "type";
    private static final String NUM_KEY = "num";

    public static final Map<Integer, String> TYPE_OPTIONS = new HashMap<>();

    public long startDate;
    public long endDate;
    public int typeOpt;
    public int iNum;

    public Frequency(){
        TYPE_OPTIONS.clear();
        TYPE_OPTIONS.put(1, "On specific date every month");
        TYPE_OPTIONS.put(2, "Every number of Days");
        TYPE_OPTIONS.put(3, "Every number of Weeks");
        TYPE_OPTIONS.put(4, "Every number of Months");
        TYPE_OPTIONS.put(5, "Every month on the 1st and 3rd");
        TYPE_OPTIONS.put(6, "Every month on the 2nd and 4th");
        TYPE_OPTIONS.put(7, "On the last day of every month");
    }
    public Frequency(JSONObject jsonObject) throws JSONException {
        this();
        readJSON(jsonObject);
    }

    public void readJSON(JSONObject jsonObject) throws JSONException {
        startDate = jsonObject.getLong(START_DATE_KEY);
        endDate = jsonObject.getLong(END_DATE_KEY);
        typeOpt = jsonObject.getInt(TYPE_KEY);
        iNum = jsonObject.getInt(NUM_KEY);
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(START_DATE_KEY,startDate);
        jsonObject.put(END_DATE_KEY, endDate);
        jsonObject.put(TYPE_KEY, typeOpt);
        jsonObject.put(NUM_KEY, iNum);
        return jsonObject;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public boolean occursOn(long date){
        if(date < startDate || date > endDate) return false;

        if(typeOpt == 1) {  // On specific date every month
            GregorianCalendar gc = parseDate.getCal(date);
            return gc.get(Calendar.DAY_OF_MONTH) == iNum;
        }
        else if(typeOpt == 2) {     // Every number of Days
            long count = startDate;
            while(count < date) {
                count += iNum;
            }
            return count == date;
        }
        else if(typeOpt == 3) {     // Every number of Weeks
            GregorianCalendar gcCount = parseDate.getCal(startDate);
            while(parseDate.getLong(gcCount) < date){
                gcCount.add(Calendar.WEEK_OF_YEAR, iNum);
            }
            return parseDate.getLong(gcCount) == date;
        }
        else if(typeOpt == 4) {     // Every number of Months
            GregorianCalendar gcCount = parseDate.getCal(startDate);
            while(parseDate.getLong(gcCount) < date){
                gcCount.add(Calendar.MONTH, iNum);
            }
            return parseDate.getLong(gcCount) == date;
        }
        else if(typeOpt == 5) {     // Every month on the 1st and 3rd
            GregorianCalendar gc = parseDate.getCal(date);
            gc.set(Calendar.DAY_OF_MONTH, 1);
            while(gc.get(Calendar.DAY_OF_WEEK) != iNum){
                gc.add(Calendar.DAY_OF_MONTH, 1);
            }
            if(parseDate.getLong(gc) == date) return true;
            gc.add(Calendar.WEEK_OF_MONTH, 2);
            return parseDate.getLong(gc) == date;
        }
        else if(typeOpt == 6) {     // Every month on the 2nd and 4th
            GregorianCalendar gc = parseDate.getCal(date);
            gc.set(Calendar.DAY_OF_MONTH, 7);
            while(gc.get(Calendar.DAY_OF_WEEK) != iNum){
                gc.add(Calendar.DAY_OF_MONTH, 1);
            }
            if(parseDate.getLong(gc) == date) return true;
            gc.add(Calendar.WEEK_OF_MONTH, 2);
            return parseDate.getLong(gc) == date;
        }
        else if(typeOpt == 7) {     // On the last day of every month
            GregorianCalendar gc = parseDate.getCal(date);
            int m = gc.get(Calendar.MONTH);
            gc.add(Calendar.DAY_OF_MONTH, 1);
            return gc.get(Calendar.MONTH) != m;
        }

        return false;
    }
}
