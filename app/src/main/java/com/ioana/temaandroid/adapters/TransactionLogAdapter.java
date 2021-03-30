package com.ioana.temaandroid.adapters;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ioana.temaandroid.R;
import com.ioana.temaandroid.classes.TransactionLog;

import java.util.List;

public class TransactionLogAdapter extends ArrayAdapter {
    Activity context;
    List<TransactionLog> logs;

    public TransactionLogAdapter(Activity context, List<TransactionLog> logs) {
        super(context, R.layout.log_list_item, logs);
        this.context = context;
        this.logs = logs;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = context.getLayoutInflater();
        View view = inflater.inflate(R.layout.log_list_item, null, true);

        TextView dateTV = view.findViewById(R.id.logDateTV);
        TextView balance = view.findViewById(R.id.balanceLogTV);
        TextView expense = view.findViewById(R.id.expenseLogTV);
        TextView income = view.findViewById(R.id.incomeLogTV);
        TextView spending = view.findViewById(R.id.spendingPercLogTV);

        TransactionLog log = logs.get(position);
        dateTV.setText(log.getDate());
        balance.setText(log.getBalance());
        if(Double.parseDouble(log.getBalance())>0){
            balance.setTextColor(Color.parseColor("#3ec978"));
        }else{
            balance.setTextColor(Color.parseColor("#ed6339"));
        }
        expense.setText(log.getExpenseTotalValue());
        income.setText(log.getIncomeTotalValue());
        String sp = log.getIncomeTotalValue();
        spending.setText(sp);

        return view;
    }
}
