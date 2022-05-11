package com.example.financefree.database.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.financefree.database.entities.NotificationInfo;

import java.util.List;

@Dao
public interface DaoNotificationInfo {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(NotificationInfo ni);
    @Delete
    void delete(NotificationInfo ni);
    @Update
    void update(NotificationInfo ni);


    @Query("SELECT * FROM NotificationInfo")
    List<NotificationInfo> getAll();
    @Query("SELECT * FROM NotificationInfo WHERE payment_id == :paymentId AND payment_type == :paymentType")
    List<NotificationInfo> getNotifByPayment(long paymentId, char paymentType);
    @Query("SELECT * FROM NotificationInfo WHERE notif_time >= :time1 AND notif_time <= :time2")
    List<NotificationInfo> getNotifBetween(long time1, long time2);
}
