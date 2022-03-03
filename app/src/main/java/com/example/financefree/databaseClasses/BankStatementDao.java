package com.example.financefree.databaseClasses;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.financefree.datahandlers.CustomDate;

import java.util.List;

@Dao
public interface BankStatementDao {

    @Query("SELECT * FROM Statement")
    List<Statement> getAll();

    @Query("SELECT * FROM Statement WHERE bank_id LIKE :id")
    List<Statement> getByBankId(long id);

    @Query("SELECT * FROM Statement WHERE bank_id LIKE :id AND date <= :date")
    List<Statement> getAllBefore(long id, CustomDate date);

    @Query("SELECT * FROM Statement WHERE bank_id LIKE :id AND date >= :date")
    List<Statement> getAllAfter(long id, CustomDate date);

    @Query("SELECT * FROM Statement WHERE bank_id LIKE :id AND date <= :date2 AND date >= :date1")
    List<Statement> getAllBetween(long id, CustomDate date1, CustomDate date2);

    @Query("SELECT * FROM Statement WHERE s_id LIKE :id")
    Statement getStatement(long id);

    @Insert
    void insertAll(Statement... statements);

    @Delete
    void delete(Statement statement);
}
