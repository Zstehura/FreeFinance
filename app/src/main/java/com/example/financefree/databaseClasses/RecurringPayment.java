package com.example.financefree.databaseClasses;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.financefree.datahandlers.CustomDate;

@Entity
public class RecurringPayment {
    public static final int ON = 1;
    public static final int EVERY = 2;

    @PrimaryKey(autoGenerate = true)
    public long rp_id;

    public long bankId;
    public String notes;
    public int frequencyType;
    public int frequency;
    public double amount;
    public CustomDate start;
    public CustomDate end;

}
