package com.ioana.temaandroid.async;

import android.content.Context;

import com.ioana.temaandroid.async.AsyncTaskRunner;
import com.ioana.temaandroid.async.Callback;
import com.ioana.temaandroid.classes.Transaction;
import com.ioana.temaandroid.classes.TransactionWithCategory;
import com.ioana.temaandroid.dao.TransactionDao;
import com.ioana.temaandroid.database.AppDatabase;

import java.util.List;
import java.util.concurrent.Callable;


public class TransactionService {

    private final TransactionDao transactionDao;
    private final AsyncTaskRunner taskRunner;

    public TransactionService(Context context) {
        transactionDao = AppDatabase.getInstance(context).transactionDao();
        taskRunner = new AsyncTaskRunner();
    }

    public List<TransactionWithCategory> getAllV2() {
        return transactionDao.loadAllTransactions();
    }

    public void getAll(Callback<List<TransactionWithCategory>> callback) {
        Callable<List<TransactionWithCategory>> callable = new Callable<List<TransactionWithCategory>>() {
            @Override
            public List<TransactionWithCategory> call() {
                return transactionDao.loadAllTransactions();
            }
        };
        taskRunner.executeAsync(callable, callback);
    }

    public void insert(Callback<Transaction> callback, final Transaction transaction) {
        Callable<Transaction> callable = new Callable<Transaction>() {
            @Override
            public Transaction call() {
                if (transaction == null) {
                    return null;
                }
                transactionDao.insertTransaction(transaction);
                return transaction;
            }
        };
        taskRunner.executeAsync(callable, callback);
    }

    public void update(Callback<Transaction> callback, final Transaction transaction) {
        Callable<Transaction> callable = new Callable<Transaction>() {
            @Override
            public Transaction call() {
                if (transaction == null) {
                    return null;
                }
                transactionDao.updateTransaction(transaction);
                return transaction;
            }
        };
        taskRunner.executeAsync(callable, callback);
    }

    public void delete(Callback<Integer> callback, final Transaction transaction) {
        Callable<Integer> callable = new Callable<Integer>() {
            @Override
            public Integer call() {
                if (transaction == null) {
                    return -1;
                }
                transactionDao.delete(transaction);
                return 1;
            }
        };
        taskRunner.executeAsync(callable, callback);
    }
}
