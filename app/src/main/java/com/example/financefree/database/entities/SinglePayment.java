package com.example.financefree.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class SinglePayment {
    @PrimaryKey(autoGenerate = true)
    public long sp_id;

    public String name;
    public long bank_id;
    public double amount;
    public long date;
    public String notes;
}
