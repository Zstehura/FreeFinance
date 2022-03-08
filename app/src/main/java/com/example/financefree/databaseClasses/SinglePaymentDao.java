package com.example.financefree.databaseClasses;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface SinglePaymentDao {
    @Query("SELECT * FROM SinglePayment LIMIT 1")
    SinglePayment testDb();

    @Query("SELECT * FROM SinglePayment")
    List<SinglePayment> getAll();

    @Query("SELECT * FROM SinglePayment WHERE date == :date")
    List<SinglePayment> getAllByDate(long date);

    @Query("SELECT * FROM SinglePayment WHERE sp_id == :id")
    SinglePayment getPayment(long id);

    @Query("SELECT * FROM SinglePayment WHERE bank_id == :bank_id")
    List<SinglePayment> getFromBank(long bank_id);

    @Update
    void updateAccount(SinglePayment singlePayment);

    @Insert
    void insertAll(SinglePayment... singlePayments);

    @Delete
    void delete(SinglePayment singlePayment);
}
