package com.example.financefree.database.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = {
        @ForeignKey(entity = BankAccount.class,
                parentColumns = "bank_id",
                childColumns = "bank_id",
                onUpdate = ForeignKey.CASCADE,
                onDelete = ForeignKey.CASCADE)},
        indices = {@Index(value = "bank_id", name = "recur_bank_id")})
public class RecurringPayment {
    @PrimaryKey(autoGenerate = true)
    public long rp_id;

    public String name;
    public String notes;
    public Long bank_id;
    public double amount;
    public long start_date;
    public long end_date;
    public int type_option;
    public int frequency_number;
}
