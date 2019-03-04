package com.example.kent.split2;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kent.split2.RecyclerViewRequests.RCAdapterRequests;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RequestsFragment extends Fragment{
    String Uid;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public static RequestsFragment newInstance(){
        RequestsFragment fragment = new RequestsFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_requests, container, false);
        Uid = FirebaseAuth.getInstance().getUid();
        mRecyclerView = view.findViewById(R.id.recyclerView);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this.getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new RCAdapterRequests(getDataSet(),this.getActivity());
        mRecyclerView.setAdapter(mAdapter);
        return view;
    }

    private ArrayList<ReceiptDatabaseItem> results = new ArrayList<>();
    private ArrayList<ReceiptDatabaseItem> getDataSet() {
        results.clear();
        listenForData();
        return results;
    }

    private void listenForData() {
        DatabaseReference receiptDb = FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("receipts");
        receiptDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                results.clear();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    String name = "";
                    String uid = postSnapshot.child("uid").getValue().toString();
                    if(uid != null){
                        name = postSnapshot.child("receiptName").getValue().toString();
                    }
                    ReceiptDatabaseItem obj = new ReceiptDatabaseItem(name,uid);
                    if (!results.contains(obj)) {
                        results.add(obj);
                        mAdapter.notifyDataSetChanged();
                    }
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}
