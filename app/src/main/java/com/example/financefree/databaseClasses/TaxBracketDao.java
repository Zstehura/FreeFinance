package com.example.financefree.databaseClasses;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TaxBracketDao {

    @Query("SELECT * FROM TaxBracket")
    List<TaxBracket> getAll();

    @Query("SELECT * FROM TaxBracket WHERE bracket_id == :id")
    TaxBracket getBracket(long id);

    @Query("SELECT * FROM TaxBracket WHERE year == :year")
    List<TaxBracket> getBracketYear(int year);

    @Query("SELECT * FROM TaxBracket WHERE year == :year AND filingAs LIKE :fileAs")
    List<TaxBracket> getBracketsFileAs(int year, String fileAs);

    @Update
    void updateAccount(TaxBracket taxBracket);

    @Insert
    void insertAll(SinglePayment... singlePayments);

    @Delete
    void delete(SinglePayment singlePayment);

}
