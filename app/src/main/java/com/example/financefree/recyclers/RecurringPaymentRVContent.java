package com.example.financefree.recyclers;


import androidx.annotation.NonNull;


import com.example.financefree.database.entities.RecurringPayment;

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
public class RecurringPaymentRVContent {

    public static final List<RecurringPaymentRVItem> ITEMS = new ArrayList<>();
    public static final Map<Long, RecurringPaymentRVItem> ITEM_MAP = new HashMap<>();

    public static List<RecurringPaymentRVItem> getItems(){return ITEMS;}
    public static List<RecurringPaymentRVItem> getItems(List<RecurringPayment> recurringPaymentList){
        ITEMS.clear();
        for(RecurringPayment rp: recurringPaymentList) {
            addItem(createRVItem(rp));
        }
        return ITEMS;
    }

    private static void addItem(RecurringPaymentRVItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }
    public static void addItem(RecurringPayment item){
        addItem(createRVItem(item));
    }
    
    public static void updateItem(RecurringPayment rp) {
        for(int i = 0; i < ITEMS.size(); i++){
            if(ITEMS.get(i).id == rp.rp_id){
                ITEM_MAP.replace(rp.rp_id, createRVItem(rp));
                ITEMS.set(i, ITEM_MAP.get(rp.rp_id));
            }
        }
    }
    private static RecurringPaymentRVItem createRVItem(RecurringPayment rp) {
        NumberFormat f = NumberFormat.getCurrencyInstance();
        return new RecurringPaymentRVItem(rp.rp_id, rp.name, f.format(rp.amount));
    }

    public static class RecurringPaymentRVItem {
        public final long id;
        public final String name;
        public final String details;

        public RecurringPaymentRVItem(long id, String name, String details) {
            this.id = id;
            this.name = name;
            this.details = details;
        }

        @NonNull
        @Override
        public String toString() {
            return name;
        }
    }
}