package com.example.financefree.database.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.financefree.database.entities.PaymentEdit;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Maybe;

@Dao
public interface DaoPaymentEdit {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertAll(PaymentEdit...pe);
    @Delete
    Completable delete(PaymentEdit pe);
    @Update
    Completable update(PaymentEdit pe);

    @Query("SELECT * FROM PaymentEdit")
    Maybe<List<PaymentEdit>> getAll();
    @Query("SELECT * FROM PaymentEdit WHERE (new_date == :date || edit_date == :date)")
    Maybe<List<PaymentEdit>> getAllOnDate(long date);
    @Query("SELECT * FROM PaymentEdit WHERE rp_id == :id")
    Maybe<List<PaymentEdit>> getAllForRp(long id);
    @Query("SELECT * FROM PaymentEdit WHERE rp_id == :id AND (new_date == :date || edit_date == :date)")
    Maybe<List<PaymentEdit>> getEditsForRpOnDate(long date, long id);
    @Query("SELECT * FROM PaymentEdit WHERE edit_id == :id")
    Maybe<PaymentEdit> getEdit(long id);
}
