package com.ioana.temaandroid.classes;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(foreignKeys = @ForeignKey(entity = TransactionCategory.class,
        parentColumns = "categoryId",
        childColumns = "transactionCategoryID"))
public class Transaction implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private int transactionID;
    private String transactionName;
    private double transactionValue;
    private TransactionType transactionType;
    private int transactionCategoryID;
    private Date transactionDate;

    @Ignore
    public Transaction() {
    }

    @Ignore
    public Transaction(int transactionID, String transactionName, double transactionValue, TransactionType transactionType, Date transactionDate) {
        this.transactionID = transactionID;
        this.transactionName = transactionName;
        this.transactionValue = transactionValue;
        this.transactionType = transactionType;
        this.transactionDate = transactionDate;
    }

    @Ignore
    public Transaction(String transactionName, double transactionValue, TransactionType transactionType, int transactionCategoryID, Date transactionDate) {
        this.transactionName = transactionName;
        this.transactionValue = transactionValue;
        this.transactionType = transactionType;
        this.transactionCategoryID = transactionCategoryID;
        this.transactionDate = transactionDate;
    }

    public Transaction(int transactionID, String transactionName, double transactionValue, TransactionType transactionType, int transactionCategoryID, Date transactionDate) {
        this.transactionID = transactionID;
        this.transactionValue=transactionValue;
        this.transactionCategoryID = transactionCategoryID;
        this.transactionName = transactionName;
        this.transactionType= transactionType;
        this.transactionDate = transactionDate;
    }

    public static final Creator<Transaction> CREATOR = new Creator<Transaction>() {
        @Override
        public Transaction createFromParcel(Parcel in) {
            return new Transaction(in);
        }

        @Override
        public Transaction[] newArray(int size) {
            return new Transaction[size];
        }
    };

    public int getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(int transactionID) {
        this.transactionID = transactionID;
    }

    public String getTransactionName() {
        return transactionName;
    }

    public void setTransactionName(String transactionName) {
        this.transactionName = transactionName;
    }
    public double getTransactionValue() {
        return transactionValue;
    }

    public void setTransactionValue(double transactionValue) {
        this.transactionValue = transactionValue;
    }


    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public int getTransactionCategoryID() {
        return transactionCategoryID;
    }

    public void setTransactionCategoryID(int transactionCategoryID) {
        this.transactionCategoryID = transactionCategoryID;
    }


    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "transactionID=" + transactionID +
                ", transactionName='" + transactionName + '\'' +
                ", transactionValue=" + transactionValue +
                ", transactionType=" + transactionType +
                ", transactionCategoryID=" + transactionCategoryID +
                ", transactionDate=" + transactionDate +
                '}';
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.transactionID);
        dest.writeString(this.transactionName);
        dest.writeDouble(this.transactionValue);
        dest.writeString(this.transactionType.name());
        dest.writeInt(this.transactionCategoryID);
        dest.writeString(new DateConverter().toString( this.transactionDate));

    }

    private Transaction(Parcel source) {
        transactionID = source.readInt();
        transactionName = source.readString();
        transactionValue = source.readDouble();
        transactionType = TransactionType.valueOf(source.readString());
        transactionCategoryID = source.readInt();
        transactionDate = new DateConverter().fromString(source.readString());
    }

}
