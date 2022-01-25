import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class BankStatement {
    public static final String AMOUNT = "amount";
    public static final String BANK_ID = "bank_id";
    public static final String DATE = "date";

    private double amount;
    private String bankId;
    private Date date;

    public BankStatement(){}

    public void readJSON(JSONObject jsonObject){
        try {
            amount = jsonObject.getDouble(AMOUNT);
            bankId = jsonObject.getString(BANK_ID);
            date = new Date(jsonObject.getLong(DATE));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public JSONObject toJSON(){
        JSONObject j = new JSONObject();
        try {
            j.put(AMOUNT,amount);
            j.put(BANK_ID,bankId);
            j.put(DATE,date.getTime());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return j;
    }

}
