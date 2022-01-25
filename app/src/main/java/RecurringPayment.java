import android.annotation.SuppressLint;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

// TODO: add query features
//      add bank connections
public class RecurringPayment {

    // Done
    public class PaymentEdit {
        public static final String ACTION = "action";
        public static final String EDIT_DATE = "edit_date";
        public static final String MOVE_DATE = "move_to_date";
        public static final String ACTION_MOVE = "move";
        public static final String ACTION_SKIP = "skip";

        private String action;
        private Date editDate;
        private Date moveDate;

        public PaymentEdit() {}


        public Date getMoveDate() {return moveDate;}
        public void setMoveDate(Date moveDate) {this.moveDate = moveDate;}
        public Date getEditDate() {
            return editDate;
        }
        public void setEditDate(Date editDate) {
            this.editDate = editDate;
        }
        public String getAction() {return action;}
        public void setAction(String action) {this.action = action;}

        public JSONObject toJSONObject(){
            JSONObject j = new JSONObject();
            try {
                j.put(ACTION, action);
                j.put(EDIT_DATE, editDate.getTime());
                if (action.equals(ACTION_MOVE)){
                    j.put(MOVE_DATE, moveDate.getTime());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return j;
        }
        public void readJSON(@NonNull JSONObject jsonObject){
            try {
                action = jsonObject.getString(ACTION);
                editDate = new Date(jsonObject.getLong(EDIT_DATE));
                if(action.equals(ACTION_MOVE)) {
                    moveDate = new Date(jsonObject.getLong(MOVE_DATE));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    public static final String SOURCE_ID = "source_id";
    public static final String NOTES = "notes";
    public static final String FREQ_TYPE = "frequency_type";
    public static final String FREQUENCY = "frequency";
    public static final String ON = "on";
    public static final String EVERY = "every";
    public static final String AMOUNT = "amount";
    public static final String START = "start";
    public static final String END = "end";
    public static final String EDITS = "edits";


    private String frequencyType;
    private int frequency;
    private double amount;
    private Date start;
    private Date end;
    private String notes;
    private String bankId;
    private Map<Date, PaymentEdit> edits;

    public RecurringPayment() {
        edits = new HashMap<>();
    }

    public void setBankId(String bankId){this.bankId = bankId;}
    public void setNotes(String notes){this.notes = notes;}
    public void setFrequency(String frequencyType, int frequency){
        if(frequencyType == ON || frequencyType == EVERY) {
            this.frequencyType = frequencyType;
            this.frequency = frequency;
        }
        else {
            this.frequencyType = "";
            this.frequency = 0;
        }
    }
    public void setAmount(double amount) {this.amount = amount;}
    public void setStart(Date start) {this.start = start;}
    public void setEnd(Date end) {this.end = end;}
    public String getBankId(){return bankId;}
    public String getNotes(){return notes;}
    public String getFrequencyType(){return frequencyType;}
    public int getFrequency() {return frequency;}
    public double getAmount() {return this.amount;}
    public Date getStart() {return this.start;}
    public Date getEnd() {return this.end;}

    public void addEdit(PaymentEdit paymentEdit){
        edits.put(paymentEdit.getEditDate(), paymentEdit);
    }
    public PaymentEdit getEdit(Date date){
        return edits.get(date);
    }
    public void removeEdit(Date date){
        edits.remove(date);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public List<Date> getDatesFromMonth(int month, int year) {
        List<Date> l = new ArrayList<>();
        if (frequencyType.equals(ON)) {
            // Occurs on given date every month
            GregorianCalendar c = new GregorianCalendar(year, month, frequency);
            Date d = c.getTime();
            l.add(d);
            if (edits.containsKey(d)) {
                PaymentEdit paymentEdit = edits.get(d);
                if(paymentEdit.getAction().equals(PaymentEdit.ACTION_MOVE)){
                    l.set(0, paymentEdit.getMoveDate());
                }
                else if(paymentEdit.getAction().equals(PaymentEdit.ACTION_SKIP)){
                    l.remove(0);
                }
            }
        } else {
            // TODO: ADD FREQUENCY EVERY # DAYS
        }

        return l;
    }

    public void readJSON(String strJson) {
        try {
            JSONObject j = new JSONObject(strJson);
            JSONArray jsonArray = new JSONArray(j.getJSONArray(EDITS));
            for(int i = 0; i < jsonArray.length(); i++){
                PaymentEdit temp = new PaymentEdit();
                temp.readJSON(jsonArray.getJSONObject(i));
                edits.put(temp.getEditDate(),temp);
            }
            this.amount = j.getDouble(AMOUNT);
            this.start = new Date(j.getLong(START));
            this.end = new Date(j.getLong(END));
            this.frequencyType = j.getString(FREQ_TYPE);
            this.frequency = j.getInt(FREQUENCY);
            this.notes = j.getString(NOTES);
            this.bankId = j.getString(SOURCE_ID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public JSONObject toJSONObject() {
        JSONObject j = new JSONObject();
        try {
            JSONArray ja = new JSONArray();
            for(PaymentEdit pe: edits.values()){
                ja.put(pe.toJSONObject());
            }
            j.put(EDITS, ja);
            j.put(AMOUNT, amount);
            j.put(START, start.getTime());
            j.put(END, end.getTime());
            j.put(FREQ_TYPE, frequencyType);
            j.put(FREQUENCY, frequency);
            j.put(NOTES,notes);
            j.put(SOURCE_ID,bankId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return j;
    }

}