package com.example.financefree.fileClasses;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.financefree.structures.IdMgr;
import com.example.financefree.structures.parseDate;
import com.example.financefree.structures.payment;
import com.example.financefree.structures.statement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;


@SuppressWarnings({"ConstantConditions", "ResultOfMethodCallIgnored"})
public class DataManager {

    /**
    *
    *   App constants and variables
    *
     */

    private static boolean init = false;

    private static final String FILE_SUFFIX = ".dat";
    private static final String BANK_ACCOUNT_PREFIX = "freefinance_ba_";
    private static final String RECURRING_PAYMENT_PREFIX = "freefinance_rp_";
    private static final String SINGLE_PAYMENT_PREFIX = "freefinance_sp_";
    private static final String ID_FILENAME = "freefinance_ids.dat";

    private static final String YEAR_KEY = "year";
    private static final String BANK_ACCOUNT_KEY = "ba";
    private static final String RECURRING_PAYMENT_KEY = "rp";
    private static final String SINGLE_PAYMENT_KEY = "sp";
    private static final String TAX_BRACKET_FILENAME = "freefinance_tbs.dat";

    public static String fileAs = "single";
    public static int memLength = 365;
    private static final List<String> deleteFiles = new LinkedList<>();
    private static final Map<Long, BankAccount> bankAccounts = new HashMap<>();
    private static final Map<Long, RecurringPayment> recurringPayments = new HashMap<>();
    private static final Map<Long, List<SinglePayment>> singlePayments = new HashMap<>();

