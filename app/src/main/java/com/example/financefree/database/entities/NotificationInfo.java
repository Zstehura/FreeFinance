package com.example.financefree.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class NotificationInfo {
    @PrimaryKey
    public String note_id;

    public int frequency_type;
    public int frequency_num;
    public String message;
    public long payment_id;
    public char payment_type;   // r or s
}
