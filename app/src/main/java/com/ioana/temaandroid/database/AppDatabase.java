package com.ioana.temaandroid.database;

import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.ioana.temaandroid.dao.Converters;
import com.ioana.temaandroid.classes.Transaction;
import com.ioana.temaandroid.classes.TransactionCategory;
import com.ioana.temaandroid.dao.TransactionCategoryDao;
import com.ioana.temaandroid.dao.TransactionDao;

@Database(entities = {Transaction.class, TransactionCategory.class}, version=4, exportSchema = false)
@TypeConverters({Converters.class})
public  abstract class AppDatabase extends RoomDatabase {
    private static final String LOG_TAG = AppDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "transactions";
    private static AppDatabase sInstance;


    public static AppDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                Log.d(LOG_TAG, "Creating new database instance");
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabase.class, AppDatabase.DATABASE_NAME).fallbackToDestructiveMigration()
                        .build();
            }
        }
        Log.d(LOG_TAG, "Getting the database instance");
        return sInstance;
    }

    public abstract TransactionDao transactionDao();
    public abstract TransactionCategoryDao transactionCategoryDao();
}
