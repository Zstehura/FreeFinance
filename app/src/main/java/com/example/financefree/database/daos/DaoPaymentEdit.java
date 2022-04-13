package com.example.financefree.database.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.financefree.database.entities.PaymentEdit;

import java.util.List;

@Dao
public interface DaoPaymentEdit {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(PaymentEdit pe);
    @Delete
    void delete(PaymentEdit pe);
    @Update
    void update(PaymentEdit pe);

    @Query("DELETE FROM PaymentEdit WHERE 1 == 1")
    void deleteAll();
    @Query("SELECT * FROM PaymentEdit")
    List<PaymentEdit> getAll();
    @Query("SELECT * FROM PaymentEdit WHERE (new_date == :date || edit_date == :date)")
    List<PaymentEdit> getAllOnDate(long date);
    @Query("SELECT * FROM PaymentEdit WHERE rp_id == :id")
    List<PaymentEdit> getAllForRp(long id);
    @Query("SELECT * FROM PaymentEdit WHERE rp_id == :id AND (new_date == :date OR edit_date == :date)")
    List<PaymentEdit> getEditsForRpOnDate(long date, long id);
    @Query("SELECT * FROM PaymentEdit WHERE edit_id == :id")
    PaymentEdit getEdit(long id);
    @Query("SELECT * FROM PaymentEdit WHERE rp_id == :rpId AND (" +
            "(edit_date >= :date1 AND edit_date <= :date2) OR" +
            "(new_date >= :date1 AND new_date <= :date2))")
    List<PaymentEdit> getEditsForRpBetween(long date1, long date2, long rpId);
}
