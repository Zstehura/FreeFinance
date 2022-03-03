package com.example.financefree.databaseClasses;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.financefree.datahandlers.CustomDate;

import java.util.List;

@Dao
public interface SinglePaymentDao {
    @Query("SELECT * FROM SinglePayment")
    List<SinglePayment> getAll();

    @Query("SELECT * FROM SinglePayment WHERE date LIKE :date")
    List<SinglePayment> getAllByDate(CustomDate date);

    @Query("SELECT * FROM SinglePayment WHERE sp_id LIKE :id")
    SinglePayment getPayment(long id);

    @Query("SELECT * FROM SinglePayment WHERE bank_id LIKE :bank_id")
    List<SinglePayment> getFromBank(long bank_id);

    @Insert
    void insertAll(SinglePayment... singlePayments);

    @Delete
    void delete(SinglePayment singlePayment);
}
