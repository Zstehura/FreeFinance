package com.example.financefree.databaseClasses;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface BankAccountDao {
    @Query("SELECT * FROM BankAccount LIMIT 1")
    Maybe<BankAccount> testDb();

    @Query(value = "SELECT * FROM BankAccount")
    Flowable<List<BankAccount>> getAll();

    @Query(value = "SELECT * FROM BankAccount WHERE bank_id == :bankId LIMIT 1")
    Single<BankAccount> getById(long bankId);

    @Query(value = "SELECT accountName FROM BankAccount WHERE bank_id == :bankId LIMIT 1")
    Single<String> getName(long bankId);

    @Query("DELETE FROM BankAccount WHERE 1 == 1")
    Completable deleteAll();

    @Query("SELECT bank_id FROM BankAccount WHERE 1 == 1")
    Single<List<Long>> getAllBankIDs();

    @Update
    Completable updateAccount(BankAccount bankAccount);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertAll(BankAccount... bankAccounts);

    @Delete
    Completable delete(BankAccount bankAccount);
}
