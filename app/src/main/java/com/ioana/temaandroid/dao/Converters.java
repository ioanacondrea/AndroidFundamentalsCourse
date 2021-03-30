package com.ioana.temaandroid.dao;

import androidx.room.TypeConverter;

import com.ioana.temaandroid.classes.TransactionType;

import java.util.Date;

public class Converters {

    @TypeConverter
    public static int fromTransactionType(TransactionType value){
        return value.name().equals("Expense")?0:1;
    }

    @TypeConverter
    public static TransactionType intToTransactionType(int i){
        return i==0?TransactionType.Expense:TransactionType.Income;
    }

    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

}
