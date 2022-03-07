package com.example.financefree.databaseClasses;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

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
    public long startDate;
    public long endDate;
    public String notes;
    public long bankId;
}
