package com.example.financefree.database.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.financefree.database.entities.RecurringPayment;

import java.util.List;

@Dao
public interface DaoRecurringPayment {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(RecurringPayment rp);
    @Delete
    void delete(RecurringPayment rp);
    @Update
    void update(RecurringPayment rp);

    @Query("SELECT * FROM RecurringPayment")
    List<RecurringPayment> getAll();
    @Query("SELECT * FROM RecurringPayment WHERE rp_id == :id")
    RecurringPayment getRecurringPayment(long id);
    @Query("SELECT * FROM RecurringPayment WHERE bank_id == :id")
    List<RecurringPayment> getBanksRps(long id);
    @Query("SELECT * FROM RecurringPayment WHERE start_date <= :date AND end_date >= :date")
    List<RecurringPayment> getRpsOnDate(long date);
}
