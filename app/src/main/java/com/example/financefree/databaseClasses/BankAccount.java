package com.example.financefree.databaseClasses;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class BankAccount {
    @PrimaryKey(autoGenerate = true)
    public long bank_id;

    public String accountName;
    public String notes;
}