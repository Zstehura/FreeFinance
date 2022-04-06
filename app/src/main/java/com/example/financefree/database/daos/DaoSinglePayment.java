package com.example.financefree.database.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.financefree.database.entities.SinglePayment;

import java.util.List;

@Dao
public interface DaoSinglePayment {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(SinglePayment sp);
    @Delete
    void delete(SinglePayment sp);
    @Update
    void update(SinglePayment sp);

    @Query("SELECT * FROM SinglePayment")
    List<SinglePayment> getAll();
    @Query("SELECT * FROM SinglePayment WHERE sp_id == :id")
    SinglePayment getPayment(long id);
    @Query("SELECT * FROM SinglePayment WHERE bank_id == :id")
    List<SinglePayment> getAllForBank(long id);
    @Query("SELECT * FROM SinglePayment WHERE date == :date")
    List<SinglePayment> getAllOnDate(long date);
    @Query("SELECT * FROM SinglePayment WHERE date == :date AND bank_id == :id")
    List<SinglePayment> getAllOnDateForBank(long id, long date);
}
