package com.example.financefree.databaseClasses;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.financefree.structures.CustomDate;

@Entity
public class PaymentEdit {
    public static final int ACTION_SKIP = 0;
    public static final int ACTION_MOVE_DATE = 1;
    public static final int ACTION_CHANGE_AMOUNT = 2;

    @PrimaryKey(autoGenerate = true)
    public long edit_id;

    public long rp_id;
    public int action;
    public CustomDate edit_date;
    public CustomDate move_to_date;
    public double new_amount;
}
