package com.example.financefree.placeholder;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class RecyclerContent {

    /**
     * An array of sample (placeholder) items.
     */
    public static final List<ListItem> ITEMS = new ArrayList<>();


    private static void addItem(ListItem item) {
        ITEMS.add(item);
    }

    private static ListItem createPlaceholderItem(String name, double amount, String bankName) {
        return new ListItem(name, amount, bankName);
    }


    /**
     * A placeholder item representing a piece of content.
     */
    public static class ListItem {
        public final String name;
        public final double amount;
        public final String bankName;


        public ListItem(String name, double amount, String bankName) {
            this.name = name;
            this.amount = amount;
            this.bankName = bankName;
        }

        @Override
        public String toString() {
            String str = name + " ";
            if(amount > 0) str += "+";
            else str += "-";
            str += "$" + Math.abs(amount);
            return str;
        }
    }
}