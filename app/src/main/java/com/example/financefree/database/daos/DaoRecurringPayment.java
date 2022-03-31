package com.example.financefree.database.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.financefree.database.entities.RecurringPayment;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Maybe;

@Dao
public interface DaoRecurringPayment {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertAll(RecurringPayment...rp);
    @Delete
    Completable delete(RecurringPayment rp);
    @Update
    Completable update(RecurringPayment rp);

    @Query("SELECT * FROM RecurringPayment")
    Maybe<List<RecurringPayment>> getAll();
    @Query("SELECT * FROM RecurringPayment WHERE rp_id == :id")
    Maybe<RecurringPayment> getRecurringPayment(long id);
    @Query("SELECT * FROM RecurringPayment WHERE bank_id == :id")
    Maybe<List<RecurringPayment>> getBanksRps(long id);
    @Query("SELECT * FROM RecurringPayment WHERE start_date <= :date AND end_date >= :date")
    Maybe<List<RecurringPayment>> getRpsOnDate(long date);
}
