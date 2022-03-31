package com.example.financefree.database.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = {
        @ForeignKey(entity = RecurringPayment.class,
                parentColumns = "rp_id",
                childColumns = "rp_id",
                onDelete = ForeignKey.CASCADE,
                onUpdate = ForeignKey.CASCADE)},
indices = {@Index(value = "rp_id", name = "rp_ids"), @Index(value = "edit_date", name = "edit_dates")})
public class PaymentEdit {
    @PrimaryKey(autoGenerate = true)
    public long edit_id;

    public long rp_id;
    public long edit_date;
    public long new_date;
    public boolean skip;
    public double new_amount;
    public long new_bank_id;
}
