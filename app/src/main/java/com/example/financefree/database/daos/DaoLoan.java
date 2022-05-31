package com.example.financefree.database.daos;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.financefree.database.entities.Loan;

import java.util.List;

public interface DaoLoan {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Loan loan);
    @Delete
    void delete(Loan loan);
    @Update
    void update(Loan loan);

    @Query("DELETE FROM Loan WHERE 1 == 1")
    void deleteAll();
    @Query("SELECT * FROM Loan")
    List<Loan> getAll();
    @Query("SELECT * FROM Loan WHERE loan_id == :loanId")
    Loan getLoan(long loanId);

}
