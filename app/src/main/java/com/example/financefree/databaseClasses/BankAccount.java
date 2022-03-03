package com.example.financefree.databaseClasses;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity
public class BankAccount {
    @PrimaryKey(autoGenerate = true)
    public long bank_id;


    public String accountName;
    public String notes;
}