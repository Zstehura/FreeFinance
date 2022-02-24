package com.example.financefree.datahandlers;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Vector;

/*
    This class handles the larger dataset of data relevant to the profile as a whole
    contains:
    -Bank Accounts
      -Bank Statements
    -Tax Bracket Info
    -Recurring Payments
 */

public class DataManager {
    // Label Constants
    public static final String FILE_AS = "file_as";
    public static final String DEFAULT_BANK = "default_bank";
    public static final String BANK_ACCOUNTS = "bank_accts";
    public static final String SINGLE_PAYMENTS = "single_payments";
    public static final String RECURRING_PAYMENTS = "recurring_payments";
    public static final String BILLS = "bills";

    // Local data
    private String fileAs = TaxBrackets.SINGLE;
    private String defaultBankId = "";
    private final Map<Integer, TaxBrackets> taxBrackets = new HashMap<>();
    private final Map<String, BankAccount> bankAccounts = new HashMap<>();
    private final Map<String, RecurringPayment> recurringPayments = new HashMap<>();
    private final Map<String, List<SinglePayment>> singlePayments = new HashMap<>();
    private final Map<String, Bill> bills = new HashMap<>();

    // Constructor
    public DataManager(){}

    // Accessors
    public void setFileAs(String fileAs){this.fileAs = fileAs;}
    public void setDefaultBankId(String defaultBankId){this.defaultBankId = defaultBankId;}
    public String getFileAs(){return fileAs;}
    public String getDefaultBankId(){return defaultBankId;}

    public Map<String, Bill> getBills() {return bills;}
    public Map<String, BankAccount> getBankAccounts() {return bankAccounts;}
    public Map<String,RecurringPayment> getRecurringPayments() {return recurringPayments;}
    public Bill getBill(String billId){
        if(bills.containsKey(billId)) return bills.get(billId);
        else return null;
    }
    public BankAccount getBankAccount(String accountId) {
        if(bankAccounts.containsKey(accountId)) return bankAccounts.get(accountId);
        else return null;
    }
    public RecurringPayment getRecurringPayment(String paymentId) {
        if(recurringPayments.containsKey(paymentId)) return recurringPayments.get(paymentId);
        else return null;
    }
    public List<SinglePayment> getPayments(CustomDate gc) {
        if(singlePayments.containsKey(gc.toString())) return singlePayments.get(gc.toString());
        else return new Vector<>();
    }
    public void setBill(Bill bill){bills.put(bill.getBillId(), new Bill(bill));}
    public void setBankAccount(@NonNull BankAccount ba){bankAccounts.put(ba.getAccountId(), new BankAccount(ba));}
    public void setRecurringPayment(@NonNull RecurringPayment rp) throws CustomDate.DateErrorException {
        recurringPayments.put(rp.getPaymentId(), new RecurringPayment(rp));
    }
    public void setSinglePayment(SinglePayment sp) throws CustomDate.DateErrorException {
        if(!singlePayments.containsKey(sp.getDate().toString())){
            singlePayments.put(sp.getDate().toString(), new ArrayList<>());
        }
        Objects.requireNonNull(singlePayments.get(sp.getDate().toString())).add(new SinglePayment(sp));
    }
    public void removeBill(String billId) {bills.remove(billId);}
    public void removeBankAccount(String accountId){bankAccounts.remove(accountId);}
    public void removeRecurringPayment(String paymentId){recurringPayments.remove(paymentId);}
    public void removeSinglePayment(SinglePayment sp) {
        if(singlePayments.containsKey(sp.getDate().toString())){
            for(int i = 0; i < Objects.requireNonNull(singlePayments.get(sp.getDate().toString())).size(); i++){
                if(Objects.requireNonNull(singlePayments.get(sp.getDate().toString())).get(i).equals(sp)){
                    Objects.requireNonNull(singlePayments.get(sp.getDate().toString())).remove(i);
                    break;
                }
            }
        }
    }

    // Read/Write Functions
    public void readTaxBracket(JSONObject jsonObject) throws  JSONException {
        TaxBrackets tb = new TaxBrackets();
        tb.readJSON(jsonObject);
        if(!taxBrackets.containsKey(tb.getYear())){
            taxBrackets.put(tb.getYear(), new TaxBrackets(tb));
        }
    }
    public void readJSON(@NonNull JSONObject jsonObject) throws JSONException, CustomDate.DateErrorException {
        fileAs = jsonObject.getString(FILE_AS);
        defaultBankId = jsonObject.getString(DEFAULT_BANK);

        // Read Bank Accounts
        JSONArray jsonArray = jsonObject.getJSONArray(BANK_ACCOUNTS);
        for(int i = 0; i < jsonArray.length(); i++) {
            JSONObject j = jsonArray.getJSONObject(i);
            BankAccount ba = new BankAccount();
            ba.readJSON(j);
            bankAccounts.put(ba.getAccountId(), new BankAccount(ba));
        }

        // Read recurringPayments
        jsonArray = jsonObject.getJSONArray(RECURRING_PAYMENTS);
        for(int i = 0; i < jsonArray.length(); i++){
            JSONObject j = jsonArray.getJSONObject(i);
            RecurringPayment rp = new RecurringPayment();
            rp.readJSON(j);
            recurringPayments.put(rp.getPaymentId(), new RecurringPayment(rp));
        }

        // Read singlePayments
        jsonArray = jsonObject.getJSONArray(SINGLE_PAYMENTS);
        for(int i = 0; i < jsonArray.length(); i++){
            JSONObject j = jsonArray.getJSONObject(i);
            SinglePayment sp = new SinglePayment();
            sp.readJSON(j);
            if(!singlePayments.containsKey(sp.getDate().toString())) {
                singlePayments.put(sp.getDate().toString(), new ArrayList<>());
            }
            Objects.requireNonNull(singlePayments.get(sp.getDate().toString())).add(sp);
        }

        // Read Bills
        jsonArray = jsonObject.getJSONArray(BILLS);
        for(int i = 0; i < jsonArray.length(); i++) {
            JSONObject j = jsonArray.getJSONObject(i);
            Bill b = new Bill();
            b.readJSON(j);
            bills.put(b.getBillId(), new Bill(b));
        }
    }
    public JSONObject toJSON() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(FILE_AS,fileAs);
        jsonObject.put(DEFAULT_BANK,defaultBankId);

