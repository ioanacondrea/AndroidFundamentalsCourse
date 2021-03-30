package com.ioana.temaandroid;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ioana.temaandroid.adapters.TransactionLogAdapter;
import com.ioana.temaandroid.classes.TransactionLog;

import java.util.ArrayList;
import java.util.List;


public class TransactionLogFirebase extends Fragment {

    private DatabaseReference firebaseDb;
    private List<TransactionLog> logs;

    public static TransactionLogFirebase newInstance() {
        TransactionLogFirebase fragment = new TransactionLogFirebase();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_transaction_log_firebase, container, false);
        firebaseDb = FirebaseDatabase.getInstance().getReference("ReportLogs");
        logs = new ArrayList<>();
        ListView listView = view.findViewById(R.id.logListView);

        firebaseDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                logs.clear();
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    TransactionLog log =dataSnapshot.getValue(TransactionLog.class);
                    logs.add(log);
                }

                if(getActivity()!=null){
                    TransactionLogAdapter adapter = new TransactionLogAdapter(getActivity(),logs );
                    listView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return view;
    }
}