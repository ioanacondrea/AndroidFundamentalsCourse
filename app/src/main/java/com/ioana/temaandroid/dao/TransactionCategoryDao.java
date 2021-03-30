package com.ioana.temaandroid.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.ioana.temaandroid.classes.TransactionCategory;

import java.util.List;

@Dao
public interface TransactionCategoryDao {

    @Query("SELECT * FROM `transactioncategory` ORDER BY categoryId")
    List<TransactionCategory> loadAllCategories();

    @Query("SELECT categoryId FROM transactioncategory where categoryName=:category")
    int getCategoryIdByName(String category);

    @Query("SELECT * FROM `transactioncategory` WHERE categoryType=:categoryType")
    List<TransactionCategory> loadAllCategoriesByType(int categoryType);

    @Insert
    void insertCategory(TransactionCategory category);

    @Delete
    Integer delete(TransactionCategory category);

    @Query("SELECT * FROM `transactioncategory` WHERE categoryId = :id")
    TransactionCategory loadCategoryById(int id);

    @Update
    void updateCategory(TransactionCategory category);

    @Query("SELECT COUNT(categoryId) from transactioncategory")
    int getNoCategories();

    @Query("DELETE FROM transactioncategory")
    void deleteAll();
}
