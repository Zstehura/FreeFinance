package com.example.financefree.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class BankAccount {
    @PrimaryKey(autoGenerate = true)
    public long bank_id;

    public String name;
    public String notes;
}
