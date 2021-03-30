package com.ioana.temaandroid.async;

import android.content.Context;

import com.ioana.temaandroid.async.AsyncTaskRunner;
import com.ioana.temaandroid.async.Callback;
import com.ioana.temaandroid.classes.TransactionCategory;
import com.ioana.temaandroid.dao.TransactionCategoryDao;
import com.ioana.temaandroid.database.AppDatabase;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public class TransactionCategoryService {

    private final TransactionCategoryDao transactionCategoryDao;
    private final AsyncTaskRunner taskRunner;

    public TransactionCategoryService(Context context) {
        transactionCategoryDao = AppDatabase.getInstance(context).transactionCategoryDao();
        taskRunner = new AsyncTaskRunner();
    }

    public void getAllCategories(Callback<List<TransactionCategory>> callback) {
        Callable<List<TransactionCategory>> callable = new Callable<List<TransactionCategory>>() {
            @Override
            public List<TransactionCategory> call() {
                return transactionCategoryDao.loadAllCategories();
            }
        };
        taskRunner.executeAsync(callable, callback);
    }

    public void getAllCategoriesByType(Callback<List<TransactionCategory>> callback, int type) {
        Callable<List<TransactionCategory>> callable = new Callable<List<TransactionCategory>>() {
            @Override
            public List<TransactionCategory> call() {
                return transactionCategoryDao.loadAllCategoriesByType(type);
            }
        };
        taskRunner.executeAsync(callable, callback);
    }

    public Future<Integer> getNumberOfCategories(Callback<Integer> callback) {
        Callable<Integer> callable = new Callable<Integer>() {
            @Override
            public Integer call() {
                return transactionCategoryDao.getNoCategories();
            }
        };
        taskRunner.executeAsync(callable, callback);
        return null;
    }


    public void insertCategory(Callback<TransactionCategory> callback, final TransactionCategory transactionCategory) {
            Callable<TransactionCategory> callable = new Callable<TransactionCategory>() {
                @Override
                public TransactionCategory call() {
                    if (transactionCategory == null) {
                        return null;
                    }
                    transactionCategoryDao.insertCategory(transactionCategory);
                    return transactionCategory;
                }
            };
            taskRunner.executeAsync(callable, callback);
        }

        public void updateCategory(Callback<TransactionCategory> callback, final TransactionCategory transactionCategory) {
            Callable<TransactionCategory> callable = new Callable<TransactionCategory>() {
                @Override
                public TransactionCategory call() {
                    if (transactionCategory == null) {
                        return null;
                    }
                    transactionCategoryDao.updateCategory(transactionCategory);
                    return transactionCategory;
                }
            };
            taskRunner.executeAsync(callable, callback);
        }

        public void deleteCategory(Callback<Integer> callback, final TransactionCategory transactionCategory) {
            Callable<Integer> callable = new Callable<Integer>() {
                @Override
                public Integer call() {
                    if (transactionCategory == null) {
                        return -1;
                    }
                    return transactionCategoryDao.delete(transactionCategory);
                }
            };
            taskRunner.executeAsync(callable, callback);
        }

}
