package com.example.financefree.databaseClasses;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.financefree.datahandlers.CustomDate;

import java.util.List;

@Dao
public interface RecurringPaymentDao {
    @Query("SELECT * FROM RecurringPayment")
    List<RecurringPayment> getAll();

    @Query("SELECT * FROM RecurringPayment WHERE rp_id LIKE :id")
    RecurringPayment getRecurringPayment(long id);

    @Query("SELECT * FROM RecurringPayment WHERE start >= :customDate AND endDate <= :customDate")
    List<RecurringPayment> getFromDate(CustomDate customDate);

    @Insert
    void insertAll(RecurringPayment... recurringPayments);

    @Delete
    void delete(RecurringPayment recurringPayment);
}
