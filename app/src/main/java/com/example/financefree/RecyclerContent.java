package com.example.financefree;

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

    private static ListItem createListItem(String name, double amount, String subName, char cType, long id) {
        return new ListItem(name, amount, subName, cType, id);
    }


    /**
     * A placeholder item representing a piece of content.
     */
    public static class ListItem {
        public final String name;
        public final double amount;
        public final String subName;
        public final char cType;
        public final long id;

        public ListItem(String name, double amount, String subName, char cType, long id) {
            this.name = name;
            this.amount = amount;
            this.subName = subName;
            this.cType = cType;
            this.id = id;
        }

        public String amountToString(){
            String str = "";
            if(amount > 0) str += "+";
            else str += "-";
            str += "$" + Math.abs(amount);
            return str;
        }

        public String limitedDesc(){
            StringBuilder str = new StringBuilder();
            int i = 0;
            while(i < 30 && i < subName.length()){
                str.append(subName.charAt(i));
                i++;
            }
            if (i == 30) str.append("...");
            return str.toString();
        }

        @NonNull
        @Override
        public String toString() {
            return name + " " + amountToString();
        }
    }
}