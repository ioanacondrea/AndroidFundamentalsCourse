package com.ioana.temaandroid;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.ioana.temaandroid.async.AppExecutors;
import com.ioana.temaandroid.classes.TransactionWithCategory;
import com.ioana.temaandroid.database.AppDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

public class PieChartFragment extends Fragment {

    private PieChart expensePieChart;
    private PieChart incomePieChart;
    private AppDatabase mDb;
    private List<TransactionWithCategory> transactions;

    public static PieChartFragment newInstance() {
        PieChartFragment fragment = new PieChartFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_pie_chart, container, false);

        mDb = AppDatabase.getInstance(getContext());
        expensePieChart = view.findViewById(R.id.expensePieChart);
        incomePieChart = view.findViewById(R.id.incomePieChart);

        getPieChartDataFromDatabase(view);
        return view;
    }

    public void getPieChartDataFromDatabase(View view){
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                transactions = mDb.transactionDao().loadAllTransactions();
                drawPieChart(transactions.stream().filter(c -> c.getCategory().getCategoryType()==0).collect(Collectors.toList()), R.id.expensePieChart, view, "Expense chart");
                drawPieChart(transactions.stream().filter(c -> c.getCategory().getCategoryType()==1).collect(Collectors.toList()), R.id.incomePieChart, view, "Income chart");
            }
        });
    }

    public void drawPieChart(List<TransactionWithCategory> transactions, int pieChartID,View view, String pieLabel){
        List<String> categories = transactions.stream().map(c -> c.getCategory().getCategoryName()).collect(Collectors.toList());
        Set<String> uniqueCategories = new HashSet<String>(categories);
        Object[] filteredCategories = uniqueCategories.toArray();
        PieChart chart = view.findViewById(pieChartID);
        ArrayList<PieEntry> pieEntries = new ArrayList<>();
        Map<String, Double> data = new HashMap<>();
        for(int i =0; i<uniqueCategories.size();i++){
            data.put(filteredCategories[i].toString(), 0.0);
        }

        for(TransactionWithCategory t: transactions){
            Double newValue = data.get(t.getCategory().getCategoryName()) + t.getTransaction().getTransactionValue();
            data.put(t.getCategory().getCategoryName(), newValue);
        }

        Random rnd = new Random();
        ArrayList<Integer> colors = new ArrayList<>();

        for(int i=0; i<transactions.size();i++){
            int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
            colors.add(color);
        }

        for(String type: data.keySet()) {
            pieEntries.add(new PieEntry(data.get(type).floatValue(), type));
        }

        PieDataSet pieDataSet = new PieDataSet(pieEntries, pieLabel);
        pieDataSet.setValueTextSize(16f);
        pieDataSet.setColors(colors);
        PieData pieData = new PieData(pieDataSet);
        pieData.setDrawValues(true);
        chart.setEntryLabelColor(Color.BLACK);
        chart.setData(pieData);
        chart.invalidate();

    }

}