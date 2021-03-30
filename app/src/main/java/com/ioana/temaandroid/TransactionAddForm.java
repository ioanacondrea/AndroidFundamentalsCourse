package com.ioana.temaandroid;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.ioana.temaandroid.adapters.RowItem;
import com.ioana.temaandroid.adapters.TransactionCategorySpinnerAdapter;
import com.ioana.temaandroid.async.AppExecutors;
import com.ioana.temaandroid.classes.DateConverter;
import com.ioana.temaandroid.classes.Transaction;
import com.ioana.temaandroid.classes.TransactionCategory;
import com.ioana.temaandroid.classes.TransactionType;
import com.ioana.temaandroid.classes.TransactionWithCategory;
import com.ioana.temaandroid.database.AppDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TransactionAddForm extends AppCompatActivity {

    public static final int ADD_CATEGORY_REQUEST_CODE = 205;
    public static final String ACTIVITY_MODE = "ACTIVITY_MODE";
    public static final String ACTIVITY_TYPE = "ACTIVITY_TYPE";
    public static final String TRANSACTION_DATA = "TRANSACTION_DATA";

    DatePickerDialog picker;
    EditText eText;

    private EditText transactionNameInput;
    private EditText transactionValueInput;
    private EditText transactionDateInput;
    private RadioGroup transactionTypeOptions;
    private RadioButton transactionSelectedOption;
    private int transactionTypeSelected;
    private Button addTransactionButton;
    private final DateConverter dateConverter = new DateConverter();
    private Intent intent;
    private String activityType;
    private TransactionWithCategory editingTransaction;


    private Spinner spinner;
    private TransactionCategorySpinnerAdapter adapterExpenses;
    private TransactionCategorySpinnerAdapter adapterIncome;
    private TransactionCategorySpinnerAdapter currentAdapter;
    public List<TransactionCategory> transactionExpenseCategory = new ArrayList<TransactionCategory>();
    public List<TransactionCategory> transactionIncomeCategory = new ArrayList<TransactionCategory>();

    private AppDatabase mDb;
    private Button addCategoryButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction_form);
        mDb = AppDatabase.getInstance(getApplicationContext());
        initComponents();
        intent = getIntent();
        activityType = intent.getStringExtra(ACTIVITY_TYPE);
        editingTransaction = intent.getParcelableExtra(TRANSACTION_DATA);
        spinner = findViewById(R.id.transactionCategorySpinner);
        initTransactionCategoryLists();

        eText = (EditText) findViewById(R.id.transactionDateInput);
        eText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(TransactionAddForm.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                eText.setText(String.format("%d/%d/%d", dayOfMonth, monthOfYear + 1, year));
                            }
                        }, year, month, day);
                picker.show();
            }
        });

       transactionTypeOptions.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                transactionSelectedOption = findViewById(transactionTypeOptions.getCheckedRadioButtonId());
                Log.i("Id button:", String.valueOf(transactionTypeSelected));
                if(transactionSelectedOption.getText().toString().equals("Expense")){
                    currentAdapter = adapterExpenses;
                }else{
                    currentAdapter = adapterIncome;
                }
                spinner.setAdapter(currentAdapter);
            }
        });

        if(activityType.equals("EDIT")){
            setFieldsForEdit();
        }

    }

    public void setSpinner(){
        currentAdapter = adapterExpenses;
        spinner.setAdapter(currentAdapter);
    }

    public void setFieldsForEdit(){

        spinner = findViewById(R.id.transactionCategorySpinner);
        transactionNameInput.setText(editingTransaction.getTransaction().getTransactionName());
        transactionValueInput.setText(Double.toString(editingTransaction.getTransaction().getTransactionValue()));
        transactionDateInput.setText( dateConverter.toString(editingTransaction.getTransaction().getTransactionDate()));
        addTransactionButton.setText(R.string.update_label);
    }


 public void loadExpenseCategoriesInSpinner() {
     int[] iconIdsExpense = returnIconsIdArray(transactionExpenseCategory);
     List<RowItem> rowListExpense = new ArrayList<>();
     for (int i = 0; i < transactionExpenseCategory.size(); i++) {
         rowListExpense.add(new RowItem(transactionExpenseCategory.get(i).getCategoryName(), iconIdsExpense[i], transactionExpenseCategory.get(i).getCategoryId()));
     }

     adapterExpenses = new TransactionCategorySpinnerAdapter(TransactionAddForm.this, R.id.categoryIconSpinner, R.id.categoryTextSpinner, rowListExpense);
     if (activityType.equals("EDIT")) {
         if (editingTransaction.getTransaction().getTransactionType().name().equals(getString(R.string.expense_label))) {
             RadioButton btn = findViewById(R.id.expenseOption);
             btn.setChecked(true);
             currentAdapter = adapterExpenses;
             spinner.setAdapter(currentAdapter);
             String category = editingTransaction.getCategory().getCategoryName();
             for (int i = 0; i < currentAdapter.getCount(); i++) {
                 if (currentAdapter.getRowItem(i).getCategoryRowName().equals(category)) {
                     spinner.setSelection(i);
                 }
             }
         }
     }
     else{
         setSpinner();
     }
 }


    public class LoadExpenseCategories extends AsyncTask<Void, Void, List<TransactionCategory>>{

        @Override
        protected List<TransactionCategory> doInBackground(Void... voids) {
            final List<TransactionCategory> list = mDb.transactionCategoryDao().loadAllCategoriesByType(0);
            return list;
        }

        @Override
        protected void onPostExecute(List<TransactionCategory> transactionCategories) {
            transactionExpenseCategory.clear();
            transactionExpenseCategory.addAll(transactionCategories);
            loadExpenseCategoriesInSpinner();
        }
    }

    public class LoadIncomeCategories extends AsyncTask<Void, Void, List<TransactionCategory>>{

        @Override
        protected List<TransactionCategory> doInBackground(Void... voids) {
            final List<TransactionCategory> list = mDb.transactionCategoryDao().loadAllCategoriesByType(1);
            return list;
        }

        @Override
        protected void onPostExecute(List<TransactionCategory> transactionCategories) {
            transactionIncomeCategory.clear();
            transactionIncomeCategory.addAll(transactionCategories);
            loadIncomeCategoriesSpinner();
        }
    }

    private void loadIncomeCategoriesSpinner() {
        int[] iconIdsIncome = returnIconsIdArray(transactionIncomeCategory);
        List<RowItem> rowListIncome = new ArrayList<>();
        for(int i=0;i<transactionIncomeCategory.size();i++){
            rowListIncome.add(new RowItem(transactionIncomeCategory.get(i).getCategoryName(), iconIdsIncome[i], transactionIncomeCategory.get(i).getCategoryId()));
        }

        adapterIncome = new TransactionCategorySpinnerAdapter(TransactionAddForm.this, R.id.categoryIconSpinner, R.id.categoryTextSpinner, rowListIncome);
        if(activityType.equals("EDIT")){
            if(editingTransaction.getTransaction().getTransactionType().name().equals(getString(R.string.income))){
                RadioButton btn = findViewById(R.id.incomeOption);
                btn.setChecked(true);
                currentAdapter=adapterIncome;
                spinner.setAdapter(currentAdapter);
                String category = editingTransaction.getCategory().getCategoryName();
                for(int i=0; i < currentAdapter.getCount(); i++) {
                    if(category.equals(currentAdapter.getItem(i).getCategoryRowName())){
                        spinner.setSelection(i);
                    }
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==ADD_CATEGORY_REQUEST_CODE && resultCode==RESULT_OK){
            initTransactionCategoryLists();
        }
    }

    public void initTransactionCategoryLists() {
        new LoadExpenseCategories().execute();
        new LoadIncomeCategories().execute();
    }

    public int[] returnIconsIdArray(List<TransactionCategory> transactionCategories) {
        int[] icons = new int[transactionCategories.size()];

        for (int i = 0; i < transactionCategories.size(); i++) {
            icons[i] = transactionCategories.get(i).getImageId(this, transactionCategories.get(i).getIconName());
        }
        return icons;
    }


    private void initComponents() {

        transactionNameInput = findViewById(R.id.transactionNameInput);
        transactionValueInput = findViewById(R.id.transactionValueInput);
        transactionDateInput = findViewById(R.id.transactionDateInput);
        transactionSelectedOption = findViewById(R.id.expenseOption);
        transactionTypeOptions = findViewById(R.id.transactionTypeOptions);
        transactionTypeSelected = transactionTypeOptions.getCheckedRadioButtonId();
        addTransactionButton = findViewById(R.id.addTransactionButton);

        addCategoryButton = findViewById(R.id.addCategory);

        addCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TransactionAddForm.this, CategoryAddForm.class);
                intent.putExtra(ACTIVITY_MODE, "ADD");
                startActivityForResult(intent, ADD_CATEGORY_REQUEST_CODE);
            }
        });


        addTransactionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validareInput()) {

                    if(activityType.equals("EDIT")){
                        Transaction transactionEdited= getCardFromView();
                        transactionEdited.setTransactionID(editingTransaction.getTransaction().getTransactionID());
                        AppExecutors.getInstance().diskIO().execute(new Runnable() {
                            @Override
                            public void run() {
                                mDb.transactionDao().updateTransaction(transactionEdited);
                                setResult(RESULT_OK, intent);
                                finish();
                            }
                        });


                    }else{

                        Transaction transactionToAdd = getCardFromView();
                        AppExecutors.getInstance().diskIO().execute(new Runnable() {
                            @Override
                            public void run() {
                                mDb.transactionDao().insertTransaction(transactionToAdd);
                                setResult(RESULT_OK, intent);
                                finish();
                            }
                        });
                    }


                }
            }
        });
    }


    private boolean validareInput() {

        if (transactionNameInput.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(),
                   R.string.valid_name,
                    Toast.LENGTH_LONG).show();
            return false;
        }
        try {
            Double.parseDouble(transactionValueInput.getText().toString());
        } catch (NumberFormatException e) {
            transactionValueInput.setError(getString(R.string.value_invalid));
            return false;
        }

        if (transactionDateInput.getText() == null || dateConverter.fromString(transactionDateInput.getText().toString()) == null) {
            Toast.makeText(getApplicationContext(),
                   R.string.invadid_value,
                    Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    public String setOption() {
        transactionTypeOptions = findViewById(R.id.transactionTypeOptions);
        transactionSelectedOption = findViewById(transactionTypeOptions.getCheckedRadioButtonId());
        return transactionSelectedOption.getText().toString();
    }


    public Transaction getCardFromView() {
        Transaction transaction = new Transaction();
        String transactionName = transactionNameInput.getText().toString();
        Double transactionValue = Double.parseDouble(transactionValueInput.getText().toString());
        Date date = dateConverter.fromString(transactionDateInput.getText().toString());
        TransactionType type = TransactionType.valueOf(setOption());
        int pos = spinner.getSelectedItemPosition();
        transaction.setTransactionName(transactionName);
        transaction.setTransactionValue(transactionValue);
        transaction.setTransactionType(type);
        transaction.setTransactionCategoryID(currentAdapter.getRowItem(pos).getCategoryIdFromDB());
        transaction.setTransactionDate(date);
        //transaction = new Transaction(transactionName, transactionValue, type, this.categoryId, date);
        return transaction;
    }

}