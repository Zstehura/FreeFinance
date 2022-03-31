package com.example.financefree.database.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = {
        @ForeignKey(entity = BankAccount.class,
                parentColumns = "bank_id",
                childColumns = "bank_id",
                onUpdate = ForeignKey.CASCADE,
                onDelete = ForeignKey.CASCADE)})
public class RecurringPayment {
    @PrimaryKey(autoGenerate = true)
    public long rp_id;

    public String name;
    public String notes;
    public long bank_id;
    public double amount;
    public long start_date;
    public long end_date;
    public int type_option;
    public int frequency_number;
}
