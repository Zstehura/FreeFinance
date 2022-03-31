package com.example.financefree.database.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.financefree.database.entities.SinglePayment;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Maybe;

@Dao
public interface DaoSinglePayment {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertAll(SinglePayment...sp);
    @Delete
    Completable delete(SinglePayment sp);
    @Update
    Completable update(SinglePayment sp);

    @Query("SELECT * FROM SinglePayment")
    Maybe<List<SinglePayment>> getAll();
    @Query("SELECT * FROM SinglePayment WHERE sp_id == :id")
    Maybe<SinglePayment> getPayment(long id);
    @Query("SELECT * FROM SinglePayment WHERE bank_id == :id")
    Maybe<List<SinglePayment>> getAllForBank(long id);
    @Query("SELECT * FROM SinglePayment WHERE date == :date")
    Maybe<List<SinglePayment>> getAllOnDate(long date);
    @Query("SELECT * FROM SinglePayment WHERE date == :date AND bank_id == :id")
    Maybe<List<SinglePayment>> getAllOnDateForBank(long id, long date);
}
