import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

public class Profile {
    public static final String FILE_AS = "file_as";
    public static final String INCOME = "income";
    public static final String PRETAX = "pretax";

    private String fileAs;
    private double tax;
    private TaxBrackets tb;
    private List<Double> pretax;
    private List<Double> grossAnnual;

    public Profile(){
        grossAnnual = new LinkedList<>();
        pretax = new LinkedList<>();
    }

    public void setFileAs(String fileAs){this.fileAs = fileAs; calculateTax();}
    public String getFileAs(){return fileAs;}

    public void readJSON(JSONObject jsonObject){
        try {
            fileAs = jsonObject.getString(FILE_AS);
            calculateTax();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setTaxBrackets(JSONObject jsonObject){
        tb = new TaxBrackets();
        tb.readJSON(jsonObject);
    }

    public List<Double> getPretax(){return pretax;}
    public List<Double> getGrossAnnual(){return grossAnnual;}
    public void removePretax(double amount) {pretax.remove(amount); calculateTax();}
    public void removeGrossAnnual(double amount) {grossAnnual.remove(amount); calculateTax();}
    public void addGrossAnnual(double amount) {grossAnnual.add(amount); calculateTax();}
    public void addPretax(double amount) {pretax.add(amount); calculateTax();}

    public double getGrossIncome() {
        double n = 0;
        for(Double d: grossAnnual){
            n += d;
        }
        return n;
    }
    public double getGrossPretax(){
        double n = 0;
        for(Double d: pretax){
            n += d;
        }
        return n;
    }
    public double getGrossTotal(){
        double n = getGrossIncome();
        n -= getGrossPretax();
        return n;
    }

    public double calculateTax() {
        tax = 0;
        TaxBrackets.Bracket brack = tb.getBracket(fileAs);
        double gt = getGrossTotal();
        for(int i = 0; i < brack.length(); i++){
            if(gt > brack.getLowerLim(i)){
                if(gt > brack.getUpperLim(i) && brack.getUpperLim(i) > 0){
                    tax += (brack.getPct(i) * (brack.getUpperLim(i) - brack.getLowerLim(i)));
                }
                else {
                    tax += (brack.getPct(i) *(gt - brack.getLowerLim(i)));
                }
            }

        }
        return tax;
    }


}