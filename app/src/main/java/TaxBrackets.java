import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class TaxBrackets {
    public static final String SINGLE = "single";
    public static final String HEAD_OF_HOUSE = "head_of_household";
    public static final String MARRIED_JOINT = "married_joint";
    public static final String MARRIED_SEP = "married_separate";
    public static final String UPPER_LIMIT = "upper_limit";
    public static final String LOWER_LIMIT = "lower_limit";
    public static final String PERCENTAGE = "percentage";

    public class Bracket{
        private final double upperLim;
        private final double lowerLim;
        private final double pct;

        public Bracket(double upperLim, double lowerLim, double pct) {
            this.upperLim = upperLim;
            this.lowerLim = lowerLim;
            this.pct = pct;
        }

        public double getUpperLim(){return upperLim;}
        public double getLowerLim(){return lowerLim;}
        public double getPct(){return pct;}
    }

    private Map<String, Bracket> bracket;

    public TaxBrackets(){
        bracket = new HashMap<>();
    }

    public void readJSON(String strJson) {
        try {
            JSONObject j = new JSONObject(strJson);
            this.readJSON(j);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void readJSON(@NonNull JSONObject jsonObject){
        try {
            JSONObject temp = jsonObject.getJSONObject(SINGLE);
            bracket.put(SINGLE, new Bracket(temp.getDouble(UPPER_LIMIT),
                    temp.getDouble(LOWER_LIMIT), temp.getDouble(PERCENTAGE)));

            temp = jsonObject.getJSONObject(HEAD_OF_HOUSE);
            bracket.put(HEAD_OF_HOUSE, new Bracket(temp.getDouble(UPPER_LIMIT),
                    temp.getDouble(LOWER_LIMIT), temp.getDouble(PERCENTAGE)));

            temp = jsonObject.getJSONObject(MARRIED_JOINT);
            bracket.put(MARRIED_JOINT, new Bracket(temp.getDouble(UPPER_LIMIT),
                    temp.getDouble(LOWER_LIMIT), temp.getDouble(PERCENTAGE)));

            temp = jsonObject.getJSONObject(MARRIED_SEP);
            bracket.put(MARRIED_SEP, new Bracket(temp.getDouble(UPPER_LIMIT),
                    temp.getDouble(LOWER_LIMIT), temp.getDouble(PERCENTAGE)));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Bracket getBracket(String type) {
        return bracket.get(type);
    }

}
