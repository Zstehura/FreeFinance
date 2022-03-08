package com.example.financefree.databaseClasses;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface PaymentEditDao {
    @Query("SELECT * FROM PaymentEdit LIMIT 1")
    PaymentEdit testDb();

    @Query("SELECT * FROM PaymentEdit")
    List<PaymentEdit> getAll();

    @Query("SELECT * FROM PaymentEdit WHERE edit_id == :id")
    PaymentEdit getEdit(long id);

    @Query("SELECT * FROM PaymentEdit WHERE rp_id == :rp_id")
    List<PaymentEdit> getEditsByPayment(long rp_id);

    @Query("SELECT * FROM PaymentEdit WHERE rp_id == :rp_id AND edit_date == :date")
    List<PaymentEdit> getEditsByDate(long rp_id, long date);

    @Update
    void updateAccount(PaymentEdit paymentEdit);

    @Insert
    void insertAll(PaymentEdit... paymentEdits);

    @Delete
    void delete(PaymentEdit paymentEdit);

}
