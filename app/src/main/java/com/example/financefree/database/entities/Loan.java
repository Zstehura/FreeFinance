package com.example.financefree.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Map;

@Entity
public class Loan {
    @PrimaryKey (autoGenerate = true)
    public long loan_id;

    public Map<Long, Double> payments;
    public String name;
    public String notes;
    public long start_date;
    public int frequency_type;
    public int frequency_num;
    public double principle;
    public double apr;
    public int term_Length;
}
