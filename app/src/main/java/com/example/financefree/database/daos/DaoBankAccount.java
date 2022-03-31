package com.example.financefree.database.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.financefree.database.entities.BankAccount;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Maybe;

@Dao
public interface DaoBankAccount {
    @Insert
    Completable insertAll(BankAccount...ba);
    @Delete
    Completable delete(BankAccount ba);
    @Update
    Completable update(BankAccount ba);

    @Query("SELECT * FROM BankAccount WHERE bank_id == :id")
    Maybe<BankAccount> getBankAccount(long id);
    @Query("SELECT * FROM BankAccount")
    Maybe<List<BankAccount>> getAll();
}
