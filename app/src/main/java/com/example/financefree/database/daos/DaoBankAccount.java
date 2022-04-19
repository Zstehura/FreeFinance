package com.example.financefree.database.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.financefree.database.entities.BankAccount;

import java.util.List;

@Dao
public interface DaoBankAccount {
    @Insert
    long insert(BankAccount ba);
    @Delete
    void delete(BankAccount ba);
    @Update
    void update(BankAccount ba);

    @Query("DELETE FROM BankAccount WHERE bank_id == :id")
    void deleteById(long id);
    @Query("DELETE FROM BankAccount WHERE 1 == 1")
    void deleteAll();
    @Query("SELECT * FROM BankAccount WHERE bank_id == :id")
    BankAccount getBankAccount(long id);
    @Query("SELECT * FROM BankAccount")
    List<BankAccount> getAll();
}
