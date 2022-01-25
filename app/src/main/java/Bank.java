import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

public class Bank {
    public static final String BANK_ID = "bank_id";
    public static final String NOTES = "notes";

    private String bankId;
    private String notes;

    public Bank(){}

    public void setBankId(String bankId){this.bankId = bankId;}
    public void setNotes(String notes){this.notes = notes;}
    public String getBankId(){ return bankId;}
    public String getNotes(){return notes;}

    public void readJSON(@NonNull JSONObject jsonObject){
        try {
            bankId = jsonObject.getString(BANK_ID);
            notes = jsonObject.getString(NOTES);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public JSONObject toJson(){
        JSONObject j = new JSONObject();
        try {
            j.put(BANK_ID, bankId);
            j.put(NOTES, notes);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return j;
    }

}