    /**
    *
    *   IO Functions, initialize data and write when app is ready
    *
     */

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void initData(Context context) throws IOException, JSONException {
        if(!init) {
            String idOut = readFile(context, ID_FILENAME);
            if(!idOut.equals("")) {
                JSONObject idJson = new JSONObject(idOut);


                // cycle through bank account ids first
                JSONArray bankIds = idJson.getJSONArray(BANK_ACCOUNT_KEY);
                for (int i = 0; i < bankIds.length(); i++) {
                    long id = bankIds.getLong(i);
                    IdMgr.registerId(id);
                    JSONObject baJson = new JSONObject(readFile(context, BANK_ACCOUNT_PREFIX + id + FILE_SUFFIX));
                    bankAccounts.put(id, new BankAccount(baJson));
                }

                // cycle through recurring payment ids
                JSONArray rpIds = idJson.getJSONArray(RECURRING_PAYMENT_KEY);
                for (int i = 0; i < rpIds.length(); i++) {
                    long id = rpIds.getLong(i);
                    IdMgr.registerId(id);
                    JSONObject rpJson = new JSONObject(readFile(context, RECURRING_PAYMENT_PREFIX + id + FILE_SUFFIX));
                    recurringPayments.put(id, new RecurringPayment(rpJson));
                }

                // cycle through single payment lists
                JSONArray spDates = idJson.getJSONArray(SINGLE_PAYMENT_KEY);
                for (int i = 0; i < spDates.length(); i++) {
                    long date = spDates.getLong(i);
                    JSONArray spJsonList = new JSONArray(readFile(context, SINGLE_PAYMENT_PREFIX + date + FILE_SUFFIX));
                    List<SinglePayment> l = new LinkedList<>();
                    for (int n = 0; n < spJsonList.length(); n++) {
                        JSONObject j = spJsonList.getJSONObject(n);
                        l.add(new SinglePayment(j));
                    }
                    singlePayments.put(date, l);
                }

                init = true;
            }
            else {
                // ID file doesn't exist, let's make some stuff up
                BankAccount ba = new BankAccount();
                ba.name = "National Bank of Federal National";
                ba.notes = "Default bank account";
                ba.statements.put(parseDate.getLong(new GregorianCalendar()), 100d);
                long bid = insertBankAccount(ba);

                RecurringPayment rp = new RecurringPayment();
                rp.name = "Rent";
                rp.notes = "monthly rent. Notice the amount is a negative because it is a bill";
                Frequency f = new Frequency();
                f.startDate = parseDate.getLong(0,1,2020);
                f.endDate = parseDate.getLong(11,31,2099);
                f.typeOpt = 1;
                f.iNum = 15;
                rp.frequency = f;
                rp.bankId = bid;
                rp.amount = -500d;
                insertRecurringPayment(rp);

                SinglePayment sp = new SinglePayment();
                sp.name = "Fast Food";
                sp.bankId = bid;
                sp.notes = "A single payment that happens once and never again";
                sp.amount = 30d;
                sp.date = parseDate.getLong(new GregorianCalendar());
                insertSinglePayment(sp);

            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void close(Context context) throws JSONException, IOException {
        JSONObject ids = new JSONObject();
        JSONArray baIds = new JSONArray();
        JSONArray rpIds = new JSONArray();
        JSONArray spDates = new JSONArray();

        // Delete old files first
        for(String s: deleteFiles){
            File f = new File(context.getFilesDir(), s);
            f.delete();
        }
        deleteFiles.clear();

        for(long id: bankAccounts.keySet()){
            baIds.put(id);
            JSONObject j = bankAccounts.get(id).toJSON();
            writeFile(context, BANK_ACCOUNT_PREFIX + id + FILE_SUFFIX, j.toString());
        }

        for(long id: recurringPayments.keySet()) {
            rpIds.put(id);
            JSONObject j = recurringPayments.get(id).toJSON();
            writeFile(context, RECURRING_PAYMENT_PREFIX + id + FILE_SUFFIX, j.toString());
        }

        for(long date: singlePayments.keySet()) {
            spDates.put(date);
            JSONArray j = new JSONArray();
            for (SinglePayment sp: singlePayments.get(date)){
                j.put(sp.toJSON());
            }
            writeFile(context, SINGLE_PAYMENT_PREFIX + date + FILE_SUFFIX, j.toString());
        }

        ids.put(BANK_ACCOUNT_KEY, baIds);
        ids.put(SINGLE_PAYMENT_KEY, spDates);
        ids.put(RECURRING_PAYMENT_KEY, rpIds);
        writeFile(context,ID_FILENAME, ids.toString());

        clearOldData();
        deleteFiles.clear();
    }

    private static void writeFile(Context context,String fn, String out) throws IOException {
        FileOutputStream fos = context.openFileOutput(fn, Context.MODE_PRIVATE);
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
        bw.write(out);
        bw.flush();
        bw.close();
        fos.flush();
        fos.close();
    }

    private static String readFile(Context context, String fn) {
        FileInputStream fis;
        StringBuilder sb = new StringBuilder();
        try {
            fis = context.openFileInput(fn);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            String txt = br.readLine();
            while(txt != null) {
                sb.append(txt);
                txt = br.readLine();
            }
            br.close();
            fis.close();
        } catch (IOException e) {
            Log.e("IO Error", e.getMessage());
            try {
                FileOutputStream fos = context.openFileOutput(fn, Context.MODE_PRIVATE);
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
                bw.write("");
                fos.flush();
                fos.close();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }

        }
        return sb.toString();
    }

    /**
    *
    *   public accessor functions
    *
     */

    public static Map<Long, BankAccount> getAllBankAccounts() {return bankAccounts;}
    public static Map<Long, RecurringPayment> getAllRecurringPayments() {return recurringPayments;}
    public static Map<Long,List<SinglePayment>> getAllSinglePayments() {return singlePayments;}

    public static List<Long> getBankIds() {return new LinkedList<>(bankAccounts.keySet());}
    public static List<Long> getRecurringIds() {return new LinkedList<>(recurringPayments.keySet());}

    public static long insertBankAccount(BankAccount ba) {
        long id = IdMgr.genNewId();
        bankAccounts.put(id,ba);
        return id;
    }
    public static long insertRecurringPayment(RecurringPayment rp) {
        long id = IdMgr.genNewId();
        recurringPayments.put(id, rp);
        return id;
    }
    public static void insertSinglePayment(SinglePayment sp) {
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
    public static void updateBankAccount(BankAccount ba, long id) {
        if(bankAccounts.containsKey(id)) bankAccounts.replace(id, ba);
        else bankAccounts.put(id,ba);
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void updateRecurringPayment(RecurringPayment rp, long id){
        if(recurringPayments.containsKey(id)) recurringPayments.replace(id, rp);
        else recurringPayments.put(id, rp);
    }
    public static void updateSinglePayment(SinglePayment sp){
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

    public static BankAccount getBankAccount(long id) { return bankAccounts.get(id);}
    public static RecurringPayment getRecurringPayment(long id) { return recurringPayments.get(id);}
    public static List<SinglePayment> getSinglePayments(long date) {return singlePayments.get(date);}

    public static void removeBankAccount(long id) {
        IdMgr.releaseId(id);
        bankAccounts.remove(id);
        deleteFiles.add(BANK_ACCOUNT_PREFIX + id + FILE_SUFFIX);
    }
    public static void removeRecurringPayment(long id) {
        IdMgr.releaseId(id);
        recurringPayments.remove(id);
        deleteFiles.add(RECURRING_PAYMENT_PREFIX + id + FILE_SUFFIX);
    }
    public static void removeSinglePayment(long date, String name) {
        if(singlePayments.containsKey(date)) {
            for(int i = 0; i < singlePayments.get(date).size(); i++) {
                if(singlePayments.get(date).get(i).name.equals(name)) {
                    singlePayments.get(date).remove(i);
                    break;
                }
            }
            if(singlePayments.get(date).size() < 1) {
                singlePayments.remove(date);
                deleteFiles.add(SINGLE_PAYMENT_PREFIX + date + FILE_SUFFIX);
            }
        }
    }

    /**
    *
    * Complex data retrieval
    *
     */

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static List<payment> getPaymentsOnDate(long date) {
        List<payment> l = new LinkedList<>();

        if(getSinglePayments(date) != null) {
            for (SinglePayment sp : getSinglePayments(date)) {
                payment p = new payment(sp.amount, sp.date, sp.name, sp.bankId, 's', 0);
                l.add(p);
            }
        }

        for(long id: recurringPayments.keySet()){
            RecurringPayment rp = getRecurringPayment(id);
            if(parseDate.dateIncludedInRp(rp, date)){
                payment p = new payment(rp.amount, date, rp.name, rp.bankId, 'r', id);
                for(PaymentEdit pe: rp.edits.values()) {
                    if(pe.editDate == date || pe.newDate == date) {
                        if(pe.newBankId != 0) p.bankId = pe.newBankId;
                        if(pe.newAmount != 0)p.amount = pe.newAmount;
                    }
                }
                l.add(p);
            }
        }

        return l;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static statement getStatementOnDate(long date, long bankId){
        BankAccount ba = getBankAccount(bankId);
        statement s;
        if(ba.statements.containsKey(date)){
            s = new statement(bankId, ba.statements.get(date), date, ba.name);
        }
        else {
            // Find last available statement
            double lastVal = 0;
            long lastDate = parseDate.dateNumDaysAgo(memLength);
            for (long d : ba.statements.keySet()) {
                if (d > lastDate && d < date) {
                    lastDate = d;
                    lastVal = ba.statements.get(d);
                }
            }
            // Calculate forward from there
            while (lastDate <= date) {
                for (payment p : getPaymentsOnDate(lastDate)) {
                    if (p.bankId == bankId) lastVal += p.amount;
                }
                lastDate++;
            }
            s = new statement(bankId, lastVal, date, ba.name);
        }
        return s;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static List<statement> getStatementsOnDate(long date){
        List<statement> l = new LinkedList<>();

        for(long id: bankAccounts.keySet()) {
            l.add(getStatementOnDate(date, id));
        }
        return l;
    }
    public static Map<Long, String> getBankMap() {
        Map<Long, String> m = new HashMap<>();
        for(long id: bankAccounts.keySet()){
            m.put(id, bankAccounts.get(id).name);
        }
        return m;
    }
    public static String getBankName(long id) {
        return getBankMap().get(id);
    }

    /**
     *
     * Data Clearing Methods
     *
     */

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void clearOldData() {
        // never clear bank accounts, just the statements
        // only clear recurring payments that have ended in the period
        // Clear out all single payments old enough
        long date = parseDate.dateNumDaysAgo(memLength);

        for(long id: bankAccounts.keySet()) {
            BankAccount ba = getBankAccount(id);
            for(long sDate: ba.statements.keySet()){
                if(sDate <= date) {
                    ba.statements.remove(sDate);
                }
            }
            updateBankAccount(ba, id);
        }

        for(long id: recurringPayments.keySet()) {
            RecurringPayment rp = getRecurringPayment(id);
            if(rp.frequency.endDate <= date) {
                removeRecurringPayment(id);
            }
            else {
                // check edits for old stuff
                for(long rDate: rp.edits.keySet()) {
                    if(rDate <= date) {
                        rp.edits.remove(rDate);
                    }
                }
                updateRecurringPayment(rp, id);
            }
        }

        for(long sDate: singlePayments.keySet()) {
            if(sDate <= date) {
                singlePayments.remove(sDate);
                deleteFiles.add(SINGLE_PAYMENT_PREFIX + sDate + FILE_SUFFIX);
            }
        }

    }
    public static void clearAllData() {
        killBankAccounts();
        killRecurringPayments();
        killSinglePayments();
    }

    private static void killSinglePayments() {
        if(singlePayments.size() > 0){
            long date = (long) singlePayments.keySet().toArray()[0];
            String name = singlePayments.get(date).get(0).name;
            removeSinglePayment(date,name);
            killSinglePayments();
        }
    }
    private static void killRecurringPayments() {
        if(recurringPayments.size() > 0){
            long id = (long) recurringPayments.keySet().toArray()[0];
            removeRecurringPayment(id);
            killRecurringPayments();
        }
    }
    private static void killBankAccounts() {
        if(bankAccounts.size() > 0){
            long id = (long) bankAccounts.keySet().toArray()[0];
            removeBankAccount(id);
            killBankAccounts();
        }
    }
}
