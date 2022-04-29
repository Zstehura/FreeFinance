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

    private static final List<BankAccountRVItem> ITEMS = new ArrayList<>();
    public static final Map<Long, BankAccountRVItem> ITEM_MAP = new HashMap<>();

    public static List<BankAccountRVItem> getItems(){return ITEMS;}
    public static List<BankAccountRVItem> getItems(List<BankAccount> bankAccountList){
        ITEMS.clear();
        for(BankAccount ba: bankAccountList){
            addItem(createRvItem(ba));
        }
        return ITEMS;
    }

    private static void addItem(BankAccountRVItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.itemId, item);
    }
    public static void addItem(BankAccount item) {
        addItem(createRvItem(item));
    }
    
    public static void updateItem(BankAccount item) {
        for(int i = 0; i < ITEMS.size(); i++){
            if(ITEMS.get(i).itemId == item.bank_id){
                ITEM_MAP.replace(item.bank_id, createRvItem(item));
                ITEMS.set(i, ITEM_MAP.get(item.bank_id));
            }
        }
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
        public final long itemId;
        public final String name;
        public final String details;

        public BankAccountRVItem(long itemId, String name, String details) {
            this.itemId = itemId;
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