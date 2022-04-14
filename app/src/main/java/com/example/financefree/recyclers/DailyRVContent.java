package com.example.financefree.recyclers;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.example.financefree.structures.Payment;
import com.example.financefree.structures.Statement;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 */
public class DailyRVContent {

    private static final List<DailyRVItem> ITEMS = new ArrayList<>();
    public static final Map<Long, DailyRVItem> ITEM_MAP = new HashMap<>();

    public static List<DailyRVItem> getItems(){return ITEMS;}
    public static List<DailyRVItem> getItems(List<Statement> statementList, List<Payment> paymentList){
        ITEMS.clear();
        for(Statement s: statementList){
            addItem(createRVItem(s));
        }
        for (Payment p: paymentList){
            addItem(createRVItem(p));
        }
        return ITEMS;
    }

    private static void addItem(DailyRVItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.itemId, item);
    }

    public static void addItem(Payment item) {
        addItem(createRVItem(item));
    }
    public static void addItem(Statement item){
        addItem(createRVItem(item));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void updateItem(Payment item) {
        for(int i = 0; i < ITEMS.size(); i++){
            if(ITEMS.get(i).itemId == (item.id * -1)){
                ITEM_MAP.replace((item.id * -1), createRVItem(item));
                ITEMS.set(i, ITEM_MAP.get(item.id * -1));
            }
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void updateItem(Statement item) {
        for(int i = 0; i < ITEMS.size(); i++){
            if(ITEMS.get(i).itemId == item.bankId){
                ITEM_MAP.replace(item.bankId, createRVItem(item));
                ITEMS.set(i, ITEM_MAP.get(item.bankId));
            }
        }
    }

    private static DailyRVItem createRVItem(Statement statement){
        return new DailyRVItem(statement.bankId, statement.bankName, statement.amount, false,false, statement.isCalculated);
    }
    private static DailyRVItem createRVItem(Payment payment) {
        return new DailyRVItem((-1 * payment.id), payment.name, payment.amount, true, (payment.cType == 'r'), false);
    }

    public static class DailyRVItem {
        private final long itemId;
        public final boolean isPayment;
        public final boolean isRecurring;
        public final String name;
        public final String details;
        public final double amount;
        public final boolean isCalculated;

        public DailyRVItem(long itemId, String name, double details, boolean isPayment, boolean isRecurring, boolean isCalculated) {
            this.itemId = itemId;
            this.name = name;
            this.amount = details;
            NumberFormat f = NumberFormat.getCurrencyInstance();
            this.details = f.format(details);
            this.isPayment = isPayment;
            this.isRecurring = isRecurring;
            this.isCalculated = isCalculated;
        }

        public long getItemId() {
            if(isPayment) return itemId * -1;
            else return itemId;
        }

        @NonNull
        @Override
        public String toString() {
            return name;
        }
    }
}