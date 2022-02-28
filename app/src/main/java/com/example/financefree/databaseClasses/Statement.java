package com.example.financefree.databaseClasses;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.financefree.datahandlers.CustomDate;

@Entity
public class Statement {
    @PrimaryKey
    public String s_id;

    public double amount;
    public CustomDate date;
    public String bank_id;
}
