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

    @Query("SELECT * FROM RecurringPayment WHERE start_date <= :date2 AND end_date >= :date1")
    List<RecurringPayment> getRpsBetween(long date1, long date2);
    @Query("DELETE FROM RecurringPayment WHERE 1 == 1")
    void deleteAll();
    @Query("SELECT * FROM RecurringPayment")
    List<RecurringPayment> getAll();
    @Query("SELECT * FROM RecurringPayment WHERE rp_id == :id")
    RecurringPayment getRecurringPayment(long id);
    @Query("DELETE FROM RecurringPayment WHERE rp_id == :id")
    void deleteById(long id);
    @Query("SELECT * FROM RecurringPayment WHERE start_date <= :date AND end_date >= :date")
    List<RecurringPayment> getRpsOnDate(long date);
    @Query("SELECT * FROM RecurringPayment WHERE start_date <= :date2 AND end_date >= :date1" +
            " AND bank_id == :bankId")
    List<RecurringPayment> getRpsForBankBetween(long date1, long date2, long bankId);

    @Query("DELETE FROM RecurringPayment WHERE end_date < :date")
    void deleteOlderThan(long date);
}
