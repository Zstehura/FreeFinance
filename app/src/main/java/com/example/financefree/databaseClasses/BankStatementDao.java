package com.example.financefree.databaseClasses;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.financefree.structures.CustomDate;

import java.util.List;

@Dao
public interface BankStatementDao {

    @Query("SELECT * FROM BankStatement")
    List<BankStatement> getAll();

    @Query("SELECT * FROM BankStatement WHERE bank_id LIKE :id")
    List<BankStatement> getByBankId(long id);

    @Query("SELECT * FROM BankStatement WHERE bank_id LIKE :id AND date <= :date")
    List<BankStatement> getAllBefore(long id, CustomDate date);

    @Query("SELECT * FROM BankStatement WHERE bank_id LIKE :id AND date >= :date")
    List<BankStatement> getAllAfter(long id, CustomDate date);

    @Query("SELECT * FROM BankStatement WHERE bank_id LIKE :id AND date <= :date2 AND date >= :date1")
    List<BankStatement> getAllBetween(long id, CustomDate date1, CustomDate date2);

    @Query("SELECT * FROM BankStatement WHERE s_id LIKE :id")
    BankStatement getStatement(long id);

    @Update
    void updateAccount(BankStatement bankStatement);

    @Insert
    void insertAll(BankStatement... bankStatements);

    @Delete
    void delete(BankStatement bankStatement);
}
