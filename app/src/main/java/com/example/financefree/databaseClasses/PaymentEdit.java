package com.example.financefree.databaseClasses;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.financefree.datahandlers.CustomDate;

@Entity
public class PaymentEdit {
    @PrimaryKey(autoGenerate = true)
    public long edit_id;

    public long rp_id;
    public String action;
    public CustomDate edit_date;
    public CustomDate move_to_date;
    public double new_amount;
}
