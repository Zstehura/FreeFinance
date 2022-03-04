package com.example.financefree.databaseClasses;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.financefree.structures.CustomDate;

@Entity
public class SinglePayment {
    @PrimaryKey(autoGenerate = true)
    public long sp_id;

    public String name;
    public double amount;
    public CustomDate date;
    public long bank_id;
}
