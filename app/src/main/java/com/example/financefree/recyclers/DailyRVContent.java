package com.example.financefree.recyclers;

import android.graphics.Color;

import androidx.annotation.NonNull;


import com.example.financefree.R;
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
    public static final int SINGLE_PAYMENT_CLR = R.color.blue;
    public static final int RECURRING_PAYMENT_CLR = R.color.dark_red;
    public static final int BANK_STATEMENT_CLR = R.color.dark_green;

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

    
    public static void updateItem(Payment item) {
        for(int i = 0; i < ITEMS.size(); i++){
            if(ITEMS.get(i).itemId == (item.id * -1)){
                ITEM_MAP.replace((item.id * -1), createRVItem(item));
                ITEMS.set(i, ITEM_MAP.get(item.id * -1));
            }
        }
    }
    
    public static void updateItem(Statement item) {
        for(int i = 0; i < ITEMS.size(); i++){
            if(ITEMS.get(i).itemId == item.bankId){
                ITEM_MAP.replace(item.bankId, createRVItem(item));
                ITEMS.set(i, ITEM_MAP.get(item.bankId));
            }
        }
    }

    private static DailyRVItem createRVItem(Statement statement){
        return new DailyRVItem(statement.id, statement.bankName, statement.amount, false, statement.bankId, false, statement.isCalculated, BANK_STATEMENT_CLR);
    }
    private static DailyRVItem createRVItem(Payment payment) {
        int color = SINGLE_PAYMENT_CLR;
        if(payment.cType == 'r') color = RECURRING_PAYMENT_CLR;
        return new DailyRVItem((-1 * payment.id), payment.name, payment.amount, true, payment.bankId, (payment.cType == 'r'), false, color);
    }

    public static class DailyRVItem {
        private final long itemId;
        public final boolean isPayment;
        public final boolean isRecurring;
        public final String name;
        public final String details;
        public final long bankId;
        public final double amount;
        public final boolean isCalculated;
        public final int color;

        public DailyRVItem(long itemId, String name, double details, boolean isPayment, long bankId, boolean isRecurring, boolean isCalculated, int color) {
            this.itemId = itemId;
            this.name = name;
            this.bankId = bankId;
            this.amount = details;
            NumberFormat f = NumberFormat.getCurrencyInstance();
            this.details = f.format(details);
            this.isPayment = isPayment;
            this.isRecurring = isRecurring;
            this.isCalculated = isCalculated;
            this.color = color;
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