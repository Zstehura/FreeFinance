package com.example.financefree.databaseClasses;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class BankStatement {
    @PrimaryKey(autoGenerate = true)
    public long s_id;

    public double amount;
    public long date;
    public long bank_id;
}
