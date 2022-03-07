package com.example.financefree.databaseClasses;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class PaymentEdit {
    public static final int ACTION_SKIP = 0;
    public static final int ACTION_MOVE_DATE = 1;
    public static final int ACTION_CHANGE_AMOUNT = 2;

    @PrimaryKey(autoGenerate = true)
    public long edit_id;

    public long rp_id;
    public int action;
    public long edit_date;
    public long move_to_date;
    public double new_amount;
}
