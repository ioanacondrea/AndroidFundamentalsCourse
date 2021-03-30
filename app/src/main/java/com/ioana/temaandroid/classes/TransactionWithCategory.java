package com.ioana.temaandroid.classes;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Embedded;
import androidx.room.Relation;

public class TransactionWithCategory implements Parcelable {
    @Embedded public Transaction transaction;
    @Relation(
            parentColumn = "transactionCategoryID",
            entityColumn = "categoryId"
    )
    public TransactionCategory category;

    public Transaction getTransaction() {
        return transaction;
    }

    public TransactionWithCategory(Transaction transaction, TransactionCategory category) {
        this.transaction = transaction;
        this.category = category;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public TransactionCategory getCategory() {
        return category;
    }

    public void setCategory(TransactionCategory category) {
        this.category = category;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TransactionWithCategory> CREATOR = new Creator<TransactionWithCategory>() {
        @Override
        public TransactionWithCategory createFromParcel(Parcel in) {
            return new TransactionWithCategory(in);
        }

        @Override
        public TransactionWithCategory[] newArray(int size) {
            return new TransactionWithCategory[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.transaction, flags);
        dest.writeParcelable(this.category, flags);
    }

    private TransactionWithCategory (Parcel source){
        this.transaction = source.readParcelable(getClass().getClassLoader());
        this.category = source.readParcelable(getClass().getClassLoader());
    }

    @Override
    public String toString() {
        return "TransactionWithCategory{" +
                "transaction=" + transaction +
                ", category=" + category +
                '}';
    }
}
