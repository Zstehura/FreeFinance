package com.example.financefree.recyclers;

import androidx.annotation.NonNull;

import com.example.financefree.database.entities.BankAccount;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 */
public class BankAccountRVContent {

    /**
     * An array of sample (placeholder) items.
     */
    private static final List<BankAccountRVItem> ITEMS = new ArrayList<>();

    /**
     * A map of sample (placeholder) items, by ID.
     */
    public static final Map<Long, BankAccountRVItem> ITEM_MAP = new HashMap<>();

    public static List<BankAccountRVItem> getItems(List<BankAccount> bankAccountList){
        for(BankAccount ba: bankAccountList){
            addItem(createRvItem(ba));
        }

        return ITEMS;
    }

    private static void addItem(BankAccountRVItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static BankAccountRVItem createRvItem(BankAccount eba) {
        int charLim = 25;
        String desc = eba.notes;
        if(desc.length() > charLim) {
            StringBuilder str = new StringBuilder();
            for(int i = 0; i < charLim; i++){
                str.append(desc.charAt(i));
            }
            desc = str.toString();
        }

        return new BankAccountRVItem(eba.bank_id , eba.name, desc);
    }

    public static class BankAccountRVItem {
        public final long id;
        public final String name;
        public final String details;

        public BankAccountRVItem(long id, String name, String details) {
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