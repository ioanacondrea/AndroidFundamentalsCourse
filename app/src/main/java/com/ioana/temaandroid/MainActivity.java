package com.ioana.temaandroid;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.RatingBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.navigation.NavigationView;
import com.ioana.temaandroid.adapters.TransactionRecyclerAdapter;
import com.ioana.temaandroid.async.AppExecutors;
import com.ioana.temaandroid.async.AsyncTaskRunner;
import com.ioana.temaandroid.async.Callback;
import com.ioana.temaandroid.async.HttpManager;
import com.ioana.temaandroid.async.TransactionCategoryService;
import com.ioana.temaandroid.async.TransactionService;
import com.ioana.temaandroid.classes.DateConverter;
import com.ioana.temaandroid.classes.TransactionCategory;
import com.ioana.temaandroid.classes.TransactionWithCategory;
import com.ioana.temaandroid.database.AppDatabase;
import com.ioana.temaandroid.json.TransactionJsonParser;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public class MainActivity extends AppCompatActivity {
    private static final int ADD_TRANSACTION_FORM_REQUEST = 200;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Fragment currentFragment;
    private final ArrayList<TransactionWithCategory> transactionsJson = new ArrayList<>();
    private final ArrayList<TransactionWithCategory> transactionsFromDB = new ArrayList<>();
    private DateConverter dateConverter = new DateConverter();
    private static final String TRANSACTION_LIST_URL = "https://jsonkeeper.com/b/JR5I";
    //"https://jsonkeeper.com/b/6H0C";
    private AsyncTaskRunner asyncTaskRunner = new AsyncTaskRunner();
    private Double incomeValue, expenseValue, spendingPercentageValue;
    private AppDatabase mDb;
    private Future<Integer> noCategories;
    private TransactionService transactionService;
    private TransactionCategoryService transactionCategoryService;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);
        configNavigation();

        mDb = AppDatabase.getInstance(getApplicationContext());
        transactionService = new TransactionService((getApplicationContext()));
        transactionCategoryService = new TransactionCategoryService(getApplicationContext());
        getBankAccountsFromHttp();
        loadTransactionsDB();
        //new GetTransactionsFromDatabase().execute();
        initTransactionCategoryLists();
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                computeValues();
                if(item.getItemId() == R.id.nav_transactions){
                    currentFragment = TransactionList.newInstance(transactionsFromDB);
                }
                else if(item.getItemId() == R.id.nav_transactions_report){
                    currentFragment = TransactionReport.newInstance(incomeValue, expenseValue, spendingPercentageValue);
                }
                else if (item.getItemId() == R.id.nav_add_transaction) {

                    Intent intent = new Intent(getApplicationContext(), TransactionAddForm.class);
                    intent.putExtra("ACTIVITY_TYPE", "ADD");
                    startActivityForResult(intent, ADD_TRANSACTION_FORM_REQUEST);
                }else if(item.getItemId() == R.id.nav_category_list){
                    Intent intent = new Intent(getApplicationContext(), CategoryList.class);
                    startActivity(intent);
                }else if (item.getItemId()==R.id.pieCharts){
                    currentFragment = PieChartFragment.newInstance();
                }
                openFragment();
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
        openDefaultFragment(savedInstanceState);
        Log.i("Transaction", transactionsJson.toString());


    }

    private class GetTransactionsFromDatabase extends AsyncTask<Void, Void, List<TransactionWithCategory>> {
        @Override
        protected List<TransactionWithCategory> doInBackground(Void... voids) {
            return mDb.transactionDao().loadAllTransactions();
        }

        protected void onPostExecute(List<TransactionWithCategory> result) {
            transactionsFromDB.addAll(result);
        }

    }


    private Callback<List<TransactionWithCategory>> getAllTransactionsFromDbCallback() {
        return new Callback<List<TransactionWithCategory>>() {
            @Override
            public void runResultOnUiThread(List<TransactionWithCategory> result) {
                if (result != null) {
                    transactionsFromDB.clear();
                    transactionsFromDB.addAll(result);
                    updateDataInRecyclerView();
                }
            }
        };
    }

    private Callback<Integer> getNumberOfCategories() {
        return new Callback<Integer>() {
            @Override
            public void runResultOnUiThread(Integer result) {
                if(result==0){
                        transactionCategoryService.insertCategory(insertCategory(),(new TransactionCategory(0, "Shopping", "caticon_expense_shopping")));
                        transactionCategoryService.insertCategory(insertCategory(),(new TransactionCategory( 0,"Food & Beverages", "caticon_expense_food")));
                        transactionCategoryService.insertCategory(insertCategory(),new TransactionCategory( 0, "Transportation", "caticon_expense_transport"));
                        transactionCategoryService.insertCategory(insertCategory(),new TransactionCategory( 0, "Bills & Utilities", "caticon_expense_bills"));
                        transactionCategoryService.insertCategory(insertCategory(),new TransactionCategory( 0, "Others", "caticon_zothers"));
                        transactionCategoryService.insertCategory(insertCategory(),new TransactionCategory( 1, "Salary", "caticon_income_salary"));
                        transactionCategoryService.insertCategory(insertCategory(),new TransactionCategory( 1, "Gifts", "caticon_income_gifts"));
                        transactionCategoryService.insertCategory(insertCategory(),new TransactionCategory( 1, "Others", "caticon_zothers"));
                }
            }
        };
    }

    private Callback<TransactionCategory> insertCategory() {
        return new Callback<TransactionCategory>() {
            @Override
            public void runResultOnUiThread(TransactionCategory result) {
                if (result != null) {
                    Log.i("S-a inserat categoria",result.toString() );
                }
            }
        };
    }

    public void loadTransactionsDB(){
        transactionService.getAll(getAllTransactionsFromDbCallback());

    }


    public void deleteAllTransactions(){

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.transactionDao().deleteAll();
            }
        });
    }

    public void deleteAllCategories(){

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.transactionCategoryDao().deleteAll();
            }
        });
    }

    public void updateDataInRecyclerView(){
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        TransactionRecyclerAdapter adapter = (TransactionRecyclerAdapter) recyclerView.getAdapter();
        if (adapter != null) {
            adapter.changeDataSet(transactionsFromDB);
        }
    }

    public void initTransactionCategoryLists() {
        //deleteAllTransactions();
        //getNoCategories();
        //deleteAllCategories();
        noCategories = transactionCategoryService.getNumberOfCategories(getNumberOfCategories());
        System.out.println(noCategories);
    }

        @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==ADD_TRANSACTION_FORM_REQUEST && resultCode==RESULT_OK){
            Log.i("Am ajuns din add form aici:", "in OnResult");
            loadTransactionsDB();
            updateDataInRecyclerView();
        }

    }

    private void computeValues(){
        expenseValue =0.0;
        incomeValue = 0.0;
        for (TransactionWithCategory t: transactionsFromDB) {
            if(t.getTransaction().getTransactionType().name().equals("Expense")){
                expenseValue+=t.getTransaction().getTransactionValue();
            }else{
                incomeValue+=t.getTransaction().getTransactionValue();
            }
        }
        if(incomeValue==0.0){
            spendingPercentageValue=0.0;
        }
        else{
            spendingPercentageValue = Math.floor(expenseValue/incomeValue*10000)/100;
        }

    }


    private void getBankAccountsFromHttp() {
        Callable<String> asyncOperation = new HttpManager(TRANSACTION_LIST_URL);
        Callback<String> mainThreadOperation = receiveTransactionsFromHttp();
        asyncTaskRunner.executeAsync(asyncOperation, mainThreadOperation);

    }

    private Callback<String> receiveTransactionsFromHttp() {
        return result -> {
            transactionsJson.addAll(TransactionJsonParser.fromJson(result));
        };
    }


    private void configNavigation() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    private void openDefaultFragment(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            currentFragment = TransactionList.newInstance(transactionsFromDB);
            openFragment();
            navigationView.setCheckedItem(R.id.nav_transactions);
        }
    }

    private void openFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_frame_container, currentFragment)
                .commit();
    }
}