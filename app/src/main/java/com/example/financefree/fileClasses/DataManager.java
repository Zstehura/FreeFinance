package com.example.financefree.fileClasses;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.financefree.structures.parseDate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@SuppressWarnings("ConstantConditions")
public class DataManager {

    /*
    *
    *   App constants and variables
    *
     */

    private static boolean init = false;

    private static final String ID_KEY = "id";
    private static final String YEAR_KEY = "year";
    private static final String BANK_ACCOUNT_KEY = "ba";
    private static final String RECURRING_PAYMENT_KEY = "rp";
    private static final String SINGLE_PAYMENT_KEY = "sp";
    private static final String BANK_ACCOUNT_FILENAME = "ba_freefinance_appdata.dat";
    private static final String RECURRING_PAYMENT_FILENAME = "rp_freefinance_appdata.dat";
    private static final String SINGLE_PAYMENT_FILENAME = "sp_freefinance_appdata.dat";
    private static final String TAX_BRACKET_FILENAME = "tb_freefinance_appdata.dat";

    private static Map<Long, BankAccount> bankAccounts = new HashMap<>();
    private static Map<Long, RecurringPayment> recurringPayments = new HashMap<>();
    private static Map<Long, List<SinglePayment>> singlePayments = new HashMap<>();

    /*
    *
    *   IO Functions, initialize data and write when app is ready
    *
     */

    public static void initData(Context context) throws IOException, JSONException {
        if(!init) {
            JSONArray jsonArray = new JSONArray(readFile(context.getFilesDir(), BANK_ACCOUNT_FILENAME));
            for(int i = 0; i < jsonArray.length(); i++) {
                JSONObject j = jsonArray.getJSONObject(i);
                BankAccount ba = new BankAccount(j.getJSONObject(BANK_ACCOUNT_KEY));
                bankAccounts.put(j.getLong(ID_KEY), ba);
            }
            // TODO: finish reading all data


            init = true;
        }
    }

    public static void close(Context context) throws JSONException, IOException {
        JSONArray jsonArray = new JSONArray();
        for(long id: bankAccounts.keySet()){
            JSONObject j = new JSONObject();
            j.put(ID_KEY, id);
            j.put(BANK_ACCOUNT_KEY, Objects.requireNonNull(bankAccounts.get(id)).toJSON());
        }
        writeFile(context.getFilesDir(), BANK_ACCOUNT_FILENAME, jsonArray.toString());

    }

    private static void writeFile(File dir,String fn, String out) throws IOException {
        File file = new File(dir, fn);
        BufferedWriter bw = new BufferedWriter(new FileWriter(file));
        bw.write(out);
        bw.close();
    }

    private static String readFile(File dir, String fn) throws IOException {
        File file = new File(dir, fn);
        BufferedReader br = new BufferedReader(new FileReader(file));
        StringBuilder sb = new StringBuilder();
        String txt = br.readLine();
        while(txt != null) {
            sb.append(txt);
            txt = br.readLine();
        }
        br.close();
        return sb.toString();
    }

    /*
    *
    *   public accessor functions
    *
     */

    public long insertBankAccount(BankAccount ba) {
        long id = parseDate.genID();
        bankAccounts.put(id,ba);
        return id;
    }
    public long insertRecurringPayment(RecurringPayment rp) {
        long id = parseDate.genID();
        recurringPayments.put(id, rp);
        return id;
    }
    public void insertSinglePayment(SinglePayment sp) {
        // prevent single payments from the same day having the same name
        boolean skip = false;
        if(singlePayments.containsKey(sp.date)) {
            for (SinglePayment sTemp : Objects.requireNonNull(singlePayments.get(sp.date))) {
                if (sTemp.name.equals(sp.name)) {
                    skip = true;
                    break;
                }
            }
        }
        else { singlePayments.put(sp.date, new LinkedList<>());}

        if(!skip) {
            singlePayments.get(sp.date).add(sp);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void updateBankAccount(BankAccount ba, long id) {
        if(bankAccounts.containsKey(id)) bankAccounts.replace(id, ba);
        else bankAccounts.put(id,ba);
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void updateRecurringPayment(RecurringPayment rp, long id){
        if(recurringPayments.containsKey(id)) recurringPayments.replace(id, rp);
        else recurringPayments.put(id, rp);
    }
    public void updateSinglePayment(SinglePayment sp){
        boolean replaced = false;
        if(singlePayments.containsKey(sp.date)){
            for(int i = 0; i < singlePayments.get(sp.date).size(); i++){
                if(singlePayments.get(sp.date).get(i).name.equals(sp.name)){
                    singlePayments.get(sp.date).remove(i);
                    singlePayments.get(sp.date).add(sp);
                    replaced = true;
                    break;
                }
            }

            if(!replaced) {
                singlePayments.get(sp.date).add(sp);
            }
        }
        else {
            singlePayments.put(sp.date, new LinkedList<>());
            singlePayments.get(sp.date).add(sp);
        }

    }

    public BankAccount getBankAccount(long id) { return bankAccounts.get(id);}
    public RecurringPayment getRecurringPayment(long id) { return recurringPayments.get(id);}
    public List<SinglePayment> getSinglePayments(long date) {return singlePayments.get(date);}

    public void removeBankAccount(long id) {bankAccounts.remove(id);}
    public void removeRecurringPayment(long id) {recurringPayments.remove(id);}
    public void removeSinglePayment(long date, String name) {
        if(singlePayments.containsKey(date)) {
            for(int i = 0; i < singlePayments.get(date).size(); i++) {
                if(singlePayments.get(date).get(i).name.equals(name)) {
                    singlePayments.get(date).remove(i);
                    break;
                }
            }
            if(singlePayments.get(date).size() < 1) singlePayments.remove(date);
        }
    }

}
