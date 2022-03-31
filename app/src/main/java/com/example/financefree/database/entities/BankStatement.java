package com.example.financefree.database.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = {@ForeignKey(
        entity = BankAccount.class,
        parentColumns = "bank_id",
        childColumns = "bank_id",
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
)},
indices = {@Index(value = "bank_id", name = "bank_ids")})

public class BankStatement {
    @PrimaryKey(autoGenerate = true)
    public long statement_id;

    public long bank_id;
    public long date;
    public double amount;
}