        JSONArray temp = new JSONArray();
        for(String id:bankAccounts.keySet()){
            temp.put(Objects.requireNonNull(bankAccounts.get(id)).toJSON());
        }
        jsonObject.put(BANK_ACCOUNTS, temp);

        temp = new JSONArray();
        for(String id: recurringPayments.keySet()){
            temp.put(Objects.requireNonNull(recurringPayments.get(id)).toJSONObject());
        }
        jsonObject.put(RECURRING_PAYMENTS, temp);

        temp = new JSONArray();
        List<String> gc = new ArrayList<>(singlePayments.keySet());
        Collections.sort(gc);
        for(int i = 0; i < gc.size(); i++){
            for(SinglePayment sp: Objects.requireNonNull(singlePayments.get(gc.get(i)))){
                temp.put(sp.toJSON());
            }
        }
        jsonObject.put(SINGLE_PAYMENTS, temp);

        temp = new JSONArray();
        for(String id: bills.keySet()){
            temp.put(Objects.requireNonNull(bills.get(id)).toJSON());
        }
        jsonObject.put(BILLS, temp);

        return jsonObject;
    }

    public double calculateAnnualIncomeTax(int nYear) throws CustomDate.DateErrorException {
        if(!taxBrackets.containsKey(nYear)) return -1;
        TaxBrackets.Bracket br = Objects.requireNonNull(taxBrackets.get(nYear)).getBracket(fileAs);
        double d = 0;
        double tax = 0;
        for(RecurringPayment rp: recurringPayments.values()){
            if(rp.getAmount() > 0){
                d += rp.getAnnualTotal(nYear);
            }
        }
        if(d == 0) return 0;
        for(int i = 0; i < br.length(); i++){
            if(d > br.getLowerLim(i)){
                if(d < br.getUpperLim(i)){
                    tax += (d - br.getLowerLim(i)) * br.getPct(i);
                }
                else {
                    tax += (br.getUpperLim(i) - br.getLowerLim(i)) * br.getPct(i);
                }
            }
        }
        return tax;
    }

    public List<SinglePayment> getPayments(int nMonth, int nYear) throws CustomDate.DateErrorException {
        List<SinglePayment> paymentList = new Vector<>();
        CustomDate cal = new CustomDate(nMonth,1,nYear);
        while(cal.get(Calendar.MONTH) == nMonth){
            if(singlePayments.containsKey(cal.toString())){
                paymentList.addAll(Objects.requireNonNull(singlePayments.get(cal.toString())));
            }
            cal.addDays(1);
        }

        for(RecurringPayment rp: recurringPayments.values()){
            paymentList.addAll(rp.getDatesFromMonth(nMonth, nYear));
        }

        return paymentList;
    }
    public Map<String, Double> getAccountBalances(String accountId, int nMonth, int nYear) throws CustomDate.DateErrorException {
        // Make sure account exists
        if(!bankAccounts.containsKey(accountId)) return null;

        Map<String, Double> acct = new HashMap<>();
        BankAccount ba = bankAccounts.get(accountId);
        assert(ba != null);
        CustomDate cal = new CustomDate(nMonth, 1, nYear);
        double bal = 0;

        //Get starting amount
        while(bal == 0 && cal.get(CustomDate.YEAR) > 1900) {
            bal = ba.getStatement(cal);
            cal.addDays(-1);
        }

        // Work out balance each day
        cal = new CustomDate(nMonth,1,nYear);
        List<SinglePayment> payments = getPayments(nMonth,nYear);
        while(cal.get(Calendar.MONTH) == nMonth){
            if(ba.getStatement(cal) != 0) bal = ba.getStatement(cal);
            for(SinglePayment sp: payments){
                if(sp.getDate().toString().equals(cal.toString()) && sp.getBankId().equals(accountId)){
                    bal += sp.getAmount();
                }
            }

            acct.put(cal.toString(), bal);
            cal.addDays(1);
        }

        return acct;
    }

}