package com.example.financefree.databaseClasses;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface PaymentEditDao {

    @Query("SELECT * FROM PaymentEdit")
    List<PaymentEdit> getAll();

    @Query("SELECT * FROM PaymentEdit WHERE edit_id LIKE :id")
    PaymentEdit getEdit(long id);

    @Query("SELECT * FROM PaymentEdit WHERE rp_id LIKE :rp_id")
    List<PaymentEdit> getEditsByPayment(long rp_id);

    @Insert
    void insertAll(PaymentEdit... paymentEdits);

    @Delete
    void delete(PaymentEdit paymentEdit);

}
