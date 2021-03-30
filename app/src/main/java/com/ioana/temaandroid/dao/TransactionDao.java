package com.ioana.temaandroid.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.ioana.temaandroid.classes.TransactionWithCategory;
import com.ioana.temaandroid.classes.Transaction;

import java.util.List;

@Dao
public interface TransactionDao {

    @androidx.room.Transaction
    @Query("SELECT * FROM `transaction`")
    List<TransactionWithCategory> loadAllTransactions();

    @Insert
    void insertTransaction(Transaction transaction);

    @Delete
    void delete(Transaction transaction);

    @Query("SELECT * FROM `transaction` WHERE TransactionID = :id")
    Transaction loadTransactionById(int id);

    @Update
    void updateTransaction(Transaction transaction);

    @Query("DELETE FROM `transaction`")
    void deleteAll();
}
