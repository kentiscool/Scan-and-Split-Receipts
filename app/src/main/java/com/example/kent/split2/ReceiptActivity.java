package com.example.kent.split2;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.kent.split2.RecyclerViewFriends.FriendObject;
import com.example.kent.split2.RecyclerViewReceipt.RCAdapterReceipt;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ReceiptActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView mTotalTextView;
    private String userId;
    private String receiptId;
    FloatingActionButton mFab;
    private FriendObject currentUser;
    double total = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_receipt3);

        receiptId = this.getIntent().getStringExtra("receiptId");
        userId = this.getIntent().getStringExtra("userId");
        currentUser = new FriendObject(FirebaseAuth.getInstance().getCurrentUser().getEmail(), FirebaseAuth.getInstance().getCurrentUser().getUid());

        mTotalTextView = findViewById(R.id.totalTextView);
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getApplication());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new RCAdapterReceipt(getDataSet(),getApplication(),receiptId);
        mRecyclerView.setAdapter(mAdapter);

        mFab = findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

    }

    private ArrayList<ProductDatabaseItem> results = new ArrayList<>();
    private ArrayList<ProductDatabaseItem> getDataSet() {
        results.clear();
        listenForData();
        return results;
    }

    private void listenForData() {
        final DatabaseReference receiptDb = FirebaseDatabase.getInstance().getReference().child("receipt").child(receiptId).child("products");
        receiptDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                total = 0;
                results.clear();
                for (DataSnapshot productSnapshot: dataSnapshot.getChildren()) {
                    String productName = productSnapshot.child("productName").getValue().toString();
                    double price = 0;
                    String productUid = productSnapshot.getRef().getKey();
                    if(productName != null){
                        price = Double.parseDouble(productSnapshot.child("productPrice").getValue().toString());
                        ProductDatabaseItem obj = new ProductDatabaseItem(receiptId,productUid, productName, price);
                        if (!results.contains(obj)) {
                            results.add(obj);
                            final ProductDatabaseItem current = obj;
                            final DatabaseReference userListDB = productSnapshot.getRef().child("userList");

                            userListDB.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    total = 0;
                                    boolean found = false;
                                    current.getUsers().clear();
                                    for (DataSnapshot userSnapShot : dataSnapshot.getChildren()) {
                                        if (userSnapShot.getValue() != null) {
                                            String userEmail = userSnapShot.getValue().toString();
                                            String userUid = userSnapShot.getKey();
                                            FriendObject user = new FriendObject(userEmail, userUid);
                                            if (userEmail.equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())) {
                                                found = true;
                                            }
                                            if (!current.getUsers().contains(user)) {
                                                current.getUsers().add(user);
                                            }
                                        }
                                    }
                                    mAdapter.notifyDataSetChanged();

                                    for (int i = 0; i < results.size(); i ++) {
                                        ProductDatabaseItem currentProduct = results.get(i);
                                        System.out.println("totes : " + currentProduct.getUsers().size());
                                        for (int a = 0; a < currentProduct.getUsers().size(); a++) {
                                            if (currentProduct.getUsers().get(a).getEmail().equals(currentUser.getEmail())) {
                                                total += currentProduct.getProductPrice()/(currentProduct.getUsers().size());
                                            }
                                        }

                                    }
                                    mTotalTextView.setText(Double.toString(total));
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {}
                            });
                        }
                    }
                }
                mAdapter.notifyDataSetChanged();

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });

    }
}

