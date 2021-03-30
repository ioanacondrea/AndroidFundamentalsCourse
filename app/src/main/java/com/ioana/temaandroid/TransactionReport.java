package com.ioana.temaandroid;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ioana.temaandroid.classes.TransactionLog;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TransactionReport#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TransactionReport extends Fragment {

    public static final String INCOME_VALUE = "IncomeValue";
    public static final String EXPENSE_VALUE = "ExpenseValue";
    public static final String SPENDING_PERCENTAGE_VALUE = "SpendingPercentageValue";
    public static String TRANSACTION_REPORT_KEY="transaction_report_key";
    private Double balanceValue, incomeValue, expenseValue, spendingPercentageValue;
    private TextView balanceValueTV, incomeValueTV, expenseValueTV, spendingPercentageValueTV;
    private DatabaseReference firebaseDb;
    public TransactionReport() {
        // Required empty public constructor
    }
    public static TransactionReport newInstance(Double incomeValue, Double expenseValue, Double spendingPercentageValue) {
        TransactionReport fragment = new TransactionReport();
        Bundle bundle = new Bundle();
        bundle.putDouble(INCOME_VALUE, incomeValue);
        bundle.putDouble(EXPENSE_VALUE, expenseValue);
        bundle.putDouble(SPENDING_PERCENTAGE_VALUE, spendingPercentageValue);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tansactions_balance, container, false);
        Bundle bundle = this.getArguments();

        MobileAds.initialize(getContext(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        AdView mAdView = view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
        mAdView.loadAd(adRequest);

        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        });

        incomeValue = bundle.getDouble(INCOME_VALUE);
        expenseValue = bundle.getDouble(EXPENSE_VALUE);
        spendingPercentageValue = bundle.getDouble(SPENDING_PERCENTAGE_VALUE);
        balanceValue = incomeValue-expenseValue;
        firebaseDb = FirebaseDatabase.getInstance().getReference("ReportLogs");
        initComponents(view);
        return view;
    }

    private void initComponents(View view) {

        incomeValueTV = view.findViewById(R.id.incomeValue);
        expenseValueTV = view.findViewById(R.id.expenseValue);
        spendingPercentageValueTV = view.findViewById(R.id.spendingPercentageValue);
        balanceValueTV = view.findViewById(R.id.balanceValue);


        incomeValueTV.setText(incomeValue.toString());
        expenseValueTV.setText(expenseValue.toString());
        if(incomeValue==0){
            spendingPercentageValueTV.setText("N/A");
        }else{
            spendingPercentageValueTV.setText(spendingPercentageValue.toString()+"%");
        }
        if(balanceValue< 0 ){
            balanceValueTV.setTextColor(getResources().getColor(R.color.warning_red));
        }else {
            balanceValueTV.setTextColor(getResources().getColor(R.color.aqua_green));
        }
        balanceValueTV.setText(balanceValue.toString());

        String id = firebaseDb.push().getKey();
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yy HH:mm");
        TransactionLog log = new TransactionLog(id,format.format( new Date()),Double.toString(expenseValue), Double.toString(incomeValue), Double.toString(spendingPercentageValue) , Double.toString(incomeValue-expenseValue));
        firebaseDb.child(id).setValue(log);
    }

}