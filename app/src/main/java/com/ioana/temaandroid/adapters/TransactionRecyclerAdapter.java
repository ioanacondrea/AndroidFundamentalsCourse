package com.ioana.temaandroid.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ioana.temaandroid.classes.DateConverter;
import com.ioana.temaandroid.R;
import com.ioana.temaandroid.TransactionAddForm;
import com.ioana.temaandroid.classes.TransactionCategory;
import com.ioana.temaandroid.classes.TransactionWithCategory;

import java.util.ArrayList;
import java.util.List;

public class TransactionRecyclerAdapter extends RecyclerView.Adapter<TransactionViewHolder> {


    public List<TransactionWithCategory> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<TransactionWithCategory> transactions) {
        this.transactions = transactions;
    }

    private List<TransactionWithCategory> transactions;
    private final DateConverter dateConverter = new DateConverter();
    public final List<TransactionCategory> transactionExpenseCategory;
    public final List<TransactionCategory> transactionIncomeCategory;
    private static final int ADD_TRANSACTION_FORM_REQUEST = 200;

    public TransactionRecyclerAdapter(List<TransactionWithCategory> transactions, List<TransactionCategory> expenseCat, List<TransactionCategory> incomeCat) {
        this.transactions = transactions;
        this.transactionExpenseCategory = expenseCat;
        this.transactionIncomeCategory = incomeCat;

    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.list_row, null);
        return new TransactionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
        TransactionWithCategory transaction = transactions.get(position);
        holder.getTransactionName().setText(transaction.getTransaction().getTransactionName());
        holder.getTransactionCategory().setText(transaction.getCategory().getCategoryName());
        holder.getTransactionDate().setText(dateConverter.toString(transaction.getTransaction().getTransactionDate()));
        String sign;
        String colorId;
        int iconID=  R.drawable.caticon_zothers;
        if (transaction.getTransaction().getTransactionType().name().equals("Expense")) {
            sign = "- ";
            colorId = "#ed6339";

        } else {
            sign = "+ ";
            colorId = "#3ec978";

        }
        iconID= transaction.getCategory().getImageId( holder.getIcon().getContext(), transaction.getCategory().getIconName());
        holder.getValue().setText(sign + transaction.getTransaction().getTransactionValue());
        holder.getValue().setTextColor(Color.parseColor(colorId));
        holder.getIcon().setImageResource(iconID);

        holder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context , TransactionAddForm.class);
                TransactionWithCategory transaction = transactions.get(position);
                intent.putExtra("ACTIVITY_TYPE", "EDIT");
                intent.putExtra("TRANSACTION_DATA", transaction);
                ((Activity)context).startActivityForResult( intent, ADD_TRANSACTION_FORM_REQUEST);
            }
        });

    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    public void changeDataSet(ArrayList<TransactionWithCategory> newTransactions) {

        this.transactions = newTransactions;
        notifyDataSetChanged();

    }
}
