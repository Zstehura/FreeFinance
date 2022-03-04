package com.example.financefree.databaseClasses;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.financefree.structures.CustomDate;

@Entity
public class RecurringPayment {
    public static final int ON = 1;
    public static final int EVERY = 2;

    @PrimaryKey(autoGenerate = true)
    public long rp_id;

    public String name;
    public int frequencyType;
    public int frequency;
    public double amount;
    public CustomDate start;
    public CustomDate endDate;
    public String notes;
    public long bankId;
}
