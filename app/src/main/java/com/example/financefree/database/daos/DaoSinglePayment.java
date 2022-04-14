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

    @Query("DELETE FROM SinglePayment WHERE 1 == 1")
    void deleteAll();
    @Query("SELECT * FROM SinglePayment")
    List<SinglePayment> getAll();
    @Query("SELECT * FROM SinglePayment WHERE date == :date")
    List<SinglePayment> getAllOnDate(long date);
    @Query("SELECT * FROM SinglePayment WHERE bank_id == :bankId AND date >= :date1" +
            " AND date <= :date2")
    List<SinglePayment> getAllForBankBetween(long date1, long date2, long bankId);
    @Query("SELECT * FROM SinglePayment WHERE sp_id == :id LIMIT 1")
    SinglePayment getPayment(long id);
}
