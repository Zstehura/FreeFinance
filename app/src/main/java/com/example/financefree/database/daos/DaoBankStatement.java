package com.example.financefree.database.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.financefree.database.entities.BankStatement;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Maybe;

@Dao
public interface DaoBankStatement {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(BankStatement bs);
    @Delete
    void delete(BankStatement bs);
    @Update
    void update(BankStatement bs);

    @Query("SELECT * FROM BankStatement")
    List<BankStatement> getAll();
    @Query("SELECT * FROM BankStatement WHERE statement_id == :id")
    BankStatement getStatement(long id);
    @Query("SELECT * FROM BankStatement WHERE bank_id == :id")
    List<BankStatement> getBanksStatements(long id);
    @Query("SELECT * FROM BankStatement WHERE bank_id == :id AND date == :date")
    BankStatement getBanksStatementForDate(long id, long date);
}
