package com.example.financefree.databaseClasses;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface RecurringPaymentDao {
    @Query("SELECT * FROM RecurringPayment")
    List<RecurringPayment> getAll();

    @Query("SELECT * FROM RecurringPayment WHERE rp_id == :id")
    RecurringPayment getRecurringPayment(long id);

    @Query("SELECT * FROM RecurringPayment WHERE startDate <= :date AND endDate >= :date")
    List<RecurringPayment> getFromDate(long date);

    @Update
    void updateAccount(RecurringPayment recurringPayment);

    @Insert
    void insertAll(RecurringPayment... recurringPayments);

    @Delete
    void delete(RecurringPayment recurringPayment);
}
