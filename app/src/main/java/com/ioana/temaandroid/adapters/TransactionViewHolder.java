package com.ioana.temaandroid.adapters;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ioana.temaandroid.R;

public class TransactionViewHolder extends RecyclerView.ViewHolder {

    private final TextView transactionName;
    private final TextView transactionCategory;
    private final TextView transactionDate;
    private final ImageView icon;
    private final TextView value;

    public TransactionViewHolder(@NonNull View itemView) {
        super(itemView);
        transactionName = itemView.findViewById(R.id.name);
        transactionCategory = itemView.findViewById(R.id.category);
        transactionDate = itemView.findViewById(R.id.date);
        icon = itemView.findViewById(R.id.imagine_item);
        value= itemView.findViewById(R.id.transactionValueDisplay);
    }

    public TextView getTransactionName() {
        return transactionName;
    }

    public TextView getTransactionCategory() {
        return transactionCategory;
    }

    public TextView getTransactionDate() {
        return transactionDate;
    }

    public ImageView getIcon() {
        return icon;
    }

    public TextView getValue() {
        return value;
    }

}
