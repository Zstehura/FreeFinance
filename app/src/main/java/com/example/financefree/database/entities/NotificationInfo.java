package com.example.financefree.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class NotificationInfo {
    @PrimaryKey (autoGenerate = true)
    public long notif_id;

    public long notif_time;
    public String title;
    public String message;
    public long payment_id;
    public char payment_type;   // r or s
}
