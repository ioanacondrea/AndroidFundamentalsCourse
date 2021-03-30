package com.ioana.temaandroid;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ioana.temaandroid.adapters.CategoryListAdapter;
import com.ioana.temaandroid.adapters.RowItem;
import com.ioana.temaandroid.async.AppExecutors;
import com.ioana.temaandroid.classes.TransactionCategory;
import com.ioana.temaandroid.database.AppDatabase;

import java.util.ArrayList;
import java.util.List;

public class CategoryList extends AppCompatActivity {

    public static final String ACTIVITY_MODE= "ACTIVITY_MODE";
    public static final String CATEGORY_DATA = "CATEGORY_DATA";
    public static final int EDIT_CATEGORY_FORM_REQUEST =206;
    private ListView listView;
    private List<TransactionCategory> categories = new ArrayList<>();
    private CategoryListAdapter adapter;
    private List<RowItem> icons = new ArrayList<>();
    private AppDatabase mDb;

    private class LoadCategoriesAsync extends AsyncTask<Void, Void, List<TransactionCategory>> {

        @Override
        protected List<TransactionCategory> doInBackground(Void... voids) {
           return mDb.transactionCategoryDao().loadAllCategories();
        }

        protected void onPostExecute(List<TransactionCategory> result) {
            categories.clear();
            categories.addAll(result);
            setupListView();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list);
        mDb = AppDatabase.getInstance(getApplicationContext());
        listView = findViewById(R.id.categoryListView);
        new LoadCategoriesAsync().execute();


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                RowItem item = adapter.getRowItem(position);
               TransactionCategory t = categories.stream().filter(c -> c.getCategoryId()==item.getCategoryIdFromDB()).findFirst().orElseGet(null);
               Intent intent = new Intent(CategoryList.this, CategoryAddForm.class);
               intent.putExtra(ACTIVITY_MODE, "EDIT");
               intent.putExtra(CATEGORY_DATA, t );
               startActivityForResult(intent, EDIT_CATEGORY_FORM_REQUEST);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==EDIT_CATEGORY_FORM_REQUEST && resultCode == RESULT_OK){
            new LoadCategoriesAsync().execute();
        }
    }


    public List<RowItem> transformInRowItems(List<TransactionCategory> categories){
        List<RowItem> rows = new ArrayList<>();
        for(TransactionCategory c: categories){
            String type = c.getCategoryType()==0? "Expense":"Income";
            rows.add( new RowItem(c.getCategoryName()+" - " + type,c.getImageId(CategoryList.this, c.getIconName()), c.getCategoryId()));
        }
        return rows;
    }

    @Override
    protected void onResume() {
        super.onResume();
        new LoadCategoriesAsync().execute();
    }

    public void loadCategoriesFromDb(){
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                categories = mDb.transactionCategoryDao().loadAllCategories();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.setRowList(transformInRowItems(categories));
                    }
                });
            }
        });
    }

    public void setupListView(){
        icons = transformInRowItems(categories);
        adapter = new CategoryListAdapter(CategoryList.this, R.id.iconListView, R.id.textListView, icons);
        listView.setAdapter(adapter);
    }
}