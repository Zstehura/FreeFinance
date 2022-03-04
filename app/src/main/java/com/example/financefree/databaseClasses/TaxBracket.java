package com.example.financefree.databaseClasses;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class TaxBracket {
    @PrimaryKey (autoGenerate = true)
    public long bracket_id;

    public int year;
    public double percentage;
    public long upperLimit;
    public long lowerLimit;
    public String filingAs;
}
