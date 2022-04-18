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

    @Query("DELETE FROM BankStatement WHERE 1 == 1")
    void deleteAll();
    @Query("SELECT * FROM BankStatement")
    List<BankStatement> getAll();
    @Query("SELECT * FROM BankStatement WHERE bank_id == :id AND date <= :date" +
            " ORDER BY date DESC LIMIT 1")
    BankStatement getBanksLastStatementForDate(long id, long date);

    @Query("DELETE FROM BankStatement WHERE date < :date")
    void deleteOlderThan(long date);
}
