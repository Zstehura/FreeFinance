package com.example.financefree.databaseClasses;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface BankStatementDao {

    @Query("SELECT * FROM BankStatement")
    List<BankStatement> getAll();

    @Query("SELECT * FROM BankStatement WHERE bank_id == :id")
    List<BankStatement> getByBankId(long id);

    @Query("SELECT * FROM BankStatement WHERE bank_id == :id AND date <= :date")
    List<BankStatement> getAllBefore(long id, long date);

    @Query("SELECT * FROM BankStatement WHERE bank_id == :id AND date >= :date")
    List<BankStatement> getAllAfter(long id, long date);

    @Query("SELECT * FROM BankStatement WHERE bank_id == :id AND date >= :date1 AND date <= :date2")
    List<BankStatement> getAllBetween(long id, long date1, long date2);

    @Query("SELECT * FROM BankStatement WHERE bank_id == :bank_id AND date == :date LIMIT 1")
    BankStatement getStatement(long bank_id, long date);

    @Query("SELECT * FROM BankStatement WHERE s_id == :s_id")
    BankStatement getStatement(long s_id);

    @Update
    void updateAccount(BankStatement bankStatement);

    @Insert
    void insertAll(BankStatement... bankStatements);

    @Delete
    void delete(BankStatement bankStatement);
}
