import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class SinglePayment {
    public static final String AMOUNT = "amount";
    public static final String DATE = "date";
    public static final String NAME = "name";
    public static final String NOTES = "notes";

    private double amount;
    private Date date;
    private String name;
    private String notes;

    public SinglePayment(){}

    public void readJSON(JSONObject jsonObject) {
        try {
            amount = jsonObject.getDouble(AMOUNT);
            date = new Date(jsonObject.getLong(DATE));
            name = jsonObject.getString(NAME);
            notes = jsonObject.getString(NOTES);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public JSONObject toJSON() {
        JSONObject j = new JSONObject();
        try {
            j.put(AMOUNT,amount);
            j.put(DATE,date.getTime());
            j.put(NAME,name);
            j.put(NOTES,notes);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return j;
    }


}
