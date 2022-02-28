package com.example.financefree.databaseClasses;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity
public class BankAccount {
    @PrimaryKey
    public String bank_id;

    @ColumnInfo(name = "notes")
    public String notes;
}