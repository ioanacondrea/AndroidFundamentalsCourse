package com.ioana.temaandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.ioana.temaandroid.adapters.RowItem;
import com.ioana.temaandroid.adapters.TransactionCategorySpinnerAdapter;
import com.ioana.temaandroid.async.AppExecutors;
import com.ioana.temaandroid.async.Callback;
import com.ioana.temaandroid.async.TransactionCategoryService;
import com.ioana.temaandroid.classes.TransactionCategory;
import com.ioana.temaandroid.database.AppDatabase;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class CategoryAddForm extends AppCompatActivity {

    public static final String ACTIVITY_MODE = "ACTIVITY_MODE";
    public static final String CATEGORY_DATA = "CATEGORY_DATA";
    private Spinner spinner;
    private TransactionCategorySpinnerAdapter adapter;
    private List<RowItem> icons = new ArrayList<RowItem>();
    private List<String> iconNames = new ArrayList<>();
    private EditText categoryName;
    private RadioGroup categoryOptions;
    private int selectedType;
    private Button addCategoryButton;
    private AppDatabase mDb;
    private String activityType;
    private TransactionCategory editingCategory;
    private Intent intent;
    private Button deleteCategoryButton;
    private  TransactionCategoryService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_add_form);
        intent = getIntent();
        activityType= intent.getStringExtra(ACTIVITY_MODE);
        editingCategory = intent.getParcelableExtra(CATEGORY_DATA);
        mDb = AppDatabase.getInstance(getApplicationContext());
        service = new TransactionCategoryService(getApplicationContext());
        setupIconSpinner();
        categoryName = findViewById(R.id.categoryNameInput);
        categoryOptions = findViewById(R.id.categoryTypeOptions);
        selectedType=categoryOptions.getCheckedRadioButtonId();
        addCategoryButton = findViewById(R.id.addCategory);

        addCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final TransactionCategory category = getTransactionCategoryFromView();
                if(activityType.equals("ADD")){
                    insertNewCategory(category);
                }
                else{
                    category.setCategoryId(editingCategory.getCategoryId());
                    updateCategory(category);

                }
                setResult(RESULT_OK);
                finish();

            }
        });

        deleteCategoryButton = findViewById(R.id.deleteCategoryButton);

        if(activityType.equals("EDIT")  && editingCategory!=null){
            setFieldsForEdit();
            deleteCategoryButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final TransactionCategory transactionCategory = editingCategory;
                    deleteCategory(transactionCategory);
                    setResult(RESULT_OK);
                    finish();

                }
            });
        }
        else{
            deleteCategoryButton.setEnabled(false);
            deleteCategoryButton.setBackgroundColor(Color.parseColor("#808080"));
        }

    }

    private Callback<Integer> deleteInDbCallback(){
        return new Callback<Integer>(){
            @Override
            public void runResultOnUiThread(Integer result) {
                if(result != 1){
                    Toast.makeText(CategoryAddForm.this, "The category is used in transactions!", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(CategoryAddForm.this, "The category was deleted!", Toast.LENGTH_LONG).show();
                }
            }
        };
    }

    public void deleteCategory(TransactionCategory category){
        service.deleteCategory(deleteInDbCallback(), category);
    }


    public void updateCategory(TransactionCategory category){
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.transactionCategoryDao().updateCategory(category);
            }
        });
    }

    private void setFieldsForEdit() {
        categoryName.setText(editingCategory.getCategoryName());
        int id =editingCategory.getCategoryType()==0?R.id.categoryExpenseOption:R.id.categoryIncomeOption;
        categoryOptions.check(id);
        RowItem item = icons.stream().filter(r -> r.getIconRowId()==editingCategory.getImageId(CategoryAddForm.this, editingCategory.getIconName())).findFirst().orElse(null);
        int pos = adapter.getPositionByName(item);
        spinner.setSelection(pos);

        addCategoryButton.setText(R.string.update_label);

    }

    public void insertNewCategory(TransactionCategory category){
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.transactionCategoryDao().insertCategory(category);
            }
        });
    }


    private TransactionCategory getTransactionCategoryFromView() {
        String name = categoryName.getText().toString();
        int type = categoryOptions.getCheckedRadioButtonId()==R.id.categoryExpenseOption?0:1;
        int pos = spinner.getSelectedItemPosition();
        return new TransactionCategory( type,name, iconNames.get(pos));
    }

    public void setupIconList(){
        for (Field field: com.ioana.temaandroid.R.drawable.class.getFields()) {
            if(field.getName().startsWith("caticon_")){
                icons.add(new RowItem("",
                        CategoryAddForm.this.getResources().getIdentifier(field.getName(), "drawable", CategoryAddForm.this.getOpPackageName()),
                        0) );
                iconNames.add(field.getName());
            }
        }
    }



    public void setupIconSpinner() {
        setupIconList();
        spinner =  findViewById(R.id.iconSpinner);
        adapter = new TransactionCategorySpinnerAdapter(this, R.id.categoryIconSpinner, R.id.categoryTextSpinner, icons );
        spinner.setAdapter(adapter);

    }
}