package com.ioana.temaandroid.classes;

import java.io.Serializable;
import java.util.Date;

public class TransactionLog implements Serializable {

    private String logId;

    public String getLogId() {
        return logId;
    }

    public void setLogId(String logId) {
        this.logId = logId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getExpenseTotalValue() {
        return expenseTotalValue;
    }

    public void setExpenseTotalValue(String expenseTotalValue) {
        this.expenseTotalValue = expenseTotalValue;
    }

    public String getIncomeTotalValue() {
        return incomeTotalValue;
    }

    public void setIncomeTotalValue(String incomeTotalValue) {
        this.incomeTotalValue = incomeTotalValue;
    }

    public String getSpendingPercentage() {
        return spendingPercentage;
    }

    public void setSpendingPercentage(String spendingPercentage) {
        this.spendingPercentage = spendingPercentage;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    private String date;
    private String expenseTotalValue;
    private String incomeTotalValue;
    private String spendingPercentage;
    private String balance;

    public TransactionLog(String logId, String date, String expenseTotalValue, String incomeTotalValue, String spendingPercentage, String balance) {
        this.logId = logId;
        this.date = date;
        this.expenseTotalValue = expenseTotalValue;
        this.incomeTotalValue = incomeTotalValue;
        this.spendingPercentage = spendingPercentage;
        this.balance = balance;
    }

    public TransactionLog() {

    }

    @Override
    public String toString() {
        return "TransactionLog {" +
                "expenseTotalValue=" + expenseTotalValue +
                ", incomeTotalValue=" + incomeTotalValue +
                ", spendingPercentage=" + spendingPercentage +
                ", balance=" + balance +
                ", logId=" + logId +
                '}';
    }
}

