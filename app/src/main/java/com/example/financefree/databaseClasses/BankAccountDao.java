package com.example.financefree.databaseClasses;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface BankAccountDao {
    @Query(value = "SELECT * FROM BankAccount")
    List<BankAccount> getAll();

    @Query(value = "SELECT * FROM BankAccount WHERE bank_id LIKE :bankId LIMIT 1")
    BankAccount getById(String bankId);

    @Insert
    void insertAll(BankAccount... users);

    @Delete
    void delete(BankAccount bankAccount);

}
