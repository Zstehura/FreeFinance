package com.example.financefree.databaseClasses;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.financefree.structures.CustomDate;

@Entity
public class BankStatement {
    @PrimaryKey(autoGenerate = true)
    public long s_id;

    public double amount;
    public CustomDate date;
    public long bank_id;
}
