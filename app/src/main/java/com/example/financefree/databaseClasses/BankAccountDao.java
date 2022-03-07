package com.example.financefree.databaseClasses;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface BankAccountDao {
    @Query(value = "SELECT * FROM BankAccount")
    List<BankAccount> getAll();

    @Query(value = "SELECT * FROM BankAccount WHERE bank_id == :bankId LIMIT 1")
    BankAccount getById(long bankId);

    @Update
    int updateAccount(BankAccount bankAccount);

    @Insert
    void insertAll(BankAccount... bankAccounts);

    @Delete
    void delete(BankAccount bankAccount);
}
