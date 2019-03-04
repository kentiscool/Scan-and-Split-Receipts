package com.example.kent.split2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.kent.split2.RecyclerViewFriends.FriendObject;
import com.example.kent.split2.RecyclerViewRecipient.RCAdapterRecipient;
import com.example.kent.split2.RecyclerViewRecipient.RecipientObject;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.sql.Array;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChooseReceiverActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    Receipt mReceipt;
    DatabaseReference receiptDatabase;

    String Uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_reciever);
        List<Product> listOfProducts = new ArrayList<>();
        ArrayList<String> data = this.getIntent().getStringArrayListExtra("data");
        for (int i =0; i < data.size(); i = i+2) {
            Product p =new Product(data.get(i),Double.valueOf(data.get(i+1)));
            listOfProducts.add(p);
        }

        mReceipt = new Receipt(listOfProducts);
        mReceipt.setTotal(this.getIntent().getExtras().getDouble("total"));

        FriendObject user = new FriendObject(FirebaseAuth.getInstance().getCurrentUser().getEmail(), FirebaseAuth.getInstance().getCurrentUser().getUid());
        ArrayList<Integer> selected = this.getIntent().getExtras().getIntegerArrayList("data2");
        for (int i = 0; i < selected.size(); i++) {
            mReceipt.getItemsBoughtAfterTaxes().get(selected.get(i)).addUser(user);
            System.out.println("index: " + mReceipt.getItemsBoughtAfterTaxes().get(selected.get(i)).getName());
        }

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getApplication());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new RCAdapterRecipient(getDataSet(),getApplication());
        mRecyclerView.setAdapter(mAdapter);

        FloatingActionButton mFab = findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendData();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }


    private ArrayList<RecipientObject> results = new ArrayList<>();
    private ArrayList<RecipientObject> getDataSet() {
        listenForData();
        return results;
    }

    private void sendData() {
        System.out.println("size2: " + mReceipt.getItemsBoughtAfterTaxes().size());
        receiptDatabase = FirebaseDatabase.getInstance().getReference().child("receipt").push();
        ReceiptDatabaseItem mReceiptDataBase = new ReceiptDatabaseItem(this.getIntent().getExtras().getString("title"), receiptDatabase.getKey());
        System.out.println("title2: " + this.getIntent().getExtras().getString("title"));
        receiptDatabase.setValue(mReceiptDataBase);

        for(int i = 0; i< results.size(); i++) {
            if (results.get(i).getRecieve()) {
                receiptDatabase.child("users").push().setValue(results.get(i));
            }
        }

        for (int i = 0 ;i < mReceipt.getItemsBoughtAfterTaxes().size(); i++) {

            DatabaseReference productDatabase = receiptDatabase.child("products").push();
            String productId = productDatabase.getKey();
            ProductDatabaseItem p = new ProductDatabaseItem(receiptDatabase.getKey(),productId, mReceipt.getItemsBoughtAfterTaxes().get(i).getName(), mReceipt.getItemsBoughtAfterTaxes().get(i).getPrice());
            Map<String,Object> productInfo = p.toMap();
            productDatabase.setValue(productInfo);

            if (!mReceipt.getItemsBoughtAfterTaxes().get(i).getUsers().isEmpty()) {
                for (int a = 0; a < mReceipt.getItemsBoughtAfterTaxes().get(i).getUsers().size(); a++){
                    String email = mReceipt.getItemsBoughtAfterTaxes().get(i).getUsers().get(a).getEmail();
                    String userUid = mReceipt.getItemsBoughtAfterTaxes().get(i).getUsers().get(a).getUid();
                    productDatabase.child("userList").child(userUid).setValue(email);
                }
            }
        }

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        ReceiptDatabaseItem receiptDatabaseItem = new ReceiptDatabaseItem(this.getIntent().getExtras().getString("title"), receiptDatabase.getKey());
        FirebaseDatabase.getInstance().getReference().child("users").child(userId).child("receipts").push().setValue(receiptDatabaseItem);
    }

    private void listenForData() {
        for(int i = 0; i < UserInformation.listFollowing.size();i++){
            DatabaseReference usersDb = FirebaseDatabase.getInstance().getReference().child("users").child(UserInformation.listFollowing.get(i));
            usersDb.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String email = "";
                    String uid = dataSnapshot.getRef().getKey();
                    if(dataSnapshot.child("email").getValue() != null){
                        email = dataSnapshot.child("email").getValue().toString();
                    }
                    RecipientObject obj = new RecipientObject(email, uid, false);
                    if(!results.contains(obj)){
                        results.add(obj);
                        mAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }


}