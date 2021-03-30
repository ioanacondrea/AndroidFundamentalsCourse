package com.ioana.temaandroid;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ioana.temaandroid.adapters.TransactionRecyclerAdapter;
import com.ioana.temaandroid.async.AppExecutors;
import com.ioana.temaandroid.async.Callback;
import com.ioana.temaandroid.async.TransactionCategoryService;
import com.ioana.temaandroid.classes.DateConverter;
import com.ioana.temaandroid.classes.TransactionCategory;
import com.ioana.temaandroid.classes.TransactionWithCategory;
import com.ioana.temaandroid.database.AppDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TransactionList extends Fragment {

    private static final int ADD_TRANSACTION_FORM_REQUEST = 200;

    private static final String TRANSACTION_LIST_KEY = "transaction_list_key";
    private static final int RESULT_OK = -1;
    public static final String ACTIVITY_TYPE = "ACTIVITY_TYPE";
    private RecyclerView recyclerView;
    private ArrayList<TransactionWithCategory> transactions = new ArrayList<>();
    private DateConverter dateConverter = new DateConverter();
    private Intent floatingIntent;
    private FloatingActionButton floatingBtnListView;
    TransactionRecyclerAdapter adapter;
    private AppDatabase mDb;
    public  List<TransactionCategory> transactionExpenseCategory = new ArrayList<TransactionCategory>();
    public  List<TransactionCategory> transactionIncomeCategory = new ArrayList<TransactionCategory>();
    private int noCategories;
    private TransactionCategoryService transactionCategoryService;

    public TransactionList() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_transaction_list, container, false);
        Bundle bundle=this.getArguments();
        transactions = bundle.getParcelableArrayList(TRANSACTION_LIST_KEY);
        mDb = AppDatabase.getInstance(getContext());
        transactionCategoryService = new TransactionCategoryService(getContext());
        initComponents(view);
        return view;
    }

    public static TransactionList newInstance(ArrayList<TransactionWithCategory> transactions) {
        TransactionList fragment = new TransactionList();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(TransactionList.TRANSACTION_LIST_KEY, transactions);
        fragment.setArguments(bundle);
        return fragment;
    }

    private Callback<List<TransactionCategory>> getAllCategoriesByType(int type) {
        return new Callback<List<TransactionCategory>>() {
            @Override
            public void runResultOnUiThread(List<TransactionCategory> result) {
                if (result != null) {
                    if(type==0){
                        transactionExpenseCategory.addAll(result);
                    }else{
                        transactionIncomeCategory.addAll(result);
                    }
                }
            }
        };
    }


    public void initTransactionCategoryLists() {

        transactionCategoryService.getAllCategoriesByType(getAllCategoriesByType(0), 0);
        transactionCategoryService.getAllCategoriesByType(getAllCategoriesByType(1), 1);
        Log.i("Categories E", transactionExpenseCategory.toString());
        Log.i("Categories I", transactionIncomeCategory.toString());

        Collections.sort(transactionExpenseCategory);
        Collections.sort(transactionIncomeCategory);

    }

    private void initComponents(View view) {

        initTransactionCategoryLists();
        recyclerView = view.findViewById(R.id.recyclerView);
        floatingBtnListView = view.findViewById(R.id.floatingBtnAddTransaction);
        floatingBtnListView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                floatingIntent = new Intent(view.getContext(), TransactionAddForm.class);
                floatingIntent.putExtra(ACTIVITY_TYPE, "ADD");
                startActivityForResult(floatingIntent, ADD_TRANSACTION_FORM_REQUEST);

            }
        });


        LinearLayoutManager manager = new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false);
        adapter = new TransactionRecyclerAdapter(transactions, transactionExpenseCategory, transactionIncomeCategory);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NotNull RecyclerView recyclerView, @NotNull RecyclerView.ViewHolder viewHolder, @NotNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        int position = viewHolder.getAdapterPosition();
                        List<TransactionWithCategory> transactions = adapter.getTransactions();
                        mDb.transactionDao().delete(transactions.get(position).getTransaction());
                        ((MainActivity) getActivity()).loadTransactionsDB();
                    }
                });
            }
        }).attachToRecyclerView(recyclerView);



    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==ADD_TRANSACTION_FORM_REQUEST && resultCode==RESULT_OK){
            ((MainActivity)getActivity()).loadTransactionsDB();
        }

    }


    public void notifyInternalAdapter() {
        adapter = (TransactionRecyclerAdapter) recyclerView.getAdapter();
        if (adapter != null) {
            adapter.changeDataSet(transactions);
        }
    }


}