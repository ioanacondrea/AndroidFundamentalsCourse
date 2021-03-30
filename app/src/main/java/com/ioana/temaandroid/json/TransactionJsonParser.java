package com.ioana.temaandroid.json;

import com.ioana.temaandroid.classes.DateConverter;
import com.ioana.temaandroid.classes.Transaction;
import com.ioana.temaandroid.classes.TransactionCategory;
import com.ioana.temaandroid.classes.TransactionType;
import com.ioana.temaandroid.classes.TransactionWithCategory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TransactionJsonParser {
    public static final String TRANSACTION_ID="TransactionID";
    public static final String TRANSACTION_NAME="TransactionName";
    public static final String TRANSACTION_VALUE="TransactionValue";
    public static final String TRANSACTION_TYPE="TransactionType";
    public static final String TRANSACTION_CATEGORY="TransactionCategory";
    public static final String TRANSACTION_DATE="TransactionDate";
    private static final DateConverter dateConverter= new DateConverter();
    public static final String CATEGORY_ID = "categoryId";
    public static final String CATEGORY_TYPE ="categoryType";
    public static final String CATEGORY="category";
    public static final String ICON_NAME="iconName";
    public static final String TRANSACTION_CATEGORY_ID = "TransactionCategoryID";

    public static List<TransactionWithCategory> fromJson(String json){
        try{
            JSONArray array= new JSONArray(json);
            return readTransactions(array);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    private static List<TransactionWithCategory> readTransactions(JSONArray array) throws JSONException {
        List<TransactionWithCategory> transactions = new ArrayList<>();
        for(int i=0;i<array.length();i++){
            TransactionWithCategory transaction = readTransaction(array.getJSONObject(i));
            transactions.add(transaction);
        }
        return transactions;
    }

    private static TransactionWithCategory readTransaction(JSONObject jsonObject) throws JSONException {
        int transactionID = jsonObject.getInt(TRANSACTION_ID);
        String transactionName = jsonObject.getString(TRANSACTION_NAME);
        double transactionValue= jsonObject.getDouble(TRANSACTION_VALUE);
        TransactionType transactionType = TransactionType.valueOf(jsonObject.getString(TRANSACTION_TYPE));
        int transactionCategoryId = jsonObject.getInt(TRANSACTION_CATEGORY_ID);
        TransactionCategory transactionCategory= readTransactionCategory(jsonObject.getJSONObject(TRANSACTION_CATEGORY));
        Date transactionDate= dateConverter.fromString(jsonObject.getString(TRANSACTION_DATE));
        Transaction t = new Transaction(transactionID, transactionName, transactionValue, transactionType, transactionCategoryId, transactionDate);

         return new TransactionWithCategory(t, transactionCategory);
    }

    private static TransactionCategory readTransactionCategory(JSONObject jsonObject) throws JSONException {
        int categoryId = jsonObject.getInt(CATEGORY_ID);
        int categoryType =jsonObject.getInt(CATEGORY_TYPE);
        String category = jsonObject.getString(CATEGORY);
        String iconName = jsonObject.getString(ICON_NAME);

        return new TransactionCategory(categoryId, categoryType, category, iconName);
    }

}
